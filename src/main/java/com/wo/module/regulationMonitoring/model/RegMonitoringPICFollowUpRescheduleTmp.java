package com.wo.module.regulationMonitoring.model;

import java.io.Serializable;
import java.util.Date;

import com.wo.module.common.model.BaseEntity;

public class RegMonitoringPICFollowUpRescheduleTmp extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 851426325642757071L;
	
	private Long regMonitoringPicFollowUpRescheduleTmpId;
	private RegMonitoringPICFollowUpTmp regMonitoringPicFollowUpTmp;
	
	private Date oldTargetDate;
	private Date newTargetDate;
	
	private String rescheduleReason;
	private Long delId;
	
	public Long getRegMonitoringPicFollowUpRescheduleTmpId() {
		return regMonitoringPicFollowUpRescheduleTmpId;
	}
	public void setRegMonitoringPicFollowUpRescheduleTmpId(Long regMonitoringPicFollowUpRescheduleTmpId) {
		this.regMonitoringPicFollowUpRescheduleTmpId = regMonitoringPicFollowUpRescheduleTmpId;
	}
	public RegMonitoringPICFollowUpTmp getRegMonitoringPicFollowUpTmp() {
		return regMonitoringPicFollowUpTmp;
	}
	public void setRegMonitoringPicFollowUpTmp(RegMonitoringPICFollowUpTmp regMonitoringPicFollowUpTmp) {
		this.regMonitoringPicFollowUpTmp = regMonitoringPicFollowUpTmp;
	}
	public Date getOldTargetDate() {
		return oldTargetDate;
	}
	public void setOldTargetDate(Date oldTargetDate) {
		this.oldTargetDate = oldTargetDate;
	}
	public Date getNewTargetDate() {
		return newTargetDate;
	}
	public void setNewTargetDate(Date newTargetDate) {
		this.newTargetDate = newTargetDate;
	}
	public String getRescheduleReason() {
		return rescheduleReason;
	}
	public void setRescheduleReason(String rescheduleReason) {
		this.rescheduleReason = rescheduleReason;
	}
	public Long getDelId() {
		return delId;
	}
	public void setDelId(Long delId) {
		this.delId = delId;
	}
}