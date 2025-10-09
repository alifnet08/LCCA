package com.wo.module.article.bean;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.primefaces.event.FileUploadEvent;

import com.wo.module.article.constant.ArticleConstants;
import com.wo.module.article.model.TmpArticle;
import com.wo.module.article.model.TmpArticleDocument;
import com.wo.module.article.model.TmpArticleTag;
import com.wo.module.article.service.ArticleService;
import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.exception.CustomAPIException;
import com.wo.module.common.model.UploadedFileWO;
import com.wo.module.common.util.FileUtil;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.emailTemplate.model.EmailTemplate;
import com.wo.module.emailTemplate.service.EmailTemplateService;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.outgoingLetter.constant.OutgoingLetterConstants;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;
import com.wo.module.user.model.User;
import com.wo.module.user.service.UserService;

public class ArticleEditBean extends CommonBean implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(ArticleEditBean.class);

//	private Article article;
	private TmpArticle tmpArticle;

	private Boolean isViewOnly;

	private String actionMode;
	
	private String textWarningUpload;
	
	private List<String> tagName;

	private String editedId;

	private ArticleService articleService;
	
	private UserService userService;
	
	private EmailTemplateService emailTemplateService;

	public FacesUtil facesUtil;
	
	private Integer lastSequenceOfDtl;

	private List<SelectItem> nameList;
	private List<SelectItem> slaTypeList;
	private List<SelectItem> articleTypeList;
	private List<SelectItem> activeStatusList;
	
	private List<UploadedFileWO> uploadFiles;
	private List<UploadedFileWO> deleteFiles;
	
	private FileUtil fileUtil;

	private boolean checkAll;

	private String navigateSeaarticleh = ArticleConstants.NAVIGATE_SEARCH;
	
	Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();

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
		articleTypeList = new ArrayList<SelectItem>();
		List<ParameterDetail> listArticleType = parameterDetailService
					.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_ARTICLE_TYPE);
		User userLogin = userService.getUserByNik(facesUtil.retrieveUserLogin());

		for (ParameterDetail vo : listArticleType) {
			SelectItem si = new SelectItem();
			si.setLabel(vo.getName());
			si.setValue(vo.getParameterDtlCode());
			if(userLogin!=null && userLogin.getDivisionName()!=null){
				if(userLogin.getDivisionName().equals("COMPLIANCE")){
					if(si.getValue().equals("COMPLIANCE_FLASH") || si.getValue().equals("COMPLIANCE_OPINION") || si.getValue().equals("GENERAL")|| si.getValue().equals("TRAINING_MATERIAL")|| si.getValue().equals("WORKSHOP")){
						articleTypeList.add(si);
					}
				}else if(userLogin.getDivisionName().equals("CORPORATE LEGAL & LITIGATION")){
					if(si.getValue().equals("LEGAL_OPINION") || si.getValue().equals("LEGAL_REVIEW") || si.getValue().equals("GENERAL")|| si.getValue().equals("TRAINING_MATERIAL")|| si.getValue().equals("WORKSHOP")){
						articleTypeList.add(si);
					}
				}else{
					if(si.getValue().equals("GENERAL")|| si.getValue().equals("TRAINING_MATERIAL")|| si.getValue().equals("WORKSHOP")){
						articleTypeList.add(si);
					}
				}
				
			}
		}
		
		activeStatusList = new ArrayList<SelectItem>();
		List<ParameterDetail> listParamReminderDtl = parameterDetailService
				.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_CODE_DATA_STATUS);
		
		for (ParameterDetail vo : listParamReminderDtl) {
			SelectItem si = new SelectItem();
			
			if (vo.getParameterDtlCode().equals(ParameterDetail.PARAM_DET_CODE_DATA_ACTIVE) ||
					(vo.getParameterDtlCode().equals(ParameterDetail.PARAM_DET_CODE_DATA_INACTIVE))) {
				si.setLabel(vo.getName());
				si.setValue(vo.getParameterDtlCode());
				
				activeStatusList.add(si);
			}
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
		tmpArticle = new TmpArticle();
		ParameterDetail pd = new ParameterDetail();
		tmpArticle.setArticleType(pd);
		tmpArticle.setStatus(new ParameterDetail());
		tmpArticle.setActiveStatus(new ParameterDetail());
		tmpArticle.getActiveStatus().setParameterDtlCode(ParameterDetail.PARAM_DET_CODE_DATA_ACTIVE);
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
		tmpArticle = articleService.findById(idLong);
		
		uploadFiles = new ArrayList<UploadedFileWO>();
		for (int i = 0; i < tmpArticle.getTmpArticleDocumentList().size(); i++) {
			TmpArticleDocument ra = tmpArticle.getTmpArticleDocumentList().get(i);
			UploadedFileWO uf = new UploadedFileWO();
			uf.setFileName(ra.getAttachmentFile());
			uf.setFileId(ra.getFileId());
			uf.setIsNew(false);
			uf.setFileSize(ra.getFileSize());
			uploadFiles.add(uf);
			
		}
		for (int i = 0; i < tmpArticle.getTmpArticleTagList().size(); i++) {
			TmpArticleTag articleTag = tmpArticle.getTmpArticleTagList().get(i);
			tmpArticle.setTagName(articleTag.getTag());
		}
		lastSequenceOfDtl = 0;
		
		List<String> listTag = new ArrayList<String>();
		for (int i = 0; i < tmpArticle.getTmpArticleTagList().size(); i++) {
			TmpArticleTag articleTag = tmpArticle.getTmpArticleTagList().get(i);
			listTag.add(articleTag.getTag());
		}

		if (tmpArticle.getTmpArticleTagList().size() > 0) {
			tagName = listTag;
		}
		
		
	}

	public Boolean validate() {
		Boolean flag = false;

		if (StringUtils.isEmpty(tmpArticle.getArticleType().getParameterDtlCode())) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formArticleType") 
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}

		if (StringUtils.isEmpty(tmpArticle.getAuthor())) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formArticleAuthor") 
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}
		
		if (tmpArticle.getPublishDate()==null) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formArticlePublishDate") 
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}
		
		if (StringUtils.isEmpty(tmpArticle.getArticleTitleIn())) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formArticleTitle") + " "
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}
		
		/*if (StringUtils.isEmpty(tmpArticle.getDescriptionIn())) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formArticleDescription") + " "
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}*/

		

		return flag;
	}

	public void save() {
		try {
			if (!validate()) {
				
				if(tmpArticle.getTmpArticleDocumentList() == null || tmpArticle.getTmpArticleDocumentList().size() <= 0) {
					tmpArticle.setTmpArticleDocumentList(new ArrayList<TmpArticleDocument>());
				}
				tmpArticle.getTmpArticleDocumentList().clear();
				
				if(tmpArticle.getTmpArticleTagList() == null || tmpArticle.getTmpArticleTagList().size() <= 0) {
					TmpArticleTag articleTag = new TmpArticleTag();
					articleTag.setTag(tmpArticle.getTagName());
					articleTag.setCreatedBy(facesUtil.retrieveUserLogin());
					articleTag.setCreationDate(new Timestamp(new Date().getTime()));
					articleTag.setDelId(new Long(0));
					articleTag.setEnabledFlag(Constants.CONSTANT_YES);
					List<TmpArticleTag> list = new ArrayList<TmpArticleTag>();
					list.add(articleTag);
					tmpArticle.setTmpArticleTagList(list);
				}
				
				tmpArticle.getTmpArticleTagList().clear();
				
				if(uploadFiles != null) {
					for (int i = 0; i < uploadFiles.size(); i++) {
						TmpArticleDocument doc = new TmpArticleDocument();
						UploadedFileWO uf = (UploadedFileWO) uploadFiles.get(i);
						doc.setTmpArticle(tmpArticle);
						
						doc.setAttachmentFile(uf.getFileName());
						
						doc.setCreatedBy(facesUtil.retrieveUserLogin());
						doc.setCreationDate(new Timestamp(new Date().getTime()));
						doc.setDelId(new Long(0));
						doc.setEnabledFlag(Constants.CONSTANT_YES);
						
						doc.setFileId(uf.getFileId());
						doc.setFileSize(uf.getFileSize());
						tmpArticle.getTmpArticleDocumentList().add(doc);
					}
				}
				
				/*ArticleTag tag = new ArticleTag();
				tag.setArticle(article);
				tag.setTag(article.getTagName());
				tag.setCreatedBy(facesUtil.retrieveUserLogin());
				tag.setCreationDate(new Timestamp(new Date().getTime()));
				tag.setDelId(new Long(0));
				tag.setEnabledFlag(Constants.CONSTANT_YES);
				article.getArticleTags().add(tag);*/
				
				if (tagName != null) {
						for (int i = 0; i < tagName.size(); i++) {
								TmpArticleTag key = new TmpArticleTag();
								key.setTag(tagName.get(i));
								key.setTmpArticle(tmpArticle);
								key.setCreatedBy(facesUtil.retrieveUserLogin());
								key.setCreationDate(new Timestamp(new Date().getTime()));
								key.setDelId(new Long(0));
								key.setEnabledFlag(Constants.CONSTANT_YES);
								tmpArticle.getTmpArticleTagList().add(key);
							}

							
					}

				ParameterDetail pdActiveStatus = parameterDetailService
						.getParameterDetailByParamDtlCode(tmpArticle.getActiveStatus().getParameterDtlCode());
				tmpArticle.setActiveStatus(pdActiveStatus);
				if (pdActiveStatus.getParameterDtlCode().equals(ParameterDetail.PARAM_DET_CODE_DATA_ACTIVE)) {
					ParameterDetail pdNewStatus = parameterDetailService
							.getParameterDetailByParamDtlCode(ParameterDetail.PARAM_DET_CODE_DATA_NEW);
					tmpArticle.setStatus(pdNewStatus);
				} else if (pdActiveStatus.getParameterDtlCode().equals(ParameterDetail.PARAM_DET_CODE_DATA_INACTIVE)) {
					ParameterDetail pdInactiveStatus = parameterDetailService
							.getParameterDetailByParamDtlCode(ParameterDetail.PARAM_DET_CODE_DATA_INACTIVE);
					tmpArticle.setStatus(pdInactiveStatus);
				}
				
				if (tmpArticle.getArticleId() != null) {
					tmpArticle.setLastUpdateBy(facesUtil.retrieveUserLogin());
					tmpArticle.setLastUpdateDate(new Timestamp(new Date().getTime()));
					tmpArticle.setDelId(new Long(0));
					tmpArticle.setEnabledFlag(Constants.CONSTANT_YES);
					articleService.update(tmpArticle);
				} else {
					tmpArticle.setCreatedBy(facesUtil.retrieveUserLogin());
					tmpArticle.setCreationDate(new Timestamp(new Date().getTime()));
					tmpArticle.setDelId(new Long(0));
					tmpArticle.setEnabledFlag(Constants.CONSTANT_YES);
					articleService.save(tmpArticle);
				}
				
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
				facesUtil.redirect("/pages/article/article.faces");
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
	
	public void sendEmail() {
		try {
			
			EmailTemplate emailTemplate = emailTemplateService.getEmailTemplateByEmailTemplateCode(Constants.EMAIL_APPROVAL);
			String emailSubject = emailTemplate.getEmailSubject()+" - Article/Opinion";
	
			String emailContent = ""; 
			String emailTo  = "";
			String emailCc = "";
		   
			String documentNumberTemp = tmpArticle.getArticleTitleIn() != null ? tmpArticle.getArticleTitleIn() : "";

					emailContent = emailTemplate.getEmailContent().replace(Constants.NOTIFICATION_TYPE_AND_DOC_NUM, tmpArticle.getArticleType().getNameIn() + " - "
							+ documentNumberTemp);
					
					ParameterDetail pdHostName = parameterDetailService.getParameterDetailByParamDtlCode(Constants.HOST_NAME_APPLICATION);
					String token = Constants.encryptString(tmpArticle.getArticleId().toString());
					String menuId = Constants.encryptString(Constants.MENU_ID_APPROVAL_REQUEST_ARTICLE);
					String urlLink = pdHostName.getNameIn().concat("pages/dashboard/dashboard.faces?token="+token+"&menuId="+menuId);
					emailContent = emailContent.replaceAll("url_link", urlLink);
					
					ParameterDetail paramEmail = null;
					/*if(tmpArticle.getArticleType().getParameterDtlCode().equals("COMPLIANCE_FLASH")
					  || tmpArticle.getArticleType().getParameterDtlCode().equals("COMPLIANCE_OPINION")
					  ){
						paramEmail = parameterDetailService.getParameterDetailByParamDtlCode(Constants.ARTICLE_CHECKER_CRA);
					}
					else if(tmpArticle.getArticleType().getParameterDtlCode().equals("LEGAL_OPINION")
							  || tmpArticle.getArticleType().getParameterDtlCode().equals("LEGAL_REVIEW")
							  ){
						paramEmail = parameterDetailService.getParameterDetailByParamDtlCode(Constants.ARTICLE_CHECKER_CLL);
					}else{
					    paramEmail = parameterDetailService.getParameterDetailByParamDtlCode(Constants.ARTICLE_CHECKER);
					}*/
					User userLogin = userService.getUserByNik(facesUtil.retrieveUserLogin());
					if(userLogin.getDivisionName().equals("COMPLIANCE")){
						paramEmail = parameterDetailService.getParameterDetailByParamDtlCode(Constants.ARTICLE_CHECKER_CRA);
					}else if(userLogin.getDivisionName().equals("CORPORATE LEGAL & LITIGATION")){
						paramEmail = parameterDetailService.getParameterDetailByParamDtlCode(Constants.ARTICLE_CHECKER_CLL);
					}else{
						paramEmail = parameterDetailService.getParameterDetailByParamDtlCode(Constants.ARTICLE_CHECKER);
					}
					
					emailTo = paramEmail.getNameIn();

						final String subject = emailSubject;
						final String content = emailContent;
						final String to = emailTo;
						//final String to = "h3ndr407@gmail.com";
						final String cc = emailCc;
						
						CallApiManager.sendEmailAPI(to, cc, subject, content, "EMAIL_ARTICLE", "true", parameterDetailService);
						

			
		    
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new CustomAPIException(ex.getMessage()); 
		}
		
	}

	public void cancel() {
		try {
			facesUtil.redirect("/pages/article/article.faces");
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

	public ArticleService getArticleService() {
		return articleService;
	}

	public void setArticleService(ArticleService articleService) {
		this.articleService = articleService;
	}

	public String getNavigateSeaarticleh() {
		return navigateSeaarticleh;
	}

	public void setNavigateSeaarticleh(String navigateSeaarticleh) {
		this.navigateSeaarticleh = navigateSeaarticleh;
	}

//	public Article getArticle() {
//		return article;
//	}
//
//	public void setArticle(Article article) {
//		this.article = article;
//	}

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

//	public Article getRc() {
//		return article;
//	}
//
//	public void setRc(Article article) {
//		this.article = article;
//	}

	public ArticleService getRcService() {
		return articleService;
	}

	public void setRcService(ArticleService articleService) {
		this.articleService = articleService;
	}

	public List<SelectItem> getArticleTypeList() {
		return articleTypeList;
	}

	public void setArticleTypeList(List<SelectItem> articleTypeList) {
		this.articleTypeList = articleTypeList;
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

	public List<String> getTagName() {
		return tagName;
	}

	public void setTagName(List<String> tagName) {
		this.tagName = tagName;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		ArticleEditBean.logger = logger;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public TmpArticle getTmpArticle() {
		return tmpArticle;
	}

	public void setTmpArticle(TmpArticle tmpArticle) {
		this.tmpArticle = tmpArticle;
	}

	public List<SelectItem> getActiveStatusList() {
		return activeStatusList;
	}

	public void setActiveStatusList(List<SelectItem> activeStatusList) {
		this.activeStatusList = activeStatusList;
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

	public String getTextWarningUpload() {
		return textWarningUpload;
	}

	public void setTextWarningUpload(String textWarningUpload) {
		this.textWarningUpload = textWarningUpload;
	}
	
	
	

}