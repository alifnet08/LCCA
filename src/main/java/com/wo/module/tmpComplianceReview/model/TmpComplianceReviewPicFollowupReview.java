package com.wo.module.tmpComplianceReview.model;

import java.io.Serializable;

import com.wo.module.common.model.BaseEntity;

public class TmpComplianceReviewPicFollowupReview extends BaseEntity implements Serializable {
	private static final long serialVersionUID = -939090548086042621L;
	private Long complianceReviewPicFollowupReviewId;
	private TmpComplianceReviewPicFollowup tmpComplianceReviewPicFolllowup;
	private String areaReview;
	
	// transient
	private int seq;

	public Long getComplianceReviewPicFollowupReviewId() {
		return complianceReviewPicFollowupReviewId;
	}

	public void setComplianceReviewPicFollowupReviewId(Long complianceReviewPicFollowupReviewId) {
		this.complianceReviewPicFollowupReviewId = complianceReviewPicFollowupReviewId;
	}

	public TmpComplianceReviewPicFollowup getTmpComplianceReviewPicFolllowup() {
		return tmpComplianceReviewPicFolllowup;
	}

	public void setTmpComplianceReviewPicFolllowup(TmpComplianceReviewPicFollowup tmpComplianceReviewPicFolllowup) {
		this.tmpComplianceReviewPicFolllowup = tmpComplianceReviewPicFolllowup;
	}

	public String getAreaReview() {
		return areaReview;
	}

	public void setAreaReview(String areaReview) {
		this.areaReview = areaReview;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

}
