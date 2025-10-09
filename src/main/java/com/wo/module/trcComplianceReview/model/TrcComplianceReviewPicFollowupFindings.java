package com.wo.module.trcComplianceReview.model;

import java.io.Serializable;

import com.wo.module.common.model.BaseEntity;

public class TrcComplianceReviewPicFollowupFindings extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -6624493920553307016L;
	private Long complianceReviewPicFollowupFindingsId;
	private TrcComplianceReviewPicFollowup trcComplianceReviewPicFollowup;
	private String findings;

	// transient
	private int seq;

	public Long getComplianceReviewPicFollowupFindingsId() {
		return complianceReviewPicFollowupFindingsId;
	}

	public void setComplianceReviewPicFollowupFindingsId(Long complianceReviewPicFollowupFindingsId) {
		this.complianceReviewPicFollowupFindingsId = complianceReviewPicFollowupFindingsId;
	}

	public String getFindings() {
		return findings;
	}

	public void setFindings(String findings) {
		this.findings = findings;
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
