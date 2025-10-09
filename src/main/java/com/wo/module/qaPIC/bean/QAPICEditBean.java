package com.wo.module.qaPIC.bean;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
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

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.exception.CustomAPIException;
import com.wo.module.common.util.MathUtil;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.emailTemplate.model.EmailTemplate;
import com.wo.module.emailTemplate.service.EmailTemplateService;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.lov.bean.SelectorListener;
import com.wo.module.lov.bean.SelectorModel.SelectorInfo;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;
import com.wo.module.qa.constant.QAConstants;
import com.wo.module.qa.model.QA;
import com.wo.module.qaPIC.service.QAPICService;
import com.wo.module.user.model.User;
import com.wo.module.user.service.UserService;

public class QAPICEditBean extends CommonBean  implements SelectorListener<Object>,Serializable {
 
	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(QAPICEditBean.class);
	
	private QA qa;
	
	private Boolean isViewOnly;
	
	private String actionMode;
	
	private String editedId;
	
	private String nik;
	
	private String keyword;
	
	private String questionedBy;
	
	private String closeThread;
	
	private Long userIdLogin;
	
	private List<SelectItem> slaTypes;
	
	private List<SelectItem> categoryTypes;
	
	private List<SelectItem> closeTypes;
	
	private SelectorInfo selectorPic;
	
	private QAPICService qaPICService ;
	
	public UserService userService;
	
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
    	selectSlaType();
    	selectCategoryType();
    	selectCloseType();
    	checkNewOrEdit();
    	userIdLogin = facesUtil.getUserLogin().getUserId();
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
    
    public void selectCloseType() {
    	closeTypes = new ArrayList<SelectItem>();
    	SelectItem si = new SelectItem();
		si.setLabel("Yes");
		si.setValue("Y");
		closeTypes.add(si);
		SelectItem si2 = new SelectItem();
		si2.setLabel("No");
		si2.setValue("N");
		closeTypes.add(si2);
    	
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
		qa = qaPICService.findById(idLong);
		questionedBy = qa.getqUser().getName();
		
		//if the QnA havent answered, answerUser is still null 
		if(qa.getaUser() !=null) {
			nik = qa.getaUser().getNik();
		}else {
			nik = facesUtil.getUserLogin().getNik();
		}
		qa.setaDate(new Timestamp(System.currentTimeMillis()));
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
    
    public void close() {
    	try {
    		if(!validate()){
    		if(qa.getQnaId()!=null) {
    			
    			qa.setThreadStatus(qa.getThreadStatus());
    			qa.setLastUpdateBy(facesUtil.retrieveUserLogin());
	    		qa.setLastUpdateDate(new Timestamp(new Date().getTime()));
	    		qa.setDelId(new Long(0));
	    		
	    		qa.setEnabledFlag(Constants.CONSTANT_YES);
	    		
	    		qaPICService.update(qa);
    		}

    		facesUtil.redirect("/pages/qaPic/qaPic.faces");
    		}
    		
    	}catch (Exception ex) {
            facesUtil.addFacesMsg(
                    FacesMessage.SEVERITY_ERROR, 
                    null, 
                    "Operation Failed : " + ex.getMessage(), "");
        }
    	
    }
    
    public void save() {
    	try {
    		if(!validate()){
	    		if(qa.getQnaId()!=null) {
	    			qa.setqStatus(QAConstants.QNA_STATUS_CLOSE);
	    			qa.setReadFlag(Constants.CONSTANT_NO);
	    			qa.setLastUpdateBy(facesUtil.retrieveUserLogin());
		    		qa.setLastUpdateDate(new Timestamp(new Date().getTime()));
		    		qa.setDelId(new Long(0));
		    		qa.setEnabledFlag(Constants.CONSTANT_YES);
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
		    		qaPICService.update(qa);
		    		facesUtil.redirect("/pages/qaPic/qaPic.faces");
	    		}
    		}
    		
    	}catch (Exception ex) {
    		qa.setqStatus(QAConstants.QNA_STATUS_IP);
            facesUtil.addFacesMsg(
                    FacesMessage.SEVERITY_ERROR, 
                    null, 
                    "Operation Failed : " + ex.getMessage(), "");
        }
    	
    }
    
	public void sendEmail() {
		try {

			SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
			EmailTemplate emailTemplate = emailTemplateService
					.getEmailTemplateByEmailTemplateCode(QAConstants.EMAIL_QA_ANSWER);
			String emailSubject = emailTemplate.getEmailSubject();
			
			emailSubject = emailSubject.replaceAll("counter_type", "Notification");
			emailSubject = emailSubject.replaceAll("ticket_no", qa.getTicketNo());

			String emailContent = "";
			String emailTo = "";
			String emailCc = "";

			emailContent = emailTemplate.getEmailContent().replace("question_date", sdf.format(qa.getqDate()));
			emailContent = emailContent.replaceAll("ticket_no",qa.getTicketNo());
			emailContent = emailContent.replace("question_by", qa.getqUser().getName());
			
			User userPUK = userService.getUserByNik(qa.getqUser().getPukNik());
			//emailCc = userPUK.getEmail();
			
			emailTo = qa.getaUser().getEmail()+","+userPUK.getEmail()+","+qa.getAdminUser().getEmail();
			emailCc = qa.getqUser().getEmail();
			
			final String subject = emailSubject;
			final String content = emailContent;
			final String to = emailTo;
			// final String to = "h3ndr407@gmail.com";
			final String cc = emailCc;

			CallApiManager.sendEmailAPI(to, cc, subject, content, QAConstants.EMAIL_QA_ANSWER, "true",
					parameterDetailService);
			

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
	
    public void cancel() {
    	try {
			facesUtil.redirect("/pages/qaPic/qaPic.faces");
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


	public QA getQa() {
		return qa;
	}

	public void setQa(QA qa) {
		this.qa = qa;
	}

	

	public QAPICService getQaPICService() {
		return qaPICService;
	}

	public void setQaPICService(QAPICService qaPICService) {
		this.qaPICService = qaPICService;
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

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getQuestionedBy() {
		return questionedBy;
	}

	public void setQuestionedBy(String questionedBy) {
		this.questionedBy = questionedBy;
	}

	public String getCloseThread() {
		return closeThread;
	}

	public void setCloseThread(String closeThread) {
		this.closeThread = closeThread;
	}

	public List<SelectItem> getCloseTypes() {
		return closeTypes;
	}

	public void setCloseTypes(List<SelectItem> closeTypes) {
		this.closeTypes = closeTypes;
	}

	public Long getUserIdLogin() {
		return userIdLogin;
	}

	public void setUserIdLogin(Long userIdLogin) {
		this.userIdLogin = userIdLogin;
	}

	
	
	
	

	
   
}