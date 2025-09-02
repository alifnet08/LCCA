package com.wo.module.complianceTestingFEMockup.bean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.exception.CustomAPIException;
import com.wo.module.common.model.UploadedFileWO;
import com.wo.module.common.util.FileUtil;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.complianceTestingFE.service.ComplianceTestingFEService;
import com.wo.module.emailTemplate.model.EmailTemplate;
import com.wo.module.emailTemplate.service.EmailTemplateService;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.trcComplianceReview.model.TrcComplianceReview;
import com.wo.module.trcComplianceReview.model.TrcComplianceReviewPicCompliance;
import com.wo.module.trcComplianceReview.model.TrcComplianceReviewPicFollowup;
import com.wo.module.trcComplianceReview.model.TrcComplianceReviewPicFollowupPoints;
import com.wo.module.trcComplianceReview.model.TrcComplianceReviewPicFollowupPointsAttachment;
import com.wo.module.trcComplianceReview.service.TrcComplianceReviewPicFollowupService;
import com.wo.module.trcComplianceReview.service.TrcComplianceReviewService;
import com.wo.module.user.model.User;
import com.wo.module.user.service.UserService;

public class ComplianceTestingFEEditBean extends CommonBean implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(ComplianceTestingFEEditBean.class);

	private TrcComplianceReviewPicFollowup trcComplianceReviewPicFollowup;
	private TrcComplianceReview trcComplianceReview;

	private FacesUtil facesUtil;
	private FileUtil fileUtil;
	
	private ComplianceTestingFEService  complianceTestingFEService;
	private TrcComplianceReviewPicFollowupService trcComplianceReviewPicFollowupService;
	private TrcComplianceReviewService trcComplianceReviewService;
	private UserService userService;
	private EmailTemplateService emailTemplateService;
	
	private List<TrcComplianceReviewPicFollowup> tableModelFollowupPoints;
	private List<UploadedFileWO> deletedFiles;
	private List<UploadedFileWO> uploadedFiles;

	private Long trcComplianceReviewPICFollowupId;
	private String viewOnly;
	private String textWarningUpload;
	private Integer testFirst;
	private Boolean isSelect;

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
	
	public void downloadFile (String fileId, String fileName, byte[] content) throws Exception {
		CallApiManager.downloadFile(fileId, fileName, content, parameterDetailService);
	}
	
	public void onSelect() {
		System.out.println("isSelect=="+isSelect);
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
		trcComplianceReviewPICFollowupId = idLong;
		
		uploadedFiles = new ArrayList<UploadedFileWO>();
		
		trcComplianceReviewPicFollowup = trcComplianceReviewPicFollowupService.findById(idLong);
		trcComplianceReview = trcComplianceReviewService.findById(trcComplianceReviewPicFollowup.getTrcComplianceReview().getComplianceReviewId());
		
		for (TrcComplianceReviewPicFollowupPoints fol : trcComplianceReviewPicFollowup.getTrcComplianceReviewPicFollowupPoints()) {
			List<UploadedFileWO> woFile = new ArrayList<UploadedFileWO>();
			
			for (TrcComplianceReviewPicFollowupPointsAttachment file : fol.getTrcComplianceReviewPicFollowupPointsAttachments()) {
				UploadedFileWO wo = new UploadedFileWO(file.getFileId(), file.getAttachmentFile(), file.getFileSize());
				wo.setIsNew(false);
				woFile.add(wo);
			}
			
			fol.setUploadedFilesEvidence(woFile);
		}
		
		/*trcComplianceReviewPicFollowup.getTrcComplianceReviewPicFollowupPoints().forEach(fol -> {
			System.out.println("for each");
			List<UploadedFileWO> woFile = new ArrayList<UploadedFileWO>();
			fol.getTrcComplianceReviewPicFollowupPointsAttachments().forEach(file -> {
				UploadedFileWO wo = new UploadedFileWO(file.getFileId(), file.getAttachmentFile(), file.getFileSize());
				wo.setIsNew(false);
				woFile.add(wo);
				System.out.println("BABY " + wo.getFileName());
				
			});
			fol.setUploadedFilesEvidence(woFile);
		});*/
		
		tableModelFollowupPoints = new ArrayList<TrcComplianceReviewPicFollowup>();
		tableModelFollowupPoints.add(trcComplianceReviewPicFollowup);

		if (trcComplianceReviewPicFollowup.getFollowupStatus() != null
				&& trcComplianceReviewPicFollowup.getFollowupStatus().getParameterDtlCode() != null
				&& trcComplianceReviewPicFollowup.getFollowupStatus().getParameterDtlCode()
						.equals(ParameterDetail.PARAM_DET_CODE_PIC_FOLLOWUP_STATUS_PIC_DONE)) {
			viewOnly = "Y";

			SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", new Locale("id", "ID"));
			
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
			
			String noDocAssigment = trcComplianceReview.getDocumentNo() != null ? " - " + trcComplianceReview.getDocumentNo() : "";

			emailContent = emailTemplate.getEmailContent().replace(Constants.CONFIRMATION_TYPE_AND_DOC_NUM, "Compliance Testing" 
					+ noDocAssigment);
			
			emailSubject = emailSubject.replace(Constants.CONFIRMATION_TYPE_AND_DOC_NUM, "Compliance Testing" 
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
			
			facesUtil.setSessionAttribute("BACK_SESSION", true);
			facesUtil.redirect("/pages/complianceTestingFE/complianceTestingFE.faces");
		} catch (Exception ex) {
			ex.printStackTrace();
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + ex.getMessage(), "");
		}
	}
	
	public void handleFileUploadEvidence(FileUploadEvent event) throws Exception {
		try {
			int idx = (int) event.getComponent().getAttributes().get("idxFollowup");
			
			List<UploadedFileWO> uploadedFilesEvidence = tableModelFollowupPoints.get(0).getTrcComplianceReviewPicFollowupPoints().get(idx).getUploadedFilesEvidence();
			
			uploadedFilesEvidence.add(new UploadedFileWO(CallApiManager.callUploadAPI(event.getFile(),
					Constants.COMPLIANCE_DOC_TYPE_PIC_FOLLOWUP, parameterDetailService, false, fileUtil),
					event.getFile().getFileName(), event.getFile().getContentType(), event.getFile().getSize(), true));
			
			//tableModelFollowupPoints.get(0).getTrcComplianceReviewPicFollowupPoints().get(idx).setUploadedFilesEvidence(uploadedFilesEvidence);
	
			/*PrimeFaces.current().ajax()
				.update("form:dataTableFollowupPoints:0:dataTablePointsPointFollowupEvidence:"+idx+":evidenceList");*/
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

	public TrcComplianceReviewPicFollowup getTrcComplianceReviewPicFollowup() {
		return trcComplianceReviewPicFollowup;
	}

	public void setTrcComplianceReviewPicFollowup(TrcComplianceReviewPicFollowup trcComplianceReviewPicFollowup) {
		this.trcComplianceReviewPicFollowup = trcComplianceReviewPicFollowup;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public TrcComplianceReviewPicFollowupService getTrcComplianceReviewPicFollowupService() {
		return trcComplianceReviewPicFollowupService;
	}

	public void setTrcComplianceReviewPicFollowupService(
			TrcComplianceReviewPicFollowupService trcComplianceReviewPicFollowupService) {
		this.trcComplianceReviewPicFollowupService = trcComplianceReviewPicFollowupService;
	}

	public TrcComplianceReview getTrcComplianceReview() {
		return trcComplianceReview;
	}

	public void setTrcComplianceReview(TrcComplianceReview trcComplianceReview) {
		this.trcComplianceReview = trcComplianceReview;
	}

	public FileUtil getFileUtil() {
		return fileUtil;
	}

	public void setFileUtil(FileUtil fileUtil) {
		this.fileUtil = fileUtil;
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

	public EmailTemplateService getEmailTemplateService() {
		return emailTemplateService;
	}

	public void setEmailTemplateService(EmailTemplateService emailTemplateService) {
		this.emailTemplateService = emailTemplateService;
	}

	public List<TrcComplianceReviewPicFollowup> getTableModelFollowupPoints() {
		return tableModelFollowupPoints;
	}

	public void setTableModelFollowupPoints(List<TrcComplianceReviewPicFollowup> tableModelFollowupPoints) {
		this.tableModelFollowupPoints = tableModelFollowupPoints;
	}

	public List<UploadedFileWO> getDeletedFiles() {
		return deletedFiles;
	}

	public void setDeletedFiles(List<UploadedFileWO> deletedFiles) {
		this.deletedFiles = deletedFiles;
	}

	public Long getTrcComplianceReviewPICFollowupId() {
		return trcComplianceReviewPICFollowupId;
	}

	public void setTrcComplianceReviewPICFollowupId(Long trcComplianceReviewPICFollowupId) {
		this.trcComplianceReviewPICFollowupId = trcComplianceReviewPICFollowupId;
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

	public Boolean getIsSelect() {
		return isSelect;
	}

	public void setIsSelect(Boolean isSelect) {
		this.isSelect = isSelect;
	}

	public List<UploadedFileWO> getUploadedFiles() {
		return uploadedFiles;
	}

	public void setUploadedFiles(List<UploadedFileWO> uploadedFiles) {
		this.uploadedFiles = uploadedFiles;
	}
	
}