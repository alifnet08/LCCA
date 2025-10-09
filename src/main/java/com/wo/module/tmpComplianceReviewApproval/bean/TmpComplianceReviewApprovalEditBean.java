package com.wo.module.tmpComplianceReviewApproval.bean;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.model.SelectItem;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.CommonConstants;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.exception.CustomAPIException;
import com.wo.module.common.js.JsUtil;
import com.wo.module.common.model.UploadedFileWO;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.counterType.model.CounterTypeDtl;
import com.wo.module.counterType.service.CounterTypeService;
import com.wo.module.emailTemplate.model.EmailTemplate;
import com.wo.module.emailTemplate.service.EmailTemplateService;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.lov.bean.SelectorModel.SelectorInfo;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;
import com.wo.module.parameter.service.ParameterDetailService;
import com.wo.module.tmpComplianceReview.constant.TmpComplianceReviewConstants;
import com.wo.module.tmpComplianceReview.model.TmpComplianceReview;
import com.wo.module.tmpComplianceReview.model.TmpComplianceReviewDocument;
import com.wo.module.tmpComplianceReview.model.TmpComplianceReviewPicCompliance;
import com.wo.module.tmpComplianceReview.model.TmpComplianceReviewPicComplianceTableModel;
import com.wo.module.tmpComplianceReview.model.TmpComplianceReviewPicFollowup;
import com.wo.module.tmpComplianceReview.model.TmpComplianceReviewPicFollowupFindings;
import com.wo.module.tmpComplianceReview.model.TmpComplianceReviewPicFollowupPoints;
import com.wo.module.tmpComplianceReview.model.TmpComplianceReviewPicFollowupReview;
import com.wo.module.tmpComplianceReview.model.TmpComplianceReviewPicFollowupTableModel;
import com.wo.module.tmpComplianceReview.service.ComplianceReviewService;
import com.wo.module.tmpComplianceReview.service.TmpComplianceReviewService;
import com.wo.module.tmpComplianceReview.vo.ComplianceReviewApprovalVO;
import com.wo.module.tmpComplianceReview.vo.StatusConfirmationVO;
import com.wo.module.tmpComplianceReviewApproval.constant.TmpComplianceReviewApprovalConstants;
import com.wo.module.tmpComplianceReviewApproval.model.TmpComplianceReviewApproval;
import com.wo.module.tmpComplianceReviewApproval.service.ComplianceReviewApprovalService;
import com.wo.module.user.model.Division;
import com.wo.module.user.model.User;
import com.wo.module.user.service.UserService;

public class TmpComplianceReviewApprovalEditBean extends CommonBean implements Serializable {

	private static final long serialVersionUID = -7012141491831753560L;

	static Logger logger = Logger.getLogger(TmpComplianceReviewApprovalEditBean.class);

	private TmpComplianceReview tmpComplianceReview;

	private Boolean isViewOnly;

	private String actionMode;
	private String editedId;

	private Long counterTypeId;

	private Long reviewCategoryId;

	private List<SelectItem> reviewCategoryList;

	private List<SelectItem> divisions;

	private List<SelectItem> followUps;

	private List<SelectItem> counterTypes;

	private List<SelectItem> reminderStatusList;

	private SelectorInfo selectorCompliance;

	private SelectorInfo selectorFollowup;

	private SelectorInfo selectorPic1;

	private SelectorInfo selectorPic2;

	private SelectorInfo selectorPic3;

	private TmpComplianceReviewPicCompliance[] selectedDataCompliance;

	private TmpComplianceReviewPicFollowup[] selectedDataFollowup;

	private TmpComplianceReviewPicComplianceTableModel<TmpComplianceReviewPicCompliance> tableModelCompliance;

	private TmpComplianceReviewPicFollowupTableModel<TmpComplianceReviewPicFollowup> tableModelFollowupPoints;
	
	private TmpComplianceReviewPicFollowup[] selectedDataFollowupPoints;

	private List<StatusConfirmationVO> tableStatus;

	private List<ComplianceReviewApprovalVO> tableApproval;

	private Integer indexDtl;

	private Integer indexDtlCompliance;

	private Integer indexDtlFollowup;

	private ComplianceReviewApprovalService complianceReviewApprovalService;

	private ComplianceReviewService complianceReviewService;

	private TmpComplianceReviewService tmpComplianceReviewService;

	private UserService userService;

	private CounterTypeService counterTypeService;

	private EmailTemplateService emailTemplateService;

	public FacesUtil facesUtil;

	private String navigateSearch = TmpComplianceReviewConstants.NAVIGATE_SEARCH;

	private String note;

	SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");

	private String REMINDER_ACTIVE = ParameterDetail.PARAM_DET_CODE_REMINDER_ACTIVE;
	private String REMINDER_INACTIVE = ParameterDetail.PARAM_DET_CODE_REMINDER_INACTIVE;

	private Integer lastSequenceOfCompliance;
	private Integer lastSequenceOfFollowupPoints;
	private Integer lastSequenceOfFollowupAreaReviewPoints;
	private Integer lastSequenceOfFollowupFindingsPoints;
	private Integer lastSequenceOfFollowupPointsPoints;

