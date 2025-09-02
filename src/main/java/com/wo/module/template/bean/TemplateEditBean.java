package com.wo.module.template.bean;

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

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.model.UploadedFileWO;
import com.wo.module.common.util.FileUtil;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.template.constant.TemplateConstants;
import com.wo.module.template.model.Template;
import com.wo.module.template.model.TemplateDocument;
import com.wo.module.template.service.TemplateService;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.outgoingLetter.constant.OutgoingLetterConstants;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;

public class TemplateEditBean extends CommonBean implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(TemplateEditBean.class);

	private Template template;

	private Boolean isViewOnly;

	private String actionMode;

	private String editedId;

	private TemplateService templateService;

	public FacesUtil facesUtil;
	
	private Integer lastSequenceOfDtl;

	private List<SelectItem> nameList;
	private List<SelectItem> slaTypeList;
	private List<SelectItem> categoryList;
	private List<SelectItem> subCategoryList;
	
	private List<UploadedFileWO> uploadFiles;
	private List<UploadedFileWO> deleteFiles;
	
	private List<UploadedFileWO> uploadFilesEn;
	private List<UploadedFileWO> deleteFilesEn;
	
	private List<UploadedFileWO> uploadCooperationFiles;
	private List<UploadedFileWO> deleteCooperationFiles;
	
	private List<UploadedFileWO> uploadCooperationFilesEn;
	private List<UploadedFileWO> deleteCooperationFilesEn;
	
	private FileUtil fileUtil;

	private boolean checkAll;

	private String navigateSeatemplateh = TemplateConstants.NAVIGATE_SEARCH;
	private String textWarningUpload;

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
		
		try {
			ParameterDetail getText = parameterDetailService.getParameterDetailByParamDtlCode("UPLOAD_WARNING_TEXT");
			textWarningUpload = getText.getName();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void initList(){
		try {
		categoryList = new ArrayList<SelectItem>();
		List<ParameterDetail> listCategory = parameterDetailService
					.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_TEMPLATE_CATEGORY);
		

		for (ParameterDetail vo : listCategory) {
			SelectItem si = new SelectItem();
			si.setLabel(vo.getName());
			si.setValue(vo.getParameterDtlCode());
			categoryList.add(si);
		}
		
		subCategoryList = new ArrayList<SelectItem>();
		List<ParameterDetail> listSubCategory = parameterDetailService
					.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_TEMPLATE_SUB_CATEGORY);
		

		for (ParameterDetail vo : listSubCategory) {
			SelectItem si = new SelectItem();
			si.setLabel(vo.getName());
			si.setValue(vo.getParameterDtlCode());
			subCategoryList.add(si);
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
		template = new Template();
		ParameterDetail pd = new ParameterDetail();
		template.setTemplateCategory(pd);
		ParameterDetail pd2 = new ParameterDetail();
		template.setTemplateSubCategory(pd2);
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
		template = templateService.findById(idLong);
		
		
		uploadFiles = new ArrayList<UploadedFileWO>();
		uploadFilesEn = new ArrayList<UploadedFileWO>();
		uploadCooperationFiles = new ArrayList<UploadedFileWO>();
		uploadCooperationFilesEn = new ArrayList<UploadedFileWO>();
		for (int i = 0; i < template.getTemplateDocuments().size(); i++) {
			TemplateDocument ra = template.getTemplateDocuments().get(i);
			UploadedFileWO uf = new UploadedFileWO();
			uf.setFileName(ra.getAttachmentFile());
			uf.setFileId(ra.getFileId());
			uf.setIsNew(false);
			uf.setFileSize(ra.getFileSize());
			if(ra.getDocumentType()!=null && ra.getDocumentType().equals("TEMPLATE_DOC_IN")){
				uploadFiles.add(uf);
			}
			else if(ra.getDocumentType()!=null && ra.getDocumentType().equals("TEMPLATE_DOC_EN")){
				uploadFilesEn.add(uf);
			}
			else if(ra.getDocumentType()!=null && ra.getDocumentType().equals("FILL_INSTRUCTION_IN")){
				uploadCooperationFiles.add(uf);
			}
			else if(ra.getDocumentType()!=null && ra.getDocumentType().equals("FILL_INSTRUCTION_EN")){
				uploadCooperationFilesEn.add(uf);
			}
			
			
		}
		
		lastSequenceOfDtl = 0;
		
		
	}

	public Boolean validate() {
		Boolean flag = false;

		if (StringUtils.isEmpty(template.getTemplateCategory().getParameterDtlCode())) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formTemplateType") 
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}

		if (StringUtils.isEmpty(template.getTemplateSubCategory().getParameterDtlCode())) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formTemplateSubject") 
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}
		
		if (StringUtils.isEmpty(template.getPerihalIn())) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formTemplateQuestion") + " in "
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}

		

		return flag;
	}

	public void save() {
		try {
			if (!validate()) {
				
				if(template.getTemplateDocuments() == null || template.getTemplateDocuments().size() <= 0) {
					template.setTemplateDocuments(new ArrayList<TemplateDocument>());
				}
				template.getTemplateDocuments().clear();
				
				
				
				if(uploadFiles != null) {
					for (int i = 0; i < uploadFiles.size(); i++) {
						TemplateDocument doc = new TemplateDocument();
						UploadedFileWO uf = (UploadedFileWO) uploadFiles.get(i);
						doc.setTemplate(template);
						
						doc.setAttachmentFile(uf.getFileName());
						doc.setDocumentType("TEMPLATE_DOC_IN");
						
						doc.setCreatedBy(facesUtil.retrieveUserLogin());
						doc.setCreationDate(new Timestamp(new Date().getTime()));
						doc.setDelId(new Long(0));
						doc.setEnabledFlag(Constants.CONSTANT_YES);
						
						doc.setFileId(uf.getFileId());
						doc.setFileSize(uf.getFileSize());
						template.getTemplateDocuments().add(doc);
					}
				}
				
				if(uploadFilesEn != null) {
					for (int i = 0; i < uploadFilesEn.size(); i++) {
						TemplateDocument doc = new TemplateDocument();
						UploadedFileWO uf = (UploadedFileWO) uploadFilesEn.get(i);
						doc.setTemplate(template);
						
						doc.setAttachmentFile(uf.getFileName());
						doc.setDocumentType("TEMPLATE_DOC_EN");
						
						doc.setCreatedBy(facesUtil.retrieveUserLogin());
						doc.setCreationDate(new Timestamp(new Date().getTime()));
						doc.setDelId(new Long(0));
						doc.setEnabledFlag(Constants.CONSTANT_YES);
						
						doc.setFileId(uf.getFileId());
						doc.setFileSize(uf.getFileSize());
						template.getTemplateDocuments().add(doc);
					}
				}
				
				if(uploadCooperationFiles != null) {
					for (int i = 0; i < uploadCooperationFiles.size(); i++) {
						TemplateDocument doc = new TemplateDocument();
						UploadedFileWO uf = (UploadedFileWO) uploadCooperationFiles.get(i);
						doc.setTemplate(template);
						
						doc.setAttachmentFile(uf.getFileName());
						doc.setDocumentType("FILL_INSTRUCTION_IN");
						
						doc.setCreatedBy(facesUtil.retrieveUserLogin());
						doc.setCreationDate(new Timestamp(new Date().getTime()));
						doc.setDelId(new Long(0));
						doc.setEnabledFlag(Constants.CONSTANT_YES);
						
						doc.setFileId(uf.getFileId());
						doc.setFileSize(uf.getFileSize());
						template.getTemplateDocuments().add(doc);
					}
				}
				
				if(uploadCooperationFilesEn != null) {
					for (int i = 0; i < uploadCooperationFilesEn.size(); i++) {
						TemplateDocument doc = new TemplateDocument();
						UploadedFileWO uf = (UploadedFileWO) uploadCooperationFilesEn.get(i);
						doc.setTemplate(template);
						
						doc.setAttachmentFile(uf.getFileName());
						doc.setDocumentType("FILL_INSTRUCTION_EN");
						
						doc.setCreatedBy(facesUtil.retrieveUserLogin());
						doc.setCreationDate(new Timestamp(new Date().getTime()));
						doc.setDelId(new Long(0));
						doc.setEnabledFlag(Constants.CONSTANT_YES);
						
						doc.setFileId(uf.getFileId());
						doc.setFileSize(uf.getFileSize());
						template.getTemplateDocuments().add(doc);
					}
				}
				
				
				ParameterDetail pd = parameterDetailService.getParameterDetailByParamDtlCode(template.getTemplateCategory().getParameterDtlCode());
				template.setTemplateCategory(pd);
				
				ParameterDetail pd2 = parameterDetailService.getParameterDetailByParamDtlCode(template.getTemplateSubCategory().getParameterDtlCode());
				template.setTemplateSubCategory(pd2);
			

				if (template.getTemplateId() != null) {
					template.setLastUpdateBy(facesUtil.retrieveUserLogin());
					template.setLastUpdateDate(new Timestamp(new Date().getTime()));
					template.setDelId(new Long(0));
					template.setEnabledFlag(Constants.CONSTANT_YES);
					templateService.update(template);

				} else {
					template.setCreatedBy(facesUtil.retrieveUserLogin());
					template.setCreationDate(new Timestamp(new Date().getTime()));
					template.setDelId(new Long(0));
					template.setEnabledFlag(Constants.CONSTANT_YES);
					templateService.save(template);
				}

				facesUtil.redirect("/pages/cooperationTemplate/template.faces");
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
	
	/*public void handleFileUploadEn (FileUploadEvent event) {
		try {
			uploadFilesEn = uploadFilesEn == null ? new ArrayList<UploadedFileWO>() : uploadFilesEn;
			uploadFilesEn.add(new UploadedFileWO( CallApiManager.callUploadAPI(event.getFile(), Constants.ARTICLE, parameterDetailService, false, fileUtil),
					event.getFile().getFileName(),event.getFile().getContentType(),event.getFile().getSize()));
		} catch (Exception e) {
			e.printStackTrace();
			facesUtil.addErrMessage(e.getMessage());
		}
	}
	
	public void deleteAttachmentEn (String fileId, int index, String uploadType) throws Exception {
		deleteFilesEn = deleteFilesEn != null ? deleteFilesEn : new ArrayList<UploadedFileWO>();
		deleteFilesEn.add(new UploadedFileWO(fileId,null,null,null));
		
		if(uploadType != null && uploadType.equals(OutgoingLetterConstants.UPLOAD_TYPE_DOCUMENT)) {
			deleteFilesEn.remove(deleteFilesEn.get(index));
		}
	}
	
	public void downloadFileEn (String fileId, String fileName, byte[] content) throws Exception {
		CallApiManager.downloadFile(fileId, fileName, content, parameterDetailService);
	}*/
	
	public void handleFileUploadCooperation (FileUploadEvent event) {
		try {
			uploadCooperationFiles = uploadCooperationFiles == null ? new ArrayList<UploadedFileWO>() : uploadCooperationFiles;
			uploadCooperationFiles.add(new UploadedFileWO( CallApiManager.callUploadAPI(event.getFile(), Constants.ARTICLE, parameterDetailService, false, fileUtil),
					event.getFile().getFileName(),event.getFile().getContentType(),event.getFile().getSize()));
		} catch (Exception e) {
			e.printStackTrace();
			facesUtil.addErrMessage(e.getMessage());
		}
	}
	
	public void deleteAttachmentCooperation (String fileId, int index, String uploadType) throws Exception {
		deleteCooperationFiles = deleteCooperationFiles != null ? deleteCooperationFiles : new ArrayList<UploadedFileWO>();
		deleteCooperationFiles.add(new UploadedFileWO(fileId,null,null,null));
		
		if(uploadType != null && uploadType.equals(OutgoingLetterConstants.UPLOAD_TYPE_DOCUMENT)) {
			uploadCooperationFiles.remove(uploadCooperationFiles.get(index));
		}
	}
	
	public void downloadFileCooperation (String fileId, String fileName, byte[] content) throws Exception {
		CallApiManager.downloadFile(fileId, fileName, content, parameterDetailService);
	}
	
	/*public void handleFileUploadCooperationEn (FileUploadEvent event) {
		try {
			uploadCooperationFilesEn = uploadCooperationFilesEn == null ? new ArrayList<UploadedFileWO>() : uploadCooperationFilesEn;
			uploadCooperationFilesEn.add(new UploadedFileWO( CallApiManager.callUploadAPI(event.getFile(), Constants.ARTICLE, parameterDetailService, false, fileUtil),
					event.getFile().getFileName(),event.getFile().getContentType(),event.getFile().getSize()));
		} catch (Exception e) {
			e.printStackTrace();
			facesUtil.addErrMessage(e.getMessage());
		}
	}
	
	public void deleteAttachmentCooperationEn (String fileId, int index, String uploadType) throws Exception {
		deleteCooperationFilesEn = deleteCooperationFilesEn != null ? deleteCooperationFilesEn : new ArrayList<UploadedFileWO>();
		deleteCooperationFilesEn.add(new UploadedFileWO(fileId,null,null,null));
		
		if(uploadType != null && uploadType.equals(OutgoingLetterConstants.UPLOAD_TYPE_DOCUMENT)) {
			uploadCooperationFilesEn.remove(uploadCooperationFilesEn.get(index));
		}
	}
	
	public void downloadFileCooperationEn (String fileId, String fileName, byte[] content) throws Exception {
		CallApiManager.downloadFile(fileId, fileName, content, parameterDetailService);
	}*/

	public void cancel() {
		try {
			facesUtil.redirect("/pages/cooperationTemplate/template.faces");
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

	public TemplateService getTemplateService() {
		return templateService;
	}

	public void setTemplateService(TemplateService templateService) {
		this.templateService = templateService;
	}

	public String getNavigateSeatemplateh() {
		return navigateSeatemplateh;
	}

	public void setNavigateSeatemplateh(String navigateSeatemplateh) {
		this.navigateSeatemplateh = navigateSeatemplateh;
	}

	public Template getTemplate() {
		return template;
	}

	public void setTemplate(Template template) {
		this.template = template;
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

	public Template getRc() {
		return template;
	}

	public void setRc(Template template) {
		this.template = template;
	}

	public TemplateService getRcService() {
		return templateService;
	}

	public void setRcService(TemplateService templateService) {
		this.templateService = templateService;
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

	public List<UploadedFileWO> getUploadCooperationFiles() {
		return uploadCooperationFiles;
	}

	public void setUploadCooperationFiles(List<UploadedFileWO> uploadCooperationFiles) {
		this.uploadCooperationFiles = uploadCooperationFiles;
	}

	public List<UploadedFileWO> getDeleteCooperationFiles() {
		return deleteCooperationFiles;
	}

	public void setDeleteCooperationFiles(List<UploadedFileWO> deleteCooperationFiles) {
		this.deleteCooperationFiles = deleteCooperationFiles;
	}

	public List<SelectItem> getSubCategoryList() {
		return subCategoryList;
	}

	public void setSubCategoryList(List<SelectItem> subCategoryList) {
		this.subCategoryList = subCategoryList;
	}

	public List<UploadedFileWO> getUploadFilesEn() {
		return uploadFilesEn;
	}

	public void setUploadFilesEn(List<UploadedFileWO> uploadFilesEn) {
		this.uploadFilesEn = uploadFilesEn;
	}

	public List<UploadedFileWO> getDeleteFilesEn() {
		return deleteFilesEn;
	}

	public void setDeleteFilesEn(List<UploadedFileWO> deleteFilesEn) {
		this.deleteFilesEn = deleteFilesEn;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		TemplateEditBean.logger = logger;
	}

	public List<UploadedFileWO> getUploadCooperationFilesEn() {
		return uploadCooperationFilesEn;
	}

	public void setUploadCooperationFilesEn(List<UploadedFileWO> uploadCooperationFilesEn) {
		this.uploadCooperationFilesEn = uploadCooperationFilesEn;
	}

	public List<UploadedFileWO> getDeleteCooperationFilesEn() {
		return deleteCooperationFilesEn;
	}

	public void setDeleteCooperationFilesEn(List<UploadedFileWO> deleteCooperationFilesEn) {
		this.deleteCooperationFilesEn = deleteCooperationFilesEn;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getTextWarningUpload() {
		return textWarningUpload;
	}

	public void setTextWarningUpload(String textWarningUpload) {
		this.textWarningUpload = textWarningUpload;
	}
	
	

}