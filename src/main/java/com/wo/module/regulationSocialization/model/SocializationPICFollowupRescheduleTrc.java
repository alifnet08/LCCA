package com.wo.module.regulationSocialization.model;

import java.io.Serializable;
import java.util.Date;

import com.wo.module.common.model.BaseEntity;

public class SocializationPICFollowupRescheduleTrc extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = -5820440623404519761L;
	
	private long socializationPicFollowupRescheduleId;
	private SocializationPICFollowupTrc socializationPICFollowupTrc;
	
	
	private Date oldTargetDate;
	private Date newTargetDate;
	
	private Long delId;

	public Long getDelId() {
		return delId;
	}

	public void setDelId(Long delId) {
		this.delId = delId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public long getSocializationPicFollowupRescheduleId() {
		return socializationPicFollowupRescheduleId;
	}

	public void setSocializationPicFollowupRescheduleId(long socializationPicFollowupRescheduleId) {
		this.socializationPicFollowupRescheduleId = socializationPicFollowupRescheduleId;
	}
	
	public SocializationPICFollowupTrc getSocializationPICFollowupTrc() {
		return socializationPICFollowupTrc;
	}

	public void setSocializationPICFollowupTrc(SocializationPICFollowupTrc socializationPICFollowupTrc) {
		this.socializationPICFollowupTrc = socializationPICFollowupTrc;
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

}
