package com.wo.module.outgoingLetter.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.outgoingLetter.constant.OutgoingLetterConstants;
import com.wo.module.outgoingLetter.model.OutgoingLetter;
import com.wo.module.outgoingLetter.model.OutgoingLetterAttachment;
import com.wo.module.outgoingLetter.service.OutgoingLetterService;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.user.model.User;

public class OutgoingLetterEditBean extends CommonBean implements Serializable {

	private static final long serialVersionUID = -5311445213853990409L;
	static Logger logger = Logger.getLogger(OutgoingLetterEditBean.class);
	
	private OutgoingLetter outgoingLetter;
	
	private Boolean isViewOnly;
	
	private String actionMode;
	
	private String editId;
	private String textWarningUpload;
	
	private List<UploadedFileWO> uploadFilesOutgoingLetters;
	private List<UploadedFileWO> uploadFilesReceipt;
	private List<UploadedFileWO> deleteFiles;
	private List<UploadedFileWO> deleteReceiptFiles;
	
	private OutgoingLetterService outgoingLetterService;
	
	private FacesUtil facesUtil;
	private FileUtil fileUtil;
	
	private String navigateSearch = OutgoingLetterConstants.NAVIGATE_SEARCH;

	public void addMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void addErrMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
	@PostConstruct
	public void init(){
		super.init();
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
		String editId = facesUtil.retrieveRequestParam("id");
		String token = facesUtil.retrieveRequestParam("token");
		String viewId = facesUtil.retrieveRequestParam("viewId");
		
		isViewOnly = false;
		
		if(viewId != null && !viewId.isEmpty()) {
			if(viewId.trim().equalsIgnoreCase("true")) {
				isViewOnly = true;
			}
		}
		
		if (StringUtils.isBlank(editId) && StringUtils.isBlank(token)) {
			this.handleNew();
		} else {
			this.handleEdit(editId);
		}
	}
	
	private void handleNew() {
		try {
			outgoingLetter = new OutgoingLetter();
			
			uploadFilesOutgoingLetters = new ArrayList<UploadedFileWO>();
			
			actionMode = Constants.ACTION_ADD;
			
			facesUtil.setSessionAttribute("token", null);
			
		} catch (Exception e) {
			e.printStackTrace();
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
		outgoingLetter = outgoingLetterService.findById(idLong);
		
		uploadFilesOutgoingLetters = new ArrayList<UploadedFileWO>();
		
		uploadFilesReceipt = new ArrayList<UploadedFileWO>();
		
		for (int i = 0; i < outgoingLetter.getOutgoingLetterAttachments().size(); i++) {
			OutgoingLetterAttachment ra = outgoingLetter.getOutgoingLetterAttachments().get(i);
			UploadedFileWO uf = new UploadedFileWO();
			uf.setFileName(ra.getAttachmentFile());
			uf.setFileId(ra.getFileId());
			uf.setIsNew(false);
			uf.setFileSize(ra.getFileSize());
			if(ra.getAttachmentType()!=null && ra.getAttachmentType().equals(OutgoingLetterConstants.OUTGOING_LETTER_ATTACHMENT)){
				uploadFilesOutgoingLetters.add(uf);
			}else{
				uploadFilesReceipt.add(uf);
			}
		}
	}
	
	public Boolean validate() {
		Boolean flag = false;
		
		try {
			if (actionMode.equals(Constants.ACTION_ADD)) {
				
				Integer validateSameValue = outgoingLetterService.getOutgoingLetterByLetterInAndNo(
						outgoingLetter.getLetterPurposeIn(),
						outgoingLetter.getLetterNo());
				
				if (validateSameValue > 0) {
					facesUtil.addErrMessage(facesUtil.retrieveMessage("formOutgoingLetterPurpose") + /*" "
							+ facesUtil.retrieveMessage("indonesia") +*/ " & "
							+ facesUtil.retrieveMessage("formOutgoingLetterNo") + " "
							+ facesUtil.retrieveMessage("errorAlreadyExists") );
					flag = true;
				}
				
			} else if (actionMode.equals(Constants.ACTION_EDIT)) {
				
				Integer validateSameValue = outgoingLetterService.getOutgoingLetterByLetterInAndNo(
						outgoingLetter.getOutgoingLetterId(),
						outgoingLetter.getLetterPurposeIn(), 
						outgoingLetter.getLetterNo());
				
				if (validateSameValue > 0) {
					facesUtil.addErrMessage(facesUtil.retrieveMessage("formOutgoingLetterPurpose") + /*" "
							+ facesUtil.retrieveMessage("indonesia") +*/ " & "
							+ facesUtil.retrieveMessage("formOutgoingLetterNo") + " "
							+ facesUtil.retrieveMessage("errorAlreadyExists") );
					flag = true;
				}
				
			}
			
			if(StringUtils.isEmpty(outgoingLetter.getLetterPurposeIn())) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formOutgoingLetterPurpose") + " "
//						+ facesUtil.retrieveMessage("indonesia") + " " 
						+ facesUtil.retrieveMessage("validateRequired"));
				flag = true;
			}
			
			if (StringUtils.isEmpty(outgoingLetter.getLetterNo())) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formOutgoingLetterNo") + " "
						+ facesUtil.retrieveMessage("validateRequired"));
				flag = true;
			}
			
			if (outgoingLetter.getLetterDate() == null) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formOutgoingLetterDate") + " "
						+ facesUtil.retrieveMessage("validateRequired"));
				flag = true;
			}
			
			if (StringUtils.isEmpty(outgoingLetter.getPerihalIn())) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formOutgoingLetterPerihal") + " "
