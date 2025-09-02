package com.wo.module.mstAudit.vo;

import java.io.Serializable;
import java.util.Locale;

import javax.faces.context.FacesContext;

public class MstAuditVO implements Serializable {
	private static final long serialVersionUID = -6760546699455346090L;
	private Long mstAuditId;
	private String auditor;
	private String auditorIn;
	private String auditorEn;
	private String auditorCode;
	private String auditTemplateName;
	private String auditTemplateNameIn;
	private String auditTemplateNameEn;
	private String auditDateFrom;
	private String auditDateTo;
	private String scope;

	@SuppressWarnings("static-access")
	public String getAuditor() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if (locale != null && locale.equals(locale.ENGLISH)) {
			auditor = auditorEn;
		} else {
			auditor = auditorIn;
		}
		return auditor;
	}

	public void setAuditor(String auditor) {
		this.auditor = auditor;
	}

	public String getAuditorIn() {
		return auditorIn;
	}

	public void setAuditorIn(String auditorIn) {
		this.auditorIn = auditorIn;
	}

	public String getAuditorEn() {
		return auditorEn;
	}

	public void setAuditorEn(String auditorEn) {
		this.auditorEn = auditorEn;
	}

	public String getAuditDateFrom() {
		return auditDateFrom;
	}

	public void setAuditDateFrom(String auditDateFrom) {
		this.auditDateFrom = auditDateFrom;
	}

	public String getAuditDateTo() {
		return auditDateTo;
	}

	public void setAuditDateTo(String auditDateTo) {
		this.auditDateTo = auditDateTo;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public Long getMstAuditId() {
		return mstAuditId;
	}

	public void setMstAuditId(Long mstAuditId) {
		this.mstAuditId = mstAuditId;
	}

	@SuppressWarnings("static-access")
	public String getAuditTemplateName() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if (locale != null && locale.equals(locale.ENGLISH)) {
			auditTemplateName = auditTemplateNameEn;
		} else {
			auditTemplateName = auditTemplateNameIn;
		}
		return auditTemplateName;
	}

	public void setAuditTemplateName(String auditTemplateName) {
		this.auditTemplateName = auditTemplateName;
	}

	public String getAuditTemplateNameIn() {
		return auditTemplateNameIn;
	}

	public void setAuditTemplateNameIn(String auditTemplateNameIn) {
		this.auditTemplateNameIn = auditTemplateNameIn;
	}

	public String getAuditTemplateNameEn() {
		return auditTemplateNameEn;
	}

	public void setAuditTemplateNameEn(String auditTemplateNameEn) {
		this.auditTemplateNameEn = auditTemplateNameEn;
	}

	public String getAuditorCode() {
		return auditorCode;
	}

	public void setAuditorCode(String auditorCode) {
		this.auditorCode = auditorCode;
	}

}
