package com.wo.module.report.reportComplianceOnSiteReviewRekap.bean;

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
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.report.reportComplianceOnSiteReviewRekap.constant.ReportComplianceOnSiteReviewRekapConstants;
import com.wo.module.report.reportComplianceOnSiteReviewRekap.service.ReportComplianceOnSiteReviewRekapService;
import com.wo.module.report.reportComplianceOnSiteReviewRekap.task.ReportComplianceOnSiteReviewRekapTask;
import com.wo.module.report.reportGen.constant.ReportGenConstant;
import com.wo.module.report.reportGen.model.ReportGen;
import com.wo.module.report.reportGen.model.ReportGenDetailTableModel;
import com.wo.module.report.reportGen.service.ReportGenService;
import com.wo.module.tmpComplianceReview.service.ComplianceReviewService;
import com.wo.module.user.model.User;

public class ReportComplianceOnSiteReviewRekapBean extends CommonReportRunnableBean implements Serializable, ReportGenConstant, ReportComplianceOnSiteReviewRekapConstants{

	private static final long serialVersionUID = -870412755247133981L;

	static Logger logger = Logger.getLogger(ReportComplianceOnSiteReviewRekapBean.class);
	
	private Date searchDateFrom;
	private Date searchDateTo;
	private String searchReviewCategory;
	
	private List<SelectItem> reviewCategorys;
	
	private ComplianceReviewService complianceReviewService;
	private ReportGenService reportGenService;
	private TaskExecutorBean taskExecutorBean;
	private ReportComplianceOnSiteReviewRekapService reportComplianceOnSiteReviewRekapService;
	
	private ReportGenDetailTableModel tableModel;
	private ReportGen[] selectedReportGen;
	private FileUtil fileUtil;
	
	@PostConstruct
	public void init() {
		super.init();
		selectComponent();
		tableModel = new ReportGenDetailTableModel(reportGenService, paging);
		search();
		fileUtil = FileUtil.getInstance();
	}
	
	private void selectComponent() {
		selectReviewCategory();
	}
	
	private void selectReviewCategory() {
		reviewCategorys = new ArrayList<SelectItem>();

		try {
			List<ParameterDetail> pd = parameterDetailService.getParameterDetailByParamCode("REVIEW_CATEGORY");
			for (int i = 0; i < pd.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((ParameterDetail) pd.get(i)).getName());
				si.setValue(((ParameterDetail) pd.get(i)).getParameterDtlCode());
				reviewCategorys.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void search () {
		tableModel.setSearchCriteria(Arrays.asList(
				new DefaultSearchObject(SEARCH_FILTER_BY_REPORT_CODE, REPORT_COMPLIANCE_ON_SITE_REVIEW_REKAP)));
	}
	
	public void reset(ActionEvent actionEvent) {
		searchDateFrom = null;
		searchDateTo = null;
		searchReviewCategory = null;
		
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
				new DefaultSearchObject(WHERE_REVIEW_CATEGORY, searchReviewCategory));
	}
	
	public void generateExcelInBackgroundJob() {
		ReportGen newReportGen = saveReportGenHistoryAsInProgress(); 
		
		Long newReportGenId = newReportGen.getReportGenId();
		facesUtil.addSuccessMsg("Report Generated Id : [" + newReportGenId + "]");
		
		List<DefaultSearchObject> searchCriteria = buildSearchCriteriaReportGen();

		User user = facesUtil.getUserLogin();
		String userNikName = user.getNik() + " - " + user.getName();
		
		ReportComplianceOnSiteReviewRekapTask task = new ReportComplianceOnSiteReviewRekapTask(
				newReportGenId,
				reportGenService,
				parameterDetailService, 
				runnableFacesUtil, 
				reportComplianceOnSiteReviewRekapService, 
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
			newReportGen
					.setReportGenReportName(REPORT_COMPLIANCE_ON_SITE_REVIEW_REKAP);
			newReportGen
				.setReportGenStatus(CommonConstants.REPORT_GEN_STATUS_ON_PROGRESS);
			newReportGen.setEnabledFlag(CommonConstants.ENABLED_FLAG_TRUE);
		
			newReportGen.setCreationDate(currentDate);
			newReportGen.setCreatedBy(user.getNik());
			reportGenService.save(newReportGen);
			return newReportGen;
			
		}catch (Exception e) {
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
	public String getSearchReviewCategory() {
		return searchReviewCategory;
	}
	public void setSearchReviewCategory(String searchReviewCategory) {
		this.searchReviewCategory = searchReviewCategory;
	}
	public List<SelectItem> getReviewCategorys() {
		return reviewCategorys;
	}
	public void setReviewCategorys(List<SelectItem> reviewCategorys) {
		this.reviewCategorys = reviewCategorys;
	}
	public ComplianceReviewService getComplianceReviewService() {
		return complianceReviewService;
	}
	public void setComplianceReviewService(ComplianceReviewService complianceReviewService) {
		this.complianceReviewService = complianceReviewService;
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
	public ReportComplianceOnSiteReviewRekapService getReportComplianceOnSiteReviewRekapService() {
		return reportComplianceOnSiteReviewRekapService;
	}
	public void setReportComplianceOnSiteReviewRekapService(
			ReportComplianceOnSiteReviewRekapService reportComplianceOnSiteReviewRekapService) {
		this.reportComplianceOnSiteReviewRekapService = reportComplianceOnSiteReviewRekapService;
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
}
