package com.wo.module.report.reportRmdRekap.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
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
import com.wo.module.parameter.model.ParameterHeader;
import com.wo.module.report.reportGen.constant.ReportGenConstant;
import com.wo.module.report.reportGen.model.ReportGen;
import com.wo.module.report.reportGen.service.ReportGenService;
import com.wo.module.report.reportRmdRekap.constant.ReportRmdRekapConstants;
import com.wo.module.report.reportRmdRekap.model.ReportRmdRekapTableModel;
import com.wo.module.report.reportRmdRekap.service.ReportRmdRekapService;
import com.wo.module.report.reportRmdRekap.task.ReportRmdRekapTask;
import com.wo.module.user.model.User;

public class ReportRmdRekapBean extends CommonReportRunnableBean 
	implements Serializable, ReportGenConstant, ReportRmdRekapConstants{

	private static final long serialVersionUID = -8270931364602598743L;

	static Logger logger = Logger.getLogger(ReportRmdRekapBean.class);
	
	private Date searchDateFrom;
	private Date searchDateTo;
	private String searchProvType;
	private String searchDocType;
	private String searchSenderType;
	
	private List<SelectItem> provTypes;
	private List<SelectItem> docTypes;
	private List<SelectItem> senderTypes;

	private DocumentTypeService documentTypeService;
	private ReportGenService reportGenService;
	private TaskExecutorBean taskExecutorBean;
	private ReportRmdRekapService reportRmdRekapService;
	
	private ReportRmdRekapTableModel tableModel;
	private ReportGen[] selectedReportGen;
	private FileUtil fileUtil;
	
	@PostConstruct
	public void init() {
		super.init();
		selectComponent();
		tableModel = new ReportRmdRekapTableModel(reportGenService, paging);
		search();
		fileUtil = FileUtil.getInstance();
	}
	
	private void selectComponent() {
		selectProvTypes();
		selectDocTypes();
		selectSenderTypes();
	}
	
	private void selectProvTypes() {
		provTypes = new ArrayList<SelectItem>();
		try {
			provTypes = parameterDetailService.getListLabelValue(ParameterHeader.PARAM_HEAD_CODE_JENIS_KETENTUAN,
					false, true, facesUtil.retrieveDefaultLocale());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void selectDocTypes() {
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
	
	private void selectSenderTypes() {
		senderTypes = new ArrayList<SelectItem>();
		try {
			senderTypes = parameterDetailService.getListLabelValue(ParameterHeader.PARAM_HEAD_CODE_SENDER, 
					false, true, facesUtil.retrieveDefaultLocale());
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
		tableModel.setSearchCriteria(Arrays.asList(new DefaultSearchObject(CommonConstants.SEARCH_FILTER_BY_REPORT_CODE, REPORT_MATRIX_DIARY_REKAP)));
	}
	
	public void reset() {
		searchDateFrom = null;
		searchDateTo = null;
		searchProvType = null;
		searchDocType = null;
		searchSenderType = null;
		
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
		return Arrays.asList(
				new DefaultSearchObject(WHERE_CREATION_DATE_FROM, DateUtil.dateToStringYYYYMMDD(searchDateFrom, false)),
				new DefaultSearchObject(WHERE_CREATION_DATE_TO, DateUtil.dateToStringYYYYMMDD(searchDateTo, false)),
				new DefaultSearchObject(WHERE_PROVISION_TYPE, searchProvType),
				new DefaultSearchObject(WHERE_DOCUMENT_TYPE, searchDocType),
				new DefaultSearchObject(WHERE_SENDER, searchSenderType));
	}
	
	public void generateExcelInBackgroundJob() {
		ReportGen newReportGen = saveReportGenHistoryAsInProgress();
		
		String docName = "";
		String docNameIn = "";
		String docNameEn = "";
		
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
				docNameEn = getDocumentTypeName.getDocumentTypeEn();
				docNameIn = getDocumentTypeName.getDocumentTypeIn();
			}
		}
		
		ReportRmdRekapTask task = new ReportRmdRekapTask(
				newReportGenId, 
				reportGenService, 
				parameterDetailService, 
				runnableFacesUtil, 
				reportRmdRekapService, 
				searchCriteria,
				userNikName,
				docName,
				docNameIn,
				docNameEn);
		
		taskExecutorBean.submitTask(task);
	}
	
	private ReportGen saveReportGenHistoryAsInProgress() {
		
		try {
			Timestamp currentDate = new Timestamp(new Date().getTime());
			ReportGen newReportGen = new ReportGen();

			User user = facesUtil.getUserLogin();

			newReportGen.setReportGenNik(user.getNik());
			newReportGen.setReportGenEmployeeName(user.getName());
			newReportGen.setReportGenReportName(REPORT_MATRIX_DIARY_REKAP);
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
	public String getSearchSenderType() {
		return searchSenderType;
	}
	public void setSearchSenderType(String searchSenderType) {
		this.searchSenderType = searchSenderType;
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
	public List<SelectItem> getSenderTypes() {
		return senderTypes;
	}
	public void setSenderTypes(List<SelectItem> senderTypes) {
		this.senderTypes = senderTypes;
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
	public ReportRmdRekapService getReportRmdRekapService() {
		return reportRmdRekapService;
	}
	public void setReportRmdRekapService(ReportRmdRekapService reportRmdRekapService) {
		this.reportRmdRekapService = reportRmdRekapService;
	}
	
	public ReportRmdRekapTableModel getTableModel() {
		return tableModel;
	}

	public void setTableModel(ReportRmdRekapTableModel tableModel) {
		this.tableModel = tableModel;
	}

	public ReportGen[] getSelectedReportGen() {
		return selectedReportGen;
	}
	public void setSelectedReportGen(ReportGen[] selectedReportGen) {
		this.selectedReportGen = selectedReportGen;
	}
	public FileUtil getFileUtil() {
		return fileUtil;
	}
	public void setFileUtil(FileUtil fileUtil) {
		this.fileUtil = fileUtil;
	}
}
