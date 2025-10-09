package com.wo.module.tmpFine.model;

import java.io.Serializable;
import java.util.Date;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.user.model.User;

public class TmpFineApproval extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1610586809672446260L;
	private Long fineApprovalId;
	private TmpFine tmpFine;
	private User user;
	private ParameterDetail approvalStatus;
	private Date approvalDate;
	private String approvalNote;

	public TmpFineApproval() {
		super();
	}

	public Long getFineApprovalId() {
		return fineApprovalId;
	}

	public void setFineApprovalId(Long fineApprovalId) {
		this.fineApprovalId = fineApprovalId;
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

	public TmpFine getTmpFine() {
		return tmpFine;
	}

	public void setTmpFine(TmpFine tmpFine) {
		this.tmpFine = tmpFine;
	}
}
