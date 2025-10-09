package com.wo.module.report.reportRegulation.bean;

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
import com.wo.module.documentType.constant.DocumentTypeConstants;
import com.wo.module.documentType.model.DocumentType;
import com.wo.module.documentType.service.DocumentTypeService;
import com.wo.module.externalRegulation.constant.ExternalRegulationConstants;
import com.wo.module.internalRegulation.constant.InternalRegulationConstants;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;
import com.wo.module.report.reportGen.constant.ReportGenConstant;
import com.wo.module.report.reportGen.model.ReportGen;
import com.wo.module.report.reportGen.model.ReportGenDetailTableModel;
import com.wo.module.report.reportGen.service.ReportGenService;
import com.wo.module.report.reportRegulation.constant.ReportRegulationConstant;
import com.wo.module.report.reportRegulation.service.ReportRegulationService;
import com.wo.module.report.reportRegulation.task.ReportRegulationTask;
import com.wo.module.user.model.User;
import com.wo.module.user.service.UserService;

public class ReportRegulationBean extends CommonReportRunnableBean 
	implements Serializable{

	private static final long serialVersionUID = 976795816175549799L;
	private static final Logger logger = Logger.getLogger(ReportRegulationBean.class);
	
	private ReportGenService reportGenService;
	private TaskExecutorBean taskExecutorBean;
	private ReportRegulationService reportRegulationService;
	private DocumentTypeService documentTypeService;
	private UserService userService;
	
	private ReportGenDetailTableModel tableModel;
	private ReportGen[] selectedReportGen;
	private FileUtil fileUtil;
	
	private String searchJenisKetentuan;
	private String searchTipePeraturan;
	private Date searchPublishDateFrom;
	private Date searchPublishDateTo;
	private Date searchExpiredDateFrom;
	private Date searchExpiredDateTo;
	private String searchStatus;
	private String searchPublisherUnit;
	
	private List<SelectItem> selectJenisKetentuan;
	private List<SelectItem> selectStatus;
	private List<SelectItem> selectTipePeraturan;
	private List<SelectItem> selectDirectorat;
	
	@PostConstruct
	public void init() {
		super.init();
		initComponent();
		
		tableModel = new ReportGenDetailTableModel(reportGenService, paging);
//		search();
		fileUtil = FileUtil.getInstance();
	}
	
	private void initComponent() {
		initSelectJenisKetentuan();
//		selectTipePeraturran();
  	    initSelectStatus();
		initSelectDirectorate();
	}
	
	private void initSelectDirectorate() {
		selectDirectorat = new ArrayList<SelectItem>();
		try {
			List<String> getDirectorate = userService.getAllDirectorate();
			for (String vo : getDirectorate) {
				SelectItem si = new SelectItem();
				si.setLabel(vo);
				si.setValue(vo);
				
				selectDirectorat.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void initSelectJenisKetentuan() {
		selectJenisKetentuan = new ArrayList<SelectItem>();
		try {
			List<ParameterDetail> getJenisKetentutan = parameterDetailService.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_CODE_JENIS_KETENTUAN);
			for (ParameterDetail vo : getJenisKetentutan) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getName());
				si.setValue(vo.getParameterDtlCode());
				
				selectJenisKetentuan.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void selectTipePeraturran() {
		selectTipePeraturan = new ArrayList<SelectItem>();
		try {
			List<DocumentType> pd = documentTypeService.searchData(
					Arrays.asList(new DefaultSearchObject(DocumentTypeConstants.WHERE_JENIS_KETENTUAN_CODE,
							InternalRegulationConstants.PARAM_DTL_CODE_KETENTUAN_INTERNAL)),
					0, Integer.MAX_VALUE, null, null);
			for (int i = 0; i < pd.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((DocumentType) pd.get(i)).getDocumentType());
				si.setValue(((DocumentType) pd.get(i)).getDocumentType());
				selectTipePeraturan.add(si);
			}
			
			List<DocumentType> pd2 = documentTypeService.searchData(
					Arrays.asList(new DefaultSearchObject(DocumentTypeConstants.WHERE_JENIS_KETENTUAN_CODE,
							ExternalRegulationConstants.PARAMETER_DTL_CODE_KETENTUAN_EKSTERNAL)),
					0, Integer.MAX_VALUE, null, null);
			for (int i = 0; i < pd2.size(); i++) {
				SelectItem si2 = new SelectItem();
				si2.setLabel(((DocumentType) pd2.get(i)).getDocumentType());
				si2.setValue(((DocumentType) pd2.get(i)).getDocumentType());
				selectTipePeraturan.add(si2);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void initSelectStatus() {
		selectStatus = new ArrayList<SelectItem>();
		try {
			List<ParameterDetail> getStatus = parameterDetailService.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_CODE_DATA_STATUS);
			for (ParameterDetail vo : getStatus) {
				if (vo.getParameterDtlCode().equals(ParameterDetail.PARAM_DET_CODE_DATA_ACTIVE) 
						|| vo.getParameterDtlCode().equals(ParameterDetail.PARAM_DET_CODE_DATA_INACTIVE) ) {
					SelectItem si = new SelectItem();
					si.setLabel(vo.getName());
					si.setValue(vo.getParameterDtlCode());
					
					selectStatus.add(si);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void onChangeJenisKetentuan(){
		
		if(searchJenisKetentuan!= null && searchJenisKetentuan.equals(ExternalRegulationConstants.PARAMETER_DTL_CODE_KETENTUAN_EKSTERNAL)){
			selectTipePeraturan = new ArrayList<SelectItem>();
			List<DocumentType> pd2;
			try {
				pd2 = documentTypeService.searchData(
						Arrays.asList(new DefaultSearchObject(DocumentTypeConstants.WHERE_JENIS_KETENTUAN_CODE,
								ExternalRegulationConstants.PARAMETER_DTL_CODE_KETENTUAN_EKSTERNAL)),
						0, Integer.MAX_VALUE, null, null);
				for (int i = 0; i < pd2.size(); i++) {
					SelectItem si2 = new SelectItem();
					si2.setLabel(((DocumentType) pd2.get(i)).getDocumentType());
					si2.setValue(((DocumentType) pd2.get(i)).getDocumentType());
					selectTipePeraturan.add(si2);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		else if(searchJenisKetentuan!= null && searchJenisKetentuan.equals(InternalRegulationConstants.PARAM_DTL_CODE_KETENTUAN_INTERNAL)){
			selectTipePeraturan = new ArrayList<SelectItem>();
			List<DocumentType> pd2;
			try {
				pd2 = documentTypeService.searchData(
						Arrays.asList(new DefaultSearchObject(DocumentTypeConstants.WHERE_JENIS_KETENTUAN_CODE,
								InternalRegulationConstants.PARAM_DTL_CODE_KETENTUAN_INTERNAL)),
						0, Integer.MAX_VALUE, null, null);
				for (int i = 0; i < pd2.size(); i++) {
					SelectItem si2 = new SelectItem();
					si2.setLabel(((DocumentType) pd2.get(i)).getDocumentType());
					si2.setValue(((DocumentType) pd2.get(i)).getDocumentType());
					selectTipePeraturan.add(si2);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else{
			selectTipePeraturran();
		}
	}
	
	public void search() {
		tableModel.setSearchCriteria(Arrays.asList(new DefaultSearchObject(CommonConstants.SEARCH_FILTER_BY_REPORT_CODE, ReportGenConstant.REPORT_REGULATION)));
		
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	public void reset() {
		searchJenisKetentuan = "";
		searchTipePeraturan = "";
		searchStatus = "";
		searchPublisherUnit = "";
		searchPublishDateFrom = null;
		searchPublishDateTo = null;
		searchExpiredDateFrom = null;
		searchExpiredDateTo = null;
		
		search();
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	public void delete() {
		if (selectedReportGen == null) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("validateDeleteMinOneData"));
		}
		
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
				new DefaultSearchObject(ReportRegulationConstant.SEARCH_BY_JENIS_PERATURAN, searchJenisKetentuan),
				new DefaultSearchObject(ReportRegulationConstant.SEARCH_BY_TIPE_PERATURAN, searchTipePeraturan),
				new DefaultSearchObject(ReportRegulationConstant.SEARCH_BY_PUBLISH_DATE_FROM, DateUtil.dateToStringYYYYMMDD(searchPublishDateFrom, false)),
				new DefaultSearchObject(ReportRegulationConstant.SEARCH_BY_PUBLISH_DATE_TO, DateUtil.dateToStringYYYYMMDD(searchPublishDateTo, false)),
				new DefaultSearchObject(ReportRegulationConstant.SEARCH_BY_EXPIRED_DATE_FROM, DateUtil.dateToStringYYYYMMDD(searchExpiredDateFrom, false)),
				new DefaultSearchObject(ReportRegulationConstant.SEARCH_BY_EXPIRED_DATE_TO, DateUtil.dateToStringYYYYMMDD(searchExpiredDateTo, false)),
				new DefaultSearchObject(ReportRegulationConstant.SEARCH_BY_STATUS, searchStatus),
				new DefaultSearchObject(ReportRegulationConstant.SEARCH_BY_UNIT_PENGUSUL, searchPublisherUnit));
	}
	
	public void generateExcelInBackgroundJob() {
		ReportGen newReportGen = saveReportGenHistoryAsInProgress();
		
		Long newReportGenId = newReportGen.getReportGenId();
		facesUtil.addSuccessMsg("Report Generate Id : [" + newReportGenId +"]");
		List<DefaultSearchObject> searchCriteria = buildSearchCriteriaReportGen();
		
		User user = facesUtil.getUserLogin();
		String userNikName = user.getNik() + " - " + user.getName();
		
		ReportRegulationTask task = new ReportRegulationTask(
				newReportGenId,
				reportGenService,
				parameterDetailService,
				runnableFacesUtil, 
				reportRegulationService, 
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
			newReportGen.setReportGenReportName(ReportGenConstant.REPORT_REGULATION);
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
	public ReportRegulationService getReportRegulationService() {
		return reportRegulationService;
	}
	public void setReportRegulationService(ReportRegulationService reportRegulationService) {
		this.reportRegulationService = reportRegulationService;
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
	public String getSearchJenisKetentuan() {
		return searchJenisKetentuan;
	}
	public void setSearchJenisKetentuan(String searchJenisKetentuan) {
		this.searchJenisKetentuan = searchJenisKetentuan;
	}
	public Date getSearchPublishDateFrom() {
		return searchPublishDateFrom;
	}
	public void setSearchPublishDateFrom(Date searchPublishDateFrom) {
		this.searchPublishDateFrom = searchPublishDateFrom;
	}
	public Date getSearchPublishDateTo() {
		return searchPublishDateTo;
	}
	public void setSearchPublishDateTo(Date searchPublishDateTo) {
		this.searchPublishDateTo = searchPublishDateTo;
	}
	public Date getSearchExpiredDateFrom() {
		return searchExpiredDateFrom;
	}
	public void setSearchExpiredDateFrom(Date searchExpiredDateFrom) {
		this.searchExpiredDateFrom = searchExpiredDateFrom;
	}
	public Date getSearchExpiredDateTo() {
		return searchExpiredDateTo;
	}
	public void setSearchExpiredDateTo(Date searchExpiredDateTo) {
		this.searchExpiredDateTo = searchExpiredDateTo;
	}
	public String getSearchStatus() {
		return searchStatus;
	}
	public void setSearchStatus(String searchStatus) {
		this.searchStatus = searchStatus;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public static Logger getLogger() {
		return logger;
	}

	public List<SelectItem> getSelectJenisKetentuan() {
		return selectJenisKetentuan;
	}

	public void setSelectJenisKetentuan(List<SelectItem> selectJenisKetentuan) {
		this.selectJenisKetentuan = selectJenisKetentuan;
	}

	public List<SelectItem> getSelectStatus() {
		return selectStatus;
	}

	public void setSelectStatus(List<SelectItem> selectStatus) {
		this.selectStatus = selectStatus;
	}

	public String getSearchPublisherUnit() {
		return searchPublisherUnit;
	}

	public void setSearchPublisherUnit(String searchPublisherUnit) {
		this.searchPublisherUnit = searchPublisherUnit;
	}

	public DocumentTypeService getDocumentTypeService() {
		return documentTypeService;
	}

	public void setDocumentTypeService(DocumentTypeService documentTypeService) {
		this.documentTypeService = documentTypeService;
	}

	public List<SelectItem> getSelectTipePeraturan() {
		return selectTipePeraturan;
	}

	public void setSelectTipePeraturan(List<SelectItem> selectTipePeraturan) {
		this.selectTipePeraturan = selectTipePeraturan;
	}

	public String getSearchTipePeraturan() {
		return searchTipePeraturan;
	}

	public void setSearchTipePeraturan(String searchTipePeraturan) {
		this.searchTipePeraturan = searchTipePeraturan;
	}

	public List<SelectItem> getSelectDirectorat() {
		return selectDirectorat;
	}

	public void setSelectDirectorat(List<SelectItem> selectDirectorat) {
		this.selectDirectorat = selectDirectorat;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	
	
}
