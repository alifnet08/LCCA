package com.wo.module.trcLock.model;

import java.io.Serializable;
import java.sql.Timestamp;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.user.model.User;

public class TrcLock extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -2025144397314994566L;
	
	private Long lockId;
	private String lockModule;
	private User user;
	private Timestamp startDate;
	private Timestamp endDate;
	
	private String userName;
	private String startDateStr;
	private String userNik;
	
	public Long getLockId() {
		return lockId;
	}
	public void setLockId(Long lockId) {
		this.lockId = lockId;
	}
	public String getLockModule() {
		return lockModule;
	}
	public void setLockModule(String lockModule) {
		this.lockModule = lockModule;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Timestamp getStartDate() {
		return startDate;
	}
	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}
	public Timestamp getEndDate() {
		return endDate;
	}
	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getStartDateStr() {
		return startDateStr;
	}
	public void setStartDateStr(String startDateStr) {
		this.startDateStr = startDateStr;
	}
	public String getUserNik() {
		return userNik;
	}
	public void setUserNik(String userNik) {
		this.userNik = userNik;
	}
}
