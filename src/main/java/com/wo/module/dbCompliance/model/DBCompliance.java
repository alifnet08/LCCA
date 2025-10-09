package com.wo.module.dbCompliance.model;

import java.io.Serializable;
import java.util.List;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.reportType.model.ReportType;

public class DBCompliance extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 6395152164269025885L;

	private Long databaseComplianceId;
	private String reportNameIn;
	private String reportNameEn;
	private ReportType reportType;
	private String description;
	private String note;
	
	private List<DBComplianceDoc> dbComplianceDocs;
	private List<DBComplianceDueDate> dbComplianceDuedates;
	
	private String reportTypeStr;
	
	public Long getDatabaseComplianceId() {
		return databaseComplianceId;
	}
	public void setDatabaseComplianceId(Long databaseComplianceId) {
		this.databaseComplianceId = databaseComplianceId;
	}
	public String getReportNameIn() {
		return reportNameIn;
	}
	public void setReportNameIn(String reportNameIn) {
		this.reportNameIn = reportNameIn;
	}
	public String getReportNameEn() {
		return reportNameEn;
	}
	public void setReportNameEn(String reportNameEn) {
		this.reportNameEn = reportNameEn;
	}
	
	public ReportType getReportType() {
		return reportType;
	}
	public void setReportType(ReportType reportType) {
		this.reportType = reportType;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public List<DBComplianceDoc> getDbComplianceDocs() {
		return dbComplianceDocs;
	}
	public void setDbComplianceDocs(List<DBComplianceDoc> dbComplianceDocs) {
		this.dbComplianceDocs = dbComplianceDocs;
	}
	public List<DBComplianceDueDate> getDbComplianceDuedates() {
		return dbComplianceDuedates;
	}
	public void setDbComplianceDuedates(List<DBComplianceDueDate> dbComplianceDuedates) {
		this.dbComplianceDuedates = dbComplianceDuedates;
	}
	public String getReportTypeStr() {
		return reportTypeStr;
	}
	public void setReportTypeStr(String reportTypeStr) {
		this.reportTypeStr = reportTypeStr;
	}
	
	
	
	
}
