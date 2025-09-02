package com.wo.module.auditMockup.model;

import java.io.Serializable;
import java.util.Date;
import com.wo.module.common.model.BaseEntity;

public class BankCommitment extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -1875824242862762223L;
	private Long auditPicFollowupBankCommitmentId;
	private BankResponse bankResponse;
	private String bankCommitment;
	
	private Boolean isRecurr;
	private String recurringType;
	
	private Date recurringEndDate;
	
	private PicFollowup picFollowup;
	
	//transient
	private boolean checked;
	
	public BankCommitment() {
		super();
	}

	public Long getAuditPicFollowupBankCommitmentId() {
		return auditPicFollowupBankCommitmentId;
	}

	public void setAuditPicFollowupBankCommitmentId(Long auditPicFollowupBankCommitmentId) {
		this.auditPicFollowupBankCommitmentId = auditPicFollowupBankCommitmentId;
	}
	

	public BankResponse getBankResponse() {
		return bankResponse;
	}

	public void setBankResponse(BankResponse bankResponse) {
		this.bankResponse = bankResponse;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getBankCommitment() {
		return bankCommitment;
	}

	public void setBankCommitment(String bankCommitment) {
		this.bankCommitment = bankCommitment;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public PicFollowup getPicFollowup() {
		return picFollowup;
	}

	public void setPicFollowup(PicFollowup picFollowup) {
		this.picFollowup = picFollowup;
	}

	public Boolean getIsRecurr() {
		return isRecurr;
	}

	public void setIsRecurr(Boolean isRecurr) {
		this.isRecurr = isRecurr;
	}

	public String getRecurringType() {
		return recurringType;
	}

	public void setRecurringType(String recurringType) {
		this.recurringType = recurringType;
	}

	public Date getRecurringEndDate() {
		return recurringEndDate;
	}

	public void setRecurringEndDate(Date recurringEndDate) {
		this.recurringEndDate = recurringEndDate;
	}
	
}
