package com.wo.module.report.reportSocializationRekap.bean;

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
import com.wo.module.lov.bean.RunnableFacesUtil;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.parameter.model.ParameterHeader;
import com.wo.module.report.reportGen.constant.ReportGenConstant;
import com.wo.module.report.reportGen.model.ReportGen;
import com.wo.module.report.reportGen.service.ReportGenService;
import com.wo.module.report.reportSocializationRekap.constant.ReportSocializationRekapConstants;
import com.wo.module.report.reportSocializationRekap.model.ReportSocializationRekapTableModel;
import com.wo.module.report.reportSocializationRekap.service.ReportSocializationRekapService;
import com.wo.module.report.reportSocializationRekap.task.ReportSocializationRekapTask;
import com.wo.module.user.model.User;

public class ReportSocializationRekapBean extends CommonReportRunnableBean implements Serializable, ReportGenConstant{

	private static final long serialVersionUID = -8242903382774448518L;

	static Logger logger = Logger.getLogger(ReportSocializationRekapBean.class);
	
	private Date searchCreationDateFrom;
	private Date searchCreationDateTo;
	private String searchProvType;
	private String searchDocType;
	
	private List<SelectItem> provTypes;
	private List<SelectItem> docTypes;
	
	private DocumentTypeService documentTypeService;
	private ReportGenService reportGenService;
	private TaskExecutorBean taskExecutorBean;
	private ReportSocializationRekapService reportSocializationRekapService;
	
	private ReportSocializationRekapTableModel tableModel;
	private ReportGen[] selectedReportGen;
	private FileUtil fileUtil;
	
	@PostConstruct
	public void init() {
		super.init();
		selectListProvTypes();
		selectListDocTypes();
		tableModel = new ReportSocializationRekapTableModel(reportGenService, paging);
		search();
		fileUtil = FileUtil.getInstance();
	}
	
	private void selectListProvTypes() {
		provTypes = new ArrayList<SelectItem>();
		try {
			provTypes = parameterDetailService.getListLabelValue(ParameterHeader.PARAM_HEAD_CODE_JENIS_KETENTUAN,
					false, true, facesUtil.retrieveDefaultLocale());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void selectListDocTypes() {
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
		tableModel.setSearchCriteria(Arrays.asList(new DefaultSearchObject(CommonConstants.SEARCH_FILTER_BY_REPORT_CODE, REPORT_SOCIALIZATION_REKAP)));
	}
	
	public void reset(ActionEvent e) {
		searchCreationDateFrom = null;
		searchCreationDateTo = null;
		searchDocType = null;
		searchProvType = null;
		
		search();
		
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	public List<DefaultSearchObject> buildSearchCriteriaReportGen(){
		return Arrays.asList(
				new DefaultSearchObject(ReportSocializationRekapConstants.SEARCH_DATE_FROM, DateUtil.dateToStringYYYYMMDD(searchCreationDateFrom, false) ),
				new DefaultSearchObject(ReportSocializationRekapConstants.SEARCH_DATE_TO, DateUtil.dateToStringYYYYMMDD(searchCreationDateTo, false)),
				new DefaultSearchObject(ReportSocializationRekapConstants.SEARCH_JENIS_PERATURAN, searchProvType),
				new DefaultSearchObject(ReportSocializationRekapConstants.SEARCH_KATEGORI_DOKUMEN, searchDocType));
	}
	
	public void generateExcelInBackgroundJob () {
		
		String docName = "";
		
		ReportGen newReportGen = saveReportGenHistoryAsInProgress();
		
		Long newReportGenId = newReportGen.getReportGenId();
		
		facesUtil.addSuccessMsg("Report Generate Id : [" + newReportGenId + "]");
		
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
		
		ReportSocializationRekapTask task = new ReportSocializationRekapTask(
				newReportGenId
				, reportGenService
				, parameterDetailService
				, runnableFacesUtil
				, reportSocializationRekapService
				, searchCriteria
				, userNikName
				, docName);
		
		taskExecutorBean.submitTask(task);
	}
	
	public ReportGen saveReportGenHistoryAsInProgress()	{
		try {
			Timestamp currentDate = new Timestamp(new Date().getTime());
			ReportGen newReportGen = new ReportGen();
			
			User user = facesUtil.getUserLogin();
			
			newReportGen.setReportGenNik(user.getNik());
			newReportGen.setReportGenEmployeeName(user.getName());
			newReportGen.setReportGenReportName(REPORT_SOCIALIZATION_REKAP);
			newReportGen.setReportGenStatus(CommonConstants.REPORT_GEN_STATUS_ON_PROGRESS);
			newReportGen.setEnabledFlag(CommonConstants.ENABLED_FLAG_TRUE);
			
			newReportGen.setCreationDate(currentDate);
			newReportGen.setCreatedBy(user.getNik());
			reportGenService.save(newReportGen);
			
			return newReportGen;
			
		} catch (Exception e) {
			logger.error("Error while trying to update status on progress ", e);
			return null;
		}
	}
	
	public void delete() {
		if (selectedReportGen == null) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("validateDeleteMinOneData"));
		}
		
		try {
			reportGenService.bulkDelete(selectedReportGen, facesUtil.retrieveUserLogin(), parameterDetailService, fileUtil);
			facesUtil.addSuccessMsg(facesUtil.retrieveMessage("deleteSuccess"));
		} catch (Exception e) {
			e.printStackTrace();
			facesUtil.addErrMessage(facesUtil.retrieveMessage("errDeleteBecause") + e.getMessage());
		}
	}
	
	public void downloadFile(String fileId, String fileName, byte[] content) throws Exception {
		CallApiManager.downloadFile(fileId, fileName, content, parameterDetailService);
	}
	
	public Date getSearchCreationDateFrom() {
		return searchCreationDateFrom;
	}

	public void setSearchCreationDateFrom(Date searchCreationDateFrom) {
		this.searchCreationDateFrom = searchCreationDateFrom;
	}

	public Date getSearchCreationDateTo() {
		return searchCreationDateTo;
	}

	public void setSearchCreationDateTo(Date searchCreationDateTo) {
		this.searchCreationDateTo = searchCreationDateTo;
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

	public ReportSocializationRekapService getReportSocializationRekapService() {
		return reportSocializationRekapService;
	}

	public void setReportSocializationRekapService(ReportSocializationRekapService reportSocializationRekapService) {
		this.reportSocializationRekapService = reportSocializationRekapService;
	}

	public FileUtil getFileUtil() {
		return fileUtil;
	}

	public void setFileUtil(FileUtil fileUtil) {
		this.fileUtil = fileUtil;
	}

	public void setTableModel(ReportSocializationRekapTableModel tableModel) {
		this.tableModel = tableModel;
	}

	public ReportSocializationRekapTableModel getTableModel() {
		return tableModel;
	}

	public RunnableFacesUtil getRunnableFacesUtil() {
		return runnableFacesUtil;
	}

	public void setRunnableFacesUtil(RunnableFacesUtil runnableFacesUtil) {
		this.runnableFacesUtil = runnableFacesUtil;
	}
	
	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

	public ReportGen[] getSelectedReportGen() {
		return selectedReportGen;
	}

	public void setSelectedReportGen(ReportGen[] selectedReportGen) {
		this.selectedReportGen = selectedReportGen;
	}
}