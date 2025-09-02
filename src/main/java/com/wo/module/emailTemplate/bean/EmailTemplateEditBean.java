package com.wo.module.emailTemplate.bean;

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

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.emailTemplate.constant.EmailTemplateConstants;
import com.wo.module.emailTemplate.model.EmailTemplate;
import com.wo.module.emailTemplate.service.EmailTemplateService;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;
import com.wo.module.parameter.service.ParameterDetailService;

public class EmailTemplateEditBean extends CommonBean implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(EmailTemplateEditBean.class);

	private EmailTemplate emailTemplate;

	private Boolean isViewOnly;

	private String actionMode;

	private String editedId;

	private EmailTemplateService emailTemplateService;
	
	public FacesUtil facesUtil;

	private List<SelectItem> templateNameList;

	private String navigateSearch = EmailTemplateConstants.NAVIGATE_SEARCH;

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
	}

	public void initList() {
		try {
			templateNameList = new ArrayList<SelectItem>();
			List<ParameterDetail> listParamDtl = parameterDetailService
					.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_CODE_EMAIL_TEMPLATE);

			for (ParameterDetail vo : listParamDtl) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getName());
				si.setValue(vo.getParameterDtlCode());
				templateNameList.add(si);
			}
		} catch (Exception e) {
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
		emailTemplate = new EmailTemplate();
		ParameterDetail pd = new ParameterDetail();
		emailTemplate.setEmailTemplate(pd);
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
		emailTemplate = emailTemplateService.findById(idLong);
	}

	public Boolean validate() {
		Boolean flag = false;
		
		try {
//			if(actionMode.equals(Constants.ACTION_EDIT)) {
//				Integer validateSameValue = emailTemplateService.getEditEmailTemplateByIdAndName(
//						emailTemplate.getEmailTemplateId()
//						, emailTemplate.getEmailTemplate().getParameterDtlCode());
//				
//				if(validateSameValue > 0) {
//					facesUtil.addErrMessage(facesUtil.retrieveMessage("formEmailTemplateEmailTemplateName") + " "
//							+ facesUtil.retrieveMessage("validateRequired"));
//					flag = true;
//				}
//			}
			
			if (StringUtils.isEmpty(emailTemplate.getEmailTemplate().getParameterDtlCode())) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formEmailTemplateEmailTemplateName") + " "
						+ facesUtil.retrieveMessage("validateRequired"));
				flag = true;
			}

			if (emailTemplateService.isEmailTemplateDuplicate(emailTemplate)) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formEmailTemplateEmailTemplateName") + " "
						+ facesUtil.retrieveMessage("errorAlreadyExists"));
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return flag;
	}

	public void save() {
		try {
			if (!validate()) {
				if (emailTemplate.getEmailTemplateId() != null) {
					ParameterDetail pd = parameterDetailService
							.getParameterDetailByParamDtlCode(emailTemplate.getEmailTemplate().getParameterDtlCode());
					emailTemplate.setEmailTemplate(pd);
					emailTemplate.setLastUpdateBy(facesUtil.retrieveUserLogin());
					emailTemplate.setLastUpdateDate(new Timestamp(new Date().getTime()));
					emailTemplate.setDelId(new Long(0));
					emailTemplate.setEnabledFlag(Constants.CONSTANT_YES);
					emailTemplateService.update(emailTemplate);

				} else {
					ParameterDetail pd = parameterDetailService
							.getParameterDetailByParamDtlCode(emailTemplate.getEmailTemplate().getParameterDtlCode());
					emailTemplate.setEmailTemplate(pd);
					emailTemplate.setCreatedBy(facesUtil.retrieveUserLogin());
					emailTemplate.setCreationDate(new Timestamp(new Date().getTime()));
					emailTemplate.setDelId(new Long(0));
					emailTemplate.setEnabledFlag(Constants.CONSTANT_YES);
					emailTemplateService.save(emailTemplate);
				}
				facesUtil.redirect("/pages/emailTemplate/emailTemplate.faces");
			}

		} catch (Exception ex) {
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + ex.getMessage(), "");
		}

	}

	public void cancel() {
		try {
			facesUtil.redirect("/pages/emailTemplate/emailTemplate.faces");
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

	public EmailTemplateService getEmailTemplateService() {
		return emailTemplateService;
	}

	public void setEmailTemplateService(EmailTemplateService emailTemplateService) {
		this.emailTemplateService = emailTemplateService;
	}

	public String getNavigateSearch() {
		return navigateSearch;
	}

	public void setNavigateSearch(String navigateSearch) {
		this.navigateSearch = navigateSearch;
	}

	public EmailTemplate getEmailTemplate() {
		return emailTemplate;
	}

	public void setEmailTemplate(EmailTemplate emailTemplate) {
		this.emailTemplate = emailTemplate;
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


	public List<SelectItem> getTemplateNameList() {
		return templateNameList;
	}

	public void setTemplateNameList(List<SelectItem> templateNameList) {
		this.templateNameList = templateNameList;
	}

}