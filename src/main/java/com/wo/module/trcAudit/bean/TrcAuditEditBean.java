package com.wo.module.trcAudit.bean;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
import com.wo.module.common.exception.CustomAPIException;
import com.wo.module.common.model.ColumnModel;
import com.wo.module.common.model.UploadedFileWO;
import com.wo.module.common.util.DateUtil;
import com.wo.module.common.util.FileUtil;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.counterType.service.CounterTypeService;
import com.wo.module.emailTemplate.model.EmailTemplate;
import com.wo.module.emailTemplate.service.EmailTemplateService;
import com.wo.module.externalRegulation.model.RegulationTrackRecord;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.mstAudit.model.MstAudit;
import com.wo.module.mstAudit.service.MstAuditService;
import com.wo.module.mstAudit.vo.MstAuditVO;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;
import com.wo.module.parameter.service.ParameterDetailService;
import com.wo.module.regulationSocialization.vo.StatusConfirmationVO;
import com.wo.module.trcAudit.constant.TrcAuditConstants;
import com.wo.module.trcAudit.model.TrcAudit;
import com.wo.module.trcAudit.model.TrcAuditDocument;
import com.wo.module.trcAudit.model.TrcAuditPICComplianceTableModel;
import com.wo.module.trcAudit.model.TrcAuditPICFollowupTableModel;
import com.wo.module.trcAudit.model.TrcAuditPicCompliance;
import com.wo.module.trcAudit.model.TrcAuditPicFollowup;
import com.wo.module.trcAudit.service.TrcAuditPICFollowupService;
import com.wo.module.trcAudit.service.TrcAuditService;
import com.wo.module.user.model.Division;
import com.wo.module.user.model.User;
import com.wo.module.user.service.UserService;

public class TrcAuditEditBean extends CommonBean implements Serializable, TrcAuditConstants {

	private static final long serialVersionUID = -7626645263134025560L;

	static Logger logger = Logger.getLogger(TrcAuditEditBean.class);

	private TrcAudit trcAudit;
	private TrcAuditPicFollowup trcAuditPicFollowup;
	private Long trcAuditPicFollowupId;

	private Boolean isViewOnly;
	private String viewOnly;

	private String actionMode;

	private String editedId;

	private Long oldCounterTypeId;
	private Long newCounterTypeId;

	private Boolean disabledFollowUpStatus;
	private Integer indexDtlCompliance;
	private Long counterTypeId;

	private List<SelectItem> selectAuditFollowUp;
	private List<SelectItem> selectAuditObject;
	private List<SelectItem> selectAuditCategory;
	private List<SelectItem> counterTypes;
	private List<SelectItem> followUps;

	private List<UploadedFileWO> uploadedFilesAttachmentLetter;
	private List<UploadedFileWO> uploadedFiles;
	private List<UploadedFileWO> uploadedFilesAuditBankAttachment;
	private List<UploadedFileWO> deletedFiles;

	private FileUtil fileUtil;

	// services
	private TrcAuditService trcAuditService;
	private CounterTypeService counterTypeService;
	private UserService userService;
	private TrcAuditPICFollowupService trcAuditPICFollowupService;
	private EmailTemplateService emailTemplateService;
	private MstAuditService mstAuditService;

	private List<SelectItem> divisions;

	private List<SelectItem> reminderStatusList;

	private List<SelectItem> templateAuditList;
	
	private TrcAuditPicCompliance[] selectedDataCompliance;

	private TrcAuditPicFollowup[] selectedDataFollowup;

	private TrcAuditPICComplianceTableModel<TrcAuditPicCompliance> tableModelCompliance;

	private TrcAuditPICFollowupTableModel<TrcAuditPicFollowup> tableModelFollowup;

	private Integer lastSequenceOfCompliance;
	private Integer lastSequenceOfFollowup;

	private List<StatusConfirmationVO> tableStatus;

	private RegulationTrackRecord selectedRow;

	private Integer indexDtl;
	private Integer indexDtlFollowup;

	public FacesUtil facesUtil;

	private String navigateSearch = NAVIGATE_SEARCH;
	
	private List<TrcAuditPicFollowup> complianceTableList;

	private String note;

	SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
	private List<SelectItem> yesNo;
	
	private String textWarningUpload;
	
