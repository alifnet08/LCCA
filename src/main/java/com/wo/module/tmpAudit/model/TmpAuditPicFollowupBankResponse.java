package com.wo.module.tmpAudit.model;

import java.io.Serializable;

import com.wo.module.common.model.BaseEntity;

public class TmpAuditPicFollowupBankResponse extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 2327294638281128744L;
	private Long auditPicFollowupBankResponseId;
	private TmpAuditPicFollowup tmpAuditPicFollowup;
	private String bankResponse;
	
	//transient
	private boolean checked;

	public TmpAuditPicFollowupBankResponse() {
		super();
	}

	public Long getAuditPicFollowupBankResponseId() {
		return auditPicFollowupBankResponseId;
	}

	public void setAuditPicFollowupBankResponseId(Long auditPicFollowupBankResponseId) {
		this.auditPicFollowupBankResponseId = auditPicFollowupBankResponseId;
	}

	public TmpAuditPicFollowup getTmpAuditPicFollowup() {
		return tmpAuditPicFollowup;
	}

	public void setTmpAuditPicFollowup(TmpAuditPicFollowup tmpAuditPicFollowup) {
		this.tmpAuditPicFollowup = tmpAuditPicFollowup;
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

}
