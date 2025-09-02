package com.wo.module.tmpAudit.bean;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

import com.wo.module.auditMockup.model.Audit;
import com.wo.module.auditMockup.model.AuditFindings;
import com.wo.module.auditMockup.model.BankCommitment;
import com.wo.module.auditMockup.model.BankResponse;
import com.wo.module.auditMockup.model.PicFollowup;
import com.wo.module.auditMockup.model.PoinPemeriksaan;
import com.wo.module.auditMockup.model.PoinPemeriksaanTableModel;
import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.exception.CustomAPIException;
import com.wo.module.common.js.JsUtil;
import com.wo.module.common.model.ColumnModel;
import com.wo.module.common.model.UploadedFileWO;
import com.wo.module.common.util.DateUtil;
import com.wo.module.common.util.EntityUtil;
import com.wo.module.common.util.FileUtil;
import com.wo.module.common.util.MathUtil;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.complianceTestingMockup.vo.AttachmentVO;
import com.wo.module.complianceTestingMockup.vo.StatusKonfirmasiVO;
import com.wo.module.counterType.model.CounterType;
import com.wo.module.counterType.model.CounterTypeDtl;
import com.wo.module.counterType.service.CounterTypeService;
import com.wo.module.emailTemplate.model.EmailTemplate;
import com.wo.module.emailTemplate.service.EmailTemplateService;
import com.wo.module.externalRegulation.model.RegulationTrackRecord;
import com.wo.module.holiday.service.HolidayService;
import com.wo.module.internalRegulationPenerbitan.model.InternalRegulationPenerbitanPicTpkEmail;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.lov.bean.SelectorListener;
import com.wo.module.lov.bean.SelectorModel.SelectorInfo;
import com.wo.module.mstAudit.model.MstAudit;
import com.wo.module.mstAudit.service.MstAuditService;
import com.wo.module.mstAudit.vo.MstAuditVO;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;
import com.wo.module.parameter.service.ParameterDetailService;
import com.wo.module.regulationSocialization.constant.RegulationSocializationConstants;
import com.wo.module.tmpAudit.constant.AuditConstant;
import com.wo.module.tmpAudit.model.TmpAudit;
import com.wo.module.tmpAudit.model.TmpAuditCheckPoint;
import com.wo.module.tmpAudit.model.TmpAuditCheckPointTableModel;
import com.wo.module.tmpAudit.model.TmpAuditDocument;
import com.wo.module.tmpAudit.model.TmpAuditPICComplianceTableModel;
import com.wo.module.tmpAudit.model.TmpAuditPicCompliance;
import com.wo.module.tmpAudit.model.TmpAuditPicFollowup;
import com.wo.module.tmpAudit.model.TmpAuditPicFollowupAuditFindingsTable;
import com.wo.module.tmpAudit.model.TmpAuditPicFollowupBankCommitment;
import com.wo.module.tmpAudit.model.TmpAuditPicFollowupEmail;
import com.wo.module.tmpAudit.model.TmpAuditPicFollowupRec;
import com.wo.module.tmpAudit.model.TmpAuditPicFollowupRecEmail;
import com.wo.module.tmpAudit.model.TmpAuditPicFollowupSupportingUnit;
import com.wo.module.tmpAudit.service.TmpAuditService;
import com.wo.module.tmpAuditApproval.service.TmpAuditApprovalService;
import com.wo.module.tmpAuditApproval.vo.TmpAuditApprovalVO;
import com.wo.module.tmpCorrespondence.constant.TmpCorrespondenceConstants;
import com.wo.module.tmpFine.constant.TmpFineConstants;
import com.wo.module.trcAudit.service.TrcAuditPICFollowupService;
import com.wo.module.user.model.Division;
import com.wo.module.user.model.User;
import com.wo.module.user.service.UserService;

public class TmpAuditEditBean extends CommonBean implements SelectorListener<Object>, Serializable, AuditConstant {

	private static final long serialVersionUID = 6613096689118647308L;

	static Logger logger = Logger.getLogger(TmpAuditEditBean.class);
	
	private TmpAudit tmpAudit;

	private String actionMode;

	private String editedId;

	private Long oldCounterTypeId;
	private Long newCounterTypeId;
	
	private Boolean disabledFollowUpStatus;
	private Boolean disableMstAudit;
	private Integer indexDtlCompliance;
	private Long counterTypeId;
	
	
	private List<SelectItem> selectAuditFollowUp;
	private List<SelectItem> selectAuditObject;
	private List<SelectItem> selectAuditCategory;
	private List<SelectItem> counterTypes;
	private List<SelectItem> followUps;
	
	private List<UploadedFileWO> uploadedFilesAuditFindings;
	private List<UploadedFileWO> uploadedFilesAuditBankResponse;
	private List<UploadedFileWO> uploadedFilesAuditBankCommitment;
	private List<UploadedFileWO> uploadedFilesAuditBankAttachment;
	private List<UploadedFileWO> deletedAuditFindingsFiles;
	private List<UploadedFileWO> deletedAuditBankResponseFiles;
	private List<UploadedFileWO> deletedAuditBankCommitmentFiles;
	private List<UploadedFileWO> deletedAuditAttachment;
	
	private FileUtil fileUtil;
	
	//services
	private TmpAuditService tmpAuditService;
	private TmpAuditService tmpAuditService2;
	private TmpAuditApprovalService tmpAuditApprovalService;
	private ParameterDetailService parameterDetailService;
	private CounterTypeService counterTypeService;
	private UserService userService;
	private TrcAuditPICFollowupService trcAuditPICFollowupService;
	private EmailTemplateService emailTemplateService;
	private MstAuditService mstAuditService;
	private HolidayService holidayService;

	private List<SelectItem> divisions;

	private List<SelectItem> reminderStatusList;
	
	private List<SelectItem> templateAuditList;

	private SelectorInfo selectorCompliance;

	private SelectorInfo selectorFollowup;

	private SelectorInfo selectorPic1;

	private SelectorInfo selectorPic2;

	private SelectorInfo selectorPic3;
	
	private SelectorInfo selectorUserCc1;
	private SelectorInfo selectorUserCc2;
	private SelectorInfo selectorUserCc3;

	private TmpAuditPicCompliance[] selectedDataCompliance;

	private TmpAuditPicFollowup[] selectedDataFollowup;

	private TmpAuditPICComplianceTableModel<TmpAuditPicCompliance> tableModelCompliance;

	private TmpAuditCheckPointTableModel<TmpAuditCheckPoint> tableModelFollowup;
	
	private Integer lastSequenceOfCompliance;
	private Integer lastSequenceOfFollowup;

	private List<StatusKonfirmasiVO> tableStatus;

	private List<TmpAuditApprovalVO> tableApproval;

	private RegulationTrackRecord selectedRow;
	
	private String divNameLogin;

	private Integer indexDtl;
	private Integer indexDtlCheckPoint;
	private Integer indexDtlFollowup;
	private Integer indexDtlBankResponse;
	private Integer indexDtlBankCommitment;
	private Integer indexDtlSuppUnit;
	private Integer indexDtlAuditFinding;
	

	public FacesUtil facesUtil;

	private String navigateSearch = TMP_AUDIT_SEARCH;

	SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
	
	private List<SelectItem> yesNo;
	
	private String textWarningUpload;
	
