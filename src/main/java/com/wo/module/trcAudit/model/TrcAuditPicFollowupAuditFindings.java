package com.wo.module.trcAudit.model;

import java.io.Serializable;
import java.util.List;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.common.model.ColumnModel;

public class TrcAuditPicFollowupAuditFindings extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 475236966354597565L;
	private Long auditPicFollowupAuditFindingsId;
	private TrcAuditPicFollowup trcAuditPicFollowup;
	private String auditFindings;
	private int column;
	private String hasHeader;
	
	private List<TrcAuditPicFollowupAuditFindingsTable> trcAuditPicFollowupAuditFindingsTables;

	//transient
	private boolean checked;
	private ColumnModel columnModel;
	
	public TrcAuditPicFollowupAuditFindings() {
		super();
	}

	public Long getAuditPicFollowupAuditFindingsId() {
		return auditPicFollowupAuditFindingsId;
	}

	public void setAuditPicFollowupAuditFindingsId(Long auditPicFollowupAuditFindingsId) {
		this.auditPicFollowupAuditFindingsId = auditPicFollowupAuditFindingsId;
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

	public List<TrcAuditPicFollowupAuditFindingsTable> getTrcAuditPicFollowupAuditFindingsTables() {
		return trcAuditPicFollowupAuditFindingsTables;
	}

	public void setTrcAuditPicFollowupAuditFindingsTables(
			List<TrcAuditPicFollowupAuditFindingsTable> trcAuditPicFollowupAuditFindingsTables) {
		this.trcAuditPicFollowupAuditFindingsTables = trcAuditPicFollowupAuditFindingsTables;
	}

	public ColumnModel getColumnModel() {
		return columnModel;
	}

	public void setColumnModel(ColumnModel columnModel) {
		this.columnModel = columnModel;
	}

}
