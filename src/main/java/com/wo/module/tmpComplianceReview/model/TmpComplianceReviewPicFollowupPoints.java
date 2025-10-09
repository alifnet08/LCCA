package com.wo.module.tmpComplianceReview.model;

import java.io.Serializable;

import com.wo.module.common.model.BaseEntity;

public class TmpComplianceReviewPicFollowupPoints extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -2113211494356274810L;
	private Long complianceReviewPicFollowupPointsId;
	private TmpComplianceReviewPicFollowup tmpComplianceReviewPicFolllowup;
	private String followupPoints;

	// transient
	private int seq;

	public Long getComplianceReviewPicFollowupPointsId() {
		return complianceReviewPicFollowupPointsId;
	}

	public void setComplianceReviewPicFollowupPointsId(Long complianceReviewPicFollowupPointsId) {
		this.complianceReviewPicFollowupPointsId = complianceReviewPicFollowupPointsId;
	}

	public TmpComplianceReviewPicFollowup getTmpComplianceReviewPicFolllowup() {
		return tmpComplianceReviewPicFolllowup;
	}

	public void setTmpComplianceReviewPicFolllowup(TmpComplianceReviewPicFollowup tmpComplianceReviewPicFolllowup) {
		this.tmpComplianceReviewPicFolllowup = tmpComplianceReviewPicFolllowup;
	}

	public String getFollowupPoints() {
		return followupPoints;
	}

	public void setFollowupPoints(String followupPoints) {
		this.followupPoints = followupPoints;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

}
