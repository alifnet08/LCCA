package com.wo.module.report.reportRegulationInternalHistory.vo;

import java.io.Serializable;
import java.util.Date;


import com.wo.module.user.model.User;

public class ReportRegulationInternalHistoryVo implements Serializable{

	private static final long serialVersionUID = -7047034826642723536L;
	
	
	private Long logActivityId;
	private User user;
	private String activityType;
	private Date activityDate;
	private String activityNote;
	private String createdBy;
	private Date createdDate;
	private String lastUpdateBy;
	private Date lastUpdateDate;
	private String enabledFlag;
	private String delId;
	
	// helper
		private String createdDateStr;
		private String lastUpdateDateStr;
	
	
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

	public String getActivityNote() {
		return activityNote;
	}
	public void setActivityNote(String activityNote) {
		this.activityNote = activityNote;
	}

	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public void setActivityDate(Date activityDate) {
		this.activityDate = activityDate;
	}
	public Date getActivityDate() {
		return activityDate;
	}
	
	public String getLastUpdateBy() {
		return lastUpdateBy;
	}
	public void setLastUpdateBy(String lastUpdateBy) {
		this.lastUpdateBy = lastUpdateBy;
	}
	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}
	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	public String getEnabledFlag() {
		return enabledFlag;
	}
	public void setEnabledFlag(String enabledFlag) {
		this.enabledFlag = enabledFlag;
	}
	public String getDelId() {
		return delId;
	}
	public void setDelId(String delId) {
		this.delId = delId;
	}
	public String getCreatedDateStr() {
		return createdDateStr;
	}
	public void setCreatedDateStr(String createdDateStr) {
		this.createdDateStr = createdDateStr;
	}
	public String getLastUpdateDateStr() {
		return lastUpdateDateStr;
	}
	public void setLastUpdateDateStr(String lastUpdateDateStr) {
		this.lastUpdateDateStr = lastUpdateDateStr;
	}
	
	



}
