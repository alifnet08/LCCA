package com.wo.module.complianceTestingMockup.model;

import java.io.Serializable;
import java.util.List;

import com.wo.module.common.model.BaseEntity;

public class ComplianceTestingDtl extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 4701579265839588096L;
	
	private Long complianceTestingDtlId;
	private ComplianceTesting complianceTesting;
	private String subject; 
	private String rating;
	
	private String observationResults;
	private String recommendation;
	
	private List<ComplianceTestingPICFollowup> complianceTestingPICFollowups;
	
	private boolean checked;
	private String isFollowup;
	
	public Long getComplianceTestingDtlId() {
		return complianceTestingDtlId;
	}
	public void setComplianceTestingDtlId(Long complianceTestingDtlId) {
		this.complianceTestingDtlId = complianceTestingDtlId;
	}
	public ComplianceTesting getComplianceTesting() {
		return complianceTesting;
	}
	public void setComplianceTesting(ComplianceTesting complianceTesting) {
		this.complianceTesting = complianceTesting;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getRating() {
		return rating;
	}
	public void setRating(String rating) {
		this.rating = rating;
	}
	
	public String getObservationResults() {
		return observationResults;
	}
	public void setObservationResults(String observationResults) {
		this.observationResults = observationResults;
	}
	public String getRecommendation() {
		return recommendation;
	}
	public void setRecommendation(String recommendation) {
		this.recommendation = recommendation;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public List<ComplianceTestingPICFollowup> getComplianceTestingPICFollowups() {
		return complianceTestingPICFollowups;
	}
	public void setComplianceTestingPICFollowups(List<ComplianceTestingPICFollowup> complianceTestingPICFollowups) {
		this.complianceTestingPICFollowups = complianceTestingPICFollowups;
	}
	
	public String getIsFollowup() {
		return isFollowup;
	}
	public void setIsFollowup(String isFollowup) {
		this.isFollowup = isFollowup;
	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	
	
	
	
	
	
}