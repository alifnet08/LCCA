package com.wo.module.mstAudit.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Locale;

import javax.faces.context.FacesContext;

import com.wo.module.common.model.BaseEntity;

public class MstAudit extends BaseEntity implements Serializable {
	private static final long serialVersionUID = -1436190368347707084L;
	private Long mstAuditId;
	private String auditor;
	private String auditTemplate;
	private String auditTemplateIn;
	private String auditTemplateEn;
	private Date auditDateFrom;
	private Date auditDateTo;
	private String scope;

	public Long getMstAuditId() {
		return mstAuditId;
	}

	public void setMstAuditId(Long mstAuditId) {
		this.mstAuditId = mstAuditId;
	}

	public String getAuditor() {
		return auditor;
	}

	public void setAuditor(String auditor) {
		this.auditor = auditor;
	}

	@SuppressWarnings("static-access")
	public String getAuditTemplate() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if(locale!=null && locale.equals(locale.ENGLISH)) {
			auditTemplate = auditTemplateEn;
		}else {
			auditTemplate = auditTemplateIn;
		}
		return auditTemplate;
	}

	public void setAuditTemplate(String auditTemplate) {
		this.auditTemplate = auditTemplate;
	}

	public Date getAuditDateFrom() {
		return auditDateFrom;
	}

	public void setAuditDateFrom(Date auditDateFrom) {
		this.auditDateFrom = auditDateFrom;
	}

	public Date getAuditDateTo() {
		return auditDateTo;
	}

	public void setAuditDateTo(Date auditDateTo) {
		this.auditDateTo = auditDateTo;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getAuditTemplateIn() {
		return auditTemplateIn;
	}

	public void setAuditTemplateIn(String auditTemplateIn) {
		this.auditTemplateIn = auditTemplateIn;
	}

	public String getAuditTemplateEn() {
		return auditTemplateEn;
	}

	public void setAuditTemplateEn(String auditTemplateEn) {
		this.auditTemplateEn = auditTemplateEn;
	}

}
