package com.wo.module.socializationFE.bean;

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
import org.apache.log4j.Logger;
import org.primefaces.event.FileUploadEvent;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.model.UploadedFileWO;
import com.wo.module.common.util.FileUtil;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.emailTemplate.model.EmailTemplate;
import com.wo.module.emailTemplate.service.EmailTemplateService;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.picFollowupConfirmation.service.PICFollowupConfirmationService;
import com.wo.module.regulationSocialization.model.SocializationDocumentTrc;
import com.wo.module.regulationSocialization.model.SocializationPICComplianceTrc;
import com.wo.module.regulationSocialization.model.SocializationPICFollowupAttachmentTrc;
import com.wo.module.regulationSocialization.model.SocializationPICFollowupTrc;
import com.wo.module.regulationSocialization.model.SocializationRegulationTrc;
import com.wo.module.regulationSocialization.model.SocializationTrc;
import com.wo.module.regulationSocialization.service.SocializationPICFollowupTrcService;
import com.wo.module.socializationFE.constant.SocializationFEConstant;
import com.wo.module.socializationFE.service.SocializationFEService;
import com.wo.module.user.model.User;
import com.wo.module.user.service.UserService;

public class SocializationFEEditBean extends CommonBean implements Serializable{

	private static final long serialVersionUID = -5008862071273906554L;
	private static final Logger logger = Logger.getLogger(SocializationFEEditBean.class);
	private static final String NAVIGATE_BACK = SocializationFEConstant.NAVIGATE_SOCIALIZATION_FE;
	
	private SocializationFEService socializationFEService;
	private UserService userService;
	private EmailTemplateService emailTemplateService;
	private SocializationPICFollowupTrcService socializationPICFollowupTrcService;
	private PICFollowupConfirmationService picFollowupConfirmationService;
	
	private SocializationTrc socializationTrc;
	private SocializationPICFollowupTrc socializationPICFollowupTrc;
	
	private String editId;
	private String jenisPeraturan;
	private String tanggalTerbitKetentuan;
	private String nomorDokumen;
	private String tanggalEfektif;
	private String judulPeraturan;
	private String tanggalReview;
	private String tipePeraturan;
	private String catatan;
	private String targetDate;
	
	private List<UploadedFileWO> deletedFiles;
	private List<UploadedFileWO> uploadedFiles;
	private List<UploadedFileWO> uploadedFileAttachs;
	
	private FacesUtil facesUtil;
	private FileUtil fileUtil;

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
		facesUtil.setSessionAttribute("FIRST_SOCIAL_FE", testFirst);
		facesUtil.setSessionAttribute("BACK_SESSION", false);
		checkNewOrEdit();
		fileUtil = FileUtil.getInstance();
		
