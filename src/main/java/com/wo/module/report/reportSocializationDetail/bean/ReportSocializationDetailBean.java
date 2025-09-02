package com.wo.module.report.reportSocializationDetail.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;
import org.primefaces.PrimeFaces;

import com.wo.module.common.bean.CommonReportRunnableBean;
import com.wo.module.common.constant.CommonConstants;
import com.wo.module.common.paging.DefaultSearchObject;
import com.wo.module.common.task.TaskExecutorBean;
import com.wo.module.common.util.DateUtil;
import com.wo.module.common.util.FileUtil;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.documentType.constant.DocumentTypeConstants;
import com.wo.module.documentType.model.DocumentType;
import com.wo.module.documentType.service.DocumentTypeService;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.lov.bean.RunnableFacesUtil;
import com.wo.module.parameter.model.ParameterHeader;
import com.wo.module.report.reportGen.constant.ReportGenConstant;
import com.wo.module.report.reportGen.model.ReportGen;
import com.wo.module.report.reportGen.model.ReportGenDetailTableModel;
import com.wo.module.report.reportGen.service.ReportGenService;
import com.wo.module.report.reportSocializationDetail.constant.ReportSocializationDetailConstant;
import com.wo.module.report.reportSocializationDetail.service.ReportSocializationDetailService;
import com.wo.module.report.reportSocializationDetail.task.ReportSocializationDetailTask;
import com.wo.module.report.reportSocializationRekap.service.ReportSocializationRekapService;
import com.wo.module.user.model.User;

public class ReportSocializationDetailBean extends CommonReportRunnableBean implements Serializable, ReportGenConstant, ReportSocializationDetailConstant {

	private static final long serialVersionUID = 1377743521986834585L;

	static Logger logger = Logger.getLogger(ReportSocializationDetailBean.class);

	private Date searchDateFrom;
	private Date searchDateTo;
	private Date searchTargetDateFrom;
	private Date searchTargetDateTo;
	private String searchProvType;
	private String searchDocType;
	
	private List<SelectItem> provTypes;
	private List<SelectItem> docTypes;

	private DocumentTypeService documentTypeService;
	private ReportGenService reportGenService;
	private TaskExecutorBean taskExecutorBean;
	private ReportSocializationDetailService reportSocializationDetailService;
	private ReportSocializationRekapService reportSocializationRekapService;
	
	private ReportGenDetailTableModel tableModel;
	private ReportGen[] selectedReportGen;
	private FileUtil fileUtil;

	@PostConstruct
	public void init() {
		super.init();
		constructSelectComponent();
		tableModel = new ReportGenDetailTableModel(reportGenService, paging);
		search();
		fileUtil = FileUtil.getInstance();
	}

	private void constructSelectComponent() {
		setupProvTypes();
		setupDocTypes();
	}

