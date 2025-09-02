package com.wo.module.dbCompliance.bean;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.CommonConstants;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.model.UploadedFileWO;
import com.wo.module.common.util.FileUtil;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.dbCompliance.constant.DBComplianceConstants;
import com.wo.module.dbCompliance.model.DBCompliance;
import com.wo.module.dbCompliance.model.DBComplianceDoc;
import com.wo.module.dbCompliance.model.DBComplianceDueDate;
import com.wo.module.dbCompliance.model.DBComplianceDueDateTableModel;
import com.wo.module.dbCompliance.service.DBComplianceService;
import com.wo.module.externalRegulation.model.Regulation;
import com.wo.module.internalRegulation.constant.InternalRegulationConstants;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.outgoingLetter.constant.OutgoingLetterConstants;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;
import com.wo.module.reportType.model.ReportType;
import com.wo.module.reportType.service.ReportTypeService;
import com.wo.module.tmpRmd.model.TmpRmdDueDate;
import com.wo.module.tmpRmd.model.TmpRmdDueDateTableModel;

public class DBComplianceEditBean extends CommonBean implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(DBComplianceEditBean.class);

	private DBCompliance dbCompliance;

	private Boolean isViewOnly;

	private String actionMode;
	private String dueDateType;

	private String editedId;
	private String textWarningUpload;

	private DBComplianceService dbComplianceService;
	
	private ReportTypeService reportTypeService;

	public FacesUtil facesUtil;
	
	private Integer lastSequenceOfDtl;

	private List<SelectItem> nameList;
	private List<SelectItem> slaTypeList;
	private List<SelectItem> reportTypeList;
	private List<SelectItem> dayList;
	private List<SelectItem> dateList;
	
	private List<UploadedFileWO> uploadFiles;
	private List<UploadedFileWO> deleteFiles;
	
	private DBComplianceDueDate[] selectedDueDateData;
	private DBComplianceDueDateTableModel<DBComplianceDueDate> tableDueDateModel;
	
	private Integer lastSequenceOfDueDate;
	
	private FileUtil fileUtil;

	private boolean checkAll;

	private String navigateSeadbComplianceh = DBComplianceConstants.NAVIGATE_SEARCH;

	public void addMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void addErrMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	@PostConstruct
	public void init() {
		super.init();
		initList();
		checkNewOrEdit();
		fileUtil = FileUtil.getInstance();
		
		try {
			ParameterDetail getText = parameterDetailService.getParameterDetailByParamDtlCode("UPLOAD_WARNING_TEXT");
			textWarningUpload = getText.getName();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void initList(){
		try {
			reportTypeList = new ArrayList<SelectItem>();
			List<ReportType> listReportType = reportTypeService.getAllReportType();
			

			for (ReportType vo : listReportType) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getReportTypeIn());
				si.setValue(vo.getReportTypeId());
				reportTypeList.add(si);
			}
		
		dayList = new ArrayList<SelectItem>();
		dayList.add(new SelectItem("1", facesUtil.getResource("formSunday")));
		dayList.add(new SelectItem("2", facesUtil.getResource("formMonday")));
		dayList.add(new SelectItem("3", facesUtil.getResource("formTuesday")));
		dayList.add(new SelectItem("4", facesUtil.getResource("formWednesday")));
		dayList.add(new SelectItem("5", facesUtil.getResource("formThursday")));
		dayList.add(new SelectItem("6", facesUtil.getResource("formFriday")));
		dayList.add(new SelectItem("7", facesUtil.getResource("formSaturday")));

		dateList = new ArrayList<SelectItem>();
		for (int i = 1; i <= 31; i++) {
			dateList.add(new SelectItem(String.valueOf(i), String.valueOf(i)));
		}
		
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


	
	private void checkNewOrEdit() {
		try {
			String editId = facesUtil.retrieveRequestParam("id");String token = facesUtil.retrieveRequestParam("token"); 
			
			String viewId = facesUtil.retrieveRequestParam("viewId");
			isViewOnly = false;
			if (viewId != null && !viewId.isEmpty()) {
				if (viewId.trim().equalsIgnoreCase("true")) {
					isViewOnly = true;
				}
			}
			if (StringUtils.isBlank(editId) && StringUtils.isBlank(token)) {
				this.handleNew();

			} else {
				this.handleEdit(editId);
			}
		} catch (Exception e) {

		}

	}

	private void handleNew() {
		dbCompliance = new DBCompliance();
		ReportType rt = new ReportType();
		dbCompliance.setReportType(rt);
		lastSequenceOfDtl = 0;
		actionMode = Constants.ACTION_ADD;
		uploadFiles = new ArrayList<UploadedFileWO>();
		//onChangeReportTypeNow();
		if (dbCompliance.getDbComplianceDuedates() == null || dbCompliance.getDbComplianceDuedates().size() == 0) {
			dbCompliance.setDbComplianceDuedates(new ArrayList<DBComplianceDueDate>());
			lastSequenceOfDueDate = 0;
		}
		tableDueDateModel = new DBComplianceDueDateTableModel<DBComplianceDueDate>(dbCompliance.getDbComplianceDuedates());
		facesUtil.setSessionAttribute("token", null);
	}

	private void handleEdit(String editId) {
		String token = facesUtil.retrieveRequestParam("token");
		if(StringUtils.isNotEmpty(token)) {
			editId = Constants.decryptString(token);
		}
		facesUtil.setSessionAttribute("token", Constants.encryptString(editId));
		actionMode = Constants.ACTION_EDIT;
		Long idLong = Long.parseLong(editId);
		dbCompliance = dbComplianceService.findById(idLong);
		
		onChangeReportType();
		
		uploadFiles = new ArrayList<UploadedFileWO>();
		for (int i = 0; i < dbCompliance.getDbComplianceDocs().size(); i++) {
			DBComplianceDoc ra = dbCompliance.getDbComplianceDocs().get(i);
			UploadedFileWO uf = new UploadedFileWO();
			uf.setFileName(ra.getAttachmentFile());
			uf.setFileId(ra.getFileId());
			uf.setIsNew(false);
			uf.setFileSize(ra.getFileSize());
			uploadFiles.add(uf);
			
		}
		
		
		if (dbCompliance.getDbComplianceDuedates() != null) {
			lastSequenceOfDueDate = dbCompliance.getDbComplianceDuedates().size();
			for (int i = 0; i < dbCompliance.getDbComplianceDuedates().size(); i++) {
				DBComplianceDueDate dtl = (DBComplianceDueDate) dbCompliance.getDbComplianceDuedates().get(i);

				lastSequenceOfDueDate = lastSequenceOfDueDate + 1;
				dtl.setSequence(lastSequenceOfDueDate);
			}
		}
		
		tableDueDateModel = new DBComplianceDueDateTableModel<DBComplianceDueDate>(dbCompliance.getDbComplianceDuedates());
		
		lastSequenceOfDtl = 0;
		
		
	}

	public Boolean validate() {
		Boolean flag = false;

		if (dbCompliance.getReportType().getReportTypeId() == null) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formDBComplianceReportType") 
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}else{
			ReportType reportType = reportTypeService.findById(dbCompliance.getReportType().getReportTypeId());
			dbCompliance.setReportType(reportType);
		}

		if (StringUtils.isEmpty(dbCompliance.getReportNameIn())) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formDBComplianceReportName") 
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}
		
		
		
		if (StringUtils.isEmpty(dbCompliance.getDescription())) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formDBComplianceDescriptions") + " "
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}
		
		if (!flag) {
			DBCompliance regulValid = dbComplianceService.getCheckDataDBCompliance(dbCompliance.getDatabaseComplianceId(),
					dbCompliance.getReportNameIn(),
					dbCompliance.getReportType().getReportTypeIn());
			if (actionMode.equals(Constants.ACTION_EDIT)) {
				if (regulValid != null && regulValid.getDatabaseComplianceId() != null
						&& regulValid.getDatabaseComplianceId() > 0) {
					facesUtil.addErrMessage(facesUtil.retrieveMessage("formDBComplianceReportName") + " : "
							+ regulValid.getReportNameIn() + " "
							+ facesUtil.retrieveMessage("formDBComplianceReportType")  + " : "
							+ regulValid.getReportTypeStr() + " " 
							+ facesUtil.retrieveMessage("validateAlreadyExist"));
					flag = true;
				}
			} else {
				if (regulValid != null && regulValid.getDatabaseComplianceId() != null
						&& regulValid.getDatabaseComplianceId() > 0) {
					
						facesUtil.addErrMessage(facesUtil.retrieveMessage("formDBComplianceReportName") + " : "
								+ regulValid.getReportNameIn() + " "
								+ facesUtil.retrieveMessage("formDBComplianceReportType")  + " : "
								+ regulValid.getReportTypeStr() + " " 
								+ facesUtil.retrieveMessage("validateAlreadyExist"));
						flag = true;
					
				}
			}
		}

		

		return flag;
	}

	public void save() {
		try {
			if (!validate()) {
				
				if (dbCompliance.getDbComplianceDuedates() != null) {
					for (int i = 0; i < dbCompliance.getDbComplianceDuedates().size(); i++) {
						DBComplianceDueDate dtl = (DBComplianceDueDate) dbCompliance.getDbComplianceDuedates().get(i);
						dtl.setDbCompliance(dbCompliance);
						if (dtl.getCreatedBy() == null) {
							dtl.setCreatedBy(facesUtil.retrieveUserLogin());
							dtl.setCreationDate(new Timestamp(new Date().getTime()));
						}

						if ("day".equals(dueDateType)) {
							// dtl.setDueDay(null);
							dtl.setDueDateOnly(null);
							dtl.setDueDate(null);
						} else if ("dateonly".equals(dueDateType)) {
							dtl.setDueDay(null);
							// dtl.setDueDateDateOnly(null);
							dtl.setDueDate(null);
						} else {
							dtl.setDueDay(null);
							dtl.setDueDateOnly(null);
							// dtl.setDueDate(null);
						}

						dtl.setLastUpdateBy(facesUtil.retrieveUserLogin());
						dtl.setLastUpdateDate(new Timestamp(new Date().getTime()));
						dtl.setDelId(new Long(0));
						dtl.setEnabledFlag(Constants.CONSTANT_YES);
					}
				}
				
				if(dbCompliance.getDbComplianceDocs() == null || dbCompliance.getDbComplianceDocs().size() <= 0) {
					dbCompliance.setDbComplianceDocs(new ArrayList<DBComplianceDoc>());
				}
				dbCompliance.getDbComplianceDocs().clear();
				
				
				
				if(uploadFiles != null) {
					for (int i = 0; i < uploadFiles.size(); i++) {
						DBComplianceDoc doc = new DBComplianceDoc();
						UploadedFileWO uf = (UploadedFileWO) uploadFiles.get(i);
						doc.setDbCompliance(dbCompliance);
						
						doc.setAttachmentFile(uf.getFileName());
						
						doc.setCreatedBy(facesUtil.retrieveUserLogin());
						doc.setCreationDate(new Timestamp(new Date().getTime()));
						doc.setDelId(new Long(0));
						doc.setEnabledFlag(Constants.CONSTANT_YES);
						
						doc.setFileId(uf.getFileId());
						doc.setFileSize(uf.getFileSize());
						dbCompliance.getDbComplianceDocs().add(doc);
					}
				}
				
				
				
				ReportType rt = reportTypeService.findById(dbCompliance.getReportType().getReportTypeId());
				dbCompliance.setReportType(rt);

				if (dbCompliance.getDatabaseComplianceId() != null) {
					dbCompliance.setLastUpdateBy(facesUtil.retrieveUserLogin());
					dbCompliance.setLastUpdateDate(new Timestamp(new Date().getTime()));
					dbCompliance.setDelId(new Long(0));
					dbCompliance.setEnabledFlag(Constants.CONSTANT_YES);
					dbComplianceService.update(dbCompliance);

				} else {
					
					dbCompliance.setCreatedBy(facesUtil.retrieveUserLogin());
					dbCompliance.setCreationDate(new Timestamp(new Date().getTime()));
					dbCompliance.setDelId(new Long(0));
					dbCompliance.setEnabledFlag(Constants.CONSTANT_YES);
					dbComplianceService.save(dbCompliance);
				}

				facesUtil.redirect("/pages/dbCompliance/dbCompliance.faces");
			}

		} catch (Exception ex) {
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + ex.getMessage(), "");
		}

	}
	
	public void handleFileUpload (FileUploadEvent event) {
		try {
			uploadFiles = uploadFiles == null ? new ArrayList<UploadedFileWO>() : uploadFiles;
			uploadFiles.add(new UploadedFileWO( CallApiManager.callUploadAPI(event.getFile(), Constants.ARTICLE, parameterDetailService, false, fileUtil),
					event.getFile().getFileName(),event.getFile().getContentType(),event.getFile().getSize()));
		} catch (Exception e) {
			e.printStackTrace();
			facesUtil.addErrMessage(e.getMessage());
		}
	}
	
	public void deleteAttachment (String fileId, int index, String uploadType) throws Exception {
		deleteFiles = deleteFiles != null ? deleteFiles : new ArrayList<UploadedFileWO>();
		deleteFiles.add(new UploadedFileWO(fileId,null,null,null));
		
		if(uploadType != null && uploadType.equals(OutgoingLetterConstants.UPLOAD_TYPE_DOCUMENT)) {
			uploadFiles.remove(uploadFiles.get(index));
		}
	}
	
	public void downloadFile (String fileId, String fileName, byte[] content) throws Exception {
		CallApiManager.downloadFile(fileId, fileName, content, parameterDetailService);
	}
	
	public void onChangeReportType() {

		if (dbCompliance.getReportType().getReportTypeId() != null) {
			ReportType rt = reportTypeService.findById(dbCompliance.getReportType().getReportTypeId());
			if (CommonConstants.Y.equals(rt.getDueDay())) {
				dueDateType = "day";
			} else if (CommonConstants.Y.equals(rt.getDueDate()) && CommonConstants.N.equals(rt.getDueMonth())
					&& CommonConstants.N.equals(rt.getDueYear())) {
				dueDateType = "dateonly";
			} else {
				dueDateType = "date";
			}
		} else {
			dueDateType = "";
		}

	}
	
	public void onChangeReportTypeNow() {
		dbCompliance.getDbComplianceDuedates().clear();
		if (dbCompliance.getReportType().getReportTypeId() != null) {
			ReportType rt = reportTypeService.findById(dbCompliance.getReportType().getReportTypeId());
			if (CommonConstants.Y.equals(rt.getDueDay())) {
				dueDateType = "day";
			} else if (CommonConstants.Y.equals(rt.getDueDate()) && CommonConstants.N.equals(rt.getDueMonth())
					&& CommonConstants.N.equals(rt.getDueYear())) {
				dueDateType = "dateonly";
			} else {
				dueDateType = "date";
			}

			if (rt.getNumberOfDueDate() != null) {
				for (int i = 1; i <= rt.getNumberOfDueDate(); i++) {
					onAddNewDueDate();
				}
			}

		} else {
			dueDateType = "";
		}
	}
	
	public void onAddNewDueDate() {
		if (dbCompliance.getDbComplianceDuedates() == null) {
			dbCompliance.setDbComplianceDuedates(new ArrayList<DBComplianceDueDate>());
			lastSequenceOfDueDate = 0;
		} else {
			if (dbCompliance.getDbComplianceDuedates().size() == 0) {
				lastSequenceOfDueDate = 0;
			}
		}

		DBComplianceDueDate d = new DBComplianceDueDate();
		lastSequenceOfDueDate = lastSequenceOfDueDate + 1;
		d.setSequence(lastSequenceOfDueDate);

		dbCompliance.getDbComplianceDuedates().add(d);

		tableDueDateModel.setWrappedData(dbCompliance.getDbComplianceDuedates());

		PrimeFaces.current().executeScript("reInitSelect2();");
	}

	public void onDeleteRowDueDate() {
		for (int i = 0; i < selectedDueDateData.length; i++) {
			dbCompliance.getDbComplianceDuedates().remove(selectedDueDateData[i]);
		}

		if (dbCompliance.getDbComplianceDuedates() == null || dbCompliance.getDbComplianceDuedates().size() == 0) {
			lastSequenceOfDueDate = 0;
		}

		tableDueDateModel.setWrappedData(dbCompliance.getDbComplianceDuedates());

		PrimeFaces.current().executeScript("reInitSelect2();");
	}


	public void cancel() {
		try {
			facesUtil.redirect("/pages/dbCompliance/dbCompliance.faces");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

	public DBComplianceService getDBComplianceService() {
		return dbComplianceService;
	}

	public void setDBComplianceService(DBComplianceService dbComplianceService) {
		this.dbComplianceService = dbComplianceService;
	}

	public String getNavigateSeadbComplianceh() {
		return navigateSeadbComplianceh;
	}

	public void setNavigateSeadbComplianceh(String navigateSeadbComplianceh) {
		this.navigateSeadbComplianceh = navigateSeadbComplianceh;
	}

	public DBCompliance getDBCompliance() {
		return dbCompliance;
	}

	public void setDBCompliance(DBCompliance dbCompliance) {
		this.dbCompliance = dbCompliance;
	}

	public Boolean getIsViewOnly() {
		return isViewOnly;
	}

	public void setIsViewOnly(Boolean isViewOnly) {
		this.isViewOnly = isViewOnly;
	}

	public String getActionMode() {
		return actionMode;
	}

	public void setActionMode(String actionMode) {
		this.actionMode = actionMode;
	}

	public String getEditedId() {
		return editedId;
	}

	public void setEditedId(String editedId) {
		this.editedId = editedId;
	}


	public List<SelectItem> getNameList() {
		return nameList;
	}

	public void setNameList(List<SelectItem> nameList) {
		this.nameList = nameList;
	}

	public List<SelectItem> getSlaTypeList() {
		return slaTypeList;
	}

	public void setSlaTypeList(List<SelectItem> slaTypeList) {
		this.slaTypeList = slaTypeList;
	}

	public boolean isCheckAll() {
		return checkAll;
	}

	public void setCheckAll(boolean checkAll) {
		this.checkAll = checkAll;
	}


	public Integer getLastSequenceOfDtl() {
		return lastSequenceOfDtl;
	}

	public void setLastSequenceOfDtl(Integer lastSequenceOfDtl) {
		this.lastSequenceOfDtl = lastSequenceOfDtl;
	}

	public DBCompliance getRc() {
		return dbCompliance;
	}

	public void setRc(DBCompliance dbCompliance) {
		this.dbCompliance = dbCompliance;
	}

	public DBComplianceService getRcService() {
		return dbComplianceService;
	}

	public void setRcService(DBComplianceService dbComplianceService) {
		this.dbComplianceService = dbComplianceService;
	}

	public List<SelectItem> getReportTypeList() {
		return reportTypeList;
	}

	public void setReportTypeList(List<SelectItem> reportTypeList) {
		this.reportTypeList = reportTypeList;
	}

	public List<UploadedFileWO> getUploadFiles() {
		return uploadFiles;
	}

	public void setUploadFiles(List<UploadedFileWO> uploadFiles) {
		this.uploadFiles = uploadFiles;
	}

	public FileUtil getFileUtil() {
		return fileUtil;
	}

	public void setFileUtil(FileUtil fileUtil) {
		this.fileUtil = fileUtil;
	}

	public List<UploadedFileWO> getDeleteFiles() {
		return deleteFiles;
	}

	public void setDeleteFiles(List<UploadedFileWO> deleteFiles) {
		this.deleteFiles = deleteFiles;
	}

	public DBCompliance getDbCompliance() {
		return dbCompliance;
	}

	public void setDbCompliance(DBCompliance dbCompliance) {
		this.dbCompliance = dbCompliance;
	}

	public DBComplianceService getDbComplianceService() {
		return dbComplianceService;
	}

	public void setDbComplianceService(DBComplianceService dbComplianceService) {
		this.dbComplianceService = dbComplianceService;
	}

	public DBComplianceDueDate[] getSelectedDueDateData() {
		return selectedDueDateData;
	}

	public void setSelectedDueDateData(DBComplianceDueDate[] selectedDueDateData) {
		this.selectedDueDateData = selectedDueDateData;
	}

	public DBComplianceDueDateTableModel<DBComplianceDueDate> getTableDueDateModel() {
		return tableDueDateModel;
	}

	public void setTableDueDateModel(DBComplianceDueDateTableModel<DBComplianceDueDate> tableDueDateModel) {
		this.tableDueDateModel = tableDueDateModel;
	}

	public Integer getLastSequenceOfDueDate() {
		return lastSequenceOfDueDate;
	}

	public void setLastSequenceOfDueDate(Integer lastSequenceOfDueDate) {
		this.lastSequenceOfDueDate = lastSequenceOfDueDate;
	}

	public List<SelectItem> getDayList() {
		return dayList;
	}

	public void setDayList(List<SelectItem> dayList) {
		this.dayList = dayList;
	}

	public List<SelectItem> getDateList() {
		return dateList;
	}

	public void setDateList(List<SelectItem> dateList) {
		this.dateList = dateList;
	}

	public ReportTypeService getReportTypeService() {
		return reportTypeService;
	}

	public void setReportTypeService(ReportTypeService reportTypeService) {
		this.reportTypeService = reportTypeService;
	}

	public String getDueDateType() {
		return dueDateType;
	}

	public void setDueDateType(String dueDateType) {
		this.dueDateType = dueDateType;
	}

	public String getTextWarningUpload() {
		return textWarningUpload;
	}

	public void setTextWarningUpload(String textWarningUpload) {
		this.textWarningUpload = textWarningUpload;
	}
	
	

}