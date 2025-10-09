package com.wo.module.complianceTestingMockup.model;

import java.io.Serializable;

import com.wo.module.common.model.BaseEntity;

public class ComplianceTestingPICReviewFW extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 4701579265839588096L;
	
	private Long complianceTestingPICRvwFwId;
	private ComplianceTesting complianceTesting;
	private Long userId;
	private String status;
	
	
	public Long getComplianceTestingPICRvwFwId() {
		return complianceTestingPICRvwFwId;
	}
	public void setComplianceTestingPICRvwFwId(Long complianceTestingPICRvwFwId) {
		this.complianceTestingPICRvwFwId = complianceTestingPICRvwFwId;
	}
	public ComplianceTesting getComplianceTesting() {
		return complianceTesting;
	}
	public void setComplianceTesting(ComplianceTesting complianceTesting) {
		this.complianceTesting = complianceTesting;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
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
	
	
	
	
	
	
	
}