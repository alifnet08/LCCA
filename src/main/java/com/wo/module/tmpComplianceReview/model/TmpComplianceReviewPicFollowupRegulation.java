package com.wo.module.tmpComplianceReview.model;

import java.io.Serializable;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.externalRegulation.model.RegulationMst;

public class TmpComplianceReviewPicFollowupRegulation extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = -4867849409150679088L;
	private Long complianceReviewPicFollowupRegulationId;
	private TmpComplianceReviewPicFollowup tmpComplianceReviewPicFolllowup;
	private RegulationMst regulationMst;
	
	// helper
	private String documentNo;
	
	// transient
	private int seq;

	public TmpComplianceReviewPicFollowup getTmpComplianceReviewPicFolllowup() {
		return tmpComplianceReviewPicFolllowup;
	}

	public void setTmpComplianceReviewPicFolllowup(TmpComplianceReviewPicFollowup tmpComplianceReviewPicFolllowup) {
		this.tmpComplianceReviewPicFolllowup = tmpComplianceReviewPicFolllowup;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public RegulationMst getRegulationMst() {
		return regulationMst;
	}

	public void setRegulationMst(RegulationMst regulationMst) {
		this.regulationMst = regulationMst;
	}

	public Long getComplianceReviewPicFollowupRegulationId() {
		return complianceReviewPicFollowupRegulationId;
	}

	public void setComplianceReviewPicFollowupRegulationId(Long complianceReviewPicFollowupRegulationId) {
		this.complianceReviewPicFollowupRegulationId = complianceReviewPicFollowupRegulationId;
	}

	public String getDocumentNo() {
		return documentNo;
	}

	public void setDocumentNo(String documentNo) {
		this.documentNo = documentNo;
	}

}
