package com.wo.module.tmpCorrespondence.model;

import java.io.Serializable;
import java.util.Date;

import com.wo.module.common.model.BaseEntity;

public class TmpCorrespondencePicFollowupReschedule extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -6391019659390961531L;
	private Long correspondencePicFollowupRescheduleId;
	private TmpCorrespondence tmpCorrespondence;
	private Date oldTargetDate;
	private Date newTargetDate;

	public TmpCorrespondencePicFollowupReschedule() {
		super();
	}

	public Long getCorrespondencePicFollowupRescheduleId() {
		return correspondencePicFollowupRescheduleId;
	}

	public void setCorrespondencePicFollowupRescheduleId(Long correspondencePicFollowupRescheduleId) {
		this.correspondencePicFollowupRescheduleId = correspondencePicFollowupRescheduleId;
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

	public TmpCorrespondence getTmpCorrespondence() {
		return tmpCorrespondence;
	}

	public void setTmpCorrespondence(TmpCorrespondence tmpCorrespondence) {
		this.tmpCorrespondence = tmpCorrespondence;
	}

}
