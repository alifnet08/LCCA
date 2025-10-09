package com.wo.module.trcRmd.bean;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.primefaces.event.FileUploadEvent;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.CommonConstants;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.model.UploadedFileWO;
import com.wo.module.common.util.FileUtil;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.counterType.service.CounterTypeService;
import com.wo.module.externalRegulation.service.RegulationMstService;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;
import com.wo.module.reportType.model.ReportType;
import com.wo.module.reportType.service.ReportTypeService;
import com.wo.module.trcRmd.model.TrcRmd;
import com.wo.module.trcRmd.model.TrcRmdCorrespondence;
import com.wo.module.trcRmd.model.TrcRmdCorrespondenceTableModel;
import com.wo.module.trcRmd.model.TrcRmdDueDate;
import com.wo.module.trcRmd.model.TrcRmdDueDateTableModel;
import com.wo.module.trcRmd.model.TrcRmdPicFollowup;
import com.wo.module.trcRmd.model.TrcRmdPicFollowupAttachment;
import com.wo.module.trcRmd.model.TrcRmdRegulation;
import com.wo.module.trcRmd.model.TrcRmdRegulationTableModel;
import com.wo.module.trcRmd.model.TrcRmdSupportingUnit;
import com.wo.module.trcRmd.model.TrcRmdSupportingUnitTableModel;
import com.wo.module.trcRmd.service.TrcRmdPicFollowupService;
import com.wo.module.trcRmd.service.TrcRmdService;
import com.wo.module.user.model.Division;
import com.wo.module.user.service.UserService;

