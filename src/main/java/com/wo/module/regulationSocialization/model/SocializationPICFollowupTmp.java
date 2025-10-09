package com.wo.module.regulationSocialization.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.user.model.User;

public class SocializationPICFollowupTmp extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 6395152164269025885L;
	private Long socializationPicFollowupId;
	private SocializationTmp socialization;
	//private Division division;
	
	private User user1;
	private User user2;
	private User user3;
	
	private Date targetDate;
	private String rescheduleReason;
	private String notes;
	
	private Date oldTargetDate;
	
	private Long delId;
	
	private Long divisionId;
	
	private String divisionName;
	
	private Integer sequence;
	
	private Boolean isEditableTemp;
	
	private List<SocializationPICFollowupEmailTmp> socializationPICFollowupEmailTmps;
	
	private List<SocializationPICFollowupAttachmentTrc> socializationPICFollowupAttachmentTrcs;

	public SocializationTmp getSocialization() {
		return socialization;
	}

	public void setSocialization(SocializationTmp socialization) {
		this.socialization = socialization;
	}

	public Long getSocializationPicFollowupId() {
		return socializationPicFollowupId;
	}

	public void setSocializationPicFollowupId(Long socializationPicFollowupId) {
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

	public List<SocializationPICFollowupEmailTmp> getSocializationPICFollowupEmailTmps() {
		return socializationPICFollowupEmailTmps;
	}

	public void setSocializationPICFollowupEmailTmps(
			List<SocializationPICFollowupEmailTmp> socializationPICFollowupEmailTmps) {
		this.socializationPICFollowupEmailTmps = socializationPICFollowupEmailTmps;
	}

	public List<SocializationPICFollowupAttachmentTrc> getSocializationPICFollowupAttachmentTrcs() {
		return socializationPICFollowupAttachmentTrcs;
	}

	public void setSocializationPICFollowupAttachmentTrcs(
			List<SocializationPICFollowupAttachmentTrc> socializationPICFollowupAttachmentTrcs) {
		this.socializationPICFollowupAttachmentTrcs = socializationPICFollowupAttachmentTrcs;
	}

	public Date getOldTargetDate() {
		return oldTargetDate;
	}

	public void setOldTargetDate(Date oldTargetDate) {
		this.oldTargetDate = oldTargetDate;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Boolean getIsEditableTemp() {
		return isEditableTemp;
	}

	public void setIsEditableTemp(Boolean isEditableTemp) {
		this.isEditableTemp = isEditableTemp;
	}

	public String getRescheduleReason() {
		return rescheduleReason;
	}

	public void setRescheduleReason(String rescheduleReason) {
		this.rescheduleReason = rescheduleReason;
	}

	
	
	
	

}
