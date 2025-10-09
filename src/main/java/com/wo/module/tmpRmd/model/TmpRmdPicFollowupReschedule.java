package com.wo.module.tmpRmd.model;

import java.io.Serializable;
import java.util.Date;

import com.wo.module.common.model.BaseEntity;

public class TmpRmdPicFollowupReschedule extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 3019374791490640764L;

	private Long rmdPicFollowupRescheduleId;
	private TmpRmd tmpRmd;

	private Date oldTargetDate;
	private Date newTargetDate;

	public Long getRmdPicFollowupRescheduleId() {
		return rmdPicFollowupRescheduleId;
	}

	public void setRmdPicFollowupRescheduleId(Long rmdPicFollowupRescheduleId) {
		this.rmdPicFollowupRescheduleId = rmdPicFollowupRescheduleId;
	}

	public TmpRmd getTmpRmd() {
		return tmpRmd;
	}

	public void setTmpRmd(TmpRmd tmpRmd) {
		this.tmpRmd = tmpRmd;
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