	private List<UploadedFileWO> uploadedFilesDocument;
	private List<UploadedFileWO> uploadedFilesFollowupPoints;
	private List<UploadedFileWO> deletedFiles;

	private TmpComplianceReviewPicFollowupReview[] selectedTmpComplianceReviewPicFollowupReview;
	private TmpComplianceReviewPicFollowupFindings[] selectedTmpComplianceReviewPicFollowupFindings;
	private TmpComplianceReviewPicFollowupPoints[] selectedTmpComplianceReviewPicFollowupPoints;

	private String approvalStat;
	
	private List<SelectItem> approvalStatus;
	
	@PostConstruct
	public void init() {
		super.init();
		selectDivision();
		selectFollowup();
		selectCounterType();
		selectReminderStatus();
		selectReviewCategory();
		selectApprovalStatus();
		selectorCompliance = TmpComplianceReviewConstants.buildSelectorPICCompliance(facesUtil);
		selectorFollowup = TmpComplianceReviewConstants.buildSelectorDivision(facesUtil);
		selectorPic1 = TmpComplianceReviewConstants.buildSelectorPIC(facesUtil);
		selectorPic2 = TmpComplianceReviewConstants.buildSelectorPIC(facesUtil);
		selectorPic3 = TmpComplianceReviewConstants.buildSelectorPIC(facesUtil);

		tableApproval = new ArrayList<ComplianceReviewApprovalVO>();
		tableStatus = new ArrayList<StatusConfirmationVO>();
		checkNewOrEdit();
	}
	
