package com.wo.module.tmpAuditApproval.vo;

import java.io.Serializable;
import java.util.Locale;

import javax.faces.context.FacesContext;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.counterType.model.CounterType;

public class TmpAuditApprovalVO extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = -7244786550452641516L;
	private Long auditId;
	private String auditor;
	private String auditorIn;
	private String auditorEn;
	private String auditObject;
	private String auditObjectIn;
	private String auditObjectEn;
	private String auditTopic;
	private String auditTopicIn;
	private String auditTopicEn;
	private String auditCategory;
	private String auditCategoryIn;
	private String auditCategoryEn;
	private String auditDateFrom;
	private String auditDateTo;
	private String scope;
	private String auditFindings;
	private String bankResponse;
	private String bankCommitment;
	private CounterType counterType;
	private String reminderStatus;
	private String statusCd;
	private String status;
	private String statusIn;
	private String statusEn;
	private String followupStatusCd;
	
	private String approvalBy;
	private String approvalStatus;
	private String approvalStatusEn;
	private String approvalStatusIn;
	private String approvalDate;
	private String approvalNote;
	
	private String findingName;
	private String findingNameIn;
	private String findingNameEn;

	private String auditTemplateName;
	private String auditTemplateNameIn;
	private String auditTemplateNameEn;
	
	public Long getAuditId() {
		return auditId;
	}

	public void setAuditId(Long auditId) {
		this.auditId = auditId;
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

	public String getAuditFindings() {
		return auditFindings;
	}

	public void setAuditFindings(String auditFindings) {
		this.auditFindings = auditFindings;
	}

	public String getBankResponse() {
		return bankResponse;
	}

	public void setBankResponse(String bankResponse) {
		this.bankResponse = bankResponse;
	}

	public String getBankCommitment() {
		return bankCommitment;
	}

	public void setBankCommitment(String bankCommitment) {
		this.bankCommitment = bankCommitment;
	}

	public CounterType getCounterType() {
		return counterType;
	}

	public void setCounterType(CounterType counterType) {
		this.counterType = counterType;
	}

	public String getReminderStatus() {
		return reminderStatus;
	}

	public void setReminderStatus(String reminderStatus) {
		this.reminderStatus = reminderStatus;
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

	public String getFollowupStatusCd() {
		return followupStatusCd;
	}

	public void setFollowupStatusCd(String followupStatusCd) {
		this.followupStatusCd = followupStatusCd;
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

	@SuppressWarnings("static-access")
	public String getAuditCategory() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if (locale != null && locale.equals(locale.ENGLISH)) {
			auditCategory = auditCategoryEn;
		} else {
			auditCategory = auditCategoryIn;
		}
		return auditCategory;
	}

	public void setAuditCategory(String auditCategory) {
		this.auditCategory = auditCategory;
	}

	public String getStatusCd() {
		return statusCd;
	}

	public void setStatusCd(String statusCd) {
		this.statusCd = statusCd;
	}

	public String getApprovalBy() {
		return approvalBy;
	}

	public void setApprovalBy(String approvalBy) {
		this.approvalBy = approvalBy;
	}

	@SuppressWarnings("static-access")
	public String getApprovalStatus() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if (locale != null && locale.equals(locale.ENGLISH)) {
			approvalStatus = approvalStatusEn;
		} else {
			approvalStatus = approvalStatusIn;
		}

		return approvalStatus;
	}

	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public String getApprovalStatusEn() {
		return approvalStatusEn;
	}

	public void setApprovalStatusEn(String approvalStatusEn) {
		this.approvalStatusEn = approvalStatusEn;
	}

	public String getApprovalStatusIn() {
		return approvalStatusIn;
	}

	public void setApprovalStatusIn(String approvalStatusIn) {
		this.approvalStatusIn = approvalStatusIn;
	}

	public String getApprovalDate() {
		return approvalDate;
	}

	public void setApprovalDate(String approvalDate) {
		this.approvalDate = approvalDate;
	}

	public String getApprovalNote() {
		return approvalNote;
	}

	public void setApprovalNote(String approvalNote) {
		this.approvalNote = approvalNote;
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
