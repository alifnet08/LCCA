package com.wo.module.logActivity.model;

import java.io.Serializable;
import java.sql.Timestamp;
import com.wo.module.common.model.BaseEntity;
import java.util.Date;
import com.wo.module.common.model.BaseEntity;
import com.wo.module.externalRegulation.model.Regulation;
import com.wo.module.internalRegulation.model.InternalRegulation;
import com.wo.module.user.model.User;

public class LogActivity extends BaseEntity implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long logActivityId;
	private User user;
	private String activityType;
	private Timestamp activityDate;
	private String activityNote;
	
	//helper
	
	public Long getLogActivityId() {
		return logActivityId;
	}
	public void setLogActivityId(Long logActivityId) {
		this.logActivityId = logActivityId;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getActivityType() {
		return activityType;
	}
	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}
	public Timestamp getActivityDate() {
		return activityDate;
	}
	public void setActivityDate(Timestamp activityDate) {
		this.activityDate = activityDate;
	}
	public String getActivityNote() {
		return activityNote;
	}
	public void setActivityNote(String activityNote) {
		this.activityNote = activityNote;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
