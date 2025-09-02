package com.wo.module.complianceTestingMockup.model;

import java.io.Serializable;
import java.util.Date;

import com.wo.module.common.model.BaseEntity;

public class ComplianceTestingPICFollowupEmail extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 4701579265839588096L;
	
	private Long complianceTestingPICFollowEmailId;
	private ComplianceTestingPICFollowup complianceTestingPICFollowup;
	private Date emailDate;
	private String slaType;
	private Long sla;

	private String emailStatus;

	private String emailSubject;

	private String emailContent;

	private Integer resendCount;

	

	public Long getComplianceTestingPICFollowEmailId() {
		return complianceTestingPICFollowEmailId;
	}

	public void setComplianceTestingPICFollowEmailId(Long complianceTestingPICFollowEmailId) {
		this.complianceTestingPICFollowEmailId = complianceTestingPICFollowEmailId;
	}

	public ComplianceTestingPICFollowup getComplianceTestingPICFollowup() {
		return complianceTestingPICFollowup;
	}

	public void setComplianceTestingPICFollowup(ComplianceTestingPICFollowup complianceTestingPICFollowup) {
		this.complianceTestingPICFollowup = complianceTestingPICFollowup;
	}

	public Date getEmailDate() {
		return emailDate;
	}

	public void setEmailDate(Date emailDate) {
		this.emailDate = emailDate;
	}

	public String getSlaType() {
		return slaType;
	}

	public void setSlaType(String slaType) {
		this.slaType = slaType;
	}

	public Long getSla() {
		return sla;
	}

	public void setSla(Long sla) {
		this.sla = sla;
	}

	public String getEmailStatus() {
		return emailStatus;
	}

	public void setEmailStatus(String emailStatus) {
		this.emailStatus = emailStatus;
	}

	public String getEmailSubject() {
		return emailSubject;
	}

	public void setEmailSubject(String emailSubject) {
		this.emailSubject = emailSubject;
	}

	public String getEmailContent() {
		return emailContent;
	}

	public void setEmailContent(String emailContent) {
		this.emailContent = emailContent;
	}

	public Integer getResendCount() {
		return resendCount;
	}

	public void setResendCount(Integer resendCount) {
		this.resendCount = resendCount;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
	
	
}