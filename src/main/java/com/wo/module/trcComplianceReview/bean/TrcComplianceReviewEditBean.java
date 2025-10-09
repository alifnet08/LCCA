package com.wo.module.trcComplianceReview.bean;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.CommonConstants;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.exception.CustomAPIException;
import com.wo.module.common.js.JsUtil;
import com.wo.module.common.model.UploadedFileWO;
import com.wo.module.common.util.FileUtil;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.counterType.service.CounterTypeService;
import com.wo.module.emailTemplate.model.EmailTemplate;
import com.wo.module.emailTemplate.service.EmailTemplateService;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.lov.bean.SelectorModel.SelectorInfo;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;
import com.wo.module.tmpComplianceReview.constant.TmpComplianceReviewConstants;
import com.wo.module.tmpComplianceReview.service.ComplianceReviewService;
import com.wo.module.tmpComplianceReview.vo.ComplianceReviewApprovalVO;
import com.wo.module.tmpComplianceReview.vo.StatusConfirmationVO;
import com.wo.module.trcComplianceReview.constant.TrcComplianceReviewConstants;
import com.wo.module.trcComplianceReview.model.TrcComplianceReview;
import com.wo.module.trcComplianceReview.model.TrcComplianceReviewDocument;
import com.wo.module.trcComplianceReview.model.TrcComplianceReviewPicCompliance;
import com.wo.module.trcComplianceReview.model.TrcComplianceReviewPicComplianceTableModel;
import com.wo.module.trcComplianceReview.model.TrcComplianceReviewPicFollowup;
import com.wo.module.trcComplianceReview.model.TrcComplianceReviewPicFollowupPoints;
import com.wo.module.trcComplianceReview.service.TrcComplianceReviewPicFollowupService;
import com.wo.module.trcComplianceReview.service.TrcComplianceReviewService;
import com.wo.module.user.model.Division;
import com.wo.module.user.model.User;
import com.wo.module.user.service.UserService;

public class TrcComplianceReviewEditBean extends CommonBean implements Serializable {

	private static final long serialVersionUID = 5721271313770036670L;

	static Logger logger = Logger.getLogger(TrcComplianceReviewEditBean.class);

	private TrcComplianceReview trcComplianceReview;

	private Boolean isViewOnly;

	private String actionMode;

	private String editedId;

	private Long oldCounterTypeId;
	private Long newCounterTypeId;

	private Boolean disabledFollowUpStatus;

	private Long counterTypeId;

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

	private TrcComplianceReviewPicCompliance[] selectedDataCompliance;

	private TrcComplianceReviewPicFollowup[] selectedDataFollowup;

	private TrcComplianceReviewPicComplianceTableModel<TrcComplianceReviewPicCompliance> tableModelCompliance;
	
	private List<TrcComplianceReviewPicFollowup> tableModelFollowupPoints;

	private Integer lastSequenceOfCompliance;
	private Integer lastSequenceOfFollowup;

	private List<StatusConfirmationVO> tableStatus;

	private List<ComplianceReviewApprovalVO> tableApproval;

	private Integer indexDtlCompliance;

	private Integer indexDtlFollowup;

	private ComplianceReviewService complianceReviewService;

	private TrcComplianceReviewService trcComplianceReviewService;

	private UserService userService;

	private CounterTypeService counterTypeService;

	private EmailTemplateService emailTemplateService;

	private TrcComplianceReviewPicFollowupService trcComplianceReviewPicFollowupService;

	public FacesUtil facesUtil;

	private String navigateSearch = TrcComplianceReviewConstants.NAVIGATE_SEARCH;

	private Long trcComplianceReviewPICFollowupId;

	SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");

	private List<UploadedFileWO> uploadedFilesDocument;
//	private List<UploadedFileWO> uploadedFilesFollowupPoints;
	//private List<UploadedFileWO> uploadedFilesEvidence;
	private List<UploadedFileWO> deletedFiles;

