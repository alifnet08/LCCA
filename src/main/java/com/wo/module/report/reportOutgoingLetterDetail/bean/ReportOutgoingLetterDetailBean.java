package com.wo.module.report.reportOutgoingLetterDetail.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

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
import com.wo.module.report.reportOutgoingLetterDetail.constant.ReportOutgoingLetterDetailConstants;
import com.wo.module.report.reportOutgoingLetterDetail.service.ReportOutgoingLetterDetailService;
import com.wo.module.report.reportOutgoingLetterDetail.task.ReportOutgoingLetterDetailTask;
import com.wo.module.report.reportOutgoingLetterRekap.service.ReportOutgoingLetterRekapService;
import com.wo.module.user.model.User;

public class ReportOutgoingLetterDetailBean extends CommonReportRunnableBean
	implements Serializable, ReportGenConstant, ReportOutgoingLetterDetailConstants{

	private static final long serialVersionUID = -4665694498943322880L;

	static Logger logger = Logger.getLogger(ReportOutgoingLetterDetailBean.class);
	
	private Date searchCreationDateFrom;
	private Date searchCreationDateTo;
	private Date searchLetterDateFrom;
	private Date searchLetterDateTo;
	
	private ReportGenService reportGenService;
	private TaskExecutorBean taskExecutorBean;
	private ReportOutgoingLetterDetailService reportOutgoingLetterDetailService;
	private ReportOutgoingLetterRekapService reportOutgoingLetterRekapService;
	
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
		tableModel.setSearchCriteria(Arrays.asList(new DefaultSearchObject(CommonConstants.SEARCH_FILTER_BY_REPORT_CODE,REPORT_OUTGOING_LETTER_DETAIL)));
		
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	public void reset() {
		searchCreationDateFrom = null;
		searchCreationDateTo = null;
		searchLetterDateFrom = null;
		searchLetterDateTo = null;
		
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
	
	public List<DefaultSearchObject> buildSearchCriteriaReportGen() {
		return Arrays.asList(
				new DefaultSearchObject(WHERE_CREATION_DATE_FROM, DateUtil.dateToStringYYYYMMDD(searchCreationDateFrom, false)),
				new DefaultSearchObject(WHERE_CREATION_DATE_TO, DateUtil.dateToStringYYYYMMDD(searchCreationDateTo, false)),
				new DefaultSearchObject(WHERE_LETTER_DATE_FROM, DateUtil.dateToStringYYYYMMDD(searchLetterDateFrom, false)),
				new DefaultSearchObject(WHERE_LETTER_DATE_TO, DateUtil.dateToStringYYYYMMDD(searchLetterDateTo, false))
				);
	}
	
	public void generateExcelInBackgroundJob() {
		
		if (searchCreationDateFrom == null && searchCreationDateTo == null && searchLetterDateFrom == null && searchLetterDateTo == null) {
			facesUtil.addErrMessage(
					facesUtil.retrieveMessage("formReportOutgoingLetterDetailCreationDate") + " "
					+ facesUtil.retrieveMessage("or") + " "
					+ facesUtil.retrieveMessage("formReportOutgoingLetterDetailLetterDate") + " "
					+ facesUtil.retrieveMessage("validateRequired"));
			return;
		}
		
		ReportGen newReportGen = saveReportGenHistoryAsInProgress();
		
		Long newReportGenId = newReportGen.getReportGenId();
		facesUtil.addSuccessMsg("Report Generated Id : [" + newReportGenId + "]");
		
		List<DefaultSearchObject> searchCriteria = buildSearchCriteriaReportGen();
		
		User user = facesUtil.getUserLogin();
		String userNikName = user.getNik() + " - " + user.getName();
		
		ReportOutgoingLetterDetailTask task = new ReportOutgoingLetterDetailTask(
				newReportGenId, 
				reportGenService, 
				parameterDetailService, 
				runnableFacesUtil, 
				reportOutgoingLetterDetailService, 
				reportOutgoingLetterRekapService,
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
			newReportGen.setReportGenReportName(REPORT_OUTGOING_LETTER_DETAIL);
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

	public Date getSearchLetterDateFrom() {
		return searchLetterDateFrom;
	}

	public void setSearchLetterDateFrom(Date searchLetterDateFrom) {
		this.searchLetterDateFrom = searchLetterDateFrom;
	}

	public Date getSearchLetterDateTo() {
		return searchLetterDateTo;
	}

	public void setSearchLetterDateTo(Date searchLetterDateTo) {
		this.searchLetterDateTo = searchLetterDateTo;
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

	public ReportOutgoingLetterDetailService getReportOutgoingLetterDetailService() {
		return reportOutgoingLetterDetailService;
	}

	public void setReportOutgoingLetterDetailService(ReportOutgoingLetterDetailService reportOutgoingLetterDetailService) {
		this.reportOutgoingLetterDetailService = reportOutgoingLetterDetailService;
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

	public ReportOutgoingLetterRekapService getReportOutgoingLetterRekapService() {
		return reportOutgoingLetterRekapService;
	}

	public void setReportOutgoingLetterRekapService(ReportOutgoingLetterRekapService reportOutgoingLetterRekapService) {
		this.reportOutgoingLetterRekapService = reportOutgoingLetterRekapService;
	}
}
