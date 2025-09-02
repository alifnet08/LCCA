package com.wo.module.report.reportRegulationMonitoring.bean;

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
import com.wo.module.parameter.model.ParameterHeader;
import com.wo.module.report.reportGen.constant.ReportGenConstant;
import com.wo.module.report.reportGen.model.ReportGen;
import com.wo.module.report.reportGen.model.ReportGenDetailTableModel;
import com.wo.module.report.reportGen.service.ReportGenService;
import com.wo.module.report.reportRegulationMonitoring.constant.ReportRegulationMonitoringConstant;
import com.wo.module.report.reportRegulationMonitoring.service.ReportRegulationMonitoringService;
import com.wo.module.report.reportRegulationMonitoring.task.ReportRegulationMonitoringTask;
import com.wo.module.user.model.User;

public class ReportRegulationMonitoringBean extends CommonReportRunnableBean implements Serializable{

	private static final long serialVersionUID = -2494919279602291821L;
	private static final Logger logger = Logger.getLogger(ReportRegulationMonitoringBean.class);

	private Date searchDateFrom;
	private Date searchDateTo;
	private Date searchTargetDateFrom;
	private Date searchTargetDateTo;
	private String searchProvType;
	private String searchDocType;
	
	private List<SelectItem> provTypeList;
	private List<SelectItem> docTypeList;
	
	private DocumentTypeService documentTypeService;
	private ReportGenService reportGenService;
	private TaskExecutorBean taskExecutorBean;
	private ReportRegulationMonitoringService reportRegulationMonitoringService;
	
	private ReportGenDetailTableModel tableModel;
	private ReportGen[] selectedReportGen;
	private FileUtil fileUtil;
	
	@PostConstruct
	public void init() {
		super.init();
		initComponent();
		tableModel = new ReportGenDetailTableModel(reportGenService, paging);
		search();
		fileUtil = FileUtil.getInstance();
	}
	
	private void initComponent() {
		initProvTypeList();
		initDocTypeList();
	}
	