	private void setupProvTypes() {
		provTypes = new ArrayList<SelectItem>();
		try {
			provTypes = parameterDetailService.getListLabelValue(ParameterHeader.PARAM_HEAD_CODE_JENIS_KETENTUAN,
					false, true, facesUtil.retrieveDefaultLocale());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setupDocTypes() {
		docTypes = new ArrayList<SelectItem>();
		try {
			List<DocumentType> pd = documentTypeService.searchData(
					Arrays.asList(new DefaultSearchObject(DocumentTypeConstants.WHERE_JENIS_KETENTUAN_CODE, null)), 0,
					Integer.MAX_VALUE, null, null);
			for (int i = 0; i < pd.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((DocumentType) pd.get(i)).getDocumentType());
				si.setValue(((DocumentType) pd.get(i)).getDocumentTypeId());
				docTypes.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void onChangeProvType() {
		selectDocType();
	}
	
	public void selectDocType() {
		docTypes = new ArrayList<SelectItem>();
		try {
			List<DocumentType> pd = documentTypeService.searchData(
					Arrays.asList(new DefaultSearchObject(DocumentTypeConstants.WHERE_JENIS_KETENTUAN_CODE, searchProvType)), 0,
					Integer.MAX_VALUE, null, null);
			for (int i = 0; i < pd.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((DocumentType) pd.get(i)).getDocumentType());
				si.setValue(((DocumentType) pd.get(i)).getDocumentTypeId());
				docTypes.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	public void search() {
		tableModel.setSearchCriteria(Arrays.asList(new DefaultSearchObject(CommonConstants.SEARCH_FILTER_BY_REPORT_CODE, REPORT_SOCIALIZATION_DETAIL)));
	}

	public void reset(ActionEvent actionEvent) {
		searchDateFrom = null;
		searchDateTo = null;
		searchProvType = null;
		searchDocType = null;
		searchTargetDateFrom= null;
		searchTargetDateTo = null;
		search();
		
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	public void delete()  {
		if(selectedReportGen == null)
			facesUtil.addErrMessage(facesUtil.retrieveMessage("validateDeleteMinOneData"));
		
		try {
			reportGenService.bulkDelete(selectedReportGen, facesUtil.retrieveUserLogin(), parameterDetailService, fileUtil);
			facesUtil.addSuccessMsg(facesUtil.retrieveMessage("deleteSuccess"));
		} catch (Exception e) {
			e.printStackTrace();
			facesUtil.addErrMessage(facesUtil.retrieveMessage("errDeleteBecause") + e.getMessage());
		}
	}
	
	public List<DefaultSearchObject> buildSearchCriteriaReportGen() {
		return Arrays.asList(new DefaultSearchObject(WHERE_CREATION_DATE_FROM, DateUtil.dateToStringYYYYMMDD(searchDateFrom, false)),
				new DefaultSearchObject(WHERE_CREATION_DATE_TO, DateUtil.dateToStringYYYYMMDD(searchDateTo, false)),
				new DefaultSearchObject(WHERE_PROV_TYPE, searchProvType),
				new DefaultSearchObject(WHERE_DOC_TYPE, searchDocType),
				new DefaultSearchObject(WHERE_TARGET_DATE_FROM, DateUtil.dateToStringYYYYMMDD(searchTargetDateFrom, false)),
				new DefaultSearchObject(WHERE_TARGET_DATE_TO, DateUtil.dateToStringYYYYMMDD(searchTargetDateTo, false)));
	}
	
	public void generateExcelInBackgroundJob() {
		
		if(searchDateFrom == null && searchDateTo == null && searchTargetDateFrom == null && searchTargetDateTo==null) {
			facesUtil.addErrMessage(
					facesUtil.retrieveMessage("formReportSocializationDetailCreationDate") + " "
					+ facesUtil.retrieveMessage("or") + " "
					+ facesUtil.retrieveMessage("formReportSocializationDetailTargetDate") + " "
					+ facesUtil.retrieveMessage("validateRequired"));
			return;
		} 
//		else if((searchDateFrom != null || searchDateTo != null)){
//			if((searchDateFrom == null || searchDateTo == null)) {
//				facesUtil.addErrMessage(
//						facesUtil.retrieveMessage("formReportSocializationDetailCreationDate") + " "
//						+ facesUtil.retrieveMessage("from") + " "
//						+ facesUtil.retrieveMessage("and") + " "
//						+ facesUtil.retrieveMessage("formReportSocializationDetailCreationDate") + " "
//						+ facesUtil.retrieveMessage("to") + " "
//						+ facesUtil.retrieveMessage("validateRequired"));
//				return;
//			}
//		} else if(searchTargetDateFrom != null || searchTargetDateTo != null){
//			if((searchTargetDateFrom == null || searchTargetDateTo == null)) {
//				facesUtil.addErrMessage(
//						facesUtil.retrieveMessage("formReportSocializationDetailTargetDate") + " "
//						+ facesUtil.retrieveMessage("from") + " "
//						+ facesUtil.retrieveMessage("and") + " "
//						+ facesUtil.retrieveMessage("formReportSocializationDetailTargetDate") + " "
//						+ facesUtil.retrieveMessage("to") + " "
//						+ facesUtil.retrieveMessage("validateRequired"));
//				return;
//			}
//		}
		
		String docName = "";
		
		ReportGen newReportGen = saveReportGenHistoryAsInProgress();
		
		Long newReportGenId = newReportGen.getReportGenId();
		facesUtil.addSuccessMsg("Report Generated Id : [" + newReportGenId + "]");
		
		List<DefaultSearchObject> searchCriteria = buildSearchCriteriaReportGen();

		User user = facesUtil.getUserLogin();
		String userNikName = user.getNik() + " - " + user.getName();
		
		if (searchDocType != null && !searchDocType.equals("")) {
			Long docId = Long.parseLong(searchDocType);
			DocumentType getDocumentTypeName = documentTypeService.findById(docId);
			
			if (getDocumentTypeName != null) {
				docName = getDocumentTypeName.getDocumentType();
			}
		}
		
		ReportSocializationDetailTask task = new ReportSocializationDetailTask(
				newReportGenId, 
				reportGenService, 
				parameterDetailService, 
				runnableFacesUtil,
				reportSocializationDetailService,
				reportSocializationRekapService,
				searchCriteria,
				userNikName,
				docName
				);

		taskExecutorBean.submitTask(task);
	}
	
	private ReportGen saveReportGenHistoryAsInProgress() {
		try {
			Timestamp currentDate = new Timestamp(new Date().getTime());
			ReportGen newReportGen = new ReportGen();

			User user = facesUtil.getUserLogin();

			newReportGen.setReportGenNik(user.getNik());
			newReportGen.setReportGenEmployeeName(user.getName());
			newReportGen
					.setReportGenReportName(REPORT_SOCIALIZATION_DETAIL);
			newReportGen
					.setReportGenStatus(CommonConstants.REPORT_GEN_STATUS_ON_PROGRESS);
			newReportGen.setEnabledFlag(CommonConstants.ENABLED_FLAG_TRUE);

			newReportGen.setCreationDate(currentDate);
			newReportGen.setCreatedBy(user.getNik());
			reportGenService.save(newReportGen);
			return newReportGen;
		} catch (Exception e) {
			logger.error("Error while trying to update status on progress", e);
			return null;
		}
	}
	
	public void downloadFile(String fileId, String fileName, byte[] content) throws Exception {
		CallApiManager.downloadFile(fileId, fileName, content, parameterDetailService);
	}

	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Date getSearchDateFrom() {
		return searchDateFrom;
	}

	public void setSearchDateFrom(Date searchDateFrom) {
		this.searchDateFrom = searchDateFrom;
	}

	public Date getSearchDateTo() {
		return searchDateTo;
	}

	public void setSearchDateTo(Date searchDateTo) {
		this.searchDateTo = searchDateTo;
	}

	public List<SelectItem> getProvTypes() {
		return provTypes;
	}

	public void setProvTypes(List<SelectItem> provTypes) {
		this.provTypes = provTypes;
	}

	public List<SelectItem> getDocTypes() {
		return docTypes;
	}

	public void setDocTypes(List<SelectItem> docTypes) {
		this.docTypes = docTypes;
	}

	public DocumentTypeService getDocumentTypeService() {
		return documentTypeService;
	}

	public void setDocumentTypeService(DocumentTypeService documentTypeService) {
		this.documentTypeService = documentTypeService;
	}

	public String getSearchProvType() {
		return searchProvType;
	}

	public void setSearchProvType(String searchProvType) {
		this.searchProvType = searchProvType;
	}

	public String getSearchDocType() {
		return searchDocType;
	}

	public void setSearchDocType(String searchDocType) {
		this.searchDocType = searchDocType;
	}

	public ReportGenService getReportGenService() {
		return reportGenService;
	}

	public void setReportGenService(ReportGenService reportGenService) {
		this.reportGenService = reportGenService;
	}

	public TaskExecutorBean getTaskExecutorBean() {
		return taskExecutorBean;
	}

	public void setTaskExecutorBean(TaskExecutorBean taskExecutorBean) {
		this.taskExecutorBean = taskExecutorBean;
	}

	public ReportSocializationDetailService getReportSocializationDetailService() {
		return reportSocializationDetailService;
	}

	public void setReportSocializationDetailService(ReportSocializationDetailService reportSocializationDetailService) {
		this.reportSocializationDetailService = reportSocializationDetailService;
	}

	public RunnableFacesUtil getRunnableFacesUtil() {
		return runnableFacesUtil;
	}

	public void setRunnableFacesUtil(RunnableFacesUtil runnableFacesUtil) {
		this.runnableFacesUtil = runnableFacesUtil;
	}

	public ReportGen[] getSelectedReportGen() {
		return selectedReportGen;
	}

	public void setSelectedReportGen(ReportGen[] selectedReportGen) {
		this.selectedReportGen = selectedReportGen;
	}

	public ReportGenDetailTableModel getTableModel() {
		return tableModel;
	}

	public void setTableModel(ReportGenDetailTableModel tableModel) {
		this.tableModel = tableModel;
	}

	public FileUtil getFileUtil() {
		return fileUtil;
	}

	public void setFileUtil(FileUtil fileUtil) {
		this.fileUtil = fileUtil;
	}

	public ReportSocializationRekapService getReportSocializationRekapService() {
		return reportSocializationRekapService;
	}

	public void setReportSocializationRekapService(ReportSocializationRekapService reportSocializationRekapService) {
		this.reportSocializationRekapService = reportSocializationRekapService;
	}

	public Date getSearchTargetDateFrom() {
		return searchTargetDateFrom;
	}

	public void setSearchTargetDateFrom(Date searchTargetDateFrom) {
		this.searchTargetDateFrom = searchTargetDateFrom;
	}

	public Date getSearchTargetDateTo() {
		return searchTargetDateTo;
	}

	public void setSearchTargetDateTo(Date searchTargetDateTo) {
		this.searchTargetDateTo = searchTargetDateTo;
	}

}