package com.wo.module.tmpAudit.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.counterType.model.CounterType;
import com.wo.module.mstAudit.model.MstAudit;
import com.wo.module.parameter.model.ParameterDetail;

public class TmpAudit extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -7878121967287192410L;
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

	private List<TmpAuditApproval> tmpAuditApprovals = new ArrayList<TmpAuditApproval>();
	private List<TmpAuditDocument> tmpAuditDocuments = new ArrayList<TmpAuditDocument>();
	private List<TmpAuditPicCompliance> tmpAuditPicCompliances = new ArrayList<TmpAuditPicCompliance>();
	private List<TmpAuditCheckPoint> tmpAuditCheckPoints = new ArrayList<TmpAuditCheckPoint>();

	private String auditTopicName;

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

	public List<TmpAuditApproval> getTmpAuditApprovals() {
		return tmpAuditApprovals;
	}

	public void setTmpAuditApprovals(List<TmpAuditApproval> tmpAuditApprovals) {
		this.tmpAuditApprovals = tmpAuditApprovals;
	}

	public List<TmpAuditDocument> getTmpAuditDocuments() {
		return tmpAuditDocuments;
	}

	public void setTmpAuditDocuments(List<TmpAuditDocument> tmpAuditDocuments) {
		this.tmpAuditDocuments = tmpAuditDocuments;
	}

	public List<TmpAuditPicCompliance> getTmpAuditPicCompliances() {
		return tmpAuditPicCompliances;
	}

	public void setTmpAuditPicCompliances(List<TmpAuditPicCompliance> tmpAuditPicCompliances) {
		this.tmpAuditPicCompliances = tmpAuditPicCompliances;
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

	@SuppressWarnings("static-access")
	public String getAuditTopicName() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();

		if (locale != null && locale.equals(locale.ENGLISH)) {
			auditTopicName = auditTopicEn;
		} else {
			auditTopicName = auditTopicIn;
		}
		return auditTopicName;
	}

	public void setAuditTopicName(String auditTopicName) {
		this.auditTopicName = auditTopicName;
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

	public List<TmpAuditCheckPoint> getTmpAuditCheckPoints() {
		return tmpAuditCheckPoints;
	}

	public void setTmpAuditCheckPoints(List<TmpAuditCheckPoint> tmpAuditCheckPoints) {
		this.tmpAuditCheckPoints = tmpAuditCheckPoints;
	}
	
	
	
}
