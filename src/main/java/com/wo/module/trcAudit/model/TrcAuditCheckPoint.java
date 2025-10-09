package com.wo.module.trcAudit.model;

import java.io.Serializable;
import java.util.List;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.common.model.ColumnModel;

public class TrcAuditCheckPoint extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -1688724972837542498L;
	private Long auditCheckPointId;
	private TrcAudit trcAudit;
	private String auditFindings;
	private int column;
	private String hasHeader;
	private String bankResponse;
	
	private List<TrcAuditPicFollowupAuditFindingsTable> trcAuditPicFollowupAuditFindingsTables;
	
	private List<TrcAuditPicFollowupBankCommitment> trcAuditPicFollowupBankCommitments;
	
	private int sequence;
	
	private boolean checked;
	
	private boolean disableEdit;
	
	private ColumnModel columnModel;
	
	public Long getAuditCheckPointId() {
		return auditCheckPointId;
	}
	public void setAuditCheckPointId(Long auditCheckPointId) {
		this.auditCheckPointId = auditCheckPointId;
	}
	public TrcAudit getTrcAudit() {
		return trcAudit;
	}
	public void setTrcAudit(TrcAudit trcAudit) {
		this.trcAudit = trcAudit;
	}
	public String getAuditFindings() {
		return auditFindings;
	}
	public void setAuditFindings(String auditFindings) {
		this.auditFindings = auditFindings;
	}
	public int getColumn() {
		return column;
	}
	public void setColumn(int column) {
		this.column = column;
	}
	public String getHasHeader() {
		return hasHeader;
	}
	public void setHasHeader(String hasHeader) {
		this.hasHeader = hasHeader;
	}
	
	public String getBankResponse() {
		return bankResponse;
	}
	public void setBankResponse(String bankResponse) {
		this.bankResponse = bankResponse;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public List<TrcAuditPicFollowupAuditFindingsTable> getTrcAuditPicFollowupAuditFindingsTables() {
		return trcAuditPicFollowupAuditFindingsTables;
	}
	public void setTrcAuditPicFollowupAuditFindingsTables(
			List<TrcAuditPicFollowupAuditFindingsTable> trcAuditPicFollowupAuditFindingsTables) {
		this.trcAuditPicFollowupAuditFindingsTables = trcAuditPicFollowupAuditFindingsTables;
	}
	public List<TrcAuditPicFollowupBankCommitment> getTrcAuditPicFollowupBankCommitments() {
		return trcAuditPicFollowupBankCommitments;
	}
	public void setTrcAuditPicFollowupBankCommitments(
			List<TrcAuditPicFollowupBankCommitment> trcAuditPicFollowupBankCommitments) {
		this.trcAuditPicFollowupBankCommitments = trcAuditPicFollowupBankCommitments;
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
	public ColumnModel getColumnModel() {
		return columnModel;
	}
	public void setColumnModel(ColumnModel columnModel) {
		this.columnModel = columnModel;
	}
	
	

}
