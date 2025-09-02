package com.wo.module.tmpComplianceReview.model;

import java.io.Serializable;

import com.wo.module.common.model.BaseEntity;

public class TmpComplianceReviewPicFollowupFindings extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -2759235801860249057L;
	private Long complianceReviewPicFollowupFindingsId;
	private TmpComplianceReviewPicFollowup tmpComplianceReviewPicFolllowup;
	private String findings;
	
	// transient
	private int seq;

	public Long getComplianceReviewPicFollowupFindingsId() {
		return complianceReviewPicFollowupFindingsId;
	}

	public void setComplianceReviewPicFollowupFindingsId(Long complianceReviewPicFollowupFindingsId) {
		this.complianceReviewPicFollowupFindingsId = complianceReviewPicFollowupFindingsId;
	}

	public TmpComplianceReviewPicFollowup getTmpComplianceReviewPicFolllowup() {
		return tmpComplianceReviewPicFolllowup;
	}

	public void setTmpComplianceReviewPicFolllowup(TmpComplianceReviewPicFollowup tmpComplianceReviewPicFolllowup) {
		this.tmpComplianceReviewPicFolllowup = tmpComplianceReviewPicFolllowup;
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

}
