package com.wo.module.tmpAudit.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.wo.module.common.model.BaseEntity;

public class TmpAuditPicFollowupRec extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -1047401298164310412L;
	private Long auditPicFollowupRecId;
	private TmpAuditPicFollowup tmpAuditPicFollowup;

	private Date targetDate;
	private String rescheduleReason;
	private String notes;

	private Integer sequence;

	private List<TmpAuditPicFollowupRecEmail> tmpAuditPicFollowupRecEmails;

	public Long getAuditPicFollowupRecId() {
		return auditPicFollowupRecId;
	}

	public void setAuditPicFollowupRecId(Long auditPicFollowupRecId) {
		this.auditPicFollowupRecId = auditPicFollowupRecId;
	}

	public TmpAuditPicFollowup getTmpAuditPicFollowup() {
		return tmpAuditPicFollowup;
	}

	public void setTmpAuditPicFollowup(TmpAuditPicFollowup tmpAuditPicFollowup) {
		this.tmpAuditPicFollowup = tmpAuditPicFollowup;
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

	public List<TmpAuditPicFollowupRecEmail> getTmpAuditPicFollowupRecEmails() {
		return tmpAuditPicFollowupRecEmails;
	}

	public void setTmpAuditPicFollowupRecEmails(List<TmpAuditPicFollowupRecEmail> tmpAuditPicFollowupRecEmails) {
		this.tmpAuditPicFollowupRecEmails = tmpAuditPicFollowupRecEmails;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
	

}
