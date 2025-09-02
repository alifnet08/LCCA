package com.wo.module.regulationMonitoringFE.bean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.event.FileUploadEvent;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.CommonConstants;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.model.UploadedFileWO;
import com.wo.module.common.util.FileUtil;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.emailTemplate.model.EmailTemplate;
import com.wo.module.emailTemplate.service.EmailTemplateService;
import com.wo.module.externalRegulation.model.RegulationMst;
import com.wo.module.externalRegulation.service.RegulationService;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.regMonitoringPICFpConfirmation.service.RegMonitoringPICFpConfirmationService;
import com.wo.module.regulationMonitoring.model.RegMonitoringPICComplianceTrc;
import com.wo.module.regulationMonitoring.model.RegMonitoringPICFollowUpAttachmentTrc;
import com.wo.module.regulationMonitoring.model.RegMonitoringPICFollowUpTrc;
import com.wo.module.regulationMonitoring.model.RegMonitoringPICFollowUpTrcTableModel;
import com.wo.module.regulationMonitoring.model.RegMonitoringRegulationTrc;
import com.wo.module.regulationMonitoring.model.RegMonitoringTrc;
import com.wo.module.regulationMonitoring.service.RegMonitoringPICFollowUpTrcService;
import com.wo.module.regulationMonitoring.service.RegMonitoringRegulationTrcService;
import com.wo.module.regulationMonitoring.service.RegMonitoringTrcService;
import com.wo.module.regulationMonitoring.service.RegulationMonitoringService;
import com.wo.module.regulationMonitoringFE.service.RegulationMonitoringFEService;
import com.wo.module.regulationMonitoringFE.vo.RegulationMonitoringFEVO;
import com.wo.module.user.model.User;
import com.wo.module.user.service.UserService;