	public void selectApprovalStatus() {
		approvalStatus = new ArrayList<SelectItem>();
		try {
			approvalStatus = parameterDetailService.getListLabelValue(ParameterHeader.PARAM_HEAD_CODE_APPROVAL_STATUS,
					false, true, facesUtil.retrieveDefaultLocale());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void selectReviewCategory() {
		reviewCategoryList = new ArrayList<SelectItem>();
		try {
			List<ParameterDetail> pd = parameterDetailService.getParameterDetailByParamCode("REVIEW_CATEGORY");
			for (int i = 0; i < pd.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((ParameterDetail) pd.get(i)).getName());
				si.setValue(((ParameterDetail) pd.get(i)).getParameterDtlCode());
				reviewCategoryList.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void selectReminderStatus() {
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

	public void selectFollowup() {
		followUps = new ArrayList<SelectItem>();
		SelectItem si = new SelectItem();
		si.setLabel("Yes");
		si.setValue("Y");
		followUps.add(si);

		SelectItem si2 = new SelectItem();
		si2.setLabel("No");
		si2.setValue("N");
		followUps.add(si2);
	}

	public void selectDivision() {
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
	}

	public void selectCounterType() {
		try {
			counterTypes = counterTypeService.getAllCounterTypeLabelValue();
		} catch (Exception e) {
			e.printStackTrace();
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
			JsUtil.hideTHeadFollowupPoints();
		}
	}

	private void handleNew() {
		try {

			tmpComplianceReview = new TmpComplianceReview();

			tmpComplianceReview.setReminderStatus(new ParameterDetail());
			tmpComplianceReview.getReminderStatus().setParameterDtlCode(REMINDER_ACTIVE);
			tmpComplianceReview.setReviewCategory(new ParameterDetail());
			tmpComplianceReview.getReviewCategory().setParameterDtlCode("");

			lastSequenceOfCompliance = 0;

			tableModelCompliance = new TmpComplianceReviewPicComplianceTableModel<TmpComplianceReviewPicCompliance>(
					tmpComplianceReview.getTmpComplianceReviewPicCompliances());

//			tableModelFollowup = new TmpComplianceReviewPicFollowupTableModel<TmpComplianceReviewPicFollowup>(
//					tmpComplianceReview.getTmpComplianceReviewPicFollowups());

			if (tmpComplianceReview.getTmpComplianceReviewPicCompliances() == null
					|| tmpComplianceReview.getTmpComplianceReviewPicCompliances().size() == 0) {
				tmpComplianceReview
						.setTmpComplianceReviewPicCompliances(new ArrayList<TmpComplianceReviewPicCompliance>());
				lastSequenceOfCompliance = 0;
			}

			TmpComplianceReviewPicCompliance rt = new TmpComplianceReviewPicCompliance();
			lastSequenceOfCompliance = lastSequenceOfCompliance + 1;
			rt.setSequence(lastSequenceOfCompliance);

			User user = (User) facesUtil.getSessionAttribute(Constants.SESSION_EMPLOYEE);
			rt.setUser(user);
			tmpComplianceReview.getTmpComplianceReviewPicCompliances().add(rt);
			tableModelCompliance.setWrappedData(tmpComplianceReview.getTmpComplianceReviewPicCompliances());
//			tableModelFollowup.setWrappedData(tmpComplianceReview.getTmpComplianceReviewPicFollowups());

			uploadedFilesDocument = new ArrayList<UploadedFileWO>();
			uploadedFilesFollowupPoints = new ArrayList<UploadedFileWO>();

			actionMode = Constants.ACTION_ADD;
			facesUtil.setSessionAttribute("token", null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void handleEdit(String editId) {
		String token = facesUtil.retrieveRequestParam("token");
		if (StringUtils.isNotEmpty(token)) {
			editId = Constants.decryptString(token);
		}
		facesUtil.setSessionAttribute("token", Constants.encryptString(editId));
		actionMode = Constants.ACTION_EDIT;
		Long idLong = Long.parseLong(editId);
		tmpComplianceReview = tmpComplianceReviewService.findById(idLong);
		if (tmpComplianceReview.getCounterType() != null) {
			counterTypeId = tmpComplianceReview.getCounterType().getCounterTypeId();
		}

		uploadedFilesDocument = new ArrayList<UploadedFileWO>();

		for (int i = 0; i < tmpComplianceReview.getTmpComplianceReviewDocuments().size(); i++) {
			TmpComplianceReviewDocument ra = tmpComplianceReview.getTmpComplianceReviewDocuments().get(i);
			UploadedFileWO uf = new UploadedFileWO();
			uf.setFileName(ra.getAttachmentFile());
			uf.setFileId(ra.getFileId());
			uf.setFileSize(ra.getFileSize());

			uploadedFilesDocument.add(uf);

		}

		uploadedFilesFollowupPoints = new ArrayList<UploadedFileWO>();

		tableModelCompliance = new TmpComplianceReviewPicComplianceTableModel<TmpComplianceReviewPicCompliance>(
				tmpComplianceReview.getTmpComplianceReviewPicCompliances());
		tableModelFollowupPoints = new TmpComplianceReviewPicFollowupTableModel<TmpComplianceReviewPicFollowup>(
				tmpComplianceReview.getTmpComplianceReviewPicFollowupPoints());

		tableApproval = complianceReviewService.getDataApprovalByComplianceReviewId(idLong);
		tableStatus = complianceReviewService.getDataConfirmStatusByComplianceReviewId(idLong);

	}

	public String replaceAll(String INPUT, String REGEX, String REPLACE) {
		Pattern p = Pattern.compile(REGEX);
		// get a matcher object
		Matcher m = p.matcher(INPUT);
		INPUT = m.replaceAll(REPLACE);
		return INPUT;
	}

	public void sendEmail() {
		try {

			EmailTemplate emailTemplate = emailTemplateService.getEmailTemplateByEmailTemplateCode(
					TmpComplianceReviewApprovalConstants.EMAIL_TEMPLATE_EMAIL_COMPLIANCE_ASSESSMENT);
			String emailSubject = replaceAll(emailTemplate.getEmailSubject(),
					TmpComplianceReviewApprovalConstants.EMAIL_TEMPLATE_EMAIL_COUNTER_TYPE, "NOTIFICATION");
			emailSubject = replaceAll(emailSubject,
					TmpComplianceReviewApprovalConstants.EMAIL_TEMPLATE_EMAIL_DOCUMENT_NO,
					tmpComplianceReview.getDocumentNo());
			emailSubject = emailSubject.replaceAll(TmpComplianceReviewApprovalConstants.EMAIL_TEMPLATE_EMAIL_COUNTER_TYPE,"");
			
			
			ParameterDetail pdHostName = parameterDetailService
					.getParameterDetailByParamDtlCode(ParameterDetail.PARAM_DET_CODE_HOST_NAME_APPLICATION);
			if (tmpComplianceReview.getReminderStatus() != null
				&& tmpComplianceReview.getReminderStatus().getParameterDtlCode().equals(ParameterDetail.PARAM_DET_CODE_REMINDER_ACTIVE) && 
				tmpComplianceReview.getTmpComplianceReviewPicFollowupPoints() != null && tmpComplianceReview.getTmpComplianceReviewPicFollowupPoints().size() > 0) {
				for (int i = 0; i < tmpComplianceReview.getTmpComplianceReviewPicFollowupPoints().size(); i++) {
					buildAndSendEmail(emailTemplate, emailSubject, pdHostName, i);
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new CustomAPIException(ex.getMessage());
		}
	}

	private void buildAndSendEmail(EmailTemplate emailTemplate, String emailSubject, ParameterDetail pdHostName, int followupIdx)
			throws Exception {
		String emailContent = "";
		String emailTo = "";
		String emailCc1 = "";
		String emailCc2 = "";
		String emailCc = "";
		String emailCcCompliance = "";
		TmpComplianceReviewPicFollowup tmp = tmpComplianceReview.getTmpComplianceReviewPicFollowupPoints()
				.get(followupIdx);
		String menuId = "";
		String urlLink = "";
		String token = "";
		if (tmpComplianceReview.getFollowUp() != null && tmpComplianceReview.getFollowUp().equals(CommonConstants.Y)) {
			token = Constants.encryptString(tmp.getComplianceReviewPicFollowupId().toString());
			menuId = Constants.encryptString(Constants.MENU_ID_FOLLOWUP_CONFIRMATION_COMPLIANCE_REVIEW);
			urlLink = pdHostName.getNameIn()
					.concat("pages/trcComplianceReview/trcComplianceReviewEdit.faces?token=" + token
							+ "&menuId=" + menuId);
		} else if (tmpComplianceReview.getFollowUp() != null && tmpComplianceReview.getFollowUp().equals(CommonConstants.N)) {
			menuId = Constants.encryptString(Constants.MENU_ID_COMPLIANCE_REVIEW_VIEW);
			token = Constants.encryptString(tmpComplianceReview.getComplianceReviewId().toString());
			urlLink = pdHostName.getNameIn()
					.concat("pages/trcComplianceReviewView/trcComplianceReviewViewEdit.faces?token=" + token
							+ "&menuId=" + menuId);
		}
		emailContent = emailTemplate.getEmailContent().replaceAll(
				TmpComplianceReviewApprovalConstants.EMAIL_TEMPLATE_EMAIL_TARGET_DATE,
				tmp.getTargetDate() != null ? sdf.format(tmp.getTargetDate()) : "");
		emailContent = emailContent.replaceAll(
				TmpComplianceReviewApprovalConstants.EMAIL_TEMPLATE_EMAIL_DOCUMENT_NO,
				tmpComplianceReview.getDocumentNo());
		emailContent = emailContent.replaceAll(
				TmpComplianceReviewApprovalConstants.EMAIL_TEMPLATE_EMAIL_DOCUMENT_DATE,
				tmpComplianceReview != null ? sdf.format(tmpComplianceReview.getDocumentDate()) : "");
		emailContent = emailContent.replaceAll(
				TmpComplianceReviewApprovalConstants.EMAIL_TEMPLATE_EMAIL_PERIHAL_IN,
				tmpComplianceReview.getPerihalIn() != null ? tmpComplianceReview.getPerihalIn() : "");
		emailContent = emailContent.replaceAll(
				TmpComplianceReviewApprovalConstants.EMAIL_TEMPLATE_EMAIL_PERIHAL_EN,
				tmpComplianceReview.getPerihalEn() != null ? tmpComplianceReview.getPerihalEn() : "");
		emailContent = emailContent
				.replaceAll(TmpComplianceReviewApprovalConstants.EMAIL_TEMPLATE_EMAIL_URL_LINK, urlLink);
		
		if(tmp.getTmpComplianceReviewPicFollowupReviews() != null && !tmp.getTmpComplianceReviewPicFollowupReviews().isEmpty()) {
			String areaReview = "<ul>";
			for(TmpComplianceReviewPicFollowupReview crpfr : tmp.getTmpComplianceReviewPicFollowupReviews())
				areaReview += "<li>" + crpfr.getAreaReview() + "</li>";
			areaReview += "</ul>";
			emailContent = emailContent.replaceAll(TmpComplianceReviewApprovalConstants.EMAIL_TEMPLATE_EMAIL_AREA_REVIEW_POIN, areaReview);
		} else {
			emailContent = emailContent.replaceAll(TmpComplianceReviewApprovalConstants.EMAIL_TEMPLATE_EMAIL_AREA_REVIEW_POIN, "NA");
		}
		
		if(tmp.getTmpComplianceReviewPicFollowupFindings() != null && !tmp.getTmpComplianceReviewPicFollowupFindings().isEmpty()) {
			String temuan = "<ul>";
			for(TmpComplianceReviewPicFollowupFindings crpff : tmp.getTmpComplianceReviewPicFollowupFindings())
				temuan += "<li>" + crpff.getFindings() + "</li>";
			temuan += "</ul>";
			emailContent = emailContent.replaceAll(TmpComplianceReviewApprovalConstants.EMAIL_TEMPLATE_EMAIL_TEMUAN_POIN, temuan);
		} else {
			emailContent = emailContent.replaceAll(TmpComplianceReviewApprovalConstants.EMAIL_TEMPLATE_EMAIL_TEMUAN_POIN, "NA");
		}
		
		if(tmp.getTmpComplianceReviewPicFollowupPoints() != null && !tmp.getTmpComplianceReviewPicFollowupPoints().isEmpty()) {
			String followup = "<ul>";
			for(TmpComplianceReviewPicFollowupPoints crpfp : tmp.getTmpComplianceReviewPicFollowupPoints())
				followup += "<li>" + crpfp.getFollowupPoints() + "</li>";
			followup += "</ul>";
			emailContent = emailContent.replaceAll(TmpComplianceReviewApprovalConstants.EMAIL_TEMPLATE_EMAIL_CATATAN_POIN, followup);
		} else {
			emailContent = emailContent.replaceAll(TmpComplianceReviewApprovalConstants.EMAIL_TEMPLATE_EMAIL_CATATAN_POIN, "NA");
		}
		
		emailCc = "";
		emailCcCompliance = "";
		// for(int
		// x=0;x<socializationTmp.getCounterType().getDetails().size();x++)
		// {
		if (tmpComplianceReview != null) {
			if (tmpComplianceReview.getTmpComplianceReviewPicCompliances() != null && tmpComplianceReview.getTmpComplianceReviewPicCompliances().size() > 0) {
				for (TmpComplianceReviewPicCompliance tcrpc : tmpComplianceReview.getTmpComplianceReviewPicCompliances()) {
					User ue = null;
					if (tcrpc.getUser() != null) {
						ue = userService.findById(tcrpc.getUser().getUserId());
						if (ue != null && StringUtils.isNotBlank(ue.getEmail())) {
							emailCcCompliance = StringUtils.isNotEmpty(emailCcCompliance)?emailCcCompliance.concat(",").concat(ue.getEmail()):emailCcCompliance.concat(ue.getEmail());
						}
					}
				}
			}
		}
		
		if (tmpComplianceReview != null) {
			
			if (tmpComplianceReview.getCounterType() != null
				&& tmpComplianceReview.getCounterType().getDetails() != null
				&& tmpComplianceReview.getCounterType().getDetails().size() > 0) {
				CounterTypeDtl cd = tmpComplianceReview.getCounterType().getDetails().get(0);
				emailTo = cd.getEmailTo();
				emailCc1 = cd.getEmailCc1();
				emailCc2 = cd.getEmailCc2();

				if (emailTo.equals(Constants.REMINDER_PIC1)) {
					emailTo = tmp.getUser1() != null ? tmp.getUser1().getEmail() : "";
				} else if (emailTo.equals(Constants.REMINDER_PIC2)) {
					emailTo = tmp.getUser2() != null ? tmp.getUser2().getEmail() : "";
				} else if (emailTo.equals(Constants.REMINDER_PIC3)) {
					emailTo = tmp.getUser3() != null ? tmp.getUser3().getEmail() : "";
				}

				if (emailCc1 != null && emailCc1.equals(Constants.REMINDER_PIC1)) {
					emailCc1 = tmp.getUser1() != null ? tmp.getUser1().getEmail() : "";
				} else if (emailCc1 != null && emailCc1.equals(Constants.REMINDER_PIC2)) {
					emailCc1 = tmp.getUser2() != null ? tmp.getUser2().getEmail() : "";
				} else if (emailCc1 != null && emailCc1.equals(Constants.REMINDER_PIC3)) {
					emailCc1 = tmp.getUser3() != null ? tmp.getUser3().getEmail() : "";
				}

				if (emailCc2 != null && emailCc2.equals(Constants.REMINDER_PIC1)) {
					emailCc2 = tmp.getUser1() != null ? tmp.getUser1().getEmail() : "";
				} else if (emailCc2 != null && emailCc2.equals(Constants.REMINDER_PIC2)) {
					emailCc2 = tmp.getUser2() != null ? tmp.getUser2().getEmail() : "";
				} else if (emailCc2 != null && emailCc2.equals(Constants.REMINDER_PIC3)) {
					emailCc2 = tmp.getUser3() != null ? tmp.getUser3().getEmail() : "";
				}

				
			} else {
				emailTo = tmp.getUser1() != null ? tmp.getUser1().getEmail() : "";
				emailCc1 = tmp.getUser2() != null ? tmp.getUser2().getEmail() : "";
				emailCc2 = tmp.getUser3() != null ? tmp.getUser3().getEmail() : "";
			}
			
			if (StringUtils.isNotEmpty(emailCc1)) {
				emailCc = emailCc.concat(emailCc1);
			}
			if (StringUtils.isNotEmpty(emailCc2)) {
				emailCc = StringUtils.isNotEmpty(emailCc) ? emailCc.concat(",").concat(emailCc2)
						: emailCc.concat(emailCc2);
			}
			if (StringUtils.isNotEmpty(emailCcCompliance)) {
				emailCc = StringUtils.isNotEmpty(emailCc)?emailCc.concat(",").concat(emailCcCompliance)
						: emailCc.concat(emailCcCompliance);
			}
			
//						ExecutorService emailExecutor = Executors.newCachedThreadPool();

			final String subject = emailSubject;
			final String content = emailContent;
			final String to = emailTo;
			// final String to = "h3ndr407@gmail.com";
			final String cc = emailCc;
			// System.out.println(urlLink);
			CallApiManager.sendEmailAPI(to, cc, subject, content, "EMAIL_COMPLIANCE_ASSESSMENT", "true",
					parameterDetailService);
		}
	}
	
	public void save() {
		if (StringUtils.isBlank(getApprovalStat())) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("textApprovalStatus") + " "
					+ facesUtil.retrieveMessage("validateRequired"));
		} else if (ParameterDetail.PARAM_DET_CODE_STATUS_REVISE.equals(getApprovalStat())) {
			revise();
		} else if (ParameterDetail.PARAM_DET_CODE_STATUS_APPROVED.equals(getApprovalStat())) {
			approve();
		}
	}

	public void approve() {
		try {
//
//			if (tmpComplianceReview.getTmpComplianceReviewPicFollowups() == null
//					|| tmpComplianceReview.getTmpComplianceReviewPicFollowups().size() == 0) {
//				tmpComplianceReview.setTmpComplianceReviewPicFollowups(new ArrayList<TmpComplianceReviewPicFollowup>());
//			}

			TmpComplianceReviewApproval dtl = new TmpComplianceReviewApproval();
			dtl.setTmpComplianceReview(tmpComplianceReview);
			dtl.setApprovalDate(new Date());
			dtl.setApprovalNote(note);
			dtl.setApprovalStatus("STATUS_APPROVED");
			dtl.setUser(userService.getUserByNik(facesUtil.retrieveUserLogin()));
			if (dtl.getCreatedBy() == null) {
				dtl.setCreatedBy(facesUtil.retrieveUserLogin());
				dtl.setCreationDate(new Timestamp(new Date().getTime()));
			}

			dtl.setDelId(new Long(0));
			dtl.setEnabledFlag(Constants.CONSTANT_YES);

			List<TmpComplianceReviewApproval> tmpComplianceReviewApprovals = new ArrayList<TmpComplianceReviewApproval>();
			tmpComplianceReviewApprovals.add(dtl);

			if (tmpComplianceReview.getTmpComplianceReviewApprovals() == null) {
				tmpComplianceReview.setTmpComplianceReviewApprovals(tmpComplianceReviewApprovals);
			} else {
				tmpComplianceReview.getTmpComplianceReviewApprovals().add(dtl);
			}

			if (tmpComplianceReview.getComplianceReviewId() != null) {
				tmpComplianceReview.setStatus(parameterDetailService
						.getParameterDetailByParamDtlCode(ParameterDetail.PARAM_DET_CODE_DATA_ACTIVE));
				tmpComplianceReview.setLastUpdateBy(facesUtil.retrieveUserLogin());
				tmpComplianceReview.setLastUpdateDate(new Timestamp(new Date().getTime()));
				tmpComplianceReview.setDelId(new Long(0));
				tmpComplianceReview.setEnabledFlag(Constants.CONSTANT_YES);
				tmpComplianceReviewService.update(tmpComplianceReview);
				
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
			}

			facesUtil.redirect("/pages/tmpComplianceReviewApproval/tmpComplianceReviewApproval.faces");

		} catch (Exception ex) {
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + ex.getMessage(), "");
			ex.printStackTrace();
		}
	}

	public void revise() {
		try {

			if (StringUtils.isEmpty(note)) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpComplianceReviewApprovalNote") + " "
						+ facesUtil.retrieveMessage("validateRequired"));
			} else {
				if (tmpComplianceReview.getTmpComplianceReviewApprovals() == null
						|| tmpComplianceReview.getTmpComplianceReviewApprovals().size() == 0) {
					tmpComplianceReview.setTmpComplianceReviewApprovals(new ArrayList<TmpComplianceReviewApproval>());
				}

				TmpComplianceReviewApproval dtl = new TmpComplianceReviewApproval();
				dtl.setTmpComplianceReview(tmpComplianceReview);
				dtl.setApprovalDate(new Date());
				dtl.setApprovalNote(note);
				dtl.setApprovalStatus("STATUS_REVISE");
				dtl.setUser(userService.getUserByNik(facesUtil.retrieveUserLogin()));
				if (dtl.getCreatedBy() == null) {
					dtl.setCreatedBy(facesUtil.retrieveUserLogin());
					dtl.setCreationDate(new Timestamp(new Date().getTime()));
				}

				dtl.setDelId(new Long(0));
				dtl.setEnabledFlag(Constants.CONSTANT_YES);

				List<TmpComplianceReviewApproval> tmpComplianceReviewApprovals = new ArrayList<TmpComplianceReviewApproval>();
				tmpComplianceReviewApprovals.add(dtl);

				if (tmpComplianceReview.getTmpComplianceReviewApprovals() == null) {
					tmpComplianceReview.setTmpComplianceReviewApprovals(tmpComplianceReviewApprovals);
				} else {
					tmpComplianceReview.getTmpComplianceReviewApprovals().add(dtl);
				}

				if (tmpComplianceReview.getComplianceReviewId() != null) {
					tmpComplianceReview.setStatus(parameterDetailService
							.getParameterDetailByParamDtlCode(ParameterDetail.PARAM_DET_CODE_DATA_REVISE));
					tmpComplianceReview.setLastUpdateBy(facesUtil.retrieveUserLogin());
					tmpComplianceReview.setLastUpdateDate(new Timestamp(new Date().getTime()));
					tmpComplianceReview.setDelId(new Long(0));
					tmpComplianceReview.setEnabledFlag(Constants.CONSTANT_YES);
					tmpComplianceReviewService.update(tmpComplianceReview);
				}

				facesUtil.redirect("/pages/tmpComplianceReviewApproval/tmpComplianceReviewApproval.faces");
			}
		} catch (Exception ex) {
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + ex.getMessage(), "");
			ex.printStackTrace();
		}
	}

	public void downloadFile(String fileId, String fileName, byte[] content) throws Exception {
		CallApiManager.downloadFile(fileId, fileName, content, parameterDetailService);
	}

	public void cancel() {
		try {
			facesUtil.redirect("/pages/tmpComplianceReviewApproval/tmpComplianceReviewApproval.faces");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		TmpComplianceReviewApprovalEditBean.logger = logger;
	}

	public TmpComplianceReview getTmpComplianceReview() {
		return tmpComplianceReview;
	}

	public void setTmpComplianceReview(TmpComplianceReview tmpComplianceReview) {
		this.tmpComplianceReview = tmpComplianceReview;
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

	public Long getCounterTypeId() {
		return counterTypeId;
	}

	public void setCounterTypeId(Long counterTypeId) {
		this.counterTypeId = counterTypeId;
	}

	public List<SelectItem> getDivisions() {
		return divisions;
	}

	public void setDivisions(List<SelectItem> divisions) {
		this.divisions = divisions;
	}

	public List<SelectItem> getFollowUps() {
		return followUps;
	}

	public void setFollowUps(List<SelectItem> followUps) {
		this.followUps = followUps;
	}

	public List<SelectItem> getCounterTypes() {
		return counterTypes;
	}

	public void setCounterTypes(List<SelectItem> counterTypes) {
		this.counterTypes = counterTypes;
	}

	public List<SelectItem> getReminderStatusList() {
		return reminderStatusList;
	}

	public void setReminderStatusList(List<SelectItem> reminderStatusList) {
		this.reminderStatusList = reminderStatusList;
	}

	public SelectorInfo getSelectorCompliance() {
		return selectorCompliance;
	}

	public void setSelectorCompliance(SelectorInfo selectorCompliance) {
		this.selectorCompliance = selectorCompliance;
	}

	public SelectorInfo getSelectorFollowup() {
		return selectorFollowup;
	}

	public void setSelectorFollowup(SelectorInfo selectorFollowup) {
		this.selectorFollowup = selectorFollowup;
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

	public TmpComplianceReviewPicCompliance[] getSelectedDataCompliance() {
		return selectedDataCompliance;
	}

	public void setSelectedDataCompliance(TmpComplianceReviewPicCompliance[] selectedDataCompliance) {
		this.selectedDataCompliance = selectedDataCompliance;
	}

	public TmpComplianceReviewPicFollowup[] getSelectedDataFollowup() {
		return selectedDataFollowup;
	}

	public void setSelectedDataFollowup(TmpComplianceReviewPicFollowup[] selectedDataFollowup) {
		this.selectedDataFollowup = selectedDataFollowup;
	}

	public TmpComplianceReviewPicComplianceTableModel<TmpComplianceReviewPicCompliance> getTableModelCompliance() {
		return tableModelCompliance;
	}

	public void setTableModelCompliance(
			TmpComplianceReviewPicComplianceTableModel<TmpComplianceReviewPicCompliance> tableModelCompliance) {
		this.tableModelCompliance = tableModelCompliance;
	}

	public List<StatusConfirmationVO> getTableStatus() {
		return tableStatus;
	}

	public void setTableStatus(List<StatusConfirmationVO> tableStatus) {
		this.tableStatus = tableStatus;
	}

	public List<ComplianceReviewApprovalVO> getTableApproval() {
		return tableApproval;
	}

	public void setTableApproval(List<ComplianceReviewApprovalVO> tableApproval) {
		this.tableApproval = tableApproval;
	}

	public Integer getIndexDtl() {
		return indexDtl;
	}

	public void setIndexDtl(Integer indexDtl) {
		this.indexDtl = indexDtl;
	}

	public Integer getIndexDtlCompliance() {
		return indexDtlCompliance;
	}

	public void setIndexDtlCompliance(Integer indexDtlCompliance) {
		this.indexDtlCompliance = indexDtlCompliance;
	}

	public Integer getIndexDtlFollowup() {
		return indexDtlFollowup;
	}

	public void setIndexDtlFollowup(Integer indexDtlFollowup) {
		this.indexDtlFollowup = indexDtlFollowup;
	}

	public ComplianceReviewApprovalService getComplianceReviewApprovalService() {
		return complianceReviewApprovalService;
	}

	public void setComplianceReviewApprovalService(ComplianceReviewApprovalService complianceReviewApprovalService) {
		this.complianceReviewApprovalService = complianceReviewApprovalService;
	}

	public ComplianceReviewService getComplianceReviewService() {
		return complianceReviewService;
	}

	public void setComplianceReviewService(ComplianceReviewService complianceReviewService) {
		this.complianceReviewService = complianceReviewService;
	}

	public TmpComplianceReviewService getTmpComplianceReviewService() {
		return tmpComplianceReviewService;
	}

	public void setTmpComplianceReviewService(TmpComplianceReviewService tmpComplianceReviewService) {
		this.tmpComplianceReviewService = tmpComplianceReviewService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public CounterTypeService getCounterTypeService() {
		return counterTypeService;
	}

	public void setCounterTypeService(CounterTypeService counterTypeService) {
		this.counterTypeService = counterTypeService;
	}

	public EmailTemplateService getEmailTemplateService() {
		return emailTemplateService;
	}

	public void setEmailTemplateService(EmailTemplateService emailTemplateService) {
		this.emailTemplateService = emailTemplateService;
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

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public SimpleDateFormat getSdf() {
		return sdf;
	}

	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
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

	public Integer getLastSequenceOfCompliance() {
		return lastSequenceOfCompliance;
	}

	public void setLastSequenceOfCompliance(Integer lastSequenceOfCompliance) {
		this.lastSequenceOfCompliance = lastSequenceOfCompliance;
	}

	public List<UploadedFileWO> getUploadedFilesDocument() {
		return uploadedFilesDocument;
	}

	public void setUploadedFilesDocument(List<UploadedFileWO> uploadedFilesDocument) {
		this.uploadedFilesDocument = uploadedFilesDocument;
	}

	public List<UploadedFileWO> getUploadedFilesFollowupPoints() {
		return uploadedFilesFollowupPoints;
	}

	public void setUploadedFilesFollowupPoints(List<UploadedFileWO> uploadedFilesFollowupPoints) {
		this.uploadedFilesFollowupPoints = uploadedFilesFollowupPoints;
	}

	public List<UploadedFileWO> getDeletedFiles() {
		return deletedFiles;
	}

	public void setDeletedFiles(List<UploadedFileWO> deletedFiles) {
		this.deletedFiles = deletedFiles;
	}

	public List<SelectItem> getReviewCategoryList() {
		return reviewCategoryList;
	}

	public void setReviewCategoryList(List<SelectItem> reviewCategoryList) {
		this.reviewCategoryList = reviewCategoryList;
	}

	public Long getReviewCategoryId() {
		return reviewCategoryId;
	}

	public void setReviewCategoryId(Long reviewCategoryId) {
		this.reviewCategoryId = reviewCategoryId;
	}

	public TmpComplianceReviewPicFollowupTableModel<TmpComplianceReviewPicFollowup> getTableModelFollowupPoints() {
		return tableModelFollowupPoints;
	}

	public void setTableModelFollowupPoints(
			TmpComplianceReviewPicFollowupTableModel<TmpComplianceReviewPicFollowup> tableModelFollowupPoints) {
		this.tableModelFollowupPoints = tableModelFollowupPoints;
	}

	public TmpComplianceReviewPicFollowup[] getSelectedDataFollowupPoints() {
		return selectedDataFollowupPoints;
	}

	public void setSelectedDataFollowupPoints(TmpComplianceReviewPicFollowup[] selectedDataFollowupPoints) {
		this.selectedDataFollowupPoints = selectedDataFollowupPoints;
	}

	public Integer getLastSequenceOfFollowupPoints() {
		return lastSequenceOfFollowupPoints;
	}

	public void setLastSequenceOfFollowupPoints(Integer lastSequenceOfFollowupPoints) {
		this.lastSequenceOfFollowupPoints = lastSequenceOfFollowupPoints;
	}

	public Integer getLastSequenceOfFollowupAreaReviewPoints() {
		return lastSequenceOfFollowupAreaReviewPoints;
	}

	public void setLastSequenceOfFollowupAreaReviewPoints(Integer lastSequenceOfFollowupAreaReviewPoints) {
		this.lastSequenceOfFollowupAreaReviewPoints = lastSequenceOfFollowupAreaReviewPoints;
	}

	public Integer getLastSequenceOfFollowupFindingsPoints() {
		return lastSequenceOfFollowupFindingsPoints;
	}

	public void setLastSequenceOfFollowupFindingsPoints(Integer lastSequenceOfFollowupFindingsPoints) {
		this.lastSequenceOfFollowupFindingsPoints = lastSequenceOfFollowupFindingsPoints;
	}

	public Integer getLastSequenceOfFollowupPointsPoints() {
		return lastSequenceOfFollowupPointsPoints;
	}

	public void setLastSequenceOfFollowupPointsPoints(Integer lastSequenceOfFollowupPointsPoints) {
		this.lastSequenceOfFollowupPointsPoints = lastSequenceOfFollowupPointsPoints;
	}

	public TmpComplianceReviewPicFollowupReview[] getSelectedTmpComplianceReviewPicFollowupReview() {
		return selectedTmpComplianceReviewPicFollowupReview;
	}

	public void setSelectedTmpComplianceReviewPicFollowupReview(
			TmpComplianceReviewPicFollowupReview[] selectedTmpComplianceReviewPicFollowupReview) {
		this.selectedTmpComplianceReviewPicFollowupReview = selectedTmpComplianceReviewPicFollowupReview;
	}

	public TmpComplianceReviewPicFollowupFindings[] getSelectedTmpComplianceReviewPicFollowupFindings() {
		return selectedTmpComplianceReviewPicFollowupFindings;
	}

	public void setSelectedTmpComplianceReviewPicFollowupFindings(
			TmpComplianceReviewPicFollowupFindings[] selectedTmpComplianceReviewPicFollowupFindings) {
		this.selectedTmpComplianceReviewPicFollowupFindings = selectedTmpComplianceReviewPicFollowupFindings;
	}

	public TmpComplianceReviewPicFollowupPoints[] getSelectedTmpComplianceReviewPicFollowupPoints() {
		return selectedTmpComplianceReviewPicFollowupPoints;
	}

	public void setSelectedTmpComplianceReviewPicFollowupPoints(
			TmpComplianceReviewPicFollowupPoints[] selectedTmpComplianceReviewPicFollowupPoints) {
		this.selectedTmpComplianceReviewPicFollowupPoints = selectedTmpComplianceReviewPicFollowupPoints;
	}

	public String getApprovalStat() {
		return approvalStat;
	}

	public void setApprovalStat(String approvalStat) {
		this.approvalStat = approvalStat;
	}

	public List<SelectItem> getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(List<SelectItem> approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

}