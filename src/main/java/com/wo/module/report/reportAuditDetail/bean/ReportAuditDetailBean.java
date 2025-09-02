package com.wo.module.report.reportAuditDetail.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
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
import com.wo.module.mstAudit.model.MstAudit;
import com.wo.module.mstAudit.service.MstAuditService;
import com.wo.module.mstAudit.vo.MstAuditVO;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.report.reportAuditDetail.constant.ReportAuditDetailConstants;
import com.wo.module.report.reportAuditDetail.service.ReportAuditDetailService;
import com.wo.module.report.reportAuditDetail.task.ReportAuditDetailTask;
import com.wo.module.report.reportAuditRekap.service.ReportAuditRekapService;
import com.wo.module.report.reportGen.constant.ReportGenConstant;
import com.wo.module.report.reportGen.model.ReportGen;
import com.wo.module.report.reportGen.model.ReportGenDetailTableModel;
import com.wo.module.report.reportGen.service.ReportGenService;
import com.wo.module.trcAudit.service.TrcAuditPICFollowupService;
import com.wo.module.trcAudit.service.TrcAuditService;
import com.wo.module.user.model.Division;
import com.wo.module.user.model.User;
import com.wo.module.user.service.UserService;

public class ReportAuditDetailBean extends CommonReportRunnableBean implements Serializable, ReportAuditDetailConstants, ReportGenConstant{

	private static final long serialVersionUID = -2562717509642142997L;

	static Logger logger = Logger.getLogger(ReportAuditDetailBean.class);
	
	private Date searchTargetDateFrom;
	private Date searchTargetDateTo;
	private String searchAuditor;
	private Date searchAuditDateFrom;
	private Date searchAuditDateTo;
	private Long divisionId;
	private String searchStatusFollowup;
	private String searchStatusVerifikasi;
	private String searchFindingName;
	private String searchAuditTemplateName;
	private Long searchMstAuditId;
	
	private String getTemplateName;
	private String findingNameIn;
	private String findingNameEn;
	
	private List<SelectItem> auditorCategorys;
	private List<SelectItem> divisions;
	private List<SelectItem> statusFollowups;
	private List<SelectItem> statusVerifications;
	private List<SelectItem> auditTemplateNameList;
	
	private TrcAuditService trcAuditService;
	private ReportGenService reportGenService;
	private TaskExecutorBean taskExecutorBean;
	private ReportAuditDetailService reportAuditDetailService;
	private ReportAuditRekapService reportAuditRekapService;
	private TrcAuditPICFollowupService trcAuditPICFollowupService;
	private UserService userService;
	private MstAuditService mstAuditService;
	
	private ReportGenDetailTableModel tableModel;
	private ReportGen[] selectedReportGen;
	private FileUtil fileUtil;
	
	@PostConstruct
	public void init () {
		super.init();
		selectComponent();
		tableModel = new ReportGenDetailTableModel(reportGenService, paging);
		search();
		fileUtil = FileUtil.getInstance();
	}
	
	private void selectComponent() {
		selectAudit();
		selectDivision();
		selectStatusFollowup();
		selectStatusVerification();
		selectAuditTemplateName();
	}
	
