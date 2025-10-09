package com.wo.module.qa.bean;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
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

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.exception.CustomAPIException;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.emailTemplate.model.EmailTemplate;
import com.wo.module.emailTemplate.service.EmailTemplateService;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.qa.constant.QAConstants;
import com.wo.module.qa.model.QA;
import com.wo.module.qa.service.QAService;
import com.wo.module.user.model.User;

public class QAEditBean extends CommonBean implements Serializable {
 
	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(QABean.class);
	
	private QA qa;
	
	private Boolean isViewOnly;
	
	private String actionMode;
	
	private String editedId;
	
	private List<SelectItem> provTypes;
	
	private QAService qaService;
	
	private EmailTemplateService emailTemplateService;
	
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
    
    
    @PostConstruct
	public void init() {
    	super.init();
    	checkNewOrEdit();
	}
    
    
    private void checkNewOrEdit() {
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
	}

    private void handleNew() {
    	qa = new QA();
    	qa.setqDate(new Date());
    	qa.setqUser(facesUtil.getUserLogin());
    	qa.setqStatus(QAConstants.QNA_STATUS_NEW);
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
		qa = qaService.findById(idLong);
		
		if(isViewOnly && qa.getqStatus().equals(QAConstants.QNA_STATUS_CLOSE) && (qa.getReadFlag() == null || !qa.getReadFlag().equals(Constants.CONSTANT_YES))) {
			qa.setReadFlag(Constants.CONSTANT_YES);
			qaService.update(qa);
		}
    }
    