		try {
			ParameterDetail getText = parameterDetailService.getParameterDetailByParamDtlCode("UPLOAD_WARNING_TEXT");
			textWarningUpload = getText.getName();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void checkNewOrEdit() {
		this.editId = facesUtil.retrieveRequestParam("id");
		
		String token = facesUtil.retrieveRequestParam("token");
		if (StringUtils.isNotEmpty(token)) {
			editId = Constants.decryptString(token);
		}
		facesUtil.setSessionAttribute("token", Constants.encryptString(editId));
		
		if (StringUtils.isBlank(editId)) {
			// do nothing
		} else {
			handleEdit(editId);
		}
	}

	private void handleEdit(String editId) {
		
		uploadedFileAttachs = new ArrayList<UploadedFileWO>();
		
		Long editIdLong = Long.parseLong(editId);
		socializationPICFollowupTrc = socializationPICFollowupTrcService.findById(editIdLong);
		socializationTrc = socializationFEService.findById(socializationPICFollowupTrc.getSocializationTrc().getSocializationId());
		this.judulPeraturan = socializationTrc.getJenisKetentuan().getNameIn() != null ? socializationTrc.getJenisKetentuan().getNameIn() : "";
		this.catatan = socializationPICFollowupTrc.getNotes() != null ? socializationPICFollowupTrc.getNotes() : "";
		this.targetDate = socializationPICFollowupTrc.getTargetDate() != null ? sdf.format(socializationPICFollowupTrc.getTargetDate()) : ""; 
		if (socializationTrc.getSocializationRegulationTrcs() != null) {
			for (int i = 0; i < socializationTrc.getSocializationRegulationTrcs().size(); i++) {
				SocializationRegulationTrc vo = (SocializationRegulationTrc) socializationTrc.getSocializationRegulationTrcs().get(i);
				if(vo.getPrimaryFlag()!=null && vo.getPrimaryFlag().equals(Constants.CONSTANT_YES)){
				this.tanggalTerbitKetentuan = vo.getRegulation().getPublishedDate() != null ? sdf.format(vo.getRegulation().getPublishedDate()) : "";
				this.nomorDokumen = vo.getRegulation().getDocumentNo() != null ? vo.getRegulation().getDocumentNo() : "";
				this.tanggalEfektif = vo.getRegulation().getEffectiveDate() != null ? sdf.format(vo.getRegulation().getEffectiveDate()) : "";
				this.judulPeraturan = vo.getRegulation().getNameIn() != null ? vo.getRegulation().getNameIn() : "";
				this.tipePeraturan = vo.getRegulation().getJenisKetentuan().getName() != null ? vo.getRegulation().getJenisKetentuan().getName() : "";
				}
			}
		}
		if (socializationTrc.getSocializationDocumentTrcs() != null) {
			for (int i = 0; i < socializationTrc.getSocializationDocumentTrcs().size(); i++) {
				SocializationDocumentTrc vo = (SocializationDocumentTrc) socializationTrc.getSocializationDocumentTrcs().get(i);
				UploadedFileWO uf = new UploadedFileWO();
				uf.setFileName(vo.getAttachmentFile());
				uf.setFileId(vo.getFileId());
				uf.setIsNew(false);
				uf.setFileSize(vo.getFileSize());
				uploadedFileAttachs.add(uf);
			}
		}
		
		if (socializationTrc.getSocializationPICFollowupTrcs() != null) {
			uploadedFiles = new ArrayList<UploadedFileWO>();
			for (int i = 0; i < socializationTrc.getSocializationPICFollowupTrcs().size(); i++) {
				SocializationPICFollowupTrc vo = (SocializationPICFollowupTrc) socializationTrc.getSocializationPICFollowupTrcs().get(i);
				this.catatan = vo.getFollowupNote() != null ? vo.getFollowupNote() : "";
				this.targetDate = vo.getTargetDate() != null ? sdf.format(vo.getTargetDate()) : "";
				if (vo.getSocializationPICFollowupAttachmentTrcs() != null) {
					for (int j = 0; j < vo.getSocializationPICFollowupAttachmentTrcs().size(); j++) {
						SocializationPICFollowupAttachmentTrc vo1 = (SocializationPICFollowupAttachmentTrc) vo.getSocializationPICFollowupAttachmentTrcs().get(i);
						UploadedFileWO uf = new UploadedFileWO();
						uf.setFileName(vo1.getAttachmentFile());
						uf.setFileId(vo1.getFileId());
						uf.setIsNew(false);
						uf.setFileSize(vo1.getFileSize());
						uploadedFiles.add(uf);
					}
				}
			}
		}
	}
	
	public void handleFileUpload(FileUploadEvent event) throws Exception {
		try {
		uploadedFiles = uploadedFiles == null ? new ArrayList<UploadedFileWO>() : uploadedFiles;
		uploadedFiles.add(new UploadedFileWO(CallApiManager.callUploadAPI(event.getFile(),
				Constants.COMPLIANCE_DOC_TYPE_DOKUMEN_TINDAK_LANJUT_DARI_PIC, parameterDetailService, false, fileUtil),
				event.getFile().getFileName(), event.getFile().getContentType(), event.getFile().getSize()));
		} catch (Exception e) {
			e.printStackTrace();
			facesUtil.addErrMessage(e.getMessage());
		}
	}
	
	private boolean isValidate() {
		boolean flag = true;
		
		if (socializationPICFollowupTrc.getFollowupDate() == null) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formPICFollowupConfirmationFollowupDate") + " "
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = false;
		}
		
		if (uploadedFiles == null || uploadedFiles.size() <= 0) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formPICFollowupConfirmationPICConfirmationEvidence") + " File "
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = false;
		}
		
