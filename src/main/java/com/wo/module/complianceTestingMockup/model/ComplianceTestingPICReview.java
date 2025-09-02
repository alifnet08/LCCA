package com.wo.module.complianceTestingMockup.model;

import java.io.Serializable;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.user.model.User;

public class ComplianceTestingPICReview extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 4701579265839588096L;
	
	private Long complianceTestingPICRvwId;
	private ComplianceTesting complianceTesting;
	private User user;
	private String status;
	
	private int sequence;
	
	public Long getComplianceTestingPICRvwId() {
		return complianceTestingPICRvwId;
	}
	public void setComplianceTestingPICRvwId(Long complianceTestingPICRvwId) {
		this.complianceTestingPICRvwId = complianceTestingPICRvwId;
	}
	public ComplianceTesting getComplianceTesting() {
		return complianceTesting;
	}
	public void setComplianceTesting(ComplianceTesting complianceTesting) {
		this.complianceTesting = complianceTesting;
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
	public int getSequence() {
		return sequence;
	}
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	
	
	
	
	
	
}