    public Boolean validate(){
		Boolean flag = false;
		try {
			
			/*
			 * if(actionMode.equals(Constants.ACTION_ADD)) { Integer validateSameValue =
			 * qaService.getQAByProvAndType( qa.getParameterDetail().getParameterDtlCode(),
			 * qa.getQAIn());
			 * 
			 * if(validateSameValue > 0) {
			 * facesUtil.addErrMessage(facesUtil.retrieveMessage("formQAProvType") + " & " +
			 * facesUtil.retrieveMessage("formQATitle") + " " +
			 * facesUtil.retrieveMessage("indonesia") + " " +
			 * facesUtil.retrieveMessage("errorAlreadyExists") ); flag = true; } }else
			 * if(actionMode.equals(Constants.ACTION_EDIT)) {
			 * 
			 * Integer validateEditSameValue = qaService.getEditQAByIdProvAndType(
			 * qa.getQAId() , qa.getParameterDetail().getParameterDtlCode() , qa.getQAIn());
			 * 
			 * if(validateEditSameValue > 0) {
			 * facesUtil.addErrMessage(facesUtil.retrieveMessage("formQAProvType") + " & " +
			 * facesUtil.retrieveMessage("formQATitle") + " " +
			 * facesUtil.retrieveMessage("indonesia") + " " +
			 * facesUtil.retrieveMessage("errorAlreadyExists") ); flag = true; } }
			 * 
			 * if(StringUtils.isEmpty(qa.getParameterDetail().getParameterDtlCode())){
			 * facesUtil.addErrMessage(facesUtil.retrieveMessage("formQAProvType") + " "
			 * +facesUtil.retrieveMessage("validateRequired")); flag = true; } else
			 * if(StringUtils.isEmpty(qa.getQAEn())){
			 * facesUtil.addErrMessage(facesUtil.retrieveMessage("formQATitle")+" en "
			 * +facesUtil.retrieveMessage("validateRequired")); flag = true; } else
			 * if(StringUtils.isEmpty(qa.getQAIn())){
			 * facesUtil.addErrMessage(facesUtil.retrieveMessage("formQATitle")+" in "
			 * +facesUtil.retrieveMessage("validateRequired")); flag = true; } else
			 * if(StringUtils.isEmpty(qa.getTypeDescription())){
			 * facesUtil.addErrMessage(facesUtil.retrieveMessage("formQADocTypeDesc")+" in "
			 * +facesUtil.retrieveMessage("validateRequired")); flag = true; }
			 */
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return flag;
    }
    public void save() {
    	try {
    		if(!validate()){
    		if(qa.getQnaId()!=null) {
    			
    			qa.setLastUpdateBy(facesUtil.retrieveUserLogin());
	    		qa.setLastUpdateDate(new Timestamp(new Date().getTime()));
	    		qa.setDelId(new Long(0));
	    		qa.setEnabledFlag(Constants.CONSTANT_YES);
	    		qaService.update(qa);
    		}else {
	    		qa.setCreatedBy(facesUtil.retrieveUserLogin());
	    		qa.setCreationDate(new Timestamp(new Date().getTime()));
	    		qa.setDelId(new Long(0));
	    		qa.setEnabledFlag(Constants.CONSTANT_YES);
	    		qa.setTicketNo(generateTicketNo());
	    		qaService.save(qa);
    		}
    		sendEmailToChecker();
    		sendEmailToSubmitter();
    		facesUtil.redirect("/pages/qa/qa.faces");
    		}
    		
    	}catch (Exception ex) {
            facesUtil.addFacesMsg(
                    FacesMessage.SEVERITY_ERROR, 
                    null, 
                    "Operation Failed : " + ex.getMessage(), "");
        }
    	
    }
    
    public String generateTicketNo(){
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
    	String year = sdf.format(new Date());
    	Long seqNo = qaService.getTicketNo().longValue();
    	return year.concat("-").concat(seqNo.toString());
    }
    
	public void sendEmailToChecker() {
		try {

			SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
			EmailTemplate emailTemplate = emailTemplateService
					.getEmailTemplateByEmailTemplateCode(QAConstants.EMAIL_QA_QUESTION_TO_ADMIN);
			String emailSubject = emailTemplate.getEmailSubject();
			
			emailSubject = emailSubject.replaceAll("ticket_no", qa.getTicketNo());

			String emailContent = "";
			String emailTo = "";
			String emailCc = "";

			User user = facesUtil.getUserLogin();
			emailContent = emailTemplate.getEmailContent().replace("question_date", sdf.format(new Date())).replace("question_by", user.getName());
			 
			ParameterDetail paramEmail = parameterDetailService
					.getParameterDetailByParamDtlCode(Constants.OSCAR_CHECKER);
			emailTo = paramEmail.getNameIn();

			final String subject = emailSubject;
			final String content = emailContent;
			final String to = emailTo;
			// final String to = "h3ndr407@gmail.com";
			final String cc = emailCc;

			CallApiManager.sendEmailAPI(to, cc, subject, content, QAConstants.EMAIL_QA_QUESTION_TO_ADMIN, "true",
					parameterDetailService);
			
			/*ExecutorService emailExecutor = Executors.newCachedThreadPool();
			
			emailExecutor.execute(new Runnable() {
	            @Override
	            public void run() {
					
	            	try {
						CallApiManager.sendEmailAPI(to, cc, subject, content, QAConstants.EMAIL_QA_QUESTION_TO_ADMIN, "true",
				parameterDetailService);
						 
					} catch (Exception e) {
						e.printStackTrace();
					}
	            }
	        });*/
			
			

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new CustomAPIException(ex.getMessage());
		}

	}
	
	public void sendEmailToSubmitter() {
		try {

			SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
			EmailTemplate emailTemplate = emailTemplateService
					.getEmailTemplateByEmailTemplateCode(QAConstants.EMAIL_QA_QUESTION_TO_SUBMITTER);
			String emailSubject = emailTemplate.getEmailSubject();
			
			emailSubject = emailSubject.replaceAll("ticket_no", qa.getTicketNo());

			String emailContent = "";
			String emailTo = "";
			String emailCc = "";

			User user = facesUtil.getUserLogin();
			emailContent = emailTemplate.getEmailContent().replace("question_date", sdf.format(new Date()))
					.replace("question_by", user.getName());

			emailTo = user.getEmail();

			final String subject = emailSubject;
			final String content = emailContent;
			final String to = emailTo;
			// final String to = "h3ndr407@gmail.com";
			final String cc = emailCc;

			ExecutorService emailExecutor = Executors.newCachedThreadPool();

			emailExecutor.execute(new Runnable() {
				@Override
				public void run() {

					try {
						CallApiManager.sendEmailAPI(to, cc, subject, content,
								QAConstants.EMAIL_QA_QUESTION_TO_SUBMITTER, "true", parameterDetailService);

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new CustomAPIException(ex.getMessage());
		}

	}
    
    public void cancel() {
    	try {
			facesUtil.redirect("/pages/qa/qa.faces");
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

	
	public QAService getQAService() {
		return qaService;
	}

	public void setQAService(QAService qaService) {
		this.qaService = qaService;
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

	public List<javax.faces.model.SelectItem> getProvTypes() {
		return provTypes;
	}

	public void setProvTypes(List<javax.faces.model.SelectItem> provTypes) {
		this.provTypes = provTypes;
	}


	public QA getQa() {
		return qa;
	}

	public void setQa(QA qa) {
		this.qa = qa;
	}

	public QAService getQaService() {
		return qaService;
	}

	public void setQaService(QAService qaService) {
		this.qaService = qaService;
	}

	public EmailTemplateService getEmailTemplateService() {
		return emailTemplateService;
	}

	public void setEmailTemplateService(EmailTemplateService emailTemplateService) {
		this.emailTemplateService = emailTemplateService;
	}

	
	

	
   
}