package com.wo.module.litigationView.vo;

import java.io.Serializable;

public class LitigationViewVo implements Serializable{

	private static final long serialVersionUID = -5534058956280979744L;
	
	private Long litigationId;
	private String litigationNo;
	private String caseTypeName;
	private String caseTypeDtlName;
	private String divisionOrBranchOffice;
	private String segment;
	private String createdUpdatePic;
	private String note;
	private String activeStatus;
	private String debtor;
	private String caseNumber;
	private String reportNumber;

	public Long getLitigationId() {
		return litigationId;
	}

	public void setLitigationId(Long litigationId) {
		this.litigationId = litigationId;
	}

	public String getLitigationNo() {
		return litigationNo;
	}

	public void setLitigationNo(String litigationNo) {
		this.litigationNo = litigationNo;
	}

	public String getCaseTypeName() {
		return caseTypeName;
	}

	public void setCaseTypeName(String caseTypeName) {
		this.caseTypeName = caseTypeName;
	}

	public String getCaseTypeDtlName() {
		return caseTypeDtlName;
	}

	public void setCaseTypeDtlName(String caseTypeDtlName) {
		this.caseTypeDtlName = caseTypeDtlName;
	}

	public String getDivisionOrBranchOffice() {
		return divisionOrBranchOffice;
	}

	public void setDivisionOrBranchOffice(String divisionOrBranchOffice) {
		this.divisionOrBranchOffice = divisionOrBranchOffice;
	}

	public String getSegment() {
		return segment;
	}

	public void setSegment(String segment) {
		this.segment = segment;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getCreatedUpdatePic() {
		return createdUpdatePic;
	}

	public void setCreatedUpdatePic(String createdUpdatePic) {
		this.createdUpdatePic = createdUpdatePic;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(String activeStatus) {
		this.activeStatus = activeStatus;
	}

	public String getDebtor() {
		return debtor;
	}

	public void setDebtor(String debtor) {
		this.debtor = debtor;
	}

	public String getCaseNumber() {
		return caseNumber;
	}

	public void setCaseNumber(String caseNumber) {
		this.caseNumber = caseNumber;
	}

	public String getReportNumber() {
		return reportNumber;
	}

	public void setReportNumber(String reportNumber) {
		this.reportNumber = reportNumber;
	}
	
	
	
	
	
}
