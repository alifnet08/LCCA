package com.wo.module.regulationMonitoring.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import com.wo.module.common.model.BaseEntity;

public class RegMonitoringPICFollowUpEmailTrc extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = -1345257794288871048L;
	
	private Long regMonitoringPicFollowUpEmailTrcId;
	private RegMonitoringPICFollowUpTrc regMonitoringPicFollowUpTrc;
	
	private Date emailDate;
	
	private String slaType;
	private Long sla;
	
	private String emailStatus;
	private String emailSubject;
	private String emailContent;
	
	private Integer resendCount;
	
	private Long delId;

	public Long getRegMonitoringPicFollowUpEmailTrcId() {
		return regMonitoringPicFollowUpEmailTrcId;
	}

	public void setRegMonitoringPicFollowUpEmailTrcId(Long regMonitoringPicFollowUpEmailTrcId) {
		this.regMonitoringPicFollowUpEmailTrcId = regMonitoringPicFollowUpEmailTrcId;
	}

	public RegMonitoringPICFollowUpTrc getRegMonitoringPicFollowUpTrc() {
		return regMonitoringPicFollowUpTrc;
	}

	public void setRegMonitoringPicFollowUpTrc(RegMonitoringPICFollowUpTrc regMonitoringPicFollowUpTrc) {
		this.regMonitoringPicFollowUpTrc = regMonitoringPicFollowUpTrc;
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

	public Long getDelId() {
		return delId;
	}

	public void setDelId(Long delId) {
		this.delId = delId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}