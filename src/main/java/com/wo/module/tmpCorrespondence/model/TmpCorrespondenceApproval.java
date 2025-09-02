package com.wo.module.tmpCorrespondence.model;

import java.io.Serializable;
import java.util.Date;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.user.model.User;

public class TmpCorrespondenceApproval extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1610586809672446260L;
	private Long correspondenceApprovalId;
	private TmpCorrespondence tmpCorrespondence;
	private User user;
	private ParameterDetail approvalStatus;
	private Date approvalDate;
	private String approvalNote;

	public TmpCorrespondenceApproval() {
		super();
	}

	public Long getCorrespondenceApprovalId() {
		return correspondenceApprovalId;
	}

	public void setCorrespondenceApprovalId(Long correspondenceApprovalId) {
		this.correspondenceApprovalId = correspondenceApprovalId;
	}

	public ParameterDetail getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(ParameterDetail approvalStatus) {
		this.approvalStatus = approvalStatus;
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public TmpCorrespondence getTmpCorrespondence() {
		return tmpCorrespondence;
	}

	public void setTmpCorrespondence(TmpCorrespondence tmpCorrespondence) {
		this.tmpCorrespondence = tmpCorrespondence;
	}
}
