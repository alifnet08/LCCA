package com.wo.module.articleApproval.bean;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.wo.module.article.model.Article;
import com.wo.module.article.model.TmpArticle;
import com.wo.module.article.model.TmpArticleDocument;
import com.wo.module.article.model.TmpArticleTag;
import com.wo.module.articleApproval.constant.ArticleApprovalConstant;
import com.wo.module.articleApproval.model.TmpArticleApproval;
import com.wo.module.articleApproval.service.ArticleApprovalService;
import com.wo.module.articleFE.service.ArticleFEService;
import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.exception.CustomAPIException;
import com.wo.module.common.model.UploadedFileWO;
import com.wo.module.common.util.FileUtil;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.emailTemplate.model.EmailTemplate;
import com.wo.module.externalRegulation.model.RegulationMst;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;
import com.wo.module.user.service.UserService;

public class ArticleApprovalEditBean extends CommonBean implements Serializable{

	private static final long serialVersionUID = -2009326368323479423L;
	private static final Logger logger = Logger.getLogger(ArticleApprovalEditBean.class);
	
	private TmpArticle tmpArticle;
	
	private String editId;
	private String actionMode;
	private String approvalStatus;
	private String approvalNote;
	
	private List<String> tagNames;
	
	private List<SelectItem> statusList;
	private List<SelectItem> articleTypeList;
	
	private List<UploadedFileWO> uploadFiles;
	
	private ArticleApprovalService articleApprovalService;
	
	private ArticleFEService articleFEService;
	
	private UserService userService;
	
