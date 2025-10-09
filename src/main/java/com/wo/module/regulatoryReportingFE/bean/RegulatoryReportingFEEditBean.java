package com.wo.module.regulatoryReportingFE.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
import com.wo.module.emailTemplate.service.EmailTemplateService;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.regulatoryReportingFE.constant.RegulatoryReportingFEConstant;
import com.wo.module.regulatoryReportingFE.service.RegulatoryReportingFEService;
import com.wo.module.trcRmd.model.TrcRmd;
import com.wo.module.trcRmd.model.TrcRmdPicFollowup;
import com.wo.module.trcRmd.model.TrcRmdPicFollowupAttachment;
import com.wo.module.trcRmd.service.TrcRmdPicFollowupService;
import com.wo.module.user.service.UserService;

public class RegulatoryReportingFEEditBean extends CommonBean implements Serializable{

	private static final long serialVersionUID = -7650945246413857182L;
	private static final Logger logger = Logger.getLogger(RegulatoryReportingFEEditBean.class);
	private static final String NAVIGATE_BACK = RegulatoryReportingFEConstant.NAVIGATE_REGULATORY_REPORTING_FE;
	
	private RegulatoryReportingFEService regulatoryReportingFEService;
	private EmailTemplateService emailTemplateService;
	private UserService userService;
	private TrcRmdPicFollowupService trcRmdPicFollowupService;
	
	private TrcRmd trcRmd;
	private TrcRmdPicFollowup trcRmdPicFollowup;
	
	private String actionMode;
	private String editId;
	private String targetDate;
	private String note;
	
	private List<UploadedFileWO> uploadFiles;
	private List<UploadedFileWO> deleteFiles;
	
	private FacesUtil facesUtil;
	
	private FileUtil fileUtil;

	private SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", new Locale("id", "ID"));
	
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
		facesUtil.setSessionAttribute("FIRST_REG_REPORT_FE", testFirst);
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
		
