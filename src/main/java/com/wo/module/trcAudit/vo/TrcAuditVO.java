package com.wo.module.trcAudit.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;

public class TrcAuditVO implements Serializable {

	private static final long serialVersionUID = -8606962301901305297L;
	private Long auditId;
	private String auditTopic;
	private String auditTopicIn;
	private String auditTopicEn;
	private String auditor;
	private String auditorIn;
	private String auditorEn;
	private String auditorCd;

	private String dueDate;
	private String status;
	private String statusIn;
	private String statusEn;

	private Long picFollowupId;
	private String picName1;
	private String picName2;
	private String picName3;
	private String complianceNote;

	private String auditObject;
	private String auditObjectIn;
	private String auditObjectEn;
	private String auditCategory;
	private String auditCategoryIn;
	private String auditCategoryEn;
	private String auditDateFrom;
	private String auditDateTo;
	private String scope;
	
	private String findingName;
	private String findingNameIn;
	private String findingNameEn;
	private String auditTemplateName;
	private String auditTemplateNameIn;
	private String auditTemplateNameEn;

//	private List<StatusConfirmationVO> statusList;

	private List<String> picNameList;

	public Long getAuditId() {
		return auditId;
	}

	public void setAuditId(Long auditId) {
		this.auditId = auditId;
	}

	@SuppressWarnings("static-access")
	public String getAuditTopic() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if (locale != null && locale.equals(locale.ENGLISH)) {
			auditTopic = auditTopicEn;
		} else {
			auditTopic = auditTopicIn;
		}
		return auditTopic;
	}

	public void setAuditTopic(String auditTopic) {
		this.auditTopic = auditTopic;
	}

	public String getAuditTopicIn() {
		return auditTopicIn;
	}

	public void setAuditTopicIn(String auditTopicIn) {
		this.auditTopicIn = auditTopicIn;
	}

	public String getAuditTopicEn() {
		return auditTopicEn;
	}

	public void setAuditTopicEn(String auditTopicEn) {
		this.auditTopicEn = auditTopicEn;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	@SuppressWarnings("static-access")
	public String getStatus() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if (locale != null && locale.equals(locale.ENGLISH)) {
			status = statusEn;
		} else {
			status = statusIn;
		}
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatusIn() {
		return statusIn;
	}

	public void setStatusIn(String statusIn) {
		this.statusIn = statusIn;
	}

	public String getStatusEn() {
		return statusEn;
	}

	public void setStatusEn(String statusEn) {
		this.statusEn = statusEn;
	}

	public Long getPicFollowupId() {
		return picFollowupId;
	}

	public void setPicFollowupId(Long picFollowupId) {
		this.picFollowupId = picFollowupId;
	}

	public String getPicName1() {
		return picName1;
	}

	public void setPicName1(String picName1) {
		this.picName1 = picName1;
	}

	public String getPicName2() {
		return picName2;
	}

	public void setPicName2(String picName2) {
		this.picName2 = picName2;
	}

	public String getPicName3() {
		return picName3;
	}

	public void setPicName3(String picName3) {
		this.picName3 = picName3;
	}

	public List<String> getPicNameList() {
		return picNameList;
	}

	public void setPicNameList(List<String> picNameList) {
		this.picNameList = picNameList;
	}

	public String getComplianceNote() {
		return complianceNote;
	}

	public void setComplianceNote(String complianceNote) {
		this.complianceNote = complianceNote;
	}

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

	public String getAuditorCd() {
		return auditorCd;
	}

	public void setAuditorCd(String auditorCd) {
		this.auditorCd = auditorCd;
	}

	@SuppressWarnings("static-access")
	public String getAuditObject() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if (locale != null && locale.equals(locale.ENGLISH)) {
			auditObject = auditObjectEn;
		} else {
			auditObject = auditObjectIn;
		}
		return auditObject;
	}

	public void setAuditObject(String auditObject) {
		this.auditObject = auditObject;
	}

	public String getAuditCategory() {
		return auditCategory;
	}

	@SuppressWarnings("static-access")
	public void setAuditCategory(String auditCategory) {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if (locale != null && locale.equals(locale.ENGLISH)) {
			auditCategory = auditCategoryEn;
		} else {
			auditCategory = auditCategoryIn;
		}
		this.auditCategory = auditCategory;
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

	public String getAuditObjectIn() {
		return auditObjectIn;
	}

	public void setAuditObjectIn(String auditObjectIn) {
		this.auditObjectIn = auditObjectIn;
	}

	public String getAuditObjectEn() {
		return auditObjectEn;
	}

	public void setAuditObjectEn(String auditObjectEn) {
		this.auditObjectEn = auditObjectEn;
	}

	public String getAuditCategoryIn() {
		return auditCategoryIn;
	}

	public void setAuditCategoryIn(String auditCategoryIn) {
		this.auditCategoryIn = auditCategoryIn;
	}

	public String getAuditCategoryEn() {
		return auditCategoryEn;
	}

	public void setAuditCategoryEn(String auditCategoryEn) {
		this.auditCategoryEn = auditCategoryEn;
	}

	@SuppressWarnings("static-access")
	public String getFindingName() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if (locale != null && locale.equals(locale.ENGLISH)) {
			findingName = findingNameEn;
		} else {
			findingName = findingNameIn;
		}
		return findingName;
	}

	public void setFindingName(String findingName) {
		this.findingName = findingName;
	}

	public String getFindingNameIn() {
		return findingNameIn;
	}

	public void setFindingNameIn(String findingNameIn) {
		this.findingNameIn = findingNameIn;
	}

	public String getFindingNameEn() {
		return findingNameEn;
	}

	public void setFindingNameEn(String findingNameEn) {
		this.findingNameEn = findingNameEn;
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

}
