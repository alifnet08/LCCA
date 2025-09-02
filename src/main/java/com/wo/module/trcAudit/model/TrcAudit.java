package com.wo.module.trcAudit.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.counterType.model.CounterType;
import com.wo.module.mstAudit.model.MstAudit;
import com.wo.module.parameter.model.ParameterDetail;

public class TrcAudit extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -3986736849730798946L;
	private Long auditId;
	private String auditor;
	private String auditObject;
	private String auditTopicIn;
	private String auditTopicEn;
	private String auditCategory;
	private Date khpDate;
	private Date auditDateFrom;
	private Date auditDateTo;
	private String scope;
	private CounterType counterType;
	private String reminderStatus;
	private String followUp;
	private String risk;
	private ParameterDetail status;
	private String findingNameIn;
	private String findingNameEn;
	private MstAudit mstAudit;

	private List<TrcAuditDocument> trcAuditDocuments = new ArrayList<TrcAuditDocument>();
	private List<TrcAuditPicCompliance> trcAuditPicCompliances = new ArrayList<TrcAuditPicCompliance>();
	private List<TrcAuditPicFollowupAttachment> trcAuditPicFollowupAttachments = new ArrayList<TrcAuditPicFollowupAttachment>();
	private List<TrcAuditCheckPoint> trcAuditCheckPoints = new ArrayList<TrcAuditCheckPoint>();

	public Long getAuditId() {
		return auditId;
	}

	public void setAuditId(Long auditId) {
		this.auditId = auditId;
	}

	public String getAuditor() {
		return auditor;
	}

	public void setAuditor(String auditor) {
		this.auditor = auditor;
	}

	public String getAuditObject() {
		return auditObject;
	}

	public void setAuditObject(String auditObject) {
		this.auditObject = auditObject;
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

	public String getAuditCategory() {
		return auditCategory;
	}

	public void setAuditCategory(String auditCategory) {
		this.auditCategory = auditCategory;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getReminderStatus() {
		return reminderStatus;
	}

	public void setReminderStatus(String reminderStatus) {
		this.reminderStatus = reminderStatus;
	}

	public ParameterDetail getStatus() {
		return status;
	}

	public void setStatus(ParameterDetail status) {
		this.status = status;
	}

	public CounterType getCounterType() {
		return counterType;
	}

	public void setCounterType(CounterType counterType) {
		this.counterType = counterType;
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

	public String getFollowUp() {
		return followUp;
	}

	public void setFollowUp(String followUp) {
		this.followUp = followUp;
	}

	public List<TrcAuditDocument> getTrcAuditDocuments() {
		return trcAuditDocuments;
	}

	public void setTrcAuditDocuments(List<TrcAuditDocument> trcAuditDocuments) {
		this.trcAuditDocuments = trcAuditDocuments;
	}

	public List<TrcAuditPicCompliance> getTrcAuditPicCompliances() {
		return trcAuditPicCompliances;
	}

	public void setTrcAuditPicCompliances(List<TrcAuditPicCompliance> trcAuditPicCompliances) {
		this.trcAuditPicCompliances = trcAuditPicCompliances;
	}


	public List<TrcAuditPicFollowupAttachment> getTrcAuditPicFollowupAttachments() {
		return trcAuditPicFollowupAttachments;
	}

	public void setTrcAuditPicFollowupAttachments(List<TrcAuditPicFollowupAttachment> trcAuditPicFollowupAttachments) {
		this.trcAuditPicFollowupAttachments = trcAuditPicFollowupAttachments;
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

	public MstAudit getMstAudit() {
		return mstAudit;
	}

	public void setMstAudit(MstAudit mstAudit) {
		this.mstAudit = mstAudit;
	}

	public String getRisk() {
		return risk;
	}

	public void setRisk(String risk) {
		this.risk = risk;
	}

	public Date getKhpDate() {
		return khpDate;
	}

	public void setKhpDate(Date khpDate) {
		this.khpDate = khpDate;
	}

	public List<TrcAuditCheckPoint> getTrcAuditCheckPoints() {
		return trcAuditCheckPoints;
	}

	public void setTrcAuditCheckPoints(List<TrcAuditCheckPoint> trcAuditCheckPoints) {
		this.trcAuditCheckPoints = trcAuditCheckPoints;
	}
	
	

}