//						+ facesUtil.retrieveMessage("indonesia") + " "
						+ facesUtil.retrieveMessage("validateRequired"));
				flag = true;
			}
			
//			if (StringUtils.isEmpty(outgoingLetter.getDeliveredTo())) {
//				facesUtil.addErrMessage(facesUtil.retrieveMessage("formOutgoingLetterDeliveredTo") + " "
//						+ facesUtil.retrieveMessage("validateRequired"));
//				flag = true;
//			}
			
			if (StringUtils.isEmpty(outgoingLetter.getTembusan())) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formOutgoingLetterCarbonCopyNatation") + " "
						+ facesUtil.retrieveMessage("validateRequired"));
				flag = true;
			}
			
			if (uploadFilesOutgoingLetters == null || uploadFilesOutgoingLetters.size() <= 0) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formOutgoingLetterSubTitle") + " File "
						+ facesUtil.retrieveMessage("validateRequired"));
				flag = true;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return flag;
		
	}
	
	public void save () {
		try {
			
			if(!validate()) {
				if(outgoingLetter.getOutgoingLetterAttachments() == null || outgoingLetter.getOutgoingLetterAttachments().size() <= 0) {
					outgoingLetter.setOutgoingLetterAttachments(new ArrayList<OutgoingLetterAttachment>());
				}
				outgoingLetter.getOutgoingLetterAttachments().clear();
				
				if(uploadFilesOutgoingLetters != null) {
					for (int i = 0; i < uploadFilesOutgoingLetters.size(); i++) {
						OutgoingLetterAttachment doc = new OutgoingLetterAttachment();
						UploadedFileWO uf = (UploadedFileWO) uploadFilesOutgoingLetters.get(i);
						doc.setOutgoingLetter(outgoingLetter);
						
						doc.setAttachmentFile(uf.getFileName());
						doc.setAttachmentType(OutgoingLetterConstants.OUTGOING_LETTER_ATTACHMENT);
						doc.setCreatedBy(facesUtil.retrieveUserLogin());
						doc.setCreationDate(new Timestamp(new Date().getTime()));
						doc.setDelId(new Long(0));
						doc.setEnabledFlag(Constants.CONSTANT_YES);
						
						doc.setFileId(uf.getFileId());
						doc.setFileSize(uf.getFileSize());
						outgoingLetter.getOutgoingLetterAttachments().add(doc);
					}
				}
				
				if(uploadFilesReceipt != null) {
					for (int i = 0; i < uploadFilesReceipt.size(); i++) {
						OutgoingLetterAttachment doc = new OutgoingLetterAttachment();
						UploadedFileWO uf = (UploadedFileWO) uploadFilesReceipt.get(i);
						doc.setOutgoingLetter(outgoingLetter);
						doc.setAttachmentType(OutgoingLetterConstants.OUTGOING_LETTER_RECEIPT);
						doc.setAttachmentFile(uf.getFileName());
						doc.setCreatedBy(facesUtil.retrieveUserLogin());
						doc.setCreationDate(new Timestamp(new Date().getTime()));
						doc.setDelId(new Long(0));
						doc.setEnabledFlag(Constants.CONSTANT_YES);
						
						doc.setFileId(uf.getFileId());
						doc.setFileSize(uf.getFileSize());
						outgoingLetter.getOutgoingLetterAttachments().add(doc);
					}
				}
				
				User user = facesUtil.getUserLogin();
				outgoingLetter.setUserDivisionId(user.getDivisionId());
				
				if(outgoingLetter.getOutgoingLetterId() != null) {
					outgoingLetter.setLastUpdateBy(facesUtil.retrieveUserLogin());
					outgoingLetter.setLastUpdateDate(new Timestamp(new Date().getTime()));
					outgoingLetter.setDelId(new Long(0));
					outgoingLetter.setEnabledFlag(Constants.CONSTANT_YES);
					outgoingLetterService.update(outgoingLetter);
				} else {
					outgoingLetter.setCreatedBy(facesUtil.retrieveUserLogin());
					outgoingLetter.setCreationDate(new Timestamp(new Date().getTime()));
					outgoingLetter.setDelId(new Long(0));
					outgoingLetter.setEnabledFlag(Constants.CONSTANT_YES);
					outgoingLetterService.save(outgoingLetter);
				}
				
				if(deleteFiles != null) {
					for (int i = 0; i < deleteFiles.size(); i++) {
						UploadedFileWO uf = (UploadedFileWO) deleteFiles.get(i);
						CallApiManager.deleteFile(uf.getFileId(), parameterDetailService, fileUtil);
					}
				}
				if(deleteReceiptFiles != null) {
					for (int i = 0; i < deleteReceiptFiles.size(); i++) {
						UploadedFileWO uf = (UploadedFileWO) deleteReceiptFiles.get(i);
						CallApiManager.deleteFile(uf.getFileId(), parameterDetailService, fileUtil);
					}
				}
				
				facesUtil.redirect("/pages/outgoingLetter/outgoingLetter.faces");
			}	
		} catch (Exception e) {
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + e.getMessage(), " ");
		}
	}
	
	public void handleFileUploadOutgoingLetter (FileUploadEvent event) {
		try {
			uploadFilesOutgoingLetters = uploadFilesOutgoingLetters == null ? new ArrayList<UploadedFileWO>() : uploadFilesOutgoingLetters;
			uploadFilesOutgoingLetters.add(new UploadedFileWO( CallApiManager.callUploadAPI(event.getFile(), Constants.OUTGOING_LETTER, parameterDetailService, false, fileUtil),
					event.getFile().getFileName(),event.getFile().getContentType(),event.getFile().getSize()));
		} catch (Exception e) {
			e.printStackTrace();
			facesUtil.addErrMessage(e.getMessage());
		}
	}
	
	public void deleteAttachment (String fileId, int index, String uploadType) throws Exception {
		deleteFiles = deleteFiles != null ? deleteFiles : new ArrayList<UploadedFileWO>();
		deleteFiles.add(new UploadedFileWO(fileId,null,null,null));
		
		//if(uploadType != null && uploadType.equals(OutgoingLetterConstants.OUTGOING_LETTER_ATTACHMENT)) {
			uploadFilesOutgoingLetters.remove(uploadFilesOutgoingLetters.get(index));
		//}
	}
	
	public void downloadFile (String fileId, String fileName, byte[] content) throws Exception {
		CallApiManager.downloadFile(fileId, fileName, content, parameterDetailService);
	}
	
	public void handleReceiptFileUpload (FileUploadEvent event) {
		try {
			uploadFilesReceipt = uploadFilesReceipt == null ? new ArrayList<UploadedFileWO>() : uploadFilesReceipt;
			uploadFilesReceipt.add(new UploadedFileWO( CallApiManager.callUploadAPI(event.getFile(), Constants.RECEIPT, parameterDetailService, false, fileUtil),
					event.getFile().getFileName(),event.getFile().getContentType(),event.getFile().getSize()));
		} catch (Exception e) {
			e.printStackTrace();
			facesUtil.addErrMessage(e.getMessage());
		}
	}
	
	public void deleteReceiptAttachment (String fileId, int index, String uploadType) throws Exception {
		deleteReceiptFiles = deleteReceiptFiles != null ? deleteReceiptFiles : new ArrayList<UploadedFileWO>();
		deleteReceiptFiles.add(new UploadedFileWO(fileId,null,null,null));
		
		//if(uploadType != null && uploadType.equals(OutgoingLetterConstants.OUTGOING_LETTER_RECEIPT)) {
			uploadFilesReceipt.remove(uploadFilesReceipt.get(index));
		//}
	}
	
	
	
	public void cancel () {
		try {
			if(uploadFilesOutgoingLetters != null) {
				for (int i = 0; i < uploadFilesOutgoingLetters.size(); i++) {
					UploadedFileWO uf = (UploadedFileWO) uploadFilesOutgoingLetters.get(i);
					if(uf.getIsNew() == null) {
						CallApiManager.deleteFile(uf.getFileId(), parameterDetailService, fileUtil);
					}
				}
			}
			facesUtil.redirect("/pages/outgoingLetter/outgoingLetter.faces");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public OutgoingLetter getOutgoingLetter() {
		return outgoingLetter;
	}

	public void setOutgoingLetter(OutgoingLetter outgoingLetter) {
		this.outgoingLetter = outgoingLetter;
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

	public String getEditId() {
		return editId;
	}

	public void setEditId(String editId) {
		this.editId = editId;
	}

	public List<UploadedFileWO> getUploadFilesOutgoingLetters() {
		return uploadFilesOutgoingLetters;
	}

	public void setUploadFilesOutgoingLetters(List<UploadedFileWO> uploadFilesOutgoingLetters) {
		this.uploadFilesOutgoingLetters = uploadFilesOutgoingLetters;
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

	public String getNavigateSearch() {
		return navigateSearch;
	}

	public void setNavigateSearch(String navigateSearch) {
		this.navigateSearch = navigateSearch;
	}

	public OutgoingLetterService getOutgoingLetterService() {
		return outgoingLetterService;
	}

	public void setOutgoingLetterService(OutgoingLetterService outgoingLetterService) {
		this.outgoingLetterService = outgoingLetterService;
	}

	public List<UploadedFileWO> getDeleteFiles() {
		return deleteFiles;
	}

	public void setDeleteFiles(List<UploadedFileWO> deleteFiles) {
		this.deleteFiles = deleteFiles;
	}

	public List<UploadedFileWO> getUploadFilesReceipt() {
		return uploadFilesReceipt;
	}

	public void setUploadFilesReceipt(List<UploadedFileWO> uploadFilesReceipt) {
		this.uploadFilesReceipt = uploadFilesReceipt;
	}

	public List<UploadedFileWO> getDeleteReceiptFiles() {
		return deleteReceiptFiles;
	}

	public void setDeleteReceiptFiles(List<UploadedFileWO> deleteReceiptFiles) {
		this.deleteReceiptFiles = deleteReceiptFiles;
	}

	public String getTextWarningUpload() {
		return textWarningUpload;
	}

	public void setTextWarningUpload(String textWarningUpload) {
		this.textWarningUpload = textWarningUpload;
	}
	
	
}