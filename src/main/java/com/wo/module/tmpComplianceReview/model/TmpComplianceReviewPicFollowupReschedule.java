package com.wo.module.tmpComplianceReview.model;

import java.io.Serializable;
import java.util.Date;

import com.wo.module.common.model.BaseEntity;

public class TmpComplianceReviewPicFollowupReschedule extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -6391019659390961531L;
	private Long complianceReviewPicFollowupRescheduleId;
	private TmpComplianceReviewPicFollowup tmpComplianceReviewPicFolllowup;
	private Date oldTargetDate;
	private Date newTargetDate;

	public TmpComplianceReviewPicFollowupReschedule() {
		super();
	}

	public Long getComplianceReviewPicFollowupRescheduleId() {
		return complianceReviewPicFollowupRescheduleId;
	}

	public void setComplianceReviewPicFollowupRescheduleId(Long complianceReviewPicFollowupRescheduleId) {
		this.complianceReviewPicFollowupRescheduleId = complianceReviewPicFollowupRescheduleId;
	}

	public TmpComplianceReviewPicFollowup getTmpComplianceReviewPicFolllowup() {
		return tmpComplianceReviewPicFolllowup;
	}

	public void setTmpComplianceReviewPicFolllowup(TmpComplianceReviewPicFollowup tmpComplianceReviewPicFolllowup) {
		this.tmpComplianceReviewPicFolllowup = tmpComplianceReviewPicFolllowup;
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

}
