package com.wo.module.tmpFine.model;

import java.io.Serializable;
import java.util.Date;

import com.wo.module.common.model.BaseEntity;

public class TmpFinePicFollowupReschedule extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -6391019659390961531L;
	private Long finePicFollowupRescheduleId;
	private TmpFinePicFollowup tmpFinePicFollowup;
	private Date oldTargetDate;
	private Date newTargetDate;
	private String rescheduleReason;

	public TmpFinePicFollowupReschedule() {
		super();
	}

	public Long getFinePicFollowupRescheduleId() {
		return finePicFollowupRescheduleId;
	}

	public void setFinePicFollowupRescheduleId(Long finePicFollowupRescheduleId) {
		this.finePicFollowupRescheduleId = finePicFollowupRescheduleId;
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

	public TmpFinePicFollowup getTmpFinePicFollowup() {
		return tmpFinePicFollowup;
	}

	public void setTmpFinePicFollowup(TmpFinePicFollowup tmpFinePicFollowup) {
		this.tmpFinePicFollowup = tmpFinePicFollowup;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getRescheduleReason() {
		return rescheduleReason;
	}

	public void setRescheduleReason(String rescheduleReason) {
		this.rescheduleReason = rescheduleReason;
	}

	

}