	@PostConstruct
	public void init() {
		super.init();
		fileUtil = FileUtil.getInstance();

		constructSelectComponent();

		tableStatus = new ArrayList<StatusConfirmationVO>();
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
			selectAuditCategory = parameterDetailService.getListLabelValue(
					ParameterHeader.PARAM_HEAD_CODE_AUDIT_CATEGORY, false, true, facesUtil.retrieveDefaultLocale());
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

		PrimeFaces.current().executeScript("reInitSelect2();");
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
//			this.handleNew();
		} else {
			this.handleEdit(editId);
		}
	}

	private void handleEdit(String editId) {
		String token = facesUtil.retrieveRequestParam("token");
		if (StringUtils.isNotEmpty(token)) {
			editId = Constants.decryptString(token);
		}
		facesUtil.setSessionAttribute("token", Constants.encryptString(editId));
		actionMode = Constants.ACTION_EDIT;
		disabledFollowUpStatus = false;
		Long idLong = Long.parseLong(editId);
		trcAuditPicFollowupId = idLong;
		trcAuditPicFollowup = trcAuditPICFollowupService.findById(idLong);
		trcAudit = trcAuditPicFollowup.getTrcAuditPicFollowupBankCommitment().getTrcAuditCheckPoint().getTrcAudit();
		
		
		/*trcAuditPicFollowup.getTrcAuditPicFollowupAuditFindings().forEach(finding -> {
			
			if(finding.getColumnModel() == null) {
				int idx = trcAuditPicFollowup.getTrcAuditPicFollowupAuditFindings().indexOf(finding);
				ColumnModel cm = new ColumnModel();
				cm.setRow(idx);
				cm.setColumnModels(new ArrayList<ColumnModel>());
				
				int i=0;
				while(i < finding.getColumn()) {
					cm.getColumnModels().add(new ColumnModel("column"+(i+1),"column"+(i+1)));
					i++;
				}
				
				finding.setColumnModel(cm);
			}
		});*/
		
		if(trcAuditPicFollowup.getTrcAuditPicFollowupSupportingUnits() != null
				&& !trcAuditPicFollowup.getTrcAuditPicFollowupSupportingUnits().isEmpty())
			trcAuditPicFollowup.getTrcAuditPicFollowupSupportingUnits().forEach(supp -> {
				if (supp.getEmailCc1() != null)
					supp.setEmailCcTemp1(supp.getEmailCc1().getNik() + "-" + supp.getEmailCc1().getName());
				if (supp.getEmailCc2() != null)
					supp.setEmailCcTemp2(supp.getEmailCc2().getNik() + "-" + supp.getEmailCc2().getName());
				if (supp.getEmailCc3() != null)
					supp.setEmailCcTemp3(supp.getEmailCc3().getNik() + "-" + supp.getEmailCc3().getName());
		});

		complianceTableList = new ArrayList<TrcAuditPicFollowup>();
		complianceTableList.add(trcAuditPicFollowup);
		
		lastSequenceOfCompliance = 0;
		lastSequenceOfFollowup = 0;

		if (trcAudit.getCounterType() != null) {
			counterTypeId = trcAudit.getCounterType().getCounterTypeId();
			oldCounterTypeId = new Long(counterTypeId);
		}

		if (trcAudit.getTrcAuditPicCompliances() != null) {
			lastSequenceOfCompliance = trcAudit.getTrcAuditPicCompliances().size();
			for (int i = 0; i < trcAudit.getTrcAuditPicCompliances().size(); i++) {
				TrcAuditPicCompliance dtl = trcAudit.getTrcAuditPicCompliances().get(i);
				lastSequenceOfCompliance = lastSequenceOfCompliance + 1;
				dtl.setSequence(lastSequenceOfCompliance);
			}
		}

		// filter DOC_TYPE_AUDIT_FINDINGS
//		uploadedFilesAuditFindings = new ArrayList<UploadedFileWO>();
//		List<TrcAuditDocument> auditFindingsDocuments = trcAudit.getTrcAuditDocuments().stream()
//				.filter(doc -> DOC_TYPE_AUDIT_FINDINGS.equals(doc.getDocumentType())).collect(Collectors.toList());
//		for (TrcAuditDocument tad : auditFindingsDocuments) {
//			UploadedFileWO uf = new UploadedFileWO();
//			uf.setFileName(tad.getAttachmentFile());
//			uf.setFileId(tad.getFileId());
//			uf.setIsNew(false);
//			uf.setFileSize(tad.getFileSize());
//			uploadedFilesAuditFindings.add(uf);
//		}

		// filter DOC_TYPE_BANK_RESPONSE
//		uploadedFilesAuditBankResponse = new ArrayList<UploadedFileWO>();
//		List<TrcAuditDocument> auditBankResponseDocuments = trcAudit.getTrcAuditDocuments().stream()
//				.filter(doc -> DOC_TYPE_BANK_RESPONSE.equals(doc.getDocumentType())).collect(Collectors.toList());
//		for (TrcAuditDocument tad : auditBankResponseDocuments) {
//			UploadedFileWO uf = new UploadedFileWO();
//			uf.setFileName(tad.getAttachmentFile());
//			uf.setFileId(tad.getFileId());
//			uf.setIsNew(false);
//			uf.setFileSize(tad.getFileSize());
//			uploadedFilesAuditBankResponse.add(uf);
//		}

		// filter DOC_TYPE_BANK_RESPONSE
//		uploadedFilesAuditBankCommitment = new ArrayList<UploadedFileWO>();
//		List<TrcAuditDocument> auditBankCommitmentDocuments = trcAudit.getTrcAuditDocuments().stream()
//				.filter(doc -> DOC_TYPE_BANK_COMMITMENT.equals(doc.getDocumentType())).collect(Collectors.toList());
//		for (TrcAuditDocument tad : auditBankCommitmentDocuments) {
//			UploadedFileWO uf = new UploadedFileWO();
//			uf.setFileName(tad.getAttachmentFile());
//			uf.setFileId(tad.getFileId());
//			uf.setIsNew(false);
//			uf.setFileSize(tad.getFileSize());
//			uploadedFilesAuditBankCommitment.add(uf);
//		}
		
		uploadedFilesAuditBankAttachment = new ArrayList<UploadedFileWO>();
		for (TrcAuditDocument tad : trcAudit.getTrcAuditDocuments()) {
			UploadedFileWO uf = new UploadedFileWO();
			uf.setFileName(tad.getAttachmentFile());
			uf.setFileId(tad.getFileId());
			uf.setIsNew(false);
			uf.setFileSize(tad.getFileSize());
			uploadedFilesAuditBankAttachment.add(uf);
		}

		uploadedFiles = new ArrayList<UploadedFileWO>();
		if(trcAuditPicFollowup.getFollowupAttachDoc() != null && !trcAuditPicFollowup.getFollowupAttachDoc().isEmpty())
		{
			trcAuditPicFollowup.getFollowupAttachDoc().forEach(attDoc -> {
				UploadedFileWO uf = new UploadedFileWO();
				uf.setFileName(attDoc.getAttachmentFile());
				uf.setFileId(attDoc.getFileId());
				uf.setIsNew(false);
				uf.setFileSize(attDoc.getFileSize());
				uploadedFiles.add(uf);
			});
		}
		
		uploadedFilesAttachmentLetter = new ArrayList<UploadedFileWO>();
		if(trcAuditPicFollowup.getLetterAttachDoc() != null && !trcAuditPicFollowup.getLetterAttachDoc().isEmpty())
		{
			trcAuditPicFollowup.getLetterAttachDoc().forEach(letterDoc -> {
				UploadedFileWO uf = new UploadedFileWO();
				uf.setFileName(letterDoc.getAttachmentFile());
				uf.setFileId(letterDoc.getFileId());
				uf.setIsNew(false);
				uf.setFileSize(letterDoc.getFileSize());
				uploadedFilesAttachmentLetter.add(uf);
			});
		}
//		for (TrcAuditPicFollowupAttachment attac : trcAudit.getTrcAuditPicFollowupAttachments()) {
//			UploadedFileWO uf = new UploadedFileWO();
//			uf.setFileName(attac.getAttachmentFile());
//			uf.setFileId(attac.getFileId());
//			uf.setIsNew(false);
//			uf.setFileSize(attac.getFileSize());
//			uploadedFiles.add(uf);
//		}
		
		tableModelCompliance = new TrcAuditPICComplianceTableModel<TrcAuditPicCompliance>(
				trcAudit.getTrcAuditPicCompliances());
		/*tableModelFollowup = new TrcAuditPICFollowupTableModel<TrcAuditPicFollowup>(trcAudit.getTrcAuditPicFollowups());

		if (trcAuditPicFollowup.getFollowupStatus() != null
				&& trcAuditPicFollowup.getFollowupStatus().getParameterDtlCode() != null
				&& trcAuditPicFollowup.getFollowupStatus().getParameterDtlCode()
						.equals(ParameterDetail.PARAM_DET_CODE_PIC_FOLLOWUP_STATUS_PIC_DONE)) {
			viewOnly = "Y";

			SimpleDateFormat sdf = new SimpleDateFormat(CommonConstants.INPUT_DATE_FORMAT);

			facesUtil.addWarnMessage(facesUtil.retrieveMessage("formRegulationSocializationNotifConfirmationDone",
					sdf.format(trcAuditPicFollowup.getTargetDate()), trcAuditPicFollowup.getFollowupBy().getName()));

		} else {
			viewOnly = "N";
		}*/
		
//		tableApproval = regulationSocializationService.getDataApprovalBySocializationId(idLong);
//		tableStatus = regulationSocializationService.getDataConfirmStatusBySocializationId(idLong);

	}
	
	public void handleFileUpload(FileUploadEvent event) throws Exception {
		try {
			if (uploadedFiles == null)
				uploadedFiles = new ArrayList<UploadedFileWO>();

			uploadedFiles.add(new UploadedFileWO(
					CallApiManager.callUploadAPI(event.getFile(), Constants.COMPLIANCE_DOC_TYPE_AUDIT_FOLLOWUP,
							parameterDetailService, false, fileUtil),
					event.getFile().getFileName(), event.getFile().getContentType(), event.getFile().getSize()));
		} catch (Exception e) {
			e.printStackTrace();
			facesUtil.addErrMessage(e.getMessage());
		}
	}
	
	public void handleFileAttachmentLetterUpload(FileUploadEvent event) throws Exception {
		try {
			if (uploadedFilesAttachmentLetter == null)
				uploadedFilesAttachmentLetter = new ArrayList<UploadedFileWO>();

			uploadedFilesAttachmentLetter.add(new UploadedFileWO(
					CallApiManager.callUploadAPI(event.getFile(), Constants.COMPLIANCE_DOC_TYPE_AUDIT_FOLLOWUP_LETTER,
							parameterDetailService, false, fileUtil),
					event.getFile().getFileName(), event.getFile().getContentType(), event.getFile().getSize()));
		} catch (Exception e) {
			e.printStackTrace();
			facesUtil.addErrMessage(e.getMessage());
		}
	}

	public void downloadFile(String fileId, String fileName, byte[] content) throws Exception {
		CallApiManager.downloadFile(fileId, fileName, content, parameterDetailService);
	}
	
	public void deleteConfirmationLetter(String fileId,int index) throws Exception {
		//CallApiManager.deleteFile(fileId, parameterDetailService, fileUtil);
		deletedFiles = deletedFiles!=null?deletedFiles: new ArrayList<UploadedFileWO>();
		
		deletedFiles.add(new UploadedFileWO(fileId,null,null, null));
		
		uploadedFilesAttachmentLetter.remove(uploadedFilesAttachmentLetter.get(index));
		
	}

	public void cancel() {
		try {
			facesUtil.redirect("/pages/trcAudit/trcAudit.faces");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void save() {
		try {
			if (!isError()) {
				User user = (User) facesUtil.getUserLogin();

				trcAuditService.processConfirm(trcAuditPicFollowup, uploadedFiles, uploadedFilesAttachmentLetter, user);

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

				facesUtil.redirect("/pages/trcAudit/trcAudit.faces");
			}

		} catch (Exception ex) {
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + ex.getMessage(), "");
			ex.printStackTrace();
		}
	}
	
	public Boolean isError() {
		Boolean flag = false;

		if (trcAuditPicFollowup.getFollowupDate() == null) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formPICFollowupConfirmationFollowupDate") + " "
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}
		
		
		if (uploadedFiles == null || uploadedFiles.size() <= 0) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formPICFollowupConfirmationPICConfirmationEvidence") + " File "
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}
		
