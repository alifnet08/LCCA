package com.wo.module.tmpAudit.model;

import java.io.Serializable;
import java.util.List;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.common.model.ColumnModel;

public class TmpAuditCheckPoint extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -1688724972837542498L;
	private Long auditCheckPointId;
	private TmpAudit tmpAudit;
	private String auditFindings;
	private int column;
	private String hasHeader;
	private String bankResponse;
	
	private List<TmpAuditPicFollowupAuditFindingsTable> tmpAuditPicFollowupAuditFindingsTables;
	
	private List<TmpAuditPicFollowupBankCommitment> tmpAuditPicFollowupBankCommitments;

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

	public TmpAudit getTmpAudit() {
		return tmpAudit;
	}

	public void setTmpAudit(TmpAudit tmpAudit) {
		this.tmpAudit = tmpAudit;
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

	public boolean isDisableEdit() {
		return disableEdit;
	}

	public void setDisableEdit(boolean disableEdit) {
		this.disableEdit = disableEdit;
	}

	public List<TmpAuditPicFollowupAuditFindingsTable> getTmpAuditPicFollowupAuditFindingsTables() {
		return tmpAuditPicFollowupAuditFindingsTables;
	}

	public void setTmpAuditPicFollowupAuditFindingsTables(
			List<TmpAuditPicFollowupAuditFindingsTable> tmpAuditPicFollowupAuditFindingsTables) {
		this.tmpAuditPicFollowupAuditFindingsTables = tmpAuditPicFollowupAuditFindingsTables;
	}

	public List<TmpAuditPicFollowupBankCommitment> getTmpAuditPicFollowupBankCommitments() {
		return tmpAuditPicFollowupBankCommitments;
	}

	public void setTmpAuditPicFollowupBankCommitments(
			List<TmpAuditPicFollowupBankCommitment> tmpAuditPicFollowupBankCommitments) {
		this.tmpAuditPicFollowupBankCommitments = tmpAuditPicFollowupBankCommitments;
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

	public ColumnModel getColumnModel() {
		return columnModel;
	}

	public void setColumnModel(ColumnModel columnModel) {
		this.columnModel = columnModel;
	}

	
	
	

}
