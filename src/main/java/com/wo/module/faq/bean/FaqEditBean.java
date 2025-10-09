package com.wo.module.faq.bean;

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
import org.primefaces.event.FileUploadEvent;

import com.wo.module.article.model.ArticleTag;
import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.exception.CustomAPIException;
import com.wo.module.common.model.UploadedFileWO;
import com.wo.module.common.util.FileUtil;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.emailTemplate.model.EmailTemplate;
import com.wo.module.emailTemplate.service.EmailTemplateService;
import com.wo.module.externalRegulation.service.RegulationService;
import com.wo.module.faq.constant.FaqConstants;
import com.wo.module.faq.model.Faq;
import com.wo.module.faq.model.FaqDocument;
import com.wo.module.faq.model.FaqKeyword;
import com.wo.module.faq.model.TmpFaq;
import com.wo.module.faq.model.TmpFaqDocument;
import com.wo.module.faq.model.TmpFaqKeyword;
import com.wo.module.faq.service.FaqService;
import com.wo.module.header.service.HeaderFrontEndService;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.outgoingLetter.constant.OutgoingLetterConstants;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;
import com.wo.module.user.model.User;
import com.wo.module.user.service.UserService;

public class FaqEditBean extends CommonBean implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(FaqEditBean.class);

	private Integer hits;
	
	private Faq faq;
	
	private TmpFaq tmpFaq;

	private Boolean isViewOnly;

	private String actionMode;
	
	private List<String> keywords;

	private String editedId;
	
	private Boolean isAdmin;

	private FaqService faqService;
	
	private RegulationService regulationService;
	
	private HeaderFrontEndService headerFrontEndService;
	
	private UserService userService;
	
	private EmailTemplateService emailTemplateService;

	public FacesUtil facesUtil;
	
	private Integer lastSequenceOfDtl;

	private List<SelectItem> nameList;
	private List<SelectItem> slaTypeList;
	private List<SelectItem> categoryList;
	private List<SelectItem> institutionList;
	
	private List<UploadedFileWO> uploadFiles;
	private List<UploadedFileWO> deleteFiles;
	
	private FileUtil fileUtil;

	private boolean checkAll;

	private String navigateSeafaqh = FaqConstants.NAVIGATE_SEARCH;
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
			isAdmin = headerFrontEndService.getIsAdmin(facesUtil.retrieveUserLogin());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			ParameterDetail getText = parameterDetailService.getParameterDetailByParamDtlCode("UPLOAD_WARNING_TEXT");
			textWarningUpload = getText.getName();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void initList(){
		try {
			User userLogin = userService.getUserByNik(facesUtil.retrieveUserLogin());
		categoryList = new ArrayList<SelectItem>();
		List<ParameterDetail> listCategory = parameterDetailService
					.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_FAQ_CATEGORY);
		

		for (ParameterDetail vo : listCategory) {
			SelectItem si = new SelectItem();
			si.setLabel(vo.getName());
			si.setValue(vo.getParameterDtlCode());
			categoryList.add(si);
		}
		
		institutionList = new ArrayList<SelectItem>();
		List<ParameterDetail> listInstitution = parameterDetailService
					.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_INSTITUTION_NAME);
		

		for (ParameterDetail vo : listInstitution) {
			SelectItem si = new SelectItem();
			si.setLabel(vo.getName());
			si.setValue(vo.getParameterDtlCode());
			
			if(userLogin!=null && userLogin.getDivisionName()!=null){
				if(userLogin.getDivisionName().equals("COMPLIANCE")){
					if(si.getValue().equals("INSTITUTION_NAME_COMPLIANCE")){
						institutionList.add(si);
					}
				} else if(userLogin.getDivisionName().equals("CORPORATE LEGAL & LITIGATION")){
					if(si.getValue().equals("INSTITUTION_NAME_LEGAL") || si.getValue().equals("INSTITUTION_NAME_LITIGATION")){
						institutionList.add(si);
					}
				} else {
					if(si.getValue().equals("INSTITUTION_NAME_FCC")){
						institutionList.add(si);
					}
				}
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
//		faq = new Faq();
		tmpFaq = new TmpFaq();
		ParameterDetail pd = new ParameterDetail();
//		faq.setFaqCategory(pd);
		tmpFaq.setFaqCategory(pd);
		ParameterDetail pd2 = new ParameterDetail();
//		faq.setInstitution(pd2);
		tmpFaq.setInstitution(pd2);
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
//		faq = faqService.findById(idLong);
		tmpFaq = faqService.findById(idLong);
		
//		if(faq.getInstitution() == null){
//			ParameterDetail pd = new ParameterDetail();
//			faq.setInstitution(pd);
//		}
		if(tmpFaq.getInstitution() == null){
			ParameterDetail pd = new ParameterDetail();
			tmpFaq.setInstitution(pd);
		}
		
		tmpFaq.setUploadDate(tmpFaq.getCreationDate());
		
		uploadFiles = new ArrayList<UploadedFileWO>();
		for (int i = 0; i < tmpFaq.getTmpFaqDocuments().size(); i++) {
			TmpFaqDocument ra = tmpFaq.getTmpFaqDocuments().get(i);
			UploadedFileWO uf = new UploadedFileWO();
			uf.setFileName(ra.getAttachmentFile());
			uf.setFileId(ra.getFileId());
			uf.setIsNew(false);
			uf.setFileSize(ra.getFileSize());
			uploadFiles.add(uf);
			
		}
		/*for (int i = 0; i < faq.getFaqKeywords().size(); i++) {
			FaqKeyword faqKey = faq.getFaqKeywords().get(i);
			faq.setKeyword(faqKey.getKeyword());
		}*/
		lastSequenceOfDtl = 0;
		List<String> listKey = new ArrayList<String>();
		for (int i = 0; i < tmpFaq.getTmpFaqKeywords().size(); i++) {
			TmpFaqKeyword faqKey = tmpFaq.getTmpFaqKeywords().get(i);
			listKey.add(faqKey.getKeyword());
		}

		if (tmpFaq.getTmpFaqKeywords().size() > 0) {
			keywords = listKey;
		}
		
		try {
			hits = regulationService.getCountHitRegulation(idLong, "/compliance/pages/faqFE/faqFE.faces");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public Boolean validate() {
		Boolean flag = false;

		if (StringUtils.isEmpty(tmpFaq.getFaqCategory().getParameterDtlCode())) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formFaqType") 
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}

		if (getKeywords() == null || getKeywords().size() == 0) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formFaqSubject") 
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}
		
		
		if (StringUtils.isEmpty(tmpFaq.getQuestionIn())) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formFaqQuestion") + " in "
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}

		

		return flag;
	}

	public void save() {
		try {
			if (!validate()) {
				
				if(tmpFaq.getTmpFaqDocuments() == null || tmpFaq.getTmpFaqDocuments().size() <= 0) {
					tmpFaq.setTmpFaqDocuments(new ArrayList<TmpFaqDocument>());
				}
				tmpFaq.getTmpFaqDocuments().clear();
				
				tmpFaq.setStatus(ParameterDetail.PARAM_DET_CODE_DATA_NEW);
				
				if(tmpFaq.getTmpFaqKeywords() == null || tmpFaq.getTmpFaqKeywords().size() <= 0) {
					TmpFaqKeyword faqTag = new TmpFaqKeyword();
					faqTag.setKeyword(tmpFaq.getKeyword());
					faqTag.setCreatedBy(facesUtil.retrieveUserLogin());
					faqTag.setCreationDate(new Timestamp(new Date().getTime()));
					faqTag.setDelId(new Long(0));
					faqTag.setEnabledFlag(Constants.CONSTANT_YES);
					List<TmpFaqKeyword> list = new ArrayList<TmpFaqKeyword>();
					list.add(faqTag);
					tmpFaq.setTmpFaqKeywords(list);
				}
				
				tmpFaq.getTmpFaqKeywords().clear();
				
				if(uploadFiles != null) {
					for (int i = 0; i < uploadFiles.size(); i++) {
						TmpFaqDocument doc = new TmpFaqDocument();
						UploadedFileWO uf = (UploadedFileWO) uploadFiles.get(i);
						doc.setTmpFaq(tmpFaq);
						
						doc.setAttachmentFile(uf.getFileName());
						
						doc.setCreatedBy(facesUtil.retrieveUserLogin());
						doc.setCreationDate(new Timestamp(new Date().getTime()));
						doc.setDelId(new Long(0));
						doc.setEnabledFlag(Constants.CONSTANT_YES);
						
						doc.setFileId(uf.getFileId());
						doc.setFileSize(uf.getFileSize());
						tmpFaq.getTmpFaqDocuments().add(doc);
					}
				}
				
				/*FaqKeyword tag = new FaqKeyword();
				tag.setFaq(faq);
				tag.setKeyword(faq.getKeyword());
				tag.setCreatedBy(facesUtil.retrieveUserLogin());
				tag.setCreationDate(new Timestamp(new Date().getTime()));
				tag.setDelId(new Long(0));
				tag.setEnabledFlag(Constants.CONSTANT_YES);
				faq.getFaqKeywords().add(tag);*/
				
				if (keywords != null) {
					for (int i = 0; i < keywords.size(); i++) {
						TmpFaqKeyword key = new TmpFaqKeyword();
							key.setKeyword(keywords.get(i));
							key.setTmpFaq(tmpFaq);
							key.setCreatedBy(facesUtil.retrieveUserLogin());
							key.setCreationDate(new Timestamp(new Date().getTime()));
							key.setDelId(new Long(0));
							key.setEnabledFlag(Constants.CONSTANT_YES);
							tmpFaq.getTmpFaqKeywords().add(key);
						}

						
				}
				
				ParameterDetail pd = parameterDetailService.getParameterDetailByParamDtlCode(tmpFaq.getFaqCategory().getParameterDtlCode());
				tmpFaq.setFaqCategory(pd);
				
				ParameterDetail pd2 = parameterDetailService.getParameterDetailByParamDtlCode(tmpFaq.getInstitution().getParameterDtlCode());
				tmpFaq.setInstitution(pd2);

				if (tmpFaq.getFaqId() != null) {
					tmpFaq.setLastUpdateBy(facesUtil.retrieveUserLogin());
					tmpFaq.setLastUpdateDate(new Timestamp(new Date().getTime()));
					tmpFaq.setDelId(new Long(0));
					tmpFaq.setEnabledFlag(Constants.CONSTANT_YES);
					faqService.update(tmpFaq);

				} else {
					
					tmpFaq.setCreatedBy(facesUtil.retrieveUserLogin());
					tmpFaq.setCreationDate(new Timestamp(new Date().getTime()));
					tmpFaq.setDelId(new Long(0));
					tmpFaq.setEnabledFlag(Constants.CONSTANT_YES);
					faqService.save(tmpFaq);
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
				facesUtil.redirect("/pages/faq/faq.faces");
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

	private void sendEmail() {
		try {
			EmailTemplate emailTemplate = emailTemplateService.getEmailTemplateByEmailTemplateCode(Constants.EMAIL_APPROVAL);
			String emailSubject = emailTemplate.getEmailSubject()+" - FAQ";
			
			String emailContent = ""; 
			String emailTo  = "";
			String emailCc = "";
			
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < keywords.size(); i++) {
				sb.append(keywords.get(i));
			}
			
			emailContent = emailTemplate.getEmailContent().replace(Constants.NOTIFICATION_TYPE_AND_DOC_NUM, sb.toString());
			ParameterDetail pdHostName = parameterDetailService.getParameterDetailByParamDtlCode(Constants.HOST_NAME_APPLICATION);
			String token = Constants.encryptString(sb.toString());
			String menuId = Constants.encryptString(Constants.MENU_ID_APPROVAL_REQUEST_FAQ);
			String urlLink = pdHostName.getNameIn().concat("pages/dashboard/dashboard.faces?token="+token+"&menuId="+menuId);
			emailContent = emailContent.replaceAll("url_link", urlLink);
			
			ParameterDetail paramEmail = null;
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
			facesUtil.redirect("/pages/faq/faq.faces");
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

	public FaqService getFaqService() {
		return faqService;
	}

	public void setFaqService(FaqService faqService) {
		this.faqService = faqService;
	}

	public String getNavigateSeafaqh() {
		return navigateSeafaqh;
	}

	public void setNavigateSeafaqh(String navigateSeafaqh) {
		this.navigateSeafaqh = navigateSeafaqh;
	}

	public Faq getFaq() {
		return faq;
	}

	public void setFaq(Faq faq) {
		this.faq = faq;
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

	public Faq getRc() {
		return faq;
	}

	public void setRc(Faq faq) {
		this.faq = faq;
	}

	public FaqService getRcService() {
		return faqService;
	}

	public void setRcService(FaqService faqService) {
		this.faqService = faqService;
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

	public List<SelectItem> getInstitutionList() {
		return institutionList;
	}

	public void setInstitutionList(List<SelectItem> institutionList) {
		this.institutionList = institutionList;
	}

	public Integer getHits() {
		return hits;
	}

	public void setHits(Integer hits) {
		this.hits = hits;
	}

	public RegulationService getRegulationService() {
		return regulationService;
	}

	public void setRegulationService(RegulationService regulationService) {
		this.regulationService = regulationService;
	}

	public HeaderFrontEndService getHeaderFrontEndService() {
		return headerFrontEndService;
	}

	public void setHeaderFrontEndService(HeaderFrontEndService headerFrontEndService) {
		this.headerFrontEndService = headerFrontEndService;
	}

	public Boolean getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public String getTextWarningUpload() {
		return textWarningUpload;
	}

	public void setTextWarningUpload(String textWarningUpload) {
		this.textWarningUpload = textWarningUpload;
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

	public TmpFaq getTmpFaq() {
		return tmpFaq;
	}

	public void setTmpFaq(TmpFaq tmpFaq) {
		this.tmpFaq = tmpFaq;
	}
	
	

}