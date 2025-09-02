package com.wo.module.tmpAudit.model;

import java.io.Serializable;
import java.util.Date;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.user.model.User;

public class TmpAuditApproval extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -7536606876839447175L;
	private Long auditApprovalId;
	private TmpAudit tmpAudit;
	private User user;
	private ParameterDetail approvalStatus;
	private Date approvalDate;
	private String approvalNote;

	public Long getAuditApprovalId() {
		return auditApprovalId;
	}

	public void setAuditApprovalId(Long auditApprovalId) {
		this.auditApprovalId = auditApprovalId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getApprovalNote() {
		return approvalNote;
	}

	public void setApprovalNote(String approvalNote) {
		this.approvalNote = approvalNote;
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

	public TmpAudit getTmpAudit() {
		return tmpAudit;
	}

	public void setTmpAudit(TmpAudit tmpAudit) {
		this.tmpAudit = tmpAudit;
	}

}
