package com.wo.module.report.reportFine.bean;

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
import com.wo.module.documentType.service.DocumentTypeService;
import com.wo.module.parameter.model.ParameterHeader;
import com.wo.module.report.reportFine.constant.ReportFineConstant;
import com.wo.module.report.reportFine.service.ReportFineService;
import com.wo.module.report.reportFine.task.ReportFineTask;
import com.wo.module.report.reportGen.constant.ReportGenConstant;
import com.wo.module.report.reportGen.model.ReportGen;
import com.wo.module.report.reportGen.model.ReportGenDetailTableModel;
import com.wo.module.report.reportGen.service.ReportGenService;
import com.wo.module.user.model.User;

public class ReportFineBean extends CommonReportRunnableBean implements Serializable{

	private static final long serialVersionUID = 1423671392516643948L;
	private static final Logger logger = Logger.getLogger(ReportFineBean.class);
	
	private Date searchCreationDateFrom;
	private Date searchCreationDateTo;
	private Date searchTargetDateFrom;
	private Date searchTargetDateTo;
	private String searchSenderCode;
	
	private List<SelectItem> senderCodeList;
	
	private DocumentTypeService documentTypeService;
	private ReportGenService reportGenService;
	private TaskExecutorBean taskExecutorBean;
	private ReportFineService reportFineService;
	
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
		initSenderCodeList();
	}
	
	private void initSenderCodeList() {
		senderCodeList = new ArrayList<SelectItem>();
		try {
			senderCodeList = parameterDetailService.getListLabelValue(ParameterHeader.PARAM_HEAD_CODE_SENDER,
					false, true, facesUtil.retrieveDefaultLocale());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		PrimeFaces.current().executeScript("reInitSelect2();");
	}

	
	public void search() {
		tableModel.setSearchCriteria(Arrays.asList(new DefaultSearchObject(CommonConstants.SEARCH_FILTER_BY_REPORT_CODE,ReportGenConstant.REPORT_FINE)));
		
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	public void reset(ActionEvent event) {
		searchCreationDateFrom = null;
		searchCreationDateTo = null;
		searchTargetDateFrom = null;
		searchTargetDateTo = null;
		searchSenderCode = null;
		
		search();
		
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	public void delete() {
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
	
	public List<DefaultSearchObject> buildSearchCriteriaReportGen(){
		return Arrays.asList(
				new DefaultSearchObject(ReportFineConstant.WHERE_CREATION_DATE_FROM, DateUtil.dateToStringYYYYMMDD(searchCreationDateFrom, false)),
				new DefaultSearchObject(ReportFineConstant.WHERE_CREATION_DATE_TO, DateUtil.dateToStringYYYYMMDD(searchCreationDateTo, false)),
				new DefaultSearchObject(ReportFineConstant.WHERE_SENDER_CODE, searchSenderCode),
				new DefaultSearchObject(ReportFineConstant.WHERE_TARGET_DATE_FROM, DateUtil.dateToStringYYYYMMDD(searchTargetDateFrom, false)),
				new DefaultSearchObject(ReportFineConstant.WHERE_TARGET_DATE_TO, DateUtil.dateToStringYYYYMMDD(searchTargetDateTo, false)));
	}
	
	public void generateExcelInBackgroundJob() {
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
		ReportGen newReportGen = saveReportGenHistoryAsInProgress();
		
		Long newReportGenId = newReportGen.getReportGenId();
		facesUtil.addSuccessMsg("Report Generated Id : [" + newReportGenId + "]");
		
		List<DefaultSearchObject> searchCriteria = buildSearchCriteriaReportGen();
		
		ReportFineTask task = new ReportFineTask(
				newReportGenId,
				reportGenService, 
				parameterDetailService, 
				runnableFacesUtil, 
				reportFineService, 
				searchCriteria, 
				userNikName);
		
		taskExecutorBean.submitTask(task);
	}
	
	private ReportGen saveReportGenHistoryAsInProgress() {
		try {
			
			Timestamp currentDate = new Timestamp(new Date().getTime());
			ReportGen newReportGen = new ReportGen();

			User user = facesUtil.getUserLogin();

			newReportGen.setReportGenNik(user.getNik());
			newReportGen.setReportGenEmployeeName(user.getName());
			newReportGen.setReportGenReportName(ReportGenConstant.REPORT_FINE);
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

	public String getSearchSenderCode() {
		return searchSenderCode;
	}

	public void setSearchSenderCode(String searchSenderCode) {
		this.searchSenderCode = searchSenderCode;
	}

	public List<SelectItem> getSenderCodeList() {
		return senderCodeList;
	}

	public void setSenderCodeList(List<SelectItem> senderCodeList) {
		this.senderCodeList = senderCodeList;
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

	public ReportFineService getReportFineTaskService() {
		return reportFineService;
	}

	public void setReportFineTaskService(ReportFineService reportFineService) {
		this.reportFineService = reportFineService;
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

	public ReportFineService getReportFineService() {
		return reportFineService;
	}

	public void setReportFineService(ReportFineService reportFineService) {
		this.reportFineService = reportFineService;
	}
	
}
