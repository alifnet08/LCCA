package com.wo.module.auditMockup.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.wo.module.tmpAudit.model.TmpAuditApproval;

public class PoinPemeriksaan implements Serializable {
	private static final long serialVersionUID = -2918504160003562655L;
	private List<AuditFindings> auditFindings = new ArrayList<AuditFindings>();
	private List<BankResponse> bankResponses = new ArrayList<BankResponse>();
	
	private int sequence;
	
	private boolean checked;
	
	private boolean disableEdit;
	
	public List<AuditFindings> getAuditFindings() {
		return auditFindings;
	}
	public void setAuditFindings(List<AuditFindings> auditFindings) {
		this.auditFindings = auditFindings;
	}
	public List<BankResponse> getBankResponses() {
		return bankResponses;
	}
	public void setBankResponses(List<BankResponse> bankResponses) {
		this.bankResponses = bankResponses;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public int getSequence() {
		return sequence;
	}
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	public boolean isDisableEdit() {
		return disableEdit;
	}
	public void setDisableEdit(boolean disableEdit) {
		this.disableEdit = disableEdit;
	}
	
	
	
}