public class RegulationMonitoringFEEditBean extends CommonBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private RegMonitoringTrc regMonitoringTrc;

	private String editedId;

	private List<UploadedFileWO> uploadedFiles;

	private List<UploadedFileWO> deletedFiles;

	private RegMonitoringPICFollowUpTrcTableModel<RegMonitoringPICFollowUpTrc> tableModelFollowup;

	private RegulationMonitoringService regulationMonitoringService;
	
	private RegMonitoringPICFpConfirmationService regMonitoringPICFpConfirmationService;

	private RegMonitoringPICFollowUpTrcService regMonitoringPICFollowUpTrcService;
	
	private RegulationMonitoringFEService regulationMonitoringFEService; 
	
	private RegMonitoringTrcService regMonitoringTrcService;

	private RegulationService regulationService;

	private UserService userService;

	private EmailTemplateService emailTemplateService;

	public FacesUtil facesUtil;

	private FileUtil fileUtil;

	private String viewOnly;
	
	private Long regMonitoringPICFollowupTrcId;

	private RegMonitoringPICFollowUpTrc regMonitoringPICFollowUpTrc;
	
	private RegulationMonitoringFEVO regulationMonitoringFEVO;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
	
	private String textWarningUpload;
	
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
		facesUtil.setSessionAttribute("FIRST_REG_MONITOR_FE", testFirst);
		facesUtil.setSessionAttribute("BACK_SESSION", false);
		handleEdit();
		fileUtil = FileUtil.getInstance();
		
		try {
			ParameterDetail getText = parameterDetailService.getParameterDetailByParamDtlCode("UPLOAD_WARNING_TEXT");
			textWarningUpload = getText.getName();
		} catch (Exception e) {
			e.printStackTrace();
		}
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
			this.regMonitoringPICFollowupTrcId = idLong;
			
			regMonitoringPICFollowUpTrc = regMonitoringPICFollowUpTrcService.findById(idLong);
			
			regulationMonitoringFEVO = regulationMonitoringFEService.searchForDetail(regMonitoringPICFollowUpTrc.getRegMonitoringTrc().getRegMonitoringTrcId());
			
			regMonitoringTrc = regMonitoringTrcService.findById(regMonitoringPICFollowUpTrc.getRegMonitoringTrc().getRegMonitoringTrcId());
			
			/*tableModelFollowup = new RegMonitoringPICFollowUpTrcTableModel<RegMonitoringPICFollowUpTrc>(
					regMonitoringTrc.getRegMonitoringPICFollowUpTrcs());
*/
			if (regMonitoringPICFollowUpTrc != null
					&& regMonitoringPICFollowUpTrc.getRegMonitoringPICFollowUpAttachmentTrcs() != null
					&& regMonitoringPICFollowUpTrc.getRegMonitoringPICFollowUpAttachmentTrcs().size() > 0) {
				uploadedFiles = new ArrayList<UploadedFileWO>();
				for (RegMonitoringPICFollowUpAttachmentTrc dtl : regMonitoringPICFollowUpTrc
						.getRegMonitoringPICFollowUpAttachmentTrcs()) {
					UploadedFileWO file = new UploadedFileWO();
					file.setFileId(dtl.getFileId());
					file.setFileName(dtl.getAttachmentFile());
					file.setIsNew(false);
					file.setFileSize(dtl.getFileSize());
					uploadedFiles.add(file);
				}
			}

			if (regMonitoringPICFollowUpTrc.getFollowUpStatus() != null
					&& regMonitoringPICFollowUpTrc.getFollowUpStatus().getParameterDtlCode() != null
					&& regMonitoringPICFollowUpTrc.getFollowUpStatus().getParameterDtlCode()
							.equals(ParameterDetail.PARAM_DET_CODE_PIC_FOLLOWUP_STATUS_PIC_DONE)) {
				viewOnly = "Y";

				SimpleDateFormat sdf = new SimpleDateFormat(CommonConstants.INPUT_DATE_FORMAT);

				facesUtil.addWarnMessage(facesUtil.retrieveMessage("formRegulationSocializationNotifConfirmationDone",
						sdf.format(regMonitoringPICFollowUpTrc.getTargetDate()),
						regMonitoringPICFollowUpTrc.getFollowUpBy().getName()));

			} else {
				viewOnly = "N";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void handleFileUpload(FileUploadEvent event) throws Exception {
		try {
			uploadedFiles = uploadedFiles == null ? new ArrayList<UploadedFileWO>() : uploadedFiles;
			uploadedFiles.add(new UploadedFileWO(
					CallApiManager.callUploadAPI(event.getFile(), Constants.COMPLIANCE_DOC_TYPE_PIC_FOLLOWUP,
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

	public void deleteAttachment(String fileId, int index) throws Exception {
		// CallApiManager.deleteFile(fileId, parameterDetailService, fileUtil);
		deletedFiles = deletedFiles != null ? deletedFiles : new ArrayList<UploadedFileWO>();

		deletedFiles.add(new UploadedFileWO(fileId, null, null, null));

		uploadedFiles.remove(uploadedFiles.get(index));

	}

	public Boolean validate() {
		Boolean flag = true;

		if (regMonitoringPICFollowUpTrc.getFollowUpDate() == null) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formPICFollowupConfirmationFollowupDate") + " "
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = false;
		}

		if (uploadedFiles == null || uploadedFiles.size() <= 0) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formPICFollowupConfirmationPICConfirmationEvidence")
					+ " File " + facesUtil.retrieveMessage("validateRequired"));
			flag = false;
		}

		return flag;
	}

	public void sendEmail() throws Exception {

		EmailTemplate emailTemplate = emailTemplateService.getEmailTemplateByEmailTemplateCode("EMAIL_PIC_COMPLIANCE");
		String emailSubject = emailTemplate.getEmailSubject();

		String emailContent = "";
		String emailTo = "";
		String emailCc = "";

		String documentNumberTemp = "";
		String documentTitleTemp = "";
		for (int i = 0; i < regMonitoringTrc.getRegMonitoringRegulationTrcs().size(); i++) {
			RegMonitoringRegulationTrc temp = regMonitoringTrc.getRegMonitoringRegulationTrcs().get(0);
			documentNumberTemp = temp.getRegulation().getDocumentNo();
			documentTitleTemp = temp.getRegulation().getNameIn();
		}

//				emailContent = emailTemplate.getEmailContent().replace("confirmation_type", "Sosialisasi");
		emailContent = emailTemplate.getEmailContent().replace(Constants.CONFIRMATION_TYPE_AND_DOC_NUM,
				"Regulation Monitoring" + " - " + documentNumberTemp + "_" + documentTitleTemp);
		
		emailSubject = emailSubject.replace(Constants.CONFIRMATION_TYPE_AND_DOC_NUM,
				"Regulation Monitoring" + " - " + documentNumberTemp + "_" + documentTitleTemp);

		for (int x = 0; x < regMonitoringTrc.getRegMonitoringPICComplianceTrcs().size(); x++) {
			RegMonitoringPICComplianceTrc cd = regMonitoringTrc.getRegMonitoringPICComplianceTrcs().get(x);
			emailTo = cd.getUser().getEmail();

//					ExecutorService emailExecutor = Executors.newCachedThreadPool();

			final String subject = emailSubject;
			final String content = emailContent;
			final String to = emailTo;
			// final String to = "h3ndr407@gmail.com";
			final String cc = emailCc;

			CallApiManager.sendEmailAPI(to, cc, subject, content, "EMAIL_REGULATION_MONITORING", "true",
					parameterDetailService);

//			        emailExecutor.execute(new Runnable() {
//			            @Override
//			            public void run() {
//							
//			            	try {
//								CallApiManager.sendEmail(subject, content, to,cc, parameterDetailService);
//								 
//							} catch (Exception e) {
//								e.printStackTrace();
//							}
//			            }
//			        });

		}

	}
	
	public void save() {
		try {
			//String tglTindakLanjut = facesUtil.retrieveRequestParam("tglTindakLanjut");
			String keterangan = facesUtil.retrieveRequestParam("keterangan");
			
			/*SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			if(!StringUtils.isEmpty(tglTindakLanjut)){
				regMonitoringPICFollowUpTrc.setFollowUpDate(sdf.parse(tglTindakLanjut));
				
			}*/
			
			if(!StringUtils.isEmpty(keterangan)){
				regMonitoringPICFollowUpTrc.setFollowUpNote(keterangan);
			}
			
			
			if (validate()) {
				User user = (User) facesUtil.getUserLogin();

				regMonitoringPICFpConfirmationService.processConfirm(regMonitoringTrc, regMonitoringPICFollowupTrcId, 
						regMonitoringPICFollowUpTrc.getFollowUpNote(), regMonitoringPICFollowUpTrc.getFollowUpDate(), uploadedFiles,user);
				
				// this should be a singleton
		        ExecutorService emailExecutor = Executors.newCachedThreadPool();

		        // from you sendEmail() method
		        emailExecutor.execute(new Runnable() {
		            @Override
		            public void run() {
		                try {
		                	sendEmail();
		                } catch (Exception e) {
		                    e.printStackTrace();
		                }
		            }
		        });
				
				if(deletedFiles!=null) {
					for(int i=0;i<deletedFiles.size();i++) {
						UploadedFileWO uf = (UploadedFileWO) deletedFiles.get(i);
						CallApiManager.deleteFile(uf.getFileId(), parameterDetailService, fileUtil);
					}
				}
				
				

				facesUtil.redirect("/pages/regulationMonitoringFE/regulationMonitoringFE.faces");
			}
			
			

		} catch (Exception ex) {
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + ex.getMessage(), "");
			ex.printStackTrace();
		}
	}

	public void cancel() {
		try {
			if(uploadedFiles != null) {
				for (int i = 0; i < uploadedFiles.size(); i++) {
					UploadedFileWO uf = (UploadedFileWO) uploadedFiles.get(i);
					if(uf.getIsNew() == null) {
						CallApiManager.deleteFile(uf.getFileId(), parameterDetailService, fileUtil);
					}
				}
			}
			
			facesUtil.setSessionAttribute("BACK_SESSION", true);
			facesUtil.redirect("/pages/regulationMonitoringFE/regulationMonitoringFE.faces");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public RegMonitoringTrc getRegMonitoringTrc() {
		return regMonitoringTrc;
	}

	public void setRegMonitoringTrc(RegMonitoringTrc regMonitoringTrc) {
		this.regMonitoringTrc = regMonitoringTrc;
	}

	public String getEditedId() {
		return editedId;
	}

	public void setEditedId(String editedId) {
		this.editedId = editedId;
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

	public RegMonitoringPICFollowUpTrcTableModel<RegMonitoringPICFollowUpTrc> getTableModelFollowup() {
		return tableModelFollowup;
	}

	public void setTableModelFollowup(
			RegMonitoringPICFollowUpTrcTableModel<RegMonitoringPICFollowUpTrc> tableModelFollowup) {
		this.tableModelFollowup = tableModelFollowup;
	}

	public RegulationMonitoringService getRegulationMonitoringService() {
		return regulationMonitoringService;
	}

	public void setRegulationMonitoringService(RegulationMonitoringService regulationMonitoringService) {
		this.regulationMonitoringService = regulationMonitoringService;
	}

	public RegMonitoringPICFpConfirmationService getRegMonitoringPICFpConfirmationService() {
		return regMonitoringPICFpConfirmationService;
	}

	public void setRegMonitoringPICFpConfirmationService(
			RegMonitoringPICFpConfirmationService regMonitoringPICFpConfirmationService) {
		this.regMonitoringPICFpConfirmationService = regMonitoringPICFpConfirmationService;
	}

	public RegMonitoringPICFollowUpTrcService getRegMonitoringPICFollowUpTrcService() {
		return regMonitoringPICFollowUpTrcService;
	}

	public void setRegMonitoringPICFollowUpTrcService(
			RegMonitoringPICFollowUpTrcService regMonitoringPICFollowUpTrcService) {
		this.regMonitoringPICFollowUpTrcService = regMonitoringPICFollowUpTrcService;
	}

	public RegMonitoringTrcService getRegMonitoringTrcService() {
		return regMonitoringTrcService;
	}

	public void setRegMonitoringTrcService(RegMonitoringTrcService regMonitoringTrcService) {
		this.regMonitoringTrcService = regMonitoringTrcService;
	}

	public RegulationService getRegulationService() {
		return regulationService;
	}

	public void setRegulationService(RegulationService regulationService) {
		this.regulationService = regulationService;
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

	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
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

	public Long getRegMonitoringPICFollowupTrcId() {
		return regMonitoringPICFollowupTrcId;
	}

	public void setRegMonitoringPICFollowupTrcId(Long regMonitoringPICFollowupTrcId) {
		this.regMonitoringPICFollowupTrcId = regMonitoringPICFollowupTrcId;
	}

	public RegMonitoringPICFollowUpTrc getRegMonitoringPICFollowUpTrc() {
		return regMonitoringPICFollowUpTrc;
	}

	public void setRegMonitoringPICFollowUpTrc(RegMonitoringPICFollowUpTrc regMonitoringPICFollowUpTrc) {
		this.regMonitoringPICFollowUpTrc = regMonitoringPICFollowUpTrc;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public RegulationMonitoringFEService getRegulationMonitoringFEService() {
		return regulationMonitoringFEService;
	}

	public void setRegulationMonitoringFEService(RegulationMonitoringFEService regulationMonitoringFEService) {
		this.regulationMonitoringFEService = regulationMonitoringFEService;
	}

	public RegulationMonitoringFEVO getRegulationMonitoringFEVO() {
		return regulationMonitoringFEVO;
	}

	public void setRegulationMonitoringFEVO(RegulationMonitoringFEVO regulationMonitoringFEVO) {
		this.regulationMonitoringFEVO = regulationMonitoringFEVO;
	}

	public SimpleDateFormat getSdf() {
		return sdf;
	}

	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
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
}