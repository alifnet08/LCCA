package com.wo.module.trcCorrespondenceApproval.bean;

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
import javax.faces.model.SelectItem;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.model.UploadedFileWO;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.FileUtil;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.counterType.service.CounterTypeService;
import com.wo.module.externalRegulation.service.RegulationMstService;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.lov.bean.SelectorModel.SelectorInfo;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;
import com.wo.module.rc.model.RC;
import com.wo.module.rc.service.RCService;
import com.wo.module.reportType.service.ReportTypeService;
import com.wo.module.trcCorrespondence.model.TrcCorrespondence;
import com.wo.module.trcCorrespondence.model.TrcCorrespondenceDocument;
import com.wo.module.trcCorrespondence.model.TrcCorrespondencePicCompliance;
import com.wo.module.trcCorrespondence.model.TrcCorrespondencePicComplianceTableModel;
import com.wo.module.trcCorrespondence.model.TrcCorrespondencePicFollowupAttendance;
import com.wo.module.trcCorrespondence.model.TrcCorrespondencePicFollowupAttendanceTableModel;
import com.wo.module.trcCorrespondence.model.TrcCorrespondenceSupportingUnit;
import com.wo.module.trcCorrespondence.model.TrcCorrespondenceSupportingUnitTableModel;
import com.wo.module.trcCorrespondence.model.TrcCrpdcPicConfirm;
import com.wo.module.trcCorrespondence.model.TrcCrpdcPicConfirmTableModel;
import com.wo.module.trcCorrespondence.model.TrcCrpdcReffLetter;
import com.wo.module.trcCorrespondence.model.TrcCrpdcReffLetterTableModel;
import com.wo.module.trcCorrespondence.service.TrcCorrespondenceService;
import com.wo.module.trcCorrespondenceApproval.constant.TrcCorrespondenceApprovalConstants;
import com.wo.module.user.model.Division;
import com.wo.module.user.service.UserService;

