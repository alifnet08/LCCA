package com.wo.module.tmpCorrespondenceAml.bean;

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
import com.wo.module.common.util.FileUtil;
import com.wo.module.common.util.MathUtil;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.counterType.model.CounterType;
import com.wo.module.counterType.service.CounterTypeService;
import com.wo.module.emailTemplate.model.EmailTemplate;
import com.wo.module.emailTemplate.service.EmailTemplateService;
import com.wo.module.externalRegulation.service.RegulationMstService;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.lov.bean.SelectorListener;
import com.wo.module.lov.bean.SelectorModel.SelectorInfo;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;
import com.wo.module.reportType.service.ReportTypeService;
import com.wo.module.tmpCorrespondence.model.TmpCorrespondence;
import com.wo.module.tmpCorrespondence.model.TmpCorrespondenceDocument;
import com.wo.module.tmpCorrespondence.model.TmpCorrespondencePicCompliance;
import com.wo.module.tmpCorrespondence.model.TmpCorrespondencePicComplianceTableModel;
import com.wo.module.tmpCorrespondence.model.TmpCorrespondenceSupportingUnit;
import com.wo.module.tmpCorrespondence.model.TmpCorrespondenceSupportingUnitTableModel;
import com.wo.module.tmpCorrespondenceAml.constant.TmpCorrespondenceAmlConstants;
import com.wo.module.tmpCorrespondenceAml.service.TmpCorrespondenceAmlService;
import com.wo.module.trcCorrespondence.model.TrcCorrespondence;
import com.wo.module.trcCorrespondenceAml.service.TrcCorrespondenceAmlService;
import com.wo.module.user.model.Division;
import com.wo.module.user.model.User;
import com.wo.module.user.service.UserService;

public class TmpCorrespondenceAmlEditBean extends CommonBean implements SelectorListener<Object>, Serializable {

	private static final long serialVersionUID = -8720064230782928252L;

	static Logger logger = Logger.getLogger(TmpCorrespondenceAmlEditBean.class);

	private TmpCorrespondence tmpCorrespondence;

	private Boolean isViewOnly;

	private String actionMode;

	private String editedId;

	private String dueDateType;
	
	private Boolean disabledFollowUpStatus;

	private TmpCorrespondencePicCompliance[] selectedPicComplianceData;
	private TmpCorrespondenceSupportingUnit[] selectedSupportingUnitData;

	private SelectorInfo selectorUser1;
	private SelectorInfo selectorUser2;
	private SelectorInfo selectorUser3;
	private SelectorInfo selectorPicCompliance;

	private SelectorInfo selectorUserCc1;
	private SelectorInfo selectorUserCc2;
	private SelectorInfo selectorUserCc3;

	private List<UploadedFileWO> deletedFiles;
	private List<UploadedFileWO> uploadedFilesDocument;

	private TmpCorrespondencePicComplianceTableModel<TmpCorrespondencePicCompliance> tablePicComplianceModel;
	private TmpCorrespondenceSupportingUnitTableModel<TmpCorrespondenceSupportingUnit> tableSupportingUnitModel;
	
	private Integer lastSequenceOfPicCompliance;
	private Integer lastSequenceOfSupportingUnitModel;
	
	private Integer indexDtlPicCompliance;
	private Integer indexDtlCc;

	// private List<TrcRmd> trcRmdList;
	private List<TrcCorrespondence> trcCorrespondenceList;

	SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");

	private TmpCorrespondenceAmlService tmpCorrespondenceAmlService;
	private ReportTypeService reportTypeService;
	private CounterTypeService counterTypeService;
	//private ParameterDetailService parameterDetailService;
	private UserService userService;
	private TrcCorrespondenceAmlService trcCorrespondenceAmlService;
	private RegulationMstService regulationMstService;
	private EmailTemplateService emailTemplateService;
	
	public FacesUtil facesUtil;
	
	private FileUtil fileUtil;

	private List<SelectItem> senderCodeList;
	private List<SelectItem> yesNoList;
	private List<SelectItem> counterTypeList;
	private List<SelectItem> complianceStatusList;
	private List<SelectItem> divisionList;

	private List<SelectItem> reminderStatusList;
	private List<SelectItem> correspondenceTypeCodeList;