//		if (uploadedFilesAttachmentLetter == null || uploadedFilesAttachmentLetter.isEmpty()) {
//			facesUtil.addErrMessage(facesUtil.retrieveMessage("formPICFollowupConfirmationLetter") + " File "
//					+ facesUtil.retrieveMessage("validateRequired"));
//			flag = true;
//		}

		return flag;
	}
	
	@SuppressWarnings("static-access")
	public void sendEmail() {
		try {

			EmailTemplate emailTemplate = getEmailTemplateService()
					.getEmailTemplateByEmailTemplateCode("EMAIL_PIC_COMPLIANCE");
			String emailSubject = emailTemplate.getEmailSubject();

			String emailContent = "";
			String emailTo = "";
			String emailCc = "";

			String auditorNameTemp = "";
			ParameterDetail paramAuditorTemp = parameterDetailService.getParameterDetailByParamDtlCode(trcAudit.getAuditor());
			
			Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
			
			if (locale != null && locale.equals(locale.ENGLISH)) {
				auditorNameTemp = paramAuditorTemp.getNameEn() != null ? " - " + paramAuditorTemp.getNameEn() : "";
			} else {
				auditorNameTemp = paramAuditorTemp.getNameIn() != null ? " - " + paramAuditorTemp.getNameIn() : "";
			}
			
//			String auditTopicIn = trcAudit.getAuditTopicIn() != null ? " - " + trcAudit.getAuditTopicIn() : "";
			
			emailContent = emailTemplate.getEmailContent().replace(Constants.CONFIRMATION_TYPE_AND_DOC_NUM, AUDIT 
					+ auditorNameTemp);
			
			emailSubject = emailSubject.replace(Constants.CONFIRMATION_TYPE_AND_DOC_NUM, AUDIT 
					+ auditorNameTemp);

			for (int x = 0; x < trcAudit.getTrcAuditPicCompliances().size(); x++) {
				TrcAuditPicCompliance cd = trcAudit.getTrcAuditPicCompliances().get(x);
				emailTo = cd.getUser().getEmail();

				final String subject = emailSubject;
				final String content = emailContent;
				final String to = emailTo;
				// final String to = "h3ndr407@gmail.com";
				final String cc = emailCc;

				CallApiManager.sendEmailAPI(to, cc, subject, content, "EMAIL_SOSIALISASI", "true",
						parameterDetailService);

			}

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new CustomAPIException(ex.getMessage()); 
		}

	}

	public void onChangeAuditMaster() {
		MstAuditVO getSingleData = mstAuditService.getSingleDataMstAudit(trcAudit.getMstAudit().getMstAuditId());
		
		try {
			trcAudit.setAuditor(getSingleData.getAuditorCode());
			trcAudit.setAuditDateFrom(DateUtil.stringToDateFromDDMMMYYYY(getSingleData.getAuditDateFrom()));
			trcAudit.setAuditDateTo(DateUtil.stringToDateFromDDMMMYYYY(getSingleData.getAuditDateTo()));
			trcAudit.setScope(getSingleData.getScope());
		} catch (ParseException e) {
			e.printStackTrace();
		}
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

	public SimpleDateFormat getSdf() {
		return sdf;
	}

	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}

	public static Logger getLogger() {
		return logger;
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

	public Integer getIndexDtlFollowup() {
		return indexDtlFollowup;
	}

	public void setIndexDtlFollowup(Integer indexDtlFollowup) {
		this.indexDtlFollowup = indexDtlFollowup;
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

	public List<StatusConfirmationVO> getTableStatus() {
		return tableStatus;
	}

	public void setTableStatus(List<StatusConfirmationVO> tableStatus) {
		this.tableStatus = tableStatus;
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

	public FileUtil getFileUtil() {
		return fileUtil;
	}

	public void setFileUtil(FileUtil fileUtil) {
		this.fileUtil = fileUtil;
	}

	public TrcAuditPICComplianceTableModel<TrcAuditPicCompliance> getTableModelCompliance() {
		return tableModelCompliance;
	}

	public void setTableModelCompliance(TrcAuditPICComplianceTableModel<TrcAuditPicCompliance> tableModelCompliance) {
		this.tableModelCompliance = tableModelCompliance;
	}

	public TrcAuditPicCompliance[] getSelectedDataCompliance() {
		return selectedDataCompliance;
	}

	public void setSelectedDataCompliance(TrcAuditPicCompliance[] selectedDataCompliance) {
		this.selectedDataCompliance = selectedDataCompliance;
	}

	public TrcAuditPICFollowupTableModel<TrcAuditPicFollowup> getTableModelFollowup() {
		return tableModelFollowup;
	}

	public void setTableModelFollowup(TrcAuditPICFollowupTableModel<TrcAuditPicFollowup> tableModelFollowup) {
		this.tableModelFollowup = tableModelFollowup;
	}

	public TrcAuditPicFollowup[] getSelectedDataFollowup() {
		return selectedDataFollowup;
	}

	public void setSelectedDataFollowup(TrcAuditPicFollowup[] selectedDataFollowup) {
		this.selectedDataFollowup = selectedDataFollowup;
	}

	public List<SelectItem> getFollowUps() {
		return followUps;
	}

	public void setFollowUps(List<SelectItem> followUps) {
		this.followUps = followUps;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public TrcAudit getTrcAudit() {
		return trcAudit;
	}

	public void setTrcAudit(TrcAudit trcAudit) {
		this.trcAudit = trcAudit;
	}

	public TrcAuditService getTrcAuditService() {
		return trcAuditService;
	}

	public void setTrcAuditService(TrcAuditService trcAuditService) {
		this.trcAuditService = trcAuditService;
	}

	public String getViewOnly() {
		return viewOnly;
	}

	public void setViewOnly(String viewOnly) {
		this.viewOnly = viewOnly;
	}

	public TrcAuditPicFollowup getTrcAuditPicFollowup() {
		return trcAuditPicFollowup;
	}

	public void setTrcAuditPicFollowup(TrcAuditPicFollowup trcAuditPicFollowup) {
		this.trcAuditPicFollowup = trcAuditPicFollowup;
	}

	public TrcAuditPICFollowupService getTrcAuditPICFollowupService() {
		return trcAuditPICFollowupService;
	}

	public void setTrcAuditPICFollowupService(TrcAuditPICFollowupService trcAuditPICFollowupService) {
		this.trcAuditPICFollowupService = trcAuditPICFollowupService;
	}

	public List<TrcAuditPicFollowup> getComplianceTableList() {
		return complianceTableList;
	}

	public void setComplianceTableList(List<TrcAuditPicFollowup> complianceTableList) {
		this.complianceTableList = complianceTableList;
	}

	public List<UploadedFileWO> getUploadedFiles() {
		return uploadedFiles;
	}

	public void setUploadedFiles(List<UploadedFileWO> uploadedFiles) {
		this.uploadedFiles = uploadedFiles;
	}

	public List<UploadedFileWO> getDeletedFiles() {
		return deletedFiles;
	}

	public void setDeletedFiles(List<UploadedFileWO> deletedFiles) {
		this.deletedFiles = deletedFiles;
	}

	public Long getTrcAuditPicFollowupId() {
		return trcAuditPicFollowupId;
	}

	public void setTrcAuditPicFollowupId(Long trcAuditPicFollowupId) {
		this.trcAuditPicFollowupId = trcAuditPicFollowupId;
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

	public List<UploadedFileWO> getUploadedFilesAttachmentLetter() {
		return uploadedFilesAttachmentLetter;
	}

	public void setUploadedFilesAttachmentLetter(List<UploadedFileWO> uploadedFilesAttachmentLetter) {
		this.uploadedFilesAttachmentLetter = uploadedFilesAttachmentLetter;
	}

	public List<UploadedFileWO> getUploadedFilesAuditBankAttachment() {
		return uploadedFilesAuditBankAttachment;
	}

	public void setUploadedFilesAuditBankAttachment(List<UploadedFileWO> uploadedFilesAuditBankAttachment) {
		this.uploadedFilesAuditBankAttachment = uploadedFilesAuditBankAttachment;
	}

	public MstAuditService getMstAuditService() {
		return mstAuditService;
	}

	public void setMstAuditService(MstAuditService mstAuditService) {
		this.mstAuditService = mstAuditService;
	}

	public List<SelectItem> getTemplateAuditList() {
		return templateAuditList;
	}

	public void setTemplateAuditList(List<SelectItem> templateAuditList) {
		this.templateAuditList = templateAuditList;
	}

	public String getTextWarningUpload() {
		return textWarningUpload;
	}

	public void setTextWarningUpload(String textWarningUpload) {
		this.textWarningUpload = textWarningUpload;
	}
	
}