	private String REMINDER_ACTIVE = ParameterDetail.PARAM_DET_CODE_REMINDER_ACTIVE;
	private String REMINDER_INACTIVE = ParameterDetail.PARAM_DET_CODE_REMINDER_INACTIVE;

	private List<SelectItem> searchType;

	private FileUtil fileUtil;

	private String viewOnly;

	private TrcComplianceReviewPicFollowup trcComplianceReviewPicFollowup;

	private List<TrcComplianceReviewPicFollowup> confirmationTableList;

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
		selectReviewCategory();
		selectDivision();
		selectFollowup();
		selectCounterType();
		selectReminderStatus();

		selectorCompliance = TmpComplianceReviewConstants.buildSelectorPICCompliance(facesUtil);
		selectorFollowup = TmpComplianceReviewConstants.buildSelectorDivision(facesUtil);
		selectorPic1 = TmpComplianceReviewConstants.buildSelectorPIC(facesUtil);
		selectorPic2 = TmpComplianceReviewConstants.buildSelectorPIC(facesUtil);
		selectorPic3 = TmpComplianceReviewConstants.buildSelectorPIC(facesUtil);

		tableApproval = new ArrayList<ComplianceReviewApprovalVO>();
		tableStatus = new ArrayList<StatusConfirmationVO>();
		checkNewOrEdit();

		fileUtil = FileUtil.getInstance();
		facesUtil.removeSessionAttribute(Constants.SESSION_NEED_REDIRECT);
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

