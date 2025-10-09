package com.wo.module.report.reportAuditRekap.bean;

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
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.report.reportAuditRekap.constant.ReportAuditRekapConstants;
import com.wo.module.report.reportAuditRekap.service.ReportAuditRekapService;
import com.wo.module.report.reportAuditRekap.task.ReportAuditRekapTask;
import com.wo.module.report.reportGen.constant.ReportGenConstant;
import com.wo.module.report.reportGen.model.ReportGen;
import com.wo.module.report.reportGen.model.ReportGenDetailTableModel;
import com.wo.module.report.reportGen.service.ReportGenService;
import com.wo.module.trcAudit.service.TrcAuditService;
import com.wo.module.user.model.User;

public class ReportAuditRekapBean extends CommonReportRunnableBean implements Serializable, ReportGenConstant, ReportAuditRekapConstants{

	private static final long serialVersionUID = -2353210983011665243L;
	
	static Logger logger = Logger.getLogger(ReportAuditRekapBean.class);
	
	private Date searchDateFrom;
	private Date searchDateTo;
	private String searchAuditor;
	private String searchFindingName;
	
	private String findingNameIn;
	private String findingNameEn;
	
	private List<SelectItem> auditorCategorys;
	
	private TrcAuditService trcAuditService;
	private ReportGenService reportGenService;
	private TaskExecutorBean taskExecutorBean;
	private ReportAuditRekapService reportAuditRekapService;

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
		selectAudit();
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

	public void search() {
		tableModel.setSearchCriteria(Arrays.asList(
				new DefaultSearchObject(SEARCH_FILTER_BY_REPORT_CODE, REPORT_AUDIT_REKAP)));
	}
	
	public void reset() {
		searchDateFrom = null;
		searchDateTo = null;
		searchAuditor = null;
		
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
				new DefaultSearchObject(WHERE_CREATION_DATE_FROM, DateUtil.dateToStringYYYYMMDD(searchDateFrom, false)),
				new DefaultSearchObject(WHERE_CREATION_DATE_TO, DateUtil.dateToStringYYYYMMDD(searchDateTo, false)),
				new DefaultSearchObject(WHERE_AUDIT_TYPE, searchAuditor));
	}
	
	public void generateExcelInBackgroundJob() {
		ReportGen newReportGen = saveReportGenHistoryAsInProgress(); 
		
		Long newReportGenId = newReportGen.getReportGenId();
		facesUtil.addSuccessMsg("Report Generated Id : [" + newReportGenId + "]");
		
		List<DefaultSearchObject> searchCriteria = buildSearchCriteriaReportGen();

		User user = facesUtil.getUserLogin();
		String userNikName = user.getNik() + " - " + user.getName();
		
		ReportAuditRekapTask task = new ReportAuditRekapTask(
				newReportGenId,
				reportGenService,
				parameterDetailService,
				runnableFacesUtil,
				reportAuditRekapService,
				searchCriteria,
				userNikName,
				this.findingNameIn,
				this.findingNameEn);
		
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
					.setReportGenReportName(REPORT_AUDIT_REKAP);
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
	
	@SuppressWarnings("static-access")
	public void getLocaleName() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		this.findingNameIn = "";
		
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

	public ReportAuditRekapService getReportAuditRekapService() {
		return reportAuditRekapService;
	}

	public void setReportAuditRekapService(ReportAuditRekapService reportAuditRekapService) {
		this.reportAuditRekapService = reportAuditRekapService;
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

	public String getSearchFindingName() {
		return searchFindingName;
	}

	public void setSearchFindingName(String searchFindingName) {
		this.searchFindingName = searchFindingName;
	}
	
}
