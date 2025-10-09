package com.wo.module.tmpComplianceReview.model;

import java.io.Serializable;
import java.util.Date;

import com.wo.module.common.model.BaseEntity;

public class TmpComplianceReviewPicFollowupEmail extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 6140880530217262281L;
	private Long complianceReviewPicFollowupEmailId;
	private TmpComplianceReviewPicFollowup tmpComplianceReviewPicFolllowup;
	private Date emailDate;
	private String slaType;
	private Long sla;

	public TmpComplianceReviewPicFollowupEmail() {
		super();
	}

	public Date getEmailDate() {
		return emailDate;
	}

	public void setEmailDate(Date emailDate) {
		this.emailDate = emailDate;
	}

	public Long getComplianceReviewPicFollowupEmailId() {
		return complianceReviewPicFollowupEmailId;
	}

	public void setComplianceReviewPicFollowupEmailId(Long complianceReviewPicFollowupEmailId) {
		this.complianceReviewPicFollowupEmailId = complianceReviewPicFollowupEmailId;
	}

	public TmpComplianceReviewPicFollowup getTmpComplianceReviewPicFolllowup() {
		return tmpComplianceReviewPicFolllowup;
	}

	public void setTmpComplianceReviewPicFolllowup(TmpComplianceReviewPicFollowup tmpComplianceReviewPicFolllowup) {
		this.tmpComplianceReviewPicFolllowup = tmpComplianceReviewPicFolllowup;
	}

	public String getSlaType() {
		return slaType;
	}

	public void setSlaType(String slaType) {
		this.slaType = slaType;
	}

	public Long getSla() {
		return sla;
	}

	public void setSla(Long sla) {
		this.sla = sla;
	}

}
