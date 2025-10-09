package com.wo.module.report.reportCpsa.bean;

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
import com.wo.module.parameter.model.ParameterHeader;
import com.wo.module.report.reportCpsa.constant.ReportCpsaConstants;
import com.wo.module.report.reportCpsa.service.ReportCpsaService;
import com.wo.module.report.reportCpsa.task.ReportCpsaTask;
import com.wo.module.report.reportGen.constant.ReportGenConstant;
import com.wo.module.report.reportGen.model.ReportGen;
import com.wo.module.report.reportGen.model.ReportGenDetailTableModel;
import com.wo.module.report.reportGen.service.ReportGenService;
import com.wo.module.user.model.User;

public class ReportCpsaBean extends CommonReportRunnableBean
		implements Serializable, ReportGenConstant, ReportCpsaConstants {

	private static final long serialVersionUID = -6507089326930728498L;

	static Logger logger = Logger.getLogger(ReportCpsaBean.class);

	private Date searchDateFrom;
	private Date searchDateTo;
	private Date searchTargetDateFrom;
	private Date searchTargetDateTo;
	private String searchCpsaType;

	private List<SelectItem> cpsaTypes;

	private ReportGenService reportGenService;
	private TaskExecutorBean taskExecutorBean;
	private ReportCpsaService reportCpsaService;

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
		cpsaTypes = new ArrayList<SelectItem>();

		try {
			List<ParameterDetail> pd = parameterDetailService.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_CPSA_TYPE);
			for (int i = 0; i < pd.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((ParameterDetail) pd.get(i)).getName());
				si.setValue(((ParameterDetail) pd.get(i)).getParameterDtlCode());
				cpsaTypes.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void search() {
		tableModel.setSearchCriteria(Arrays.asList(new DefaultSearchObject(SEARCH_FILTER_BY_REPORT_CODE, REPORT_CPSA)));
	}

	public void reset(ActionEvent actionEvent) {
		searchDateFrom = null;
		searchDateTo = null;
		searchCpsaType = null;
		searchTargetDateFrom = null;
		searchTargetDateTo = null;

		search();

		PrimeFaces.current().executeScript("reInitSelect2();");
	}

	public void delete() {
		if (selectedReportGen == null)
			facesUtil.addErrMessage(facesUtil.retrieveMessage("validateDeleteMinOneData"));

		try {
			reportGenService.bulkDelete(selectedReportGen, facesUtil.retrieveUserLogin(), parameterDetailService,
					fileUtil);
			facesUtil.addSuccessMsg(facesUtil.retrieveMessage("deleteSuccess"));
		} catch (Exception e) {
			e.printStackTrace();
			facesUtil.addErrMessage(facesUtil.retrieveMessage("errDeleteBecause") + e.getMessage());
		}
	}

	public List<DefaultSearchObject> buildSearchCriteriaReportGen() {
		return Arrays.asList(
				new DefaultSearchObject(WHERE_CREATED_DATE_FROM, DateUtil.dateToStringYYYYMMDD(searchDateFrom, false)),
				new DefaultSearchObject(WHERE_CREATED_DATE_TO, DateUtil.dateToStringYYYYMMDD(searchDateTo, false)),
				new DefaultSearchObject(WHERE_TARGET_DATE_FROM, DateUtil.dateToStringYYYYMMDD(searchTargetDateFrom, false)),
				new DefaultSearchObject(WHERE_TARGET_DATE_TO, DateUtil.dateToStringYYYYMMDD(searchTargetDateTo, false)),				
				new DefaultSearchObject(WHERE_CPSA_TYPE_CODE, searchCpsaType));				
	}

	public void generateExcelInBackgroundJob() {
		ReportGen newReportGen = saveReportGenHistoryAsInProgress();

		Long newReportGenId = newReportGen.getReportGenId();
		facesUtil.addSuccessMsg("Report Generated Id : [" + newReportGenId + "]");

		List<DefaultSearchObject> searchCriteria = buildSearchCriteriaReportGen();

		User user = facesUtil.getUserLogin();
		String userNikName = user.getNik() + " - " + user.getName();

		ReportCpsaTask task = new ReportCpsaTask(newReportGenId, reportGenService, parameterDetailService,
				runnableFacesUtil, reportCpsaService, searchCriteria, userNikName);

		taskExecutorBean.submitTask(task);
	}

	private ReportGen saveReportGenHistoryAsInProgress() {
		try {
			Timestamp currentDate = new Timestamp(new Date().getTime());
			ReportGen newReportGen = new ReportGen();

			User user = facesUtil.getUserLogin();

			newReportGen.setReportGenNik(user.getNik());
			newReportGen.setReportGenEmployeeName(user.getName());
			newReportGen.setReportGenReportName(REPORT_CPSA);
			newReportGen.setReportGenStatus(CommonConstants.REPORT_GEN_STATUS_ON_PROGRESS);
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

	public String getSearchCpsaType() {
		return searchCpsaType;
	}

	public void setSearchCpsaType(String searchCpsaType) {
		this.searchCpsaType = searchCpsaType;
	}

	public List<SelectItem> getCpsaTypes() {
		return cpsaTypes;
	}

	public void setCpsaTypes(List<SelectItem> cpsaTypes) {
		this.cpsaTypes = cpsaTypes;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		ReportCpsaBean.logger = logger;
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

	public ReportCpsaService getReportCpsaService() {
		return reportCpsaService;
	}

	public void setReportCpsaService(ReportCpsaService reportCpsaService) {
		this.reportCpsaService = reportCpsaService;
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
