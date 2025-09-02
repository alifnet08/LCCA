package com.wo.module.auditFE.bean;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.primefaces.event.FileUploadEvent;

import com.wo.module.auditFE.constant.AuditFEConstants;
import com.wo.module.auditFE.service.AuditFEService;
import com.wo.module.auditFE.vo.AuditFEVO;
import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.CommonConstants;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.exception.CustomAPIException;
import com.wo.module.common.model.ColumnModel;
import com.wo.module.common.model.UploadedFileWO;
import com.wo.module.common.util.FileUtil;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.complianceTestingMockup.model.ComplianceTestingPICFollowupAttachment;
import com.wo.module.complianceTestingMockup.model.ComplianceTestingPICFollowupExt;
import com.wo.module.emailTemplate.model.EmailTemplate;
import com.wo.module.emailTemplate.service.EmailTemplateService;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.trcAudit.model.TrcAudit;
import com.wo.module.trcAudit.model.TrcAuditPicCompliance;
import com.wo.module.trcAudit.model.TrcAuditPicFollowup;
import com.wo.module.trcAudit.model.TrcAuditPicFollowupAttachment;
import com.wo.module.trcAudit.model.TrcAuditPicFollowupExt;
import com.wo.module.trcAudit.model.TrcAuditPicFollowupRec;
import com.wo.module.trcAudit.service.TrcAuditPICFollowupService;
import com.wo.module.trcAudit.service.TrcAuditService;
import com.wo.module.user.model.User;
import com.wo.module.user.service.UserService;

public class AuditFEEditBean extends CommonBean implements Serializable, AuditFEConstants {

	private static final long serialVersionUID = -7626645263134025560L;

	static Logger logger = Logger.getLogger(AuditFEEditBean.class);

	private AuditFEVO auditFEVO;
	private TrcAudit trcAudit;
	private TrcAuditPicFollowup trcAuditPicFollowup;
	private Long trcAuditPicFollowupId;

	private Boolean isViewOnly;
	private String viewOnly;

	private List<UploadedFileWO> uploadedFiles;
	private List<UploadedFileWO> uploadedFilesExt;
	private List<UploadedFileWO> deletedFiles;
	private List<UploadedFileWO> uploadedFilesAttachmentLetter;

	private FileUtil fileUtil;
	public FacesUtil facesUtil;

	// services
	private TrcAuditService trcAuditService;
	private UserService userService;
	private TrcAuditPICFollowupService trcAuditPICFollowupService;
	private EmailTemplateService emailTemplateService;
	private AuditFEService auditFEService;

	private String note;
	private String textWarningUpload;
	private Date newTargetDate;

	SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
	
	private Integer testFirst;
	
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
		testFirst = Integer.parseInt(facesUtil.retrieveRequestParam("first")!=null?facesUtil.retrieveRequestParam("first"):"0");
		facesUtil.setSessionAttribute("FIRST_AUDIT_FE", testFirst);
		facesUtil.setSessionAttribute("BACK_SESSION", false);
		fileUtil = FileUtil.getInstance();

		handleEdit();
		