public class TrcRmdEditBean extends CommonBean implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(TrcRmdEditBean.class);

	private TrcRmdPicFollowup trcRmdPicFollowup;
	private TrcRmd trcRmd;

	private Boolean isViewOnly;

	private String actionMode;

	private String editedId;

	private String dueDateType;

	private List<UploadedFileWO> deletedFiles;
	private List<UploadedFileWO> uploadedFilesEvidence;

	private TrcRmdDueDate[] selectedRmdDueDateData;
	private TrcRmdRegulation[] selectedRmdRegulationData;
	private TrcRmdSupportingUnit[] selectedRmdSupportingUnitData;
	private TrcRmdCorrespondence[] selectedRmdCorrespondenceData;

	private TrcRmdDueDateTableModel<TrcRmdDueDate> tableRmdDueDateModel;
	private TrcRmdRegulationTableModel<TrcRmdRegulation> tableRmdRegulationModel;
	private TrcRmdSupportingUnitTableModel<TrcRmdSupportingUnit> tableRmdSupportingUnitModel;
	private TrcRmdCorrespondenceTableModel<TrcRmdCorrespondence> tableRmdCorrespondenceModel;

	private Integer indexDtl;
	private Integer indexDtlCc;

	SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");

	private TrcRmdService trcRmdService;
	private TrcRmdPicFollowupService trcRmdPicFollowupService;
	private ReportTypeService reportTypeService;
	private CounterTypeService counterTypeService;
	//private ParameterDetailService parameterDetailService;
	private UserService userService;
	private RegulationMstService regulationMstService;

	public FacesUtil facesUtil;
	
	private FileUtil fileUtil;
	
	private String viewOnly;

	private List<SelectItem> reportTypeList;
	private List<SelectItem> counterTypeList;

	private List<SelectItem> dayList;
	private List<SelectItem> dateList;
	private List<SelectItem> divisionList;

	private List<SelectItem> reminderStatusList;

	private String REMINDER_ACTIVE = ParameterDetail.PARAM_DET_CODE_REMINDER_ACTIVE;
	private String REMINDER_INACTIVE = ParameterDetail.PARAM_DET_CODE_REMINDER_INACTIVE;
	private String textWarningUpload;

	public void addMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void addErrMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
	public void redirectCurrentPage(ComponentSystemEvent event) {
		String url = FacesContext.getCurrentInstance().getViewRoot().getViewId();
		if (facesUtil.getSessionAttribute(Constants.SESSION_NEED_REDIRECT) != null 
				&& ((String) facesUtil.getSessionAttribute(Constants.SESSION_NEED_REDIRECT)).equals("Y")
				&& facesUtil.getSessionAttribute("token") != null
				&& !url.contains("token")) {
			facesUtil.removeSessionAttribute(Constants.SESSION_NEED_REDIRECT);
			url = url + "?token=" + ((String) facesUtil.getSessionAttribute("token"));
			try {
				facesUtil.removeSessionAttribute("token");
				facesUtil.redirect(url);
				return;
			} catch (IOException ex) {
				ex.printStackTrace();
				logger.error(ex.getMessage());
			}
		}
	}

	@PostConstruct
	public void init() {
		super.init();
		initList();

		checkNewOrEdit();
		fileUtil = FileUtil.getInstance();
		facesUtil.removeSessionAttribute(Constants.SESSION_NEED_REDIRECT);
		
		try {
			ParameterDetail getText = parameterDetailService.getParameterDetailByParamDtlCode("UPLOAD_WARNING_TEXT");
			textWarningUpload = getText.getName();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void initList() {

		try {
			reportTypeList = new ArrayList<SelectItem>();
			List<ReportType> list1 = reportTypeService.getAllReportType();
			for (ReportType vo : list1) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getReportType());
				si.setValue(vo.getReportTypeId());
				reportTypeList.add(si);
			}

			counterTypeList = counterTypeService.getAllCounterTypeLabelValue();

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

			divisionList = new ArrayList<SelectItem>();
			List<Division> listDiv = userService.getAllDivision();
			for (Division vo : listDiv) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getDivisionName());
				si.setValue(vo.getDivisionId());
				divisionList.add(si);
			}

			reminderStatusList = new ArrayList<SelectItem>();
			List<ParameterDetail> listParamDtl = parameterDetailService
					.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_CODE_REMINDER_STATUS);

			for (ParameterDetail vo : listParamDtl) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getName());
				si.setValue(vo.getParameterDtlCode());
				reminderStatusList.add(si);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onChangeReportType() {
		if(trcRmd.getReportType().getReportTypeId()!= null) {
			ReportType rt = reportTypeService.findById(trcRmd.getReportType().getReportTypeId());
			if (CommonConstants.Y.equals(rt.getDueDay())) {
				dueDateType = "day";
			} else if (CommonConstants.Y.equals(rt.getDueDate()) && 
					CommonConstants.N.equals(rt.getDueMonth()) && 
					CommonConstants.N.equals(rt.getDueYear())  ) {
				dueDateType = "dateonly";
			} else {
				dueDateType = "date";
			}
		}  else {
			dueDateType = "";
		}
	}
	
	public void handleFileUploadEvidence(FileUploadEvent event) throws Exception {
		try {
		uploadedFilesEvidence = uploadedFilesEvidence == null ? new ArrayList<UploadedFileWO>() : uploadedFilesEvidence;
		uploadedFilesEvidence
				.add(new UploadedFileWO(
						CallApiManager.callUploadAPI(event.getFile(), Constants.COMPLIANCE_DOC_TYPE_RMD,
								parameterDetailService, false, fileUtil),
						event.getFile().getFileName(), event.getFile().getContentType(),
						event.getFile().getSize()));
		} catch (Exception e) {
			e.printStackTrace();
			facesUtil.addErrMessage(e.getMessage());
		}
	}

	private void checkNewOrEdit() {
		String editId = facesUtil.retrieveRequestParam("id");String token = facesUtil.retrieveRequestParam("token"); 
		
		String viewId = facesUtil.retrieveRequestParam("viewId");
		
		isViewOnly = false;
		if (viewId != null && !viewId.isEmpty()) {
			if (viewId.trim().equalsIgnoreCase("true")) {
				isViewOnly = true;
			}
		}
		if (StringUtils.isBlank(editId) && StringUtils.isBlank(token)) {
			// this.handleNew();
		} else {
			this.handleEdit(editId);
		}
	}

	private void handleEdit(String editId) {
		String token = facesUtil.retrieveRequestParam("token");
		if(StringUtils.isNotEmpty(token)) {
			editId = Constants.decryptString(token);
		}
		facesUtil.setSessionAttribute("token", Constants.encryptString(editId));
		actionMode = Constants.ACTION_EDIT;
		Long idLong = Long.parseLong(editId);
		trcRmdPicFollowup = trcRmdPicFollowupService.findById(idLong);
		trcRmd = trcRmdService.findById(trcRmdPicFollowup.getTrcRmd().getRmdId());

		onChangeReportType();

		if (trcRmd.getUser1() != null) {
			trcRmd.setUserNameTemp1(trcRmd.getUser1().getName());
		}

		if (trcRmd.getUser2() != null) {
			trcRmd.setUserNameTemp2(trcRmd.getUser2().getName());
		}

		if (trcRmd.getUser3() != null) {
			trcRmd.setUserNameTemp3(trcRmd.getUser3().getName());
		}

		if (trcRmd.getSupportingUnitDetails() != null) {
			for (int i = 0; i < trcRmd.getSupportingUnitDetails().size(); i++) {
				TrcRmdSupportingUnit dtl = (TrcRmdSupportingUnit) trcRmd.getSupportingUnitDetails().get(i);
				if (dtl.getEmailCc1() != null) {
					dtl.setEmailCcTemp1(dtl.getEmailCc1().getNik() + "-" + dtl.getEmailCc1().getName());
				}

				if (dtl.getEmailCc2() != null) {
					dtl.setEmailCcTemp2(dtl.getEmailCc2().getNik() + "-" + dtl.getEmailCc2().getName());
				}

				if (dtl.getEmailCc3() != null) {
					dtl.setEmailCcTemp3(dtl.getEmailCc3().getNik() + "-" + dtl.getEmailCc3().getName());
				}
			}
		}

		uploadedFilesEvidence = new ArrayList<UploadedFileWO>();

		for (int i = 0; i < trcRmdPicFollowup.getPicFollowupAttachmentDetails().size(); i++) {
			TrcRmdPicFollowupAttachment ra = trcRmdPicFollowup.getPicFollowupAttachmentDetails().get(i);
			UploadedFileWO uf = new UploadedFileWO();
			uf.setFileId(ra.getFileId());
			uf.setFileName(ra.getAttachmentFile());
			uf.setIsNew(false);
			uf.setFileSize(ra.getFileSize());
			uploadedFilesEvidence.add(uf);

		}

		tableRmdDueDateModel = new TrcRmdDueDateTableModel<TrcRmdDueDate>(trcRmd.getDueDateDetails());
		tableRmdSupportingUnitModel = new TrcRmdSupportingUnitTableModel<TrcRmdSupportingUnit>(
				trcRmd.getSupportingUnitDetails());
		tableRmdRegulationModel = new TrcRmdRegulationTableModel<TrcRmdRegulation>(trcRmd.getRegulationDetails());
		tableRmdCorrespondenceModel = new TrcRmdCorrespondenceTableModel<TrcRmdCorrespondence>(trcRmd.getCorrespondenceDetails());
		
		if (trcRmdPicFollowup.getFollowupStatus() != null && trcRmdPicFollowup.getFollowupStatus().getParameterDtlCode() != null
				&& trcRmdPicFollowup.getFollowupStatus().getParameterDtlCode().equals(ParameterDetail.PARAM_DET_CODE_PIC_FOLLOWUP_STATUS_PIC_DONE)) {
			viewOnly = "Y";
			
			SimpleDateFormat sdf = new SimpleDateFormat(CommonConstants.INPUT_DATE_FORMAT);
			
			facesUtil.addWarnMessage(
					facesUtil.retrieveMessage("formTmpRmdNotifConfirmationDone", 
					sdf.format(trcRmdPicFollowup.getTargetDate()),
					trcRmdPicFollowup.getFollowupBy().getName()));		
		} else {
			viewOnly = "N";
		}
	}

	public Boolean validate() {
		Boolean flag = false;
		
		if (trcRmdPicFollowup.getFollowupDate() == null) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpCorrespondenceFollowupDate") + " "
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}
		
		if (uploadedFilesEvidence == null || uploadedFilesEvidence.size() <= 0) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpRmdPicConfirmationEvidence") + " File "
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}
		
		return flag;
	}

	public void save() {
		try {
			if (!validate()) {
				if (trcRmdPicFollowup.getRmdPicFollowupId() != null) {
					if (trcRmdPicFollowup.getPicFollowupAttachmentDetails() == null
							|| trcRmdPicFollowup.getPicFollowupAttachmentDetails().size() == 0) {
						trcRmdPicFollowup.setPicFollowupAttachmentDetails(new ArrayList<TrcRmdPicFollowupAttachment>());
					}
					trcRmdPicFollowup.getPicFollowupAttachmentDetails().clear();
					
					if(uploadedFilesEvidence != null) {
						for (int i = 0; i < uploadedFilesEvidence.size(); i++) {
							TrcRmdPicFollowupAttachment evidence = new TrcRmdPicFollowupAttachment();
							UploadedFileWO uf = (UploadedFileWO) uploadedFilesEvidence.get(i);
							evidence.setTrcRmdPicFollowup(trcRmdPicFollowup);
							evidence.setFileId(uf.getFileId());
							evidence.setFileSize(uf.getFileSize());
							evidence.setAttachmentFile(uf.getFileName());
							evidence.setCreatedBy(facesUtil.retrieveUserLogin());
							evidence.setCreationDate(new Timestamp(new Date().getTime()));
							evidence.setDelId(new Long(0));
							evidence.setEnabledFlag(Constants.CONSTANT_YES);
							trcRmdPicFollowup.getPicFollowupAttachmentDetails().add(evidence);
						}
					}

					trcRmdPicFollowup.setConfirmationDate(new Date());
					ParameterDetail followupStatus = parameterDetailService.getParameterDetailByParamDtlCode(
							ParameterDetail.PARAM_DET_CODE_PIC_FOLLOWUP_STATUS_PIC_DONE);
					trcRmdPicFollowup.setFollowupStatus(followupStatus);
					trcRmdPicFollowup.setFollowupBy(userService.getUserByNik(facesUtil.retrieveUserLogin()));

					trcRmdPicFollowup.setLastUpdateBy(facesUtil.retrieveUserLogin());
					trcRmdPicFollowup.setLastUpdateDate(new Timestamp(new Date().getTime()));
					trcRmdPicFollowup.setDelId(new Long(0));
					trcRmdPicFollowup.setEnabledFlag(Constants.CONSTANT_YES);
					trcRmdPicFollowupService.update(trcRmdPicFollowup);
				}
				
				if(deletedFiles!=null) {
					for(int i=0;i<deletedFiles.size();i++) {
						UploadedFileWO uf = (UploadedFileWO) deletedFiles.get(i);
						CallApiManager.deleteFile(uf.getFileId(), parameterDetailService, fileUtil);
					}
				}
				
				facesUtil.redirect("/pages/trcRmd/trcRmd.faces");
			}

		} catch (Exception ex) {
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + ex.getMessage(), "");
			ex.printStackTrace();
		}

	}
	
	public void downloadFile(String fileId, String fileName, byte[] content) throws Exception {
		CallApiManager.downloadFile(fileId, fileName, content, parameterDetailService);
	}
	
	public void deleteAttachment(String fileId,int index) throws Exception {
		//CallApiManager.deleteFile(fileId, parameterDetailService, fileUtil);
		deletedFiles = deletedFiles!=null?deletedFiles: new ArrayList<UploadedFileWO>();
		
		deletedFiles.add(new UploadedFileWO(fileId,null,null, null));
		
		uploadedFilesEvidence.remove(uploadedFilesEvidence.get(index));
		
	}

	public void cancel() {
		try {
			if(uploadedFilesEvidence != null) {
				for (int i = 0; i < uploadedFilesEvidence.size(); i++) {
					
					UploadedFileWO uf = (UploadedFileWO) uploadedFilesEvidence.get(i);
					if(uf.getIsNew() == null) {
						CallApiManager.deleteFile(uf.getFileId(), parameterDetailService, fileUtil);
					}
				}
			}
			facesUtil.redirect("/pages/trcRmd/trcRmd.faces");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}
	
	public TrcRmd getTrcRmd() {
		return trcRmd;
	}

	public void setTrcRmd(TrcRmd trcRmd) {
		this.trcRmd = trcRmd;
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

	/*
	 * public ParameterDetailService getParameterDetailService() { return
	 * parameterDetailService; }
	 * 
	 * public void setParameterDetailService(ParameterDetailService
	 * parameterDetailService) { this.parameterDetailService =
	 * parameterDetailService; }
	 */

	public ReportTypeService getReportTypeService() {
		return reportTypeService;
	}

	public void setReportTypeService(ReportTypeService reportTypeService) {
		this.reportTypeService = reportTypeService;
	}

	public List<SelectItem> getReportTypeList() {
		return reportTypeList;
	}

	public void setReportTypeList(List<SelectItem> reportTypeList) {
		this.reportTypeList = reportTypeList;
	}

	public CounterTypeService getCounterTypeService() {
		return counterTypeService;
	}

	public void setCounterTypeService(CounterTypeService counterTypeService) {
		this.counterTypeService = counterTypeService;
	}

	public List<SelectItem> getCounterTypeList() {
		return counterTypeList;
	}

	public void setCounterTypeList(List<SelectItem> counterTypeList) {
		this.counterTypeList = counterTypeList;
	}

	public TrcRmdDueDateTableModel<TrcRmdDueDate> getTableRmdDueDateModel() {
		return tableRmdDueDateModel;
	}

	public void setTableRmdDueDateModel(TrcRmdDueDateTableModel<TrcRmdDueDate> tableRmdDueDateModel) {
		this.tableRmdDueDateModel = tableRmdDueDateModel;
	}

	public TrcRmdRegulationTableModel<TrcRmdRegulation> getTableRmdRegulationModel() {
		return tableRmdRegulationModel;
	}

	public void setTableRmdRegulationModel(TrcRmdRegulationTableModel<TrcRmdRegulation> tableRmdRegulationModel) {
		this.tableRmdRegulationModel = tableRmdRegulationModel;
	}

	public TrcRmdSupportingUnitTableModel<TrcRmdSupportingUnit> getTableRmdSupportingUnitModel() {
		return tableRmdSupportingUnitModel;
	}

	public void setTableRmdSupportingUnitModel(
			TrcRmdSupportingUnitTableModel<TrcRmdSupportingUnit> tableRmdSupportingUnitModel) {
		this.tableRmdSupportingUnitModel = tableRmdSupportingUnitModel;
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

	public String getDueDateType() {
		return dueDateType;
	}

	public void setDueDateType(String dueDateType) {
		this.dueDateType = dueDateType;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public List<SelectItem> getDivisionList() {
		return divisionList;
	}

	public void setDivisionList(List<SelectItem> divisionList) {
		this.divisionList = divisionList;
	}

	public String getREMINDER_ACTIVE() {
		return REMINDER_ACTIVE;
	}

	public void setREMINDER_ACTIVE(String rEMINDER_ACTIVE) {
		REMINDER_ACTIVE = rEMINDER_ACTIVE;
	}

	public String getREMINDER_INACTIVE() {
		return REMINDER_INACTIVE;
	}

	public void setREMINDER_INACTIVE(String rEMINDER_INACTIVE) {
		REMINDER_INACTIVE = rEMINDER_INACTIVE;
	}

	public TrcRmdService getTrcRmdService() {
		return trcRmdService;
	}

	public void setTrcRmdService(TrcRmdService trcRmdService) {
		this.trcRmdService = trcRmdService;
	}

	public Integer getIndexDtl() {
		return indexDtl;
	}

	public void setIndexDtl(Integer indexDtl) {
		this.indexDtl = indexDtl;
	}

	public RegulationMstService getRegulationMstService() {
		return regulationMstService;
	}

	public void setRegulationMstService(RegulationMstService regulationMstService) {
		this.regulationMstService = regulationMstService;
	}

	public List<SelectItem> getReminderStatusList() {
		return reminderStatusList;
	}

	public void setReminderStatusList(List<SelectItem> reminderStatusList) {
		this.reminderStatusList = reminderStatusList;
	}

	public TrcRmdDueDate[] getSelectedRmdDueDateData() {
		return selectedRmdDueDateData;
	}

	public void setSelectedRmdDueDateData(TrcRmdDueDate[] selectedRmdDueDateData) {
		this.selectedRmdDueDateData = selectedRmdDueDateData;
	}

	public TrcRmdRegulation[] getSelectedRmdRegulationData() {
		return selectedRmdRegulationData;
	}

	public void setSelectedRmdRegulationData(TrcRmdRegulation[] selectedRmdRegulationData) {
		this.selectedRmdRegulationData = selectedRmdRegulationData;
	}

	public TrcRmdSupportingUnit[] getSelectedRmdSupportingUnitData() {
		return selectedRmdSupportingUnitData;
	}

	public void setSelectedRmdSupportingUnitData(TrcRmdSupportingUnit[] selectedRmdSupportingUnitData) {
		this.selectedRmdSupportingUnitData = selectedRmdSupportingUnitData;
	}

	public Integer getIndexDtlCc() {
		return indexDtlCc;
	}

	public void setIndexDtlCc(Integer indexDtlCc) {
		this.indexDtlCc = indexDtlCc;
	}

	public List<UploadedFileWO> getDeletedFiles() {
		return deletedFiles;
	}

	public void setDeletedFiles(List<UploadedFileWO> deletedFiles) {
		this.deletedFiles = deletedFiles;
	}

	public List<UploadedFileWO> getUploadedFilesEvidence() {
		return uploadedFilesEvidence;
	}

	public void setUploadedFilesEvidence(List<UploadedFileWO> uploadedFilesEvidence) {
		this.uploadedFilesEvidence = uploadedFilesEvidence;
	}

	public FileUtil getFileUtil() {
		return fileUtil;
	}

	public void setFileUtil(FileUtil fileUtil) {
		this.fileUtil = fileUtil;
	}

	public String getViewOnly() {
		return viewOnly;
	}

	public void setViewOnly(String viewOnly) {
		this.viewOnly = viewOnly;
	}

	public TrcRmdPicFollowup getTrcRmdPicFollowup() {
		return trcRmdPicFollowup;
	}

	public void setTrcRmdPicFollowup(TrcRmdPicFollowup trcRmdPicFollowup) {
		this.trcRmdPicFollowup = trcRmdPicFollowup;
	}

	public TrcRmdPicFollowupService getTrcRmdPicFollowupService() {
		return trcRmdPicFollowupService;
	}

	public void setTrcRmdPicFollowupService(TrcRmdPicFollowupService trcRmdPicFollowupService) {
		this.trcRmdPicFollowupService = trcRmdPicFollowupService;
	}

	public TrcRmdCorrespondence[] getSelectedRmdCorrespondenceData() {
		return selectedRmdCorrespondenceData;
	}

	public void setSelectedRmdCorrespondenceData(TrcRmdCorrespondence[] selectedRmdCorrespondenceData) {
		this.selectedRmdCorrespondenceData = selectedRmdCorrespondenceData;
	}

	public TrcRmdCorrespondenceTableModel<TrcRmdCorrespondence> getTableRmdCorrespondenceModel() {
		return tableRmdCorrespondenceModel;
	}

	public void setTableRmdCorrespondenceModel(TrcRmdCorrespondenceTableModel<TrcRmdCorrespondence> tableRmdCorrespondenceModel) {
		this.tableRmdCorrespondenceModel = tableRmdCorrespondenceModel;
	}

	public String getTextWarningUpload() {
		return textWarningUpload;
	}

	public void setTextWarningUpload(String textWarningUpload) {
		this.textWarningUpload = textWarningUpload;
	}

	

}