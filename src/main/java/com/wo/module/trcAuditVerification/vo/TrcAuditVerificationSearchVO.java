package com.wo.module.trcAuditVerification.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;

import com.wo.module.tmpAudit.vo.AuditConfirmationVO;

public class TrcAuditVerificationSearchVO implements Serializable {

	private static final long serialVersionUID = -3259959259481125891L;
	private Long auditId;
	private Long auditPicFollowupId;
	private String auditTopic;
	private String auditTopicIn;
	private String auditTopicEn;
	private String pic;
	private String dueDate;
	private String status;
	private String statusIn;
	private String statusEn;

	private String auditor;
	private String auditorIn;
	private String auditorEn;
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
	
	private List<AuditConfirmationVO> statusList;

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
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

	public List<AuditConfirmationVO> getStatusList() {
		return statusList;
	}

	public void setStatusList(List<AuditConfirmationVO> statusList) {
		this.statusList = statusList;
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

	public Long getAuditPicFollowupId() {
		return auditPicFollowupId;
	}

	public void setAuditPicFollowupId(Long auditPicFollowupId) {
		this.auditPicFollowupId = auditPicFollowupId;
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