	private FacesUtil facesUtil;
	private FileUtil fileUtil;
	
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
		initComponent();
		checkApproval();
		fileUtil = FileUtil.getInstance();
	}

	private void initComponent() {
		initStatusList();
		initArticleTypeList();
	}
	
	private void initArticleTypeList() {
		articleTypeList = new ArrayList<SelectItem>();
		try {
			List<ParameterDetail> listArticleType = parameterDetailService
					.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_ARTICLE_TYPE);
		

			for (ParameterDetail vo : listArticleType) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getName());
				si.setValue(vo.getParameterDtlCode());
				articleTypeList.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void initStatusList() {
		statusList = new ArrayList<SelectItem>();
		try {
			statusList = parameterDetailService.getListLabelValue(ParameterHeader.PARAM_HEAD_CODE_APPROVAL_STATUS,
					false, true, facesUtil.retrieveDefaultLocale());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void checkApproval() {
		this.editId = facesUtil.retrieveRequestParam("editId");
		String token = facesUtil.retrieveRequestParam("token"); 
		
		if (StringUtils.isBlank(editId) && StringUtils.isBlank(token)) {
			// do nothing
		} else {
			handleApproval(editId);
		}
		
	}
	
	private void handleApproval(String editId) {
		String token = facesUtil.retrieveRequestParam("token");
		if(StringUtils.isNotEmpty(token)) {
			editId = Constants.decryptString(token);
		}
		facesUtil.setSessionAttribute("token", Constants.encryptString(editId));
		actionMode = Constants.ACTION_EDIT;
		Long editIdLong = Long.parseLong(editId);
		tmpArticle = articleApprovalService.findById(editIdLong);
		
		uploadFiles = new ArrayList<UploadedFileWO>();
		if (tmpArticle.getTmpArticleDocumentList() != null) {
			for (int i = 0; i < tmpArticle.getTmpArticleDocumentList().size(); i++) {			
				TmpArticleDocument tad = (TmpArticleDocument) tmpArticle.getTmpArticleDocumentList().get(i);
				UploadedFileWO uf = new UploadedFileWO();
				uf.setFileName(tad.getAttachmentFile());
				uf.setFileId(tad.getFileId());
				uf.setIsNew(false);
				uf.setFileSize(tad.getFileSize());
				uploadFiles.add(uf);
			}
		}
		
		tagNames = new ArrayList<String>();
		if (tmpArticle.getTmpArticleTagList() != null) {
			for (int i = 0; i < tmpArticle.getTmpArticleTagList().size(); i++) {
				TmpArticleTag tat = (TmpArticleTag) tmpArticle.getTmpArticleTagList().get(i);
				tmpArticle.setTagName(tat.getTag());
				tagNames.add(tat.getTag());
			}
		}
		
	}

	public boolean isValidate() {
		boolean flag = true;
		
		if (StringUtils.isBlank(approvalStatus)) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("textApprovalStatus") + " "
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = false;
		}
		
		return flag;
	}
	
	public void save() {
		try {
			if (isValidate()) {
				if (approvalStatus.equals(ParameterDetail.PARAM_DET_CODE_STATUS_APPROVED)) {
					approve();
				} else if (approvalStatus.equals(ParameterDetail.PARAM_DET_CODE_STATUS_REVISE)) {
					revise();
				}							
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void approve() {
		try {
			
			if (tmpArticle.getTmpArticleApprovalList() == null || tmpArticle.getTmpArticleApprovalList().size() == 0) {
				tmpArticle.setTmpArticleApprovalList(new ArrayList<TmpArticleApproval>());
			}
			
			TmpArticleApproval tmpArticleApproval = new TmpArticleApproval();
			tmpArticleApproval.setTmpArticle(tmpArticle);
			tmpArticleApproval.setApprovalDate(new Date());
			tmpArticleApproval.setApprovalNote(approvalNote);
			tmpArticleApproval.setApprovalStatus(parameterDetailService
					.getParameterDetailByParamDtlCode(ParameterDetail.PARAM_DET_CODE_STATUS_APPROVED));
			tmpArticleApproval.setUser(userService.getUserByNik(facesUtil.retrieveUserLogin()));
			
			if (tmpArticleApproval.getCreatedBy() == null) {
				tmpArticleApproval.setCreatedBy(facesUtil.retrieveUserLogin());
				tmpArticleApproval.setCreationDate(new Timestamp(new Date().getTime()));
			}
			
			tmpArticleApproval.setDelId(new Long(0));
			tmpArticleApproval.setEnabledFlag(Constants.CONSTANT_YES);
			
			tmpArticle.getTmpArticleApprovalList().add(tmpArticleApproval);
			
			if (tmpArticle.getArticleId() != null) {
				
				if(tmpArticle.getArticleTitleIn().contains("(deleted)")){
					tmpArticle.setEnabledFlag(Constants.CONSTANT_NO);
					Article article = articleFEService.findById(tmpArticle.getArticleId());
					if(article!=null && article.getArticleId()!=null){
						article.setEnabledFlag(Constants.CONSTANT_NO);
						article.setLastUpdateBy(facesUtil.retrieveUserLogin());
						article.setLastUpdateDate(new Timestamp(new Date().getTime()));
						articleFEService.update(article);
					}
				}else{
					tmpArticle.setLastUpdateBy(facesUtil.retrieveUserLogin());
					tmpArticle.setLastUpdateDate(new Timestamp(new Date().getTime()));
					tmpArticle.setDelId(new Long(0));
					tmpArticle.setEnabledFlag(Constants.CONSTANT_YES);
					tmpArticle.setStatus(parameterDetailService
						.getParameterDetailByParamDtlCode(ParameterDetail.PARAM_DET_CODE_DATA_ACTIVE));
				}
				articleApprovalService.update(tmpArticle);
				
				// this should be a singleton
		        ExecutorService emailExecutor = Executors.newCachedThreadPool();

		        // from you sendEmail() method
		        emailExecutor.execute(new Runnable() {
		            @Override
		            public void run() {
		                try {
		                	sendEmailApprove();
		                } catch (Exception e) {
		                    logger.error("send email failed", e);
		                }
		            }
		        });
		        
				facesUtil.redirect("/pages/articleApproval/"+ArticleApprovalConstant.NAVIGATE_ARTICLE_APPROVAL);
			}
		} catch (Exception e) {
			e.printStackTrace();
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + e.getMessage(), "");
		}
	}
	
	private void revise() {
		try {
			if (tmpArticle.getTmpArticleApprovalList() == null || tmpArticle.getTmpArticleApprovalList().size() == 0) {
				tmpArticle.setTmpArticleApprovalList(new ArrayList<TmpArticleApproval>());
			}
			
			TmpArticleApproval tmpArticleApproval = new TmpArticleApproval();
			tmpArticleApproval.setTmpArticle(tmpArticle);
			tmpArticleApproval.setApprovalDate(new Date());
			tmpArticleApproval.setApprovalNote(approvalNote);
			tmpArticleApproval.setApprovalStatus(parameterDetailService
					.getParameterDetailByParamDtlCode(ParameterDetail.PARAM_DET_CODE_STATUS_REVISE));
			tmpArticleApproval.setUser(userService.getUserByNik(facesUtil.retrieveUserLogin()));
			
			if (tmpArticleApproval.getCreatedBy() == null) {
				tmpArticleApproval.setCreatedBy(facesUtil.retrieveUserLogin());
				tmpArticleApproval.setCreationDate(new Timestamp(new Date().getTime()));
			}
			
			tmpArticleApproval.setDelId(new Long(0));
			tmpArticleApproval.setEnabledFlag(Constants.CONSTANT_YES);
			
			tmpArticle.getTmpArticleApprovalList().add(tmpArticleApproval);
			
			if (tmpArticle.getArticleId() != null) {
				tmpArticle.setLastUpdateBy(facesUtil.retrieveUserLogin());
				tmpArticle.setLastUpdateDate(new Timestamp(new Date().getTime()));
				tmpArticle.setDelId(new Long(0));
				tmpArticle.setEnabledFlag(Constants.CONSTANT_YES);
				tmpArticle.setStatus(parameterDetailService
						.getParameterDetailByParamDtlCode(ParameterDetail.PARAM_DET_CODE_DATA_REVISE));
				tmpArticle.setArticleTitleIn(tmpArticle.getArticleTitleIn().replaceAll("\\(", "").replaceAll("deleted", "").replaceAll("\\)", ""));
				articleApprovalService.update(tmpArticle);
				
				// this should be a singleton
		        ExecutorService emailExecutor = Executors.newCachedThreadPool();

		        // from you sendEmail() method
		        emailExecutor.execute(new Runnable() {
		            @Override
		            public void run() {
		                try {
		                	sendEmailReject();
		                } catch (Exception e) {
		                    logger.error("send email failed", e);
		                }
		            }
		        });
				
				facesUtil.redirect("/pages/articleApproval/"+ArticleApprovalConstant.NAVIGATE_ARTICLE_APPROVAL);
			}
		} catch (Exception e) {
			e.printStackTrace();
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + e.getMessage(), "");
		}
	}
	
	public void sendEmailApprove() {
		try {
			
			String emailSubject = "LCCA Approval Notification - Article/Opinion";
	
			String emailContent = ""; 
			String emailTo  = "";
			String emailCc = "";
		   
			String articleTitle = tmpArticle.getArticleTitleIn() != null ? tmpArticle.getArticleTitleIn() : "";

			emailContent = "("+tmpArticle.getArticleType().getNameIn()+" - "+articleTitle+") telah disetujui <br/><br/>";
			if(tmpArticle.getTmpArticleApprovalList()!=null && tmpArticle.getTmpArticleApprovalList().size()>0){
			emailContent = emailContent+"Catatan: <br/>";
			emailContent = emailContent+""+tmpArticle.getTmpArticleApprovalList().get(tmpArticle.getTmpArticleApprovalList().size()-1).getApprovalNote()+ "";
			}
			emailTo = userService.getUserByNik(tmpArticle.getCreatedBy()).getEmail();

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
	
	public void sendEmailReject() {
		try {
			
			String emailSubject = "LCCA Approval Notification - Article/Opinion";
			
			
	
			String emailContent = ""; 
			String emailTo  = "";
			String emailCc = "";
		   
			String articleTitle = tmpArticle.getArticleTitleIn() != null ? tmpArticle.getArticleTitleIn() : "";

			emailContent = "("+tmpArticle.getArticleType().getNameIn()+" - "+articleTitle+") telah ditolak <br/><br/>";
			if(tmpArticle.getTmpArticleApprovalList()!=null && tmpArticle.getTmpArticleApprovalList().size()>0){
				emailContent = emailContent+"Catatan: <br/>";
				emailContent = emailContent+""+tmpArticle.getTmpArticleApprovalList().get(tmpArticle.getTmpArticleApprovalList().size()-1).getApprovalNote()+ "";
			}
			
			emailTo = userService.getUserByNik(tmpArticle.getCreatedBy()).getEmail();

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
	
	public void downloadFile (String fileId, String fileName, byte[] content) throws Exception {
		CallApiManager.downloadFile(fileId, fileName, content, parameterDetailService);
	}
	
	public void cancel() {
		try {
			facesUtil.redirect("/pages/articleApproval/"+ArticleApprovalConstant.NAVIGATE_ARTICLE_APPROVAL);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public TmpArticle getTmpArticle() {
		return tmpArticle;
	}

	public void setTmpArticle(TmpArticle tmpArticle) {
		this.tmpArticle = tmpArticle;
	}

	public String getEditId() {
		return editId;
	}

	public void setEditId(String editId) {
		this.editId = editId;
	}

	public ArticleApprovalService getArticleApprovalService() {
		return articleApprovalService;
	}

	public void setArticleApprovalService(ArticleApprovalService articleApprovalService) {
		this.articleApprovalService = articleApprovalService;
	}

	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

	public static Logger getLogger() {
		return logger;
	}

	public String getActionMode() {
		return actionMode;
	}

	public void setActionMode(String actionMode) {
		this.actionMode = actionMode;
	}

	public List<UploadedFileWO> getUploadFiles() {
		return uploadFiles;
	}

	public void setUploadFiles(List<UploadedFileWO> uploadFiles) {
		this.uploadFiles = uploadFiles;
	}

	public List<String> getTagNames() {
		return tagNames;
	}

	public void setTagNames(List<String> tagNames) {
		this.tagNames = tagNames;
	}

	public FileUtil getFileUtil() {
		return fileUtil;
	}

	public void setFileUtil(FileUtil fileUtil) {
		this.fileUtil = fileUtil;
	}

	public List<SelectItem> getStatusList() {
		return statusList;
	}

	public void setStatusList(List<SelectItem> statusList) {
		this.statusList = statusList;
	}

	public String getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public List<SelectItem> getArticleTypeList() {
		return articleTypeList;
	}

	public void setArticleTypeList(List<SelectItem> articleTypeList) {
		this.articleTypeList = articleTypeList;
	}

	public String getApprovalNote() {
		return approvalNote;
	}

	public void setApprovalNote(String approvalNote) {
		this.approvalNote = approvalNote;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public ArticleFEService getArticleFEService() {
		return articleFEService;
	}

	public void setArticleFEService(ArticleFEService articleFEService) {
		this.articleFEService = articleFEService;
	}
	
}