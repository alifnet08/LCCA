package com.wo.module.trcCorrespondence.model;

import java.io.Serializable;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.user.model.User;

public class TrcCorrespondencePicFollowupAttendance extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1802960745917916054L;
	
	private Long correspondencePicFollowupAttedanceId;
	private TrcCorrespondence trcCorrespondence;
	private User userId;
	
	//helper
	private String userNIK;
	private String userName;
	private String userEmail;
	
	private int sequence;
	
	public TrcCorrespondencePicFollowupAttendance() {
		super();
	}

	public User getUserId() {
		return userId;
	}

	public void setUserId(User userId) {
		this.userId = userId;
	}

	public TrcCorrespondence getTrcCorrespondence() {
		return trcCorrespondence;
	}

	public void setTrcCorrespondence(TrcCorrespondence trcCorrespondence) {
		this.trcCorrespondence = trcCorrespondence;
	}

	public Long getCorrespondencePicFollowupAttedanceId() {
		return correspondencePicFollowupAttedanceId;
	}

	public void setCorrespondencePicFollowupAttedanceId(Long correspondencePicFollowupAttedanceId) {
		this.correspondencePicFollowupAttedanceId = correspondencePicFollowupAttedanceId;
	}

	public String getUserNIK() {
		return userNIK;
	}

	public void setUserNIK(String userNIK) {
		this.userNIK = userNIK;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
	
}