public class TrcCorrespondenceApprovalEditBean extends CommonBean implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(TrcCorrespondenceApprovalEditBean.class);

	private TrcCorrespondence trcCorrespondence;

	private Boolean isViewOnly;

	private String actionMode;

	private String editedId;

	private String dueDateType;

	private TrcCorrespondencePicCompliance[] selectedPicComplianceData;
	private TrcCorrespondenceSupportingUnit[] selectedSupportingUnitData;
	private TrcCrpdcReffLetter[] selectedReferalLetterData;
	private TrcCrpdcPicConfirm[] selectedSubPicConfirmData;

	private SelectorInfo selectorUser1;
	private SelectorInfo selectorUser2;
	private SelectorInfo selectorUser3;
	private SelectorInfo selectorPicCompliance;

	private SelectorInfo selectorUserCc1;
	private SelectorInfo selectorUserCc2;
	private SelectorInfo selectorUserCc3;

	private List<UploadedFileWO> uploadedFilesDocument;

	private TrcCorrespondencePicComplianceTableModel<TrcCorrespondencePicCompliance> tablePicComplianceModel;
	private TrcCorrespondenceSupportingUnitTableModel<TrcCorrespondenceSupportingUnit> tableSupportingUnitModel;
	private TrcCorrespondencePicFollowupAttendanceTableModel<TrcCorrespondencePicFollowupAttendance> tableAttedanceModel;
	private TrcCrpdcReffLetterTableModel<TrcCrpdcReffLetter> tableReferalLetterModel;
	private TrcCrpdcPicConfirmTableModel<TrcCrpdcPicConfirm> tableSubPicConfirmModel;

	private Integer indexDtlPicCompliance;
	private Integer indexDtlCc;

	private List<TrcCorrespondence> trcCorrespondenceList;

	SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");

	private TrcCorrespondenceService trcCorrespondenceService;
	private ReportTypeService reportTypeService;
	private CounterTypeService counterTypeService;
	private RCService rcService;
	//private ParameterDetailService parameterDetailService;
	private UserService userService;
	private RegulationMstService regulationMstService;

	public FacesUtil facesUtil;
	
	private FileUtil fileUtil;

	private List<SelectItem> senderCodeList;
	private List<SelectItem> yesNoList;
	private List<SelectItem> counterTypeList;
	private List<SelectItem> complianceStatusList;
	private List<SelectItem> divisionList;
	private List<SelectItem> correspondenceTypeCodeList;
	private List<SelectItem> rcList;
	private List<SelectItem> categoryList;
	
	private List<SelectItem> reminderStatusList;

	private String REMINDER_ACTIVE = ParameterDetail.PARAM_DET_CODE_REMINDER_ACTIVE;
	private String REMINDER_INACTIVE = ParameterDetail.PARAM_DET_CODE_REMINDER_INACTIVE;

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
	}

	public void initList() {
		try {
			
			rcList = new ArrayList<SelectItem>();
			List<SearchObject> searchCriteria = new ArrayList<SearchObject>();
			List<RC> listRC = rcService.searchData(searchCriteria, 0, Integer.MAX_VALUE, null, null);
			
			for (RC vo : listRC) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getRegionCode()+"-"+vo.getWorkingUnit()+"-"+vo.getRegion());
				si.setValue(vo.getRcId());
				rcList.add(si);
			}
			
			categoryList = new ArrayList<SelectItem>();
			List<ParameterDetail> listCategory = parameterDetailService
					.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_FINE_CATEGORY);

			for (ParameterDetail vo : listCategory) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getName());
				si.setValue(vo.getParameterDtlCode());
				categoryList.add(si);
			}
			
			senderCodeList = new ArrayList<SelectItem>();
			List<ParameterDetail> listParamDtl = parameterDetailService
					.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_CODE_SENDER);

			for (ParameterDetail vo : listParamDtl) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getName());
				si.setValue(vo.getParameterDtlCode());
				senderCodeList.add(si);
			}
			
			reminderStatusList = new ArrayList<SelectItem>();
			List<ParameterDetail> listParamReminderDtl = parameterDetailService
					.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_CODE_REMINDER_STATUS);

			for (ParameterDetail vo : listParamReminderDtl) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getName());
				si.setValue(vo.getParameterDtlCode());
				reminderStatusList.add(si);
			}
			
			complianceStatusList = new ArrayList<SelectItem>();
			List<ParameterDetail> listComplianceDtl = parameterDetailService
					.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_CODE_COMPLIANCE_CHECK_STATUS);

			for (ParameterDetail vo : listComplianceDtl) {
				if (!(vo.getParameterDtlCode().equals("COMPLIANCE_NOT_APPROPRIATE") 
						|| vo.getParameterDtlCode().equals("COMPLIANCE_APPROPRIATE"))) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getName());
				si.setValue(vo.getParameterDtlCode());
				complianceStatusList.add(si);
				}
			}
			
			counterTypeList = counterTypeService.getAllCounterTypeLabelValue();
			
			divisionList = new ArrayList<SelectItem>();
			List<Division> listDiv = userService.getAllDivision();
			for (Division vo : listDiv) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getDivisionName());
				si.setValue(vo.getDivisionId());
				divisionList.add(si);
			}
			
			yesNoList = new ArrayList<SelectItem>();
			yesNoList.add(new SelectItem(Constants.CONSTANT_YES, "Yes"));
			yesNoList.add(new SelectItem(Constants.CONSTANT_NO, "No"));

			correspondenceTypeCodeList = new ArrayList<SelectItem>();
			List<ParameterDetail> listCorrespondenceTypeDtl = parameterDetailService
					.getParameterDetailByParamCodeOrdered(ParameterHeader.PARAM_HEAD_CODE_CORRESPONDENCE_TYPE);

			for (ParameterDetail vo : listCorrespondenceTypeDtl) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getName());
				si.setValue(vo.getParameterDtlCode());
				correspondenceTypeCodeList.add(si);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
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
			//this.handleNew();
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
		trcCorrespondence = trcCorrespondenceService.findById(idLong);
		
		//TrcCorrespondence trcCorrespondence = trcCorrespondenceService.findById(idLong);
		if (trcCorrespondence != null) {
			trcCorrespondenceList = new ArrayList<TrcCorrespondence>();
			trcCorrespondenceList.add(trcCorrespondence);
			if(trcCorrespondenceList.get(0).getComplianceStatus() == null ) {
				trcCorrespondenceList.get(0).setComplianceStatus(new ParameterDetail());
			}
			
			/*if (trcCorrespondence.getUserId1() != null) {
				trcCorrespondence.setUserNameTemp1(trcCorrespondence.getUserId1().getName());
			}
	
			if (trcCorrespondence.getUserId2() != null) {
				trcCorrespondence.setUserNameTemp2(trcCorrespondence.getUserId2().getName());
			}
	
			if (trcCorrespondence.getUserId3() != null) {
				trcCorrespondence.setUserNameTemp3(trcCorrespondence.getUserId3().getName());
			}*/
			
			if (trcCorrespondence.getTrcCorrespondencePicCompliances() != null) {
				for (int i = 0; i < trcCorrespondence.getTrcCorrespondencePicCompliances().size(); i++) {
					TrcCorrespondencePicCompliance dtl = (TrcCorrespondencePicCompliance) trcCorrespondence.getTrcCorrespondencePicCompliances().get(i);
					if (dtl.getUser() != null) {
						dtl.setNikTemp(dtl.getUser().getNik());
						dtl.setNameTemp(dtl.getUser().getName());
						dtl.setEmailTemp(dtl.getUser().getEmail());
					}
				}
			}
			
			if (trcCorrespondence.getTrcCorrespondenceSupportingUnits() != null) {
				for (int i = 0; i < trcCorrespondence.getTrcCorrespondenceSupportingUnits().size(); i++) {
					TrcCorrespondenceSupportingUnit dtl = (TrcCorrespondenceSupportingUnit) trcCorrespondence.getTrcCorrespondenceSupportingUnits().get(i);
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
			
			if (trcCorrespondence.getTrcCrpdcReffLetters() != null) {
				for (int i = 0; i < trcCorrespondence.getTrcCrpdcReffLetters().size(); i++) {
					TrcCrpdcReffLetter dtl = (TrcCrpdcReffLetter) trcCorrespondence
							.getTrcCrpdcReffLetters().get(i);

					if (dtl.getReffLetterCorrespondence() != null) {
						dtl.setPerihal(dtl.getReffLetterCorrespondence().getPerihalIn());
						dtl.setLetterNo(dtl.getReffLetterCorrespondence().getLetterNo());
						dtl.setLampiran("");					
					}
				}
			}
			
			if (trcCorrespondence.getTrcCrpdcPicConfirms() != null) {
				for (int i = 0; i < trcCorrespondence.getTrcCrpdcPicConfirms().size(); i++) {
					TrcCrpdcPicConfirm dtl = (TrcCrpdcPicConfirm) trcCorrespondence
							.getTrcCrpdcPicConfirms().get(i);
					
					if (dtl.getUser1() != null) {
						dtl.setUserName1(dtl.getUser1().getName());
					}

					if (dtl.getUser2() != null) {
						dtl.setUserName2(dtl.getUser2().getName());
					}

					if (dtl.getUser3() != null) {
						dtl.setUserName3(dtl.getUser3().getName());
					}
				}
			}
			
			if (trcCorrespondence.getTrcCorrespondencePicFollowupAttendance() != null) {
				for (int i = 0; i < trcCorrespondence.getTrcCorrespondencePicFollowupAttendance().size(); i++) {
					TrcCorrespondencePicFollowupAttendance dtl = (TrcCorrespondencePicFollowupAttendance) trcCorrespondence
							.getTrcCorrespondencePicFollowupAttendance().get(i);
					
					if(dtl !=null && dtl.getUserId() !=null) {
						dtl.setUserNIK(dtl.getUserId().getNik());
						dtl.setUserName(dtl.getUserId().getName());
					}
				}
			}
						
			uploadedFilesDocument = new ArrayList<UploadedFileWO>();
			
			for (int i = 0; i < trcCorrespondence.getTrcCorrespondenceDocuments().size(); i++) {
				TrcCorrespondenceDocument ra = trcCorrespondence.getTrcCorrespondenceDocuments().get(i);
				UploadedFileWO uf = new UploadedFileWO();
				uf.setFileId(ra.getFileId());
				uf.setFileName(ra.getAttachmentFile());
				uf.setFileSize(ra.getFileSize());
	
				uploadedFilesDocument.add(uf);
	
			}
			
			tableSupportingUnitModel = new TrcCorrespondenceSupportingUnitTableModel<TrcCorrespondenceSupportingUnit>(
					trcCorrespondence.getTrcCorrespondenceSupportingUnits());
			tablePicComplianceModel = new TrcCorrespondencePicComplianceTableModel<TrcCorrespondencePicCompliance>(trcCorrespondence.getTrcCorrespondencePicCompliances());
			tableAttedanceModel = new TrcCorrespondencePicFollowupAttendanceTableModel<TrcCorrespondencePicFollowupAttendance>(
					trcCorrespondence.getTrcCorrespondencePicFollowupAttendance());
			tableReferalLetterModel = new TrcCrpdcReffLetterTableModel<TrcCrpdcReffLetter>(
					trcCorrespondence.getTrcCrpdcReffLetters());
			tableSubPicConfirmModel = new TrcCrpdcPicConfirmTableModel<TrcCrpdcPicConfirm>(
					trcCorrespondence.getTrcCrpdcPicConfirms());
		
			trcCorrespondence.setTrcCorrespondencePicCompliances(new ArrayList<TrcCorrespondencePicCompliance>());	
		}
	}

	public Boolean validate() {
		Boolean flag = false;
		/*TrcCorrespondence trcCorrespondenceTemp = trcCorrespondenceList.get(0);
		
		if (StringUtils.isEmpty(trcCorrespondenceTemp.getComplianceStatus().getParameterDtlCode())) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpCorrespondenceComplianceCheckerStatus") + " "
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}
		
		if (StringUtils.isEmpty(trcCorrespondenceTemp.getFollowupNote())) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpCorrespondenceComplianceCheckerNote") + " "
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}*/
		
		
		if(trcCorrespondenceList !=null && trcCorrespondenceList.size() > 0) {
			for(int i=0; i<trcCorrespondenceList.size(); i++) {
				TrcCorrespondence trcCorrespondenceTemp = trcCorrespondenceList.get(i);
				if (trcCorrespondenceTemp.getComplianceStatus() == null || 
						StringUtils.isEmpty(trcCorrespondenceTemp.getComplianceStatus().getParameterDtlCode())) {
					facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpCorrespondenceComplianceCheckerStatus") + " "
							+ facesUtil.retrieveMessage("validateRequired"));
					
					flag = true;
				}else {				
					if(trcCorrespondenceTemp.getComplianceStatus() != null && 
							trcCorrespondenceTemp.getComplianceStatus().getParameterDtlCode().equals(TrcCorrespondenceApprovalConstants.COMPLIANCE_STATUS_COMPLIANCE_OPEN)) {
						if (StringUtils.isEmpty(trcCorrespondenceTemp.getComplianceNote())) {
							facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpCorrespondenceComplianceCheckerNote") + " "
									+ facesUtil.retrieveMessage("validateRequired"));
							
							flag = true;
						}
					}
				}
			}
		}
		
		

		

		return flag;
	}

	public void save() {		
		try {
			if (!validate()) {
				ParameterDetail pdReminderStatus = parameterDetailService
						.getParameterDetailByParamDtlCode(trcCorrespondence.getReminderStatus().getParameterDtlCode());
				trcCorrespondence.setReminderStatus(pdReminderStatus);

				if (trcCorrespondence.getCorrespondenceId() != null) {
					if (trcCorrespondenceList != null && trcCorrespondenceList.size() > 0) {
						for (int i = 0; i < trcCorrespondenceList.size(); i++) {
							TrcCorrespondence trcCorrespondenceTemp = trcCorrespondenceList.get(i);
							ParameterDetail complianceStatus = parameterDetailService.getParameterDetailByParamDtlCode(
									trcCorrespondenceTemp.getComplianceStatus().getParameterDtlCode());
							trcCorrespondence.setComplianceStatus(complianceStatus);
							trcCorrespondence.setComplianceNote(trcCorrespondenceTemp.getComplianceNote());
							trcCorrespondence.setComplianceBy(userService.getUserByNik(facesUtil.retrieveUserLogin()));
							trcCorrespondence.setComplianceDate(new Date());

							if (ParameterDetail.PARAM_DET_CODE_COMPLIANCE_CHECK_STATUS_COMPLIANCE_OPEN
									.equals(complianceStatus.getParameterDtlCode())) {
								ParameterDetail followupStatus = parameterDetailService
										.getParameterDetailByParamDtlCode(
												ParameterDetail.PARAM_DET_CODE_PIC_FOLLOWUP_STATUS_PIC_INPROGRESS);
								trcCorrespondence.setFollowupStatus(followupStatus);
							}

							trcCorrespondence.setLastUpdateBy(facesUtil.retrieveUserLogin());
							trcCorrespondence.setLastUpdateDate(new Timestamp(new Date().getTime()));
							trcCorrespondence.setDelId(new Long(0));
							trcCorrespondence.setEnabledFlag(Constants.CONSTANT_YES);
							trcCorrespondenceService.update(trcCorrespondence);
						}
					}
				}

				facesUtil.redirect("/pages/trcCorrespondenceApproval/trcCorrespondenceApproval.faces");
			}

		} catch (Exception ex) {
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + ex.getMessage(), "");
			ex.printStackTrace();
		}

	}

	public void cancel() {
		try {
			facesUtil.redirect("/pages/trcCorrespondenceApproval/trcCorrespondenceApproval.faces");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void downloadFile(String fileId, String fileName, byte[] content) throws Exception {
		CallApiManager.downloadFile(fileId, fileName, content, parameterDetailService);
	}
	
	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

	public TrcCorrespondence getTrcCorrespondence() {
		return trcCorrespondence;
	}

	public void setTrcCorrespondence(TrcCorrespondence trcCorrespondence) {
		this.trcCorrespondence = trcCorrespondence;
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

	/*public ParameterDetailService getParameterDetailService() {
		return parameterDetailService;
	}

	public void setParameterDetailService(ParameterDetailService parameterDetailService) {
		this.parameterDetailService = parameterDetailService;
	}*/

	public ReportTypeService getReportTypeService() {
		return reportTypeService;
	}

	public void setReportTypeService(ReportTypeService reportTypeService) {
		this.reportTypeService = reportTypeService;
	}

	public CounterTypeService getCounterTypeService() {
		return counterTypeService;
	}

	public void setCounterTypeService(CounterTypeService counterTypeService) {
		this.counterTypeService = counterTypeService;
	}


	public TrcCorrespondencePicComplianceTableModel<TrcCorrespondencePicCompliance> getTablePicComplianceModel() {
		return tablePicComplianceModel;
	}

	public void setTablePicComplianceModel(
			TrcCorrespondencePicComplianceTableModel<TrcCorrespondencePicCompliance> tablePicComplianceModel) {
		this.tablePicComplianceModel = tablePicComplianceModel;
	}

	public TrcCorrespondenceSupportingUnitTableModel<TrcCorrespondenceSupportingUnit> getTableSupportingUnitModel() {
		return tableSupportingUnitModel;
	}

	public void setTableSupportingUnitModel(
			TrcCorrespondenceSupportingUnitTableModel<TrcCorrespondenceSupportingUnit> tableSupportingUnitModel) {
		this.tableSupportingUnitModel = tableSupportingUnitModel;
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
	
	public SelectorInfo getSelectorUser1() {
		return selectorUser1;
	}

	public void setSelectorUser1(SelectorInfo selectorUser1) {
		this.selectorUser1 = selectorUser1;
	}

	public SelectorInfo getSelectorUser2() {
		return selectorUser2;
	}

	public void setSelectorUser2(SelectorInfo selectorUser2) {
		this.selectorUser2 = selectorUser2;
	}

	public SelectorInfo getSelectorUser3() {
		return selectorUser3;
	}

	public void setSelectorUser3(SelectorInfo selectorUser3) {
		this.selectorUser3 = selectorUser3;
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

	public SelectorInfo getSelectorUserCc1() {
		return selectorUserCc1;
	}

	public void setSelectorUserCc1(SelectorInfo selectorUserCc1) {
		this.selectorUserCc1 = selectorUserCc1;
	}

	public SelectorInfo getSelectorUserCc2() {
		return selectorUserCc2;
	}

	public void setSelectorUserCc2(SelectorInfo selectorUserCc2) {
		this.selectorUserCc2 = selectorUserCc2;
	}

	public SelectorInfo getSelectorUserCc3() {
		return selectorUserCc3;
	}

	public void setSelectorUserCc3(SelectorInfo selectorUserCc3) {
		this.selectorUserCc3 = selectorUserCc3;
	}

	public TrcCorrespondencePicCompliance[] getSelectedPicComplianceData() {
		return selectedPicComplianceData;
	}

	public void setSelectedPicComplianceData(TrcCorrespondencePicCompliance[] selectedPicComplianceData) {
		this.selectedPicComplianceData = selectedPicComplianceData;
	}

	public TrcCorrespondenceSupportingUnit[] getSelectedSupportingUnitData() {
		return selectedSupportingUnitData;
	}

	public void setSelectedSupportingUnitData(TrcCorrespondenceSupportingUnit[] selectedSupportingUnitData) {
		this.selectedSupportingUnitData = selectedSupportingUnitData;
	}

	public Integer getIndexDtlCc() {
		return indexDtlCc;
	}

	public void setIndexDtlCc(Integer indexDtlCc) {
		this.indexDtlCc = indexDtlCc;
	}

	public List<SelectItem> getSenderCodeList() {
		return senderCodeList;
	}

	public void setSenderCodeList(List<SelectItem> senderCodeList) {
		this.senderCodeList = senderCodeList;
	}

	public List<SelectItem> getYesNoList() {
		return yesNoList;
	}

	public void setYesNoList(List<SelectItem> yesNoList) {
		this.yesNoList = yesNoList;
	}

	public List<UploadedFileWO> getUploadedFilesDocument() {
		return uploadedFilesDocument;
	}

	public void setUploadedFilesDocument(List<UploadedFileWO> uploadedFilesDocument) {
		this.uploadedFilesDocument = uploadedFilesDocument;
	}

	public List<SelectItem> getCounterTypeList() {
		return counterTypeList;
	}

	public void setCounterTypeList(List<SelectItem> counterTypeList) {
		this.counterTypeList = counterTypeList;
	}

	public Integer getIndexDtlPicCompliance() {
		return indexDtlPicCompliance;
	}

	public void setIndexDtlPicCompliance(Integer indexDtlPicCompliance) {
		this.indexDtlPicCompliance = indexDtlPicCompliance;
	}

	public SelectorInfo getSelectorPicCompliance() {
		return selectorPicCompliance;
	}

	public void setSelectorPicCompliance(SelectorInfo selectorPicCompliance) {
		this.selectorPicCompliance = selectorPicCompliance;
	}

	public List<SelectItem> getComplianceStatusList() {
		return complianceStatusList;
	}

	public void setComplianceStatusList(List<SelectItem> complianceStatusList) {
		this.complianceStatusList = complianceStatusList;
	}

	public TrcCorrespondenceService getTrcCorrespondenceService() {
		return trcCorrespondenceService;
	}

	public void setTrcCorrespondenceService(TrcCorrespondenceService trcCorrespondenceService) {
		this.trcCorrespondenceService = trcCorrespondenceService;
	}

	public List<TrcCorrespondence> getTrcCorrespondenceList() {
		return trcCorrespondenceList;
	}

	public void setTrcCorrespondenceList(List<TrcCorrespondence> trcCorrespondenceList) {
		this.trcCorrespondenceList = trcCorrespondenceList;
	}

	public FileUtil getFileUtil() {
		return fileUtil;
	}

	public void setFileUtil(FileUtil fileUtil) {
		this.fileUtil = fileUtil;
	}

	public List<SelectItem> getCorrespondenceTypeCodeList() {
		return correspondenceTypeCodeList;
	}

	public void setCorrespondenceTypeCodeList(List<SelectItem> correspondenceTypeCodeList) {
		this.correspondenceTypeCodeList = correspondenceTypeCodeList;
	}

	public RCService getRcService() {
		return rcService;
	}

	public void setRcService(RCService rcService) {
		this.rcService = rcService;
	}

	public List<SelectItem> getRcList() {
		return rcList;
	}

	public void setRcList(List<SelectItem> rcList) {
		this.rcList = rcList;
	}

	public List<SelectItem> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<SelectItem> categoryList) {
		this.categoryList = categoryList;
	}

	public TrcCrpdcReffLetter[] getSelectedReferalLetterData() {
		return selectedReferalLetterData;
	}

	public void setSelectedReferalLetterData(TrcCrpdcReffLetter[] selectedReferalLetterData) {
		this.selectedReferalLetterData = selectedReferalLetterData;
	}

	public TrcCrpdcPicConfirm[] getSelectedSubPicConfirmData() {
		return selectedSubPicConfirmData;
	}

	public void setSelectedSubPicConfirmData(TrcCrpdcPicConfirm[] selectedSubPicConfirmData) {
		this.selectedSubPicConfirmData = selectedSubPicConfirmData;
	}

	public TrcCrpdcReffLetterTableModel<TrcCrpdcReffLetter> getTableReferalLetterModel() {
		return tableReferalLetterModel;
	}

	public void setTableReferalLetterModel(TrcCrpdcReffLetterTableModel<TrcCrpdcReffLetter> tableReferalLetterModel) {
		this.tableReferalLetterModel = tableReferalLetterModel;
	}

	public TrcCrpdcPicConfirmTableModel<TrcCrpdcPicConfirm> getTableSubPicConfirmModel() {
		return tableSubPicConfirmModel;
	}

	public void setTableSubPicConfirmModel(TrcCrpdcPicConfirmTableModel<TrcCrpdcPicConfirm> tableSubPicConfirmModel) {
		this.tableSubPicConfirmModel = tableSubPicConfirmModel;
	}

	public TrcCorrespondencePicFollowupAttendanceTableModel<TrcCorrespondencePicFollowupAttendance> getTableAttedanceModel() {
		return tableAttedanceModel;
	}

	public void setTableAttedanceModel(
			TrcCorrespondencePicFollowupAttendanceTableModel<TrcCorrespondencePicFollowupAttendance> tableAttedanceModel) {
		this.tableAttedanceModel = tableAttedanceModel;
	}

	public SimpleDateFormat getSdf() {
		return sdf;
	}

	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}

	
}