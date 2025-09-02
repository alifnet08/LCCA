package com.wo.module.trcAudit.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.tmpAudit.model.TmpAuditPicFollowupRec;
import com.wo.module.user.model.User;

public class TrcAuditPicFollowupRec extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -1047401298164310412L;
	private Long auditPicFollowupRecId;
	private TrcAuditPicFollowup trcAuditPicFollowup;

	private Date targetDate;
	private String rescheduleReason;
	private String notes;

	private Integer sequence;
	
	private ParameterDetail followupStatus;
	private User followupBy;
	private Date confirmationDate;
	private Date followupDate;
	private String followupNote;
	private ParameterDetail complianceStatus;
	private String complianceNote;
	private User complianceBy;
	private Date complianceDate;

	private List<TrcAuditPicFollowupRecEmail> trcAuditPicFollowupRecEmails;
	private List<TrcAuditPicFollowupRecAttachment> trcAuditPicFollowupRecAttchs;
	private List<TrcAuditPicFollowupRecExt> trcAuditPicFollowupRecExts;
	

	public Long getAuditPicFollowupRecId() {
		return auditPicFollowupRecId;
	}

	public void setAuditPicFollowupRecId(Long auditPicFollowupRecId) {
		this.auditPicFollowupRecId = auditPicFollowupRecId;
	}

	

	public Date getTargetDate() {
		return targetDate;
	}

	public void setTargetDate(Date targetDate) {
		this.targetDate = targetDate;
	}

	public String getRescheduleReason() {
		return rescheduleReason;
	}

	public void setRescheduleReason(String rescheduleReason) {
		this.rescheduleReason = rescheduleReason;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public TrcAuditPicFollowup getTrcAuditPicFollowup() {
		return trcAuditPicFollowup;
	}

	public void setTrcAuditPicFollowup(TrcAuditPicFollowup trcAuditPicFollowup) {
		this.trcAuditPicFollowup = trcAuditPicFollowup;
	}

	public List<TrcAuditPicFollowupRecEmail> getTrcAuditPicFollowupRecEmails() {
		return trcAuditPicFollowupRecEmails;
	}

	public void setTrcAuditPicFollowupRecEmails(List<TrcAuditPicFollowupRecEmail> trcAuditPicFollowupRecEmails) {
		this.trcAuditPicFollowupRecEmails = trcAuditPicFollowupRecEmails;
	}

	

	public ParameterDetail getFollowupStatus() {
		return followupStatus;
	}

	public void setFollowupStatus(ParameterDetail followupStatus) {
		this.followupStatus = followupStatus;
	}

	public User getFollowupBy() {
		return followupBy;
	}

	public void setFollowupBy(User followupBy) {
		this.followupBy = followupBy;
	}

	public Date getConfirmationDate() {
		return confirmationDate;
	}

	public void setConfirmationDate(Date confirmationDate) {
		this.confirmationDate = confirmationDate;
	}

	public Date getFollowupDate() {
		return followupDate;
	}

	public void setFollowupDate(Date followupDate) {
		this.followupDate = followupDate;
	}

	public String getFollowupNote() {
		return followupNote;
	}

	public void setFollowupNote(String followupNote) {
		this.followupNote = followupNote;
	}

	public ParameterDetail getComplianceStatus() {
		return complianceStatus;
	}

	public void setComplianceStatus(ParameterDetail complianceStatus) {
		this.complianceStatus = complianceStatus;
	}

	public String getComplianceNote() {
		return complianceNote;
	}

	public void setComplianceNote(String complianceNote) {
		this.complianceNote = complianceNote;
	}

	public User getComplianceBy() {
		return complianceBy;
	}

	public void setComplianceBy(User complianceBy) {
		this.complianceBy = complianceBy;
	}

	public Date getComplianceDate() {
		return complianceDate;
	}

	public void setComplianceDate(Date complianceDate) {
		this.complianceDate = complianceDate;
	}

	public List<TrcAuditPicFollowupRecAttachment> getTrcAuditPicFollowupRecAttchs() {
		return trcAuditPicFollowupRecAttchs;
	}

	public void setTrcAuditPicFollowupRecAttchs(List<TrcAuditPicFollowupRecAttachment> trcAuditPicFollowupRecAttchs) {
		this.trcAuditPicFollowupRecAttchs = trcAuditPicFollowupRecAttchs;
	}

	public List<TrcAuditPicFollowupRecExt> getTrcAuditPicFollowupRecExts() {
		return trcAuditPicFollowupRecExts;
	}

	public void setTrcAuditPicFollowupRecExts(List<TrcAuditPicFollowupRecExt> trcAuditPicFollowupRecExts) {
		this.trcAuditPicFollowupRecExts = trcAuditPicFollowupRecExts;
	}
	
	
	
	

}
