package com.wo.module.qaAdmin.bean;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.primefaces.PrimeFaces;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.exception.CustomAPIException;
import com.wo.module.common.util.MathUtil;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.emailTemplate.model.EmailTemplate;
import com.wo.module.emailTemplate.service.EmailTemplateService;
import com.wo.module.engine.service.SendEmailService;
import com.wo.module.engine.vo.SendEmailVO;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.lov.bean.SelectorListener;
import com.wo.module.lov.bean.SelectorModel;
import com.wo.module.lov.bean.SelectorModel.SelectorInfo;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;
import com.wo.module.parameter.service.ParameterDetailService;
import com.wo.module.qa.constant.QAConstants;
import com.wo.module.qa.model.QA;
import com.wo.module.qa.model.QAAttachment;
import com.wo.module.qa.model.QAKeyword;
import com.wo.module.qaAdmin.service.QAAdminKeywordService;
import com.wo.module.qaAdmin.service.QAAdminService;
import com.wo.module.user.model.User;
import com.wo.module.user.service.UserService;

public class QAAdminEditBean extends CommonBean  implements SelectorListener<Object>,Serializable {
 
	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(QAAdminEditBean.class);
	
	private QA qa;
	
	private Boolean isViewOnly;
	
	private String actionMode;
	private String editedId;
	private String nik;
	private String questionedBy;
	private String answeredBy;
	private String keyword;
	private String category;
	
	private List<SelectItem> slaTypes;
	private List<SelectItem> categoryTypes;
	
	private SelectorInfo selectorPic;
	
	private QAAdminService qaAdminService ;
	private UserService userService;
	private ParameterDetailService parameterDetailService;
	private EmailTemplateService emailTemplateService;
	private QAAdminKeywordService qaAdminKeywordService;
	private SendEmailService sendEmailService;
	
	public FacesUtil facesUtil;
	
	private String navigateSearch = QAConstants.NAVIGATE_SEARCH;
    
