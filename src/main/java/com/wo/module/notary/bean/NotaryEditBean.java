package com.wo.module.notary.bean;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.primefaces.event.FileUploadEvent;

import com.wo.module.article.model.ArticleTag;
import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.model.UploadedFileWO;
import com.wo.module.common.util.FileUtil;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.notary.constant.NotaryConstants;
import com.wo.module.notary.model.Notary;
import com.wo.module.notary.service.NotaryService;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.outgoingLetter.constant.OutgoingLetterConstants;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;

public class NotaryEditBean extends CommonBean implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(NotaryEditBean.class);

	private Notary notary;

	private Boolean isViewOnly;

	private String actionMode;
	
	private List<String> keywords;

	private String editedId;

	private NotaryService notaryService;

	public FacesUtil facesUtil;
	
	private Integer lastSequenceOfDtl;

	private List<SelectItem> nameList;
	private List<SelectItem> slaTypeList;
	private List<SelectItem> categoryList;
	
	private List<UploadedFileWO> uploadFiles;
	private List<UploadedFileWO> deleteFiles;
	
	private FileUtil fileUtil;

	private boolean checkAll;

	private String navigateSeanotaryh = NotaryConstants.NAVIGATE_SEARCH;

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
		initList();
		checkNewOrEdit();
		fileUtil = FileUtil.getInstance();
	}
	
	public void initList(){
		try {
		categoryList = new ArrayList<SelectItem>();
		List<ParameterDetail> listCategory = parameterDetailService
					.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_NOTARY_CATEGORY);
		

		for (ParameterDetail vo : listCategory) {
			SelectItem si = new SelectItem();
			si.setLabel(vo.getName());
			si.setValue(vo.getParameterDtlCode());
			categoryList.add(si);
		}
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


	
	private void checkNewOrEdit() {
		try {
			String editId = facesUtil.retrieveRequestParam("id");String token = facesUtil.retrieveRequestParam("token"); 
			
			String viewId = facesUtil.retrieveRequestParam("viewId");
			isViewOnly = false;
			if (viewId != null && !viewId.isEmpty()) {
				if (viewId.trim().equalsIgnoreCase("true")) {
					isViewOnly = true;
				}
			}
			if (StringUtils.isBlank(editId) && StringUtils.isBlank(token)) {
				this.handleNew();

			} else {
				this.handleEdit(editId);
			}
		} catch (Exception e) {

		}

	}

	private void handleNew() {
		notary = new Notary();
		ParameterDetail pd = new ParameterDetail();
		notary.setNotaryCategory(pd);
		lastSequenceOfDtl = 0;
		actionMode = Constants.ACTION_ADD;
		uploadFiles = new ArrayList<UploadedFileWO>();
		facesUtil.setSessionAttribute("token", null);
	}

	private void handleEdit(String editId) {
		String token = facesUtil.retrieveRequestParam("token");
		if(StringUtils.isNotEmpty(token)) {
			editId = Constants.decryptString(token);
		}
		facesUtil.setSessionAttribute("token", Constants.encryptString(editId));
		actionMode = Constants.ACTION_EDIT;
		Long idLong = Long.parseLong(editId);
		notary = notaryService.findById(idLong);
		
		
		lastSequenceOfDtl = 0;
		
		
	}

	public Boolean validate() {
		Boolean flag = false;

		if (StringUtils.isEmpty(notary.getNotaryCategory().getParameterDtlCode())) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formNotaryCategory") 
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}
		if (StringUtils.isEmpty(notary.getArea())) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formNotaryArea") 
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}
		if (StringUtils.isEmpty(notary.getNotaryName())) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formNotaryNotarisName") 
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}
		if (StringUtils.isEmpty(notary.getAddress())) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formNotaryOfficeAddr") 
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}
		if (StringUtils.isEmpty(notary.getAreaCode())) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formNotaryAreaCode") 
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}
		if (StringUtils.isEmpty(notary.getPhoneNo())) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formNotaryTelpNo") 
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}


		return flag;
	}

	public void save() {
		try {
			if (!validate()) {
				
				
				ParameterDetail pd = parameterDetailService.getParameterDetailByParamDtlCode(notary.getNotaryCategory().getParameterDtlCode());
				notary.setNotaryCategory(pd);

				if (notary.getNotaryId() != null) {
					notary.setLastUpdateBy(facesUtil.retrieveUserLogin());
					notary.setLastUpdateDate(new Timestamp(new Date().getTime()));
					notary.setDelId(new Long(0));
					notary.setEnabledFlag(Constants.CONSTANT_YES);
					notaryService.update(notary);

				} else {
					notary.setCreatedBy(facesUtil.retrieveUserLogin());
					notary.setCreationDate(new Timestamp(new Date().getTime()));
					notary.setDelId(new Long(0));
					notary.setEnabledFlag(Constants.CONSTANT_YES);
					notaryService.save(notary);
				}

				facesUtil.redirect("/pages/notary/notary.faces");
			}

		} catch (Exception ex) {
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + ex.getMessage(), "");
		}

	}
	
	public void handleFileUpload (FileUploadEvent event) {
		try {
			uploadFiles = uploadFiles == null ? new ArrayList<UploadedFileWO>() : uploadFiles;
			uploadFiles.add(new UploadedFileWO( CallApiManager.callUploadAPI(event.getFile(), Constants.ARTICLE, parameterDetailService, false, fileUtil),
					event.getFile().getFileName(),event.getFile().getContentType(),event.getFile().getSize()));
		} catch (Exception e) {
			e.printStackTrace();
			facesUtil.addErrMessage(e.getMessage());
		}
	}
	
	public void deleteAttachment (String fileId, int index, String uploadType) throws Exception {
		deleteFiles = deleteFiles != null ? deleteFiles : new ArrayList<UploadedFileWO>();
		deleteFiles.add(new UploadedFileWO(fileId,null,null,null));
		
		if(uploadType != null && uploadType.equals(OutgoingLetterConstants.UPLOAD_TYPE_DOCUMENT)) {
			uploadFiles.remove(uploadFiles.get(index));
		}
	}
	
	public void downloadFile (String fileId, String fileName, byte[] content) throws Exception {
		CallApiManager.downloadFile(fileId, fileName, content, parameterDetailService);
	}

	public void cancel() {
		try {
			facesUtil.redirect("/pages/notary/notary.faces");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

	public NotaryService getNotaryService() {
		return notaryService;
	}

	public void setNotaryService(NotaryService notaryService) {
		this.notaryService = notaryService;
	}

	public String getNavigateSeanotaryh() {
		return navigateSeanotaryh;
	}

	public void setNavigateSeanotaryh(String navigateSeanotaryh) {
		this.navigateSeanotaryh = navigateSeanotaryh;
	}

	public Notary getNotary() {
		return notary;
	}

	public void setNotary(Notary notary) {
		this.notary = notary;
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


	public List<SelectItem> getNameList() {
		return nameList;
	}

	public void setNameList(List<SelectItem> nameList) {
		this.nameList = nameList;
	}

	public List<SelectItem> getSlaTypeList() {
		return slaTypeList;
	}

	public void setSlaTypeList(List<SelectItem> slaTypeList) {
		this.slaTypeList = slaTypeList;
	}

	public boolean isCheckAll() {
		return checkAll;
	}

	public void setCheckAll(boolean checkAll) {
		this.checkAll = checkAll;
	}


	public Integer getLastSequenceOfDtl() {
		return lastSequenceOfDtl;
	}

	public void setLastSequenceOfDtl(Integer lastSequenceOfDtl) {
		this.lastSequenceOfDtl = lastSequenceOfDtl;
	}

	public Notary getRc() {
		return notary;
	}

	public void setRc(Notary notary) {
		this.notary = notary;
	}

	public NotaryService getRcService() {
		return notaryService;
	}

	public void setRcService(NotaryService notaryService) {
		this.notaryService = notaryService;
	}


	public List<SelectItem> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<SelectItem> categoryList) {
		this.categoryList = categoryList;
	}

	public List<UploadedFileWO> getUploadFiles() {
		return uploadFiles;
	}

	public void setUploadFiles(List<UploadedFileWO> uploadFiles) {
		this.uploadFiles = uploadFiles;
	}

	public FileUtil getFileUtil() {
		return fileUtil;
	}

	public void setFileUtil(FileUtil fileUtil) {
		this.fileUtil = fileUtil;
	}

	public List<UploadedFileWO> getDeleteFiles() {
		return deleteFiles;
	}

	public void setDeleteFiles(List<UploadedFileWO> deleteFiles) {
		this.deleteFiles = deleteFiles;
	}

	public List<String> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}
	
	

}