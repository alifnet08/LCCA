package com.wo.module.complianceTestingFE.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.primefaces.event.FileUploadEvent;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.exception.CustomAPIException;
import com.wo.module.common.model.UploadedFileWO;
import com.wo.module.common.util.FileUtil;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.complianceTestingFE.service.ComplianceTestingFEService;
import com.wo.module.complianceTestingMockup.model.ComplianceTesting;
import com.wo.module.complianceTestingMockup.model.ComplianceTestingPICFollowup;
import com.wo.module.complianceTestingMockup.model.ComplianceTestingPICFollowupAttachment;
import com.wo.module.complianceTestingMockup.model.ComplianceTestingPICFollowupExt;
import com.wo.module.complianceTestingMockup.model.ComplianceTestingPICReview;
import com.wo.module.complianceTestingMockup.service.ComplianceTestingPICFollowupService;
import com.wo.module.complianceTestingMockup.service.ComplianceTestingService;
import com.wo.module.emailTemplate.model.EmailTemplate;
import com.wo.module.emailTemplate.service.EmailTemplateService;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.user.model.User;
import com.wo.module.user.service.UserService;

public class ComplianceTestingFEEditBean extends CommonBean implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(ComplianceTestingFEEditBean.class);

	private ComplianceTestingPICFollowup complianceTestingPICFollowup;
	private ComplianceTesting complianceTesting;

	private FacesUtil facesUtil;
	private FileUtil fileUtil;
	
	private ComplianceTestingFEService  complianceTestingFEService;
	private ComplianceTestingPICFollowupService complianceTestingPICFollowupService;
	private ComplianceTestingService complianceTestingService;
	private UserService userService;
	private EmailTemplateService emailTemplateService;
	
	private List<UploadedFileWO> uploadedFilesExt;
	private List<UploadedFileWO> uploadedFiles;
	private List<UploadedFileWO> deletedFiles;

	private Long complianceTestingPICFollowupId;
	private String viewOnly;
	private String textWarningUpload;
	private Integer testFirst;
	private Date newTargetDate;
	

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
		facesUtil.setSessionAttribute("FIRST_COMP_TEST_FE", testFirst);
		facesUtil.setSessionAttribute("BACK_SESSION", false);
		handleEdit();
		fileUtil = FileUtil.getInstance();
		facesUtil.removeSessionAttribute(Constants.SESSION_NEED_REDIRECT);
		
		try {
			ParameterDetail getText = parameterDetailService.getParameterDetailByParamDtlCode("UPLOAD_WARNING_TEXT");
			textWarningUpload = getText.getName();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void onSelect() {
		System.out.println("isSelect=="+complianceTestingPICFollowup.getIsExtension());
	}
	
	public void downloadFile (String fileId, String fileName, byte[] content) throws Exception {
		CallApiManager.downloadFile(fileId, fileName, content, parameterDetailService);
	}
	
	private void handleEdit() {
		try {
		String editId = facesUtil.retrieveRequestParam("id");
		String token = facesUtil.retrieveRequestParam("token");
		if (StringUtils.isNotEmpty(token)) {
			editId = Constants.decryptString(token);
		}
		facesUtil.setSessionAttribute("token", Constants.encryptString(editId));
		Long idLong = Long.parseLong(editId);
		complianceTestingPICFollowupId = idLong;
		
		complianceTestingPICFollowup = complianceTestingPICFollowupService.findById(idLong);
		complianceTesting = complianceTestingService.findById(complianceTestingPICFollowup.getComplianceTestingDtl().getComplianceTesting().getComplianceTestingId());
		
		if (complianceTestingPICFollowup.getFollowupStatus() != null
				&& complianceTestingPICFollowup.getFollowupStatus() != null
				&& complianceTestingPICFollowup.getFollowupStatus().equals(ParameterDetail.PARAM_DET_CODE_PIC_FOLLOWUP_STATUS_PIC_DONE)) {
			viewOnly = "Y";

			SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", new Locale("id", "ID"));
			
			StringBuilder sbFollowupBy = new StringBuilder();
			
			facesUtil.addWarnMessage(facesUtil.retrieveMessage("formTmpComplianceReviewNotifConfirmationDone",
					sdf.format(complianceTestingPICFollowup.getTargetDate()),
					sbFollowupBy.toString()));

		} else {
			viewOnly = "N";
			uploadedFiles = new ArrayList<UploadedFileWO>();
			for(int i=0;i<complianceTestingPICFollowup.getComplianceTestingPicFollowupAttachs().size();i++) {
				ComplianceTestingPICFollowupAttachment attach = complianceTestingPICFollowup.getComplianceTestingPicFollowupAttachs().get(i);
				UploadedFileWO uf = new UploadedFileWO();
				uf.setFileId(attach.getFileId());
				uf.setFileName(attach.getAttachmentFile());
				uf.setFileSize(attach.getFileSize());
				uploadedFiles.add(uf);
			}
			
			uploadedFilesExt = new ArrayList<UploadedFileWO>();
			for(int i=0;i<complianceTestingPICFollowup.getComplianceTestingPicFollowupExts().size();i++) {
				ComplianceTestingPICFollowupExt attach = complianceTestingPICFollowup.getComplianceTestingPicFollowupExts().get(i);
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
		}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			
			String noDocAssigment = complianceTesting.getInspectionNo() != null ? " - " + complianceTesting.getInspectionNo() : "";

			emailContent = emailTemplate.getEmailContent().replace(Constants.CONFIRMATION_TYPE_AND_DOC_NUM, "Compliance Testing" 
					+ noDocAssigment);
			
			emailSubject = emailSubject.replace(Constants.CONFIRMATION_TYPE_AND_DOC_NUM, "Compliance Testing" 
					+ noDocAssigment);

			for (int x = 0; x < complianceTesting.getComplianceTestingPICReviews().size(); x++) {
				ComplianceTestingPICReview cd = complianceTesting.getComplianceTestingPICReviews().get(x);
				emailTo = cd.getUser().getEmail();

				// ExecutorService emailExecutor =
				// Executors.newCachedThreadPool();

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

	public void deleteAttachment(String fileId, int index) throws Exception {
		deletedFiles = deletedFiles != null ? deletedFiles : new ArrayList<UploadedFileWO>();
		deletedFiles.add(new UploadedFileWO(fileId, null, null, null));
		if(complianceTestingPICFollowup.getIsExtension().equals("N")) {
			UploadedFileWO up = uploadedFiles.stream().filter(a-> a.getFileId().equals(fileId)).findFirst().orElse(null);
			if(up!=null) {
				uploadedFiles.remove(up);
			}
		}else {
			UploadedFileWO up = uploadedFilesExt.stream().filter(a-> a.getFileId().equals(fileId)).findFirst().orElse(null);
			if(up!=null) {
				uploadedFilesExt.remove(up);
			}
		}
	}
	
	public void cancel() {
		try {
			
			if (deletedFiles != null) {
				for (int i = 0; i < deletedFiles.size(); i++) {
					UploadedFileWO uf = (UploadedFileWO) deletedFiles.get(i);
					CallApiManager.deleteFile(uf.getFileId(), parameterDetailService, fileUtil);
				}
			}
			
			facesUtil.setSessionAttribute("BACK_SESSION", true);
			facesUtil.redirect("/pages/complianceTestingFE/complianceTestingFE.faces");
		} catch (Exception ex) {
			ex.printStackTrace();
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + ex.getMessage(), "");
		}
	}
	
	
	public void handleFileUploadEvidence(FileUploadEvent event) throws Exception {
		try {
			uploadedFiles = uploadedFiles == null ? new ArrayList<UploadedFileWO>() : uploadedFiles;
			
			uploadedFiles.add(new UploadedFileWO(CallApiManager.callUploadAPI(event.getFile(),
				Constants.COMPLIANCE_DOC_TYPE_PIC_FOLLOWUP, parameterDetailService, false, fileUtil),
				event.getFile().getFileName(), event.getFile().getContentType(), event.getFile().getSize()));
		} catch (Exception e) {
			e.printStackTrace();
			facesUtil.addErrMessage(e.getMessage());
		}
	}
	
	public void handleFileUploadExt(FileUploadEvent event) throws Exception {
		try {
			uploadedFilesExt = uploadedFilesExt == null ? new ArrayList<UploadedFileWO>() : uploadedFilesExt;
			if(complianceTestingPICFollowup.getIsExtension().equals("Y")) {
				uploadedFiles.clear();
			}
			uploadedFilesExt.add(new UploadedFileWO(CallApiManager.callUploadAPI(event.getFile(),
				Constants.COMPLIANCE_DOC_TYPE_PIC_FOLLOWUP, parameterDetailService, false, fileUtil),
				event.getFile().getFileName(), event.getFile().getContentType(), event.getFile().getSize()));
		} catch (Exception e) {
			e.printStackTrace();
			facesUtil.addErrMessage(e.getMessage());
		}
	}
	
	
	public Boolean validate() {
		Boolean flag = false;
		
		System.out.println("getIsExtension=="+complianceTestingPICFollowup.getIsExtension());
		
		if(complianceTestingPICFollowup.getIsExtension().equals("N")) {
			if (complianceTestingPICFollowup.getFollowupDate() == null) {
				facesUtil.addErrMessage("Tanggal Tindak Lanjut "
						+ facesUtil.retrieveMessage("validateRequired"));
				flag = true;
			}
			
			if (uploadedFiles == null || uploadedFiles.size() <= 0) {
				facesUtil.addErrMessage( "Bukti Tindak Lanjut "
						+ facesUtil.retrieveMessage("validateRequired"));
				flag = true;
			}
		}else {
			System.out.println("Alasan=="+complianceTestingPICFollowup.getRescheduleReason());
			if (complianceTestingPICFollowup.getRescheduleReason() == null) {
				facesUtil.addErrMessage("Alasan Perubahan Target "
						+ facesUtil.retrieveMessage("validateRequired"));
				flag = true;
			}
			if (newTargetDate == null) {
				facesUtil.addErrMessage("Perpanjangan Target Date "
						+ facesUtil.retrieveMessage("validateRequired"));
				flag = true;
			}
		}

		return flag;

	}
	
	public void save() {
		try {

			if (!validate()) {

				User user = (User) facesUtil.getUserLogin();
				
				
				
				if(complianceTestingPICFollowup.getIsExtension().equals("N")) {
					
					if(uploadedFiles != null) {
						if(complianceTestingPICFollowup.getComplianceTestingPicFollowupAttachs() == null) {
							complianceTestingPICFollowup.setComplianceTestingPicFollowupAttachs(new ArrayList<ComplianceTestingPICFollowupAttachment>());
						}else {
							complianceTestingPICFollowup.getComplianceTestingPicFollowupAttachs().clear();
						}
						for (int i = 0; i < uploadedFiles.size(); i++) {
							ComplianceTestingPICFollowupAttachment evidence = new ComplianceTestingPICFollowupAttachment();
							UploadedFileWO uf = (UploadedFileWO) uploadedFiles.get(i);
							evidence.setComplianceTestingPICFollowup(complianceTestingPICFollowup);

							evidence.setAttachmentFile(uf.getFileName());
							evidence.setCreatedBy(facesUtil.retrieveUserLogin());
							evidence.setCreationDate(new Timestamp(new Date().getTime()));
							evidence.setDelId(new Long(0));
							evidence.setEnabledFlag(Constants.CONSTANT_YES);
							evidence.setFileId(uf.getFileId());
							evidence.setFileSize(uf.getFileSize());
											
							complianceTestingPICFollowup.getComplianceTestingPicFollowupAttachs().add(evidence);
						}
					}
					
					complianceTestingPICFollowup.setConfirmationDate(new Date());
					
					//ParameterDetail followupStatus = parameterDetailService.getParameterDetailByParamDtlCode(
							//ParameterDetail.PARAM_DET_CODE_PIC_FOLLOWUP_STATUS_PIC_DONE);
					
					//complianceTestingPICFollowup.setFollowupStatus(followupStatus);
					
					//complianceTestingPICFollowup.setFollowupById(userService.getUserByNik(facesUtil.retrieveUserLogin()).getUserId());
					
					complianceTestingPICFollowup.setComplianceNote(null);
					complianceTestingPICFollowup.setComplianceById(null);
					complianceTestingPICFollowup.setComplianceDate(null);
				}else {
					if(uploadedFilesExt != null) {
						if(complianceTestingPICFollowup.getComplianceTestingPicFollowupExts() == null) {
							complianceTestingPICFollowup.setComplianceTestingPicFollowupExts(new ArrayList<ComplianceTestingPICFollowupExt>());
						}
						for (int i = 0; i < uploadedFilesExt.size(); i++) {
							ComplianceTestingPICFollowupExt evidence = new ComplianceTestingPICFollowupExt();
							UploadedFileWO uf = (UploadedFileWO) uploadedFilesExt.get(i);
							evidence.setComplianceTestingPICFollowup(complianceTestingPICFollowup);
							evidence.setOldTargetDate(complianceTestingPICFollowup.getTargetDate());
							evidence.setNewTargetDate(newTargetDate);
							evidence.setAttachmentFile(uf.getFileName());
							evidence.setCreatedBy(facesUtil.retrieveUserLogin());
							evidence.setCreationDate(new Timestamp(new Date().getTime()));
							evidence.setDelId(new Long(0));
							evidence.setEnabledFlag(Constants.CONSTANT_YES);
							evidence.setFileId(uf.getFileId());
							evidence.setFileSize(uf.getFileSize());
											
							complianceTestingPICFollowup.getComplianceTestingPicFollowupExts().add(evidence);
						}
					}
					
					//ParameterDetail followupStatus = parameterDetailService.getParameterDetailByParamDtlCode(
							//ParameterDetail.PARAM_DET_CODE_PIC_FOLLOWUP_STATUS_PIC_EXTENSION);
					//complianceTestingPICFollowup.setFollowupStatus(followupStatus);
					//complianceTestingPICFollowup.setFollowupById(userService.getUserByNik(facesUtil.retrieveUserLogin()).getUserId());
				}

				complianceTestingPICFollowup.setLastUpdateBy(facesUtil.retrieveUserLogin());
				complianceTestingPICFollowup.setLastUpdateDate(new Timestamp(new Date().getTime()));
				complianceTestingPICFollowup.setDelId(new Long(0));
				complianceTestingPICFollowup.setEnabledFlag(Constants.CONSTANT_YES);
				complianceTestingPICFollowupService.update(complianceTestingPICFollowup);
				
				// this should be a singleton
		        ExecutorService emailExecutor = Executors.newCachedThreadPool();

		        // from you sendEmail() method
		        emailExecutor.execute(new Runnable() {
		            @Override
		            public void run() {
		                try {
		                	//sendEmail();
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

				facesUtil.redirect("/pages/complianceTestingFE/complianceTestingFE.faces");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + ex.getMessage(), "");
		}

	}
	
	public void Submit() {
		try {

			if (!validate()) {

				User user = (User) facesUtil.getUserLogin();
				
				if(complianceTestingPICFollowup.getIsExtension().equals("N")) {
					
					if(uploadedFiles != null) {
						if(complianceTestingPICFollowup.getComplianceTestingPicFollowupAttachs() == null) {
							complianceTestingPICFollowup.setComplianceTestingPicFollowupAttachs(new ArrayList<ComplianceTestingPICFollowupAttachment>());
						}else {
							complianceTestingPICFollowup.getComplianceTestingPicFollowupAttachs().clear();
						}
						for (int i = 0; i < uploadedFiles.size(); i++) {
							ComplianceTestingPICFollowupAttachment evidence = new ComplianceTestingPICFollowupAttachment();
							UploadedFileWO uf = (UploadedFileWO) uploadedFiles.get(i);
							evidence.setComplianceTestingPICFollowup(complianceTestingPICFollowup);

							evidence.setAttachmentFile(uf.getFileName());
							evidence.setCreatedBy(facesUtil.retrieveUserLogin());
							evidence.setCreationDate(new Timestamp(new Date().getTime()));
							evidence.setDelId(new Long(0));
							evidence.setEnabledFlag(Constants.CONSTANT_YES);
							evidence.setFileId(uf.getFileId());
							evidence.setFileSize(uf.getFileSize());
											
							complianceTestingPICFollowup.getComplianceTestingPicFollowupAttachs().add(evidence);
						}
					}
					
					complianceTestingPICFollowup.setConfirmationDate(new Date());
					
					ParameterDetail followupStatus = parameterDetailService.getParameterDetailByParamDtlCode(
							ParameterDetail.PARAM_DET_CODE_PIC_FOLLOWUP_STATUS_PIC_DONE);
					
					complianceTestingPICFollowup.setFollowupStatus(followupStatus);
					
					complianceTestingPICFollowup.setFollowupById(userService.getUserByNik(facesUtil.retrieveUserLogin()).getUserId());
					
					complianceTestingPICFollowup.setComplianceNote(null);
					complianceTestingPICFollowup.setComplianceById(null);
					complianceTestingPICFollowup.setComplianceDate(null);
				}else {
					if(uploadedFilesExt != null) {
						if(complianceTestingPICFollowup.getComplianceTestingPicFollowupExts() == null) {
							complianceTestingPICFollowup.setComplianceTestingPicFollowupExts(new ArrayList<ComplianceTestingPICFollowupExt>());
						}
						for (int i = 0; i < uploadedFilesExt.size(); i++) {
							ComplianceTestingPICFollowupExt evidence = new ComplianceTestingPICFollowupExt();
							UploadedFileWO uf = (UploadedFileWO) uploadedFilesExt.get(i);
							evidence.setComplianceTestingPICFollowup(complianceTestingPICFollowup);
							evidence.setOldTargetDate(complianceTestingPICFollowup.getTargetDate());
							evidence.setNewTargetDate(newTargetDate);
							evidence.setAttachmentFile(uf.getFileName());
							evidence.setCreatedBy(facesUtil.retrieveUserLogin());
							evidence.setCreationDate(new Timestamp(new Date().getTime()));
							evidence.setDelId(new Long(0));
							evidence.setEnabledFlag(Constants.CONSTANT_YES);
							evidence.setFileId(uf.getFileId());
							evidence.setFileSize(uf.getFileSize());
											
							complianceTestingPICFollowup.getComplianceTestingPicFollowupExts().add(evidence);
						}
					}
					
					ParameterDetail followupStatus = parameterDetailService.getParameterDetailByParamDtlCode(
							ParameterDetail.PARAM_DET_CODE_PIC_FOLLOWUP_STATUS_PIC_EXTENSION);
					complianceTestingPICFollowup.setFollowupStatus(followupStatus);
					complianceTestingPICFollowup.setFollowupById(userService.getUserByNik(facesUtil.retrieveUserLogin()).getUserId());
				}

				complianceTestingPICFollowup.setLastUpdateBy(facesUtil.retrieveUserLogin());
				complianceTestingPICFollowup.setLastUpdateDate(new Timestamp(new Date().getTime()));
				complianceTestingPICFollowup.setDelId(new Long(0));
				complianceTestingPICFollowup.setEnabledFlag(Constants.CONSTANT_YES);
				complianceTestingPICFollowupService.update(complianceTestingPICFollowup);
				
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

				facesUtil.redirect("/pages/complianceTestingFE/complianceTestingFE.faces");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + ex.getMessage(), "");
		}

	}

	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

	public ComplianceTestingFEService getComplianceTestingFEService() {
		return complianceTestingFEService;
	}

	public void setComplianceTestingFEService(ComplianceTestingFEService complianceTestingFEService) {
		this.complianceTestingFEService = complianceTestingFEService;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		ComplianceTestingFEEditBean.logger = logger;
	}

	

	public ComplianceTestingPICFollowup getComplianceTestingPICFollowup() {
		return complianceTestingPICFollowup;
	}

	public void setComplianceTestingPICFollowup(ComplianceTestingPICFollowup complianceTestingPICFollowup) {
		this.complianceTestingPICFollowup = complianceTestingPICFollowup;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	public ComplianceTesting getComplianceTesting() {
		return complianceTesting;
	}

	public void setComplianceTesting(ComplianceTesting complianceTesting) {
		this.complianceTesting = complianceTesting;
	}

	public FileUtil getFileUtil() {
		return fileUtil;
	}

	public void setFileUtil(FileUtil fileUtil) {
		this.fileUtil = fileUtil;
	}

	

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public EmailTemplateService getEmailTemplateService() {
		return emailTemplateService;
	}

	public void setEmailTemplateService(EmailTemplateService emailTemplateService) {
		this.emailTemplateService = emailTemplateService;
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

	
	public Long getComplianceTestingPICFollowupId() {
		return complianceTestingPICFollowupId;
	}

	public void setComplianceTestingPICFollowupId(Long complianceTestingPICFollowupId) {
		this.complianceTestingPICFollowupId = complianceTestingPICFollowupId;
	}

	public String getViewOnly() {
		return viewOnly;
	}

	public void setViewOnly(String viewOnly) {
		this.viewOnly = viewOnly;
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

	public ComplianceTestingPICFollowupService getComplianceTestingPICFollowupService() {
		return complianceTestingPICFollowupService;
	}

	public void setComplianceTestingPICFollowupService(
			ComplianceTestingPICFollowupService complianceTestingPICFollowupService) {
		this.complianceTestingPICFollowupService = complianceTestingPICFollowupService;
	}

	public ComplianceTestingService getComplianceTestingService() {
		return complianceTestingService;
	}

	public void setComplianceTestingService(ComplianceTestingService complianceTestingService) {
		this.complianceTestingService = complianceTestingService;
	}

	public List<UploadedFileWO> getUploadedFilesExt() {
		return uploadedFilesExt;
	}

	public void setUploadedFilesExt(List<UploadedFileWO> uploadedFilesExt) {
		this.uploadedFilesExt = uploadedFilesExt;
	}

	public Date getNewTargetDate() {
		return newTargetDate;
	}

	public void setNewTargetDate(Date newTargetDate) {
		this.newTargetDate = newTargetDate;
	}

	
	
	
}