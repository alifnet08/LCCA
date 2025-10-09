package com.wo.module.regulationMonitoring.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import com.wo.module.common.model.BaseEntity;

public class RegMonitoringPICFollowUpEmailTmp extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 6562310514135688592L;
	
	private Long regMonitoringPicFollowUpEmailTmpId;
	private RegMonitoringPICFollowUpTmp regMonitoringPicFollowUpTmp;
	
	private Date emailDate;
	
	private String slaType;
	private Long sla;
	
	private Long delId;
	
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

	public Long getDelId() {
		return delId;
	}

	public void setDelId(Long delId) {
		this.delId = delId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Long getRegMonitoringPicFollowUpEmailTmpId() {
		return regMonitoringPicFollowUpEmailTmpId;
	}

	public void setRegMonitoringPicFollowUpEmailTmpId(Long regMonitoringPicFollowUpEmailTmpId) {
		this.regMonitoringPicFollowUpEmailTmpId = regMonitoringPicFollowUpEmailTmpId;
	}

	public RegMonitoringPICFollowUpTmp getRegMonitoringPicFollowUpTmp() {
		return regMonitoringPicFollowUpTmp;
	}

	public void setRegMonitoringPicFollowUpTmp(RegMonitoringPICFollowUpTmp regMonitoringPicFollowUpTmp) {
		this.regMonitoringPicFollowUpTmp = regMonitoringPicFollowUpTmp;
	}
}