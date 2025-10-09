package com.wo.module.regulationMonitoring.model;

import java.io.Serializable;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.user.model.User;

public class RegMonitoringPICComplianceTrc extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -8321249438610865373L;
	
	private Long regMonitoringPicComplianceTrcId;
	private RegMonitoringTrc regMonitoringTrc;
	
	private User user;
	private Long delId;
	
	private Integer sequence;
	
	public Long getRegMonitoringPicComplianceTrcId() {
		return regMonitoringPicComplianceTrcId;
	}
	public void setRegMonitoringPicComplianceTrcId(Long regMonitoringPicComplianceTrcId) {
		this.regMonitoringPicComplianceTrcId = regMonitoringPicComplianceTrcId;
	}
	public RegMonitoringTrc getRegMonitoringTrc() {
		return regMonitoringTrc;
	}
	public void setRegMonitoringTrc(RegMonitoringTrc regMonitoringTrc) {
		this.regMonitoringTrc = regMonitoringTrc;
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
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Integer getSequence() {
		return sequence;
	}
	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}
}