		try {
			ParameterDetail getText = parameterDetailService.getParameterDetailByParamDtlCode("UPLOAD_WARNING_TEXT");
			textWarningUpload = getText.getName();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void onSelect() {
		System.out.println("isSelect=="+trcAuditPicFollowup.getIsExtension());
	}

	private void handleEdit() {
		String editId = facesUtil.retrieveRequestParam("id");
		String token = facesUtil.retrieveRequestParam("token");
		if (StringUtils.isNotEmpty(token)) {
			editId = Constants.decryptString(token);
		}
		facesUtil.setSessionAttribute("token", Constants.encryptString(editId));
		Long idLong = Long.parseLong(editId);
		trcAuditPicFollowupId = idLong;
		trcAuditPicFollowup = trcAuditPICFollowupService.findById(idLong);
		
		trcAudit = trcAuditPicFollowup.getTrcAuditPicFollowupBankCommitment().getTrcAuditCheckPoint().getTrcAudit();
		
		auditFEVO = auditFEService.searchForDetail(idLong);
		
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

		uploadedFiles = new ArrayList<UploadedFileWO>();
		if(trcAuditPicFollowup.getTrcAuditPicFollowupAttachment() != null && !trcAuditPicFollowup.getTrcAuditPicFollowupAttachment().isEmpty())
		{
			trcAuditPicFollowup.getTrcAuditPicFollowupAttachment().forEach(attDoc -> {
				UploadedFileWO uf = new UploadedFileWO();
				uf.setFileName(attDoc.getAttachmentFile());
				uf.setFileId(attDoc.getFileId());
				uf.setIsNew(false);
				uf.setFileSize(attDoc.getFileSize());
				uploadedFiles.add(uf);
			});
		}
		
		uploadedFilesExt = new ArrayList<UploadedFileWO>();
		for(int i=0;i<trcAuditPicFollowup.getTrcAuditPicFollowupExt().size();i++) {
			TrcAuditPicFollowupExt attach = trcAuditPicFollowup.getTrcAuditPicFollowupExt().get(i);
			UploadedFileWO uf = new UploadedFileWO();
			uf.setFileId(attach.getFileId());
			uf.setFileName(attach.getAttachmentFile());
			uf.setFileSize(attach.getFileSize());
			if(!uploadedFilesExt.isEmpty()) {
				uploadedFilesExt.clear();
			}
			newTargetDate = attach.getNewTargetDate();
			uploadedFilesExt.add(uf);
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
		}
	}
	
	public void handleFileUploadExt(FileUploadEvent event) throws Exception {
		try {
			uploadedFilesExt = uploadedFilesExt == null ? new ArrayList<UploadedFileWO>() : uploadedFilesExt;
			if(trcAuditPicFollowup.getIsExtension().equals("Y")) {
				uploadedFiles.clear();
			}
			uploadedFilesExt.add(new UploadedFileWO(CallApiManager.callUploadAPI(event.getFile(),
				Constants.COMPLIANCE_DOC_TYPE_AUDIT_FOLLOWUP, parameterDetailService, false, fileUtil),
				event.getFile().getFileName(), event.getFile().getContentType(), event.getFile().getSize()));
		} catch (Exception e) {
			e.printStackTrace();
			facesUtil.addErrMessage(e.getMessage());
		}
	}
	
	
	public void handleFileUpload(FileUploadEvent event) throws Exception {
		try {
			
			uploadedFiles = uploadedFiles == null ? new ArrayList<UploadedFileWO>() : uploadedFiles;

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
		
		uploadedFiles.remove(uploadedFiles.get(index));
	}

	public void cancel() {
		try {
			facesUtil.setSessionAttribute("BACK_SESSION", true);
			facesUtil.redirect("/pages/auditFE/auditFE.faces");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void save() {
		try {
			
			//String tglTindakLanjut = facesUtil.retrieveRequestParam("tglTindakLanjut");
			String keterangan = facesUtil.retrieveRequestParam("keterangan");
			
			/*SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			if(!StringUtils.isEmpty(tglTindakLanjut)){
				trcAuditPicFollowup.setFollowupDate(sdf.parse(tglTindakLanjut));
				
			}*/
			
			if(!StringUtils.isEmpty(keterangan)){
				trcAuditPicFollowup.setFollowupNote(keterangan);
			}
			
			if (!isError()) {
				User user = (User) facesUtil.getUserLogin();

				//trcAuditService.processConfirm(trcAuditPicFollowup, uploadedFiles, uploadedFilesAttachmentLetter, user);
				
				if(trcAuditPicFollowup.getTrcAuditPicFollowupRecs()!=null && trcAuditPicFollowup.getTrcAuditPicFollowupRec() == null) {
					TrcAuditPicFollowupRec trcAuditPicFollowupRec = trcAuditPicFollowup.getTrcAuditPicFollowupRecs().stream().filter(e->e.getFollowupDate() == null).findFirst().orElse(null);
					if(trcAuditPicFollowupRec!=null) {
						trcAuditPicFollowup.setTrcAuditPicFollowupRec(trcAuditPicFollowupRec);
					}
				}
				System.out.println("trcAuditPicFollowup.getIsExtension()=="+trcAuditPicFollowup.getIsExtension());
				if(trcAuditPicFollowup.getIsExtension().equals("N")) {
					
					if(uploadedFiles != null) {
						System.out.println("uploadedFiles=="+uploadedFiles.size());
						if(trcAuditPicFollowup.getTrcAuditPicFollowupAttachment() == null) {
							trcAuditPicFollowup.setTrcAuditPicFollowupAttachment(new ArrayList<TrcAuditPicFollowupAttachment>());
						}else {
							trcAuditPicFollowup.getTrcAuditPicFollowupAttachment().clear();
						}
						for (int i = 0; i < uploadedFiles.size(); i++) {
							TrcAuditPicFollowupAttachment evidence = new TrcAuditPicFollowupAttachment();
							UploadedFileWO uf = (UploadedFileWO) uploadedFiles.get(i);
							evidence.setTrcAuditPicFollowup(trcAuditPicFollowup);

							evidence.setAttachmentFile(uf.getFileName());
							evidence.setCreatedBy(facesUtil.retrieveUserLogin());
							evidence.setCreationDate(new Timestamp(new Date().getTime()));
							evidence.setDelId(new Long(0));
							evidence.setEnabledFlag(Constants.CONSTANT_YES);
							evidence.setFileId(uf.getFileId());
							evidence.setFileSize(uf.getFileSize());
											
							trcAuditPicFollowup.getTrcAuditPicFollowupAttachment().add(evidence);
						}
					}
					
					trcAuditPicFollowup.setConfirmationDate(new Date());
					trcAuditPicFollowup.setFollowupDate(new Date());
					
					ParameterDetail followupStatus = parameterDetailService.getParameterDetailByParamDtlCode(
							ParameterDetail.PARAM_DET_CODE_PIC_FOLLOWUP_STATUS_PIC_DONE);
					
					trcAuditPicFollowup.setFollowupStatus(followupStatus);
					
					trcAuditPicFollowup.setFollowupBy(userService.getUserByNik(facesUtil.retrieveUserLogin()));
					
					trcAuditPicFollowup.setComplianceNote(null);
					trcAuditPicFollowup.setComplianceBy(null);
					trcAuditPicFollowup.setComplianceDate(null);
					
					
				}else {
					if(uploadedFilesExt != null) {
						if(trcAuditPicFollowup.getTrcAuditPicFollowupExt() == null) {
							trcAuditPicFollowup.setTrcAuditPicFollowupExt(new ArrayList<TrcAuditPicFollowupExt>());
						}
						for (int i = 0; i < uploadedFilesExt.size(); i++) {
							TrcAuditPicFollowupExt evidence = new TrcAuditPicFollowupExt();
							UploadedFileWO uf = (UploadedFileWO) uploadedFilesExt.get(i);
							evidence.setTrcAuditPicFollowup(trcAuditPicFollowup);
							evidence.setOldTargetDate(trcAuditPicFollowup.getTargetDate());
							evidence.setNewTargetDate(newTargetDate);
							evidence.setAttachmentFile(uf.getFileName());
							evidence.setCreatedBy(facesUtil.retrieveUserLogin());
							evidence.setCreationDate(new Timestamp(new Date().getTime()));
							evidence.setDelId(new Long(0));
							evidence.setEnabledFlag(Constants.CONSTANT_YES);
							evidence.setFileId(uf.getFileId());
							evidence.setFileSize(uf.getFileSize());
											
							trcAuditPicFollowup.getTrcAuditPicFollowupExt().add(evidence);
						}
					}
					
					ParameterDetail followupStatus = parameterDetailService.getParameterDetailByParamDtlCode(
							ParameterDetail.PARAM_DET_CODE_PIC_FOLLOWUP_STATUS_PIC_EXTENSION);
					trcAuditPicFollowup.setFollowupStatus(followupStatus);
					trcAuditPicFollowup.setFollowupBy(userService.getUserByNik(facesUtil.retrieveUserLogin()));
				}

				if(trcAuditPicFollowup.getTrcAuditPicFollowupRec()!=null) {
					trcAuditPicFollowup.getTrcAuditPicFollowupRec().setFollowupDate(trcAuditPicFollowup.getFollowupDate());
					trcAuditPicFollowup.getTrcAuditPicFollowupRec().setFollowupStatus(trcAuditPicFollowup.getFollowupStatus());
					trcAuditPicFollowup.getTrcAuditPicFollowupRec().setFollowupBy(trcAuditPicFollowup.getFollowupBy());
					trcAuditPicFollowup.getTrcAuditPicFollowupRec().setFollowupNote(trcAuditPicFollowup.getFollowupNote());
					trcAuditPicFollowup.getTrcAuditPicFollowupRec().setConfirmationDate(trcAuditPicFollowup.getConfirmationDate());
					trcAuditPicFollowup.getTrcAuditPicFollowupRec().setComplianceNote(trcAuditPicFollowup.getComplianceNote());
					trcAuditPicFollowup.getTrcAuditPicFollowupRec().setComplianceBy(trcAuditPicFollowup.getComplianceBy());
					trcAuditPicFollowup.getTrcAuditPicFollowupRec().setComplianceDate(trcAuditPicFollowup.getComplianceDate());
					trcAuditPicFollowup.getTrcAuditPicFollowupRec().setLastUpdateBy(facesUtil.retrieveUserLogin());
					trcAuditPicFollowup.getTrcAuditPicFollowupRec().setLastUpdateDate(new Timestamp(new Date().getTime()));
				}
				
				trcAuditPicFollowup.setLastUpdateBy(facesUtil.retrieveUserLogin());
				trcAuditPicFollowup.setLastUpdateDate(new Timestamp(new Date().getTime()));
				trcAuditPicFollowup.setDelId(new Long(0));
				trcAuditPicFollowup.setEnabledFlag(Constants.CONSTANT_YES);
				trcAuditPICFollowupService.update(trcAuditPicFollowup);
				
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

				facesUtil.redirect("/pages/auditFE/auditFE.faces");
			}

		} catch (Exception ex) {
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + ex.getMessage(), "");
			ex.printStackTrace();
		}
	}
	
	public Boolean isError() {
		Boolean flag = false;
		
		if(trcAuditPicFollowup.getIsExtension().equals("N")) {
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
			
			if (trcAuditPicFollowup.getFollowupNote() == null) {
				facesUtil.addErrMessage("Penjelasan Tindak Lanjut  "
						+ facesUtil.retrieveMessage("validateRequired"));
				flag = true;
			}
		}else {
			
			if (trcAuditPicFollowup.getRescheduleReason() == null) {
				facesUtil.addErrMessage("Alasan Perubahan Target "
						+ facesUtil.retrieveMessage("validateRequired"));
				flag = true;
			}
			if (newTargetDate == null) {
				facesUtil.addErrMessage("Perpanjangan Target Date "
						+ facesUtil.retrieveMessage("validateRequired"));
				flag = true;
			}
			
			if (uploadedFilesExt == null || uploadedFilesExt.size() <= 0) {
				facesUtil.addErrMessage(" File Lampiran "
						+ facesUtil.retrieveMessage("validateRequired"));
				flag = true;
			}
		}

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
			
			auditorNameTemp = paramAuditorTemp.getNameIn() != null ? " - " + paramAuditorTemp.getNameIn() : "";
			
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

				CallApiManager.sendEmailAPI(to, cc, subject, content, "EMAIL_PIC_COMPLIANCE", "true",
						parameterDetailService);

			}

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new CustomAPIException(ex.getMessage()); 
		}

	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		AuditFEEditBean.logger = logger;
	}

	public AuditFEVO getAuditFEVO() {
		return auditFEVO;
	}

	public void setAuditFEVO(AuditFEVO auditFEVO) {
		this.auditFEVO = auditFEVO;
	}

	public TrcAudit getTrcAudit() {
		return trcAudit;
	}

	public void setTrcAudit(TrcAudit trcAudit) {
		this.trcAudit = trcAudit;
	}

	public TrcAuditPicFollowup getTrcAuditPicFollowup() {
		return trcAuditPicFollowup;
	}

	public void setTrcAuditPicFollowup(TrcAuditPicFollowup trcAuditPicFollowup) {
		this.trcAuditPicFollowup = trcAuditPicFollowup;
	}

	public Long getTrcAuditPicFollowupId() {
		return trcAuditPicFollowupId;
	}

	public void setTrcAuditPicFollowupId(Long trcAuditPicFollowupId) {
		this.trcAuditPicFollowupId = trcAuditPicFollowupId;
	}

	public Boolean getIsViewOnly() {
		return isViewOnly;
	}

	public void setIsViewOnly(Boolean isViewOnly) {
		this.isViewOnly = isViewOnly;
	}

	public String getViewOnly() {
		return viewOnly;
	}

	public void setViewOnly(String viewOnly) {
		this.viewOnly = viewOnly;
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

	public FileUtil getFileUtil() {
		return fileUtil;
	}

	public void setFileUtil(FileUtil fileUtil) {
		this.fileUtil = fileUtil;
	}

	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

	public TrcAuditService getTrcAuditService() {
		return trcAuditService;
	}

	public void setTrcAuditService(TrcAuditService trcAuditService) {
		this.trcAuditService = trcAuditService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public TrcAuditPICFollowupService getTrcAuditPICFollowupService() {
		return trcAuditPICFollowupService;
	}

	public void setTrcAuditPICFollowupService(TrcAuditPICFollowupService trcAuditPICFollowupService) {
		this.trcAuditPICFollowupService = trcAuditPICFollowupService;
	}

	public EmailTemplateService getEmailTemplateService() {
		return emailTemplateService;
	}

	public void setEmailTemplateService(EmailTemplateService emailTemplateService) {
		this.emailTemplateService = emailTemplateService;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public AuditFEService getAuditFEService() {
		return auditFEService;
	}

	public void setAuditFEService(AuditFEService auditFEService) {
		this.auditFEService = auditFEService;
	}

	public List<UploadedFileWO> getUploadedFilesAttachmentLetter() {
		return uploadedFilesAttachmentLetter;
	}

	public void setUploadedFilesAttachmentLetter(List<UploadedFileWO> uploadedFilesAttachmentLetter) {
		this.uploadedFilesAttachmentLetter = uploadedFilesAttachmentLetter;
	}

	public String getTextWarningUpload() {
		return textWarningUpload;
	}

	public void setTextWarningUpload(String textWarningUpload) {
		this.textWarningUpload = textWarningUpload;
	}

	public Integer getTestFirst() {
		return testFirst;
	}

	public void setTestFirst(Integer testFirst) {
		this.testFirst = testFirst;
	}

	public Date getNewTargetDate() {
		return newTargetDate;
	}

	public void setNewTargetDate(Date newTargetDate) {
		this.newTargetDate = newTargetDate;
	}

	public List<UploadedFileWO> getUploadedFilesExt() {
		return uploadedFilesExt;
	}

	public void setUploadedFilesExt(List<UploadedFileWO> uploadedFilesExt) {
		this.uploadedFilesExt = uploadedFilesExt;
	}
	
	
	
}