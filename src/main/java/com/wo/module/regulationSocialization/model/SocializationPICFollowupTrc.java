package com.wo.module.regulationSocialization.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.user.model.User;

public class SocializationPICFollowupTrc extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = 3944154469574926625L;
	
	private long socializationPicFollowupId;
	private SocializationTrc socializationTrc;
	
	private User user1;
	private User user2;
	private User user3;
	
	private Date targetDate;
	private String rescheduleReason;
	private String notes;
	
	private ParameterDetail followupStatus;
	private User followupBy;
	private Date confirmationDate;
	private Date followupDate;
	private String followupNote;
	private ParameterDetail complianceStatus;
	private String complianceNote;
	private User complianceBy;
	private Date complianceDate;
	
	
	private Long delId;
	
	private Long divisionId;
	
	private String divisionName;
	
	private Integer sequence;
	
	private List<SocializationPICFollowupEmailTrc> socializationPICFollowupEmailTrcs;
	
	private List<SocializationPICFollowupAttachmentTrc> socializationPICFollowupAttachmentTrcs;

	public long getSocializationPicFollowupId() {
		return socializationPicFollowupId;
	}

	public void setSocializationPicFollowupId(long socializationPicFollowupId) {
		this.socializationPicFollowupId = socializationPicFollowupId;
	}

	/*
	 * public Division getDivision() { return division; }
	 * 
	 * public void setDivision(Division division) { this.division = division; }
	 */

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

	public Long getDelId() {
		return delId;
	}

	public void setDelId(Long delId) {
		this.delId = delId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Long getDivisionId() {
		return divisionId;
	}

	public void setDivisionId(Long divisionId) {
		this.divisionId = divisionId;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public String getDivisionName() {
		return divisionName;
	}

	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}

	public SocializationTrc getSocializationTrc() {
		return socializationTrc;
	}

	public void setSocializationTrc(SocializationTrc socializationTrc) {
		this.socializationTrc = socializationTrc;
	}

	public List<SocializationPICFollowupEmailTrc> getSocializationPICFollowupEmailTrcs() {
		return socializationPICFollowupEmailTrcs;
	}

	public void setSocializationPICFollowupEmailTrcs(
			List<SocializationPICFollowupEmailTrc> socializationPICFollowupEmailTrcs) {
		this.socializationPICFollowupEmailTrcs = socializationPICFollowupEmailTrcs;
	}

	public List<SocializationPICFollowupAttachmentTrc> getSocializationPICFollowupAttachmentTrcs() {
		return socializationPICFollowupAttachmentTrcs;
	}

	public void setSocializationPICFollowupAttachmentTrcs(
			List<SocializationPICFollowupAttachmentTrc> socializationPICFollowupAttachmentTrcs) {
		this.socializationPICFollowupAttachmentTrcs = socializationPICFollowupAttachmentTrcs;
	}

	public Date getFollowupDate() {
		return followupDate;
	}

	public void setFollowupDate(Date followupDate) {
		this.followupDate = followupDate;
	}

	public String getFollowupNote() {
		return followupNote;
	}

	public void setFollowupNote(String followupNote) {
		this.followupNote = followupNote;
	}

	public User getFollowupBy() {
		return followupBy;
	}

	public void setFollowupBy(User followupBy) {
		this.followupBy = followupBy;
	}

	public Date getConfirmationDate() {
		return confirmationDate;
	}

	public void setConfirmationDate(Date confirmationDate) {
		this.confirmationDate = confirmationDate;
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

	public void setFollowupStatus(ParameterDetail followupStatus) {
		this.followupStatus = followupStatus;
	}

	public void setComplianceStatus(ParameterDetail complianceStatus) {
		this.complianceStatus = complianceStatus;
	}

	public ParameterDetail getFollowupStatus() {
		return followupStatus;
	}

	public ParameterDetail getComplianceStatus() {
		return complianceStatus;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getRescheduleReason() {
		return rescheduleReason;
	}

	public void setRescheduleReason(String rescheduleReason) {
		this.rescheduleReason = rescheduleReason;
	}

	
	
	
}
