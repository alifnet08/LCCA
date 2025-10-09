package com.wo.module.regulationMonitoring.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.user.model.User;

public class RegMonitoringPICFollowUpTrc extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -4189968609976612564L;
	
	private Long regMonitoringPicFollowUpTrcId;
	private RegMonitoringTrc regMonitoringTrc;
	
	private Long divisionId;
	private String divisionName;
	
	private User user1;
	private User user2;
	private User user3;
	
	private Date targetDate;
	private Date oldTargetDate;
	
	private String rescheduleReason;
	private String notes;
	
	private ParameterDetail followUpStatus;
	private User followUpBy;
	
	private Date confirmationDate;
	private Date followUpDate;
	private String followUpNote;
	
	private ParameterDetail complianceStatus;
	private String complianceNote;
	private User complianceBy;
	private Date complianceDate;
	
	private Long delId;
	
	private Integer sequence;
	private Boolean isEditableTemp;
	
	private List<RegMonitoringPICFollowUpEmailTrc> regMonitoringPICFollowUpEmailTrcs;
	private List<RegMonitoringPICFollowUpAttachmentTrc> regMonitoringPICFollowUpAttachmentTrcs;
	
	
	public List<RegMonitoringPICFollowUpEmailTrc> getRegMonitoringPICFollowUpEmailTrcs() {
		return regMonitoringPICFollowUpEmailTrcs;
	}
	public void setRegMonitoringPICFollowUpEmailTrcs(
			List<RegMonitoringPICFollowUpEmailTrc> regMonitoringPICFollowUpEmailTrcs) {
		this.regMonitoringPICFollowUpEmailTrcs = regMonitoringPICFollowUpEmailTrcs;
	}
	public List<RegMonitoringPICFollowUpAttachmentTrc> getRegMonitoringPICFollowUpAttachmentTrcs() {
		return regMonitoringPICFollowUpAttachmentTrcs;
	}
	public void setRegMonitoringPICFollowUpAttachmentTrcs(
			List<RegMonitoringPICFollowUpAttachmentTrc> regMonitoringPICFollowUpAttachmentTrcs) {
		this.regMonitoringPICFollowUpAttachmentTrcs = regMonitoringPICFollowUpAttachmentTrcs;
	}
	public Long getRegMonitoringPicFollowUpTrcId() {
		return regMonitoringPicFollowUpTrcId;
	}
	public void setRegMonitoringPicFollowUpTrcId(Long regMonitoringPicFollowUpTrcId) {
		this.regMonitoringPicFollowUpTrcId = regMonitoringPicFollowUpTrcId;
	}
	public RegMonitoringTrc getRegMonitoringTrc() {
		return regMonitoringTrc;
	}
	public void setRegMonitoringTrc(RegMonitoringTrc regMonitoringTrc) {
		this.regMonitoringTrc = regMonitoringTrc;
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
	public ParameterDetail getFollowUpStatus() {
		return followUpStatus;
	}
	public void setFollowUpStatus(ParameterDetail followUpStatus) {
		this.followUpStatus = followUpStatus;
	}
	public User getFollowUpBy() {
		return followUpBy;
	}
	public void setFollowUpBy(User followUpBy) {
		this.followUpBy = followUpBy;
	}
	public Date getConfirmationDate() {
		return confirmationDate;
	}
	public void setConfirmationDate(Date confirmationDate) {
		this.confirmationDate = confirmationDate;
	}
	public Date getFollowUpDate() {
		return followUpDate;
	}
	public void setFollowUpDate(Date followUpDate) {
		this.followUpDate = followUpDate;
	}
	public String getFollowUpNote() {
		return followUpNote;
	}
	public void setFollowUpNote(String followUpNote) {
		this.followUpNote = followUpNote;
	}
	public ParameterDetail getComplianceStatus() {
		return complianceStatus;
	}
	public void setComplianceStatus(ParameterDetail complianceStatus) {
		this.complianceStatus = complianceStatus;
	}
	public String getComplianceNote() {
		return complianceNote;
	}
	public void setComplianceNote(String complianceNote) {
		this.complianceNote = complianceNote;
	}
	public User getComplianceBy() {
		return complianceBy;
	}
	public void setComplianceBy(User complianceBy) {
		this.complianceBy = complianceBy;
	}
	public Date getComplianceDate() {
		return complianceDate;
	}
	public void setComplianceDate(Date complianceDate) {
		this.complianceDate = complianceDate;
	}
}