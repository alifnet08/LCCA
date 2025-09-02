package com.wo.module.outgoingLetterView.bean;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.commons.lang3.StringUtils;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.model.UploadedFileWO;
import com.wo.module.common.util.FileUtil;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.outgoingLetter.constant.OutgoingLetterConstants;
import com.wo.module.outgoingLetterView.constant.OutgoingLetterViewConstant;
import com.wo.module.outgoingLetterView.model.OutgoingLetterAttachmentView;
import com.wo.module.outgoingLetterView.model.OutgoingLetterView;
import com.wo.module.outgoingLetterView.service.OutgoingLetterViewService;
import com.wo.module.parameter.service.ParameterDetailService;

public class OutgoingLetterViewEditBean extends CommonBean implements Serializable{

	private static final long serialVersionUID = -9051068551080105189L;

	private OutgoingLetterView outgoingLetterView;
	
	private Boolean isViewOnly;
	
	private String actionMode;
	
	private String editId;
	
	private List<UploadedFileWO> uploadFilesOutgoingLetters;
	private List<UploadedFileWO> uploadFilesReceipt;
	
	private OutgoingLetterViewService outgoingLetterViewService;
	private ParameterDetailService parameterDetailService;
	
	private FacesUtil facesUtil;
	private FileUtil fileUtil;
	
	private String navigateSearch = OutgoingLetterViewConstant.NAVIGATE_SEARCH;
	
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
		checkNewOrEdit();
		fileUtil = FileUtil.getInstance();
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
		outgoingLetterView = outgoingLetterViewService.findById(idLong);
		
		uploadFilesOutgoingLetters = new ArrayList<UploadedFileWO>();
		uploadFilesReceipt = new ArrayList<UploadedFileWO>();
		
		for (int i = 0; i < outgoingLetterView.getOutgoingLetterAttachmentView().size(); i++) {
			OutgoingLetterAttachmentView ra = outgoingLetterView.getOutgoingLetterAttachmentView().get(i);
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
	
	public void cancel() {
		try {
			facesUtil.redirect("/pages/outgoingLetterView/outgoingLetterView.faces");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void downloadFile(String fileId, String fileName, byte[] content) throws Exception {
		CallApiManager.downloadFile(fileId, fileName, content, parameterDetailService);
	}
	
	public OutgoingLetterView getOutgoingLetterView() {
		return outgoingLetterView;
	}

	public void setOutgoingLetterView(OutgoingLetterView outgoingLetterView) {
		this.outgoingLetterView = outgoingLetterView;
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

	public OutgoingLetterViewService getOutgoingLetterViewService() {
		return outgoingLetterViewService;
	}

	public void setOutgoingLetterViewService(OutgoingLetterViewService outgoingLetterViewService) {
		this.outgoingLetterViewService = outgoingLetterViewService;
	}

	public ParameterDetailService getParameterDetailService() {
		return parameterDetailService;
	}

	public void setParameterDetailService(ParameterDetailService parameterDetailService) {
		this.parameterDetailService = parameterDetailService;
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

	public List<UploadedFileWO> getUploadFilesReceipt() {
		return uploadFilesReceipt;
	}

	public void setUploadFilesReceipt(List<UploadedFileWO> uploadFilesReceipt) {
		this.uploadFilesReceipt = uploadFilesReceipt;
	}
	
}