	private void initProvTypeList() {
		provTypeList = new ArrayList<SelectItem>();
		try {
			provTypeList = parameterDetailService.getListLabelValue(ParameterHeader.PARAM_HEAD_CODE_JENIS_KETENTUAN,
					false, true, facesUtil.retrieveDefaultLocale());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	private void initDocTypeList() {
		docTypeList = new ArrayList<SelectItem>();
		try {
			List<DocumentType> pd = documentTypeService.searchData(
					Arrays.asList(new DefaultSearchObject(DocumentTypeConstants.WHERE_JENIS_KETENTUAN_CODE, null)), 0,
					Integer.MAX_VALUE, null, null);
			for (int i = 0; i < pd.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((DocumentType) pd.get(i)).getDocumentType());
				si.setValue(((DocumentType) pd.get(i)).getDocumentTypeId());
				docTypeList.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		PrimeFaces.current().executeScript("reInitSelect2();");
	}

	public void onChangeProvType() {
		initSelectDocType();
	}
	
	public void initSelectDocType() {
		docTypeList = new ArrayList<SelectItem>();
		
		try {
			List<DocumentType> pd = documentTypeService.searchData(
					Arrays.asList(new DefaultSearchObject(DocumentTypeConstants.WHERE_JENIS_KETENTUAN_CODE, searchProvType)), 0,
					Integer.MAX_VALUE, null, null);
			for (int i = 0; i < pd.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((DocumentType) pd.get(i)).getDocumentType());
				si.setValue(((DocumentType) pd.get(i)).getDocumentTypeId());
				docTypeList.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	public void search() {
		tableModel.setSearchCriteria(Arrays.asList(new DefaultSearchObject(CommonConstants.SEARCH_FILTER_BY_REPORT_CODE, ReportGenConstant.REPORT_REGULATION_MONITORING)));
	}
	
	public void reset(ActionEvent event) {
		searchDateFrom = null;
		searchDateTo = null;
		searchTargetDateFrom = null;
		searchTargetDateTo = null;
		searchProvType = null;
		searchDocType = null;
		
		search();
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	public List<DefaultSearchObject> buildSearchCriteriaReportGen(){
		return Arrays.asList(new DefaultSearchObject(ReportRegulationMonitoringConstant.WHERE_CREATION_DATE_FROM, DateUtil.dateToStringYYYYMMDD(searchDateFrom, false)),
				new DefaultSearchObject(ReportRegulationMonitoringConstant.WHERE_CREATION_DATE_TO, DateUtil.dateToStringYYYYMMDD(searchDateTo, false)),
				new DefaultSearchObject(ReportRegulationMonitoringConstant.WHERE_TARGET_DATE_FROM, DateUtil.dateToStringYYYYMMDD(searchTargetDateFrom, false)),
				new DefaultSearchObject(ReportRegulationMonitoringConstant.WHERE_TARGET_DATE_TO, DateUtil.dateToStringYYYYMMDD(searchTargetDateTo, false)),
				new DefaultSearchObject(ReportRegulationMonitoringConstant.WHERE_PROV_TYPE, searchProvType),
				new DefaultSearchObject(ReportRegulationMonitoringConstant.WHERE_DOC_TYPE, searchDocType));
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
		
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	public void generateExcelInBackgroundJob () {
		String docName = "";
		String userNikName = "";
		User user = facesUtil.getUserLogin();
		
		if (user.getNik() != null && !user.getNik().equals("")) {
			userNikName = user.getNik();
			if (user.getName() != null && !user.getName().equals("")) {
				userNikName = user.getNik() + " - " + user.getName();
			}
		} else {
			if (user.getName() != null && !user.getName().equals("")) {
				userNikName = user.getName();				
			}
		}
		if (searchDocType != null && !searchDocType.equals("")) {
			Long docId = Long.parseLong(searchDocType);
			DocumentType getDocumentTypeName = documentTypeService.findById(docId);
			
			if (getDocumentTypeName != null) {
				docName = getDocumentTypeName.getDocumentType();
			}
		}
		
		ReportGen newReportGen = saveReportGenHistoryAsInProgress();
		Long newReportGenId = newReportGen.getReportGenId();
		
		facesUtil.addSuccessMsg("Report Generate Id : [" + newReportGenId + "]");
		
		List<DefaultSearchObject> searchCriteria = buildSearchCriteriaReportGen();
		
		ReportRegulationMonitoringTask task = new ReportRegulationMonitoringTask(
				newReportGenId,
				reportGenService, 
				parameterDetailService, 
				runnableFacesUtil, 
				reportRegulationMonitoringService, 
				searchCriteria, 
				userNikName, 
				docName);
		
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
				.setReportGenReportName(ReportGenConstant.REPORT_REGULATION_MONITORING);
			newReportGen
				.setReportGenStatus(CommonConstants.REPORT_GEN_STATUS_ON_PROGRESS);
			newReportGen
				.setEnabledFlag(CommonConstants.ENABLED_FLAG_TRUE);
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

	public List<SelectItem> getProvTypeList() {
		return provTypeList;
	}

	public void setProvTypeList(List<SelectItem> provTypeList) {
		this.provTypeList = provTypeList;
	}

	public List<SelectItem> getDocTypeList() {
		return docTypeList;
	}

	public void setDocTypeList(List<SelectItem> docTypeList) {
		this.docTypeList = docTypeList;
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

	public ReportRegulationMonitoringService getReportRegulationMonitoringService() {
		return reportRegulationMonitoringService;
	}

	public void setReportRegulationMonitoringService(ReportRegulationMonitoringService reportRegulationMonitoringService) {
		this.reportRegulationMonitoringService = reportRegulationMonitoringService;
	}

	public ReportGenDetailTableModel getTableModel() {
		return tableModel;
	}

	public void setTableModel(ReportGenDetailTableModel tableModel) {
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

	public static Logger getLogger() {
		return logger;
	}

	public DocumentTypeService getDocumentTypeService() {
		return documentTypeService;
	}

	public void setDocumentTypeService(DocumentTypeService documentTypeService) {
		this.documentTypeService = documentTypeService;
	}
	
}
