package com.wo.module.report.reportComplianceDocumentRekap.bean;

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
import com.wo.module.complianceReviewDocument.service.ComplianceReviewDocumentService;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;
import com.wo.module.report.reportComplianceDocumentRekap.constant.ReportComplianceDocumentRekapConstants;
import com.wo.module.report.reportComplianceDocumentRekap.service.ReportComplianceDocumentRekapService;
import com.wo.module.report.reportComplianceDocumentRekap.task.ReportComplianceDocumentRekapTask;
import com.wo.module.report.reportGen.constant.ReportGenConstant;
import com.wo.module.report.reportGen.model.ReportGen;
import com.wo.module.report.reportGen.model.ReportGenDetailTableModel;
import com.wo.module.report.reportGen.service.ReportGenService;
import com.wo.module.user.model.User;

public class ReportComplianceDocumentRekapBean extends CommonReportRunnableBean 
	implements Serializable, ReportGenConstant, ReportComplianceDocumentRekapConstants{

	private static final long serialVersionUID = -2602107154589158831L;

	static Logger logger = Logger.getLogger(ReportComplianceDocumentRekapBean.class);
	
	private Date searchDateFrom;
	private Date searchDateTo;
	private Date searchDocumentDateFrom;
	private Date searchDocumentDateTo;
	private String searchDocumentType;
	private String searchDocumentSubmitter;
	
	private List<SelectItem> documentTypes;
	private List<SelectItem> documentSubmitters;
	
	private ComplianceReviewDocumentService complianceReviewDocumentService;
	private ReportGenService reportGenService;
	private TaskExecutorBean taskExecutorBean;
	private ReportComplianceDocumentRekapService reportComplianceDocumentRekapService;
	
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
		selectComplianceDocument();
		selectComplianceDocumentSubmitter();
	}
	
	private void selectComplianceDocument() {
		documentTypes = new ArrayList<SelectItem>();
		
		try {
			List<ParameterDetail> pd = parameterDetailService.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_CODE_DOCUMENT_TYPE);
			for (int i = 0; i < pd.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((ParameterDetail) pd.get(i)).getName());
				si.setValue(((ParameterDetail) pd.get(i)).getParameterDtlCode());
				documentTypes.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void selectComplianceDocumentSubmitter() {
		documentSubmitters = new ArrayList<SelectItem>();
		
		try {
			List<ParameterDetail> pd = parameterDetailService.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_CODE_DOCUMENT_SUBMITTER);
			for (int i = 0; i < pd.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((ParameterDetail) pd.get(i)).getName());
				si.setValue(((ParameterDetail) pd.get(i)).getParameterDtlCode());
				documentSubmitters.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void search() {
		tableModel.setSearchCriteria(Arrays.asList(
				new DefaultSearchObject(SEARCH_FILTER_BY_REPORT_CODE, REPORT_COMPLIANCE_DOCUMENT_REKAP)));
	}
	
	public void reset() {
		searchDateFrom = null;
		searchDateTo = null;
		searchDocumentDateFrom = null;
		searchDocumentDateTo = null;
		searchDocumentType = null;
		searchDocumentSubmitter = null;
		
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
				new DefaultSearchObject(WHERE_DOCUMENT_DATE_FROM, DateUtil.dateToStringYYYYMMDD(searchDocumentDateFrom, false)),
				new DefaultSearchObject(WHERE_DOCUMENT_DATE_TO, DateUtil.dateToStringYYYYMMDD(searchDocumentDateTo, false)),
				new DefaultSearchObject(WHERE_DOCUMENT_TYPE, searchDocumentType),
				new DefaultSearchObject(WHERE_DOCUMENT_SUBMITTER, searchDocumentSubmitter));
	}
	
	public void generateExcelInBackgroundJob() {
		ReportGen newReportGen = saveReportGenHistoryAsInProgress(); 
		
		Long newReportGenId = newReportGen.getReportGenId();
		facesUtil.addSuccessMsg("Report Generated Id : [" + newReportGenId + "]");
		
		List<DefaultSearchObject> searchCriteria = buildSearchCriteriaReportGen();

		User user = facesUtil.getUserLogin();
		String userNikName = user.getNik() + " - " + user.getName();
		
		ReportComplianceDocumentRekapTask task = new ReportComplianceDocumentRekapTask(
				newReportGenId,
				reportGenService, 
				parameterDetailService, 
				runnableFacesUtil, 
				reportComplianceDocumentRekapService, 
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
					.setReportGenReportName(REPORT_COMPLIANCE_DOCUMENT_REKAP);
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

	public Date getSearchDocumentDateFrom() {
		return searchDocumentDateFrom;
	}

	public void setSearchDocumentDateFrom(Date searchDocumentDateFrom) {
		this.searchDocumentDateFrom = searchDocumentDateFrom;
	}

	public Date getSearchDocumentDateTo() {
		return searchDocumentDateTo;
	}

	public void setSearchDocumentDateTo(Date searchDocumentDateTo) {
		this.searchDocumentDateTo = searchDocumentDateTo;
	}

	public String getSearchDocumentType() {
		return searchDocumentType;
	}

	public void setSearchDocumentType(String searchDocumentType) {
		this.searchDocumentType = searchDocumentType;
	}

	public List<SelectItem> getDocumentTypes() {
		return documentTypes;
	}

	public void setDocumentTypes(List<SelectItem> documentTypes) {
		this.documentTypes = documentTypes;
	}

	public ComplianceReviewDocumentService getComplianceReviewDocumentService() {
		return complianceReviewDocumentService;
	}

	public void setComplianceReviewDocumentService(ComplianceReviewDocumentService complianceReviewDocumentService) {
		this.complianceReviewDocumentService = complianceReviewDocumentService;
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

	public ReportComplianceDocumentRekapService getReportComplianceDocumentRekapService() {
		return reportComplianceDocumentRekapService;
	}

	public void setReportComplianceDocumentRekapService(
			ReportComplianceDocumentRekapService reportComplianceDocumentRekapService) {
		this.reportComplianceDocumentRekapService = reportComplianceDocumentRekapService;
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

	public String getSearchDocumentSubmitter() {
		return searchDocumentSubmitter;
	}

	public void setSearchDocumentSubmitter(String searchDocumentSubmitter) {
		this.searchDocumentSubmitter = searchDocumentSubmitter;
	}

	public List<SelectItem> getDocumentSubmitters() {
		return documentSubmitters;
	}

	public void setDocumentSubmitters(List<SelectItem> documentSubmitters) {
		this.documentSubmitters = documentSubmitters;
	}
}
