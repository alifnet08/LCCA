package com.wo.module.trcComplianceReview.model;

import java.io.Serializable;

import com.wo.module.common.model.BaseEntity;

public class TrcComplianceReviewPicFollowupReview extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = 2545633204900198949L;
	private Long complianceReviewPicFollowupReviewId;
	private TrcComplianceReviewPicFollowup trcComplianceReviewPicFollowup;
	private String areaReview;
	
	// transient
	private int seq;

	public Long getComplianceReviewPicFollowupReviewId() {
		return complianceReviewPicFollowupReviewId;
	}

	public void setComplianceReviewPicFollowupReviewId(Long complianceReviewPicFollowupReviewId) {
		this.complianceReviewPicFollowupReviewId = complianceReviewPicFollowupReviewId;
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

	public TrcComplianceReviewPicFollowup getTrcComplianceReviewPicFollowup() {
		return trcComplianceReviewPicFollowup;
	}

	public void setTrcComplianceReviewPicFollowup(TrcComplianceReviewPicFollowup trcComplianceReviewPicFollowup) {
		this.trcComplianceReviewPicFollowup = trcComplianceReviewPicFollowup;
	}

}
