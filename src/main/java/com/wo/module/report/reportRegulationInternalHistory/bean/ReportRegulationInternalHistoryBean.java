package com.wo.module.report.reportRegulationInternalHistory.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.event.ActionEvent;


import org.apache.log4j.Logger;
import org.primefaces.PrimeFaces;

import com.wo.module.common.bean.CommonReportRunnableBean;
import com.wo.module.common.constant.CommonConstants;
import com.wo.module.common.paging.DefaultSearchObject;
import com.wo.module.common.task.TaskExecutorBean;
import com.wo.module.common.util.DateUtil;
import com.wo.module.common.util.FileUtil;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.report.reportGen.constant.ReportGenConstant;
import com.wo.module.report.reportGen.model.ReportGen;
import com.wo.module.report.reportGen.model.ReportGenDetailTableModel;
import com.wo.module.report.reportGen.service.ReportGenService;
import com.wo.module.report.reportRegulationInternalHistory.constant.ReportRegulationInternalHistoryConstant;
import com.wo.module.report.reportRegulationInternalHistory.service.ReportRegulationInternalHistoryService;
import com.wo.module.report.reportRegulationInternalHistory.task.ReportRegulationInternalHistoryTask;
import com.wo.module.user.model.User;

public class ReportRegulationInternalHistoryBean extends CommonReportRunnableBean 
	implements Serializable{

	private static final long serialVersionUID = 976795816175549799L;
	private static final Logger logger = Logger.getLogger(ReportRegulationInternalHistoryBean.class);
	
	private Date searchCreationDateFrom;
	private Date searchCreationDateTo;
	
	private ReportGenService reportGenService;
	private TaskExecutorBean taskExecutorBean;
	private ReportRegulationInternalHistoryService reportRegulationInternalHistoryService;
	
	private ReportGenDetailTableModel tableModel;
	private ReportGen[] selectedReportGen;
	private FileUtil fileUtil;
	
	@PostConstruct
	public void init() {
		super.init();
		tableModel = new ReportGenDetailTableModel(reportGenService, paging);
		search();
		fileUtil = FileUtil.getInstance();
	}

	public void search() {
		tableModel.setSearchCriteria(Arrays.asList(new DefaultSearchObject(CommonConstants.SEARCH_FILTER_BY_REPORT_CODE, ReportGenConstant.REPORT_LOG_ACTIVITY)));
		
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	public void reset(ActionEvent event) {
		searchCreationDateFrom = null;
		searchCreationDateTo = null;
		
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
		return  Arrays.asList(
				new DefaultSearchObject(ReportRegulationInternalHistoryConstant.WHERE_CREATE_DATE_FROM,DateUtil.dateToStringYYYYMMDD(searchCreationDateFrom, false)),
				new DefaultSearchObject(ReportRegulationInternalHistoryConstant.WHERE_CREATE_DATE_TO,DateUtil.dateToStringYYYYMMDD(searchCreationDateTo, false)));
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
		
		ReportRegulationInternalHistoryTask task = new ReportRegulationInternalHistoryTask(
				newReportGenId,
				reportGenService,
				parameterDetailService,
				runnableFacesUtil,
				reportRegulationInternalHistoryService,
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
			newReportGen.setReportGenReportName(ReportGenConstant.REPORT_LOG_ACTIVITY);
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

	public ReportRegulationInternalHistoryService getReportRegulationInternalHistoryService() {
		return reportRegulationInternalHistoryService;
	}

	public void setReportRegulationInternalHistoryService(
			ReportRegulationInternalHistoryService reportRegulationInternalHistoryService) {
		this.reportRegulationInternalHistoryService = reportRegulationInternalHistoryService;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static Logger getLogger() {
		return logger;
	}
	
	
}
