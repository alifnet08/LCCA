package com.wo.module.trcComplianceReview.model;

import java.io.Serializable;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.externalRegulation.model.RegulationMst;

public class TrcComplianceReviewPicFollowupRegulation extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = 3017863723934930415L;
	private Long complianceReviewPicFollowupRegulationId;
	private TrcComplianceReviewPicFollowup trcComplianceReviewPicFolllowup;
	private RegulationMst regulationMst;
	
	// helper
	private String documentNo;
	
	// transient
	private int seq;

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

	public TrcComplianceReviewPicFollowup getTrcComplianceReviewPicFolllowup() {
		return trcComplianceReviewPicFolllowup;
	}

	public void setTrcComplianceReviewPicFolllowup(TrcComplianceReviewPicFollowup trcComplianceReviewPicFolllowup) {
		this.trcComplianceReviewPicFolllowup = trcComplianceReviewPicFolllowup;
	}

	public String getDocumentNo() {
		return documentNo;
	}

	public void setDocumentNo(String documentNo) {
		this.documentNo = documentNo;
	}

}
