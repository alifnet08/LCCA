package com.wo.module.regulationMonitoring.model;

import java.io.Serializable;
import java.util.Date;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.user.model.User;

public class RegMonitoringApprovalTmp extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -1961328255846585656L;
	
	private Long regMonitoringApprovalTmpId;
	private RegMonitoringTmp regMonitoringTmp;
	private User user;
	
	private String approvalStatus;
	private Date approvalDate;
	private String approvalNote;
	
	private Long delId;

	public Long getRegMonitoringApprovalTmpId() {
		return regMonitoringApprovalTmpId;
	}

	public void setRegMonitoringApprovalTmpId(Long regMonitoringApprovalTmpId) {
		this.regMonitoringApprovalTmpId = regMonitoringApprovalTmpId;
	}

	public RegMonitoringTmp getRegMonitoringTmp() {
		return regMonitoringTmp;
	}

	public void setRegMonitoringTmp(RegMonitoringTmp regMonitoringTmp) {
		this.regMonitoringTmp = regMonitoringTmp;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public Date getApprovalDate() {
		return approvalDate;
	}

	public void setApprovalDate(Date approvalDate) {
		this.approvalDate = approvalDate;
	}

	public String getApprovalNote() {
		return approvalNote;
	}

	public void setApprovalNote(String approvalNote) {
		this.approvalNote = approvalNote;
	}

	public Long getDelId() {
		return delId;
	}

	public void setDelId(Long delId) {
		this.delId = delId;
	}
}