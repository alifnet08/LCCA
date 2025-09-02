package com.wo.module.trcCorrespondenceAml.bean;

import java.io.IOException;
import java.io.Serializable;
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
import javax.faces.event.ComponentSystemEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.primefaces.event.FileUploadEvent;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.CommonConstants;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.exception.CustomAPIException;
import com.wo.module.common.model.UploadedFileWO;
import com.wo.module.common.util.FileUtil;
import com.wo.module.common.util.MathUtil;
import com.wo.module.common.utility.CallApiManager;
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
import com.wo.module.trcCorrespondence.constant.TrcCorrespondenceConstants;
import com.wo.module.trcCorrespondence.model.TrcCorrespondence;
import com.wo.module.trcCorrespondence.model.TrcCorrespondenceDocument;
import com.wo.module.trcCorrespondence.model.TrcCorrespondencePicCompliance;
import com.wo.module.trcCorrespondence.model.TrcCorrespondencePicComplianceTableModel;
import com.wo.module.trcCorrespondence.model.TrcCorrespondencePicFollowupAttachment;
import com.wo.module.trcCorrespondence.model.TrcCorrespondencePicFollowupAttendance;
import com.wo.module.trcCorrespondence.model.TrcCorrespondencePicFollowupAttendanceTableModel;
import com.wo.module.trcCorrespondence.model.TrcCorrespondenceSupportingUnit;
import com.wo.module.trcCorrespondence.model.TrcCorrespondenceSupportingUnitTableModel;
import com.wo.module.trcCorrespondenceAml.constant.TrcCorrespondenceAmlConstants;
import com.wo.module.trcCorrespondenceAml.service.TrcCorrespondenceAmlService;
import com.wo.module.user.model.Division;
import com.wo.module.user.model.User;
import com.wo.module.user.service.UserService;

public class TrcCorrespondenceAmlEditBean extends CommonBean implements SelectorListener<Object>, Serializable{

	private static final long serialVersionUID = 3969960546891852237L;
	static Logger logger = Logger.getLogger(TrcCorrespondenceAmlEditBean.class);

	private TrcCorrespondence trcCorrespondence;

	private Boolean isViewOnly;

	private String actionMode;

	private String editedId;

	private String dueDateType;

	private List<UploadedFileWO> uploadedFilesEvidence;

	private TrcCorrespondencePicCompliance[] selectedPicComplianceData;
	private TrcCorrespondenceSupportingUnit[] selectedSupportingUnitData;
	
	private TrcCorrespondencePicFollowupAttendance[] selectedPicAttendanceData;

	private SelectorInfo selectorUser1;
	private SelectorInfo selectorUser2;
	private SelectorInfo selectorUser3;
	private SelectorInfo selectorPicCompliance;
	private SelectorInfo selectorPicAttendance;

	private SelectorInfo selectorUserCc1;
	private SelectorInfo selectorUserCc2;
	private SelectorInfo selectorUserCc3;

	private List<UploadedFileWO> deletedFiles;
	private List<UploadedFileWO> uploadedFilesDocument;

	private TrcCorrespondencePicComplianceTableModel<TrcCorrespondencePicCompliance> tablePicComplianceModel;
	private TrcCorrespondenceSupportingUnitTableModel<TrcCorrespondenceSupportingUnit> tableSupportingUnitModel;
	
	private TrcCorrespondencePicFollowupAttendanceTableModel<TrcCorrespondencePicFollowupAttendance> tableAttedanceModel;

	private Integer indexDtlPicCompliance;
	private Integer indexDtlCc;
	
	private Integer indexDtlPicAttendance;
	private Integer lastSequenceOfPicAttendance;

	private List<TrcCorrespondence> trcCorrespondenceList;

	SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
	private TrcCorrespondenceAmlService trcCorrespondenceAmlService;
	private ReportTypeService reportTypeService;
	private CounterTypeService counterTypeService;
	private UserService userService;
	private RegulationMstService regulationMstService;
	private EmailTemplateService emailTemplateService;

	public FacesUtil facesUtil;

	private List<SelectItem> senderCodeList;
	private List<SelectItem> yesNoList;
	private List<SelectItem> counterTypeList;
	private List<SelectItem> complianceStatusList;
	private List<SelectItem> divisionList;

