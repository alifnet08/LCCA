package com.wo.module.regulationMonitoring.model;

import java.io.Serializable;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.user.model.User;

public class RegMonitoringPICComplianceTmp extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 2099734838070944641L;
	
	private Long regMonitoringPicComplianceTmpId;
	private RegMonitoringTmp regMonitoringTmp;
	
	private User user;
	private Long delId;
	
	private Integer sequence;
	
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
	public Long getDelId() {
		return delId;
	}
	public void setDelId(Long delId) {
		this.delId = delId;
	}
	public Long getRegMonitoringPicComplianceTmpId() {
		return regMonitoringPicComplianceTmpId;
	}
	public void setRegMonitoringPicComplianceTmpId(Long regMonitoringPicComplianceTmpId) {
		this.regMonitoringPicComplianceTmpId = regMonitoringPicComplianceTmpId;
	}
	public Integer getSequence() {
		return sequence;
	}
	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}
}