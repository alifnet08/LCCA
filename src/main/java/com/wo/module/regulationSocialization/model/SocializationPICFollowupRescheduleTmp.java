package com.wo.module.regulationSocialization.model;

import java.io.Serializable;
import java.util.Date;

import com.wo.module.common.model.BaseEntity;

public class SocializationPICFollowupRescheduleTmp extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 6395152164269025885L;
	private long socializationPicFollowupRescheduleId;
	private SocializationPICFollowupTmp socializationPICFollowupTmp;
	
	
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

	public SocializationPICFollowupTmp getSocializationPICFollowupTmp() {
		return socializationPICFollowupTmp;
	}

	public void setSocializationPICFollowupTmp(SocializationPICFollowupTmp socializationPICFollowupTmp) {
		this.socializationPICFollowupTmp = socializationPICFollowupTmp;
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
