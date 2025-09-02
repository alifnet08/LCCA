package com.wo.module.tmpAudit.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.wo.module.common.model.BaseEntity;

public class TmpAuditPicFollowupBankCommitment extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -1875824242862762223L;
	private Long auditPicFollowupBankCommitmentId;
	private TmpAuditCheckPoint tmpAuditCheckPoint;
	private String bankCommitment;
	
	private List<TmpAuditPicFollowup> tmpAuditPicFollowups;
	//transient
	private boolean checked;
	
	private String isRecurr;
	
	private String recurringType;
	
	private Date recurringEndDate;
	
	public TmpAuditPicFollowupBankCommitment() {
		super();
	}

	public Long getAuditPicFollowupBankCommitmentId() {
		return auditPicFollowupBankCommitmentId;
	}

	public void setAuditPicFollowupBankCommitmentId(Long auditPicFollowupBankCommitmentId) {
		this.auditPicFollowupBankCommitmentId = auditPicFollowupBankCommitmentId;
	}


	public TmpAuditCheckPoint getTmpAuditCheckPoint() {
		return tmpAuditCheckPoint;
	}

	public void setTmpAuditCheckPoint(TmpAuditCheckPoint tmpAuditCheckPoint) {
		this.tmpAuditCheckPoint = tmpAuditCheckPoint;
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

	public List<TmpAuditPicFollowup> getTmpAuditPicFollowups() {
		return tmpAuditPicFollowups;
	}

	public void setTmpAuditPicFollowups(List<TmpAuditPicFollowup> tmpAuditPicFollowups) {
		this.tmpAuditPicFollowups = tmpAuditPicFollowups;
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
