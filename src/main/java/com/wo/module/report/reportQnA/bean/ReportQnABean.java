package com.wo.module.report.reportQnA.bean;

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
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;
import com.wo.module.report.reportGen.constant.ReportGenConstant;
import com.wo.module.report.reportGen.model.ReportGen;
import com.wo.module.report.reportGen.model.ReportGenDetailTableModel;
import com.wo.module.report.reportGen.service.ReportGenService;
import com.wo.module.report.reportQnA.constant.ReportQnAConstant;
import com.wo.module.report.reportQnA.service.ReportQnAService;
import com.wo.module.report.reportQnA.task.ReportQnATask;
import com.wo.module.user.model.User;

public class ReportQnABean extends CommonReportRunnableBean implements Serializable{

	private static final long serialVersionUID = 914600466156037730L;
	private static final Logger logger = Logger.getLogger(ReportQnABean.class);
	
	private ReportGenService reportGenService;
	private TaskExecutorBean taskExecutorBean;
	private ReportQnAService reportQnAService;
	
	private Date searchQuestionDateFrom;
	private Date searchQuestionDateTo;
	private String searchCategory;
	
	private List<SelectItem> categoryList;
	
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
		initSelectCategory();
	}
	
	private void initSelectCategory() {
		categoryList = new ArrayList<SelectItem>();

		try {
			List<ParameterDetail> pd = parameterDetailService.getParameterDetailByParamCode(ParameterHeader.PARAM_QNA_CATEGORY);
			for (ParameterDetail param : pd) {
				SelectItem si = new SelectItem();
				si.setLabel(param.getName());
				si.setValue(param.getParameterDtlCode());
				categoryList.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void search() {
		tableModel.setSearchCriteria(Arrays.asList(new DefaultSearchObject(CommonConstants.SEARCH_FILTER_BY_REPORT_CODE,ReportGenConstant.REPORT_QNA)));
		
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	public void reset()	{
		searchQuestionDateFrom = null;
		searchQuestionDateTo = null;
		searchCategory = "";
		
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
				new DefaultSearchObject(ReportQnAConstant.SEARCH_BY_QUESTION_DATE_FROM, DateUtil.dateToStringYYYYMMDD(searchQuestionDateFrom, false)),
				new DefaultSearchObject(ReportQnAConstant.SEARCH_BY_QUESTION_DATE_TO, DateUtil.dateToStringYYYYMMDD(searchQuestionDateTo, false)),
				new DefaultSearchObject(ReportQnAConstant.SEARCH_BY_CATEGORY, searchCategory));
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
		
		ReportQnATask task = new ReportQnATask(
				newReportGenId, 
				reportGenService, 
				parameterDetailService, 
				runnableFacesUtil, 
				reportQnAService, 
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
			newReportGen.setReportGenReportName(ReportGenConstant.REPORT_QNA);
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

	public ReportQnAService getReportQnAService() {
		return reportQnAService;
	}

	public void setReportQnAService(ReportQnAService reportQnAService) {
		this.reportQnAService = reportQnAService;
	}

	public Date getSearchQuestionDateFrom() {
		return searchQuestionDateFrom;
	}

	public void setSearchQuestionDateFrom(Date searchQuestionDateFrom) {
		this.searchQuestionDateFrom = searchQuestionDateFrom;
	}

	public Date getSearchQuestionDateTo() {
		return searchQuestionDateTo;
	}

	public void setSearchQuestionDateTo(Date searchQuestionDateTo) {
		this.searchQuestionDateTo = searchQuestionDateTo;
	}

	public String getSearchCategory() {
		return searchCategory;
	}

	public void setSearchCategory(String searchCategory) {
		this.searchCategory = searchCategory;
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

	public List<SelectItem> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<SelectItem> categoryList) {
		this.categoryList = categoryList;
	}
	
}