		return flag;
	}
	
	public void save() {
		try {
			//String tglTindakLanjut = facesUtil.retrieveRequestParam("tglTindakLanjut");
			String keterangan = facesUtil.retrieveRequestParam("keterangan");
			
			/*SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			if (!StringUtils.isEmpty(tglTindakLanjut)) {
				socializationPICFollowupTrc.setFollowupDate(sdf.parse(tglTindakLanjut));
			}*/
			if (!StringUtils.isEmpty(keterangan)) {
				socializationPICFollowupTrc.setFollowupNote(keterangan);
			}
			if (isValidate()) {
				User user = (User) facesUtil.getUserLogin();
				
				picFollowupConfirmationService.processConfirm(socializationTrc, Long.parseLong(editId),
						socializationPICFollowupTrc.getFollowupNote(), socializationPICFollowupTrc.getFollowupDate(), uploadedFiles, user);
				
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
				
				facesUtil.redirect("/pages/socializationFE/socializationFE.faces");
			}
		} catch (Exception ex) {
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + ex.getMessage(), "");
			ex.printStackTrace();
		}
	}
	
	public void sendEmail() throws Exception {
		
		
		EmailTemplate emailTemplate = emailTemplateService.getEmailTemplateByEmailTemplateCode("EMAIL_PIC_COMPLIANCE");
		String emailSubject = emailTemplate.getEmailSubject();

		String emailContent = ""; 
		String emailTo  = "";
		String emailCc = "";
	   
		
		String documentNumberTemp = "";
		String documentTitleTemp = "";
		for (int i = 0; i < socializationTrc.getSocializationRegulationTrcs().size(); i++) {
			SocializationRegulationTrc temp = socializationTrc.getSocializationRegulationTrcs().get(0);
			documentNumberTemp = temp.getRegulation().getDocumentNo();
			documentTitleTemp = temp.getRegulation().getNameIn();
		}
		
//				emailContent = emailTemplate.getEmailContent().replace("confirmation_type", "Sosialisasi");
				emailContent = emailTemplate.getEmailContent().replace(Constants.CONFIRMATION_TYPE_AND_DOC_NUM, "Sosialisasi" + " - "
						+ documentNumberTemp + "_" + documentTitleTemp);
				
				emailSubject = emailSubject.replace(Constants.CONFIRMATION_TYPE_AND_DOC_NUM, "Sosialisasi" + " - "
						+ documentNumberTemp + "_" + documentTitleTemp);
				
				for(int x=0;x<socializationTrc.getSocializationPICComplianceTrcs().size();x++) {
					SocializationPICComplianceTrc cd = socializationTrc.getSocializationPICComplianceTrcs().get(x);
					emailTo = cd.getUser().getEmail();
					
					
					final String subject = emailSubject;
					final String content = emailContent;
					final String to = emailTo;
					//final String to = "h3ndr407@gmail.com";
					final String cc = emailCc;
					
					CallApiManager.sendEmailAPI(to, cc, subject, content, "EMAIL_SOSIALISASI", "true", parameterDetailService);
			        
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
			facesUtil.redirect("/pages/socializationFE/socializationFE.faces");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void downloadFile(String fileId, String fileName, byte[] content) throws Exception {
		CallApiManager.downloadFile(fileId, fileName, content, parameterDetailService);
	}
	
	public void deleteAttachment(String fileId,int index,String uploadType) throws Exception {
		deletedFiles = deletedFiles!=null?deletedFiles: new ArrayList<UploadedFileWO>();
		deletedFiles.add(new UploadedFileWO(fileId,null,null, null));
		if (uploadType != null && uploadType.equals(SocializationFEConstant.UPLOAD_TYPE_DOCUMENT)) {
			uploadedFiles.remove(uploadedFiles.get(index));
		}
	}
	
	public SocializationFEService getSocializationFEService() {
		return socializationFEService;
	}

	public void setSocializationFEService(SocializationFEService socializationFEService) {
		this.socializationFEService = socializationFEService;
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

	public SocializationTrc getSocializationTrc() {
		return socializationTrc;
	}

	public void setSocializationTrc(SocializationTrc socializationTrc) {
		this.socializationTrc = socializationTrc;
	}

	public String getEditId() {
		return editId;
	}

	public void setEditId(String editId) {
		this.editId = editId;
	}

	public List<UploadedFileWO> getDeletedFiles() {
		return deletedFiles;
	}

	public void setDeletedFiles(List<UploadedFileWO> deletedFiles) {
		this.deletedFiles = deletedFiles;
	}

	public List<UploadedFileWO> getUploadedFiles() {
		return uploadedFiles;
	}

	public void setUploadedFiles(List<UploadedFileWO> uploadedFiles) {
		this.uploadedFiles = uploadedFiles;
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

	public SimpleDateFormat getSdf() {
		return sdf;
	}

	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static String getNavigateBack() {
		return NAVIGATE_BACK;
	}

	public String getJenisPeraturan() {
		return jenisPeraturan;
	}

	public void setJenisPeraturan(String jenisPeraturan) {
		this.jenisPeraturan = jenisPeraturan;
	}

	public String getTanggalTerbitKetentuan() {
		return tanggalTerbitKetentuan;
	}

	public void setTanggalTerbitKetentuan(String tanggalTerbitKetentuan) {
		this.tanggalTerbitKetentuan = tanggalTerbitKetentuan;
	}

	public String getNomorDokumen() {
		return nomorDokumen;
	}

	public void setNomorDokumen(String nomorDokumen) {
		this.nomorDokumen = nomorDokumen;
	}

	public String getTanggalEfektif() {
		return tanggalEfektif;
	}

	public void setTanggalEfektif(String tanggalEfektif) {
		this.tanggalEfektif = tanggalEfektif;
	}

	public String getJudulPeraturan() {
		return judulPeraturan;
	}

	public void setJudulPeraturan(String judulPeraturan) {
		this.judulPeraturan = judulPeraturan;
	}

	public String getTanggalReview() {
		return tanggalReview;
	}

	public void setTanggalReview(String tanggalReview) {
		this.tanggalReview = tanggalReview;
	}

	public String getTipePeraturan() {
		return tipePeraturan;
	}

	public void setTipePeraturan(String tipePeraturan) {
		this.tipePeraturan = tipePeraturan;
	}

	public String getCatatan() {
		return catatan;
	}

	public void setCatatan(String catatan) {
		this.catatan = catatan;
	}

	public List<UploadedFileWO> getUploadedFileAttachs() {
		return uploadedFileAttachs;
	}

	public void setUploadedFileAttachs(List<UploadedFileWO> uploadedFileAttachs) {
		this.uploadedFileAttachs = uploadedFileAttachs;
	}

	public SocializationPICFollowupTrc getSocializationPICFollowupTrc() {
		return socializationPICFollowupTrc;
	}

	public void setSocializationPICFollowupTrc(SocializationPICFollowupTrc socializationPICFollowupTrc) {
		this.socializationPICFollowupTrc = socializationPICFollowupTrc;
	}

	public SocializationPICFollowupTrcService getSocializationPICFollowupTrcService() {
		return socializationPICFollowupTrcService;
	}

	public void setSocializationPICFollowupTrcService(SocializationPICFollowupTrcService socializationPICFollowupTrcService) {
		this.socializationPICFollowupTrcService = socializationPICFollowupTrcService;
	}

	public PICFollowupConfirmationService getPicFollowupConfirmationService() {
		return picFollowupConfirmationService;
	}

	public void setPicFollowupConfirmationService(PICFollowupConfirmationService picFollowupConfirmationService) {
		this.picFollowupConfirmationService = picFollowupConfirmationService;
	}

	public String getTargetDate() {
		return targetDate;
	}

	public void setTargetDate(String targetDate) {
		this.targetDate = targetDate;
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
