package com.wo.module.tmpComplianceReviewApproval.model;

import java.io.Serializable;
import java.util.Date;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.tmpComplianceReview.model.TmpComplianceReview;
import com.wo.module.user.model.User;

public class TmpComplianceReviewApproval extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 6395152164269025885L;
	private Long complianceReviewApprovalId;
	private TmpComplianceReview tmpComplianceReview;
	private User user;
	private String approvalStatus;
	private Date approvalDate;
	private String approvalNote;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getApprovalDate() {
		return approvalDate;
	}

	public void setApprovalDate(Date approvalDate) {
		this.approvalDate = approvalDate;
	}

	public String getApprovalNote() {
		return approvalNote;
	}

	public void setApprovalNote(String approvalNote) {
		this.approvalNote = approvalNote;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Long getComplianceReviewApprovalId() {
		return complianceReviewApprovalId;
	}

	public void setComplianceReviewApprovalId(Long complianceReviewApprovalId) {
		this.complianceReviewApprovalId = complianceReviewApprovalId;
	}

	public String getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public TmpComplianceReview getTmpComplianceReview() {
		return tmpComplianceReview;
	}

	public void setTmpComplianceReview(TmpComplianceReview tmpComplianceReview) {
		this.tmpComplianceReview = tmpComplianceReview;
	}

}
