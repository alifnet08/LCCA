package com.wo.module.tmpAudit.model;

import java.io.Serializable;
import java.util.Date;

import com.wo.module.common.model.BaseEntity;

public class TmpAuditPicFollowupReschedule extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -6452988407646486697L;
	private Long auditPicFollowupRescheduleId;
	private TmpAuditPicFollowup tmpAuditPicFollowup;
	private Date oldTargetDate;
	private Date newTargetDate;
	private String rescheduleReason;

	public TmpAuditPicFollowupReschedule() {
		super();
	}

	public Long getAuditPicFollowupRescheduleId() {
		return auditPicFollowupRescheduleId;
	}

	public void setAuditPicFollowupRescheduleId(Long auditPicFollowupRescheduleId) {
		this.auditPicFollowupRescheduleId = auditPicFollowupRescheduleId;
	}

	public TmpAuditPicFollowup getTmpAuditPicFollowup() {
		return tmpAuditPicFollowup;
	}

	public void setTmpAuditPicFollowup(TmpAuditPicFollowup tmpAuditPicFollowup) {
		this.tmpAuditPicFollowup = tmpAuditPicFollowup;
	}

	public Date getOldTargetDate() {
		return oldTargetDate;
	}

	public void setOldTargetDate(Date oldTargetDate) {
		this.oldTargetDate = oldTargetDate;
	}

	public Date getNewTargetDate() {
		return newTargetDate;
	}

	public void setNewTargetDate(Date newTargetDate) {
		this.newTargetDate = newTargetDate;
	}

	public String getRescheduleReason() {
		return rescheduleReason;
	}

	public void setRescheduleReason(String rescheduleReason) {
		this.rescheduleReason = rescheduleReason;
	}

}