	private List<SelectItem> attendanceList;
	
	private List<SelectItem> reminderStatusList;
	private List<SelectItem> correspondenceTypeCodeList;
	
	private FileUtil fileUtil;
	
	private String viewOnly;

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
		selectorPicAttendance = TrcCorrespondenceAmlConstants.buildSelectorUserAttendace();
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
			
			attendanceList = new ArrayList<SelectItem>();
			List<ParameterDetail> listAttendance = parameterDetailService.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_CODE_ATTENDANCE);
			for (ParameterDetail vo : listAttendance) {
				SelectItem si =  new SelectItem();
				si.setLabel(vo.getName());
				si.setValue(vo.getParameterDtlCode());
				attendanceList.add(si);
			}
			
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
	
	public void handleFileUploadEvidence(FileUploadEvent event) throws Exception {
		try {
		uploadedFilesEvidence = uploadedFilesEvidence == null ? new ArrayList<UploadedFileWO>() : uploadedFilesEvidence;
		uploadedFilesEvidence.add(new UploadedFileWO(CallApiManager.callUploadAPI(event.getFile(),
				Constants.COMPLIANCE_DOC_TYPE_DOKUMEN_TINDAK_LANJUT_DARI_PIC, parameterDetailService, false, fileUtil),
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
		trcCorrespondence = trcCorrespondenceAmlService.findById(idLong);

		trcCorrespondence.setAttendance(new ParameterDetail());
		
		if (trcCorrespondence != null) {
			trcCorrespondenceList = new ArrayList<TrcCorrespondence>();
			trcCorrespondenceList.add(trcCorrespondence);
		}

		if (trcCorrespondence.getUserId1() != null) {
			trcCorrespondence.setUserNameTemp1(trcCorrespondence.getUserId1().getName());
		}

		if (trcCorrespondence.getUserId2() != null) {
			trcCorrespondence.setUserNameTemp2(trcCorrespondence.getUserId2().getName());
		}

		if (trcCorrespondence.getUserId3() != null) {
			trcCorrespondence.setUserNameTemp3(trcCorrespondence.getUserId3().getName());
		}

		if (trcCorrespondence.getTrcCorrespondencePicCompliances() != null) {
			for (int i = 0; i < trcCorrespondence.getTrcCorrespondencePicCompliances().size(); i++) {
				TrcCorrespondencePicCompliance dtl = (TrcCorrespondencePicCompliance) trcCorrespondence
						.getTrcCorrespondencePicCompliances().get(i);
				if (dtl.getUser() != null) {
					dtl.setNikTemp(dtl.getUser().getNik());
					dtl.setNameTemp(dtl.getUser().getName());
					dtl.setEmailTemp(dtl.getUser().getEmail());
				}
			}
		}

		if (trcCorrespondence.getTrcCorrespondenceSupportingUnits() != null) {
			for (int i = 0; i < trcCorrespondence.getTrcCorrespondenceSupportingUnits().size(); i++) {
				TrcCorrespondenceSupportingUnit dtl = (TrcCorrespondenceSupportingUnit) trcCorrespondence
						.getTrcCorrespondenceSupportingUnits().get(i);
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

		for (int i = 0; i < trcCorrespondence.getTrcCorrespondenceDocuments().size(); i++) {
			TrcCorrespondenceDocument ra = trcCorrespondence.getTrcCorrespondenceDocuments().get(i);
			UploadedFileWO uf = new UploadedFileWO();
			uf.setFileId(ra.getFileId());
			uf.setFileName(ra.getAttachmentFile());
			uf.setIsNew(false);
			uf.setFileSize(ra.getFileSize());
			uploadedFilesDocument.add(uf);

		}

		uploadedFilesEvidence = new ArrayList<UploadedFileWO>();

		for (int i = 0; i < trcCorrespondence.getTrcCorrespondencePicFollowupAttachments().size(); i++) {
			TrcCorrespondencePicFollowupAttachment ra = trcCorrespondence.getTrcCorrespondencePicFollowupAttachments()
					.get(i);
			UploadedFileWO uf = new UploadedFileWO();
			uf.setFileId(ra.getFileId());
			uf.setFileName(ra.getAttachmentFile());
			uf.setIsNew(false);
			uf.setFileSize(ra.getFileSize());
			uploadedFilesEvidence.add(uf);
		}
		
		tableSupportingUnitModel = new TrcCorrespondenceSupportingUnitTableModel<TrcCorrespondenceSupportingUnit>(
				trcCorrespondence.getTrcCorrespondenceSupportingUnits());
		tablePicComplianceModel = new TrcCorrespondencePicComplianceTableModel<TrcCorrespondencePicCompliance>(
				trcCorrespondence.getTrcCorrespondencePicCompliances());
		
		tableAttedanceModel = new TrcCorrespondencePicFollowupAttendanceTableModel<TrcCorrespondencePicFollowupAttendance>(
				trcCorrespondence.getTrcCorrespondencePicFollowupAttendance());
		
		if (trcCorrespondence.getFollowupStatus() != null && trcCorrespondence.getFollowupStatus().getParameterDtlCode() != null
				&& trcCorrespondence.getFollowupStatus().getParameterDtlCode().equals(ParameterDetail.PARAM_DET_CODE_PIC_FOLLOWUP_STATUS_PIC_DONE)) {
			viewOnly = "Y";
			
			SimpleDateFormat sdf = new SimpleDateFormat(CommonConstants.INPUT_DATE_FORMAT);
			
			facesUtil.addWarnMessage(
					facesUtil.retrieveMessage("formTmpCorrespondenceNotifConfirmationDone", 
					sdf.format(trcCorrespondence.getTargetDate()),
					trcCorrespondence.getFollowupBy().getName()));
		} else {
			viewOnly = "N";
		}
	}
	
	public Boolean validate() {
		Boolean flag = false;
		
		if(trcCorrespondence.getCorrespondenceCode().getParameterDtlCode().equals(TrcCorrespondenceConstants.PARAM_DETAIL_CORRESPONDEN_TYPE_INVITATION)) {
			if(trcCorrespondence.getAttendance().getParameterDtlCode() == null) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpCorrespondenceAttedance") 
						+ facesUtil.retrieveMessage("validateRequired"));
				flag = true;
			}
			if(trcCorrespondence.getAttendance().getParameterDtlCode().equals(TrcCorrespondenceConstants.PARAM_DETAIL_ATTENDEE_I_ATTEND)) {
				if(trcCorrespondence.getTrcCorrespondencePicFollowupAttendance() == null ||
						trcCorrespondence.getTrcCorrespondencePicFollowupAttendance().size() == 0) {
					facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpCorrespondencePICAttendance") + " "
							+ facesUtil.retrieveMessage("validateRequired"));
					flag = true;
				}else {
					Set<Long> setUserAttendeeTemp = new HashSet<Long>();
					for (int i = 0; i < trcCorrespondence.getTrcCorrespondencePicFollowupAttendance().size(); i++) {
						TrcCorrespondencePicFollowupAttendance dtl = (TrcCorrespondencePicFollowupAttendance) trcCorrespondence
								.getTrcCorrespondencePicFollowupAttendance().get(i);
						
						if(dtl.getUserNIK() == null) {
							facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpCorrespondenceNIK")  + " "  
									+ facesUtil.retrieveMessage("validateRequired"));
							flag = true;
							break;
						}
						if(dtl.getUserId().getUserId() != null ) {
							if(!setUserAttendeeTemp.add(dtl.getUserId().getUserId())) {
								facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpCorrespondenceNIK") + " "
										+ facesUtil.retrieveMessage("errorDuplicate"));
								flag = true;
								break;
							}
						}
					}
				}
			}
			if(trcCorrespondence.getAttendance().getParameterDtlCode().equals(TrcCorrespondenceConstants.PARAM_DETAIL_ATTENDEE_NOT_ATTEND)) {
				if(trcCorrespondence.getFollowupNote() == null || trcCorrespondence.getFollowupNote().equals("")) {
					facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpRmdInformation") + " " 
							+ facesUtil.retrieveMessage("validateRequired"));
					flag = true;
				}
			}
		}else {
			if (trcCorrespondence.getFollowupDate() == null) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpCorrespondenceFollowupDate") + " "
						+ facesUtil.retrieveMessage("validateRequired"));
				flag = true;
			}
			
			if (uploadedFilesEvidence == null || uploadedFilesEvidence.size() <= 0) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpRmdPicConfirmationEvidence") + " File "
						+ facesUtil.retrieveMessage("validateRequired"));
				flag = true;
			}
		}

		return flag;
	}
	
	public void sendEmail() {
		try {
			
			EmailTemplate emailTemplate = emailTemplateService.getEmailTemplateByEmailTemplateCode("EMAIL_PIC_COMPLIANCE");
			String emailSubject = emailTemplate.getEmailSubject();
	
			String emailContent = ""; 
			String emailTo  = "";
			String emailCc = "";
		    
			emailContent = emailTemplate.getEmailContent().replace(Constants.CONFIRMATION_TYPE_AND_DOC_NUM, "Korespondensi" + " - " + trcCorrespondence.getLetterNo());
			emailSubject = emailSubject.replace(Constants.CONFIRMATION_TYPE_AND_DOC_NUM, "Korespondensi" + " - " + trcCorrespondence.getLetterNo());
					
					for(int x=0;x<trcCorrespondence.getTrcCorrespondencePicCompliances().size();x++) {
						TrcCorrespondencePicCompliance cd = trcCorrespondence.getTrcCorrespondencePicCompliances().get(x);
						emailTo = cd.getUser().getEmail();
						
						final String subject = emailSubject;
						final String content = emailContent;
						final String to = emailTo;
						//final String to = "h3ndr407@gmail.com";
						final String cc = emailCc;
						
						CallApiManager.sendEmailAPI(to, cc, subject, content, "EMAIL_SOSIALISASI", "true", parameterDetailService);
						
					}
		    
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new CustomAPIException(ex.getMessage()); 
		}
		
	}
	
	public void save() {
		try {

			if (!validate()) {
				if (trcCorrespondence.getCorrespondenceId() != null) {
					if(uploadedFilesEvidence != null) {
						trcCorrespondence.getTrcCorrespondencePicFollowupAttachments().clear();
						for (int i = 0; i < uploadedFilesEvidence.size(); i++) {
							TrcCorrespondencePicFollowupAttachment evidence = new TrcCorrespondencePicFollowupAttachment();
							UploadedFileWO uf = (UploadedFileWO) uploadedFilesEvidence.get(i);
							evidence.setTrcCorrespondence(trcCorrespondence);
	
							evidence.setAttachmentFile(uf.getFileName());
							evidence.setCreatedBy(facesUtil.retrieveUserLogin());
							evidence.setCreationDate(new Timestamp(new Date().getTime()));
							evidence.setDelId(new Long(0));
							evidence.setEnabledFlag(Constants.CONSTANT_YES);
							evidence.setFileId(uf.getFileId());
							evidence.setFileSize(uf.getFileSize());
							/*
							 * if(uf.getContents()!=null) {
							 * evidence.setFileId(CallApiManager.callUploadAPI(uf,
							 * Constants.COMPLIANCE_DOC_TYPE_DOKUMEN_TINDAK_LANJUT_DARI_PIC,
							 * parameterDetailService)); }else { evidence.setFileId(uf.getContentType()); }
							 */						
							trcCorrespondence.getTrcCorrespondencePicFollowupAttachments().add(evidence);
						}
					}
					
					if(trcCorrespondence.getTrcCorrespondencePicFollowupAttendance() != null) {
						for (int i = 0; i < trcCorrespondence.getTrcCorrespondencePicFollowupAttendance().size(); i++) {
							TrcCorrespondencePicFollowupAttendance dtl = (TrcCorrespondencePicFollowupAttendance) trcCorrespondence
									.getTrcCorrespondencePicFollowupAttendance().get(i);
							
							dtl.setTrcCorrespondence(trcCorrespondence);
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
					
					trcCorrespondence.setConfirmationDate(new Date());
					
					if(trcCorrespondence.getCorrespondenceCode().getParameterDtlCode().equals(TrcCorrespondenceConstants.PARAM_DETAIL_CORRESPONDEN_TYPE_INVITATION)) {
						if(  trcCorrespondence.getAttendance().getParameterDtlCode().equals(ParameterDetail.PARAM_DET_I_ATTEND)) {
							ParameterDetail attendee = parameterDetailService.getParameterDetailByParamDtlCode(
									ParameterDetail.PARAM_DET_I_ATTEND);
							trcCorrespondence.setFollowupStatus(attendee);
						}else {
							ParameterDetail notAttendee = parameterDetailService.getParameterDetailByParamDtlCode(
									ParameterDetail.PARAM_DET_NOT_ATTEND);
							trcCorrespondence.setFollowupStatus(notAttendee);
						}
					}else {
						ParameterDetail followupStatus = parameterDetailService.getParameterDetailByParamDtlCode(
								ParameterDetail.PARAM_DET_CODE_PIC_FOLLOWUP_STATUS_PIC_DONE);
						trcCorrespondence.setFollowupStatus(followupStatus);
					}
					
					trcCorrespondence.setFollowupBy(userService.getUserByNik(facesUtil.retrieveUserLogin()));
					
					ParameterDetail complianceStatusClose = parameterDetailService.getParameterDetailByParamDtlCode(
							ParameterDetail.PARAM_DET_CODE_COMPLIANCE_CHECK_STATUS_COMPLIANCE_CLOSE);
					
					Date currentDate = new Date();
					
					if(trcCorrespondence.getCorrespondenceCode().getParameterDtlCode().equals(TrcCorrespondenceConstants.PARAM_DETAIL_CORRESPONDEN_TYPE_INVITATION)) {
						trcCorrespondence.setComplianceStatus(complianceStatusClose);
						trcCorrespondence.setFollowupDate(currentDate);
					}else {
						trcCorrespondence.setComplianceStatus(null);
					}
					trcCorrespondence.setComplianceNote(null);
					trcCorrespondence.setComplianceBy(null);
					trcCorrespondence.setComplianceDate(null);

					trcCorrespondence.setLastUpdateBy(facesUtil.retrieveUserLogin());
					trcCorrespondence.setLastUpdateDate(new Timestamp(new Date().getTime()));
					trcCorrespondence.setDelId(new Long(0));
					trcCorrespondence.setEnabledFlag(Constants.CONSTANT_YES);
					trcCorrespondenceAmlService.update(trcCorrespondence);
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

				facesUtil.redirect("/pages/trcCorrespondenceAml/trcCorrespondenceAml.faces");
			}

		} catch (Exception ex) {
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + ex.getMessage(), "");
		}

	}
	
	public void cancel() {
		try {
			if(uploadedFilesDocument != null) {
				for (int i = 0; i < uploadedFilesDocument.size(); i++) {
					UploadedFileWO uf = (UploadedFileWO) uploadedFilesDocument.get(i);
					if(uf.getIsNew() == null) {
						CallApiManager.deleteFile(uf.getFileId(), parameterDetailService, fileUtil);
					}
				}
			}
			facesUtil.redirect("/pages/trcCorrespondenceAml/trcCorrespondenceAml.faces");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void deleteAttachment(String fileId,int index,String uploadType) throws Exception {
		deletedFiles = deletedFiles!=null?deletedFiles: new ArrayList<UploadedFileWO>();
		deletedFiles.add(new UploadedFileWO(fileId,null,null, null));
		if(uploadType!=null && uploadType.equals(TrcCorrespondenceConstants.UPLOAD_TYPE_DOCUMENT)) {
			uploadedFilesDocument.remove(uploadedFilesDocument.get(index));
		}else if(uploadType!=null && uploadType.equals(TrcCorrespondenceConstants.UPLOAD_TYPE_EVIDENCE)) {
			uploadedFilesEvidence.remove(uploadedFilesEvidence.get(index));
		}
	}
	
	public void downloadFile(String fileId, String fileName, byte[] content) throws Exception {
		CallApiManager.downloadFile(fileId, fileName, content, parameterDetailService);
	}

	public void onDeleteRowPicAttendance() {
		for (int i = 0; i < selectedPicAttendanceData.length; i++) {
			trcCorrespondence.getTrcCorrespondencePicFollowupAttendance().remove(selectedPicAttendanceData[i]);
		}
		
		if (trcCorrespondence.getTrcCorrespondencePicFollowupAttendance() == null
				|| trcCorrespondence.getTrcCorrespondencePicFollowupAttendance().size() == 0) {
			lastSequenceOfPicAttendance = 0;
		}
		
		tableAttedanceModel.setWrappedData(trcCorrespondence.getTrcCorrespondencePicFollowupAttendance());
	}
	
	public void onAddNewPicAttendance() {
		if (trcCorrespondence.getTrcCorrespondencePicFollowupAttendance() == null) {
			trcCorrespondence.setTrcCorrespondencePicFollowupAttendance(new ArrayList<TrcCorrespondencePicFollowupAttendance>());
			lastSequenceOfPicAttendance = 0;
		} else {
			if(trcCorrespondence.getTrcCorrespondencePicFollowupAttendance().size() == 0) {
				lastSequenceOfPicAttendance = 0;
			}			
		} 
		
		TrcCorrespondencePicFollowupAttendance d = new TrcCorrespondencePicFollowupAttendance();
		lastSequenceOfPicAttendance = lastSequenceOfPicAttendance + 1;
		d.setSequence(lastSequenceOfPicAttendance);
		trcCorrespondence.getTrcCorrespondencePicFollowupAttendance().add(d);
		tableAttedanceModel.setWrappedData(trcCorrespondence.getTrcCorrespondencePicFollowupAttendance());

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

	public String getDueDateType() {
		return dueDateType;
	}

	public void setDueDateType(String dueDateType) {
		this.dueDateType = dueDateType;
	}

	public List<UploadedFileWO> getUploadedFilesEvidence() {
		return uploadedFilesEvidence;
	}

	public void setUploadedFilesEvidence(List<UploadedFileWO> uploadedFilesEvidence) {
		this.uploadedFilesEvidence = uploadedFilesEvidence;
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

	public TrcCorrespondencePicFollowupAttendance[] getSelectedPicAttendanceData() {
		return selectedPicAttendanceData;
	}

	public void setSelectedPicAttendanceData(TrcCorrespondencePicFollowupAttendance[] selectedPicAttendanceData) {
		this.selectedPicAttendanceData = selectedPicAttendanceData;
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

	public SelectorInfo getSelectorPicCompliance() {
		return selectorPicCompliance;
	}

	public void setSelectorPicCompliance(SelectorInfo selectorPicCompliance) {
		this.selectorPicCompliance = selectorPicCompliance;
	}

	public SelectorInfo getSelectorPicAttendance() {
		return selectorPicAttendance;
	}

	public void setSelectorPicAttendance(SelectorInfo selectorPicAttendance) {
		this.selectorPicAttendance = selectorPicAttendance;
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

	public List<UploadedFileWO> getDeletedFiles() {
		return deletedFiles;
	}

	public void setDeletedFiles(List<UploadedFileWO> deletedFiles) {
		this.deletedFiles = deletedFiles;
	}

	public List<UploadedFileWO> getUploadedFilesDocument() {
		return uploadedFilesDocument;
	}

	public void setUploadedFilesDocument(List<UploadedFileWO> uploadedFilesDocument) {
		this.uploadedFilesDocument = uploadedFilesDocument;
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

	public TrcCorrespondencePicFollowupAttendanceTableModel<TrcCorrespondencePicFollowupAttendance> getTableAttedanceModel() {
		return tableAttedanceModel;
	}

	public void setTableAttedanceModel(
			TrcCorrespondencePicFollowupAttendanceTableModel<TrcCorrespondencePicFollowupAttendance> tableAttedanceModel) {
		this.tableAttedanceModel = tableAttedanceModel;
	}

	public Integer getIndexDtlPicCompliance() {
		return indexDtlPicCompliance;
	}

	public void setIndexDtlPicCompliance(Integer indexDtlPicCompliance) {
		this.indexDtlPicCompliance = indexDtlPicCompliance;
	}

	public Integer getIndexDtlCc() {
		return indexDtlCc;
	}

	public void setIndexDtlCc(Integer indexDtlCc) {
		this.indexDtlCc = indexDtlCc;
	}

	public Integer getIndexDtlPicAttendance() {
		return indexDtlPicAttendance;
	}

	public void setIndexDtlPicAttendance(Integer indexDtlPicAttendance) {
		this.indexDtlPicAttendance = indexDtlPicAttendance;
	}

	public Integer getLastSequenceOfPicAttendance() {
		return lastSequenceOfPicAttendance;
	}

	public void setLastSequenceOfPicAttendance(Integer lastSequenceOfPicAttendance) {
		this.lastSequenceOfPicAttendance = lastSequenceOfPicAttendance;
	}

	public List<TrcCorrespondence> getTrcCorrespondenceList() {
		return trcCorrespondenceList;
	}

	public void setTrcCorrespondenceList(List<TrcCorrespondence> trcCorrespondenceList) {
		this.trcCorrespondenceList = trcCorrespondenceList;
	}

	public SimpleDateFormat getSdf() {
		return sdf;
	}

	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}

	public TrcCorrespondenceAmlService getTrcCorrespondenceAmlService() {
		return trcCorrespondenceAmlService;
	}

	public void setTrcCorrespondenceAmlService(TrcCorrespondenceAmlService trcCorrespondenceAmlService) {
		this.trcCorrespondenceAmlService = trcCorrespondenceAmlService;
	}

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

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public RegulationMstService getRegulationMstService() {
		return regulationMstService;
	}

	public void setRegulationMstService(RegulationMstService regulationMstService) {
		this.regulationMstService = regulationMstService;
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

	public List<SelectItem> getCounterTypeList() {
		return counterTypeList;
	}

	public void setCounterTypeList(List<SelectItem> counterTypeList) {
		this.counterTypeList = counterTypeList;
	}

	public List<SelectItem> getComplianceStatusList() {
		return complianceStatusList;
	}

	public void setComplianceStatusList(List<SelectItem> complianceStatusList) {
		this.complianceStatusList = complianceStatusList;
	}

	public List<SelectItem> getDivisionList() {
		return divisionList;
	}

	public void setDivisionList(List<SelectItem> divisionList) {
		this.divisionList = divisionList;
	}

	public List<SelectItem> getAttendanceList() {
		return attendanceList;
	}

	public void setAttendanceList(List<SelectItem> attendanceList) {
		this.attendanceList = attendanceList;
	}

	public List<SelectItem> getReminderStatusList() {
		return reminderStatusList;
	}

	public void setReminderStatusList(List<SelectItem> reminderStatusList) {
		this.reminderStatusList = reminderStatusList;
	}

	public List<SelectItem> getCorrespondenceTypeCodeList() {
		return correspondenceTypeCodeList;
	}

	public void setCorrespondenceTypeCodeList(List<SelectItem> correspondenceTypeCodeList) {
		this.correspondenceTypeCodeList = correspondenceTypeCodeList;
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

	
	
	@Override
	public void itemSelected(String clientId, String widgetVar, Object selectedItem) {
		if (StringUtils.equals("picAttendanceDialog", widgetVar)) {
			Object[] objects = (Object[]) selectedItem;
			//User userPicCompliance = userService.findById(((BigInteger) objects[0]).longValue());
			User userPicAttendee = userService.findById(MathUtil.returnIdObjectToLong(objects[0]));
			if (userPicAttendee != null) {
				trcCorrespondence.getTrcCorrespondencePicFollowupAttendance().get(indexDtlPicAttendance)
						.setUserId(userPicAttendee);
				trcCorrespondence.getTrcCorrespondencePicFollowupAttendance().get(indexDtlPicAttendance)
						.setUserNIK(userPicAttendee.getNik());
				trcCorrespondence.getTrcCorrespondencePicFollowupAttendance().get(indexDtlPicAttendance)
						.setUserName(userPicAttendee.getName());
				trcCorrespondence.getTrcCorrespondencePicFollowupAttendance().get(indexDtlPicAttendance)
						.setUserEmail(userPicAttendee.getEmail());

				tableAttedanceModel.setWrappedData(trcCorrespondence.getTrcCorrespondencePicFollowupAttendance());
			}
		}
	}

	public String getTextWarningUpload() {
		return textWarningUpload;
	}

	public void setTextWarningUpload(String textWarningUpload) {
		this.textWarningUpload = textWarningUpload;
	}

}
