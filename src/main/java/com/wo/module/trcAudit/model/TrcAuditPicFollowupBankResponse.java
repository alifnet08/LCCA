package com.wo.module.trcAudit.model;

import java.io.Serializable;

import com.wo.module.common.model.BaseEntity;

public class TrcAuditPicFollowupBankResponse extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 6053006697703276950L;
	private Long auditPicFollowupBankResponseId;
	private TrcAuditPicFollowup trcAuditPicFollowup;
	private String bankResponse;
	
	//transient
	private boolean checked;

	public TrcAuditPicFollowupBankResponse() {
		super();
	}

	public Long getAuditPicFollowupBankResponseId() {
		return auditPicFollowupBankResponseId;
	}

	public void setAuditPicFollowupBankResponseId(Long auditPicFollowupBankResponseId) {
		this.auditPicFollowupBankResponseId = auditPicFollowupBankResponseId;
	}

	public String getBankResponse() {
		return bankResponse;
	}

	public void setBankResponse(String bankResponse) {
		this.bankResponse = bankResponse;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public TrcAuditPicFollowup getTrcAuditPicFollowup() {
		return trcAuditPicFollowup;
	}

	public void setTrcAuditPicFollowup(TrcAuditPicFollowup trcAuditPicFollowup) {
		this.trcAuditPicFollowup = trcAuditPicFollowup;
	}

}
