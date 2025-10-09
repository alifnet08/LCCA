package com.wo.module.tmpCorrespondence.model;

import java.io.Serializable;
import java.util.Date;

import com.wo.module.common.model.BaseEntity;

public class TmpCorrespondencePicFollowupEmail extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 6140880530217262281L;
	private Long correspondencePicFollowupEmailId;
	private TmpCorrespondence tmpCorrespondence;
	private Date emailDate;

	public TmpCorrespondencePicFollowupEmail() {
		super();
	}

	public Long getCorrespondencePicFollowupEmailId() {
		return correspondencePicFollowupEmailId;
	}

	public void setCorrespondencePicFollowupEmailId(Long correspondencePicFollowupEmailId) {
		this.correspondencePicFollowupEmailId = correspondencePicFollowupEmailId;
	}

	public Date getEmailDate() {
		return emailDate;
	}

	public void setEmailDate(Date emailDate) {
		this.emailDate = emailDate;
	}

	public TmpCorrespondence getTmpCorrespondence() {
		return tmpCorrespondence;
	}

	public void setTmpCorrespondence(TmpCorrespondence tmpCorrespondence) {
		this.tmpCorrespondence = tmpCorrespondence;
	}

}
