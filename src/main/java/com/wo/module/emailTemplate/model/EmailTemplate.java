package com.wo.module.emailTemplate.model;

import java.io.Serializable;
import java.util.Locale;

import javax.faces.context.FacesContext;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.parameter.model.ParameterDetail;

public class EmailTemplate extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = 6395152164269025885L;
	
	private Long emailTemplateId;
	
	private ParameterDetail emailTemplate;
	private String emailSubject;
	private String emailContent;
	
	private String emailTemplateNameIn;
	private String emailTemplateNameEn;
	private String emailTemplateName;

	public Long getEmailTemplateId() {
		return emailTemplateId;
	}

	public void setEmailTemplateId(Long emailTemplateId) {
		this.emailTemplateId = emailTemplateId;
	}

	public ParameterDetail getEmailTemplate() {
		return emailTemplate;
	}

	public void setEmailTemplate(ParameterDetail emailTemplate) {
		this.emailTemplate = emailTemplate;
	}

	public String getEmailSubject() {
		return emailSubject;
	}

	public void setEmailSubject(String emailSubject) {
		this.emailSubject = emailSubject;
	}
	
	public String getEmailTemplateNameIn() {
		return emailTemplateNameIn;
	}

	public void setEmailTemplateNameIn(String emailTemplateNameIn) {
		this.emailTemplateNameIn = emailTemplateNameIn;
	}

	public String getEmailTemplateNameEn() {
		return emailTemplateNameEn;
	}

	public void setEmailTemplateNameEn(String emailTemplateNameEn) {
		this.emailTemplateNameEn = emailTemplateNameEn;
	}

	@SuppressWarnings("static-access")
	public String getEmailTemplateName() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if (locale != null && locale.equals(locale.ENGLISH)) {
			emailTemplateName = emailTemplateNameEn;
		} else {
			emailTemplateName = emailTemplateNameIn;
		}

		return emailTemplateName;
	}

	public void setEmailTemplateName(String emailTemplateName) {
		this.emailTemplateName = emailTemplateName;
	}

	public String getEmailContent() {
		return emailContent;
	}

	public void setEmailContent(String emailContent) {
		this.emailContent = emailContent;
	}
	
	

}
