package com.wo.module.tmpCorrespondence.bean;

import java.io.Serializable;
//import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.primefaces.PrimeFaces;
//import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.exception.CustomAPIException;
import com.wo.module.common.model.UploadedFileWO;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.FileUtil;
import com.wo.module.common.util.MathUtil;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.common.utility.EmailUtil;
import com.wo.module.counterType.model.CounterType;
import com.wo.module.counterType.model.CounterTypeDtl;
import com.wo.module.counterType.service.CounterTypeService;
import com.wo.module.emailTemplate.model.EmailTemplate;
import com.wo.module.emailTemplate.service.EmailTemplateService;
import com.wo.module.externalRegulation.service.RegulationMstService;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.lov.bean.SelectorListener;
import com.wo.module.lov.bean.SelectorModel.SelectorInfo;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;
import com.wo.module.rc.model.RC;
import com.wo.module.rc.service.RCService;
import com.wo.module.reportType.service.ReportTypeService;
import com.wo.module.tmpCorrespondence.constant.TmpCorrespondenceConstants;
import com.wo.module.tmpCorrespondence.model.TmpCorrespondence;
import com.wo.module.tmpCorrespondence.model.TmpCorrespondenceApproval;
import com.wo.module.tmpCorrespondence.model.TmpCorrespondenceDocument;
import com.wo.module.tmpCorrespondence.model.TmpCorrespondencePicCompliance;
import com.wo.module.tmpCorrespondence.model.TmpCorrespondencePicComplianceTableModel;
import com.wo.module.tmpCorrespondence.model.TmpCorrespondenceSupportingUnit;
import com.wo.module.tmpCorrespondence.model.TmpCorrespondenceSupportingUnitTableModel;
import com.wo.module.tmpCorrespondence.model.TmpCrpdcPicConfirm;
import com.wo.module.tmpCorrespondence.model.TmpCrpdcPicConfirmTableModel;
import com.wo.module.tmpCorrespondence.model.TmpCrpdcReffLetter;
import com.wo.module.tmpCorrespondence.model.TmpCrpdcReffLetterTableModel;
import com.wo.module.tmpCorrespondence.model.TmpReffDocument;
import com.wo.module.tmpCorrespondence.service.TmpCorrespondenceService;
import com.wo.module.trcCorrespondence.model.TrcCorrespondence;
import com.wo.module.trcCorrespondence.service.TrcCorrespondenceService;
import com.wo.module.user.model.Division;
import com.wo.module.user.model.User;
import com.wo.module.user.service.UserService;