	private String REMINDER_ACTIVE = ParameterDetail.PARAM_DET_CODE_REMINDER_ACTIVE;
	private String REMINDER_INACTIVE = ParameterDetail.PARAM_DET_CODE_REMINDER_INACTIVE;

	private String textWarningUpload;
	
	@PostConstruct
	public void init() {
		super.init();
		initList();
		selectorUser1 = TmpCorrespondenceAmlConstants.buildSelectorUser();
		selectorUser2 = TmpCorrespondenceAmlConstants.buildSelectorUser();
		selectorUser3 = TmpCorrespondenceAmlConstants.buildSelectorUser();
		selectorPicCompliance = TmpCorrespondenceAmlConstants.buildSelectorUserCompliance();
		selectorUserCc1 = TmpCorrespondenceAmlConstants.buildSelectorUser();
		selectorUserCc2 = TmpCorrespondenceAmlConstants.buildSelectorUser();
		selectorUserCc3 = TmpCorrespondenceAmlConstants.buildSelectorUser();

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
			senderCodeList = new ArrayList<SelectItem>();
			List<ParameterDetail> listParamDtl = parameterDetailService
					.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_CODE_SENDER_AML);

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
		
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	public void clearPic2() {	
		tmpCorrespondence.setUserId2(null);
		tmpCorrespondence.setUserNameTemp2(null);
	}
	
	public void clearPic3() {
		tmpCorrespondence.setUserId3(null);
		tmpCorrespondence.setUserNameTemp3(null);
	}
	
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
	
	public void onChangeDivisionSingle() {
		tmpCorrespondence.setUserId1(null);
		tmpCorrespondence.setUserNameTemp1(null);
		tmpCorrespondence.setUserId2(null);
		tmpCorrespondence.setUserNameTemp2(null);
		tmpCorrespondence.setUserId3(null);
		tmpCorrespondence.setUserNameTemp3(null);
	}
	
