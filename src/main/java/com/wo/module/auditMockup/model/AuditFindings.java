package com.wo.module.auditMockup.model;

import java.io.Serializable;
import java.util.List;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.common.model.ColumnModel;
import com.wo.module.tmpAudit.model.TmpAuditPicFollowupAuditFindingsTable;

public class AuditFindings extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -6892656197478961005L;
	private Long auditPicFollowupAuditFindingsId;
	private PoinPemeriksaan poinPemeriksaan;
	private String auditFindings;
	private int column;
	private String hasHeader;
	
	private List<TmpAuditPicFollowupAuditFindingsTable> tmpAuditPicFollowupAuditFindingsTables;

	//transient
	private boolean checked;
	private ColumnModel columnModel;
	
	public AuditFindings() {
		super();
	}

	public Long getAuditPicFollowupAuditFindingsId() {
		return auditPicFollowupAuditFindingsId;
	}

	public void setAuditPicFollowupAuditFindingsId(Long auditPicFollowupAuditFindingsId) {
		this.auditPicFollowupAuditFindingsId = auditPicFollowupAuditFindingsId;
	}

	

	public PoinPemeriksaan getPoinPemeriksaan() {
		return poinPemeriksaan;
	}

	public void setPoinPemeriksaan(PoinPemeriksaan poinPemeriksaan) {
		this.poinPemeriksaan = poinPemeriksaan;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
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

	public List<TmpAuditPicFollowupAuditFindingsTable> getTmpAuditPicFollowupAuditFindingsTables() {
		return tmpAuditPicFollowupAuditFindingsTables;
	}

	public void setTmpAuditPicFollowupAuditFindingsTables(List<TmpAuditPicFollowupAuditFindingsTable> tmpAuditPicFollowupAuditFindingsTables) {
		this.tmpAuditPicFollowupAuditFindingsTables = tmpAuditPicFollowupAuditFindingsTables;
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