	private void selectAudit() {
		auditorCategorys = new ArrayList<SelectItem>();
		try {
			List<ParameterDetail> pd = parameterDetailService.getParameterDetailByParamCode("AUDITOR");
			for (int i = 0; i < pd.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((ParameterDetail) pd.get(i)).getName());
				si.setValue(((ParameterDetail) pd.get(i)).getParameterDtlCode());
				auditorCategorys.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void selectDivision() {
		divisions = new ArrayList<SelectItem>();
		try {
			List<Division> pd = userService.getAllDivision();
			for (Division vo : pd) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getDivisionName());
				si.setValue(vo.getDivisionId());
				divisions.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	private void selectStatusFollowup() {
		statusFollowups = new ArrayList<SelectItem>();
		try {
			List<ParameterDetail> pd = parameterDetailService.getParameterDetailByParamCode("PIC_FOLLOWUP_STATUS");
			for (int i = 0; i < pd.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((ParameterDetail) pd.get(i)).getName());
				si.setValue(((ParameterDetail) pd.get(i)).getParameterDtlCode());
				statusFollowups.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void selectStatusVerification() {
		statusVerifications = new ArrayList<SelectItem>();
		try {
			List<ParameterDetail> pd = parameterDetailService.getParameterDetailByParamCode("COMPLIANCE_CHECK_STATUS");
			for (int i = 0; i < pd.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((ParameterDetail) pd.get(i)).getName());
				si.setValue(((ParameterDetail) pd.get(i)).getParameterDtlCode());
				statusVerifications.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void selectAuditTemplateName(){
		auditTemplateNameList = new ArrayList<SelectItem>();
		try {
			List<MstAudit> listMstAudit = mstAuditService.getAllMstAuditData();
			for (int i = 0; i < listMstAudit.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel((listMstAudit).get(i).getAuditTemplate());
				si.setValue((listMstAudit).get(i).getMstAuditId());
				auditTemplateNameList.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void search() {
		tableModel.setSearchCriteria(Arrays.asList(
				new DefaultSearchObject(SEARCH_FILTER_BY_REPORT_CODE, REPORT_AUDIT_DETAIL)));
		
		PrimeFaces.current().executeScript("reInitSelect2();");
	}

	public void reset() {
		searchAuditor = null;
		searchTargetDateFrom= null;
		searchTargetDateTo = null;
		searchAuditDateFrom = null;
		searchAuditDateTo = null;
		searchStatusFollowup = null;
		searchStatusVerifikasi = null;
		divisionId = null;
		searchFindingName = null;
		searchMstAuditId = null;
		
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
				new DefaultSearchObject(WHERE_AUDITOR, searchAuditor),
				new DefaultSearchObject(WHERE_TARGET_DATE_FROM, DateUtil.dateToStringYYYYMMDD(searchTargetDateFrom, false)),
				new DefaultSearchObject(WHERE_TARGET_DATE_TO, DateUtil.dateToStringYYYYMMDD(searchTargetDateTo, false)),
				new DefaultSearchObject(WHERE_AUDIT_DATE_FROM, DateUtil.dateToStringYYYYMMDD(searchAuditDateFrom, false)),
				new DefaultSearchObject(WHERE_AUDIT_DATE_TO, DateUtil.dateToStringYYYYMMDD(searchAuditDateTo, false)),
				new DefaultSearchObject(WHERE_DIVISION_ID, divisionId),
				new DefaultSearchObject(WHERE_STATUS_FOLLOWUP, searchStatusFollowup),
				new DefaultSearchObject(WHERE_STATUS_VERIFICATION, searchStatusVerifikasi),
//				new DefaultSearchObject(WHERE_FINDING_NAME, searchFindingName),
				new DefaultSearchObject(WHERE_MST_AUDIT_ID, searchMstAuditId));
	}
	
	public void generateExcelInBackgroundJob() {
		if(searchAuditDateFrom == null && searchAuditDateTo == null && searchTargetDateFrom == null && searchTargetDateTo==null) {
			facesUtil.addErrMessage(
					facesUtil.retrieveMessage("formReportAuditDetailAuditDate") + " "
					+ facesUtil.retrieveMessage("or") + " "
					+ facesUtil.retrieveMessage("formReportAuditDetailTargetDate") + " "
					+ facesUtil.retrieveMessage("validateRequired"));
			return;
		}
		
		ReportGen newReportGen = saveReportGenHistoryAsInProgress(); 
		
		Long newReportGenId = newReportGen.getReportGenId();
		facesUtil.addSuccessMsg("Report Generated Id : [" + newReportGenId + "]");
		
		List<DefaultSearchObject> searchCriteria = buildSearchCriteriaReportGen();

		User user = facesUtil.getUserLogin();
		String userNikName = user.getNik() + " - " + user.getName();
		
		String getDivisionName = "";
		
		if (divisionId != null && divisionId > 0) {
			getDivisionName = userService.getDivisionNameByDivisionId(divisionId);
		}
		
		getLocaleName();
		
		ReportAuditDetailTask task = new ReportAuditDetailTask(
				newReportGenId, 
				reportGenService, 
				parameterDetailService, 
				runnableFacesUtil, 
				reportAuditDetailService,
				reportAuditRekapService,
				searchCriteria, 
				userNikName,
				trcAuditPICFollowupService,
				getDivisionName,
				this.getTemplateName,
				this.findingNameIn,
				this.findingNameEn);
		
		taskExecutorBean.submitTask(task);
		
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	private ReportGen saveReportGenHistoryAsInProgress() {
		try {
			
			Timestamp currentDate = new Timestamp(new Date().getTime());
			ReportGen newReportGen = new ReportGen();

			User user = facesUtil.getUserLogin();

			newReportGen.setReportGenNik(user.getNik());
			newReportGen.setReportGenEmployeeName(user.getName());
			newReportGen
					.setReportGenReportName(REPORT_AUDIT_DETAIL);
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
	
	@SuppressWarnings("static-access")
	public void getLocaleName() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		
		this.getTemplateName = "";
		this.findingNameIn = "";
		this.findingNameEn = "";
		
		if (searchMstAuditId != null && searchMstAuditId > 0) {
			MstAuditVO getAuditTemplateName = mstAuditService.getSingleDataMstAudit(searchMstAuditId);
			
			if (getAuditTemplateName != null) {
				this.getTemplateName = getAuditTemplateName.getAuditTemplateName();
			}
		}
		
		if (locale != null && locale.equals(locale.ENGLISH)) {
			if (searchFindingName != null && !searchFindingName.isEmpty()) {
				this.findingNameEn = searchFindingName;
			} else {
				this.findingNameEn = "";
			}
		} else {
			if (searchFindingName != null && !searchFindingName.isEmpty()) {
				this.findingNameIn = searchFindingName;
			} else {
				this.findingNameIn = "";
			}
		}
		
	}
	
	public void downloadFile(String fileId, String fileName, byte[] content) throws Exception {
		CallApiManager.downloadFile(fileId, fileName, content, parameterDetailService);
	}
	
	public String getSearchAuditor() {
		return searchAuditor;
	}

	public void setSearchAuditor(String searchAuditor) {
		this.searchAuditor = searchAuditor;
	}

	public List<SelectItem> getAuditorCategorys() {
		return auditorCategorys;
	}

	public void setAuditorCategorys(List<SelectItem> auditorCategorys) {
		this.auditorCategorys = auditorCategorys;
	}

	public TrcAuditService getTrcAuditService() {
		return trcAuditService;
	}

	public void setTrcAuditService(TrcAuditService trcAuditService) {
		this.trcAuditService = trcAuditService;
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

	public ReportAuditDetailService getReportAuditDetailService() {
		return reportAuditDetailService;
	}

	public void setReportAuditDetailService(ReportAuditDetailService reportAuditDetailService) {
		this.reportAuditDetailService = reportAuditDetailService;
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

	public TrcAuditPICFollowupService getTrcAuditPICFollowupService() {
		return trcAuditPICFollowupService;
	}

	public void setTrcAuditPICFollowupService(TrcAuditPICFollowupService trcAuditPICFollowupService) {
		this.trcAuditPICFollowupService = trcAuditPICFollowupService;
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

	public ReportAuditRekapService getReportAuditRekapService() {
		return reportAuditRekapService;
	}

	public void setReportAuditRekapService(ReportAuditRekapService reportAuditRekapService) {
		this.reportAuditRekapService = reportAuditRekapService;
	}

	public Date getSearchAuditDateFrom() {
		return searchAuditDateFrom;
	}

	public void setSearchAuditDateFrom(Date searchAuditDateFrom) {
		this.searchAuditDateFrom = searchAuditDateFrom;
	}

	public Date getSearchAuditDateTo() {
		return searchAuditDateTo;
	}

	public void setSearchAuditDateTo(Date searchAuditDateTo) {
		this.searchAuditDateTo = searchAuditDateTo;
	}

	public List<SelectItem> getDivisions() {
		return divisions;
	}

	public void setDivisions(List<SelectItem> divisions) {
		this.divisions = divisions;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public Long getDivisionId() {
		return divisionId;
	}

	public void setDivisionId(Long divisionId) {
		this.divisionId = divisionId;
	}

	public String getSearchStatusFollowup() {
		return searchStatusFollowup;
	}

	public void setSearchStatusFollowup(String searchStatusFollowup) {
		this.searchStatusFollowup = searchStatusFollowup;
	}

	public String getSearchStatusVerifikasi() {
		return searchStatusVerifikasi;
	}

	public void setSearchStatusVerifikasi(String searchStatusVerifikasi) {
		this.searchStatusVerifikasi = searchStatusVerifikasi;
	}

	public List<SelectItem> getStatusFollowups() {
		return statusFollowups;
	}

	public void setStatusFollowups(List<SelectItem> statusFollowups) {
		this.statusFollowups = statusFollowups;
	}

	public List<SelectItem> getStatusVerifications() {
		return statusVerifications;
	}

	public void setStatusVerifications(List<SelectItem> statusVerifications) {
		this.statusVerifications = statusVerifications;
	}

	public String getSearchFindingName() {
		return searchFindingName;
	}

	public void setSearchFindingName(String searchFindingName) {
		this.searchFindingName = searchFindingName;
	}

	public String getSearchAuditTemplateName() {
		return searchAuditTemplateName;
	}

	public void setSearchAuditTemplateName(String searchAuditTemplateName) {
		this.searchAuditTemplateName = searchAuditTemplateName;
	}

	public Long getSearchMstAuditId() {
		return searchMstAuditId;
	}

	public void setSearchMstAuditId(Long searchMstAuditId) {
		this.searchMstAuditId = searchMstAuditId;
	}

	public List<SelectItem> getAuditTemplateNameList() {
		return auditTemplateNameList;
	}

	public void setAuditTemplateNameList(List<SelectItem> auditTemplateNameList) {
		this.auditTemplateNameList = auditTemplateNameList;
	}

	public MstAuditService getMstAuditService() {
		return mstAuditService;
	}

	public void setMstAuditService(MstAuditService mstAuditService) {
		this.mstAuditService = mstAuditService;
	}

	public String getGetTemplateName() {
		return getTemplateName;
	}

	public void setGetTemplateName(String getTemplateName) {
		this.getTemplateName = getTemplateName;
	}

	public String getFindingNameIn() {
		return findingNameIn;
	}

	public void setFindingNameIn(String findingNameIn) {
		this.findingNameIn = findingNameIn;
	}

	public String getFindingNameEn() {
		return findingNameEn;
	}

	public void setFindingNameEn(String findingNameEn) {
		this.findingNameEn = findingNameEn;
	}
	
}