	public void onChangeFollowUpNo() {
		if(tmpCorrespondence.getSenderCode().getParameterDtlCode().equals("SENDER_KPK") || tmpCorrespondence.getSenderCode().getParameterDtlCode().equals("SENDER_PPATK")) {
			tmpCorrespondence.setFollowUp("N");
			tmpCorrespondence.setNotes("-");
			
		}else {
			tmpCorrespondence.setFollowUp(null);
			tmpCorrespondence.setNotes(null);
		}
		onChangeFollowupStatus();
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
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
	
	public void onChangeFollowupStatus() {
		if(tmpCorrespondence.getFollowUp() != null) {
			if("Y".equals(tmpCorrespondence.getFollowUp())) {				
				tmpCorrespondence.setUserId1(null);
				tmpCorrespondence.setUserNameTemp1(null);
				tmpCorrespondence.setUserId2(null);
				tmpCorrespondence.setUserNameTemp2(null);
				tmpCorrespondence.setUserId3(null);
				tmpCorrespondence.setUserNameTemp3(null);
				
				if(tmpCorrespondence.getCounterType() == null) {
					tmpCorrespondence.setCounterType(new CounterType());
				}
				
				
			} else if("N".equals(tmpCorrespondence.getFollowUp())) {
				tmpCorrespondence.setDivisionId(null);
				tmpCorrespondence.setTargetDate(null);
				tmpCorrespondence.setUserId1(null);
				tmpCorrespondence.setUserNameTemp1(null);
				tmpCorrespondence.setUserId2(null);
				tmpCorrespondence.setUserNameTemp2(null);
				tmpCorrespondence.setUserId3(null);
				tmpCorrespondence.setUserNameTemp3(null);
				
				if(tmpCorrespondence.getTmpCorrespondenceSupportingUnits() != null) {
					tmpCorrespondence.getTmpCorrespondenceSupportingUnits().clear();
				}
				
				tmpCorrespondence.setCounterType(null);
			}
			
		}
		
		PrimeFaces.current().executeScript("reInitSelect2();");
		//RequestContext.getCurrentInstance().execute("reInitSelect2();");
	}

	public void onAddNewPicCompliance() {
		if (tmpCorrespondence.getTmpCorrespondencePicCompliances() == null) {
			tmpCorrespondence.setTmpCorrespondencePicCompliances(new ArrayList<TmpCorrespondencePicCompliance>());
			lastSequenceOfPicCompliance = 0;
		} else {
			if(tmpCorrespondence.getTmpCorrespondencePicCompliances().size() == 0) {
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
			if(tmpCorrespondence.getTmpCorrespondenceSupportingUnits().size() == 0) {
				lastSequenceOfSupportingUnitModel = 0;
			}			
		} 

		TmpCorrespondenceSupportingUnit d = new TmpCorrespondenceSupportingUnit();
		lastSequenceOfSupportingUnitModel = lastSequenceOfSupportingUnitModel + 1;
		d.setSequence(lastSequenceOfSupportingUnitModel);

		tmpCorrespondence.getTmpCorrespondenceSupportingUnits().add(d);

		tableSupportingUnitModel.setWrappedData(tmpCorrespondence.getTmpCorrespondenceSupportingUnits());
		
		PrimeFaces.current().executeScript("reInitSelect2();");
		//RequestContext.getCurrentInstance().execute("reInitSelect2();");

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

	public void handleFileUploadDocument(FileUploadEvent event) throws Exception {
		try {
		uploadedFilesDocument = uploadedFilesDocument == null ? new ArrayList<UploadedFileWO>() : uploadedFilesDocument;
		uploadedFilesDocument.add(new UploadedFileWO(
				CallApiManager.callUploadAPI(event.getFile(), Constants.COMPLIANCE_DOC_TYPE_KORESPONDENSI_SURAT_MASUK,
						parameterDetailService, false, fileUtil),
				event.getFile().getFileName(), event.getFile().getContentType(), event.getFile().getSize()));
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
		tmpCorrespondence.setCorrespondenceCode(new ParameterDetail());

		lastSequenceOfPicCompliance = 0;
		lastSequenceOfSupportingUnitModel = 0;
		
		User user1 = userService.getUserByNik(facesUtil.retrieveUserLogin());
		if (user1 != null) {
		/*	tmpCorrespondence.setUserId1(user1);
			tmpCorrespondence.setUserNameTemp1(user1.getNik() + "-" + user1.getName());
			tmpCorrespondence.setDivisionId(user1.getDivisionId());*/
			
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
				/*tmpCorrespondence.setUserId2(user2);
				tmpCorrespondence.setUserNameTemp2(user2.getNik() + "-" + user2.getName());*/

				User user3 = userService.getUserByNik(user2.getPukNik());
				if (user3 != null) {
					/*tmpCorrespondence.setUserId3(user3);
					tmpCorrespondence.setUserNameTemp3(user3.getNik() + "-" + user3.getName());*/
				}
			}
		}

		uploadedFilesDocument = new ArrayList<UploadedFileWO>();

		tableSupportingUnitModel = new TmpCorrespondenceSupportingUnitTableModel<TmpCorrespondenceSupportingUnit>(
				tmpCorrespondence.getTmpCorrespondenceSupportingUnits());
		tablePicComplianceModel = new TmpCorrespondencePicComplianceTableModel<TmpCorrespondencePicCompliance>(
				tmpCorrespondence.getTmpCorrespondencePicCompliances());

		actionMode = Constants.ACTION_ADD;
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
		tmpCorrespondence = getTmpCorrespondenceAmlService().findById(idLong);
		tmpCorrespondence.setOldTargetDate(tmpCorrespondence.getTargetDate());
		lastSequenceOfPicCompliance = 0;	
		lastSequenceOfSupportingUnitModel = 0;
		
		try {
			TrcCorrespondence trcCorrespondence = trcCorrespondenceAmlService.findById(idLong);
			if (trcCorrespondence != null) {
				
				if(trcCorrespondence.getFollowupStatus() != null) {
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
		} catch (Exception e) {
			logger.error(e.getMessage());
//			e.printStackTrace();
		}

		if (tmpCorrespondence.getUserId1() != null) {
			tmpCorrespondence.setUserNameTemp1(tmpCorrespondence.getUserId1().getName());
		}

		if (tmpCorrespondence.getUserId2() != null) {
			tmpCorrespondence.setUserNameTemp2(tmpCorrespondence.getUserId2().getName());
		}

		if (tmpCorrespondence.getUserId3() != null) {
			tmpCorrespondence.setUserNameTemp3(tmpCorrespondence.getUserId3().getName());
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
		
		if(tmpCorrespondence.getSenderCode().getParameterDtlCode().equals("SENDER_KPK") || tmpCorrespondence.getSenderCode().getParameterDtlCode().equals("SENDER_PPATK")) {
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
		}else {
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
			
			if(StringUtils.isEmpty(tmpCorrespondence.getPerihalIn())) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpCorrespondencePerihal") + " "
//						+ facesUtil.retrieveMessage("indonesia") + " "
						+ facesUtil.retrieveMessage("validateRequired"));
				flag = true;
			}
			
			if(StringUtils.isEmpty(tmpCorrespondence.getLetterSummary())) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpCorrespondenceLetterSummary") + " "
						+ facesUtil.retrieveMessage("validateRequired"));
				flag = true;
			}
			
			if(tmpCorrespondence.getLetterDate() == null) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpCorrespondenceLetterDate") + " "
						+ facesUtil.retrieveMessage("validateRequired"));
				flag = true;
			}
			
			if (tmpCorrespondence.getCorrespondenceCode().getParameterDtlCode().equals(TmpCorrespondenceAmlConstants.PARAM_DTL_CODE_NONINVITATION)) {
				if(uploadedFilesDocument == null  || uploadedFilesDocument.size() <= 0) {
					facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpCorrespondenceDocumentLetterSubTitle") + " File "
							+ facesUtil.retrieveMessage("validateRequired"));
					flag = true;
				}
			}			
		}
		