		PrimeFaces.current().executeScript("reInitSelect2();");
	}

	public void selectCounterType() {
		try {
			counterTypes = counterTypeService.getAllCounterTypeLabelValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void cancel() {
		try {
			for(TrcComplianceReviewPicFollowupPoints o : tableModelFollowupPoints.get(0).getTrcComplianceReviewPicFollowupPoints()) {
				if(o.getUploadedFilesEvidence() != null) {
					for(UploadedFileWO uf : o.getUploadedFilesEvidence()) {
						if (uf.getIsNew() == null) {
							CallApiManager.deleteFile(uf.getFileId(), parameterDetailService, fileUtil);
						}
					}
				}
			}
			
			facesUtil.redirect("/pages/trcComplianceReview/trcComplianceReview.faces");
		} catch (Exception ex) {
			ex.printStackTrace();
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + ex.getMessage(), "");
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
			// this.handleNew();
		} else {
			this.handleEdit(editId);
			JsUtil.hideTHeadFollowupPoints();
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
		trcComplianceReviewPICFollowupId = idLong;
		trcComplianceReviewPicFollowup = trcComplianceReviewPicFollowupService.findById(idLong);
		confirmationTableList = new ArrayList<TrcComplianceReviewPicFollowup>();
		confirmationTableList.add(trcComplianceReviewPicFollowup);
		trcComplianceReview = trcComplianceReviewService.findById(trcComplianceReviewPicFollowup.getTrcComplianceReview().getComplianceReviewId());

		if (trcComplianceReview.getCounterType() != null) {
			counterTypeId = trcComplianceReview.getCounterType().getCounterTypeId();
		}

		uploadedFilesDocument = new ArrayList<UploadedFileWO>();

		for (int i = 0; i < trcComplianceReview.getTrcComplianceReviewDocuments().size(); i++) {
			TrcComplianceReviewDocument ra = trcComplianceReview.getTrcComplianceReviewDocuments().get(i);
			UploadedFileWO uf = new UploadedFileWO();
			uf.setFileName(ra.getAttachmentFile());
			uf.setFileId(ra.getFileId());
			uf.setFileSize(ra.getFileSize());
			uf.setIsNew(false);
			uploadedFilesDocument.add(uf);

		}

//		uploadedFilesFollowupPoints = new ArrayList<UploadedFileWO>();

		if (trcComplianceReview.getTrcComplianceReviewPicCompliances() != null) {
			lastSequenceOfCompliance = trcComplianceReview.getTrcComplianceReviewPicCompliances().size();
			for (int i = 0; i < trcComplianceReview.getTrcComplianceReviewPicCompliances().size(); i++) {
				TrcComplianceReviewPicCompliance dtl = trcComplianceReview.getTrcComplianceReviewPicCompliances()
						.get(i);
				lastSequenceOfCompliance = lastSequenceOfCompliance + 1;
				dtl.setSequence(lastSequenceOfCompliance);
			}
		}

		tableModelCompliance = new TrcComplianceReviewPicComplianceTableModel<TrcComplianceReviewPicCompliance>(
				trcComplianceReview.getTrcComplianceReviewPicCompliances());
		
		trcComplianceReviewPicFollowup.getTrcComplianceReviewPicFollowupPoints().forEach(fol -> {
			List<UploadedFileWO> woFile = new ArrayList<UploadedFileWO>();
			fol.getTrcComplianceReviewPicFollowupPointsAttachments().forEach(file -> {
				UploadedFileWO wo = new UploadedFileWO(file.getFileId(), file.getAttachmentFile(), file.getFileSize());
				wo.setIsNew(false);
				woFile.add(wo);
				
				
			});
			fol.setUploadedFilesEvidence(woFile);
		});
		
		tableModelFollowupPoints = new ArrayList<TrcComplianceReviewPicFollowup>();
		tableModelFollowupPoints.add(trcComplianceReviewPicFollowup);

		tableApproval = complianceReviewService.getDataApprovalByComplianceReviewId(idLong);
		tableStatus = complianceReviewService.getDataConfirmStatusByComplianceReviewId(idLong);

		if (trcComplianceReviewPicFollowup.getFollowupStatus() != null
				&& trcComplianceReviewPicFollowup.getFollowupStatus().getParameterDtlCode() != null
				&& trcComplianceReviewPicFollowup.getFollowupStatus().getParameterDtlCode()
						.equals(ParameterDetail.PARAM_DET_CODE_PIC_FOLLOWUP_STATUS_PIC_DONE)) {
			viewOnly = "Y";

			SimpleDateFormat sdf = new SimpleDateFormat(CommonConstants.INPUT_DATE_FORMAT);
			
			StringBuilder sbFollowupBy = new StringBuilder();
			
			if(trcComplianceReviewPicFollowup.getTrcComplianceReviewPicFollowupPoints() != null
					&& !trcComplianceReviewPicFollowup.getTrcComplianceReviewPicFollowupPoints().isEmpty()) {
				
				List<String> followupBys = new ArrayList<String>();
				for (TrcComplianceReviewPicFollowupPoints fPoint : trcComplianceReviewPicFollowup
						.getTrcComplianceReviewPicFollowupPoints()) {
					followupBys.add(fPoint.getFollowupBy().getName());
				}
				
				followupBys = followupBys.stream().distinct().collect(Collectors.toList());
				
				for (String followupBy : followupBys) {
					sbFollowupBy.append(followupBy);
					
					// if not last item, add comma
					if (followupBys.indexOf(followupBy) != followupBys.size() - 1) {
						sbFollowupBy.append(", ");
					}
				}
			}

			facesUtil.addWarnMessage(facesUtil.retrieveMessage("formTmpComplianceReviewNotifConfirmationDone",
					sdf.format(trcComplianceReviewPicFollowup.getTargetDate()),
					sbFollowupBy.toString()));

		} else {
			viewOnly = "N";
		}

	}

	public void sendEmail() {
		try {

			EmailTemplate emailTemplate = emailTemplateService
					.getEmailTemplateByEmailTemplateCode("EMAIL_PIC_COMPLIANCE");
			String emailSubject = emailTemplate.getEmailSubject();

			String emailContent = "";
			String emailTo = "";
			String emailCc = "";
			
			String noDocAssigment = trcComplianceReview.getDocumentNo() != null ? " - " + trcComplianceReview.getDocumentNo() : "";

			emailContent = emailTemplate.getEmailContent().replace(Constants.CONFIRMATION_TYPE_AND_DOC_NUM, "Compliance Onsite Review" 
					+ noDocAssigment);
			
			emailSubject = emailSubject.replace(Constants.CONFIRMATION_TYPE_AND_DOC_NUM, "Compliance Onsite Review" 
					+ noDocAssigment);

			for (int x = 0; x < trcComplianceReview.getTrcComplianceReviewPicCompliances().size(); x++) {
				TrcComplianceReviewPicCompliance cd = trcComplianceReview.getTrcComplianceReviewPicCompliances().get(x);
				emailTo = cd.getUser().getEmail();

				// ExecutorService emailExecutor =
				// Executors.newCachedThreadPool();

				final String subject = emailSubject;
				final String content = emailContent;
				final String to = emailTo;
				// final String to = "h3ndr407@gmail.com";
				final String cc = emailCc;

				CallApiManager.sendEmailAPI(to, cc, subject, content, "EMAIL_COMPLIANCE_ASSESSMENT", "true",
						parameterDetailService);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new CustomAPIException(ex.getMessage()); 
		}

	}

	public void deleteAttachment(String fileId, int index, int idxFollowup) throws Exception {
		deletedFiles = deletedFiles != null ? deletedFiles : new ArrayList<UploadedFileWO>();
		deletedFiles.add(new UploadedFileWO(fileId, null, null, null));
		tableModelFollowupPoints.get(0).getTrcComplianceReviewPicFollowupPoints().get(idxFollowup)
				.getUploadedFilesEvidence()
				.remove(tableModelFollowupPoints.get(0).getTrcComplianceReviewPicFollowupPoints().get(idxFollowup)
						.getUploadedFilesEvidence().get(index));
		
		PrimeFaces.current().ajax()
			.update("form:dataTableFollowupPoints:0:dataTablePointsPointFollowupEvidence:"+idxFollowup+":evidenceList");
	}

	public void downloadFile(String fileId, String fileName, byte[] content) throws Exception {
		CallApiManager.downloadFile(fileId, fileName, content, parameterDetailService);
	}

	public void handleFileUploadEvidence(FileUploadEvent event) throws Exception {
		try {
			int idx = (int) event.getComponent().getAttributes().get("idxFollowup");
			
			List<UploadedFileWO> uploadedFilesEvidence = tableModelFollowupPoints.get(0)
					.getTrcComplianceReviewPicFollowupPoints().get(idx).getUploadedFilesEvidence();
			
			uploadedFilesEvidence.add(new UploadedFileWO(CallApiManager.callUploadAPI(event.getFile(),
					Constants.COMPLIANCE_DOC_TYPE_PIC_FOLLOWUP, parameterDetailService, false, fileUtil),
					event.getFile().getFileName(), event.getFile().getContentType(), event.getFile().getSize(), true));
			
			PrimeFaces.current().ajax()
				.update("form:dataTableFollowupPoints:0:dataTablePointsPointFollowupEvidence:"+idx+":evidenceList");
		} catch (Exception e) {
			e.printStackTrace();
			facesUtil.addErrMessage(e.getMessage());
		}
	}

	public Boolean validate() {
		Boolean flag = false;
		
		if(tableModelFollowupPoints.get(0).getTrcComplianceReviewPicFollowupPoints() != null)
		{
			boolean dataExist = false;
			for(TrcComplianceReviewPicFollowupPoints t : tableModelFollowupPoints.get(0).getTrcComplianceReviewPicFollowupPoints()) {
				if(t.getUploadedFilesEvidence().size() > 0)
					dataExist = true;
			}
			
			for(TrcComplianceReviewPicFollowupPoints t : tableModelFollowupPoints.get(0).getTrcComplianceReviewPicFollowupPoints()) {
				
				// jika tidak ada yg diupload
				if((t.getUploadedFilesEvidence() == null || t.getUploadedFilesEvidence().size() <= 0) && !dataExist) {
					facesUtil.addErrMessage(facesUtil.retrieveMessage("formPICFollowupConfirmationPICConfirmationEvidence")
							+ " File " + facesUtil.retrieveMessage("validateRequired"));
					flag = true;
					break;
				}
				
			}
			
			for(TrcComplianceReviewPicFollowupPoints t : tableModelFollowupPoints.get(0).getTrcComplianceReviewPicFollowupPoints()) {
				if(t.getUploadedFilesEvidence().size() > 0 && t.getFollowupDate() == null) {
					facesUtil.addErrMessage(facesUtil.retrieveMessage("formTrcComplianceReviewFollowupDate") + " "
							+ facesUtil.retrieveMessage("validateRequired"));
					flag = true;
				}
				
				if(t.getUploadedFilesEvidence().isEmpty() && t.getFollowupDate() != null) {
					facesUtil.addErrMessage(facesUtil.retrieveMessage("formPICFollowupConfirmationPICConfirmationEvidence") + " "
							+ facesUtil.retrieveMessage("validateRequired"));
					flag = true;
				}
			}
			// jika ada yg di upload tetapi tgl pemenuhan surat tidak diisi
//			dataExist = t.getUploadedFilesEvidence().stream().allMatch(evi -> {
//							if(StringUtils.isNotBlank(evi.getFileName()) && t.getFollowupDate() == null)
//								return true;
//							else
//								return false;
//						});
//			for(TrcComplianceReviewPicFollowupPoints t : tableModelFollowupPoints.get(0).getTrcComplianceReviewPicFollowupPoints()) {
//				if(t.getFollowupDate() == null) {
//					facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpComplianceReviewFollowupDate") + " "
//							+ facesUtil.retrieveMessage("validateRequired"));
//					flag = true;
//				}
//					
//				
//			}
		}

		return flag;

	}

	public void save() {
		try {

			if (!validate()) {

				User user = (User) facesUtil.getUserLogin();
				trcComplianceReviewService.update(trcComplianceReview, trcComplianceReviewPicFollowup, user);
//				trcComplianceReviewService.update(trcComplianceReview, trcComplianceReviewPICFollowupId,
//						trcComplianceReviewPicFollowup.getFollowupNote(),
//						trcComplianceReviewPicFollowup.getFollowupDate(), uploadedFilesEvidence, user);

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

				facesUtil.redirect("/pages/trcComplianceReview/trcComplianceReview.faces");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + ex.getMessage(), "");
		}

	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		TrcComplianceReviewEditBean.logger = logger;
	}

	public TrcComplianceReview getTrcComplianceReview() {
		return trcComplianceReview;
	}

	public void setTrcComplianceReview(TrcComplianceReview trcComplianceReview) {
		this.trcComplianceReview = trcComplianceReview;
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

	public Boolean getDisabledFollowUpStatus() {
		return disabledFollowUpStatus;
	}

	public void setDisabledFollowUpStatus(Boolean disabledFollowUpStatus) {
		this.disabledFollowUpStatus = disabledFollowUpStatus;
	}

	public Long getCounterTypeId() {
		return counterTypeId;
	}

	public void setCounterTypeId(Long counterTypeId) {
		this.counterTypeId = counterTypeId;
	}

	public List<SelectItem> getReviewCategoryList() {
		return reviewCategoryList;
	}

	public void setReviewCategoryList(List<SelectItem> reviewCategoryList) {
		this.reviewCategoryList = reviewCategoryList;
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

	public TrcComplianceReviewPicCompliance[] getSelectedDataCompliance() {
		return selectedDataCompliance;
	}

	public void setSelectedDataCompliance(TrcComplianceReviewPicCompliance[] selectedDataCompliance) {
		this.selectedDataCompliance = selectedDataCompliance;
	}

	public TrcComplianceReviewPicFollowup[] getSelectedDataFollowup() {
		return selectedDataFollowup;
	}

	public void setSelectedDataFollowup(TrcComplianceReviewPicFollowup[] selectedDataFollowup) {
		this.selectedDataFollowup = selectedDataFollowup;
	}

	public TrcComplianceReviewPicComplianceTableModel<TrcComplianceReviewPicCompliance> getTableModelCompliance() {
		return tableModelCompliance;
	}

	public void setTableModelCompliance(
			TrcComplianceReviewPicComplianceTableModel<TrcComplianceReviewPicCompliance> tableModelCompliance) {
		this.tableModelCompliance = tableModelCompliance;
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

	public ComplianceReviewService getComplianceReviewService() {
		return complianceReviewService;
	}

	public void setComplianceReviewService(ComplianceReviewService complianceReviewService) {
		this.complianceReviewService = complianceReviewService;
	}

	public TrcComplianceReviewService getTrcComplianceReviewService() {
		return trcComplianceReviewService;
	}

	public void setTrcComplianceReviewService(TrcComplianceReviewService trcComplianceReviewService) {
		this.trcComplianceReviewService = trcComplianceReviewService;
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

	public TrcComplianceReviewPicFollowupService getTrcComplianceReviewPicFollowupService() {
		return trcComplianceReviewPicFollowupService;
	}

	public void setTrcComplianceReviewPicFollowupService(
			TrcComplianceReviewPicFollowupService trcComplianceReviewPicFollowupService) {
		this.trcComplianceReviewPicFollowupService = trcComplianceReviewPicFollowupService;
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

	public Long getTrcComplianceReviewPICFollowupId() {
		return trcComplianceReviewPICFollowupId;
	}

	public void setTrcComplianceReviewPICFollowupId(Long trcComplianceReviewPICFollowupId) {
		this.trcComplianceReviewPICFollowupId = trcComplianceReviewPICFollowupId;
	}

	public SimpleDateFormat getSdf() {
		return sdf;
	}

	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}

	public List<UploadedFileWO> getUploadedFilesDocument() {
		return uploadedFilesDocument;
	}

	public void setUploadedFilesDocument(List<UploadedFileWO> uploadedFilesDocument) {
		this.uploadedFilesDocument = uploadedFilesDocument;
	}

//	public List<UploadedFileWO> getUploadedFilesFollowupPoints() {
//		return uploadedFilesFollowupPoints;
//	}
//
//	public void setUploadedFilesFollowupPoints(List<UploadedFileWO> uploadedFilesFollowupPoints) {
//		this.uploadedFilesFollowupPoints = uploadedFilesFollowupPoints;
//	}

//	public List<UploadedFileWO> getUploadedFilesEvidence() {
//		return uploadedFilesEvidence;
//	}
//
//	public void setUploadedFilesEvidence(List<UploadedFileWO> uploadedFilesEvidence) {
//		this.uploadedFilesEvidence = uploadedFilesEvidence;
//	}

	public List<UploadedFileWO> getDeletedFiles() {
		return deletedFiles;
	}

	public void setDeletedFiles(List<UploadedFileWO> deletedFiles) {
		this.deletedFiles = deletedFiles;
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

	public List<SelectItem> getSearchType() {
		return searchType;
	}

	public void setSearchType(List<SelectItem> searchType) {
		this.searchType = searchType;
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

	public TrcComplianceReviewPicFollowup getTrcComplianceReviewPicFollowup() {
		return trcComplianceReviewPicFollowup;
	}

	public void setTrcComplianceReviewPicFollowup(TrcComplianceReviewPicFollowup trcComplianceReviewPicFollowup) {
		this.trcComplianceReviewPicFollowup = trcComplianceReviewPicFollowup;
	}

	public List<TrcComplianceReviewPicFollowup> getConfirmationTableList() {
		return confirmationTableList;
	}

	public void setConfirmationTableList(List<TrcComplianceReviewPicFollowup> confirmationTableList) {
		this.confirmationTableList = confirmationTableList;
	}

	public List<TrcComplianceReviewPicFollowup> getTableModelFollowupPoints() {
		return tableModelFollowupPoints;
	}

	public void setTableModelFollowupPoints(List<TrcComplianceReviewPicFollowup> tableModelFollowupPoints) {
		this.tableModelFollowupPoints = tableModelFollowupPoints;
	}

}