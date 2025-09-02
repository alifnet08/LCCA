package com.wo.module.report.reportSuratMasukDetail.bean;

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
import com.wo.module.documentType.service.DocumentTypeService;
import com.wo.module.parameter.model.ParameterHeader;
import com.wo.module.report.reportGen.constant.ReportGenConstant;
import com.wo.module.report.reportGen.model.ReportGen;
import com.wo.module.report.reportGen.model.ReportGenDetailTableModel;
import com.wo.module.report.reportGen.service.ReportGenService;
import com.wo.module.report.reportSuratMasukDetail.constant.ReportSuratMasukDetailConstants;
import com.wo.module.report.reportSuratMasukDetail.service.ReportSuratMasukDetailService;
import com.wo.module.report.reportSuratMasukDetail.task.ReportSuratMasukDetailTask;
import com.wo.module.report.reportSuratMasukRekap.service.ReportSuratMasukRekapService;
import com.wo.module.user.model.User;

public class ReportSuratMasukDetailBean extends CommonReportRunnableBean implements Serializable,
	ReportGenConstant, ReportSuratMasukDetailConstants{
	
	private static final long serialVersionUID = -5351179305200982154L;

	static Logger logger = Logger.getLogger(ReportSuratMasukDetailBean.class);
	
	private Date searchDateFrom;
	private Date searchDateTo;
	private Date searchTargetDateFrom;
	private Date searchTargetDateTo;
	private String searchSenderCode;
	
	private List<SelectItem> senderCodes;
	
	private DocumentTypeService documentTypeService;
	private ReportGenService reportGenService;
	private TaskExecutorBean taskExecutorBean;
	private ReportSuratMasukDetailService reportSuratMasukDetailService;
	private ReportSuratMasukRekapService reportSuratMasukRekapService;
	
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
		selectSenderCode();
	}
	
	private void selectSenderCode() {
		senderCodes = new ArrayList<SelectItem>();
		try {
			senderCodes = parameterDetailService.getListLabelValue(ParameterHeader.PARAM_HEAD_CODE_SENDER,
					false, true, facesUtil.retrieveDefaultLocale());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void search() {
		tableModel.setSearchCriteria(Arrays.asList(new DefaultSearchObject(CommonConstants.SEARCH_FILTER_BY_REPORT_CODE,REPORT_SURAT_MASUK_DETAIL)));
	}
	
	public void reset() {
		searchDateFrom = null;
		searchDateTo = null;
		searchSenderCode = null;
		searchTargetDateFrom= null;
		searchTargetDateTo = null;
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
				new DefaultSearchObject(WHERE_SENDER_CODE, searchSenderCode),
				new DefaultSearchObject(WHERE_TARGET_DATE_FROM, DateUtil.dateToStringYYYYMMDD(searchTargetDateFrom, false)),
				new DefaultSearchObject(WHERE_TARGET_DATE_TO, DateUtil.dateToStringYYYYMMDD(searchTargetDateTo, false)));
	}
	
	public void generateExcelInBackgroundJob() {
		if(searchDateFrom == null && searchDateTo == null && searchTargetDateFrom == null && searchTargetDateTo==null) {
			facesUtil.addErrMessage(
					facesUtil.retrieveMessage("formReportSuratMasukDetailCreationDate") + " "
					+ facesUtil.retrieveMessage("or") + " "
					+ facesUtil.retrieveMessage("formReportSuratMasukDetailTargetDate") + " "
					+ facesUtil.retrieveMessage("validateRequired"));
			return;
		}
		
		ReportGen newReportGen = saveReportGenHistoryAsInProgress();
		
		Long newReportGenId = newReportGen.getReportGenId();
		facesUtil.addSuccessMsg("Report Generated Id : [" + newReportGenId + "]");
		
		List<DefaultSearchObject> searchCriteria = buildSearchCriteriaReportGen();
		
		User user = facesUtil.getUserLogin();
		String userNikName = user.getNik() + " - " + user.getName();
		
		ReportSuratMasukDetailTask task = new ReportSuratMasukDetailTask(
				newReportGenId,
				reportGenService, 
				parameterDetailService, 
				runnableFacesUtil, 
				reportSuratMasukDetailService, 
				reportSuratMasukRekapService,
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
			newReportGen.setReportGenReportName(REPORT_SURAT_MASUK_DETAIL);
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
	public String getSearchSenderCode() {
		return searchSenderCode;
	}
	public void setSearchSenderCode(String searchSenderCode) {
		this.searchSenderCode = searchSenderCode;
	}
	public List<SelectItem> getSenderCodes() {
		return senderCodes;
	}
	public void setSenderCodes(List<SelectItem> senderCodes) {
		this.senderCodes = senderCodes;
	}
	public DocumentTypeService getDocumentTypeService() {
		return documentTypeService;
	}
	public void setDocumentTypeService(DocumentTypeService documentTypeService) {
		this.documentTypeService = documentTypeService;
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
	public ReportSuratMasukDetailService getReportSuratMasukDetailService() {
		return reportSuratMasukDetailService;
	}
	public void setReportSuratMasukDetailService(ReportSuratMasukDetailService reportSuratMasukDetailService) {
		this.reportSuratMasukDetailService = reportSuratMasukDetailService;
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

	public ReportGenDetailTableModel getTableModel() {
		return tableModel;
	}

	public void setTableModel(ReportGenDetailTableModel tableModel) {
		this.tableModel = tableModel;
	}

	public ReportSuratMasukRekapService getReportSuratMasukRekapService() {
		return reportSuratMasukRekapService;
	}

	public void setReportSuratMasukRekapService(ReportSuratMasukRekapService reportSuratMasukRekapService) {
		this.reportSuratMasukRekapService = reportSuratMasukRekapService;
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
}
