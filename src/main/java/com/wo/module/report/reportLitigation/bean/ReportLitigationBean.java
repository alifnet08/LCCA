package com.wo.module.report.reportLitigation.bean;

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
import com.wo.module.common.constant.Constants;
import com.wo.module.common.paging.DefaultSearchObject;
import com.wo.module.common.task.TaskExecutorBean;
import com.wo.module.common.util.DateUtil;
import com.wo.module.common.util.FileUtil;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.documentType.service.DocumentTypeService;
import com.wo.module.lov.bean.RunnableFacesUtil;
import com.wo.module.report.reportGen.constant.ReportGenConstant;
import com.wo.module.report.reportGen.model.ReportGen;
import com.wo.module.report.reportGen.model.ReportGenDetailTableModel;
import com.wo.module.report.reportGen.service.ReportGenService;
import com.wo.module.report.reportLitigation.constant.ReportLitigationConstant;
import com.wo.module.report.reportLitigation.service.ReportLitigationService;
import com.wo.module.report.reportLitigation.task.ReportLitigationTask;
import com.wo.module.user.model.User;

public class ReportLitigationBean extends CommonReportRunnableBean implements Serializable{

	private static final long serialVersionUID = 2786909546005022939L;
	private static final Logger logger = Logger.getLogger(ReportLitigationBean.class);
	
	private DocumentTypeService documentTypeService;
	private ReportGenService reportGenService;
	private ReportLitigationService reportLitigationService;
	
	private TaskExecutorBean taskExecutorBean;
	private ReportGenDetailTableModel tableModel;
	private ReportGen[] selectedReportGen;
	private FileUtil fileUtil;
	private RunnableFacesUtil runnableFacesUtil;
	
	private String searchStatus;
	private Date searchTanggalPembuatanFrom;
	private Date searchTanggalPembuatanTo;
	
	private List<SelectItem> statusSelectItemList;

	@PostConstruct
	public void init() {
		super.init();
		initComponent();
		tableModel = new ReportGenDetailTableModel(reportGenService, paging);
		search();
		fileUtil = FileUtil.getInstance();
	}
	
	public void reset(ActionEvent event) {
		searchStatus = null;
		searchTanggalPembuatanFrom = null;
		searchTanggalPembuatanTo = null;
		
		search();
		
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	private void initComponent() {
		statusSelectItemList = new ArrayList<>();
		
		statusSelectItemList.add(new SelectItem(Constants.CONSTANT_YES, facesUtil.retrieveMessage("formActive")));
		statusSelectItemList.add(new SelectItem(Constants.CONSTANT_NO, facesUtil.retrieveMessage("formInactive")));
		
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	public void delete() {
		if(selectedReportGen == null) facesUtil.addErrMessage(facesUtil.retrieveMessage("validateDeleteMinOneData"));
		
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
				new DefaultSearchObject(ReportLitigationConstant.SEARCH_TANGGAL_PEMBUATAN_FROM, DateUtil.dateToStringYYYYMMDD(searchTanggalPembuatanFrom, false)),
				new DefaultSearchObject(ReportLitigationConstant.SEARCH_TANGGAL_PEMBUATAN_TO, DateUtil.dateToStringYYYYMMDD(searchTanggalPembuatanTo, false)),
				new DefaultSearchObject(ReportLitigationConstant.SEARCH_STATUS, searchStatus));
	}
	
	public void search() {
		tableModel.setSearchCriteria(Arrays.asList(new DefaultSearchObject(CommonConstants.SEARCH_FILTER_BY_REPORT_CODE, ReportGenConstant.REPORT_LITIGATION)));
		
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	private ReportGen saveReportGenHistoryAsInProgress() {
		try {
			Timestamp currentDate = new Timestamp(new Date().getTime());
			ReportGen newReportGen = new ReportGen();

			User user = facesUtil.getUserLogin();

			newReportGen.setReportGenNik(user.getNik());
			newReportGen.setReportGenEmployeeName(user.getName());
			newReportGen.setReportGenReportName(ReportGenConstant.REPORT_LITIGATION);
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
		
		
		ReportLitigationTask task = new ReportLitigationTask(
				newReportGenId,
				reportGenService,
				parameterDetailService,
				runnableFacesUtil, 
				reportLitigationService, 
				searchCriteria, 
				userNikName, userNikName);
		
		taskExecutorBean.submitTask(task);
	}
	
	public void downloadFile(String fileId, String fileName, byte[] content) throws Exception {
		CallApiManager.downloadFile(fileId, fileName, content, parameterDetailService);
		
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

	public ReportLitigationService getReportLitigationService() {
		return reportLitigationService;
	}

	public void setReportLitigationService(ReportLitigationService reportLitigationService) {
		this.reportLitigationService = reportLitigationService;
	}

	public TaskExecutorBean getTaskExecutorBean() {
		return taskExecutorBean;
	}

	public void setTaskExecutorBean(TaskExecutorBean taskExecutorBean) {
		this.taskExecutorBean = taskExecutorBean;
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

	public RunnableFacesUtil getRunnableFacesUtil() {
		return runnableFacesUtil;
	}

	public void setRunnableFacesUtil(RunnableFacesUtil runnableFacesUtil) {
		this.runnableFacesUtil = runnableFacesUtil;
	}

	public String getSearchStatus() {
		return searchStatus;
	}

	public void setSearchStatus(String searchStatus) {
		this.searchStatus = searchStatus;
	}

	public Date getSearchTanggalPembuatanFrom() {
		return searchTanggalPembuatanFrom;
	}

	public void setSearchTanggalPembuatanFrom(Date searchTanggalPembuatanFrom) {
		this.searchTanggalPembuatanFrom = searchTanggalPembuatanFrom;
	}

	public Date getSearchTanggalPembuatanTo() {
		return searchTanggalPembuatanTo;
	}

	public void setSearchTanggalPembuatanTo(Date searchTanggalPembuatanTo) {
		this.searchTanggalPembuatanTo = searchTanggalPembuatanTo;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static Logger getLogger() {
		return logger;
	}

	public List<SelectItem> getStatusSelectItemList() {
		return statusSelectItemList;
	}

	public void setStatusSelectItemList(List<SelectItem> statusSelectItemList) {
		this.statusSelectItemList = statusSelectItemList;
	}
	
}