	@PostConstruct
	public void init() {
		super.init();
		fileUtil = FileUtil.getInstance();
		
		constructSelectComponent();
		
		this.divNameLogin = facesUtil.getUserLogin().getDivisionName();
		selectorCompliance = TmpFineConstants.buildSelectorUserCompliance(divNameLogin);
		selectorFollowup = RegulationSocializationConstants.buildSelectorDivision(facesUtil);
		selectorPic1 = RegulationSocializationConstants.buildSelectorPIC(facesUtil);
		selectorPic2 = RegulationSocializationConstants.buildSelectorPIC(facesUtil);
		selectorPic3 = RegulationSocializationConstants.buildSelectorPIC(facesUtil);
		
		selectorUserCc1 = TmpCorrespondenceConstants.buildSelectorUser();
		selectorUserCc2 = TmpCorrespondenceConstants.buildSelectorUser();
		selectorUserCc3 = TmpCorrespondenceConstants.buildSelectorUser();
		
		checkNewOrEdit();
		
		try {
			ParameterDetail getText = parameterDetailService.getParameterDetailByParamDtlCode("UPLOAD_WARNING_TEXT");
			textWarningUpload = getText.getName();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void constructSelectComponent() {
		setupAuditFollowUp();
		setupAuditObject();
		setupAuditCategory();
		setupAuditCounterType();
		setupAuditPicFollowupDivision();
		setupReminderStatus();
		setupFollowup();
		setupYesNo();
		setupTemplateAuditList();
	}
	
	private void setupTemplateAuditList() {
		templateAuditList = new ArrayList<SelectItem>();
		try {
			List<MstAudit> allDataMstAudit = mstAuditService.getAllMstAuditData();
			for (int i = 0; i < allDataMstAudit.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel((allDataMstAudit.get(i)).getAuditTemplate());
				si.setValue((allDataMstAudit.get(i)).getMstAuditId());
				templateAuditList.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		PrimeFaces.current().executeScript("initSelect2();");
	}
	
	
	
	private void setupYesNo() {
		yesNo = new ArrayList<SelectItem>();
		yesNo.add(new SelectItem(Constants.CONSTANT_YES));
		yesNo.add(new SelectItem(Constants.CONSTANT_NO));
	}
	
	private void setupAuditFollowUp() {
		setSelectAuditFollowUp(new ArrayList<SelectItem>());
		try {
			setSelectAuditFollowUp(parameterDetailService.getListLabelValue(ParameterHeader.PARAM_HEAD_CODE_AUDITOR,
					false, true, facesUtil.retrieveDefaultLocale()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void setupAuditObject() {
		selectAuditObject = new ArrayList<SelectItem>();
		try {
			selectAuditObject = parameterDetailService.getListLabelValue(ParameterHeader.PARAM_HEAD_CODE_AUDIT_OBJECT,
					false, true, facesUtil.retrieveDefaultLocale());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setupAuditCounterType() {
		try {
			counterTypes = counterTypeService.getAllCounterTypeLabelValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void setupAuditCategory() {
		selectAuditCategory = new ArrayList<SelectItem>();
		try {
			selectAuditCategory = parameterDetailService.getListLabelValue(ParameterHeader.PARAM_HEAD_CODE_AUDIT_CATEGORY,
					false, true, facesUtil.retrieveDefaultLocale());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setupAuditPicFollowupDivision() {
		divisions = new ArrayList<SelectItem>();
		try {
			List<Division> pd = userService.getAllDivision();
			for (int i = 0; i < pd.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((Division) pd.get(i)).getDivisionName());
				si.setValue(((Division) pd.get(i)).getDivisionId());
				divisions.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		PrimeFaces.current().executeScript("initSelect2();");
	}
	
	public void setupFollowup() {
		followUps = new ArrayList<SelectItem>();
		SelectItem si = new SelectItem();
		si.setLabel("Yes");
		si.setValue(Y);
		followUps.add(si);

		SelectItem si2 = new SelectItem();
		si2.setLabel("No");
		si2.setValue(N);
		followUps.add(si2);
	}
	
	public void reinitJs() {
		JsUtil.initSelect2();
	}
	
	public void handleFileUploadAuditFindings(FileUploadEvent event) throws Exception {
		try {
			uploadedFilesAuditFindings = uploadedFilesAuditFindings == null ? new ArrayList<UploadedFileWO>() : uploadedFilesAuditFindings;
			uploadedFilesAuditFindings.add(new UploadedFileWO(
				CallApiManager.callUploadAPI(event.getFile(), Constants.COMPLIANCE_DOC_TYPE_AUDIT_FINDINGS,
						parameterDetailService, false, getFileUtil()),
				event.getFile().getFileName(), event.getFile().getContentType(), event.getFile().getSize(), true));
		} catch (Exception e) {
			e.printStackTrace();
			facesUtil.addErrMessage(e.getMessage());
		}
	}

	public void handleFileUploadAuditBankResponse(FileUploadEvent event) throws Exception {
		try {
			uploadedFilesAuditBankResponse = uploadedFilesAuditBankResponse == null ? new ArrayList<UploadedFileWO>() : uploadedFilesAuditBankResponse;
			uploadedFilesAuditBankResponse.add(new UploadedFileWO(
				CallApiManager.callUploadAPI(event.getFile(), Constants.COMPLIANCE_DOC_TYPE_AUDIT_BANK_RESPONSE,
						parameterDetailService, false, getFileUtil()),
				event.getFile().getFileName(), event.getFile().getContentType(), event.getFile().getSize(), true));
		} catch (Exception e) {
			e.printStackTrace();
			facesUtil.addErrMessage(e.getMessage());
		}
	}
	
	public void handleFileUploadAuditBankCommitment(FileUploadEvent event) throws Exception {
		try {
			uploadedFilesAuditBankCommitment = uploadedFilesAuditBankCommitment == null ? new ArrayList<UploadedFileWO>() : uploadedFilesAuditBankCommitment;
			uploadedFilesAuditBankCommitment.add(new UploadedFileWO(
				CallApiManager.callUploadAPI(event.getFile(), Constants.COMPLIANCE_DOC_TYPE_AUDIT_BANK_COMMITMENT,
						parameterDetailService, false, getFileUtil()),
				event.getFile().getFileName(), event.getFile().getContentType(), event.getFile().getSize(), true));
		} catch (Exception e) {
			e.printStackTrace();
			facesUtil.addErrMessage(e.getMessage());
		}
	}
	
	public void handleFileUploadAuditAttachment(FileUploadEvent event) throws Exception {
		try {
			if(uploadedFilesAuditBankAttachment == null)
				uploadedFilesAuditBankAttachment = new ArrayList<UploadedFileWO>();
				
			uploadedFilesAuditBankAttachment.add(new UploadedFileWO(
				CallApiManager.callUploadAPI(event.getFile(), Constants.COMPLIANCE_DOC_TYPE_AUDIT_FOLLOWUP,
						parameterDetailService, false, getFileUtil()),
				event.getFile().getFileName(), event.getFile().getContentType(), event.getFile().getSize(), true));
		} catch (Exception e) {
			e.printStackTrace();
			facesUtil.addErrMessage(e.getMessage());
		}
	}

	public void setupReminderStatus() {
		reminderStatusList = new ArrayList<SelectItem>();
		try {
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
	
	public void onChangeDivision(int i) {		
		/*TmpAuditPicFollowup data = tmpAudit.getTmpAuditPicFollowups().get(i);
		data.setUser1(null);
		data.setUser2(null);
		data.setUser3(null);*/
		PrimeFaces.current().executeScript("initSelect2();");
	}
	
	public void onChangeSuppUnitDivision(int picFollowupIdx, int picEmailIdx) {		
		/*tmpAudit.getTmpAuditPicFollowups().get(picFollowupIdx).getTmpAuditPicFollowupSupportingUnits()
			.get(picEmailIdx).setEmailCc1(null);
		tmpAudit.getTmpAuditPicFollowups().get(picFollowupIdx).getTmpAuditPicFollowupSupportingUnits()
			.get(picEmailIdx).setEmailCcTemp1(null);
		tmpAudit.getTmpAuditPicFollowups().get(picFollowupIdx).getTmpAuditPicFollowupSupportingUnits()
			.get(picEmailIdx).setEmailCc2(null);
		tmpAudit.getTmpAuditPicFollowups().get(picFollowupIdx).getTmpAuditPicFollowupSupportingUnits()
		.get(picEmailIdx).setEmailCcTemp2(null);
		tmpAudit.getTmpAuditPicFollowups().get(picFollowupIdx).getTmpAuditPicFollowupSupportingUnits()
			.get(picEmailIdx).setEmailCc3(null);
		tmpAudit.getTmpAuditPicFollowups().get(picFollowupIdx).getTmpAuditPicFollowupSupportingUnits()
		.get(picEmailIdx).setEmailCcTemp3(null);*/
		
		PrimeFaces.current().executeScript("initSelect2();");
	}
	
	public void clearPicDetail2(/*int i*/) {	
		//tmpAudit.getTmpAuditPicFollowups().get(i).setUser2(null);
		tmpAudit.getTmpAuditCheckPoints().get(indexDtlCheckPoint).getTmpAuditPicFollowupBankCommitments().get(indexDtlBankCommitment).getTmpAuditPicFollowups().get(indexDtlFollowup).setUser2(null);
		//PrimeFaces.current().executeScript("initSelect2();");
		PrimeFaces.current().ajax()
		.update("form:listAuditCheckPoint:"+indexDtlCheckPoint+":listBankCommitment:"+indexDtlBankCommitment+":listPicFollowup:"+indexDtlFollowup+":panelPicFollowup");
	}
	
	public void clearPicDetail3(/*int i*/) {	
		//tmpAudit.getTmpAuditPicFollowups().get(i).setUser3(null);
		tmpAudit.getTmpAuditCheckPoints().get(indexDtlCheckPoint).getTmpAuditPicFollowupBankCommitments().get(indexDtlBankCommitment).getTmpAuditPicFollowups().get(indexDtlFollowup).setUser3(null);
		//PrimeFaces.current().executeScript("initSelect2();");
		PrimeFaces.current().ajax()
		.update("form:listAuditCheckPoint:"+indexDtlCheckPoint+":listBankCommitment:"+indexDtlBankCommitment+":listPicFollowup:"+indexDtlFollowup+":panelPicFollowup");
	}
	
	public void clearPicSuppUnitDetail2() {	
		/*tmpAudit.getTmpAuditPicFollowups().get(picFollowupIdx).getTmpAuditPicFollowupSupportingUnits()
			.get(picEmailIdx).setEmailCc2(null);
		tmpAudit.getTmpAuditPicFollowups().get(picFollowupIdx).getTmpAuditPicFollowupSupportingUnits()
			.get(picEmailIdx).setEmailCcTemp2(null);*/
		tmpAudit.getTmpAuditCheckPoints().get(indexDtlCheckPoint).getTmpAuditPicFollowupBankCommitments().get(indexDtlBankCommitment).getTmpAuditPicFollowups().get(indexDtlFollowup).getTmpAuditPicFollowupSupportingUnits().get(indexDtlSuppUnit).setEmailCc2(null);
		tmpAudit.getTmpAuditCheckPoints().get(indexDtlCheckPoint).getTmpAuditPicFollowupBankCommitments().get(indexDtlBankCommitment).getTmpAuditPicFollowups().get(indexDtlFollowup).getTmpAuditPicFollowupSupportingUnits().get(indexDtlSuppUnit).setEmailCcTemp2(null);
		//PrimeFaces.current().executeScript("initSelect2();");
		PrimeFaces.current().ajax()
		.update("form:listAuditCheckPoint:"+indexDtlCheckPoint+":listBankCommitment:"+indexDtlBankCommitment+":listPicFollowup:"+indexDtlFollowup+":panelSupportingUnit");
	}
	
	public void clearPicSuppUnitDetail3(/*int picFollowupIdx, int picEmailIdx*/) {	
		/*tmpAudit.getTmpAuditPicFollowups().get(picFollowupIdx).getTmpAuditPicFollowupSupportingUnits()
			.get(picEmailIdx).setEmailCc3(null);
		tmpAudit.getTmpAuditPicFollowups().get(picFollowupIdx).getTmpAuditPicFollowupSupportingUnits()
			.get(picEmailIdx).setEmailCcTemp3(null);*/
		tmpAudit.getTmpAuditCheckPoints().get(indexDtlCheckPoint).getTmpAuditPicFollowupBankCommitments().get(indexDtlBankCommitment).getTmpAuditPicFollowups().get(indexDtlFollowup).getTmpAuditPicFollowupSupportingUnits().get(indexDtlSuppUnit).setEmailCc3(null);
		tmpAudit.getTmpAuditCheckPoints().get(indexDtlCheckPoint).getTmpAuditPicFollowupBankCommitments().get(indexDtlBankCommitment).getTmpAuditPicFollowups().get(indexDtlFollowup).getTmpAuditPicFollowupSupportingUnits().get(indexDtlSuppUnit).setEmailCcTemp3(null);
		//PrimeFaces.current().executeScript("initSelect2();");
		PrimeFaces.current().ajax()
		.update("form:listAuditCheckPoint:"+indexDtlCheckPoint+":listBankCommitment:"+indexDtlBankCommitment+":listPicFollowup:"+indexDtlFollowup+":panelSupportingUnit");
	}
	
	public void onChangeFollowupStatus() {
		/*if(tmpAudit.getFollowUp() != null) {
			
			if(CommonConstants.Y.equals(tmpAudit.getFollowUp())) {				
				if(tmpAudit.getCounterType() == null) {
					tmpAudit.setCounterType(new CounterType());
				}
			} else if(CommonConstants.N.equals(tmpAudit.getFollowUp())) {				
				tmpAudit.setCounterType(null);
				counterTypeId = null;
				
				if (tmpAudit.getTmpAuditPicFollowups() != null) {				
					for (int i = 0; i < tmpAudit.getTmpAuditPicFollowups().size(); i++) {
						tmpAudit.getTmpAuditPicFollowups().get(i).setTargetDate(null);
					}
				}
			}
			
		}*/
		
		PrimeFaces.current().executeScript("initSelect2();");
	}

	private void checkNewOrEdit() {
		String editId = facesUtil.retrieveRequestParam("id");
		String token = facesUtil.retrieveRequestParam("token");
		String parentId = facesUtil.retrieveRequestParam("mstId");
		
		//String viewId = facesUtil.retrieveRequestParam("viewId");
		if (!StringUtils.isBlank(parentId)) {
			facesUtil.setSessionAttribute(EMAIL_TEMPLATE_AUDIT, Long.parseLong(parentId));
		}
		
		if (StringUtils.isBlank(editId) && StringUtils.isBlank(token)) {
			this.handleNew();
		} else {
			facesUtil.removeSessionAttribute(EMAIL_TEMPLATE_AUDIT);
			this.handleEdit(editId);
		}
	}

	private void handleNew() {
		try {
			disabledFollowUpStatus = false;
			
			tmpAudit = new TmpAudit();
			tmpAudit.setReminderStatus(ParameterDetail.PARAM_DET_CODE_REMINDER_ACTIVE);
			if (facesUtil.getSessionAttribute(EMAIL_TEMPLATE_AUDIT) != null) {
				tmpAudit.setMstAudit(mstAuditService.findById((Long) facesUtil.getSessionAttribute(EMAIL_TEMPLATE_AUDIT)));
				if (tmpAudit.getMstAudit() != null) {
					MstAudit t = tmpAudit.getMstAudit();
					tmpAudit.setAuditor(t.getAuditor());
					tmpAudit.setAuditDateFrom(t.getAuditDateFrom());
					tmpAudit.setAuditDateTo(t.getAuditDateTo());
					tmpAudit.setScope(t.getScope());
				}
				disableMstAudit = true;
			} else {
				tmpAudit.setMstAudit(new MstAudit());
				disableMstAudit = false;
			}
			
			lastSequenceOfCompliance = 0;
			lastSequenceOfFollowup = 0;
			
			actionMode = Constants.ACTION_ADD;

			tableModelCompliance = new TmpAuditPICComplianceTableModel<TmpAuditPicCompliance>(
					tmpAudit.getTmpAuditPicCompliances());

			tableModelFollowup = new TmpAuditCheckPointTableModel<TmpAuditCheckPoint>(
					tmpAudit.getTmpAuditCheckPoints());
			
			// init 1 row
			onAddCheckPoints();
			//addAuditFindings(0);
			//addBankResponse(0);
			//addBankBankCommitment(0);
			tableModelFollowup.setWrappedData(tmpAudit.getTmpAuditCheckPoints());

			// onAddNewCompliance();
			if (tmpAudit.getTmpAuditPicCompliances() == null
					|| tmpAudit.getTmpAuditPicCompliances().size() == 0) {
				tmpAudit.setTmpAuditPicCompliances(new ArrayList<TmpAuditPicCompliance>());
				lastSequenceOfCompliance = 0;
			}

			TmpAuditPicCompliance rt = new TmpAuditPicCompliance();
			lastSequenceOfCompliance = lastSequenceOfCompliance + 1;
			rt.setSequence(lastSequenceOfCompliance);
			
			User user = (User) facesUtil.getSessionAttribute(Constants.SESSION_EMPLOYEE);
			rt.setUser(user);
			tmpAudit.getTmpAuditPicCompliances().add(rt);
			tableModelCompliance.setWrappedData(tmpAudit.getTmpAuditPicCompliances());
			facesUtil.setSessionAttribute("token", null);
			
			JsUtil.initSelect2();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void onAddCheckPoints() {
		
		if(tmpAudit.getTmpAuditCheckPoints() == null)
			tmpAudit.setTmpAuditCheckPoints(new ArrayList<TmpAuditCheckPoint>());
			
		tmpAudit.getTmpAuditCheckPoints().add(new TmpAuditCheckPoint());
		
		int idx = tmpAudit.getTmpAuditCheckPoints().size() - 1;
		
		addBankCommitment(idx);
		JsUtil.initSelect2();
		JsUtil.hideTHeadFollowupPoints();
	}
	
	public void onDeleteCheckPoints() {
		tmpAudit.getTmpAuditCheckPoints().removeIf( f -> f.isChecked());
	}
	
	private void addBankCommitment(int indexPoin) {
		
		TmpAuditCheckPoint tmpAuditCheckPoint = tmpAudit.getTmpAuditCheckPoints().get(indexPoin);
		
		List<TmpAuditPicFollowup> listPic = new ArrayList<TmpAuditPicFollowup>();
		listPic.add(new TmpAuditPicFollowup());
		
		if (tmpAuditCheckPoint.getTmpAuditPicFollowupBankCommitments() == null) {
			List<TmpAuditPicFollowupBankCommitment> bankCommitment = new ArrayList<TmpAuditPicFollowupBankCommitment>();
			TmpAuditPicFollowupBankCommitment bc = new TmpAuditPicFollowupBankCommitment();
			bc.setTmpAuditCheckPoint(tmpAuditCheckPoint);
			bc.setTmpAuditPicFollowups(listPic);
			bankCommitment.add(bc);
			tmpAuditCheckPoint.setTmpAuditPicFollowupBankCommitments(bankCommitment);
			
		} else {
			TmpAuditPicFollowupBankCommitment bc = new TmpAuditPicFollowupBankCommitment();
			bc.setTmpAuditCheckPoint(tmpAuditCheckPoint);
			bc.setTmpAuditPicFollowups(listPic);
			tmpAuditCheckPoint.getTmpAuditPicFollowupBankCommitments().add(bc);
		}
	
		
		
	}
	
	public void onSelectRecurr(int indexPoin, int indexBankCommitment) {
		/*String isSelect = tmpAudit.getTmpAuditCheckPoints().get(indexPoin).getTmpAuditPicFollowupBankCommitments().get(indexBankCommitment).getIsRecurr();
		
		System.out.println("isSelect=="+isSelect);
		
		
		if(isSelect!=null && isSelect.equals("Y") && tmpAudit.getTmpAuditCheckPoints().get(indexPoin).getTmpAuditPicFollowupBankCommitments().get(indexBankCommitment).getTmpAuditPicFollowups() == null || tmpAudit.getTmpAuditCheckPoints().get(indexPoin).getTmpAuditPicFollowupBankCommitments().get(indexBankCommitment).getTmpAuditPicFollowups().size() == 0) {
			TmpAuditPicFollowup tmpAuditPicFollowup = new TmpAuditPicFollowup();
			List<TmpAuditPicFollowup> list = new ArrayList<>();
			list.add(tmpAuditPicFollowup);
			if(tmpAudit.getTmpAuditCheckPoints().get(indexPoin).getTmpAuditPicFollowupBankCommitments().get(indexBankCommitment).getTmpAuditPicFollowups() == null) {
				tmpAudit.getTmpAuditCheckPoints().get(indexPoin).getTmpAuditPicFollowupBankCommitments().get(indexBankCommitment).setTmpAuditPicFollowups(list);
			}else {
				tmpAudit.getTmpAuditCheckPoints().get(indexPoin).getTmpAuditPicFollowupBankCommitments().get(indexBankCommitment).getTmpAuditPicFollowups().add(tmpAuditPicFollowup);
			}
		}
		
		PrimeFaces.current().ajax()
		.update("form:listAuditCheckPoint:"+indexPoin+":listBankCommitment");*/
		
	}
	
	public void onAddAuditFindings(int rivAuditPicFollowup) {
		addAuditFindings(rivAuditPicFollowup);
		PrimeFaces.current().ajax()
			.update("form:listAuditCheckPoint:"+rivAuditPicFollowup+":listAuditFindings");
		
	}
	
	public void onDeleteAuditFindings(int rivAuditPicFollowup) {
		tmpAudit.getTmpAuditCheckPoints().get(rivAuditPicFollowup).getTmpAuditPicFollowupAuditFindingsTables().removeIf( f -> f.isChecked());
		

		PrimeFaces.current().ajax()
			.update("form:listAuditCheckPoint:"+rivAuditPicFollowup+":listAuditFindings");
		
	}

	private void addAuditFindings(int index) {
		if(tmpAudit.getTmpAuditCheckPoints().isEmpty())
			tmpAudit.getTmpAuditCheckPoints().add(new TmpAuditCheckPoint());
		
		
		TmpAuditCheckPoint tpf = tmpAudit.getTmpAuditCheckPoints().get(index);
		if (tpf.getTmpAuditPicFollowupAuditFindingsTables() == null) {
			List<TmpAuditPicFollowupAuditFindingsTable> auditFindings = new ArrayList<TmpAuditPicFollowupAuditFindingsTable>();
			auditFindings.add(new TmpAuditPicFollowupAuditFindingsTable());
			tpf.setTmpAuditPicFollowupAuditFindingsTables(auditFindings);
		} else {
			tpf.getTmpAuditPicFollowupAuditFindingsTables().add(new TmpAuditPicFollowupAuditFindingsTable());
		}
	}
	
	
	
	public void onAddBankCommitment() {
		addBankCommitment(indexDtlCheckPoint);
		
		PrimeFaces.current().ajax()
		.update("form:listAuditCheckPoint:"+indexDtlCheckPoint+":listBankCommitment");
		
	}
	
	public void onDeleteBankCommitment() {
		tmpAudit.getTmpAuditCheckPoints().get(indexDtlCheckPoint).getTmpAuditPicFollowupBankCommitments().removeIf( f -> f.isChecked());
			

		PrimeFaces.current().ajax()
		.update("form:listAuditCheckPoint:"+indexDtlCheckPoint+":listBankCommitment");
		
	}

	
	
	public void onAddAuditFindingsTable(/*int rivAuditPicFollowup, int rivAuditFindings*/) {
		
		System.out.println("indexDtlCheckPoint=="+indexDtlCheckPoint);
		TmpAuditCheckPoint tpff = tmpAudit.getTmpAuditCheckPoints()
				.get(indexDtlCheckPoint);
		
		if(tpff.getColumn() != 0) {
			tpff.getTmpAuditPicFollowupAuditFindingsTables();
			if(tpff.getTmpAuditPicFollowupAuditFindingsTables() == null) {
				tpff.setTmpAuditPicFollowupAuditFindingsTables(new ArrayList<TmpAuditPicFollowupAuditFindingsTable>());
				
			}
			tpff.getTmpAuditPicFollowupAuditFindingsTables().add(new TmpAuditPicFollowupAuditFindingsTable());
			
			if(tpff.getColumnModel() == null) {
				
				ColumnModel cm = new ColumnModel();
				cm.setRow(indexDtlAuditFinding);
				cm.setColumnModels(new ArrayList<ColumnModel>());
				
				int i=0;
				while(i < tpff.getColumn()) {
					cm.getColumnModels().add(new ColumnModel("column"+(i+1),"column"+(i+1)));
					i++;
				}
				
				tpff.setColumnModel(cm);
				
			}else {
	
				tpff.getTmpAuditPicFollowupAuditFindingsTables().forEach( afTables -> {
					int idx = tpff.getTmpAuditPicFollowupAuditFindingsTables().indexOf(afTables);
					
					try {
						if(tpff.getColumnModel().getColumnModels().get(idx) == null) {
							tpff.getColumnModel().getColumnModels().add(new ColumnModel());
						}
					} catch (IndexOutOfBoundsException e) {
						ColumnModel cm = new ColumnModel();
						cm.setRow(idx);
						tpff.getColumnModel().getColumnModels().add(new ColumnModel());
					}
						
					int i=tpff.getColumnModel().getColumnModels().size();
					while(i < tpff.getColumn()) {
						tpff.getColumnModel().getColumnModels().add(new ColumnModel("column"+(i+1),"column"+(i+1)));
						i++;
					}
					
					if(tpff.getColumn() < tpff.getColumnModel().getColumnModels().size()) {
						int size = tpff.getColumnModel().getColumnModels().size();
						while(size > tpff.getColumn()) {
							tpff.getColumnModel().getColumnModels().remove(size-1);
							try {
								Class<?>[] paramTypes = {String.class};
								Method setMethod = afTables.getClass().getMethod("setColumn"+(size)+"",paramTypes);
								setMethod.invoke(afTables, (Object) null);
							} catch (NoSuchMethodException e) {
								e.printStackTrace();
							} catch (SecurityException e) {
								e.printStackTrace();
							} catch (IllegalAccessException e) {
								e.printStackTrace();
							} catch (IllegalArgumentException e) {
								e.printStackTrace();
							} catch (InvocationTargetException e) {
								e.printStackTrace();
							}
							size--;
						}
					}
				});
			}
			
	
			
			PrimeFaces.current().ajax()
				.update("form:listAuditCheckPoint:"+indexDtlCheckPoint+":listAuditFindings:"+indexDtlAuditFinding+":dataTableAuditFindingsTable");
		}
	}
	
	public void onChangeColumn(int rivAuditPicFollowup, int rivAuditFindings) {
		TmpAuditCheckPoint tpff = tmpAudit.getTmpAuditCheckPoints()
				.get(rivAuditPicFollowup);
		
		if(tpff.getColumn() != 0) {
			if(tpff.getTmpAuditPicFollowupAuditFindingsTables() != null) {
				
				if(tpff.getColumnModel() == null) {
					
					ColumnModel cm = new ColumnModel();
					cm.setRow(rivAuditFindings);
					cm.setColumnModels(new ArrayList<ColumnModel>());
					
					int i=0;
					while(i < tpff.getColumn()) {
						cm.getColumnModels().add(new ColumnModel("column"+(i+1),"column"+(i+1)));
						i++;
					}
					
					tpff.setColumnModel(cm);
				}else {
		
					tpff.getTmpAuditPicFollowupAuditFindingsTables().forEach( afTables -> {
						int idx = tpff.getTmpAuditPicFollowupAuditFindingsTables().indexOf(afTables);
						
						try {
							if(tpff.getColumnModel().getColumnModels().get(idx) == null) {
								tpff.getColumnModel().getColumnModels().add(new ColumnModel());
							}
						} catch (IndexOutOfBoundsException e) {
							ColumnModel cm = new ColumnModel();
							cm.setRow(idx);
							tpff.getColumnModel().getColumnModels().add(new ColumnModel());
						}
							
						int i=tpff.getColumnModel().getColumnModels().size();
						while(i < tpff.getColumn()) {
							tpff.getColumnModel().getColumnModels().add(new ColumnModel("column"+(i+1),"column"+(i+1)));
							i++;
						}
						
						if(tpff.getColumn() < tpff.getColumnModel().getColumnModels().size()) {
							int size = tpff.getColumnModel().getColumnModels().size();
							while(size > tpff.getColumn()) {
								tpff.getColumnModel().getColumnModels().remove(size-1);
								try {
									Class<?>[] paramTypes = {String.class};
									Method setMethod = afTables.getClass().getMethod("setColumn"+(size)+"",paramTypes);
									setMethod.invoke(afTables, (Object) null);
								} catch (NoSuchMethodException e) {
									e.printStackTrace();
								} catch (SecurityException e) {
									e.printStackTrace();
								} catch (IllegalAccessException e) {
									e.printStackTrace();
								} catch (IllegalArgumentException e) {
									e.printStackTrace();
								} catch (InvocationTargetException e) {
									e.printStackTrace();
								}
								size--;
							}
						}
					});
				}
			}
		}
	}
	
	public void onDeleteAuditFindingsTable(/*int rivAuditPicFollowup, int rivAuditFindings*/) {
		TmpAuditCheckPoint tpff = tmpAudit.getTmpAuditCheckPoints()
				.get(indexDtlCheckPoint);
		tpff.getTmpAuditPicFollowupAuditFindingsTables().removeIf( f -> f.isChecked());
		
	}
	
	
	
	public void onAddSupportingUnit() {
		addSupportingUnit(indexDtlCheckPoint,indexDtlBankCommitment);
		
		
		PrimeFaces.current().ajax()
		.update("form:listAuditCheckPoint:"+indexDtlCheckPoint+":listBankCommitment:"+indexDtlBankCommitment+":listPicFollowup:0:tableSupportingUnitDetail");
		JsUtil.initSelect2();
	}
	
	public void onDeleteSupportingUnit() {
		tmpAudit.getTmpAuditCheckPoints().get(indexDtlCheckPoint).getTmpAuditPicFollowupBankCommitments().get(indexDtlBankCommitment).getTmpAuditPicFollowups().get(0).getTmpAuditPicFollowupSupportingUnits().removeIf( f -> f.isChecked());
			
		if(tmpAudit.getTmpAuditCheckPoints().get(indexDtlCheckPoint).getTmpAuditPicFollowupBankCommitments().get(indexDtlBankCommitment).getTmpAuditPicFollowups().get(0).getTmpAuditPicFollowupSupportingUnits().size() == 0)
			tmpAudit.getTmpAuditCheckPoints().get(indexDtlCheckPoint).getTmpAuditPicFollowupBankCommitments().get(indexDtlBankCommitment).getTmpAuditPicFollowups().get(0).setTmpAuditPicFollowupSupportingUnits(new ArrayList<TmpAuditPicFollowupSupportingUnit>());
		
		
		
		PrimeFaces.current().ajax()
		.update("form:listAuditCheckPoint:"+indexDtlCheckPoint+":listBankCommitment:"+indexDtlBankCommitment+":listPicFollowup:0:tableSupportingUnitDetail");
		
		JsUtil.initSelect2();
	}

	private void addSupportingUnit(int indexPoin, int indexBankCommitment) {
		
		TmpAuditPicFollowup tpf = tmpAudit.getTmpAuditCheckPoints().get(indexPoin).getTmpAuditPicFollowupBankCommitments().get(indexBankCommitment).getTmpAuditPicFollowups().get(0);
		if (tpf.getTmpAuditPicFollowupSupportingUnits() == null) {
			List<TmpAuditPicFollowupSupportingUnit> bankCommitment = new ArrayList<TmpAuditPicFollowupSupportingUnit>();
			TmpAuditPicFollowupSupportingUnit bc = new TmpAuditPicFollowupSupportingUnit();
			bankCommitment.add(bc);
			tpf.setTmpAuditPicFollowupSupportingUnits(bankCommitment);
		} else {
			TmpAuditPicFollowupSupportingUnit bc = new TmpAuditPicFollowupSupportingUnit();
			tpf.getTmpAuditPicFollowupSupportingUnits().add(bc);
		}
		
		
	}
	
	private void handleEdit(String editId) {
		String token = facesUtil.retrieveRequestParam("token");
		if(StringUtils.isNotEmpty(token)) {
			editId = Constants.decryptString(token);
		}
		facesUtil.setSessionAttribute("token", Constants.encryptString(editId));
		actionMode = Constants.ACTION_EDIT;
		disabledFollowUpStatus = false;
		Long idLong = Long.parseLong(editId);
		tmpAudit = tmpAuditService.findById(idLong);
		
		for(TmpAuditCheckPoint tapf : tmpAudit.getTmpAuditCheckPoints() ) {
			
			if(tapf.getColumnModel() == null) {
				int idx = tmpAudit.getTmpAuditCheckPoints().indexOf(tapf);
				ColumnModel cm = new ColumnModel();
				cm.setRow(idx);
				cm.setColumnModels(new ArrayList<ColumnModel>());
				
				int i=0;
				while(i < tapf.getColumn()) {
					cm.getColumnModels().add(new ColumnModel("column"+(i+1),"column"+(i+1)));
					i++;
				}
				
				tapf.setColumnModel(cm);
			}
			
			tapf.getTmpAuditPicFollowupBankCommitments().forEach( bCommit -> {
			
			if (bCommit.getTmpAuditPicFollowups() != null) {
				lastSequenceOfFollowup = bCommit.getTmpAuditPicFollowups().size();
				for (int i = 0; i < bCommit.getTmpAuditPicFollowups().size(); i++) {
					TmpAuditPicFollowup dtl = bCommit.getTmpAuditPicFollowups().get(i);
					lastSequenceOfFollowup = lastSequenceOfFollowup + 1;
					dtl.setSequence(lastSequenceOfFollowup);
					dtl.setOldTargetDate(dtl.getTargetDate());

				}
			}
			
			bCommit.getTmpAuditPicFollowups().forEach(picFollowup -> {
			
			if(picFollowup.getTmpAuditPicFollowupSupportingUnits() != null
					&& !picFollowup.getTmpAuditPicFollowupSupportingUnits().isEmpty())
				picFollowup.getTmpAuditPicFollowupSupportingUnits().forEach(supp -> {
					if (supp.getEmailCc1() != null)
						supp.setEmailCcTemp1(supp.getEmailCc1().getNik() + "-" + supp.getEmailCc1().getName());
					if (supp.getEmailCc2() != null)
						supp.setEmailCcTemp2(supp.getEmailCc2().getNik() + "-" + supp.getEmailCc2().getName());
					if (supp.getEmailCc3() != null)
						supp.setEmailCcTemp3(supp.getEmailCc3().getNik() + "-" + supp.getEmailCc3().getName());
				});
			});
		});
		}

		lastSequenceOfCompliance = 0;
		lastSequenceOfFollowup = 0;

		if (tmpAudit.getCounterType() != null) {
			counterTypeId = tmpAudit.getCounterType().getCounterTypeId();
			oldCounterTypeId = new Long(counterTypeId);
		}

		if (tmpAudit.getTmpAuditPicCompliances() != null) {
			lastSequenceOfCompliance = tmpAudit.getTmpAuditPicCompliances().size();
			for (int i = 0; i < tmpAudit.getTmpAuditPicCompliances().size(); i++) {
				TmpAuditPicCompliance dtl = tmpAudit.getTmpAuditPicCompliances().get(i);
				lastSequenceOfCompliance = lastSequenceOfCompliance + 1;
				dtl.setSequence(lastSequenceOfCompliance);
			}
		}

		

		
		uploadedFilesAuditBankAttachment = new ArrayList<UploadedFileWO>();
		for (TmpAuditDocument tad : tmpAudit.getTmpAuditDocuments()) {
			UploadedFileWO uf = new UploadedFileWO();
			uf.setFileName(tad.getAttachmentFile());
			uf.setFileId(tad.getFileId());
			uf.setIsNew(false);
			uf.setFileSize(tad.getFileSize());
			uploadedFilesAuditBankAttachment.add(uf);
		}

		tableModelCompliance = new TmpAuditPICComplianceTableModel<TmpAuditPicCompliance>(
				tmpAudit.getTmpAuditPicCompliances());
		tableModelFollowup = new TmpAuditCheckPointTableModel<TmpAuditCheckPoint>(tmpAudit.getTmpAuditCheckPoints());

	}

	public void onAddNewCompliance() {
		if (tmpAudit.getTmpAuditPicCompliances() == null) {
			tmpAudit.setTmpAuditPicCompliances(new ArrayList<TmpAuditPicCompliance>());
			lastSequenceOfCompliance = 0;
		}  else {
			if(tmpAudit.getTmpAuditPicCompliances().size() == 0) {
				lastSequenceOfCompliance = 0;
			}			
		} 

		TmpAuditPicCompliance rt = new TmpAuditPicCompliance();
		lastSequenceOfCompliance = lastSequenceOfCompliance + 1;
		rt.setSequence(lastSequenceOfCompliance);
		// rt.setUser(new User());
		tmpAudit.getTmpAuditPicCompliances().add(rt);
		tableModelCompliance.setWrappedData(tmpAudit.getTmpAuditPicCompliances());

	}

	public void onDeleteRowCompliance() {
		for (int i = 0; i < selectedDataCompliance.length; i++) {
			tmpAudit.getTmpAuditPicCompliances().remove(selectedDataCompliance[i]);
		}
		
		if (tmpAudit.getTmpAuditPicCompliances() == null
				|| tmpAudit.getTmpAuditPicCompliances().size() == 0) {
			lastSequenceOfCompliance = 0;
		}

		tableModelCompliance.setWrappedData(tmpAudit.getTmpAuditPicCompliances());
	}

	public void onAddNewFollowup() {
		if (tmpAudit.getTmpAuditCheckPoints() == null
				|| tmpAudit.getTmpAuditCheckPoints().size() == 0) {
			tmpAudit.setTmpAuditCheckPoints(new ArrayList<TmpAuditCheckPoint>());
			lastSequenceOfFollowup = 0;
		}  else {
			if(tmpAudit.getTmpAuditCheckPoints().size() == 0) {
				lastSequenceOfFollowup = 0;
			}			
		} 

		TmpAuditCheckPoint rt = new TmpAuditCheckPoint();
		lastSequenceOfFollowup = lastSequenceOfFollowup + 1;
		rt.setSequence(lastSequenceOfFollowup);
		//rt.setIsEditableTemp(true);
		tmpAudit.getTmpAuditCheckPoints().add(rt);

		tableModelFollowup.setWrappedData(tmpAudit.getTmpAuditCheckPoints());
		
		PrimeFaces.current().executeScript("initSelect2();");
		//RequestContext.getCurrentInstance().execute("initSelect2();");

	}

	public void onDeleteRowFollowup() {
		for (int i = 0; i < selectedDataFollowup.length; i++) {
			tmpAudit.getTmpAuditCheckPoints().remove(selectedDataFollowup[i]);
		}
		
		if (tmpAudit.getTmpAuditCheckPoints() == null
				|| tmpAudit.getTmpAuditCheckPoints().size() == 0) {
			lastSequenceOfFollowup = 0;
		}

		tableModelFollowup.setWrappedData(tmpAudit.getTmpAuditCheckPoints());
		
		PrimeFaces.current().executeScript("initSelect2();");
		//RequestContext.getCurrentInstance().execute("initSelect2();");
	}

	public Boolean validate() {
		Boolean flag = false;
		
		Set<String> checkDuplicates = new HashSet<>();

		if(tmpAudit.getTmpAuditCheckPoints()!=null && tmpAudit.getTmpAuditCheckPoints().size()>0) {
			
			for(int i=0;i<tmpAudit.getTmpAuditCheckPoints().size();i++) {
				TmpAuditCheckPoint tmpAuditCheckPoint = tmpAudit.getTmpAuditCheckPoints().get(i);
				if(StringUtils.isEmpty(tmpAuditCheckPoint.getBankResponse())) {
					facesUtil.addErrMessage("Tanggapan Bank "
							+ facesUtil.retrieveMessage("validateRequired"));
					flag = true;
					break;
				}
				if(StringUtils.isEmpty(tmpAuditCheckPoint.getAuditFindings())) {
					facesUtil.addErrMessage("Temuan Pemeriksaan "
							+ facesUtil.retrieveMessage("validateRequired"));
					flag = true;
					break;
				}
				if(tmpAuditCheckPoint.getTmpAuditPicFollowupBankCommitments()!=null && tmpAuditCheckPoint.getTmpAuditPicFollowupBankCommitments().size()>0) {
					for(int x=0;x<tmpAuditCheckPoint.getTmpAuditPicFollowupBankCommitments().size();x++) {
						TmpAuditPicFollowupBankCommitment tmpAuditPicFollowupBankCommitment = tmpAuditCheckPoint.getTmpAuditPicFollowupBankCommitments().get(x);
						
						if(StringUtils.isEmpty(tmpAuditPicFollowupBankCommitment.getBankCommitment())) {
							facesUtil.addErrMessage("Komitmen Bank "
									+ facesUtil.retrieveMessage("validateRequired"));
							flag = true;
							break;
						}
						
						StringBuilder sb = new StringBuilder();
						
						sb.append(tmpAuditCheckPoint.getAuditFindings())
						.append("||").append(tmpAuditCheckPoint.getBankResponse())
						.append("||").append(tmpAuditPicFollowupBankCommitment.getBankCommitment());
						
						if (!checkDuplicates.add(sb.toString())) {
							facesUtil.addErrMessage("Terdapat Duplikasi untuk Komitmen Bank : <br/>"+tmpAuditPicFollowupBankCommitment.getBankCommitment());
							flag = true;
							break;
						}
						
						if(tmpAuditService.hasDuplicateBankCommitment(tmpAudit.getAuditId(), tmpAuditCheckPoint.getAuditFindings(), tmpAuditCheckPoint.getAuditFindings(), tmpAuditPicFollowupBankCommitment.getBankCommitment())) {
							facesUtil.addErrMessage("Komitmen Bank : <br/>"+tmpAuditPicFollowupBankCommitment.getBankCommitment()+"<br/> Sudah Ada");
							flag = true;
							break;
						}
						
						if(tmpAuditPicFollowupBankCommitment.getIsRecurr()!=null && tmpAuditPicFollowupBankCommitment.getIsRecurr().equals("Y") && tmpAuditPicFollowupBankCommitment.getRecurringEndDate() == null) {
							facesUtil.addErrMessage("Recurring End Date "
									+ facesUtil.retrieveMessage("validateRequired"));
							flag = true;
							break;
						}
						else if(tmpAuditPicFollowupBankCommitment.getIsRecurr()!=null && tmpAuditPicFollowupBankCommitment.getIsRecurr().equals("Y") && StringUtils.isEmpty(tmpAuditPicFollowupBankCommitment.getRecurringType())) {
							facesUtil.addErrMessage("Tipe Recurring "
									+ facesUtil.retrieveMessage("validateRequired"));
							flag = true;
							break;
						}
						if(tmpAuditPicFollowupBankCommitment.getTmpAuditPicFollowups()!=null && tmpAuditPicFollowupBankCommitment.getTmpAuditPicFollowups().size()>0) {
							for(int z=0;z<tmpAuditPicFollowupBankCommitment.getTmpAuditPicFollowups().size();z++) {
								TmpAuditPicFollowup tmpAuditPicFollowup = tmpAuditPicFollowupBankCommitment.getTmpAuditPicFollowups().get(z);
								if(tmpAuditPicFollowup.getUser1() == null) {
									facesUtil.addErrMessage("PIC Tindak Lanjut "
											+ facesUtil.retrieveMessage("validateRequired"));
									flag = true;
									break;
								}
								
								if(tmpAudit.getFollowUp()!=null && tmpAudit.getFollowUp().equals("Y") && tmpAuditPicFollowup.getTargetDate() == null) {
									facesUtil.addErrMessage("Target Date "
											+ facesUtil.retrieveMessage("validateRequired"));
									flag = true;
									break;
								}
								
								if(tmpAuditPicFollowup.getTmpAuditPicFollowupSupportingUnits()!=null && tmpAuditPicFollowup.getTmpAuditPicFollowupSupportingUnits().size()>0) {
									for(int y=0;y<tmpAuditPicFollowup.getTmpAuditPicFollowupSupportingUnits().size();y++) {
										TmpAuditPicFollowupSupportingUnit tmpAuditPicFollowupSupportingUnit = tmpAuditPicFollowup.getTmpAuditPicFollowupSupportingUnits().get(y);
										if(tmpAuditPicFollowupSupportingUnit.getEmailCc1() == null) {
											facesUtil.addErrMessage("PIC Unit Pendukung "
													+ facesUtil.retrieveMessage("validateRequired"));
											flag = true;
											break;
										}
									}
								}
							}
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

				if (counterTypeId != null) {
					CounterType ct = counterTypeService.findById(counterTypeId);
					tmpAudit.setCounterType(ct);
				}
				
				ParameterDetail statusPd= parameterDetailService.getParameterDetailByParamDtlCode(ParameterDetail.PARAM_DET_CODE_DATA_NEW);
				if(statusPd != null)
					tmpAudit.setStatus(statusPd);
				
				if (tmpAudit.getTmpAuditDocuments() == null
						|| tmpAudit.getTmpAuditDocuments().size() == 0) {
					tmpAudit.setTmpAuditDocuments(new ArrayList<TmpAuditDocument>());
				}
				tmpAudit.getTmpAuditDocuments().clear();
				if(uploadedFilesAuditBankAttachment != null) {
					for (int i = 0; i < uploadedFilesAuditBankAttachment.size(); i++) {
						
						UploadedFileWO uf = (UploadedFileWO) uploadedFilesAuditBankAttachment.get(i);
						
							TmpAuditDocument auditAttachmentDoc = new TmpAuditDocument();
							auditAttachmentDoc.setTmpAudit(tmpAudit);
		
							auditAttachmentDoc.setAttachmentFile(uf.getFileName());
							auditAttachmentDoc.setCreatedBy(facesUtil.retrieveUserLogin());
							auditAttachmentDoc.setCreationDate(new Timestamp(new Date().getTime()));
							auditAttachmentDoc.setDelId(new Long(0));
							auditAttachmentDoc.setEnabledFlag(Constants.CONSTANT_YES);
							
							auditAttachmentDoc.setFileId(uf.getFileId());
							auditAttachmentDoc.setFileSize(uf.getFileSize());
							tmpAudit.getTmpAuditDocuments().add(auditAttachmentDoc);
						
					}
				}


				if (tmpAudit.getTmpAuditPicCompliances() != null) {
					for(TmpAuditPicCompliance tapc : tmpAudit.getTmpAuditPicCompliances() ) {
						EntityUtil.setCreationInfo(tapc, facesUtil.retrieveUserLogin());
						tapc.setTmpAudit(tmpAudit);
					}
				}

				if (tmpAudit.getTmpAuditCheckPoints() != null) {
					for(TmpAuditCheckPoint tapf : tmpAudit.getTmpAuditCheckPoints() ) {
						
						
						if(tapf.getTmpAuditPicFollowupAuditFindingsTables() != null && !tapf.getTmpAuditPicFollowupAuditFindingsTables().isEmpty()) {
							
							tapf.getTmpAuditPicFollowupAuditFindingsTables().forEach(findingTables -> {
										findingTables.setTmpAuditCheckPoint(tapf);
										
										int idx = tapf.getTmpAuditPicFollowupAuditFindingsTables().indexOf(findingTables);
										
										if(Constants.CONSTANT_YES.equals(tapf.getHasHeader()) && idx == 0)
										{
											findingTables.setIsHeader(Constants.CONSTANT_YES);
										}
											
										if(findingTables.getAuditPicFollowupAuditFindingsTableId() != null)
											EntityUtil.setUpdateInfo(findingTables, facesUtil.retrieveUserLogin());
										else
											EntityUtil.setCreationInfo(findingTables, facesUtil.retrieveUserLogin());
									});
						}
						
						if(tapf.getTmpAuditPicFollowupBankCommitments() != null && !tapf.getTmpAuditPicFollowupBankCommitments().isEmpty()) {
							tapf.getTmpAuditPicFollowupBankCommitments().forEach( bCommit -> {
								bCommit.setTmpAuditCheckPoint(tapf);
								if(bCommit.getAuditPicFollowupBankCommitmentId() != null) {
									EntityUtil.setUpdateInfo(bCommit, facesUtil.retrieveUserLogin());
								}else {
									EntityUtil.setCreationInfo(bCommit, facesUtil.retrieveUserLogin());
								}
								
								if(bCommit.getTmpAuditPicFollowups() !=null && !bCommit.getTmpAuditPicFollowups().isEmpty()) {
									
									//bCommit.getTmpAuditPicFollowups().removeIf( f -> f.getUser1() == null);
									
									bCommit.getTmpAuditPicFollowups().forEach( picFollow -> {
										
										picFollow.setTmpAuditPicFollowupBankCommitment(bCommit);
										
										if(picFollow.getAuditPicFollowupId() != null) {
											EntityUtil.setUpdateInfo(picFollow, facesUtil.retrieveUserLogin());
										}else {
											EntityUtil.setCreationInfo(picFollow, facesUtil.retrieveUserLogin());
										}
										
										if(picFollow.getTmpAuditPicFollowupSupportingUnits() != null && !picFollow.getTmpAuditPicFollowupSupportingUnits().isEmpty()) {
											picFollow.getTmpAuditPicFollowupSupportingUnits().forEach( supp -> {
												supp.setTmpAuditPicFollowup(picFollow);
												if(supp.getAuditPicFollowupSupportingUnitId() != null)
													EntityUtil.setUpdateInfo(supp, facesUtil.retrieveUserLogin());
												else
													EntityUtil.setCreationInfo(supp, facesUtil.retrieveUserLogin());
											});
										}
										
										
										
										// save email reminder
										if (picFollow.getTargetDate() != null) {
											if (tmpAudit.getCounterType() != null 
													&& tmpAudit.getCounterType().getDetails() != null 
													&& !tmpAudit.getCounterType().getDetails().isEmpty()) {
												//jika recurring
												if(picFollow.getTmpAuditPicFollowupBankCommitment().getIsRecurr()!=null && picFollow.getTmpAuditPicFollowupBankCommitment().getIsRecurr().equals("Y")) {
													Boolean flagRec = false;
													if(picFollow.getTmpAuditPicFollowupRecs()!=null && picFollow.getTmpAuditPicFollowupRecs().size()>0) {
														flagRec = true;
													}
													
													
													
													Calendar calendarRec = Calendar.getInstance();
													Date targetDateTmpRec = picFollow.getTargetDate();
													Date endDateRec = picFollow.getTmpAuditPicFollowupBankCommitment().getRecurringEndDate();
													Integer recurringMonth = Integer.parseInt(picFollow.getTmpAuditPicFollowupBankCommitment().getRecurringType());
													Boolean flagLoopMonthRec = false;
													calendarRec.setTime(targetDateTmpRec);
													List<Date> listTargetDate = new ArrayList<>();
													listTargetDate.add(targetDateTmpRec);
													while(!flagLoopMonthRec) {
														calendarRec.add(Calendar.MONTH, recurringMonth);
														if(calendarRec.getTime().before(endDateRec)) {
															listTargetDate.add(calendarRec.getTime());
														}else {
															flagLoopMonthRec = true;
														}
														
													}
													
													if(flagRec) {
														if(picFollow.getTmpAuditPicFollowupRecs().size() > listTargetDate.size()) {
															List<Object> listDeleted = new ArrayList<>();
															
															for(int i=0;i<picFollow.getTmpAuditPicFollowupRecs().size();i++) {
																if(i>=listTargetDate.size()-1) {
																	//picFollow.getTmpAuditPicFollowupRecs().remove(i);
																	listDeleted.add(picFollow.getTmpAuditPicFollowupRecs().get(i));
																}
															}
															listDeleted.forEach(e->picFollow.getTmpAuditPicFollowupRecs().remove(e));
														}
													}
													
													//insert or update TmpAuditPicFollowupRec
													for(int i=0;i<listTargetDate.size();i++) {
														Date targetDate = listTargetDate.get(i);
														TmpAuditPicFollowupRec tmpAuditPicFollowupRec =  null;
														Boolean flagAddRec = false;
														if(flagRec) {
															if(i<picFollow.getTmpAuditPicFollowupRecs().size() && picFollow.getTmpAuditPicFollowupRecs().get(i)!=null && picFollow.getTmpAuditPicFollowupRecs().get(i).getTargetDate()!=null) {
																tmpAuditPicFollowupRec = picFollow.getTmpAuditPicFollowupRecs().get(i);
															}else {
																tmpAuditPicFollowupRec =  new TmpAuditPicFollowupRec();
																flagAddRec = true;
															}
														}else {
															tmpAuditPicFollowupRec =  new TmpAuditPicFollowupRec();
															 flagAddRec = true;
														}
														
														tmpAuditPicFollowupRec.setTmpAuditPicFollowup(picFollow);
														tmpAuditPicFollowupRec.setTargetDate(targetDate);
														
														if(tmpAuditPicFollowupRec.getAuditPicFollowupRecId() != null)
															EntityUtil.setUpdateInfo(tmpAuditPicFollowupRec, facesUtil.retrieveUserLogin());
														else
															EntityUtil.setCreationInfo(tmpAuditPicFollowupRec, facesUtil.retrieveUserLogin());
														
														if(flagAddRec) {
															if(picFollow.getTmpAuditPicFollowupRecs() == null) {
																List<TmpAuditPicFollowupRec> tmpAuditPicFollowupRecList = new ArrayList<>();
																tmpAuditPicFollowupRecList.add(tmpAuditPicFollowupRec);
																picFollow.setTmpAuditPicFollowupRecs(tmpAuditPicFollowupRecList);
															}else {
																picFollow.getTmpAuditPicFollowupRecs().add(tmpAuditPicFollowupRec);
															}
														}
														
													}
													//insert or update TmpAuditPicFollowupRec
													
													//insert or update TmpAuditPicFollowupRecEmail
													for(int i=0;i<picFollow.getTmpAuditPicFollowupRecs().size();i++) {
														TmpAuditPicFollowupRec tmpAuditPicFollowupRec = picFollow.getTmpAuditPicFollowupRecs().get(i);
														Calendar calendar = Calendar.getInstance();
														Date targetDateTmp = tmpAuditPicFollowupRec.getTargetDate();
														int row = 0;
														Boolean flag = false;
														if(tmpAuditPicFollowupRec.getTmpAuditPicFollowupRecEmails()!=null && tmpAuditPicFollowupRec.getTmpAuditPicFollowupRecEmails().size()>0) {
															flag = true;
														}
														
														if(flag) {
															if(tmpAuditPicFollowupRec.getTmpAuditPicFollowupRecEmails().size() > tmpAudit.getCounterType().getDetails().size()) {
																List<Object> listDeleted = new ArrayList<>();
																for(int x=0;x<tmpAuditPicFollowupRec.getTmpAuditPicFollowupRecEmails().size();x++) {
																	if(x>=tmpAudit.getCounterType().getDetails().size()-1) {
																		listDeleted.add(tmpAuditPicFollowupRec.getTmpAuditPicFollowupRecEmails().get(x));
																	}
																}
																
																listDeleted.forEach(e-> tmpAuditPicFollowupRec.getTmpAuditPicFollowupRecEmails().remove(e));
															}
														}
														for (CounterTypeDtl dataCounterTypeDtl : tmpAudit.getCounterType().getDetails()) {
															
															Boolean flagLoop = false;
															if (dataCounterTypeDtl.getSlaType().equals("+")) {
																
																calendar.setTime(tmpAuditPicFollowupRec.getTargetDate());
																calendar.add(Calendar.DAY_OF_MONTH, dataCounterTypeDtl.getSla().intValue());
																targetDateTmp = calendar.getTime();
																
																while(!flagLoop) {
																	int day = calendar.get(Calendar.DAY_OF_WEEK);
																	
																	if (day == 1 || day == 7) {
																		// do nothing
																		calendar.add(Calendar.DAY_OF_MONTH, 1);
																		targetDateTmp = calendar.getTime();
																	} else {
																		
																			if (Boolean.TRUE.equals(holidayService.isAvailableDate(targetDateTmp))) {
																				flagLoop = true;
																			}else {
																				calendar.add(Calendar.DAY_OF_MONTH, 1);
																				targetDateTmp = calendar.getTime();
																			}
																		
																		
																	}
																}
																
																
															} else if (dataCounterTypeDtl.getSlaType().equals("-")) {
																
																calendar.setTime(targetDateTmp);
																calendar.add(Calendar.DAY_OF_MONTH, -dataCounterTypeDtl.getSla().intValue());
																targetDateTmp = calendar.getTime();
																
																while(!flagLoop) {
																int day = calendar.get(Calendar.DAY_OF_WEEK);
																
																if (day == 1 || day == 7) {
																	// do nothing
																	calendar.add(Calendar.DAY_OF_MONTH, -1);
																	targetDateTmp = calendar.getTime();
																} else {
																	
																		if (Boolean.TRUE.equals(holidayService.isAvailableDate(targetDateTmp))) {
																			flagLoop = true;
																		}else {
																			calendar.add(Calendar.DAY_OF_MONTH, -1);
																			targetDateTmp = calendar.getTime();
																		}
																	
																	
																}
																}
															}
															
															TmpAuditPicFollowupRecEmail tmpAuditPicFollowupRecEmail =  null;
															Boolean flagAddRec = false;
															if(flag) {
																if(row<tmpAuditPicFollowupRec.getTmpAuditPicFollowupRecEmails().size() && tmpAuditPicFollowupRec.getTmpAuditPicFollowupRecEmails().get(row)!=null && tmpAuditPicFollowupRec.getTmpAuditPicFollowupRecEmails().get(row).getAuditPicFollowupEmailRecId()!=null) {
																	tmpAuditPicFollowupRecEmail = tmpAuditPicFollowupRec.getTmpAuditPicFollowupRecEmails().get(row);
																}else {
																	tmpAuditPicFollowupRecEmail =  new TmpAuditPicFollowupRecEmail();
																	flagAddRec = true;
																}
															}else {
																tmpAuditPicFollowupRecEmail =  new TmpAuditPicFollowupRecEmail();
																 flagAddRec = true;
															}
															
															tmpAuditPicFollowupRecEmail.setTmpAuditPicFollowupRec(tmpAuditPicFollowupRec);
															
															tmpAuditPicFollowupRecEmail.setSla(dataCounterTypeDtl.getSla().longValue());
															tmpAuditPicFollowupRecEmail.setSlaType(dataCounterTypeDtl.getSlaType());
															tmpAuditPicFollowupRecEmail.setEmailDate(new Timestamp(targetDateTmp.getTime()));
															
															if(tmpAuditPicFollowupRecEmail.getAuditPicFollowupEmailRecId() != null)
																EntityUtil.setUpdateInfo(tmpAuditPicFollowupRecEmail, facesUtil.retrieveUserLogin());
															else
																EntityUtil.setCreationInfo(tmpAuditPicFollowupRecEmail, facesUtil.retrieveUserLogin());
															
															
															targetDateTmp = tmpAuditPicFollowupRec.getTargetDate();
															row++;
															
															if(flagAddRec) {
																if(tmpAuditPicFollowupRec.getTmpAuditPicFollowupRecEmails() == null) {
																	List<TmpAuditPicFollowupRecEmail> tmpAuditPicFollowupRecEmailList = new ArrayList<>();
																	tmpAuditPicFollowupRecEmailList.add(tmpAuditPicFollowupRecEmail);
																	tmpAuditPicFollowupRec.setTmpAuditPicFollowupRecEmails(tmpAuditPicFollowupRecEmailList);
																}else {
																	tmpAuditPicFollowupRec.getTmpAuditPicFollowupRecEmails().add(tmpAuditPicFollowupRecEmail);
																}
															}
														}
													}
													//insert or update TmpAuditPicFollowupRecEmail
													
												}
												
												Calendar calendar = Calendar.getInstance();
												Date targetDateTmp = picFollow.getTargetDate();
												Boolean flag = false;
												if(picFollow.getTmpAuditPicFollowupEmails()!=null && picFollow.getTmpAuditPicFollowupEmails().size()>0) {
													flag = true;
												}
												
												if(flag) {
													if(picFollow.getTmpAuditPicFollowupEmails().size() > tmpAudit.getCounterType().getDetails().size()) {
														List<Object> listDeleted = new ArrayList<>();
														
														for(int i=0;i<picFollow.getTmpAuditPicFollowupEmails().size();i++) {
															if(i>=tmpAudit.getCounterType().getDetails().size()-1) {
																//picFollow.getTmpAuditPicFollowupEmails().remove(i);
																listDeleted.add(picFollow.getTmpAuditPicFollowupEmails().get(i));
															}
														}
														listDeleted.forEach(e-> picFollow.getTmpAuditPicFollowupEmails().remove(e));
													}
												}
												
												int row = 0;
												for (CounterTypeDtl dataCounterTypeDtl : tmpAudit.getCounterType().getDetails()) {
													
													
													Boolean flagLoop = false;
													if (dataCounterTypeDtl.getSlaType().equals("+")) {
														
														calendar.setTime(targetDateTmp);
														calendar.add(Calendar.DAY_OF_MONTH, dataCounterTypeDtl.getSla().intValue());
														targetDateTmp = calendar.getTime();
														
														while(!flagLoop) {
															int day = calendar.get(Calendar.DAY_OF_WEEK);
															
															if (day == 1 || day == 7) {
																// do nothing
																calendar.add(Calendar.DAY_OF_MONTH, 1);
																targetDateTmp = calendar.getTime();
															} else {
																
																	if (Boolean.TRUE.equals(holidayService.isAvailableDate(targetDateTmp))) {
																		flagLoop = true;
																	}else {
																		calendar.add(Calendar.DAY_OF_MONTH, 1);
																		targetDateTmp = calendar.getTime();
																	}
																
																
															}
														}
														
														
													} else if (dataCounterTypeDtl.getSlaType().equals("-")) {
														
														calendar.setTime(targetDateTmp);
														calendar.add(Calendar.DAY_OF_MONTH, -dataCounterTypeDtl.getSla().intValue());
														targetDateTmp = calendar.getTime();
														
														while(!flagLoop) {
														int day = calendar.get(Calendar.DAY_OF_WEEK);
														
														if (day == 1 || day == 7) {
															// do nothing
															calendar.add(Calendar.DAY_OF_MONTH, -1);
															targetDateTmp = calendar.getTime();
														} else {
															
																if (Boolean.TRUE.equals(holidayService.isAvailableDate(targetDateTmp))) {
																	flagLoop = true;
																}else {
																	calendar.add(Calendar.DAY_OF_MONTH, -1);
																	targetDateTmp = calendar.getTime();
																}
															
															
														}
														}
													}
													
													TmpAuditPicFollowupEmail tmpAuditPicFollowupEmail =  null;
													Boolean flagAdd = false;
													if(flag) {
														if(row<picFollow.getTmpAuditPicFollowupEmails().size() && picFollow.getTmpAuditPicFollowupEmails().get(row)!=null && picFollow.getTmpAuditPicFollowupEmails().get(row).getSla()!=null) {
															tmpAuditPicFollowupEmail = picFollow.getTmpAuditPicFollowupEmails().get(row);
														}else {
															tmpAuditPicFollowupEmail =  new TmpAuditPicFollowupEmail();
															flagAdd = true;
														}
													}else {
														 tmpAuditPicFollowupEmail =  new TmpAuditPicFollowupEmail();
														 flagAdd = true;
													}
													tmpAuditPicFollowupEmail.setTmpAuditPicFollowup(picFollow);
													
													tmpAuditPicFollowupEmail.setSla(dataCounterTypeDtl.getSla().longValue());
													tmpAuditPicFollowupEmail.setSlaType(dataCounterTypeDtl.getSlaType());
													tmpAuditPicFollowupEmail.setEmailDate(new Timestamp(targetDateTmp.getTime()));
													
													if(tmpAuditPicFollowupEmail.getAuditPicFollowupEmailId() != null)
														EntityUtil.setUpdateInfo(tmpAuditPicFollowupEmail, facesUtil.retrieveUserLogin());
													else
														EntityUtil.setCreationInfo(tmpAuditPicFollowupEmail, facesUtil.retrieveUserLogin());
													
													
													targetDateTmp = picFollow.getTargetDate();
													row++;
													
													if(flagAdd) {
														if(picFollow.getTmpAuditPicFollowupEmails() == null) {
															List<TmpAuditPicFollowupEmail> tmpAuditPicFollowupEmailList = new ArrayList<>();
															tmpAuditPicFollowupEmailList.add(tmpAuditPicFollowupEmail);
															picFollow.setTmpAuditPicFollowupEmails(tmpAuditPicFollowupEmailList);
														}else {
															picFollow.getTmpAuditPicFollowupEmails().add(tmpAuditPicFollowupEmail);
														}
													}
												}
												
											}
										}
										// save email reminder
										
										
										
									});
								}
							});
						}
						
						
						
						if(tapf.getAuditCheckPointId() != null)
							EntityUtil.setUpdateInfo(tapf, facesUtil.retrieveUserLogin());
						else
							EntityUtil.setCreationInfo(tapf, facesUtil.retrieveUserLogin());
						
						tapf.setTmpAudit(tmpAudit);
					}
				}

				if (tmpAudit.getAuditId() != null) {

//					TmpAudit tmpAuditDb = tmpAuditService.findById(tmpAudit.getAuditId());
//					tmpAuditService.updateDataAlreadyExist(tmpAudit, tmpAuditDb, facesUtil.retrieveUserLogin());
					tmpAudit.setLastUpdateBy(facesUtil.retrieveUserLogin());
					tmpAudit.setLastUpdateDate(new Timestamp(new Date().getTime()));
					tmpAuditService.update(tmpAudit);
				} else {
					tmpAudit.setCreatedBy(facesUtil.retrieveUserLogin());
					tmpAudit.setCreationDate(new Timestamp(new Date().getTime()));
					tmpAudit.setDelId(new Long(0));
					tmpAudit.setEnabledFlag(Y);
					tmpAuditService.save(tmpAudit);
				}
				
				facesUtil.removeSessionAttribute(EMAIL_TEMPLATE_AUDIT);
				
				
				
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
				
				if(deletedAuditFindingsFiles!=null) {
					for(int i=0;i<deletedAuditFindingsFiles.size();i++) {
						UploadedFileWO uf = (UploadedFileWO) deletedAuditFindingsFiles.get(i);
						CallApiManager.deleteFile(uf.getFileId(), parameterDetailService, fileUtil);
					}
				}
				
				if(deletedAuditBankResponseFiles!=null) {
					for(int i=0;i<deletedAuditBankResponseFiles.size();i++) {
						UploadedFileWO uf = (UploadedFileWO) deletedAuditBankResponseFiles.get(i);
						CallApiManager.deleteFile(uf.getFileId(), parameterDetailService, fileUtil);
					}
				}
				
				if(deletedAuditBankCommitmentFiles!=null) {
					for(int i=0;i<deletedAuditBankCommitmentFiles.size();i++) {
						UploadedFileWO uf = (UploadedFileWO) deletedAuditBankCommitmentFiles.get(i);
						CallApiManager.deleteFile(uf.getFileId(), parameterDetailService, fileUtil);
					}
				}
				
				if(deletedAuditAttachment!=null) {
					for(int i=0;i<deletedAuditAttachment.size();i++) {
						UploadedFileWO uf = (UploadedFileWO) deletedAuditAttachment.get(i);
						CallApiManager.deleteFile(uf.getFileId(), parameterDetailService, fileUtil);
					}
				}

				facesUtil.redirect("/pages/tmpAudit/tmpAudit.faces");

			}

		} catch (Exception ex) {
			ex.printStackTrace();
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + ex.getMessage(), "");
		}

	}
	
	public void onTargetDateChange(int rowIdx) {
		//if(!tmpAudit.getTmpAuditPicFollowups().get(rowIdx).getTargetDate().equals(tmpAudit.getTmpAuditPicFollowups().get(rowIdx).getOldTargetDate()))
			//tmpAudit.getTmpAuditPicFollowups().get(rowIdx).setRescheduleReason(null);
	}
	
	public void deleteAuditFindingsAttachment(String fileId,int index) throws Exception {
		deletedAuditFindingsFiles = deletedAuditFindingsFiles!=null?deletedAuditFindingsFiles: new ArrayList<UploadedFileWO>();
		deletedAuditFindingsFiles.add(new UploadedFileWO(fileId,null,null, null));
		uploadedFilesAuditFindings.remove(uploadedFilesAuditFindings.get(index));
	}
	
	public void deleteAuditBankResponseAttachment(String fileId,int index) throws Exception {
		deletedAuditBankResponseFiles= deletedAuditBankResponseFiles!=null?deletedAuditBankResponseFiles: new ArrayList<UploadedFileWO>();
		deletedAuditBankResponseFiles.add(new UploadedFileWO(fileId,null,null, null));
		uploadedFilesAuditBankResponse.remove(uploadedFilesAuditBankResponse.get(index));
	}
	
	public void deleteAuditBankCommitmentAttachment(String fileId,int index) throws Exception {
		deletedAuditBankCommitmentFiles = deletedAuditBankCommitmentFiles!=null?deletedAuditBankCommitmentFiles: new ArrayList<UploadedFileWO>();
		deletedAuditBankCommitmentFiles.add(new UploadedFileWO(fileId,null,null, null));
		uploadedFilesAuditBankCommitment.remove(uploadedFilesAuditBankCommitment.get(index));
	}
	
	public void deleteAuditAttachment(String fileId,int index) throws Exception {
		deletedAuditAttachment = deletedAuditAttachment!=null?deletedAuditAttachment: new ArrayList<UploadedFileWO>();
		deletedAuditAttachment.add(new UploadedFileWO(fileId,null,null, null));
		uploadedFilesAuditBankAttachment.remove(uploadedFilesAuditBankAttachment.get(index));
	}

	public void downloadFile(String fileId, String fileName, byte[] content) throws Exception {
		CallApiManager.downloadFile(fileId, fileName, content, parameterDetailService);
	}

	public void cancel() {
		try {
			facesUtil.removeSessionAttribute(EMAIL_TEMPLATE_AUDIT);
			facesUtil.redirect("/pages/tmpAudit/tmpAudit.faces");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void sendEmail() {
		try {
			
			EmailTemplate emailTemplate = emailTemplateService.getEmailTemplateByEmailTemplateCode(Constants.EMAIL_APPROVAL);
			String emailSubject = emailTemplate.getEmailSubject()+" - Audit";
	
			String emailContent = ""; 
			String emailTo  = "";
			String emailCc = "";
		   
			String auditorTemp = "";
			ParameterDetail paramAuditorTemp = parameterDetailService.getParameterDetailByParamDtlCode(tmpAudit.getAuditor());
			
			auditorTemp = paramAuditorTemp.getNameIn() != null ? " - " + paramAuditorTemp.getNameIn() : "";
			
			
//			String auditNameTemp = tmpAudit.getAuditTopicName() != null ? " - " + tmpAudit.getAuditTopicName() : "";
		
			emailContent = emailTemplate.getEmailContent().replace(Constants.NOTIFICATION_TYPE_AND_DOC_NUM, "Audit"
					+ auditorTemp);
			
			ParameterDetail pdHostName = parameterDetailService.getParameterDetailByParamDtlCode(Constants.HOST_NAME_APPLICATION);
			String token = Constants.encryptString(tmpAudit.getAuditId().toString());
			String menuId = Constants.encryptString(Constants.MENU_ID_APPROVAL_REQUEST_AUDIT);
			String urlLink = pdHostName.getNameIn().concat("pages/dashboard/dashboard.faces?token="+token+"&menuId="+menuId);
			emailContent = emailContent.replaceAll("url_link", urlLink);
						
			ParameterDetail paramEmail = parameterDetailService.getParameterDetailByParamDtlCode(Constants.AUDIT_CHECKER);
			emailTo = paramEmail.getNameIn();
					
			final String subject = emailSubject;
			final String content = emailContent;
			final String to = emailTo;
			//final String to = "h3ndr407@gmail.com";
			final String cc = emailCc;
						
			CallApiManager.sendEmailAPI(to, cc, subject, content, "EMAIL_AUDIT", "true", parameterDetailService);
		    
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new CustomAPIException(ex.getMessage()); 
		}
		
	}

	public void onChangeAuditMaster() {
		MstAuditVO getSingleData = mstAuditService.getSingleDataMstAudit(tmpAudit.getMstAudit().getMstAuditId());
		
		try {
			if (getSingleData != null) {
				tmpAudit.setAuditor(getSingleData.getAuditorCode());
				tmpAudit.setAuditDateFrom(DateUtil.stringToDateFromDDMMMYYYY(getSingleData.getAuditDateFrom()));
				tmpAudit.setAuditDateTo(DateUtil.stringToDateFromDDMMMYYYY(getSingleData.getAuditDateTo()));
				tmpAudit.setScope(getSingleData.getScope());
			} else {
				tmpAudit.setAuditor(null);
				tmpAudit.setAuditDateFrom(null);
				tmpAudit.setAuditDateTo(null);
				tmpAudit.setScope(null);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		PrimeFaces.current().executeScript("initSelect2();");
	}
	
	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

	public String getNavigateSearch() {
		return navigateSearch;
	}

	public void setNavigateSearch(String navigateSearch) {
		this.navigateSearch = navigateSearch;
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

	public SimpleDateFormat getSdf() {
		return sdf;
	}

	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		TmpAuditEditBean.logger = logger;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public ParameterDetailService getParameterDetailService() {
		return parameterDetailService;
	}

	public void setParameterDetailService(ParameterDetailService parameterDetailService) {
		this.parameterDetailService = parameterDetailService;
	}

	public RegulationTrackRecord getSelectedRow() {
		return selectedRow;
	}

	public void setSelectedRow(RegulationTrackRecord selectedRow) {
		this.selectedRow = selectedRow;
	}

	public Integer getIndexDtl() {
		return indexDtl;
	}

	public void setIndexDtl(Integer indexDtl) {
		this.indexDtl = indexDtl;
	}

	public SelectorInfo getSelectorCompliance() {
		return selectorCompliance;
	}

	public void setSelectorCompliance(SelectorInfo selectorCompliance) {
		this.selectorCompliance = selectorCompliance;
	}

	public Integer getIndexDtlCompliance() {
		return indexDtlCompliance;
	}

	public void setIndexDtlCompliance(Integer indexDtlCompliance) {
		this.indexDtlCompliance = indexDtlCompliance;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public SelectorInfo getSelectorFollowup() {
		return selectorFollowup;
	}

	public void setSelectorFollowup(SelectorInfo selectorFollowup) {
		this.selectorFollowup = selectorFollowup;
	}

	public Integer getIndexDtlFollowup() {
		return indexDtlFollowup;
	}

	public void setIndexDtlFollowup(Integer indexDtlFollowup) {
		this.indexDtlFollowup = indexDtlFollowup;
	}

	public SelectorInfo getSelectorPic1() {
		return selectorPic1;
	}

	public void setSelectorPic1(SelectorInfo selectorPic1) {
		this.selectorPic1 = selectorPic1;
	}

	public SelectorInfo getSelectorPic2() {
		return selectorPic2;
	}

	public void setSelectorPic2(SelectorInfo selectorPic2) {
		this.selectorPic2 = selectorPic2;
	}

	public SelectorInfo getSelectorPic3() {
		return selectorPic3;
	}

	public void setSelectorPic3(SelectorInfo selectorPic3) {
		this.selectorPic3 = selectorPic3;
	}

	public List<SelectItem> getDivisions() {
		return divisions;
	}

	public void setDivisions(List<SelectItem> divisions) {
		this.divisions = divisions;
	}

	public CounterTypeService getCounterTypeService() {
		return counterTypeService;
	}

	public void setCounterTypeService(CounterTypeService counterTypeService) {
		this.counterTypeService = counterTypeService;
	}

	public List<SelectItem> getCounterTypes() {
		return counterTypes;
	}

	public void setCounterTypes(List<SelectItem> counterTypes) {
		this.counterTypes = counterTypes;
	}

	

	public List<StatusKonfirmasiVO> getTableStatus() {
		return tableStatus;
	}

	public void setTableStatus(List<StatusKonfirmasiVO> tableStatus) {
		this.tableStatus = tableStatus;
	}

	
	public TmpAudit getTmpAudit() {
		return tmpAudit;
	}

	public void setTmpAudit(TmpAudit tmpAudit) {
		this.tmpAudit = tmpAudit;
	}

	public TmpAuditCheckPointTableModel<TmpAuditCheckPoint> getTableModelFollowup() {
		return tableModelFollowup;
	}

	public void setTableModelFollowup(TmpAuditCheckPointTableModel<TmpAuditCheckPoint> tableModelFollowup) {
		this.tableModelFollowup = tableModelFollowup;
	}

	public Long getCounterTypeId() {
		return counterTypeId;
	}

	public void setCounterTypeId(Long counterTypeId) {
		this.counterTypeId = counterTypeId;
	}

	public List<SelectItem> getReminderStatusList() {
		return reminderStatusList;
	}

	public void setReminderStatusList(List<SelectItem> reminderStatusList) {
		this.reminderStatusList = reminderStatusList;
	}

	public List<TmpAuditApprovalVO> getTableApproval() {
		return tableApproval;
	}

	public void setTableApproval(List<TmpAuditApprovalVO> tableApproval) {
		this.tableApproval = tableApproval;
	}

	@Override
	public void itemSelected(String clientId, String widgetVar, Object selectedItem) {
		if (StringUtils.equals("picComplianceDialog", widgetVar)) {
			Object[] objects = (Object[]) selectedItem;
			//User user = userService.findById(((java.math.BigInteger) objects[0]).longValue());
			if (indexDtlCompliance == null) indexDtlCompliance = new Integer(0);
			User user = userService.findById(MathUtil.returnIdObjectToLong(objects[0]));
			tmpAudit.getTmpAuditPicCompliances().get(indexDtlCompliance).setUser(user);

			tableModelCompliance.setWrappedData(tmpAudit.getTmpAuditPicCompliances());
		}

		else if (StringUtils.equals("divisionDialog", widgetVar)) {
			Object[] objects = (Object[]) selectedItem;
			if (indexDtlFollowup == null) indexDtlFollowup = new Integer(0);

			tmpAudit.getTmpAuditCheckPoints().get(indexDtlFollowup).getTmpAuditPicFollowupBankCommitments().get(indexDtlBankCommitment).getTmpAuditPicFollowups().get(indexDtlFollowup)
					  .setDivisionId(MathUtil.returnIdObjectToLong(objects[0]));
			

			tableModelFollowup.setWrappedData(tmpAudit.getTmpAuditCheckPoints());
		}

		else if (StringUtils.equals("pic1Dialog", widgetVar)) {
			Object[] objects = (Object[]) selectedItem;
			//User user = userService.findById(((java.math.BigInteger) objects[0]).longValue());
			if (indexDtlCheckPoint == null) indexDtlCheckPoint = new Integer(0);
			User user = userService.findById(MathUtil.returnIdObjectToLong(objects[0]));
			tmpAudit.getTmpAuditCheckPoints().get(indexDtlCheckPoint).getTmpAuditPicFollowupBankCommitments().get(indexDtlBankCommitment).getTmpAuditPicFollowups().get(indexDtlFollowup).setUser1(user);
			/*socializationTmp.getSocializationPICFollowupTmps().get(indexDtlFollowup)
					.setDivisionId(user.getDivisionId());*/

			User user2 = userService.getUserByNik(user.getPukNik());
			if (user2 != null) {
				tmpAudit.getTmpAuditCheckPoints().get(indexDtlCheckPoint).getTmpAuditPicFollowupBankCommitments().get(indexDtlBankCommitment).getTmpAuditPicFollowups().get(indexDtlFollowup).setUser2(user2);

				User user3 = userService.getUserByNik(user2.getPukNik());
				if (user3 != null) {
					tmpAudit.getTmpAuditCheckPoints().get(indexDtlCheckPoint).getTmpAuditPicFollowupBankCommitments().get(indexDtlBankCommitment).getTmpAuditPicFollowups().get(indexDtlFollowup).setUser3(user3);
				}
			}

			tableModelFollowup.setWrappedData(tmpAudit.getTmpAuditCheckPoints());
			
			
			PrimeFaces.current().ajax()
			.update("form:listAuditCheckPoint:"+indexDtlCheckPoint+":listBankCommitment:"+indexDtlBankCommitment+":listPicFollowup:"+indexDtlFollowup+":panelPicFollowup");
		}

		else if (StringUtils.equals("pic2Dialog", widgetVar)) {
			Object[] objects = (Object[]) selectedItem;
			//User user = userService.findById(((java.math.BigInteger) objects[0]).longValue());
			if (indexDtlCheckPoint == null) indexDtlCheckPoint = new Integer(0);
			User user = userService.findById(MathUtil.returnIdObjectToLong(objects[0]));
			tmpAudit.getTmpAuditCheckPoints().get(indexDtlCheckPoint).getTmpAuditPicFollowupBankCommitments().get(indexDtlBankCommitment).getTmpAuditPicFollowups().get(indexDtlFollowup).setUser2(user);
			/*socializationTmp.getSocializationPICFollowupTmps().get(indexDtlFollowup)
					.setDivisionId(user.getDivisionId());*/
			User user3 = userService.getUserByNik(user.getPukNik());
			if (user3 != null) {
				tmpAudit.getTmpAuditCheckPoints().get(indexDtlCheckPoint).getTmpAuditPicFollowupBankCommitments().get(indexDtlBankCommitment).getTmpAuditPicFollowups().get(indexDtlFollowup).setUser3(user3);
			}

			tableModelFollowup.setWrappedData(tmpAudit.getTmpAuditCheckPoints());
			
			
			PrimeFaces.current().ajax()
			.update("form:listAuditCheckPoint:"+indexDtlCheckPoint+":listBankCommitment:"+indexDtlBankCommitment+":listPicFollowup:"+indexDtlFollowup+":panelPicFollowup");
		}

		else if (StringUtils.equals("pic3Dialog", widgetVar)) {
			Object[] objects = (Object[]) selectedItem;
			//User user = userService.findById(((java.math.BigInteger) objects[0]).longValue());
			if (indexDtlCheckPoint == null) indexDtlCheckPoint = new Integer(0);
			User user = userService.findById(MathUtil.returnIdObjectToLong(objects[0]));
			tmpAudit.getTmpAuditCheckPoints().get(indexDtlCheckPoint).getTmpAuditPicFollowupBankCommitments().get(indexDtlBankCommitment).getTmpAuditPicFollowups().get(indexDtlFollowup).setUser3(user);
			/*socializationTmp.getSocializationPICFollowupTmps().get(indexDtlFollowup)
					.setDivisionId(user.getDivisionId());*/

			tableModelFollowup.setWrappedData(tmpAudit.getTmpAuditCheckPoints());
			PrimeFaces.current().ajax()
			.update("form:listAuditCheckPoint:"+indexDtlCheckPoint+":listBankCommitment:"+indexDtlBankCommitment+":listPicFollowup:"+indexDtlFollowup+":panelPicFollowup");
		} else if (StringUtils.equals("emailCc1Dialog", widgetVar)) {
			Object[] objects = (Object[]) selectedItem;
			// User user1 = userService.findById(((BigInteger) objects[0]).longValue());
			User user1 = userService.findById(MathUtil.returnIdObjectToLong(objects[0]));
			if (indexDtlCheckPoint == null) indexDtlCheckPoint = new Integer(0);
			if (user1 != null) {
				tmpAudit.getTmpAuditCheckPoints().get(indexDtlCheckPoint).getTmpAuditPicFollowupBankCommitments().get(indexDtlBankCommitment).getTmpAuditPicFollowups().get(indexDtlFollowup).getTmpAuditPicFollowupSupportingUnits()
					.get(indexDtlSuppUnit).setEmailCc1(user1);
				tmpAudit.getTmpAuditCheckPoints().get(indexDtlCheckPoint).getTmpAuditPicFollowupBankCommitments().get(indexDtlBankCommitment).getTmpAuditPicFollowups().get(indexDtlFollowup).getTmpAuditPicFollowupSupportingUnits()
					.get(indexDtlSuppUnit).setEmailCcTemp1(user1.getNik() + "-" + user1.getName());
				
				User user2 = userService.getUserByNik(user1.getPukNik());
				if (user2 != null) {
					tmpAudit.getTmpAuditCheckPoints().get(indexDtlCheckPoint).getTmpAuditPicFollowupBankCommitments().get(indexDtlBankCommitment).getTmpAuditPicFollowups().get(indexDtlFollowup).getTmpAuditPicFollowupSupportingUnits()
						.get(indexDtlSuppUnit).setEmailCc2(user2);
					tmpAudit.getTmpAuditCheckPoints().get(indexDtlCheckPoint).getTmpAuditPicFollowupBankCommitments().get(indexDtlBankCommitment).getTmpAuditPicFollowups().get(indexDtlFollowup).getTmpAuditPicFollowupSupportingUnits()
					.get(indexDtlSuppUnit).setEmailCcTemp2(user2.getNik() + "-" + user2.getName());

					User user3 = userService.getUserByNik(user2.getPukNik());
					if (user3 != null) {
						tmpAudit.getTmpAuditCheckPoints().get(indexDtlCheckPoint).getTmpAuditPicFollowupBankCommitments().get(indexDtlBankCommitment).getTmpAuditPicFollowups().get(indexDtlFollowup).getTmpAuditPicFollowupSupportingUnits()
							.get(indexDtlSuppUnit).setEmailCc3(user3);
						tmpAudit.getTmpAuditCheckPoints().get(indexDtlCheckPoint).getTmpAuditPicFollowupBankCommitments().get(indexDtlBankCommitment).getTmpAuditPicFollowups().get(indexDtlFollowup).getTmpAuditPicFollowupSupportingUnits()
							.get(indexDtlSuppUnit).setEmailCcTemp3(user3.getNik() + "-" + user3.getName());
					}
				}
				

				PrimeFaces.current().ajax()
				.update("form:listAuditCheckPoint:"+indexDtlCheckPoint+":listBankCommitment:"+indexDtlBankCommitment+":listPicFollowup:"+indexDtlFollowup+":panelSupportingUnit");
			}
		}

		else if (StringUtils.equals("emailCc2Dialog", widgetVar)) {
			Object[] objects = (Object[]) selectedItem;
			
			User user2 = userService.findById(MathUtil.returnIdObjectToLong(objects[0]));
			if (indexDtlCheckPoint == null) indexDtlCheckPoint = new Integer(0);
			if (user2 != null) {
				tmpAudit.getTmpAuditCheckPoints().get(indexDtlCheckPoint).getTmpAuditPicFollowupBankCommitments().get(indexDtlBankCommitment).getTmpAuditPicFollowups().get(indexDtlFollowup).getTmpAuditPicFollowupSupportingUnits()
					.get(indexDtlSuppUnit).setEmailCc2(user2);
				tmpAudit.getTmpAuditCheckPoints().get(indexDtlCheckPoint).getTmpAuditPicFollowupBankCommitments().get(indexDtlBankCommitment).getTmpAuditPicFollowups().get(indexDtlFollowup).getTmpAuditPicFollowupSupportingUnits()
					.get(indexDtlSuppUnit).setEmailCcTemp2(user2.getNik() + "-" + user2.getName());
				/*
				 * tmpCorrespondence.getTmpCorrespondenceSupportingUnits().get(indexDtlCc)
				 * .setDivisionId(user2.getDivisionId());
				 */

				User user3 = userService.getUserByNik(user2.getPukNik());
				if (user3 != null) {
					tmpAudit.getTmpAuditCheckPoints().get(indexDtlCheckPoint).getTmpAuditPicFollowupBankCommitments().get(indexDtlBankCommitment).getTmpAuditPicFollowups().get(indexDtlFollowup).getTmpAuditPicFollowupSupportingUnits()
						.get(indexDtlSuppUnit).setEmailCc3(user3);
					tmpAudit.getTmpAuditCheckPoints().get(indexDtlCheckPoint).getTmpAuditPicFollowupBankCommitments().get(indexDtlBankCommitment).getTmpAuditPicFollowups().get(indexDtlFollowup).getTmpAuditPicFollowupSupportingUnits()
						.get(indexDtlSuppUnit).setEmailCcTemp3(user3.getNik() + "-" + user3.getName());
				}
				
				PrimeFaces.current().ajax()
				.update("form:listAuditCheckPoint:"+indexDtlCheckPoint+":listBankCommitment:"+indexDtlBankCommitment+":listPicFollowup:"+indexDtlFollowup+":panelSupportingUnit");
			}
		}

		else if (StringUtils.equals("emailCc3Dialog", widgetVar)) {
			Object[] objects = (Object[]) selectedItem;
			
			User user3 = userService.findById(MathUtil.returnIdObjectToLong(objects[0]));
			if (indexDtlFollowup == null) indexDtlFollowup = new Integer(0);
			if (user3 != null) {
				tmpAudit.getTmpAuditCheckPoints().get(indexDtlFollowup).getTmpAuditPicFollowupBankCommitments().get(indexDtlBankCommitment).getTmpAuditPicFollowups().get(indexDtlFollowup).getTmpAuditPicFollowupSupportingUnits()
					.get(indexDtlSuppUnit).setEmailCc3(user3);
				tmpAudit.getTmpAuditCheckPoints().get(indexDtlFollowup).getTmpAuditPicFollowupBankCommitments().get(indexDtlBankCommitment).getTmpAuditPicFollowups().get(indexDtlFollowup).getTmpAuditPicFollowupSupportingUnits()
					.get(indexDtlSuppUnit).setEmailCcTemp3(user3.getNik() + "-" + user3.getName());
				
				
				PrimeFaces.current().ajax()
				.update("form:listAuditCheckPoint:"+indexDtlCheckPoint+":listBankCommitment:"+indexDtlBankCommitment+":listPicFollowup:"+indexDtlFollowup+":panelSupportingUnit");
			}
		}
		
		PrimeFaces.current().executeScript("initSelect2();");
		//RequestContext.getCurrentInstance().execute("initSelect2();");
	}

	public Long getOldCounterTypeId() {
		return oldCounterTypeId;
	}

	public void setOldCounterTypeId(Long oldCounterTypeId) {
		this.oldCounterTypeId = oldCounterTypeId;
	}

	public Long getNewCounterTypeId() {
		return newCounterTypeId;
	}

	public void setNewCounterTypeId(Long newCounterTypeId) {
		this.newCounterTypeId = newCounterTypeId;
	}

	public Integer getLastSequenceOfCompliance() {
		return lastSequenceOfCompliance;
	}

	public void setLastSequenceOfCompliance(Integer lastSequenceOfCompliance) {
		this.lastSequenceOfCompliance = lastSequenceOfCompliance;
	}

	public Integer getLastSequenceOfFollowup() {
		return lastSequenceOfFollowup;
	}

	public void setLastSequenceOfFollowup(Integer lastSequenceOfFollowup) {
		this.lastSequenceOfFollowup = lastSequenceOfFollowup;
	}

	public Boolean getDisabledFollowUpStatus() {
		return disabledFollowUpStatus;
	}

	public void setDisabledFollowUpStatus(Boolean disabledFollowUpStatus) {
		this.disabledFollowUpStatus = disabledFollowUpStatus;
	}

	public List<SelectItem> getSelectAuditFollowUp() {
		return selectAuditFollowUp;
	}

	public void setSelectAuditFollowUp(List<SelectItem> selectAuditFollowUp) {
		this.selectAuditFollowUp = selectAuditFollowUp;
	}

	public List<SelectItem> getSelectAuditObject() {
		return selectAuditObject;
	}

	public void setSelectAuditObject(List<SelectItem> selectAuditObject) {
		this.selectAuditObject = selectAuditObject;
	}

	

	public List<SelectItem> getSelectAuditCategory() {
		return selectAuditCategory;
	}

	public void setSelectAuditCategory(List<SelectItem> selectAuditCategory) {
		this.selectAuditCategory = selectAuditCategory;
	}

	public List<UploadedFileWO> getUploadedFilesAuditFindings() {
		return uploadedFilesAuditFindings;
	}

	public void setUploadedFilesAuditFindings(List<UploadedFileWO> uploadedFilesAuditFindings) {
		this.uploadedFilesAuditFindings = uploadedFilesAuditFindings;
	}

	public FileUtil getFileUtil() {
		return fileUtil;
	}

	public void setFileUtil(FileUtil fileUtil) {
		this.fileUtil = fileUtil;
	}

	public List<UploadedFileWO> getUploadedFilesAuditBankResponse() {
		return uploadedFilesAuditBankResponse;
	}

	public void setUploadedFilesAuditBankResponse(List<UploadedFileWO> uploadedFilesAuditBankResponse) {
		this.uploadedFilesAuditBankResponse = uploadedFilesAuditBankResponse;
	}

	public List<UploadedFileWO> getUploadedFilesAuditBankCommitment() {
		return uploadedFilesAuditBankCommitment;
	}

	public void setUploadedFilesAuditBankCommitment(List<UploadedFileWO> uploadedFilesAuditBankCommitment) {
		this.uploadedFilesAuditBankCommitment = uploadedFilesAuditBankCommitment;
	}

	public TmpAuditService getTmpAuditService() {
		return tmpAuditService;
	}

	public void setTmpAuditService(TmpAuditService tmpAuditService) {
		this.tmpAuditService = tmpAuditService;
	}

	public TmpAuditPICComplianceTableModel<TmpAuditPicCompliance> getTableModelCompliance() {
		return tableModelCompliance;
	}

	public void setTableModelCompliance(
			TmpAuditPICComplianceTableModel<TmpAuditPicCompliance> tableModelCompliance) {
		this.tableModelCompliance = tableModelCompliance;
	}

	public TmpAuditPicCompliance[] getSelectedDataCompliance() {
		return selectedDataCompliance;
	}

	public void setSelectedDataCompliance(TmpAuditPicCompliance[] selectedDataCompliance) {
		this.selectedDataCompliance = selectedDataCompliance;
	}

	
	public TmpAuditPicFollowup[] getSelectedDataFollowup() {
		return selectedDataFollowup;
	}

	public void setSelectedDataFollowup(TmpAuditPicFollowup[] selectedDataFollowup) {
		this.selectedDataFollowup = selectedDataFollowup;
	}

	public List<SelectItem> getFollowUps() {
		return followUps;
	}

	public void setFollowUps(List<SelectItem> followUps) {
		this.followUps = followUps;
	}

	public TrcAuditPICFollowupService getTrcAuditPICFollowupService() {
		return trcAuditPICFollowupService;
	}

	public void setTrcAuditPICFollowupService(TrcAuditPICFollowupService trcAuditPICFollowupService) {
		this.trcAuditPICFollowupService = trcAuditPICFollowupService;
	}

	public List<UploadedFileWO> getDeletedAuditFindingsFiles() {
		return deletedAuditFindingsFiles;
	}

	public void setDeletedAuditFindingsFiles(List<UploadedFileWO> deletedAuditFindingsFiles) {
		this.deletedAuditFindingsFiles = deletedAuditFindingsFiles;
	}

	public List<UploadedFileWO> getDeletedAuditBankResponseFiles() {
		return deletedAuditBankResponseFiles;
	}

	public void setDeletedAuditBankResponseFiles(List<UploadedFileWO> deletedAuditBankResponseFiles) {
		this.deletedAuditBankResponseFiles = deletedAuditBankResponseFiles;
	}

	public List<UploadedFileWO> getDeletedAuditBankCommitmentFiles() {
		return deletedAuditBankCommitmentFiles;
	}

	public void setDeletedAuditBankCommitmentFiles(List<UploadedFileWO> deletedAuditBankCommitmentFiles) {
		this.deletedAuditBankCommitmentFiles = deletedAuditBankCommitmentFiles;
	}

	public EmailTemplateService getEmailTemplateService() {
		return emailTemplateService;
	}

	public void setEmailTemplateService(EmailTemplateService emailTemplateService) {
		this.emailTemplateService = emailTemplateService;
	}

	public List<SelectItem> getYesNo() {
		return yesNo;
	}

	public void setYesNo(List<SelectItem> yesNo) {
		this.yesNo = yesNo;
	}

	public List<UploadedFileWO> getUploadedFilesAuditBankAttachment() {
		return uploadedFilesAuditBankAttachment;
	}

	public void setUploadedFilesAuditBankAttachment(List<UploadedFileWO> uploadedFilesAuditBankAttachment) {
		this.uploadedFilesAuditBankAttachment = uploadedFilesAuditBankAttachment;
	}

	public List<UploadedFileWO> getDeletedAuditAttachment() {
		return deletedAuditAttachment;
	}

	public void setDeletedAuditAttachment(List<UploadedFileWO> deletedAuditAttachment) {
		this.deletedAuditAttachment = deletedAuditAttachment;
	}

	public Integer getIndexDtlSuppUnit() {
		return indexDtlSuppUnit;
	}

	public void setIndexDtlSuppUnit(Integer indexDtlSuppUnit) {
		this.indexDtlSuppUnit = indexDtlSuppUnit;
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

	public List<SelectItem> getTemplateAuditList() {
		return templateAuditList;
	}

	public void setTemplateAuditList(List<SelectItem> templateAuditList) {
		this.templateAuditList = templateAuditList;
	}

	public MstAuditService getMstAuditService() {
		return mstAuditService;
	}

	public void setMstAuditService(MstAuditService mstAuditService) {
		this.mstAuditService = mstAuditService;
	}

	public Boolean getDisableMstAudit() {
		return disableMstAudit;
	}

	public void setDisableMstAudit(Boolean disableMstAudit) {
		this.disableMstAudit = disableMstAudit;
	}

	public TmpAuditService getTmpAuditService2() {
		return tmpAuditService2;
	}

	public void setTmpAuditService2(TmpAuditService tmpAuditService2) {
		this.tmpAuditService2 = tmpAuditService2;
	}

	public TmpAuditApprovalService getTmpAuditApprovalService() {
		return tmpAuditApprovalService;
	}

	public void setTmpAuditApprovalService(TmpAuditApprovalService tmpAuditApprovalService) {
		this.tmpAuditApprovalService = tmpAuditApprovalService;
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


	public Integer getIndexDtlBankResponse() {
		return indexDtlBankResponse;
	}

	public void setIndexDtlBankResponse(Integer indexDtlBankResponse) {
		this.indexDtlBankResponse = indexDtlBankResponse;
	}


	public Integer getIndexDtlBankCommitment() {
		return indexDtlBankCommitment;
	}

	public void setIndexDtlBankCommitment(Integer indexDtlBankCommitment) {
		this.indexDtlBankCommitment = indexDtlBankCommitment;
	}

	public Integer getIndexDtlCheckPoint() {
		return indexDtlCheckPoint;
	}

	public void setIndexDtlCheckPoint(Integer indexDtlCheckPoint) {
		this.indexDtlCheckPoint = indexDtlCheckPoint;
	}

	public HolidayService getHolidayService() {
		return holidayService;
	}

	public void setHolidayService(HolidayService holidayService) {
		this.holidayService = holidayService;
	}

	public Integer getIndexDtlAuditFinding() {
		return indexDtlAuditFinding;
	}

	public void setIndexDtlAuditFinding(Integer indexDtlAuditFinding) {
		this.indexDtlAuditFinding = indexDtlAuditFinding;
	}
	
	
}