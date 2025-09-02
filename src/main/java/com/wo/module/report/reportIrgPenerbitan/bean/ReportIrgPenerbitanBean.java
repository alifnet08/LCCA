package com.wo.module.report.reportIrgPenerbitan.bean;

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
import com.wo.module.holiday.service.HolidayService;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.report.reportGen.constant.ReportGenConstant;
import com.wo.module.report.reportGen.model.ReportGen;
import com.wo.module.report.reportGen.model.ReportGenDetailTableModel;
import com.wo.module.report.reportGen.service.ReportGenService;
import com.wo.module.report.reportIrgPenerbitan.constant.ReportIrgPenerbitanConstants;
import com.wo.module.report.reportIrgPenerbitan.service.ReportIrgPenerbitanService;
import com.wo.module.report.reportIrgPenerbitan.task.ReportIrgPenerbitanTask;
import com.wo.module.user.model.Division;
import com.wo.module.user.model.User;
import com.wo.module.user.service.UserService;

public class ReportIrgPenerbitanBean extends CommonReportRunnableBean
		implements Serializable, ReportGenConstant, ReportIrgPenerbitanConstants {

	private static final long serialVersionUID = 4981863859918085660L;

	static Logger logger = Logger.getLogger(ReportIrgPenerbitanBean.class);

	private Date searchDateFrom;
	private Date searchDateTo;
	private Date searchTargetDateFrom;
	private Date searchTargetDateTo;
	private String searchUnitKerjaTpg;
	private String searchTypeRegulation;

	private List<SelectItem> unitKerjaTpgs;
	private List<SelectItem> typeRegulations;

	private ReportGenService reportGenService;
	private TaskExecutorBean taskExecutorBean;
	private ReportIrgPenerbitanService reportIrgPenerbitanService;
	private UserService userService;
	private HolidayService holidayService;

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
		unitKerjaTpgs = new ArrayList<SelectItem>();
		try {
			List<Division> pd = userService.getAllDivision();
			for (int i = 0; i < pd.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((Division) pd.get(i)).getDivisionName());
				si.setValue(((Division) pd.get(i)).getDivisionName());
				unitKerjaTpgs.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		typeRegulations = new ArrayList<SelectItem>();
		try {
			List<ParameterDetail> pd = parameterDetailService.getParameterDetailByParamCode("TYPE_REGULATION");
			for (int i = 0; i < pd.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((ParameterDetail) pd.get(i)).getName());
				si.setValue(((ParameterDetail) pd.get(i)).getParameterDtlCode());
				typeRegulations.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void search() {
		tableModel.setSearchCriteria(Arrays.asList(new DefaultSearchObject(SEARCH_FILTER_BY_REPORT_CODE, REPORT_IRG_PENERBITAN)));
	}

	public void reset(ActionEvent actionEvent) {
		searchDateFrom = null;
		searchDateTo = null;
		searchTypeRegulation = null;
		searchUnitKerjaTpg = null;
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
				new DefaultSearchObject(WHERE_REGULATION_TYPE, searchTypeRegulation),
				new DefaultSearchObject(WHERE_UNIT_KERJA_TPG, searchUnitKerjaTpg)
				);				
	}

	public void generateExcelInBackgroundJob() {
		ReportGen newReportGen = saveReportGenHistoryAsInProgress();

		Long newReportGenId = newReportGen.getReportGenId();
		facesUtil.addSuccessMsg("Report Generated Id : [" + newReportGenId + "]");

		List<DefaultSearchObject> searchCriteria = buildSearchCriteriaReportGen();

		User user = facesUtil.getUserLogin();
		String userNikName = user.getNik() + " - " + user.getName();

		ReportIrgPenerbitanTask task = new ReportIrgPenerbitanTask(newReportGenId, reportGenService, parameterDetailService, userService, holidayService,
				runnableFacesUtil, reportIrgPenerbitanService, searchCriteria, userNikName);

		taskExecutorBean.submitTask(task);
	}

	private ReportGen saveReportGenHistoryAsInProgress() {
		try {
			Timestamp currentDate = new Timestamp(new Date().getTime());
			ReportGen newReportGen = new ReportGen();

			User user = facesUtil.getUserLogin();

			newReportGen.setReportGenNik(user.getNik());
			newReportGen.setReportGenEmployeeName(user.getName());
			newReportGen.setReportGenReportName(REPORT_IRG_PENERBITAN);
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

	public String getSearchTypeRegulation() {
		return searchTypeRegulation;
	}

	public void setSearchTypeRegulation(String searchTypeRegulation) {
		this.searchTypeRegulation = searchTypeRegulation;
	}

	public List<SelectItem> getTypeRegulations() {
		return typeRegulations;
	}

	public void setTypeRegulations(List<SelectItem> typeRegulations) {
		this.typeRegulations = typeRegulations;
	}

	public static Logger getLogger() {
		return logger;
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

	public ReportIrgPenerbitanService getReportIrgPenerbitanService() {
		return reportIrgPenerbitanService;
	}

	public void setReportIrgPenerbitanService(ReportIrgPenerbitanService reportIrgPenerbitanService) {
		this.reportIrgPenerbitanService = reportIrgPenerbitanService;
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

	public static void setLogger(Logger logger) {
		ReportIrgPenerbitanBean.logger = logger;
	}

	public String getSearchUnitKerjaTpg() {
		return searchUnitKerjaTpg;
	}

	public void setSearchUnitKerjaTpg(String searchUnitKerjaTpg) {
		this.searchUnitKerjaTpg = searchUnitKerjaTpg;
	}

	public List<SelectItem> getUnitKerjaTpgs() {
		return unitKerjaTpgs;
	}

	public void setUnitKerjaTpgs(List<SelectItem> unitKerjaTpgs) {
		this.unitKerjaTpgs = unitKerjaTpgs;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public HolidayService getHolidayService() {
		return holidayService;
	}

	public void setHolidayService(HolidayService holidayService) {
		this.holidayService = holidayService;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}


}
