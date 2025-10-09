package com.wo.module.report.reportLogAccess.bean;

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
import com.wo.module.report.reportLogAccess.constant.ReportLogAccessConstants;
import com.wo.module.report.reportLogAccess.service.ReportLogAccessService;
import com.wo.module.report.reportLogAccess.task.ReportLogAccessTask;
import com.wo.module.user.model.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter	
public class ReportLogAccessBean extends CommonReportRunnableBean implements Serializable{

	private static final long serialVersionUID = -4702026240155248058L;
	private static final Logger logger = Logger.getLogger(ReportLogAccessBean.class);
	
	private Date searchDateFrom;	
	private Date searchDateTo;
	
	private String searchName;	
	private String searchDivName;
	
	private ReportGenService reportGenService;
	private TaskExecutorBean taskExecutorBean;
	private ReportLogAccessService reportLogAccessService;
	
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
		tableModel.setSearchCriteria(Arrays.asList(new DefaultSearchObject(CommonConstants.SEARCH_FILTER_BY_REPORT_CODE, ReportGenConstant.REPORT_LOG_ACCESS)));
		
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	public void reset(ActionEvent event) {
		searchDateFrom = null;
		searchDateTo = null;
		
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
				new DefaultSearchObject(ReportLogAccessConstants.WHERE_DATE_START, DateUtil.dateToStringYYYYMMDD(searchDateFrom, false)),
				new DefaultSearchObject(ReportLogAccessConstants.WHERE_DATE_END, DateUtil.dateToStringYYYYMMDD(searchDateTo, false)),
				new DefaultSearchObject(ReportLogAccessConstants.WHERE_NAME, searchName),
				new DefaultSearchObject(ReportLogAccessConstants.WHERE_DIV_NAME, searchDivName));
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
		
		ReportGen newReportGen = saveReportGenAsInProgress();
		
		Long newReportGenId = newReportGen.getReportGenId();
		facesUtil.addSuccessMsg("Report Generated Id : [" + newReportGenId + "]");
		
		List<DefaultSearchObject> searchCriteria = buildSearchCriteriaReportGen();
		
		ReportLogAccessTask task = new ReportLogAccessTask(
				newReportGenId,
				reportGenService,
				parameterDetailService,
				runnableFacesUtil,
				reportLogAccessService,
				searchCriteria, 
				userNikName);
		
		taskExecutorBean.submitTask(task);
	}
	
	private ReportGen saveReportGenAsInProgress() {
		try {
			Timestamp currentDate = new Timestamp(new Date().getTime());
			ReportGen newReportGen = new ReportGen();

			User user = facesUtil.getUserLogin();

			newReportGen.setReportGenNik(user.getNik());
			newReportGen.setReportGenEmployeeName(user.getName());
			newReportGen.setReportGenReportName(ReportGenConstant.REPORT_LOG_ACCESS);
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
	
	
}