    public void addMessage(String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
    
    public void addErrMessage(String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
    
    public static SelectorInfo buildSelectorPIC(FacesUtil facesUtil) {
		SelectorModel.SelectorInfo info = new SelectorModel.SelectorInfo(
				" select user_id,nik,name,email from wo_mst_user "
				+ " where (upper(nik) like upper('%{0}%') OR upper(name) like upper('%{0}%')) "
				+ " /*and division_name = (select name_in from wo_mst_parameter_dtl where parameter_dtl_code = 'COMPLIANCE_DIVISION')*/ "
				+ " and enabled_flag = 'Y' "
				+ " order by nik ",
				" SELECT COUNT(1) from wo_mst_user "
				+ " where (upper(nik) like upper('%{0}%') OR upper(name) like upper('%{0}%')) "
				+ " /*and division_name = (select name_in from wo_mst_parameter_dtl where parameter_dtl_code = 'COMPLIANCE_DIVISION')*/ "
				+ " and enabled_flag = 'Y' ",
				Arrays.asList(facesUtil.retrieveMessage("formRegulationSocializationNIK"),
						facesUtil.retrieveMessage("formRegulationSocializationName"),
						facesUtil.retrieveMessage("formRegulationSocializationEmail")),
				Arrays.asList("1", "2", "3"), false);
		return info;

	}
    
    @PostConstruct
	public void init() {
    	super.init();
    	selectorPic = buildSelectorPIC(facesUtil);
    	selectSlaType();
    	selectCategoryType();
    	checkNewOrEdit();
	}
    
    public void selectSlaType() {
    	slaTypes = new ArrayList<SelectItem>();
    	try {
			List<ParameterDetail> pd = parameterDetailService.getParameterDetailByParamCode(ParameterHeader.PARAM_QNA_SLA);
			for(int i=0;i<pd.size();i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((ParameterDetail)pd.get(i)).getName());
				si.setValue(((ParameterDetail)pd.get(i)).getParameterDtlCode());
				slaTypes.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    public void selectCategoryType() {
    	categoryTypes = new ArrayList<SelectItem>();
    	try {
			List<ParameterDetail> pd = parameterDetailService.getParameterDetailByParamCode(ParameterHeader.PARAM_QNA_CATEGORY);
			for(int i=0;i<pd.size();i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((ParameterDetail)pd.get(i)).getName());
				si.setValue(((ParameterDetail)pd.get(i)).getParameterDtlCode());
				categoryTypes.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    private void checkNewOrEdit() {
		String editId = facesUtil.retrieveRequestParam("editId");
		String token = facesUtil.retrieveRequestParam("token"); 
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
	}

    private void handleNew() {
    	qa = new QA();
    	qa.setqDate(new Date());
    	qa.setqUser(facesUtil.getUserLogin());
    	
    	//qa.setSlaType(QAConstants.QNA_SLA_LOW);
    	//qa.setCategoryType(QAConstants.QNA_CATEGORY_CMT);
    	//qa.setThreadStatus(Constants.CONSTANT_NO);
    	//qa.setFaqFlag(0);
		actionMode = Constants.ACTION_ADD;
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
		qa = qaAdminService.findById(idLong);
		
		questionedBy = qa.getqUser().getName();
		this.setCategory(qa.getCategoryType());
		
		if(qa.getaUser()!=null) {
			nik = qa.getaUser().getNik();
			answeredBy = qa.getaUser().getName();
		}
		
		if (qa.getQaKeywords() != null) {
			for (int i = 0; i < qa.getQaKeywords().size(); i++) {
				QAKeyword qk = (QAKeyword) qa.getQaKeywords().get(0);
				this.setKeyword(qk.getKeyword());
			}
		}
		
		if(qa.getPublishQna() != null && qa.getPublishQna().equals("Y")) {
			qa.setIsPublishTemp(true);
		}else {
			qa.setIsPublishTemp(false);
		}
		
		if(qa.getQaAttachmentList().size() > 0) {
			for(QAAttachment vo : qa.getQaAttachmentList()) {
				if(vo.getPublishAttachment() != null && vo.getPublishAttachment().equalsIgnoreCase("Y")) {
					vo.setBoolPublishTemp(true);
				}else {
					vo.setBoolPublishTemp(false);
				}
			}
		}
		
		if(qa.getAnswer2() != null) {
			qa.setAnswer2Filled(true);
		}else {
			qa.setAnswer2Filled(false);
		}
		
		if(qa.getAnswer3() != null) {
			qa.setAnswer3Filled(true);
		}else {
			qa.setAnswer3Filled(false);
		}
		
		PrimeFaces.current().executeScript("initSelect2();");
    }
    
    public Boolean validate(){
		Boolean flag = false;
		try {

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return flag;
    }
   
    public void save() {
		try {
			if (!validate()) {
				
				qa.setQaKeywords(new ArrayList<QAKeyword>());
				if (qa.getQnaId() != null) {					
					qa.setLastUpdateBy(facesUtil.retrieveUserLogin());
					qa.setLastUpdateDate(new Timestamp(new Date().getTime()));
					qa.setDelId(new Long(0));
					qa.setEnabledFlag(Constants.CONSTANT_YES);
					qa.setAdminUser(facesUtil.getUserLogin());

					if (keyword != null && !keyword.equals("") && !keyword.isEmpty()) {
						
						List<QAKeyword> qkList = qaAdminService.getKeyword(qa.getQnaId(), null);
						
						if (qkList != null) {
							for (int i = 0; i < qkList.size(); i++) {
								QAKeyword getQK = (QAKeyword) qkList.get(i);
								QAKeyword entity = qaAdminKeywordService.findById(getQK.getQnaKeywordId());
								entity.setEnabledFlag(Constants.CONSTANT_NO);
								entity.setLastUpdateBy(facesUtil.retrieveUserLogin());
								entity.setLastUpdateDate(new Timestamp(new Date().getTime()));
								qaAdminKeywordService.delete(entity);
							}
						}
						
						QAKeyword qaKeyword = new QAKeyword();
						qaKeyword.setKeyword(keyword);
						qaKeyword.setQa(qa);
						qaKeyword.setCreatedBy(facesUtil.retrieveUserLogin());
						qaKeyword.setCreationDate(new Timestamp(new Date().getTime()));
						qaKeyword.setDelId(new Long(0));
						qaKeyword.setEnabledFlag(Constants.CONSTANT_YES);
						qa.getQaKeywords().add(qaKeyword);
					}
					
					if (!qa.getAnswer().equals("")) {
						//sendEmailToQ();
						if (qa.getaUser() != null) {
							//qa.setqStatus(QAConstants.QNA_STATUS_IP);
						} else {
							User user = new User();
							user = userService.getUserByNik(facesUtil.getUserLogin().getNik());
							qa.setaUser(user);
							qa.setaDate(new Timestamp(System.currentTimeMillis()));
							//qa.setqStatus(QAConstants.QNA_STATUS_IP);
						} 
					}
					
					if(!qa.getCategoryType().equals(category) && qa.getqStatus().equals(QAConstants.QNA_STATUS_NEW)) {
						// this should be a singleton
				        ExecutorService emailExecutor = Executors.newCachedThreadPool();
				        
				        // from you sendEmail() method
				        emailExecutor.execute(new Runnable() {
				            @Override
				            public void run() {
				                try {
			                		sendEmailToNewCategoryPIC(category, qa.getQuestion());
				                } catch (Exception e) {
				                    logger.error("send email failed", e);
				                }
				            }
				        });
					}
					
					qa.setCategoryType(category);
					
					qaAdminService.update(qa);
				}

//				if (qa.getaUser() != null) {
//					sendEmailToPIC();
//				}
				
				facesUtil.redirect("/pages/qaAdmin/qaAdmin.faces");
			}

    	}catch (Exception ex) {
            facesUtil.addFacesMsg(
                    FacesMessage.SEVERITY_ERROR, 
                    null, 
                    "Operation Failed : " + ex.getMessage(), "");
        }
    	
    }
    
    public void sendEmailToQ() {
    	try {
    		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
    		EmailTemplate emailTemplate = emailTemplateService
					.getEmailTemplateByEmailTemplateCode(QAConstants.EMAIL_QA_QUESTION_TO_PIC);
			String emailSubject = emailTemplate.getEmailSubject().replace("counter_type","NOTIFICATION");
			String emailContent = "";
			String emailTo = "";
			String emailCc = "";
			emailContent = emailTemplate.getEmailContent().replace("question_date", sdf.format(qa.getqDate()));
			emailContent = emailContent.replace("question_by", qa.getqUser().getName());
			
			emailTo = qa.getqUser().getEmail();
			
			User userPUK = userService.getUserByNik(qa.getqUser().getPukNik());
			emailCc = userPUK.getEmail();

			final String subject = emailSubject;
			final String content = emailContent;
			final String to = emailTo;
			// final String to = "h3ndr407@gmail.com";
			final String cc = emailCc;

			CallApiManager.sendEmailAPI(to, cc, subject, content, QAConstants.EMAIL_QA_QUESTION_TO_PIC, "true",
					parameterDetailService);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new CustomAPIException(ex.getMessage());
		}
    }
    
	public void sendEmailToPIC() {
		try {

			SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
			EmailTemplate emailTemplate = emailTemplateService
					.getEmailTemplateByEmailTemplateCode(QAConstants.EMAIL_QA_QUESTION_TO_PIC);
			String emailSubject = emailTemplate.getEmailSubject().replace("counter_type","NOTIFICATION");

			String emailContent = "";
			String emailTo = "";
			String emailCc = "";

			emailContent = emailTemplate.getEmailContent().replace("question_date", sdf.format(qa.getqDate()));
			emailContent = emailContent.replace("question_by", qa.getqUser().getName());
			 
			emailTo = qa.getaUser().getEmail();
			
			User userPUK = userService.getUserByNik(qa.getqUser().getPukNik());
			emailCc = userPUK.getEmail();

			final String subject = emailSubject;
			final String content = emailContent;
			final String to = emailTo;
			// final String to = "h3ndr407@gmail.com";
			final String cc = emailCc;

			CallApiManager.sendEmailAPI(to, cc, subject, content, QAConstants.EMAIL_QA_QUESTION_TO_PIC, "true",
					parameterDetailService);
			

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new CustomAPIException(ex.getMessage());
		}

	}
	
	public void sendEmailToNewCategoryPIC(String categoryType, String question) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
			EmailTemplate emailTemplate = emailTemplateService.getEmailTemplateByEmailTemplateCode(QAConstants.EMAIL_QA_QUESTION_TO_PIC);
			String emailSubject = emailTemplate.getEmailSubject();
			
			emailSubject = emailSubject.replaceAll("counter_type", "Notification");
			emailSubject = emailSubject.replaceAll("ticket_no",qa.getTicketNo());
	
			String emailContent = ""; 
			String emailTo  = "";
			String emailCc = "";
		    
			emailContent = emailTemplate.getEmailContent();
			ParameterDetail pd = parameterDetailService.getParameterDetailByParamDtlCode(categoryType);
			emailContent = emailContent.replaceAll("division_name",pd.getNameIn());
			emailContent = emailContent.replaceAll("q_date",sdf.format(qa.getqDate()));
			emailContent = emailContent.replaceAll("q_name",qa.getqUser().getName());
			emailContent = emailContent.replaceAll("ticket_no",qa.getTicketNo());
			emailContent = emailContent.replaceAll("q_title",qa.getqTitle());
			emailContent = emailContent.replaceAll("q_question",question);
			
			List<SendEmailVO> list =  sendEmailService.getListEmailAdminByQnaCategory(categoryType);
			for(int x=0;x<list.size();x++){
				SendEmailVO vo2 = list.get(x);
				if(emailTo.equals("")){
						emailTo = vo2.getEmailTo();
				}else{
					if(emailCc.equals("")){
						emailCc = vo2.getEmailTo();
					}else{
						emailCc = emailCc.concat(",").concat(vo2.getEmailTo());
					}
				}
			}
			CallApiManager.sendEmailAPI(emailTo,emailCc, emailSubject,
					emailContent, QAConstants.EMAIL_QA_QUESTION_TO_PIC, "true", parameterDetailService);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new CustomAPIException(ex.getMessage()); 
		}
	}
	
	@Override
	public void itemSelected(String clientId, String widgetVar, Object selectedItem) {
		if (StringUtils.equals("picDialog", widgetVar)) {
			Object[] objects = (Object[]) selectedItem;
			User user = userService.findById(MathUtil.returnIdObjectToLong(objects[0]));
			qa.setaUser(user);
			nik = user.getNik();
		}
	}
	
	public void downloadFile (String fileId, String fileName, byte[] content) throws Exception {
		CallApiManager.downloadFile(fileId, fileName, content, parameterDetailService);
	}
	
    public void cancel() {
    	try {
			facesUtil.redirect("/pages/qaAdmin/qaAdmin.faces");
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public void onChangePubAttachCheckbox(int index) {
    	if(qa.getQaAttachmentList().get(index).isBoolPublishTemp()) {
    		qa.getQaAttachmentList().get(index).setPublishAttachment("Y");
    		System.out.println("Row "+index+" checkbox has changed to Y");
    	}else { 
    		qa.getQaAttachmentList().get(index).setPublishAttachment("N");
    		System.out.println("Row "+index+" checkbox has changed to N");
    	}
    	
    	PrimeFaces.current().ajax().update("form:dataListPublishAttachment:"+index+":publishAttachment");
		
    	PrimeFaces.current().executeScript("initSelect2();");
    }
    
    public void onChangePubQACheckbox() {
    	if(qa.getIsPublishTemp()) {
    		qa.setPublishQna("Y");
    		System.out.println("Publish QA checkbox has changed to Y");
    	}else { 
    		qa.setPublishQna("N");
    		System.out.println("Publish QA checkbox has changed to N");
    	}
    	
    	
    	PrimeFaces.current().ajax().update("form:publishQA");
		
		PrimeFaces.current().executeScript("initSelect2();");
    }
    
	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

	public String getNavigateSearch() {
		return navigateSearch;
	}

	public void setNavigateSearch(String navigateSearch) {
		this.navigateSearch = navigateSearch;
	}

	public QA getQA() {
		return qa;
	}

	public void setQA(QA qa) {
		this.qa = qa;
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

	

	public List<SelectItem> getSlaTypes() {
		return slaTypes;
	}

	public void setSlaTypes(List<SelectItem> slaTypes) {
		this.slaTypes = slaTypes;
	}

	public ParameterDetailService getParameterDetailService() {
		return parameterDetailService;
	}

	public void setParameterDetailService(ParameterDetailService parameterDetailService) {
		this.parameterDetailService = parameterDetailService;
	}

	public QA getQa() {
		return qa;
	}

	public void setQa(QA qa) {
		this.qa = qa;
	}

	

	public QAAdminService getQaAdminService() {
		return qaAdminService;
	}

	public void setQaAdminService(QAAdminService qaAdminService) {
		this.qaAdminService = qaAdminService;
	}

	public EmailTemplateService getEmailTemplateService() {
		return emailTemplateService;
	}

	public void setEmailTemplateService(EmailTemplateService emailTemplateService) {
		this.emailTemplateService = emailTemplateService;
	}

	public List<SelectItem> getCategoryTypes() {
		return categoryTypes;
	}

	public void setCategoryTypes(List<SelectItem> categoryTypes) {
		this.categoryTypes = categoryTypes;
	}

	public String getNik() {
		return nik;
	}

	public void setNik(String nik) {
		this.nik = nik;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public SelectorInfo getSelectorPic() {
		return selectorPic;
	}

	public void setSelectorPic(SelectorInfo selectorPic) {
		this.selectorPic = selectorPic;
	}

	public String getQuestionedBy() {
		return questionedBy;
	}

	public void setQuestionedBy(String questionedBy) {
		this.questionedBy = questionedBy;
	}

	public String getAnsweredBy() {
		return answeredBy;
	}

	public void setAnsweredBy(String answeredBy) {
		this.answeredBy = answeredBy;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public QAAdminKeywordService getQaAdminKeywordService() {
		return qaAdminKeywordService;
	}

	public void setQaAdminKeywordService(QAAdminKeywordService qaAdminKeywordService) {
		this.qaAdminKeywordService = qaAdminKeywordService;
	}

	public SendEmailService getSendEmailService() {
		return sendEmailService;
	}

	public void setSendEmailService(SendEmailService sendEmailService) {
		this.sendEmailService = sendEmailService;
	}

	
   
}