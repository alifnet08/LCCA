package com.wo.module.regulationMonitoring.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.user.model.User;

public class RegMonitoringPICFollowUpTmp extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -4189968609976612564L;
	
	private Long regMonitoringPicFollowUpTmpId;
	private RegMonitoringTmp regMonitoringTmp;
	
	private Long divisionId;
	private String divisionName;
	
	private User user1;
	private User user2;
	private User user3;
	
	private Date targetDate;
	private Date oldTargetDate;
	
	private String rescheduleReason;
	private String notes;
	private Long delId;
	
	private Integer sequence;
	private Boolean isEditableTemp;
	
	private List<RegMonitoringPICFollowUpEmailTmp> regMonitoringPICFollowUpEmailTmps;
	private List<RegMonitoringPICFollowUpAttachmentTrc> regMonitoringPICFollowUpAttachmentTrcs;
	
	public List<RegMonitoringPICFollowUpEmailTmp> getRegMonitoringPICFollowUpEmailTmps() {
		return regMonitoringPICFollowUpEmailTmps;
	}
	public void setRegMonitoringPICFollowUpEmailTmps(
			List<RegMonitoringPICFollowUpEmailTmp> regMonitoringPICFollowUpEmailTmps) {
		this.regMonitoringPICFollowUpEmailTmps = regMonitoringPICFollowUpEmailTmps;
	}
	public List<RegMonitoringPICFollowUpAttachmentTrc> getRegMonitoringPICFollowUpAttachmentTrcs() {
		return regMonitoringPICFollowUpAttachmentTrcs;
	}
	public void setRegMonitoringPICFollowUpAttachmentTrcs(
			List<RegMonitoringPICFollowUpAttachmentTrc> regMonitoringPICFollowUpAttachmentTrcs) {
		this.regMonitoringPICFollowUpAttachmentTrcs = regMonitoringPICFollowUpAttachmentTrcs;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Long getRegMonitoringPicFollowUpTmpId() {
		return regMonitoringPicFollowUpTmpId;
	}
	public void setRegMonitoringPicFollowUpTmpId(Long regMonitoringPicFollowUpTmpId) {
		this.regMonitoringPicFollowUpTmpId = regMonitoringPicFollowUpTmpId;
	}
	public RegMonitoringTmp getRegMonitoringTmp() {
		return regMonitoringTmp;
	}
	public void setRegMonitoringTmp(RegMonitoringTmp regMonitoringTmp) {
		this.regMonitoringTmp = regMonitoringTmp;
	}
	public Long getDivisionId() {
		return divisionId;
	}
	public void setDivisionId(Long divisionId) {
		this.divisionId = divisionId;
	}
	public String getDivisionName() {
		return divisionName;
	}
	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}
	public User getUser1() {
		return user1;
	}
	public void setUser1(User user1) {
		this.user1 = user1;
	}
	public User getUser2() {
		return user2;
	}
	public void setUser2(User user2) {
		this.user2 = user2;
	}
	public User getUser3() {
		return user3;
	}
	public void setUser3(User user3) {
		this.user3 = user3;
	}
	public Date getTargetDate() {
		return targetDate;
	}
	public void setTargetDate(Date targetDate) {
		this.targetDate = targetDate;
	}
	public Date getOldTargetDate() {
		return oldTargetDate;
	}
	public void setOldTargetDate(Date oldTargetDate) {
		this.oldTargetDate = oldTargetDate;
	}
	public String getRescheduleReason() {
		return rescheduleReason;
	}
	public void setRescheduleReason(String rescheduleReason) {
		this.rescheduleReason = rescheduleReason;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public Long getDelId() {
		return delId;
	}
	public void setDelId(Long delId) {
		this.delId = delId;
	}
	public Integer getSequence() {
		return sequence;
	}
	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}
	public Boolean getIsEditableTemp() {
		return isEditableTemp;
	}
	public void setIsEditableTemp(Boolean isEditableTemp) {
		this.isEditableTemp = isEditableTemp;
	}
}