public class TmpCorrespondenceEditBean extends CommonBean implements SelectorListener<Object>, Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(TmpCorrespondenceEditBean.class);

	private TmpCorrespondence tmpCorrespondence;

	private Boolean isViewOnly;

	private String actionMode;

	private String editedId;

	private String dueDateType;

	private Boolean disabledFollowUpStatus;

	private TmpCorrespondencePicCompliance[] selectedPicComplianceData;
	private TmpCorrespondenceSupportingUnit[] selectedSupportingUnitData;
	private TmpCrpdcReffLetter[] selectedReferalLetterData;
	private TmpCrpdcPicConfirm[] selectedSubPicConfirmData;

	private SelectorInfo selectorUser1;
	private SelectorInfo selectorUser2;
	private SelectorInfo selectorUser3;
	private SelectorInfo selectorPicCompliance;
	private SelectorInfo selectedReferalLetter;
	private SelectorInfo selectedSubPicConfirm;
	
	private SelectorInfo selectorUserCc1;
	private SelectorInfo selectorUserCc2;
	private SelectorInfo selectorUserCc3;

	private List<UploadedFileWO> deletedFiles;
	private List<UploadedFileWO> uploadedFilesDocument;

	private TmpCorrespondencePicComplianceTableModel<TmpCorrespondencePicCompliance> tablePicComplianceModel;
	private TmpCorrespondenceSupportingUnitTableModel<TmpCorrespondenceSupportingUnit> tableSupportingUnitModel;
	private TmpCrpdcReffLetterTableModel<TmpCrpdcReffLetter> tableReferalLetterModel;
	private TmpCrpdcPicConfirmTableModel<TmpCrpdcPicConfirm> tableSubPicConfirmModel;

	private Integer lastSequenceOfPicCompliance;
	private Integer lastSequenceOfSupportingUnitModel;
	private Integer lastSequenceOfReffLetter;
	private Integer lastSequenceOfPicConfirm;

	private Integer indexDtlPicCompliance;
	private Integer indexDtlCc;
	private Integer indexDtlRefLetter;
	private Integer indexDtlPicConfirm;
	
	private String divNameLogin;

	// private List<TrcRmd> trcRmdList;
	private List<TrcCorrespondence> trcCorrespondenceList;

	SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");

	private TmpCorrespondenceService tmpCorrespondenceService;
	private ReportTypeService reportTypeService;
	private CounterTypeService counterTypeService;
	// private ParameterDetailService parameterDetailService;
	private UserService userService;
	private TrcCorrespondenceService trcCorrespondenceService;
	private RegulationMstService regulationMstService;
	private EmailTemplateService emailTemplateService;
	private RCService rcService;

	public FacesUtil facesUtil;

	private FileUtil fileUtil;

	private List<SelectItem> senderCodeList;
	private List<SelectItem> yesNoList;
	private List<SelectItem> counterTypeList;
	private List<SelectItem> complianceStatusList;
	private List<SelectItem> divisionList;

	private List<SelectItem> reminderStatusList;
	private List<SelectItem> correspondenceTypeCodeList;
	private List<SelectItem> rcList;
	private List<SelectItem> categoryList;

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

	@PostConstruct
	public void init() {
		super.init();
		initList();
		this.divNameLogin = facesUtil.getUserLogin().getDivisionName();
		selectorUser1 = TmpCorrespondenceConstants.buildSelectorUser();
		selectorUser2 = TmpCorrespondenceConstants.buildSelectorUser();
		selectorUser3 = TmpCorrespondenceConstants.buildSelectorUser();
		selectorPicCompliance = TmpCorrespondenceConstants.buildSelectorUserCompliance(divNameLogin);
		selectorUserCc1 = TmpCorrespondenceConstants.buildSelectorUser();
		selectorUserCc2 = TmpCorrespondenceConstants.buildSelectorUser();
		selectorUserCc3 = TmpCorrespondenceConstants.buildSelectorUser();
		selectedReferalLetter = TmpCorrespondenceConstants.buildSelectorReferensiSurat();

		checkNewOrEdit();

		fileUtil = FileUtil.getInstance();

		try {
			ParameterDetail getText = parameterDetailService.getParameterDetailByParamDtlCode("UPLOAD_WARNING_TEXT");
			textWarningUpload = getText.getName();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void initList() {
		try {

			rcList = new ArrayList<SelectItem>();
			List<SearchObject> searchCriteria = new ArrayList<SearchObject>();
			List<RC> listRC = rcService.searchData(searchCriteria, 0, Integer.MAX_VALUE, null, null);

			for (RC vo : listRC) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getRegionCode() + "-" + vo.getWorkingUnit() + "-" + vo.getRegion());
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

				if (!vo.getParameterDtlCode().equals(ParameterDetail.PARAM_DET_CODE_TYPE_FINE)) {
					SelectItem si = new SelectItem();

					si.setLabel(vo.getName());
					si.setValue(vo.getParameterDtlCode());
					correspondenceTypeCodeList.add(si);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		PrimeFaces.current().executeScript("reInitSelect2();");
	}

	public void updatePanelDenda() {
		System.out.println("tipe Surat ==" + tmpCorrespondence.getCorrespondenceCode().getParameterDtlCode());

	}

/*	public void clearPic2() {
		tmpCorrespondence.setUserId2(null);
		tmpCorrespondence.setUserNameTemp2(null);
	}

	public void clearPic3() {
		tmpCorrespondence.setUserId3(null);
		tmpCorrespondence.setUserNameTemp3(null);
	} */

	public void clearPicDetail2(int i) {
		tmpCorrespondence.getTmpCorrespondenceSupportingUnits().get(i).setEmailCc2(null);
		tmpCorrespondence.getTmpCorrespondenceSupportingUnits().get(i).setEmailCcTemp2(null);
		PrimeFaces.current().executeScript("reInitSelect2();");
	}

	public void clearPicDetail3(int i) {
		tmpCorrespondence.getTmpCorrespondenceSupportingUnits().get(i).setEmailCc3(null);
		tmpCorrespondence.getTmpCorrespondenceSupportingUnits().get(i).setEmailCcTemp3(null);
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	public void clearPicConfirm2(int i) {
		tmpCorrespondence.getTmpCrpdcPicConfirms().get(i).setUser2(null);
		tmpCorrespondence.getTmpCrpdcPicConfirms().get(i).setUserName2(null);
		PrimeFaces.current().executeScript("reInitSelect2();");
	}

	public void clearPicConfirm3(int i) {
		tmpCorrespondence.getTmpCrpdcPicConfirms().get(i).setUser3(null);
		tmpCorrespondence.getTmpCrpdcPicConfirms().get(i).setUserName3(null);
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	/*public void onChangeDivisionSingle() {
		tmpCorrespondence.setUserId1(null);
		tmpCorrespondence.setUserNameTemp1(null);
		tmpCorrespondence.setUserId2(null);
		tmpCorrespondence.setUserNameTemp2(null);
		tmpCorrespondence.setUserId3(null);
		tmpCorrespondence.setUserNameTemp3(null);
	} */

	public void onChangeDivision(int i) {
		TmpCorrespondenceSupportingUnit data = tmpCorrespondence.getTmpCorrespondenceSupportingUnits().get(i);
		data.setEmailCc1(null);
		data.setEmailCcTemp1(null);
		data.setEmailCc2(null);
		data.setEmailCcTemp2(null);
		data.setEmailCc3(null);
		data.setEmailCcTemp3(null);
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	public void onChangeDivisionPicConfirm(int i) {
		TmpCrpdcPicConfirm data = tmpCorrespondence.getTmpCrpdcPicConfirms().get(i);
		data.setUser1(null);
		data.setUserName1(null);
		data.setUser2(null);
		data.setUserName2(null);
		data.setUser3(null);
		data.setUserName3(null);
		PrimeFaces.current().executeScript("reInitSelect2();");
	}

	public void onChangeFollowupStatus() {
		if (tmpCorrespondence.getFollowUp() != null) {
			if ("Y".equals(tmpCorrespondence.getFollowUp())) {
				/*
				 * tmpCorrespondence.setUserId1(null); tmpCorrespondence.setUserNameTemp1(null);
				 * tmpCorrespondence.setUserId2(null); tmpCorrespondence.setUserNameTemp2(null);
				 * tmpCorrespondence.setUserId3(null); tmpCorrespondence.setUserNameTemp3(null);
				 */
				
				if (tmpCorrespondence.getCounterType() == null) {
					tmpCorrespondence.setCounterType(new CounterType());
				}

			} else if ("N".equals(tmpCorrespondence.getFollowUp())) {
				/*
				 * tmpCorrespondence.setDivisionId(null); tmpCorrespondence.setTargetDate(null);
				 * tmpCorrespondence.setUserId1(null); tmpCorrespondence.setUserNameTemp1(null);
				 * tmpCorrespondence.setUserId2(null); tmpCorrespondence.setUserNameTemp2(null);
				 * tmpCorrespondence.setUserId3(null); tmpCorrespondence.setUserNameTemp3(null);
				 */
				
				if (tmpCorrespondence.getTmpCrpdcPicConfirms() != null) {
					tmpCorrespondence.getTmpCrpdcPicConfirms().clear();
				}

				if (tmpCorrespondence.getTmpCorrespondenceSupportingUnits() != null) {
					tmpCorrespondence.getTmpCorrespondenceSupportingUnits().clear();
				}

				tmpCorrespondence.setCounterType(null);
			}

		}

		PrimeFaces.current().executeScript("reInitSelect2();");
		// RequestContext.getCurrentInstance().execute("reInitSelect2();");
	}

	public void onAddNewPicCompliance() {
		if (tmpCorrespondence.getTmpCorrespondencePicCompliances() == null) {
			tmpCorrespondence.setTmpCorrespondencePicCompliances(new ArrayList<TmpCorrespondencePicCompliance>());
			lastSequenceOfPicCompliance = 0;
		} else {
			if (tmpCorrespondence.getTmpCorrespondencePicCompliances().size() == 0) {
				lastSequenceOfPicCompliance = 0;
			}
		}

		TmpCorrespondencePicCompliance d = new TmpCorrespondencePicCompliance();
		lastSequenceOfPicCompliance = lastSequenceOfPicCompliance + 1;
		d.setSequence(lastSequenceOfPicCompliance);
		tmpCorrespondence.getTmpCorrespondencePicCompliances().add(d);
		tablePicComplianceModel.setWrappedData(tmpCorrespondence.getTmpCorrespondencePicCompliances());

	}

	public void onDeleteRowPicCompliance() {
		for (int i = 0; i < selectedPicComplianceData.length; i++) {
			tmpCorrespondence.getTmpCorrespondencePicCompliances().remove(selectedPicComplianceData[i]);
		}

		if (tmpCorrespondence.getTmpCorrespondencePicCompliances() == null
				|| tmpCorrespondence.getTmpCorrespondencePicCompliances().size() == 0) {
			lastSequenceOfPicCompliance = 0;
		}

		tablePicComplianceModel.setWrappedData(tmpCorrespondence.getTmpCorrespondencePicCompliances());
	}

	public void onAddNewSupportingUnit() {

		if (tmpCorrespondence.getTmpCorrespondenceSupportingUnits() == null) {
			tmpCorrespondence.setTmpCorrespondenceSupportingUnits(new ArrayList<TmpCorrespondenceSupportingUnit>());
			lastSequenceOfSupportingUnitModel = 0;
		} else {
			if (tmpCorrespondence.getTmpCorrespondenceSupportingUnits().size() == 0) {
				lastSequenceOfSupportingUnitModel = 0;
			}
		}

		TmpCorrespondenceSupportingUnit d = new TmpCorrespondenceSupportingUnit();
		lastSequenceOfSupportingUnitModel = lastSequenceOfSupportingUnitModel + 1;
		d.setSequence(lastSequenceOfSupportingUnitModel);

		tmpCorrespondence.getTmpCorrespondenceSupportingUnits().add(d);

		tableSupportingUnitModel.setWrappedData(tmpCorrespondence.getTmpCorrespondenceSupportingUnits());

		PrimeFaces.current().executeScript("reInitSelect2();");
		// RequestContext.getCurrentInstance().execute("reInitSelect2();");

	}

	public void onDeleteRowSupportingUnit() {
		for (int i = 0; i < selectedSupportingUnitData.length; i++) {
			tmpCorrespondence.getTmpCorrespondenceSupportingUnits().remove(selectedSupportingUnitData[i]);
		}

		if (tmpCorrespondence.getTmpCorrespondenceSupportingUnits() == null
				|| tmpCorrespondence.getTmpCorrespondenceSupportingUnits().size() == 0) {
			lastSequenceOfSupportingUnitModel = 0;
		}

		tableSupportingUnitModel.setWrappedData(tmpCorrespondence.getTmpCorrespondenceSupportingUnits());

		PrimeFaces.current().executeScript("reInitSelect2();");
//		RequestContext.getCurrentInstance().execute("reInitSelect2();");
	}
	
	public void onAddNewRefLetter() {
		if (tmpCorrespondence.getTmpCrpdcReffLetters() == null) {
			tmpCorrespondence.setTmpCrpdcReffLetters(new ArrayList<TmpCrpdcReffLetter>());
			lastSequenceOfReffLetter = 0;
		} else {
			if (tmpCorrespondence.getTmpCrpdcReffLetters().size() == 0) {
				lastSequenceOfReffLetter = 0;
			}
		}

		TmpCrpdcReffLetter d = new TmpCrpdcReffLetter();
		lastSequenceOfReffLetter = lastSequenceOfReffLetter + 1;
		d.setSequence(lastSequenceOfReffLetter);
		tmpCorrespondence.getTmpCrpdcReffLetters().add(d);
		tableReferalLetterModel.setWrappedData(tmpCorrespondence.getTmpCrpdcReffLetters());

	}

	public void onDeleteRowRefLetter() {
		for (int i = 0; i < selectedReferalLetterData.length; i++) {
			tmpCorrespondence.getTmpCrpdcReffLetters().remove(selectedReferalLetterData[i]);
		}

		if (tmpCorrespondence.getTmpCrpdcReffLetters() == null
				|| tmpCorrespondence.getTmpCrpdcReffLetters().size() == 0) {
			lastSequenceOfReffLetter = 0;
		}

		tableReferalLetterModel.setWrappedData(tmpCorrespondence.getTmpCrpdcReffLetters());
	}
	
	public void onAddNewSubPicConfirmasi() {
		if (tmpCorrespondence.getTmpCrpdcPicConfirms() == null) {
			tmpCorrespondence.setTmpCrpdcPicConfirms(new ArrayList<TmpCrpdcPicConfirm>());
			lastSequenceOfPicConfirm = 0;
		} else {
			if (tmpCorrespondence.getTmpCrpdcPicConfirms().size() == 0) {
				lastSequenceOfPicConfirm = 0;
			}
		}

		TmpCrpdcPicConfirm d = new TmpCrpdcPicConfirm();
		lastSequenceOfPicConfirm = lastSequenceOfPicConfirm + 1;
		d.setSequence(lastSequenceOfPicConfirm);
		tmpCorrespondence.getTmpCrpdcPicConfirms().add(d);
		tableSubPicConfirmModel.setWrappedData(tmpCorrespondence.getTmpCrpdcPicConfirms());

	}

	public void onDeleteRowSubPiConfiramsi() {
		for (int i = 0; i < selectedSubPicConfirmData.length; i++) {
			tmpCorrespondence.getTmpCrpdcPicConfirms().remove(selectedSubPicConfirmData[i]);
		}

		if (tmpCorrespondence.getTmpCrpdcPicConfirms() == null
				|| tmpCorrespondence.getTmpCrpdcPicConfirms().size() == 0) {
			lastSequenceOfPicConfirm = 0;
		}

		tableSubPicConfirmModel.setWrappedData(tmpCorrespondence.getTmpCrpdcPicConfirms());
	}

	public void handleFileUploadDocument(FileUploadEvent event) throws Exception {
		try {
			uploadedFilesDocument = uploadedFilesDocument == null ? new ArrayList<UploadedFileWO>()
					: uploadedFilesDocument;
			uploadedFilesDocument.add(new UploadedFileWO(CallApiManager.callUploadAPI(event.getFile(),
					Constants.COMPLIANCE_DOC_TYPE_KORESPONDENSI_SURAT_MASUK, parameterDetailService, false, fileUtil),
					event.getFile().getFileName(), event.getFile().getContentType(), event.getFile().getSize()));
		} catch (Exception e) {
			e.printStackTrace();
			facesUtil.addErrMessage(e.getMessage());
		}
	}

	private void checkNewOrEdit() {
		String editId = facesUtil.retrieveRequestParam("id");
		String token = facesUtil.retrieveRequestParam("token");

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
	}

	private void handleNew() {
		tmpCorrespondence = new TmpCorrespondence();
		disabledFollowUpStatus = false;
		tmpCorrespondence.setCounterType(new CounterType());
		tmpCorrespondence.setSenderCode(new ParameterDetail());
		tmpCorrespondence.setReminderStatus(new ParameterDetail());
		tmpCorrespondence.getReminderStatus().setParameterDtlCode(REMINDER_ACTIVE);
		ParameterDetail pd = new ParameterDetail();
		pd.setParameterDtlCode("INVITATION");
		tmpCorrespondence.setCorrespondenceCode(pd);
		tmpCorrespondence.setRc(new RC());

		lastSequenceOfPicCompliance = 0;
		lastSequenceOfSupportingUnitModel = 0;
		lastSequenceOfReffLetter = 0;
		lastSequenceOfPicConfirm = 0;

		User user1 = userService.getUserByNik(facesUtil.retrieveUserLogin());
		if (user1 != null) {
			/*
			 * tmpCorrespondence.setUserId1(user1);
			 * tmpCorrespondence.setUserNameTemp1(user1.getNik() + "-" + user1.getName());
			 * tmpCorrespondence.setDivisionId(user1.getDivisionId());
			 */

			if (tmpCorrespondence.getTmpCorrespondencePicCompliances() == null
					|| tmpCorrespondence.getTmpCorrespondencePicCompliances().size() == 0) {
				tmpCorrespondence.setTmpCorrespondencePicCompliances(new ArrayList<TmpCorrespondencePicCompliance>());
				lastSequenceOfPicCompliance = 0;
			}

			TmpCorrespondencePicCompliance picCompliance = new TmpCorrespondencePicCompliance();
			lastSequenceOfPicCompliance = lastSequenceOfPicCompliance + 1;
			picCompliance.setSequence(lastSequenceOfPicCompliance);
			picCompliance.setUser(user1);
			picCompliance.setNikTemp(user1.getNik());
			picCompliance.setNameTemp(user1.getName());
			picCompliance.setEmailTemp(user1.getEmail());
			tmpCorrespondence.getTmpCorrespondencePicCompliances().add(picCompliance);

			User user2 = userService.getUserByNik(user1.getPukNik());
			if (user2 != null) {
				/*
				 * tmpCorrespondence.setUserId2(user2);
				 * tmpCorrespondence.setUserNameTemp2(user2.getNik() + "-" + user2.getName());
				 */

				User user3 = userService.getUserByNik(user2.getPukNik());
				if (user3 != null) {
					/*
					 * tmpCorrespondence.setUserId3(user3);
					 * tmpCorrespondence.setUserNameTemp3(user3.getNik() + "-" + user3.getName());
					 */
				}
			}
		}

		uploadedFilesDocument = new ArrayList<UploadedFileWO>();

		tableSupportingUnitModel = new TmpCorrespondenceSupportingUnitTableModel<TmpCorrespondenceSupportingUnit>(
				tmpCorrespondence.getTmpCorrespondenceSupportingUnits());
		tablePicComplianceModel = new TmpCorrespondencePicComplianceTableModel<TmpCorrespondencePicCompliance>(
				tmpCorrespondence.getTmpCorrespondencePicCompliances());
		tableReferalLetterModel = new TmpCrpdcReffLetterTableModel<TmpCrpdcReffLetter>(
				tmpCorrespondence.getTmpCrpdcReffLetters());
		tableSubPicConfirmModel = new TmpCrpdcPicConfirmTableModel<TmpCrpdcPicConfirm>(
				tmpCorrespondence.getTmpCrpdcPicConfirms());

		actionMode = Constants.ACTION_ADD;
		facesUtil.setSessionAttribute("token", null);
	}

	private void handleEdit(String editId) {
		String token = facesUtil.retrieveRequestParam("token");
		if (StringUtils.isNotEmpty(token)) {
			editId = Constants.decryptString(token);
		}
		facesUtil.setSessionAttribute("token", Constants.encryptString(editId));
		actionMode = Constants.ACTION_EDIT;
		Long idLong = Long.parseLong(editId);
		tmpCorrespondence = tmpCorrespondenceService.findById(idLong);
		tmpCorrespondence.setNotes(tmpCorrespondence.getNotesDecrypted());
		tmpCorrespondence.setOldTargetDate(tmpCorrespondence.getTargetDate());
		lastSequenceOfPicCompliance = 0;
		lastSequenceOfSupportingUnitModel = 0;
		lastSequenceOfReffLetter = 0;
		lastSequenceOfPicConfirm = 0;

		TrcCorrespondence trcCorrespondence = trcCorrespondenceService.findById(idLong);
		if (trcCorrespondence != null) {

			if (trcCorrespondence.getFollowupStatus() != null) {
				disabledFollowUpStatus = true;
			} else {
				disabledFollowUpStatus = false;
			}

			trcCorrespondenceList = new ArrayList<TrcCorrespondence>();
			trcCorrespondenceList.add(trcCorrespondence);

			if (trcCorrespondenceList.get(0).getComplianceStatus() == null) {
				trcCorrespondenceList.get(0).setComplianceStatus(new ParameterDetail());
			}
		} else {
			disabledFollowUpStatus = false;
		}

		/*if (tmpCorrespondence.getUserId1() != null) {
			tmpCorrespondence.setUserNameTemp1(tmpCorrespondence.getUserId1().getName());
		}

		if (tmpCorrespondence.getUserId2() != null) {
			tmpCorrespondence.setUserNameTemp2(tmpCorrespondence.getUserId2().getName());
		}

		if (tmpCorrespondence.getUserId3() != null) {
			tmpCorrespondence.setUserNameTemp3(tmpCorrespondence.getUserId3().getName());
		} */
		
		User userInputer = userService.getUserByNik(tmpCorrespondence.getCreatedBy());
		if (userInputer != null) {
			tmpCorrespondence.setUserNameInputer(userInputer.getName());
			User atasanUserInputer = userService.getUserByNik(userInputer.getPukNik());
			if (atasanUserInputer != null) {
				tmpCorrespondence.setUserNameAtasanInputer(atasanUserInputer.getName());
			}
		}

		if (tmpCorrespondence.getTmpCorrespondencePicCompliances() != null) {
			lastSequenceOfPicCompliance = tmpCorrespondence.getTmpCorrespondencePicCompliances().size();
			for (int i = 0; i < tmpCorrespondence.getTmpCorrespondencePicCompliances().size(); i++) {
				TmpCorrespondencePicCompliance dtl = (TmpCorrespondencePicCompliance) tmpCorrespondence
						.getTmpCorrespondencePicCompliances().get(i);

				lastSequenceOfPicCompliance = lastSequenceOfPicCompliance + 1;
				dtl.setSequence(lastSequenceOfPicCompliance);

				if (dtl.getUser() != null) {
					dtl.setNikTemp(dtl.getUser().getNik());
					dtl.setNameTemp(dtl.getUser().getName());
					dtl.setEmailTemp(dtl.getUser().getEmail());
				}
			}
		}

		if (tmpCorrespondence.getTmpCorrespondenceSupportingUnits() != null) {
			lastSequenceOfSupportingUnitModel = tmpCorrespondence.getTmpCorrespondenceSupportingUnits().size();
			for (int i = 0; i < tmpCorrespondence.getTmpCorrespondenceSupportingUnits().size(); i++) {
				TmpCorrespondenceSupportingUnit dtl = (TmpCorrespondenceSupportingUnit) tmpCorrespondence
						.getTmpCorrespondenceSupportingUnits().get(i);
				lastSequenceOfSupportingUnitModel = lastSequenceOfSupportingUnitModel + 1;
				dtl.setSequence(lastSequenceOfSupportingUnitModel);

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
		
		if (tmpCorrespondence.getTmpCrpdcReffLetters() != null) {
			lastSequenceOfReffLetter = tmpCorrespondence.getTmpCrpdcReffLetters().size();
			for (int i = 0; i < tmpCorrespondence.getTmpCrpdcReffLetters().size(); i++) {
				TmpCrpdcReffLetter dtl = (TmpCrpdcReffLetter) tmpCorrespondence
						.getTmpCrpdcReffLetters().get(i);

				lastSequenceOfReffLetter = lastSequenceOfReffLetter + 1;
				dtl.setSequence(lastSequenceOfReffLetter);
				
				if (dtl.getReffLetterCorrespondence() != null) {
					dtl.setPerihal(dtl.getReffLetterCorrespondence().getPerihalIn());
					dtl.setLetterNo(dtl.getReffLetterCorrespondence().getLetterNo());
					
					if(dtl.getReffLetterCorrespondence().getTmpCorrespondenceDocuments() !=null) {
					   List<TmpReffDocument> dataList = new ArrayList<TmpReffDocument>();
					   for(TmpCorrespondenceDocument tmpRefDocument : dtl.getReffLetterCorrespondence().getTmpCorrespondenceDocuments()) {
						  TmpReffDocument refDoc = new TmpReffDocument();
						  refDoc.setAttachmentFile(tmpRefDocument.getAttachmentFile());
						  refDoc.setCorrespondenceAttachmentId(tmpRefDocument.getCorrespondenceAttachmentId());
						  refDoc.setFileId(tmpRefDocument.getFileId());
						  dataList.add(refDoc);
					   }
						
					   dtl.setReffDocumentList(dataList);		
					}
				}
			}
		}
		
		if (tmpCorrespondence.getTmpCrpdcPicConfirms() != null) {
			lastSequenceOfPicConfirm = tmpCorrespondence.getTmpCrpdcPicConfirms().size();
			for (int i = 0; i < tmpCorrespondence.getTmpCrpdcPicConfirms().size(); i++) {
				TmpCrpdcPicConfirm dtl = (TmpCrpdcPicConfirm) tmpCorrespondence
						.getTmpCrpdcPicConfirms().get(i);

				lastSequenceOfPicConfirm = lastSequenceOfPicConfirm + 1;
				dtl.setSequence(lastSequenceOfPicConfirm);
				dtl.setDivisionIdTemp(dtl.getDivisionId());
				
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

		uploadedFilesDocument = new ArrayList<UploadedFileWO>();

		for (int i = 0; i < tmpCorrespondence.getTmpCorrespondenceDocuments().size(); i++) {
			TmpCorrespondenceDocument ra = tmpCorrespondence.getTmpCorrespondenceDocuments().get(i);
			UploadedFileWO uf = new UploadedFileWO();
			uf.setFileName(ra.getAttachmentFile());
			uf.setFileId(ra.getFileId());
			uf.setIsNew(false);
			uf.setFileSize(ra.getFileSize());
			uploadedFilesDocument.add(uf);

		}

		tableSupportingUnitModel = new TmpCorrespondenceSupportingUnitTableModel<TmpCorrespondenceSupportingUnit>(
				tmpCorrespondence.getTmpCorrespondenceSupportingUnits());
		tablePicComplianceModel = new TmpCorrespondencePicComplianceTableModel<TmpCorrespondencePicCompliance>(
				tmpCorrespondence.getTmpCorrespondencePicCompliances());
		tableReferalLetterModel = new TmpCrpdcReffLetterTableModel<TmpCrpdcReffLetter>(
				tmpCorrespondence.getTmpCrpdcReffLetters());
		tableSubPicConfirmModel = new TmpCrpdcPicConfirmTableModel<TmpCrpdcPicConfirm>(
				tmpCorrespondence.getTmpCrpdcPicConfirms());
		
	}

	public Boolean validate() {
		Boolean flag = false;

		if (StringUtils.isEmpty(tmpCorrespondence.getSenderCode().getParameterDtlCode())) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpCorrespondenceSender") + " "
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}

		if (StringUtils.isEmpty(tmpCorrespondence.getCorrespondenceCode().getParameterDtlCode())) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpCorrespondenceCorrespondenceType") + " "
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}

		if (tmpCorrespondence.getLetterReceivedDate() == null) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpCorrespondenceLetterReceiveDate") + " "
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}

		if (StringUtils.isEmpty(tmpCorrespondence.getLetterNo())) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpCorrespondenceLetterNo") + " "
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}

		if (StringUtils.isEmpty(tmpCorrespondence.getPerihalIn())) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpCorrespondencePerihal") + " "
//					+ facesUtil.retrieveMessage("indonesia") + " " 
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}

		if (StringUtils.isEmpty(tmpCorrespondence.getLetterSummary())) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpCorrespondenceLetterSummary") + " "
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}

		if (tmpCorrespondence.getLetterDate() == null) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpCorrespondenceLetterDate") + " "
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}

		if (tmpCorrespondence.getCorrespondenceCode().getParameterDtlCode()
				.equals(TmpCorrespondenceConstants.PARAM_DTL_CODE_NONINVITATION)) {
			if (uploadedFilesDocument == null || uploadedFilesDocument.size() <= 0) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpCorrespondenceDocumentLetterSubTitle")
						+ " File " + facesUtil.retrieveMessage("validateRequired"));
				flag = true;
			}
		}

		Set<Long> userComplianceTemp = new HashSet<Long>();
		for (int i = 0; i < tmpCorrespondence.getTmpCorrespondencePicCompliances().size(); i++) {
			TmpCorrespondencePicCompliance dtl = (TmpCorrespondencePicCompliance) tmpCorrespondence
					.getTmpCorrespondencePicCompliances().get(i);
			if (dtl.getUser() != null && dtl.getUser().getUserId() != null) {
				if (!userComplianceTemp.add(dtl.getUser().getUserId())) {
					facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpCorrespondenceNIK") + " "
							+ facesUtil.retrieveMessage("errorDuplicate"));
					flag = true;
					break;
				}
			}

			else if (dtl.getUser() == null) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpCorrespondenceNIK") + " "
						+ facesUtil.retrieveMessage("validateRequired"));
				flag = true;
				break;

			}
		}

		if (StringUtils.isEmpty(tmpCorrespondence.getFollowUp())) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpCorrespondenceFollowup") + " "
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		} else {
			if (tmpCorrespondence.getFollowUp().equals(Constants.CONSTANT_YES)) {
				if(tmpCorrespondence.getTmpCrpdcPicConfirms() == null || 
						tmpCorrespondence.getTmpCrpdcPicConfirms().size() == 0) {
					facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpCorrespondenceSubmissionConfirmation") + " "
							+ facesUtil.retrieveMessage("validateRequired"));
					flag = true;					
				} else {
					for (int i = 0; i < tmpCorrespondence.getTmpCrpdcPicConfirms().size(); i++) {
						TmpCrpdcPicConfirm dtl = (TmpCrpdcPicConfirm) tmpCorrespondence
								.getTmpCrpdcPicConfirms().get(i);

						if (dtl.getUser1() == null) {
							facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpCorrespondencePic1") + " "
									+ facesUtil.retrieveMessage("validateRequired"));
							flag = true;
							break;
						}
					}
				}
				
				/*if (tmpCorrespondence.getUserId1() == null) {
					facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpCorrespondencePic1") + " "
							+ facesUtil.retrieveMessage("validateRequired"));
					flag = true;
				} */

				if (tmpCorrespondence.getTmpCorrespondenceSupportingUnits() == null
						|| tmpCorrespondence.getTmpCorrespondenceSupportingUnits().size() == 0) {

				} else {
					for (int i = 0; i < tmpCorrespondence.getTmpCorrespondenceSupportingUnits().size(); i++) {
						TmpCorrespondenceSupportingUnit dtl = (TmpCorrespondenceSupportingUnit) tmpCorrespondence
								.getTmpCorrespondenceSupportingUnits().get(i);

						if (dtl.getEmailCc1() == null) {
							facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpCorrespondenceEmailCc1") + " "
									+ facesUtil.retrieveMessage("validateRequired"));
							flag = true;
							break;
						}
					}
				}

				if (tmpCorrespondence.getCounterType().getCounterTypeId() == null
						|| StringUtils.isEmpty(tmpCorrespondence.getCounterType().getCounterTypeId().toString())) {
					facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpCorrespondenceReminderType") + " "
							+ facesUtil.retrieveMessage("validateRequired"));
					flag = true;
				}

				/*if (tmpCorrespondence.getTargetDate() == null) {
					facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpCorrespondenceTargetDate") + " "
							+ facesUtil.retrieveMessage("validateRequired"));
					flag = true;

				} else {
					if (tmpCorrespondenceService.hasReachedMaximumReschedule(tmpCorrespondence.getCorrespondenceId())) {
						if (!tmpCorrespondence.getTargetDate().equals(tmpCorrespondence.getOldTargetDate())) {
							facesUtil.addErrMessage(
									facesUtil.retrieveMessage("formTmpCorrespondenceTargetDateErrorReachMaximum"));
							flag = true;
						}
					}

				}*/

			} else if (tmpCorrespondence.getFollowUp().equals(Constants.CONSTANT_NO)
					&& StringUtils.isEmpty(tmpCorrespondence.getNotes())) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpCorrespondenceNote") + "  "
						+ facesUtil.retrieveMessage("validateRequired"));
				flag = true;

			}
		}

		/*
		 * if (StringUtils.isEmpty(tmpCorrespondence.getReportNameEn())) {
		 * facesUtil.addErrMessage(facesUtil.retrieveMessage(
		 * "formTmpCorrespondenceReportName") + " EN " +
		 * facesUtil.retrieveMessage("validateRequired")); flag = true; }
		 */

		if (StringUtils.isEmpty(tmpCorrespondence.getReminderStatus().getParameterDtlCode())) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpCorrespondenceStatus") + " "
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}

		/*
		 * if (tmpCorrespondence.getTmpCorrespondencePicCompliances() == null ||
		 * tmpCorrespondence.getTmpCorrespondencePicCompliances().size() == 0) {
		 * facesUtil.addErrMessage(facesUtil.retrieveMessage(
		 * "formTmpCorrespondencePicCompliance") + " " +
		 * facesUtil.retrieveMessage("validateRequired") + " min. 1 data"); flag = true;
		 * }
		 */

		return flag;
	}

	public void save() {
		try {

			if (!validate()) {

				User getUser = facesUtil.getUserLogin();

				if (tmpCorrespondence.getTmpCorrespondenceDocuments() == null
						|| tmpCorrespondence.getTmpCorrespondenceDocuments().size() == 0) {
					tmpCorrespondence.setTmpCorrespondenceDocuments(new ArrayList<TmpCorrespondenceDocument>());
				}
				tmpCorrespondence.getTmpCorrespondenceDocuments().clear();

				if (uploadedFilesDocument != null) {
					for (int i = 0; i < uploadedFilesDocument.size(); i++) {
						TmpCorrespondenceDocument doc = new TmpCorrespondenceDocument();
						UploadedFileWO uf = (UploadedFileWO) uploadedFilesDocument.get(i);
						doc.setTmpCorrespondence(tmpCorrespondence);

						doc.setAttachmentFile(uf.getFileName());
						doc.setCreatedBy(facesUtil.retrieveUserLogin());
						doc.setCreationDate(new Timestamp(new Date().getTime()));
						doc.setDelId(new Long(0));
						doc.setEnabledFlag(Constants.CONSTANT_YES);

						doc.setFileId(uf.getFileId());
						doc.setFileSize(uf.getFileSize());
						tmpCorrespondence.getTmpCorrespondenceDocuments().add(doc);
					}
				}

				if (tmpCorrespondence.getTmpCorrespondencePicCompliances() != null) {
					for (int i = 0; i < tmpCorrespondence.getTmpCorrespondencePicCompliances().size(); i++) {
						TmpCorrespondencePicCompliance dtl = (TmpCorrespondencePicCompliance) tmpCorrespondence
								.getTmpCorrespondencePicCompliances().get(i);
						dtl.setTmpCorrespondence(tmpCorrespondence);
						if (dtl.getCreatedBy() == null) {
							dtl.setCreatedBy(facesUtil.retrieveUserLogin());
							dtl.setCreationDate(new Timestamp(new Date().getTime()));
						}

						dtl.setLastUpdateBy(facesUtil.retrieveUserLogin());
						dtl.setLastUpdateDate(new Timestamp(new Date().getTime()));
						dtl.setDelId(new Long(0));
						dtl.setEnabledFlag(Constants.CONSTANT_YES);
					}
				}

				if (tmpCorrespondence.getTmpCorrespondenceSupportingUnits() != null) {
					for (int i = 0; i < tmpCorrespondence.getTmpCorrespondenceSupportingUnits().size(); i++) {
						TmpCorrespondenceSupportingUnit dtl = (TmpCorrespondenceSupportingUnit) tmpCorrespondence
								.getTmpCorrespondenceSupportingUnits().get(i);
						dtl.setTmpCorrespondence(tmpCorrespondence);
						if (dtl.getCreatedBy() == null) {
							dtl.setCreatedBy(facesUtil.retrieveUserLogin());
							dtl.setCreationDate(new Timestamp(new Date().getTime()));
						}

						dtl.setLastUpdateBy(facesUtil.retrieveUserLogin());
						dtl.setLastUpdateDate(new Timestamp(new Date().getTime()));
						dtl.setDelId(new Long(0));
						dtl.setEnabledFlag(Constants.CONSTANT_YES);
					}
				}
				
				if (tmpCorrespondence.getTmpCrpdcReffLetters() != null) {
					for (int i = 0; i < tmpCorrespondence.getTmpCrpdcReffLetters().size(); i++) {
						TmpCrpdcReffLetter dtl = (TmpCrpdcReffLetter) tmpCorrespondence
								.getTmpCrpdcReffLetters().get(i);
						dtl.setTmpCorrespondence(tmpCorrespondence);
						if (dtl.getCreatedBy() == null) {
							dtl.setCreatedBy(facesUtil.retrieveUserLogin());
							dtl.setCreationDate(new Timestamp(new Date().getTime()));
						}

						dtl.setLastUpdateBy(facesUtil.retrieveUserLogin());
						dtl.setLastUpdateDate(new Timestamp(new Date().getTime()));
						dtl.setDelId(new Long(0));
						dtl.setEnabledFlag(Constants.CONSTANT_YES);
					}
				}
				
				if (tmpCorrespondence.getTmpCrpdcPicConfirms() != null) {
					for (int i = 0; i < tmpCorrespondence.getTmpCrpdcPicConfirms().size(); i++) {
						TmpCrpdcPicConfirm dtl = (TmpCrpdcPicConfirm) tmpCorrespondence
								.getTmpCrpdcPicConfirms().get(i);
						dtl.setTmpCorrespondence(tmpCorrespondence);
						dtl.setDivisionId(dtl.getDivisionIdTemp());
						if (dtl.getCreatedBy() == null) {
							dtl.setCreatedBy(facesUtil.retrieveUserLogin());
							dtl.setCreationDate(new Timestamp(new Date().getTime()));
						}

						dtl.setLastUpdateBy(facesUtil.retrieveUserLogin());
						dtl.setLastUpdateDate(new Timestamp(new Date().getTime()));
						dtl.setDelId(new Long(0));
						dtl.setEnabledFlag(Constants.CONSTANT_YES);
					}
				}

				ParameterDetail pdReminderStatus = parameterDetailService
						.getParameterDetailByParamDtlCode(tmpCorrespondence.getReminderStatus().getParameterDtlCode());
				tmpCorrespondence.setReminderStatus(pdReminderStatus);
				tmpCorrespondence.setNotes(tmpCorrespondence.getNotesEncrypted());
				tmpCorrespondence.setUserDivisionId(getUser.getDivisionId());

				// APPROVE PROCESS
				TmpCorrespondenceApproval dtl = new TmpCorrespondenceApproval();
				dtl.setTmpCorrespondence(tmpCorrespondence);
				dtl.setApprovalDate(new Date());
				dtl.setApprovalNote("APPROVE BY SYSTEM");
				dtl.setApprovalStatus(parameterDetailService
						.getParameterDetailByParamDtlCode(ParameterDetail.PARAM_DET_CODE_STATUS_APPROVED));
				dtl.setUser(userService.getUserByNik(facesUtil.retrieveUserLogin()));
				if (dtl.getCreatedBy() == null) {
					dtl.setCreatedBy(facesUtil.retrieveUserLogin());
					dtl.setCreationDate(new Timestamp(new Date().getTime()));
				}

				dtl.setDelId(new Long(0));
				dtl.setEnabledFlag(Constants.CONSTANT_YES);

				List<TmpCorrespondenceApproval> listApproval = new ArrayList<TmpCorrespondenceApproval>();
				listApproval.add(dtl);

				/*
				 * if(tmpCorrespondence.getTmpCorrespondenceApprovals()!=null &&
				 * tmpCorrespondence.getTmpCorrespondenceApprovals().size()>0){
				 * tmpCorrespondence.getTmpCorrespondenceApprovals().clear();
				 * tmpCorrespondence.getTmpCorrespondenceApprovals().add(dtl); }else{
				 * tmpCorrespondence.setTmpCorrespondenceApprovals(listApproval); }
				 */

				// APPROVE PROCESS
				if (tmpCorrespondence.getCorrespondenceId() != null) {
					tmpCorrespondence.setStatus(parameterDetailService
							.getParameterDetailByParamDtlCode(ParameterDetail.PARAM_DET_CODE_DATA_INACTIVE));
					tmpCorrespondence.setLastUpdateBy(facesUtil.retrieveUserLogin());
					tmpCorrespondence.setLastUpdateDate(new Timestamp(new Date().getTime()));
					tmpCorrespondence.setDelId(new Long(0));
					tmpCorrespondence.setEnabledFlag(Constants.CONSTANT_YES);
					tmpCorrespondence.getTmpCorrespondenceApprovals().clear();
					tmpCorrespondence.getTmpCorrespondenceApprovals().add(dtl);
					tmpCorrespondenceService.update(tmpCorrespondence);
				} else {
					tmpCorrespondence.setStatus(parameterDetailService
							.getParameterDetailByParamDtlCode(ParameterDetail.PARAM_DET_CODE_DATA_INACTIVE));
					tmpCorrespondence.setCreatedBy(facesUtil.retrieveUserLogin());
					tmpCorrespondence.setCreationDate(new Timestamp(new Date().getTime()));
					tmpCorrespondence.setDelId(new Long(0));
					tmpCorrespondence.setEnabledFlag(Constants.CONSTANT_YES);
					tmpCorrespondence.setTmpCorrespondenceApprovals(listApproval);
					tmpCorrespondenceService.save(tmpCorrespondence);
				}

				TmpCorrespondence tmpCorrespondence2 = tmpCorrespondenceService
						.findById(tmpCorrespondence.getCorrespondenceId());
				tmpCorrespondence2.setStatus(parameterDetailService
						.getParameterDetailByParamDtlCode(ParameterDetail.PARAM_DET_CODE_DATA_ACTIVE));
				tmpCorrespondenceService.update(tmpCorrespondence2);

				// this should be a singleton
				ExecutorService emailExecutor = Executors.newCachedThreadPool();

				// from you sendEmail() method
				emailExecutor.execute(new Runnable() {
					@Override
					public void run() {
						try {
							sendEmail();
						} catch (Exception e) {
							logger.error("send email failed", e);
						}
					}
				});

				if (deletedFiles != null) {
					for (int i = 0; i < deletedFiles.size(); i++) {
						UploadedFileWO uf = (UploadedFileWO) deletedFiles.get(i);
						CallApiManager.deleteFile(uf.getFileId(), parameterDetailService, fileUtil);
					}
				}

				facesUtil.redirect("/pages/tmpCorrespondence/tmpCorrespondence.faces");
			}

		} catch (Exception ex) {
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + ex.getMessage(), "");
			ex.printStackTrace();
		}

	}

	public void cancel() {
		try {
			for (int i = 0; i < uploadedFilesDocument.size(); i++) {
				UploadedFileWO uf = (UploadedFileWO) uploadedFilesDocument.get(i);
				if (uf.getIsNew() == null) {
					CallApiManager.deleteFile(uf.getFileId(), parameterDetailService, fileUtil);
				}
			}

			facesUtil.redirect("/pages/tmpCorrespondence/tmpCorrespondence.faces");
		} catch (Exception ex) {
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + ex.getMessage(), "");
		}
	}

	public void deleteAttachment(String fileId, int index) throws Exception {
		deletedFiles = deletedFiles != null ? deletedFiles : new ArrayList<UploadedFileWO>();
		deletedFiles.add(new UploadedFileWO(fileId, null, null, null));
		uploadedFilesDocument.remove(uploadedFilesDocument.get(index));
	}

	public void downloadFile(String fileId, String fileName, byte[] content) throws Exception {
		CallApiManager.downloadFile(fileId, fileName, content, parameterDetailService);
	}

	/*
	 * public void sendEmail() { try {
	 * 
	 * EmailTemplate emailTemplate = emailTemplateService
	 * .getEmailTemplateByEmailTemplateCode(Constants.EMAIL_APPROVAL); String
	 * emailSubject = emailTemplate.getEmailSubject();
	 * 
	 * String emailContent = ""; String emailTo = ""; String emailCc = "";
	 * 
	 * String letterNoTemp = tmpCorrespondence.getLetterNo() != null ? " - " +
	 * tmpCorrespondence.getLetterNo() : "";
	 * 
	 * emailContent = emailTemplate.getEmailContent().replace(Constants.
	 * NOTIFICATION_TYPE_AND_DOC_NUM, "Correspondence" + letterNoTemp);
	 * 
	 * ParameterDetail paramEmail = parameterDetailService
	 * .getParameterDetailByParamDtlCode(Constants.OSCAR_CHECKER); emailTo =
	 * paramEmail.getNameIn();
	 * 
	 * final String subject = emailSubject; final String content = emailContent;
	 * final String to = emailTo; // final String to = "h3ndr407@gmail.com"; final
	 * String cc = emailCc;
	 * 
	 * CallApiManager.sendEmailAPI(to, cc, subject, content, "EMAIL_CORRESPONDENCE",
	 * "true", parameterDetailService);
	 * 
	 * } catch (Exception ex) { ex.printStackTrace(); throw new
	 * CustomAPIException(ex.getMessage()); }
	 * 
	 * }
	 */

	@SuppressWarnings("unused")
	public void sendEmail() {
		try {

			EmailTemplate emailTemplate = emailTemplateService
					.getEmailTemplateByEmailTemplateCode("EMAIL_KORESPONDENSI");
			String emailSubject = emailTemplate.getEmailSubject().replaceAll("counter_type", "NOTIFICATION");
			emailSubject = emailSubject.replaceAll("perihal_in", tmpCorrespondence.getPerihalIn());
			emailSubject = emailSubject.replaceAll("perihal_en", tmpCorrespondence.getPerihalEn());
			emailSubject = emailSubject.replaceAll("letter_no", tmpCorrespondence.getLetterNo());
			String emailContent = "";
			String emailTo = "";
			String emailCc1 = "";
			String emailCc2 = "";
			String emailCc = "";
			String emailUserInputerCc = "";
			String emailAtasanUserInputerCc = "";
			String emailCcSupporting = "";

			ParameterDetail pdHostName = parameterDetailService
					.getParameterDetailByParamDtlCode(Constants.HOST_NAME_APPLICATION);

			if (tmpCorrespondence.getReminderStatus() != null
					&& tmpCorrespondence.getReminderStatus().getParameterDtlCode().equals(Constants.REMINDER_ACTIVE)
					&& tmpCorrespondence.getFollowUp() != null
					&& tmpCorrespondence.getFollowUp().equals(Constants.CONSTANT_YES)
			// tmpCorrespondence.getUserId1() != null
			) {
				String token = Constants.encryptString(tmpCorrespondence.getCorrespondenceId().toString());
				String menuId = "";
				String urlLink = "";
				if (tmpCorrespondence.getFollowUp() != null
						&& tmpCorrespondence.getFollowUp().equals(Constants.CONSTANT_YES)) {
					menuId = Constants.encryptString(Constants.MENU_ID_FOLLOWUP_CONFIRMATION_CORRESPONDENCE);
					urlLink = pdHostName.getNameIn().concat(
							"pages/correspondenceFE/correspondenceFEEdit.faces?token=" + token + "&menuId=" + menuId);
				} /*
					 * else if (tmpCorrespondence.getFollowUp() != null &&
					 * tmpCorrespondence.getFollowUp().equals("N")) { menuId =
					 * Constants.encryptString(Constants.MENU_ID_CORRESPONDENCE_VIEW); urlLink =
					 * pdHostName.getNameIn().concat(
					 * "pages/correspondenceFE/correspondenceFEView.faces?token="+token+"&menuId="+
					 * menuId); }
					 */

				emailContent = emailTemplate.getEmailContent().replaceAll("target_date",
						tmpCorrespondence.getTargetDate() != null ? sdf.format(tmpCorrespondence.getTargetDate()) : "");
				emailContent = emailContent.replaceAll("perihal_in", tmpCorrespondence.getPerihalIn());
				emailContent = emailContent.replaceAll("perihal_en", tmpCorrespondence.getPerihalEn());
				if (tmpCorrespondence.getSenderCode() != null
						&& tmpCorrespondence.getSenderCode().getParameterDtlCode() != null) {
					ParameterDetail psSenderCode = parameterDetailService
							.getParameterDetailByParamDtlCode(tmpCorrespondence.getSenderCode().getParameterDtlCode());
					emailContent = emailContent.replaceAll("sender_in",
							psSenderCode.getNameIn() != null ? psSenderCode.getNameIn() : "NA");
					emailContent = emailContent.replaceAll("sender_en",
							psSenderCode.getNameEn() != null ? psSenderCode.getNameEn() : "NA");
				} else {
					emailContent = emailContent.replaceAll("sender_in", "NA");
					emailContent = emailContent.replaceAll("sender_en", "NA");
				}
			
				
				/* 20250903 ditutup				
				emailContent = emailContent.replaceAll("division_name",
						(tmpCorrespondence.getDivisionId() != null
								? userService.getDivisionNameByDivisionId(tmpCorrespondence.getDivisionId())
								: "NA")); 
				emailContent = emailContent.replaceAll("pic_1_name",
						(tmpCorrespondence.getUserId1() != null ? tmpCorrespondence.getUserId1().getName() : "NA"));
				emailContent = emailContent.replaceAll("pic_2_name",
						(tmpCorrespondence.getUserId2() != null ? tmpCorrespondence.getUserId2().getName() : "NA"));
				emailContent = emailContent.replaceAll("pic_3_name",
						(tmpCorrespondence.getUserId3() != null ? tmpCorrespondence.getUserId3().getName() : "NA"));
				*/
				
				// 20250903 diganti pakai ini				
				if (tmpCorrespondence.getTmpCrpdcPicConfirms() != null
						&& tmpCorrespondence.getTmpCrpdcPicConfirms().size() > 0) {
					String divisionName = "";
					String user1Name = "";
					String user2Name = "";
					String user3Name = "";
				    for(TmpCrpdcPicConfirm dataPic : tmpCorrespondence.getTmpCrpdcPicConfirms()) {
						String divDataName = userService.getDivisionNameByDivisionId(dataPic.getDivisionId());
						if(StringUtils.isNotEmpty(divDataName)) {
							divisionName = divisionName.concat("<br>").concat(divDataName);
						}else {
							divisionName = "NA";
						}							
						
						if(dataPic.getUser1() !=null && dataPic.getUser1().getName() !=null) {
							user1Name = user1Name.concat("<br>").concat(dataPic.getUser1().getName());
						}else {
							user1Name = "NA";
						}
						
						if(dataPic.getUser2() !=null && dataPic.getUser2().getName() !=null) {
							user2Name = user2Name.concat("<br>").concat(dataPic.getUser2().getName());
						}else {
							user2Name = "NA";
						}
						
						if(dataPic.getUser3() !=null && dataPic.getUser3().getName() !=null) {
							user3Name = user3Name.concat("<br>").concat(dataPic.getUser3().getName());
						}else {
							user3Name = "NA";
						}
					}
						
					emailContent = emailContent.replaceAll("division_name", divisionName); 
					emailContent = emailContent.replaceAll("pic_1_name", user1Name);
					emailContent = emailContent.replaceAll("pic_2_name", user2Name);
					emailContent = emailContent.replaceAll("pic_3_name", user3Name);					
				}else {
					emailContent = emailContent.replaceAll("division_name", "NA"); 
					emailContent = emailContent.replaceAll("pic_1_name", "NA");
					emailContent = emailContent.replaceAll("pic_2_name", "NA");
					emailContent = emailContent.replaceAll("pic_3_name", "NA");		
				}					
				
				emailContent = emailContent.replaceAll("letter_no", tmpCorrespondence.getLetterNo());
				emailContent = emailContent.replaceAll("receive_letter_date",
						tmpCorrespondence.getLetterReceivedDate() != null
								? sdf.format(tmpCorrespondence.getLetterReceivedDate())
								: "");
				emailContent = emailContent.replaceAll("letter_date",
						tmpCorrespondence.getLetterDate() != null ? sdf.format(tmpCorrespondence.getLetterDate()) : "");
				emailContent = emailContent.replaceAll("url_link", urlLink);

				// add by dwi
				emailContent = emailContent.replaceAll("summary_in", tmpCorrespondence.getLetterSummary());
				
				tmpCorrespondence.setNotes(tmpCorrespondence.getNotesDecrypted());
				emailContent = emailContent.replaceAll("notes_in", tmpCorrespondence.getNotes());
				

				if (tmpCorrespondence.getTmpCorrespondenceSupportingUnits() != null
						&& tmpCorrespondence.getTmpCorrespondenceSupportingUnits().size() > 0) {
					if (tmpCorrespondence.getTmpCorrespondenceSupportingUnits().size() == 1) {
						TmpCorrespondenceSupportingUnit trsu = tmpCorrespondence.getTmpCorrespondenceSupportingUnits()
								.get(0);
						User user1 = (trsu != null && trsu.getEmailCc1() != null ? trsu.getEmailCc1() : null);
						User user2 = (trsu != null && trsu.getEmailCc2() != null ? trsu.getEmailCc2() : null);
						User user3 = (trsu != null && trsu.getEmailCc3() != null ? trsu.getEmailCc3() : null);
						Long divisionId = (trsu != null && trsu.getDivisionId() != null ? trsu.getDivisionId() : null);
						String divisionName = "";

						if (user1 != null || user2 != null || user3 != null) {
							if (user3 != null) {
								user3 = userService.findById(user3.getUserId());
								if (user3.getDivisionId() != null && divisionId != null
										&& user3.getDivisionId().longValue() == divisionId.longValue()) {
									divisionName = user3.getDivisionName();
								}
							}
							if (user2 != null) {
								user2 = userService.findById(user2.getUserId());
								if (user2.getDivisionId() != null && divisionId != null
										&& user2.getDivisionId().longValue() == divisionId.longValue()) {
									divisionName = user2.getDivisionName();
								}
							}
							if (user1 != null) {
								user1 = userService.findById(user1.getUserId());
								if (user1.getDivisionId() != null && divisionId != null
										&& user1.getDivisionId().longValue() == divisionId.longValue()) {
									divisionName = user1.getDivisionName();
								}
							}
						}

						emailContent = emailContent.replaceAll("supporting_name_division",
								StringUtils.isNotBlank(divisionName) ? divisionName : "NA");
						emailContent = emailContent.replaceAll("supporting_pic_name_1",
								(user1 != null ? user1.getName() : "NA"));
						emailContent = emailContent.replaceAll("supporting_pic_name_2",
								(user2 != null ? user2.getName() : "NA"));
						emailContent = emailContent.replaceAll("supporting_pic_name_3",
								(user3 != null ? user3.getName() : "NA"));
					} else {
						if (emailContent.contains("<tbody>") && emailContent.contains("</tbody>")
								&& emailContent.contains("supporting_")) {
							for (int z = -1; (z = emailContent.indexOf("<tbody>", z + 1)) != -1; z++) {
								String partial = emailContent.substring(z + 7, emailContent.indexOf("</tbody>", z + 7));
								if (partial.contains("supporting_")) {

									TmpCorrespondenceSupportingUnit trsu = tmpCorrespondence
											.getTmpCorrespondenceSupportingUnits().get(0);
									User user1 = (trsu != null && trsu.getEmailCc1() != null ? trsu.getEmailCc1()
											: null);
									User user2 = (trsu != null && trsu.getEmailCc2() != null ? trsu.getEmailCc2()
											: null);
									User user3 = (trsu != null && trsu.getEmailCc3() != null ? trsu.getEmailCc3()
											: null);
									Long divisionId = (trsu != null && trsu.getDivisionId() != null
											? trsu.getDivisionId()
											: null);
									String divisionName = "";

									if (user1 != null || user2 != null || user3 != null) {
										if (user3 != null) {
											user3 = userService.findById(user3.getUserId());
											if (user3.getDivisionId() != null && divisionId != null
													&& user3.getDivisionId().longValue() == divisionId.longValue()) {
												divisionName = user3.getDivisionName();
											}
										}
										if (user2 != null) {
											user2 = userService.findById(user2.getUserId());
											if (user2.getDivisionId() != null && divisionId != null
													&& user2.getDivisionId().longValue() == divisionId.longValue()) {
												divisionName = user2.getDivisionName();
											}
										}
										if (user1 != null) {
											user1 = userService.findById(user1.getUserId());
											if (user1.getDivisionId() != null && divisionId != null
													&& user1.getDivisionId().longValue() == divisionId.longValue()) {
												divisionName = user1.getDivisionName();
											}
										}
									}
									emailContent = emailContent.replaceAll("supporting_name_division",
											StringUtils.isNotBlank(divisionName) ? divisionName : "NA");
									emailContent = emailContent.replaceAll("supporting_pic_name_1",
											(user1 != null ? user1.getName() : "NA"));
									emailContent = emailContent.replaceAll("supporting_pic_name_2",
											(user2 != null ? user2.getName() : "NA"));
									emailContent = emailContent.replaceAll("supporting_pic_name_3",
											(user3 != null ? user3.getName() : "NA"));
									String temp = "";
									for (int y = 1; y < tmpCorrespondence.getTmpCorrespondenceSupportingUnits()
											.size(); y++) {
										temp += partial;

										trsu = tmpCorrespondence.getTmpCorrespondenceSupportingUnits().get(y);
										user1 = (trsu != null && trsu.getEmailCc1() != null ? trsu.getEmailCc1()
												: null);
										user2 = (trsu != null && trsu.getEmailCc2() != null ? trsu.getEmailCc2()
												: null);
										user3 = (trsu != null && trsu.getEmailCc3() != null ? trsu.getEmailCc3()
												: null);
										divisionId = (trsu != null && trsu.getDivisionId() != null
												? trsu.getDivisionId()
												: null);
										divisionName = "";

										if (user1 != null || user2 != null || user3 != null) {
											if (user3 != null) {
												user3 = userService.findById(user3.getUserId());
												if (user3.getDivisionId() != null && divisionId != null && user3
														.getDivisionId().longValue() == divisionId.longValue()) {
													divisionName = user3.getDivisionName();
												}
											}
											if (user2 != null) {
												user2 = userService.findById(user2.getUserId());
												if (user2.getDivisionId() != null && divisionId != null && user2
														.getDivisionId().longValue() == divisionId.longValue()) {
													divisionName = user2.getDivisionName();
												}
											}
											if (user1 != null) {
												user1 = userService.findById(user1.getUserId());
												if (user1.getDivisionId() != null && divisionId != null && user1
														.getDivisionId().longValue() == divisionId.longValue()) {
													divisionName = user1.getDivisionName();
												}
											}
										}

										temp = temp.replaceAll("supporting_name_division",
												StringUtils.isNotBlank(divisionName) ? divisionName : "NA");
										temp = temp.replaceAll("supporting_pic_name_1",
												(user1 != null ? user1.getName() : "NA"));
										temp = temp.replaceAll("supporting_pic_name_2",
												(user2 != null ? user2.getName() : "NA"));
										temp = temp.replaceAll("supporting_pic_name_3",
												(user3 != null ? user3.getName() : "NA"));
									}
									emailContent = EmailUtil.insertString(emailContent, temp,
											emailContent.indexOf("</tbody>", z + 7) - 1);
									break;
								}
							}
						}
					}
				} else {
					emailContent = emailContent.replaceAll("supporting_name_division", "NA");
					emailContent = emailContent.replaceAll("supporting_pic_name_1", "NA");
					emailContent = emailContent.replaceAll("supporting_pic_name_2", "NA");
					emailContent = emailContent.replaceAll("supporting_pic_name_3", "NA");
				}

				for (int i = 0; tmpCorrespondence.getTmpCorrespondenceSupportingUnits() != null
						&& i < tmpCorrespondence.getTmpCorrespondenceSupportingUnits().size(); i++) {
					TmpCorrespondenceSupportingUnit su = tmpCorrespondence.getTmpCorrespondenceSupportingUnits().get(i);
					if (su.getEmailCc1() != null && su.getEmailCc1().getEmail() != null) {
						emailCcSupporting = emailCcSupporting != ""
								? (emailCcSupporting.concat(",").concat(su.getEmailCc1().getEmail()))
								: emailCcSupporting.concat(su.getEmailCc1().getEmail());
					}
					if (su.getEmailCc2() != null && su.getEmailCc2().getEmail() != null) {
						emailCcSupporting = emailCcSupporting != ""
								? (emailCcSupporting.concat(",").concat(su.getEmailCc2().getEmail()))
								: emailCcSupporting.concat(su.getEmailCc2().getEmail());
					}
					if (su.getEmailCc3() != null && su.getEmailCc3().getEmail() != null) {
						emailCcSupporting = emailCcSupporting != ""
								? (emailCcSupporting.concat(",").concat(su.getEmailCc3().getEmail()))
								: emailCcSupporting.concat(su.getEmailCc3().getEmail());
					}
				}

//					for(int x=0;x<tmpCorrespondence.getCounterType().getDetails().size();x++) {
				if (tmpCorrespondence != null) {
					if (tmpCorrespondence.getCounterType() != null
							&& tmpCorrespondence.getCounterType().getDetails() != null
							&& tmpCorrespondence.getCounterType().getDetails().size() > 0) {
						CounterTypeDtl cd = tmpCorrespondence.getCounterType().getDetails().get(0);
						emailTo = cd.getEmailTo();
						emailCc1 = cd.getEmailCc1();
						emailCc2 = cd.getEmailCc2();
						
						/*
						if (emailTo.equals(Constants.REMINDER_PIC1)) {
							emailTo = tmpCorrespondence.getUserId1() != null ? tmpCorrespondence.getUserId1().getEmail()
									: "";
						} else if (emailTo.equals(Constants.REMINDER_PIC2)) {
							emailTo = tmpCorrespondence.getUserId2() != null ? tmpCorrespondence.getUserId2().getEmail()
									: "";
						} else if (emailTo.equals(Constants.REMINDER_PIC3)) {
							emailTo = tmpCorrespondence.getUserId3() != null ? tmpCorrespondence.getUserId3().getEmail()
									: "";
						}

						if (emailCc1 != null && emailCc1.equals(Constants.REMINDER_PIC1)) {
							emailCc1 = tmpCorrespondence.getUserId1() != null
									? tmpCorrespondence.getUserId1().getEmail()
									: "";
						} else if (emailCc1 != null && emailCc1.equals(Constants.REMINDER_PIC2)) {
							emailCc1 = tmpCorrespondence.getUserId2() != null
									? tmpCorrespondence.getUserId2().getEmail()
									: "";
						} else if (emailCc1 != null && emailCc1.equals(Constants.REMINDER_PIC3)) {
							emailCc1 = tmpCorrespondence.getUserId3() != null
									? tmpCorrespondence.getUserId3().getEmail()
									: "";
						}

						if (emailCc2 != null && emailCc2.equals(Constants.REMINDER_PIC1)) {
							emailCc2 = tmpCorrespondence.getUserId1() != null
									? tmpCorrespondence.getUserId1().getEmail()
									: "";
						} else if (emailCc2 != null && emailCc2.equals(Constants.REMINDER_PIC2)) {
							emailCc2 = tmpCorrespondence.getUserId2() != null
									? tmpCorrespondence.getUserId2().getEmail()
									: "";
						} else if (emailCc2 != null && emailCc2.equals(Constants.REMINDER_PIC3)) {
							emailCc2 = tmpCorrespondence.getUserId3() != null
									? tmpCorrespondence.getUserId3().getEmail()
									: "";
						}*/
						
						String emailToConcate = "";
						String emailCc1Concate = "";
						String emailCc2Concate = "";
						for(TmpCrpdcPicConfirm tmpData : tmpCorrespondence.getTmpCrpdcPicConfirms()) {
							if (emailTo !=null && emailTo.equals(Constants.REMINDER_PIC1) && 
									tmpData.getUser1() != null ) {
								emailToConcate = emailToConcate != "" ? (emailToConcate.concat(",").concat(tmpData.getUser1().getEmail()))
										: emailToConcate.concat(tmpData.getUser1().getEmail());
							} else if (emailTo !=null && emailTo.equals(Constants.REMINDER_PIC2) && 
									tmpData.getUser2() != null ) {
								emailToConcate = emailToConcate != "" ? (emailToConcate.concat(",").concat(tmpData.getUser2().getEmail()))
										: emailToConcate.concat(tmpData.getUser2().getEmail());
							} else if (emailTo !=null && emailTo.equals(Constants.REMINDER_PIC3) && 
									tmpData.getUser3() != null ) {
								emailToConcate = emailToConcate != "" ? (emailToConcate.concat(",").concat(tmpData.getUser3().getEmail()))
										: emailToConcate.concat(tmpData.getUser3().getEmail());
							}		
							
							if (emailCc1 !=null && emailCc1.equals(Constants.REMINDER_PIC1) && 
									tmpData.getUser1() != null ) {
								emailCc1Concate = emailCc1Concate != "" ? (emailCc1Concate.concat(",").concat(tmpData.getUser1().getEmail()))
										: emailCc1Concate.concat(tmpData.getUser1().getEmail());
							} else if (emailCc1 !=null && emailCc1.equals(Constants.REMINDER_PIC2) && 
									tmpData.getUser2() != null ) {
								emailCc1Concate = emailCc1Concate != "" ? (emailCc1Concate.concat(",").concat(tmpData.getUser2().getEmail()))
										: emailCc1Concate.concat(tmpData.getUser2().getEmail());
							} else if (emailCc1 !=null && emailCc1.equals(Constants.REMINDER_PIC3) && 
									tmpData.getUser3() != null ) {
								emailCc1Concate = emailCc1Concate != "" ? (emailCc1Concate.concat(",").concat(tmpData.getUser3().getEmail()))
										: emailCc1Concate.concat(tmpData.getUser3().getEmail());
							}		
							
							if (emailCc2 !=null && emailCc2.equals(Constants.REMINDER_PIC1) && 
									tmpData.getUser1() != null ) {
								emailCc2Concate = emailCc2Concate != "" ? (emailCc2Concate.concat(",").concat(tmpData.getUser1().getEmail()))
										: emailCc2Concate.concat(tmpData.getUser1().getEmail());
							} else if (emailCc2 !=null && emailCc2.equals(Constants.REMINDER_PIC2) && 
									tmpData.getUser2() != null ) {
								emailCc2Concate = emailCc2Concate != "" ? (emailCc2Concate.concat(",").concat(tmpData.getUser2().getEmail()))
										: emailCc2Concate.concat(tmpData.getUser2().getEmail());
							} else if (emailCc2 !=null && emailCc2.equals(Constants.REMINDER_PIC3) && 
									tmpData.getUser3() != null ) {
								emailCc2Concate = emailCc2Concate != "" ? (emailCc2Concate.concat(",").concat(tmpData.getUser3().getEmail()))
										: emailCc2Concate.concat(tmpData.getUser3().getEmail());
							}	
						}
						
						emailTo = emailToConcate;
						emailCc1 = emailCc1Concate;
						emailCc2 = emailCc2Concate;						

					} else {
						/*
						 * emailTo = tmpCorrespondence.getUserId1() != null ?
						 * tmpCorrespondence.getUserId1().getEmail() : ""; emailCc1 =
						 * tmpCorrespondence.getUserId2() != null ?
						 * tmpCorrespondence.getUserId2().getEmail() : ""; emailCc2 =
						 * tmpCorrespondence.getUserId3() != null ?
						 * tmpCorrespondence.getUserId3().getEmail() : "";
						 */
						for(TmpCrpdcPicConfirm tmpData : tmpCorrespondence.getTmpCrpdcPicConfirms()) {
							if (tmpData.getUser1() != null ) {
								emailTo = emailTo != "" ? (emailTo.concat(",").concat(tmpData.getUser1().getEmail()))
										: emailTo.concat(tmpData.getUser1().getEmail());
							}							
							if (tmpData.getUser2() != null ) {
								emailCc1 = emailCc1 != "" ? (emailCc1.concat(",").concat(tmpData.getUser2().getEmail()))
										: emailCc1.concat(tmpData.getUser2().getEmail());
							}
							if (tmpData.getUser3() != null ) {
								emailCc2 = emailCc2 != "" ? (emailCc2.concat(",").concat(tmpData.getUser3().getEmail()))
										: emailCc2.concat(tmpData.getUser3().getEmail());
							}	
						}
					}
					
					User userInputer = userService.getUserByNik(tmpCorrespondence.getCreatedBy());
					if (userInputer != null) {
						emailUserInputerCc = userInputer.getEmail();
						
						User atasanUserInputer = userService.getUserByNik(userInputer.getPukNik());
						if (atasanUserInputer != null) 
						{
							emailAtasanUserInputerCc = atasanUserInputer.getEmail();
						}
					}
					
					if (StringUtils.isNotEmpty(emailCc1)) {
						emailCc = emailCc.concat(emailCc1);
					}
					if (StringUtils.isNotEmpty(emailCc2)) {
						emailCc = StringUtils.isNotEmpty(emailCc) ? emailCc.concat(",").concat(emailCc2)
								: emailCc.concat(emailCc2);
					}
					
					if (StringUtils.isNotEmpty(emailUserInputerCc)) {
						if (StringUtils.isNotEmpty(emailCc)) {
							emailCc = emailCc.concat(",").concat(emailUserInputerCc);
						} else {
							emailCc = emailUserInputerCc;
						}
					}
					
					if (StringUtils.isNotEmpty(emailAtasanUserInputerCc)) {
						if (StringUtils.isNotEmpty(emailCc)) {
							emailCc = emailCc.concat(",").concat(emailAtasanUserInputerCc);
						} else {
							emailCc = emailAtasanUserInputerCc;
						}
					}
					
					
					if (StringUtils.isNotEmpty(emailCcSupporting)) {
						if (StringUtils.isNotEmpty(emailCc)) {
							emailCc = emailCc.concat(",").concat(emailCcSupporting);
						} else {
							emailCc = emailCcSupporting;
						}
					}

					ExecutorService emailExecutor = Executors.newCachedThreadPool();

					/* 20250903 ditutup
					if (tmpCorrespondence.getUserId1() == null && StringUtils.isNotEmpty(emailCcSupporting)) {
						emailTo = emailCcSupporting;
					}*/
					
					// 20250903 diganti
					if (tmpCorrespondence.getTmpCrpdcPicConfirms() == null && StringUtils.isNotEmpty(emailCcSupporting)) {
						emailTo = emailCcSupporting;
					}

					final String subject = emailSubject;
					final String content = emailContent;
					final String to = emailTo;
					final String cc = emailCc;

					System.out.println("subject==" + subject);
					System.out.println("content==" + content);
					System.out.println("to==" + to);
					System.out.println("cc==" + cc);

					if (emailTo != null && !emailTo.isEmpty()) {
						CallApiManager.sendEmailAPI(to, cc, subject, content, "EMAIL_CORESPONDENCE", "true",
								parameterDetailService);
					}
				}

			}

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new CustomAPIException(ex.getMessage());
		}

	}
	
	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

	public TmpCorrespondenceService getTmpCorrespondenceService() {
		return tmpCorrespondenceService;
	}

	public void setTmpCorrespondenceService(TmpCorrespondenceService tmpCorrespondenceService) {
		this.tmpCorrespondenceService = tmpCorrespondenceService;
	}

	public TmpCorrespondence getTmpCorrespondence() {
		return tmpCorrespondence;
	}

	public void setTmpCorrespondence(TmpCorrespondence tmpCorrespondence) {
		this.tmpCorrespondence = tmpCorrespondence;
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

	public CounterTypeService getCounterTypeService() {
		return counterTypeService;
	}

	public void setCounterTypeService(CounterTypeService counterTypeService) {
		this.counterTypeService = counterTypeService;
	}

	public TmpCorrespondencePicComplianceTableModel<TmpCorrespondencePicCompliance> getTablePicComplianceModel() {
		return tablePicComplianceModel;
	}

	public void setTablePicComplianceModel(
			TmpCorrespondencePicComplianceTableModel<TmpCorrespondencePicCompliance> tablePicComplianceModel) {
		this.tablePicComplianceModel = tablePicComplianceModel;
	}

	public TmpCorrespondenceSupportingUnitTableModel<TmpCorrespondenceSupportingUnit> getTableSupportingUnitModel() {
		return tableSupportingUnitModel;
	}

	public void setTableSupportingUnitModel(
			TmpCorrespondenceSupportingUnitTableModel<TmpCorrespondenceSupportingUnit> tableSupportingUnitModel) {
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

	@Override
	public void itemSelected(String clientId, String widgetVar, Object selectedItem) {
		/*if (StringUtils.equals("pic1Dialog", widgetVar)) {
			Object[] objects = (Object[]) selectedItem;
			User user1 = userService.findById(MathUtil.returnIdObjectToLong(objects[0]));
			if (user1 != null) {
				tmpCorrespondence.setUserId1(user1);
				tmpCorrespondence.setUserNameTemp1(user1.getNik() + "-" + user1.getName());

				User user2 = userService.getUserByNik(user1.getPukNik());
				if (user2 != null) {
					tmpCorrespondence.setUserId2(user2);
					tmpCorrespondence.setUserNameTemp2(user2.getNik() + "-" + user2.getName());

					User user3 = userService.getUserByNik(user2.getPukNik());
					if (user3 != null) {
						tmpCorrespondence.setUserId3(user3);
						tmpCorrespondence.setUserNameTemp3(user3.getNik() + "-" + user3.getName());
					}
				}
			}
		}

		if (StringUtils.equals("pic2Dialog", widgetVar)) {
			Object[] objects = (Object[]) selectedItem;

			User user2 = userService.findById(MathUtil.returnIdObjectToLong(objects[0]));
			if (user2 != null) {
				tmpCorrespondence.setUserId2(user2);
				tmpCorrespondence.setUserNameTemp2(user2.getNik() + "-" + user2.getName());

				User user3 = userService.getUserByNik(user2.getPukNik());
				if (user3 != null) {
					tmpCorrespondence.setUserId3(user3);
					tmpCorrespondence.setUserNameTemp3(user3.getNik() + "-" + user3.getName());
				}
			}
		} 
		
		if (StringUtils.equals("pic3Dialog", widgetVar)) {
			Object[] objects = (Object[]) selectedItem;

			// User user3 = userService.findById(((BigInteger) objects[0]).longValue());
			User user3 = userService.findById(MathUtil.returnIdObjectToLong(objects[0]));
			if (user3 != null) {
				tmpCorrespondence.setUserId3(user3);
				tmpCorrespondence.setUserNameTemp3(user3.getNik() + "-" + user3.getName());
			}
		}	*/

		if (StringUtils.equals("picComplianceDialog", widgetVar)) {
			Object[] objects = (Object[]) selectedItem;
			// User userPicCompliance = userService.findById(((BigInteger)
			// objects[0]).longValue());
			User userPicCompliance = userService.findById(MathUtil.returnIdObjectToLong(objects[0]));
			if (userPicCompliance != null) {
				tmpCorrespondence.getTmpCorrespondencePicCompliances().get(indexDtlPicCompliance)
						.setUser(userPicCompliance);
				tmpCorrespondence.getTmpCorrespondencePicCompliances().get(indexDtlPicCompliance)
						.setNikTemp(userPicCompliance.getNik());
				tmpCorrespondence.getTmpCorrespondencePicCompliances().get(indexDtlPicCompliance)
						.setNameTemp(userPicCompliance.getName());
				tmpCorrespondence.getTmpCorrespondencePicCompliances().get(indexDtlPicCompliance)
						.setEmailTemp(userPicCompliance.getEmail());

				tableSupportingUnitModel.setWrappedData(tmpCorrespondence.getTmpCorrespondenceSupportingUnits());
			}
		}

		if (StringUtils.equals("emailCc1Dialog", widgetVar)) {
			Object[] objects = (Object[]) selectedItem;
			// User user1 = userService.findById(((BigInteger) objects[0]).longValue());
			User user1 = userService.findById(MathUtil.returnIdObjectToLong(objects[0]));
			if (user1 != null) {
				tmpCorrespondence.getTmpCorrespondenceSupportingUnits().get(indexDtlCc).setEmailCc1(user1);
				tmpCorrespondence.getTmpCorrespondenceSupportingUnits().get(indexDtlCc)
						.setEmailCcTemp1(user1.getNik() + "-" + user1.getName());
				/*
				 * tmpCorrespondence.getTmpCorrespondenceSupportingUnits().get(indexDtlCc)
				 * .setDivisionId(user1.getDivisionId());
				 */

				User user2 = userService.getUserByNik(user1.getPukNik());
				if (user2 != null) {
					tmpCorrespondence.getTmpCorrespondenceSupportingUnits().get(indexDtlCc).setEmailCc2(user2);
					tmpCorrespondence.getTmpCorrespondenceSupportingUnits().get(indexDtlCc)
							.setEmailCcTemp2(user2.getNik() + "-" + user2.getName());

					User user3 = userService.getUserByNik(user2.getPukNik());
					if (user3 != null) {
						tmpCorrespondence.getTmpCorrespondenceSupportingUnits().get(indexDtlCc).setEmailCc3(user3);
						tmpCorrespondence.getTmpCorrespondenceSupportingUnits().get(indexDtlCc)
								.setEmailCcTemp3(user3.getNik() + "-" + user3.getName());
					}
				}

				tableSupportingUnitModel.setWrappedData(tmpCorrespondence.getTmpCorrespondenceSupportingUnits());
			}
		}

		if (StringUtils.equals("emailCc2Dialog", widgetVar)) {
			Object[] objects = (Object[]) selectedItem;
			// User user2 = userService.findById(((BigInteger) objects[0]).longValue());
			User user2 = userService.findById(MathUtil.returnIdObjectToLong(objects[0]));
			if (user2 != null) {
				tmpCorrespondence.getTmpCorrespondenceSupportingUnits().get(indexDtlCc).setEmailCc2(user2);
				tmpCorrespondence.getTmpCorrespondenceSupportingUnits().get(indexDtlCc)
						.setEmailCcTemp2(user2.getNik() + "-" + user2.getName());
				/*
				 * tmpCorrespondence.getTmpCorrespondenceSupportingUnits().get(indexDtlCc)
				 * .setDivisionId(user2.getDivisionId());
				 */

				User user3 = userService.getUserByNik(user2.getPukNik());
				if (user3 != null) {
					tmpCorrespondence.getTmpCorrespondenceSupportingUnits().get(indexDtlCc).setEmailCc3(user3);
					tmpCorrespondence.getTmpCorrespondenceSupportingUnits().get(indexDtlCc)
							.setEmailCcTemp3(user3.getNik() + "-" + user3.getName());
				}

				tableSupportingUnitModel.setWrappedData(tmpCorrespondence.getTmpCorrespondenceSupportingUnits());
			}
		}

		if (StringUtils.equals("emailCc3Dialog", widgetVar)) {
			Object[] objects = (Object[]) selectedItem;
			// User user3 = userService.findById(((BigInteger) objects[0]).longValue());
			User user3 = userService.findById(MathUtil.returnIdObjectToLong(objects[0]));
			if (user3 != null) {
				tmpCorrespondence.getTmpCorrespondenceSupportingUnits().get(indexDtlCc).setEmailCc3(user3);
				tmpCorrespondence.getTmpCorrespondenceSupportingUnits().get(indexDtlCc)
						.setEmailCcTemp3(user3.getNik() + "-" + user3.getName());
				/*
				 * tmpCorrespondence.getTmpCorrespondenceSupportingUnits().get(indexDtlCc)
				 * .setDivisionId(user3.getDivisionId());
				 */
				tableSupportingUnitModel.setWrappedData(tmpCorrespondence.getTmpCorrespondenceSupportingUnits());
			}
		}
		
		if (StringUtils.equals("referalLetterDialog", widgetVar)) {
			Object[] objects = (Object[]) selectedItem;

			TmpCorrespondence dataReffCorresponse = new TmpCorrespondence();
			Long correspindenIdReff = MathUtil.returnIdObjectToLong(objects[0]);			
			dataReffCorresponse = tmpCorrespondenceService.findById(correspindenIdReff);
			tmpCorrespondence.getTmpCrpdcReffLetters().get(indexDtlRefLetter).setReffLetterCorrespondence(dataReffCorresponse);
			tmpCorrespondence.getTmpCrpdcReffLetters().get(indexDtlRefLetter).setPerihal((String)objects[1]);
			tmpCorrespondence.getTmpCrpdcReffLetters().get(indexDtlRefLetter).setLetterNo((String)objects[2]);
			
			if(dataReffCorresponse !=null && dataReffCorresponse.getTmpCorrespondenceDocuments() !=null) {
			   List<TmpReffDocument> dataList = new ArrayList<TmpReffDocument>();
			   for(TmpCorrespondenceDocument tmpRefDocument : dataReffCorresponse.getTmpCorrespondenceDocuments()) {
				  TmpReffDocument refDoc = new TmpReffDocument();
				  refDoc.setAttachmentFile(tmpRefDocument.getAttachmentFile());
				  refDoc.setCorrespondenceAttachmentId(tmpRefDocument.getCorrespondenceAttachmentId());
				  refDoc.setFileId(tmpRefDocument.getFileId());
				  dataList.add(refDoc);
			   }
				
			   tmpCorrespondence.getTmpCrpdcReffLetters().get(indexDtlRefLetter).setReffDocumentList(dataList);		
			}
				
		}
		
		if (StringUtils.equals("picConfirmPic1Dialog", widgetVar)) {
			Object[] objects = (Object[]) selectedItem;
			
			User user1 = userService.findById(MathUtil.returnIdObjectToLong(objects[0]));
			if (user1 != null) {
				tmpCorrespondence.getTmpCrpdcPicConfirms().get(indexDtlPicConfirm).setUser1(user1);
				tmpCorrespondence.getTmpCrpdcPicConfirms().get(indexDtlPicConfirm).setUserName1(user1.getName());

				User user2 = userService.getUserByNik(user1.getPukNik());
				if (user2 != null) {
					tmpCorrespondence.getTmpCrpdcPicConfirms().get(indexDtlPicConfirm).setUser2(user2);
					tmpCorrespondence.getTmpCrpdcPicConfirms().get(indexDtlPicConfirm).setUserName2(user2.getName());


					User user3 = userService.getUserByNik(user2.getPukNik());
					if (user3 != null) {
						tmpCorrespondence.getTmpCrpdcPicConfirms().get(indexDtlPicConfirm).setUser3(user3);
						tmpCorrespondence.getTmpCrpdcPicConfirms().get(indexDtlPicConfirm).setUserName3(user3.getName());
					}
				}

				tableSupportingUnitModel.setWrappedData(tmpCorrespondence.getTmpCrpdcPicConfirms());
			}
		}
		
		if (StringUtils.equals("picConfirmPic2Dialog", widgetVar)) {
			Object[] objects = (Object[]) selectedItem;
			
			User user2 = userService.findById(MathUtil.returnIdObjectToLong(objects[0]));
			if (user2 != null) {
				tmpCorrespondence.getTmpCrpdcPicConfirms().get(indexDtlPicConfirm).setUser2(user2);
				tmpCorrespondence.getTmpCrpdcPicConfirms().get(indexDtlPicConfirm).setUserName2(user2.getName());

				User user3 = userService.getUserByNik(user2.getPukNik());
				if (user3 != null) {
					tmpCorrespondence.getTmpCrpdcPicConfirms().get(indexDtlPicConfirm).setUser3(user3);
					tmpCorrespondence.getTmpCrpdcPicConfirms().get(indexDtlPicConfirm).setUserName3(user3.getName());
				}
				
				tableSupportingUnitModel.setWrappedData(tmpCorrespondence.getTmpCrpdcPicConfirms());
			}
		}
		
		if (StringUtils.equals("picConfirmPic3Dialog", widgetVar)) {
			Object[] objects = (Object[]) selectedItem;
			
			User user3 = userService.findById(MathUtil.returnIdObjectToLong(objects[0]));
			if (user3 != null) {
				tmpCorrespondence.getTmpCrpdcPicConfirms().get(indexDtlPicConfirm).setUser3(user3);
				tmpCorrespondence.getTmpCrpdcPicConfirms().get(indexDtlPicConfirm).setUserName3(user3.getName());
			
				tableSupportingUnitModel.setWrappedData(tmpCorrespondence.getTmpCrpdcPicConfirms());
			}
		}

		PrimeFaces.current().executeScript("reInitSelect2();");
//		RequestContext.getCurrentInstance().execute("reInitSelect2();");

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

	public TmpCorrespondencePicCompliance[] getSelectedPicComplianceData() {
		return selectedPicComplianceData;
	}

	public void setSelectedPicComplianceData(TmpCorrespondencePicCompliance[] selectedPicComplianceData) {
		this.selectedPicComplianceData = selectedPicComplianceData;
	}

	public TmpCorrespondenceSupportingUnit[] getSelectedSupportingUnitData() {
		return selectedSupportingUnitData;
	}

	public void setSelectedSupportingUnitData(TmpCorrespondenceSupportingUnit[] selectedSupportingUnitData) {
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

	public List<UploadedFileWO> getDeletedFiles() {
		return deletedFiles;
	}

	public void setDeletedFiles(List<UploadedFileWO> deletedFiles) {
		this.deletedFiles = deletedFiles;
	}

	public Integer getLastSequenceOfPicCompliance() {
		return lastSequenceOfPicCompliance;
	}

	public void setLastSequenceOfPicCompliance(Integer lastSequenceOfPicCompliance) {
		this.lastSequenceOfPicCompliance = lastSequenceOfPicCompliance;
	}

	public Integer getLastSequenceOfSupportingUnitModel() {
		return lastSequenceOfSupportingUnitModel;
	}

	public void setLastSequenceOfSupportingUnitModel(Integer lastSequenceOfSupportingUnitModel) {
		this.lastSequenceOfSupportingUnitModel = lastSequenceOfSupportingUnitModel;
	}

	public Boolean getDisabledFollowUpStatus() {
		return disabledFollowUpStatus;
	}

	public void setDisabledFollowUpStatus(Boolean disabledFollowUpStatus) {
		this.disabledFollowUpStatus = disabledFollowUpStatus;
	}

	public List<SelectItem> getCorrespondenceTypeCodeList() {
		return correspondenceTypeCodeList;
	}

	public void setCorrespondenceTypeCodeList(List<SelectItem> correspondenceTypeCodeList) {
		this.correspondenceTypeCodeList = correspondenceTypeCodeList;
	}

	public EmailTemplateService getEmailTemplateService() {
		return emailTemplateService;
	}

	public void setEmailTemplateService(EmailTemplateService emailTemplateService) {
		this.emailTemplateService = emailTemplateService;
	}

	public SimpleDateFormat getSdf() {
		return sdf;
	}

	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
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

	public RCService getRcService() {
		return rcService;
	}

	public void setRcService(RCService rcService) {
		this.rcService = rcService;
	}

	public String getDivNameLogin() {
		return divNameLogin;
	}

	public void setDivNameLogin(String divNameLogin) {
		this.divNameLogin = divNameLogin;
	}

	public String getTextWarningUpload() {
		return textWarningUpload;
	}

	public void setTextWarningUpload(String textWarningUpload) {
		this.textWarningUpload = textWarningUpload;
	}

	public TmpCrpdcReffLetter[] getSelectedReferalLetterData() {
		return selectedReferalLetterData;
	}

	public void setSelectedReferalLetterData(TmpCrpdcReffLetter[] selectedReferalLetterData) {
		this.selectedReferalLetterData = selectedReferalLetterData;
	}

	public SelectorInfo getSelectedReferalLetter() {
		return selectedReferalLetter;
	}

	public void setSelectedReferalLetter(SelectorInfo selectedReferalLetter) {
		this.selectedReferalLetter = selectedReferalLetter;
	}

	public TmpCrpdcReffLetterTableModel<TmpCrpdcReffLetter> getTableReferalLetterModel() {
		return tableReferalLetterModel;
	}

	public void setTableReferalLetterModel(TmpCrpdcReffLetterTableModel<TmpCrpdcReffLetter> tableReferalLetterModel) {
		this.tableReferalLetterModel = tableReferalLetterModel;
	}

	public Integer getIndexDtlRefLetter() {
		return indexDtlRefLetter;
	}

	public void setIndexDtlRefLetter(Integer indexDtlRefLetter) {
		this.indexDtlRefLetter = indexDtlRefLetter;
	}

	public Integer getLastSequenceOfReffLetter() {
		return lastSequenceOfReffLetter;
	}

	public void setLastSequenceOfReffLetter(Integer lastSequenceOfReffLetter) {
		this.lastSequenceOfReffLetter = lastSequenceOfReffLetter;
	}

	public TmpCrpdcPicConfirm[] getSelectedSubPicConfirmData() {
		return selectedSubPicConfirmData;
	}

	public void setSelectedSubPicConfirmData(TmpCrpdcPicConfirm[] selectedSubPicConfirmData) {
		this.selectedSubPicConfirmData = selectedSubPicConfirmData;
	}

	public SelectorInfo getSelectedSubPicConfirm() {
		return selectedSubPicConfirm;
	}

	public void setSelectedSubPicConfirm(SelectorInfo selectedSubPicConfirm) {
		this.selectedSubPicConfirm = selectedSubPicConfirm;
	}

	public TmpCrpdcPicConfirmTableModel<TmpCrpdcPicConfirm> getTableSubPicConfirmModel() {
		return tableSubPicConfirmModel;
	}

	public void setTableSubPicConfirmModel(TmpCrpdcPicConfirmTableModel<TmpCrpdcPicConfirm> tableSubPicConfirmModel) {
		this.tableSubPicConfirmModel = tableSubPicConfirmModel;
	}

	public Integer getLastSequenceOfPicConfirm() {
		return lastSequenceOfPicConfirm;
	}

	public void setLastSequenceOfPicConfirm(Integer lastSequenceOfPicConfirm) {
		this.lastSequenceOfPicConfirm = lastSequenceOfPicConfirm;
	}

	public Integer getIndexDtlPicConfirm() {
		return indexDtlPicConfirm;
	}

	public void setIndexDtlPicConfirm(Integer indexDtlPicConfirm) {
		this.indexDtlPicConfirm = indexDtlPicConfirm;
	}

}