		Set<Long> userComplianceTemp = new HashSet<Long>();
		for (int i = 0; i < tmpCorrespondence.getTmpCorrespondencePicCompliances().size(); i++) {
			TmpCorrespondencePicCompliance dtl = (TmpCorrespondencePicCompliance) tmpCorrespondence.getTmpCorrespondencePicCompliances().get(i);
			if(dtl.getUser().getUserId() != null) {
				if(!userComplianceTemp.add(dtl.getUser().getUserId())) {
					facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpCorrespondenceNIK") + " "
							+ facesUtil.retrieveMessage("errorDuplicate"));
					flag = true;
					break;
				}
			}
		}
		
		if (StringUtils.isEmpty(tmpCorrespondence.getFollowUp())) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpCorrespondenceFollowup") + " "
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}else {
			if(tmpCorrespondence.getFollowUp().equals(Constants.CONSTANT_YES)) {
				if (tmpCorrespondence.getUserId1() == null) {
					facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpCorrespondencePic1") + " "
							+ facesUtil.retrieveMessage("validateRequired"));
					flag = true;
				}
				
				if (tmpCorrespondence.getTmpCorrespondenceSupportingUnits() == null ||
			    		tmpCorrespondence.getTmpCorrespondenceSupportingUnits().size() == 0) {
			    	
				}  else {
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

				if (tmpCorrespondence.getTargetDate() == null) {
					facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpCorrespondenceTargetDate") + " "
							+ facesUtil.retrieveMessage("validateRequired"));
					flag = true;
					
				}	else {
					if (getTmpCorrespondenceAmlService()
							.hasReachedMaximumReschedule(tmpCorrespondence.getCorrespondenceId())) {
						if(!tmpCorrespondence.getTargetDate().equals(tmpCorrespondence.getOldTargetDate())) {
							facesUtil.addErrMessage(facesUtil.
									retrieveMessage("formTmpCorrespondenceTargetDateErrorReachMaximum"));
							flag = true;
						}
					}
					
					
				}
				
			} else if(tmpCorrespondence.getFollowUp().equals(Constants.CONSTANT_NO) && StringUtils.isEmpty(tmpCorrespondence.getNotes())) {
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

				if (tmpCorrespondence.getTmpCorrespondenceDocuments() == null
						|| tmpCorrespondence.getTmpCorrespondenceDocuments().size() == 0) {
					tmpCorrespondence.setTmpCorrespondenceDocuments(new ArrayList<TmpCorrespondenceDocument>());
				}
				tmpCorrespondence.getTmpCorrespondenceDocuments().clear();

				if(uploadedFilesDocument != null) {
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

				ParameterDetail pdReminderStatus = parameterDetailService
						.getParameterDetailByParamDtlCode(tmpCorrespondence.getReminderStatus().getParameterDtlCode());
				tmpCorrespondence.setReminderStatus(pdReminderStatus);

				if (tmpCorrespondence.getCorrespondenceId() != null) {
					tmpCorrespondence.setStatus(parameterDetailService
							.getParameterDetailByParamDtlCode(ParameterDetail.PARAM_DET_CODE_DATA_NEW));
					tmpCorrespondence.setLastUpdateBy(facesUtil.retrieveUserLogin());
					tmpCorrespondence.setLastUpdateDate(new Timestamp(new Date().getTime()));
					tmpCorrespondence.setDelId(new Long(0));
					tmpCorrespondence.setEnabledFlag(Constants.CONSTANT_YES);
					getTmpCorrespondenceAmlService().update(tmpCorrespondence);
				} else {
					tmpCorrespondence.setStatus(parameterDetailService
							.getParameterDetailByParamDtlCode(ParameterDetail.PARAM_DET_CODE_DATA_NEW));
					tmpCorrespondence.setCreatedBy(facesUtil.retrieveUserLogin());
					tmpCorrespondence.setCreationDate(new Timestamp(new Date().getTime()));
					tmpCorrespondence.setDelId(new Long(0));
					tmpCorrespondence.setEnabledFlag(Constants.CONSTANT_YES);
					getTmpCorrespondenceAmlService().save(tmpCorrespondence);
				}
				
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
				
				if(deletedFiles!=null) {
					for(int i=0;i<deletedFiles.size();i++) {
						UploadedFileWO uf = (UploadedFileWO) deletedFiles.get(i);
						CallApiManager.deleteFile(uf.getFileId(), parameterDetailService, fileUtil);
					}
				}

				facesUtil.redirect("/pages/tmpCorrespondenceAml/tmpCorrespondenceAml.faces");
			}

		} catch (Exception ex) {
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + ex.getMessage(), "");
		}

	}

	public void cancel() {
		try {
			for (int i = 0; i < uploadedFilesDocument.size(); i++) {
				UploadedFileWO uf = (UploadedFileWO) uploadedFilesDocument.get(i);
				if(uf.getIsNew() == null) {
					CallApiManager.deleteFile(uf.getFileId(), parameterDetailService, fileUtil);
				}
			}
				
			facesUtil.redirect("/pages/tmpCorrespondenceAml/tmpCorrespondenceAml.faces");
		} catch (Exception ex) {
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + ex.getMessage(), "");
		}
	}
	
	public void deleteAttachment(String fileId,int index) throws Exception {
		deletedFiles = deletedFiles!=null?deletedFiles: new ArrayList<UploadedFileWO>();
		deletedFiles.add(new UploadedFileWO(fileId,null,null, null));
		uploadedFilesDocument.remove(uploadedFilesDocument.get(index));
	}
	
	public void downloadFile(String fileId, String fileName, byte[] content) throws Exception {
		CallApiManager.downloadFile(fileId, fileName, content, parameterDetailService);
	}

	public void sendEmail() {
		try {
			
			EmailTemplate emailTemplate = emailTemplateService.getEmailTemplateByEmailTemplateCode(Constants.EMAIL_APPROVAL);
			String emailSubject = emailTemplate.getEmailSubject();
	
			String emailContent = ""; 
			String emailTo  = "";
			String emailCc = "";
		   
			String letterNoTemp = tmpCorrespondence.getLetterNo() != null ? " - " + tmpCorrespondence.getLetterNo() : "";
			
			
			emailContent = emailTemplate.getEmailContent().replace(Constants.NOTIFICATION_TYPE_AND_DOC_NUM, "Correspondence" 
					+ letterNoTemp);
						
			ParameterDetail paramEmail = parameterDetailService.getParameterDetailByParamDtlCode(Constants.OSCAR_CHECKER);
			emailTo = paramEmail.getNameIn();
					
			final String subject = emailSubject;
			final String content = emailContent;
			final String to = emailTo;
			//final String to = "h3ndr407@gmail.com";
			final String cc = emailCc;
						
			CallApiManager.sendEmailAPI(to, cc, subject, content, "EMAIL_CORRESPONDENCE", "true", parameterDetailService);
		    
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
		if (StringUtils.equals("pic1Dialog", widgetVar)) {
			Object[] objects = (Object[]) selectedItem;
			//User user1 = userService.findById(((BigInteger) objects[0]).longValue());
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

			//User user2 = userService.findById(((BigInteger) objects[0]).longValue());
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

		if (StringUtils.equals("picComplianceDialog", widgetVar)) {
			Object[] objects = (Object[]) selectedItem;
			//User userPicCompliance = userService.findById(((BigInteger) objects[0]).longValue());
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

		if (StringUtils.equals("pic3Dialog", widgetVar)) {
			Object[] objects = (Object[]) selectedItem;

			//User user3 = userService.findById(((BigInteger) objects[0]).longValue());
			User user3 = userService.findById(MathUtil.returnIdObjectToLong(objects[0]));
			if (user3 != null) {
				tmpCorrespondence.setUserId3(user3);
				tmpCorrespondence.setUserNameTemp3(user3.getNik() + "-" + user3.getName());
			}
		}

		if (StringUtils.equals("emailCc1Dialog", widgetVar)) {
			Object[] objects = (Object[]) selectedItem;
			//User user1 = userService.findById(((BigInteger) objects[0]).longValue());
			User user1 = userService.findById(MathUtil.returnIdObjectToLong(objects[0]));
			if (user1 != null) {
				tmpCorrespondence.getTmpCorrespondenceSupportingUnits().get(indexDtlCc).setEmailCc1(user1);
				tmpCorrespondence.getTmpCorrespondenceSupportingUnits().get(indexDtlCc)
						.setEmailCcTemp1(user1.getNik() + "-" + user1.getName());
				/*tmpCorrespondence.getTmpCorrespondenceSupportingUnits().get(indexDtlCc)
						.setDivisionId(user1.getDivisionId());*/

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
			//User user2 = userService.findById(((BigInteger) objects[0]).longValue());
			User user2 = userService.findById(MathUtil.returnIdObjectToLong(objects[0]));
			if (user2 != null) {
				tmpCorrespondence.getTmpCorrespondenceSupportingUnits().get(indexDtlCc).setEmailCc2(user2);
				tmpCorrespondence.getTmpCorrespondenceSupportingUnits().get(indexDtlCc)
						.setEmailCcTemp2(user2.getNik() + "-" + user2.getName());
			/*	tmpCorrespondence.getTmpCorrespondenceSupportingUnits().get(indexDtlCc)
						.setDivisionId(user2.getDivisionId());*/

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
			//User user3 = userService.findById(((BigInteger) objects[0]).longValue());
			User user3 = userService.findById(MathUtil.returnIdObjectToLong(objects[0]));
			if (user3 != null) {
				tmpCorrespondence.getTmpCorrespondenceSupportingUnits().get(indexDtlCc).setEmailCc3(user3);
				tmpCorrespondence.getTmpCorrespondenceSupportingUnits().get(indexDtlCc)
						.setEmailCcTemp3(user3.getNik() + "-" + user3.getName());
				/*tmpCorrespondence.getTmpCorrespondenceSupportingUnits().get(indexDtlCc)
						.setDivisionId(user3.getDivisionId());*/
				tableSupportingUnitModel.setWrappedData(tmpCorrespondence.getTmpCorrespondenceSupportingUnits());
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

	public TmpCorrespondenceAmlService getTmpCorrespondenceAmlService() {
		return tmpCorrespondenceAmlService;
	}

	public void setTmpCorrespondenceAmlService(TmpCorrespondenceAmlService tmpCorrespondenceAmlService) {
		this.tmpCorrespondenceAmlService = tmpCorrespondenceAmlService;
	}

	public TrcCorrespondenceAmlService getTrcCorrespondenceAmlService() {
		return trcCorrespondenceAmlService;
	}

	public void setTrcCorrespondenceAmlService(TrcCorrespondenceAmlService trcCorrespondenceAmlService) {
		this.trcCorrespondenceAmlService = trcCorrespondenceAmlService;
	}

	public String getTextWarningUpload() {
		return textWarningUpload;
	}

	public void setTextWarningUpload(String textWarningUpload) {
		this.textWarningUpload = textWarningUpload;
	}
	
}