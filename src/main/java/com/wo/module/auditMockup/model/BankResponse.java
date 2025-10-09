package com.wo.module.auditMockup.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.wo.module.common.model.BaseEntity;

public class BankResponse extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 2327294638281128744L;
	private Long auditPicFollowupBankResponseId;
	private PoinPemeriksaan poinPemeriksaan;
	private String bankResponse;
	
	private List<BankCommitment> bankCommitments = new ArrayList<BankCommitment>();
	//transient
	private boolean checked;

	public BankResponse() {
		super();
	}

	public Long getAuditPicFollowupBankResponseId() {
		return auditPicFollowupBankResponseId;
	}

	public void setAuditPicFollowupBankResponseId(Long auditPicFollowupBankResponseId) {
		this.auditPicFollowupBankResponseId = auditPicFollowupBankResponseId;
	}

	

	public PoinPemeriksaan getPoinPemeriksaan() {
		return poinPemeriksaan;
	}

	public void setPoinPemeriksaan(PoinPemeriksaan poinPemeriksaan) {
		this.poinPemeriksaan = poinPemeriksaan;
	}

	public List<BankCommitment> getBankCommitments() {
		return bankCommitments;
	}

	public void setBankCommitments(List<BankCommitment> bankCommitments) {
		this.bankCommitments = bankCommitments;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
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