		if (StringUtils.isBlank(editId)) {
			// do nothing
		} else {
			handleEdit(editId);
		}
	}
	
	private void handleEdit(String editId) {
		
		Long editIdLong = Long.parseLong(editId);
		trcRmdPicFollowup = trcRmdPicFollowupService.findById(editIdLong);
		trcRmd = regulatoryReportingFEService.findById(trcRmdPicFollowup.getTrcRmd().getRmdId());
		
		if (trcRmdPicFollowup.getPicFollowupAttachmentDetails() != null) {
			uploadFiles = uploadFiles == null ? new ArrayList<UploadedFileWO>() : uploadFiles;
			for (int i = 0; i < trcRmdPicFollowup.getPicFollowupAttachmentDetails().size(); i++) {
				TrcRmdPicFollowupAttachment vo = (TrcRmdPicFollowupAttachment) trcRmdPicFollowup.getPicFollowupAttachmentDetails().get(i);
				UploadedFileWO uf = new UploadedFileWO();
				uf.setFileName(vo.getAttachmentFile());
				uf.setFileId(vo.getFileId());
				uf.setIsNew(false);
				uf.setFileSize(vo.getFileSize());
				uploadFiles.add(uf);
			}
		}
	}

	public void handleFileUpload(FileUploadEvent event) throws Exception {
		try {
			uploadFiles = uploadFiles == null ? new ArrayList<UploadedFileWO>() : uploadFiles;
			uploadFiles.add(new UploadedFileWO(
						CallApiManager.callUploadAPI(event.getFile(), Constants.COMPLIANCE_DOC_TYPE_REGULATORY_REPORTING,
								parameterDetailService, false, fileUtil),
						event.getFile().getFileName(), event.getFile().getContentType(),
						event.getFile().getSize()));
		} catch (Exception e) {
			e.printStackTrace();
			facesUtil.addErrMessage(e.getMessage());
		}
	}
	
	private boolean isValidate() {
		boolean flag = true;
		
		if (trcRmd.getFollowupDate() == null) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpCorrespondenceFollowupDate") + " "
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}
		
		if (uploadFiles == null || uploadFiles.size() <= 0) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpRmdPicConfirmationEvidence") + " File "
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}
		
		return flag;
	}
	
	public void save(){
		try {
			//this.targetDate = facesUtil.retrieveRequestParam("tglTindakLanjut");
			this.note = facesUtil.retrieveRequestParam("keterangan");
			
			/*if (!StringUtils.isEmpty(targetDate)) {
				trcRmdPicFollowup.setFollowupDate(sdf.parse(targetDate));
			}*/
			if (!StringUtils.isEmpty(note)) {
				trcRmdPicFollowup.setFollowupNote(note);
			}
			if (isValidate()) {
				if (trcRmdPicFollowup.getRmdPicFollowupId() != null) {
					if (trcRmdPicFollowup.getPicFollowupAttachmentDetails() == null || trcRmdPicFollowup.getPicFollowupAttachmentDetails().size() == 0) {
						trcRmdPicFollowup.setPicFollowupAttachmentDetails(new ArrayList<TrcRmdPicFollowupAttachment>());
					}
					trcRmdPicFollowup.getPicFollowupAttachmentDetails().clear();
					
					if (uploadFiles != null) {
						for (int i = 0; i < uploadFiles.size(); i++) {
							TrcRmdPicFollowupAttachment vo = new TrcRmdPicFollowupAttachment();
							UploadedFileWO uf = (UploadedFileWO) uploadFiles.get(i);
							vo.setTrcRmdPicFollowup(trcRmdPicFollowup);
							vo.setFileId(uf.getFileId());
							vo.setFileSize(uf.getFileSize());
							vo.setAttachmentFile(uf.getFileName());
							vo.setCreatedBy(facesUtil.retrieveUserLogin());
							vo.setCreationDate(new Timestamp(new Date().getTime()));
							vo.setDelId(new Long(0));
							vo.setEnabledFlag(Constants.CONSTANT_YES);
							trcRmdPicFollowup.getPicFollowupAttachmentDetails().add(vo);
						}
					}
					
					trcRmdPicFollowup.setConfirmationDate(new Date());
					ParameterDetail followupStatus = parameterDetailService.getParameterDetailByParamDtlCode(
							ParameterDetail.PARAM_DET_CODE_PIC_FOLLOWUP_STATUS_PIC_DONE);
					trcRmdPicFollowup.setFollowupStatus(followupStatus);
					trcRmdPicFollowup.setFollowupBy(userService.getUserByNik(facesUtil.retrieveUserLogin()));

					trcRmdPicFollowup.setLastUpdateBy(facesUtil.retrieveUserLogin());
					trcRmdPicFollowup.setLastUpdateDate(new Timestamp(new Date().getTime()));
					trcRmdPicFollowup.setDelId(new Long(0));
					trcRmdPicFollowup.setEnabledFlag(Constants.CONSTANT_YES);
					trcRmdPicFollowupService.update(trcRmdPicFollowup);
				}
				
				if (deleteFiles != null) {
					for (int i = 0; i < deleteFiles.size(); i++) {
						UploadedFileWO uf = (UploadedFileWO) deleteFiles.get(i);
						CallApiManager.deleteFile(uf.getFileId(), parameterDetailService, fileUtil);
					}
				}
				
				facesUtil.redirect("/pages/regulatoryReportingFE/regulatoryReportingFE.faces");
			}
		} catch (Exception e) {
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + e.getMessage(), "");
			e.printStackTrace();
		}
	}
	
	public void cancel() {
		try {
			if (uploadFiles != null) {
				for (int i = 0; i < uploadFiles.size(); i++) {
					UploadedFileWO uf = (UploadedFileWO) uploadFiles.get(i);
					if (uf.getIsNew() ==  null) {
						CallApiManager.deleteFile(uf.getFileId(), parameterDetailService, fileUtil);
					}
				}
			}
			
			facesUtil.setSessionAttribute("BACK_SESSION", true);
			facesUtil.redirect("/pages/regulatoryReportingFE/regulatoryReportingFE.faces");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void downloadFile(String fileId, String fileName, byte[] content) throws Exception {
		CallApiManager.downloadFile(fileId, fileName, content, parameterDetailService);
	}
	
	public void deleteAttachment(String fileId, int index,String uploadType) throws Exception{
		deleteFiles = deleteFiles != null ? deleteFiles : new ArrayList<UploadedFileWO>();
		deleteFiles.add(new UploadedFileWO(fileId, null, null, null));
		if (uploadType != null && uploadType.equals(RegulatoryReportingFEConstant.UPLOAD_TYPE_DOCUMENT)) {
			uploadFiles.remove(uploadFiles.get(index));			
		}
	}
	
	public RegulatoryReportingFEService getRegulatoryReportingFEService() {
		return regulatoryReportingFEService;
	}

	public void setRegulatoryReportingFEService(RegulatoryReportingFEService regulatoryReportingFEService) {
		this.regulatoryReportingFEService = regulatoryReportingFEService;
	}

	public EmailTemplateService getEmailTemplateService() {
		return emailTemplateService;
	}

	public void setEmailTemplateService(EmailTemplateService emailTemplateService) {
		this.emailTemplateService = emailTemplateService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public TrcRmd getTrcRmd() {
		return trcRmd;
	}

	public void setTrcRmd(TrcRmd trcRmd) {
		this.trcRmd = trcRmd;
	}

	public List<UploadedFileWO> getUploadFiles() {
		return uploadFiles;
	}

	public void setUploadFiles(List<UploadedFileWO> uploadFiles) {
		this.uploadFiles = uploadFiles;
	}

	public List<UploadedFileWO> getDeleteFiles() {
		return deleteFiles;
	}

	public void setDeleteFiles(List<UploadedFileWO> deleteFiles) {
		this.deleteFiles = deleteFiles;
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

	public static Logger getLogger() {
		return logger;
	}

	public static String getNavigateBack() {
		return NAVIGATE_BACK;
	}

	public String getEditId() {
		return editId;
	}

	public void setEditId(String editId) {
		this.editId = editId;
	}

	public String getActionMode() {
		return actionMode;
	}

	public void setActionMode(String actionMode) {
		this.actionMode = actionMode;
	}

	public SimpleDateFormat getSdf() {
		return sdf;
	}

	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}

	public String getTargetDate() {
		return targetDate;
	}

	public void setTargetDate(String targetDate) {
		this.targetDate = targetDate;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public TrcRmdPicFollowup getTrcRmdPicFollowup() {
		return trcRmdPicFollowup;
	}

	public void setTrcRmdPicFollowup(TrcRmdPicFollowup trcRmdPicFollowup) {
		this.trcRmdPicFollowup = trcRmdPicFollowup;
	}

	public TrcRmdPicFollowupService getTrcRmdPicFollowupService() {
		return trcRmdPicFollowupService;
	}

	public void setTrcRmdPicFollowupService(TrcRmdPicFollowupService trcRmdPicFollowupService) {
		this.trcRmdPicFollowupService = trcRmdPicFollowupService;
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
