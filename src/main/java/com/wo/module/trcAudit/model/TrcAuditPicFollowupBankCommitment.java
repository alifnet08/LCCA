package com.wo.module.trcAudit.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.wo.module.common.model.BaseEntity;

public class TrcAuditPicFollowupBankCommitment extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -4283223315183954915L;
	private Long auditPicFollowupBankCommitmentId;
	private TrcAuditCheckPoint trcAuditCheckPoint;
	private String bankCommitment;
	
	private String isRecurr;
	
	private String recurringType;
	
	private Date recurringEndDate;
	
	private List<TrcAuditPicFollowup> trcAuditPicFollowups;
	
	//transient
	private boolean checked;
	
	public TrcAuditPicFollowupBankCommitment() {
		super();
	}

	public Long getAuditPicFollowupBankCommitmentId() {
		return auditPicFollowupBankCommitmentId;
	}

	public void setAuditPicFollowupBankCommitmentId(Long auditPicFollowupBankCommitmentId) {
		this.auditPicFollowupBankCommitmentId = auditPicFollowupBankCommitmentId;
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

	public TrcAuditCheckPoint getTrcAuditCheckPoint() {
		return trcAuditCheckPoint;
	}

	public void setTrcAuditCheckPoint(TrcAuditCheckPoint trcAuditCheckPoint) {
		this.trcAuditCheckPoint = trcAuditCheckPoint;
	}

	public List<TrcAuditPicFollowup> getTrcAuditPicFollowups() {
		return trcAuditPicFollowups;
	}

	public void setTrcAuditPicFollowups(List<TrcAuditPicFollowup> trcAuditPicFollowups) {
		this.trcAuditPicFollowups = trcAuditPicFollowups;
	}

	public String getIsRecurr() {
		return isRecurr;
	}

	public void setIsRecurr(String isRecurr) {
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	

}
