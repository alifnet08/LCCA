package com.wo.module.complianceTestingMockup.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.counterType.model.CounterType;

public class ComplianceTesting extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 4701579265839588096L;
	
	private Long complianceTestingId; 
	private String inspectionTitle;
	private Long divisionId; 
	private String inspectionNo;
	private Date startDate;
	private Date endDate;
	private String notes;
	private CounterType counterType;
	private String reminderStatus;
	private String status;
	private String overallRating;
	
	private List<ComplianceTestingDtl> complianceTestingDtls;
	//private List<ComplianceTestingFieldWorkDoc> complianceTestingFieldWorkDocs;
	private List<ComplianceTestingDoc> complianceTestingDocs;
	//private List<ComplianceTestingPICReviewFW> complianceTestingPICReviewFWs;
	private List<ComplianceTestingPICReview> complianceTestingPICReviews;
	
	
	
	public String getInspectionTitle() {
		return inspectionTitle;
	}
	public void setInspectionTitle(String inspectionTitle) {
		this.inspectionTitle = inspectionTitle;
	}
	public Long getDivisionId() {
		return divisionId;
	}
	public void setDivisionId(Long divisionId) {
		this.divisionId = divisionId;
	}
	public String getInspectionNo() {
		return inspectionNo;
	}
	public void setInspectionNo(String inspectionNo) {
		this.inspectionNo = inspectionNo;
	}
	
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public CounterType getCounterType() {
		return counterType;
	}
	public void setCounterType(CounterType counterType) {
		this.counterType = counterType;
	}
	public String getReminderStatus() {
		return reminderStatus;
	}
	public void setReminderStatus(String reminderStatus) {
		this.reminderStatus = reminderStatus;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public List<ComplianceTestingDtl> getComplianceTestingDtls() {
		return complianceTestingDtls;
	}
	public void setComplianceTestingDtls(List<ComplianceTestingDtl> complianceTestingDtls) {
		this.complianceTestingDtls = complianceTestingDtls;
	}
	
	
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	public List<ComplianceTestingDoc> getComplianceTestingDocs() {
		return complianceTestingDocs;
	}
	public void setComplianceTestingDocs(List<ComplianceTestingDoc> complianceTestingDocs) {
		this.complianceTestingDocs = complianceTestingDocs;
	}
	public List<ComplianceTestingPICReview> getComplianceTestingPICReviews() {
		return complianceTestingPICReviews;
	}
	public void setComplianceTestingPICReviews(List<ComplianceTestingPICReview> complianceTestingPICReviews) {
		this.complianceTestingPICReviews = complianceTestingPICReviews;
	}
	public Long getComplianceTestingId() {
		return complianceTestingId;
	}
	public void setComplianceTestingId(Long complianceTestingId) {
		this.complianceTestingId = complianceTestingId;
	}
	public String getOverallRating() {
		return overallRating;
	}
	public void setOverallRating(String overallRating) {
		this.overallRating = overallRating;
	}
	
	
	
	
	
	
}