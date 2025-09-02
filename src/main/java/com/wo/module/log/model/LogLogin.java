package com.wo.module.log.model;

import java.io.Serializable;
import java.sql.Timestamp;

import com.wo.module.user.model.User;

public class LogLogin implements Serializable {
	private static final long serialVersionUID = -7666128931063230931L;
	private Long loginId;
	private String username;
	private String sourceIp;
	private String divisionName;
	private Timestamp accessTime;
	private Integer retryAttempt;
	private User user;
	private Timestamp lastLogin;
	private Timestamp lastLogout;
	private boolean save;
	private String userCreatedBy;
	

	public LogLogin() {

	}

	public LogLogin(String username, String sourceIp, Timestamp accessTime, Integer retryAttempt) {
		this.username = username;
		this.sourceIp = sourceIp;
		this.accessTime = accessTime;
		this.retryAttempt = retryAttempt;
		save = true;
	}
	
	public String getUserCreatedBy() {
		return userCreatedBy;
	}

	public void setUserCreatedBy(String userCreatedBy) {
		this.userCreatedBy = userCreatedBy;
	}

	public String getDivisionName() {
		return divisionName;
	}

	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getSourceIp() {
		return sourceIp;
	}

	public void setSourceIp(String sourceIp) {
		this.sourceIp = sourceIp;
	}

	public Timestamp getAccessTime() {
		return accessTime;
	}

	public void setAccessTime(Timestamp accessTime) {
		this.accessTime = accessTime;
	}

	public Integer getRetryAttempt() {
		return retryAttempt;
	}

	public void setRetryAttempt(Integer retryAttempt) {
		this.retryAttempt = retryAttempt;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Timestamp getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Timestamp lastLogin) {
		this.lastLogin = lastLogin;
	}

	public Timestamp getLastLogout() {
		return lastLogout;
	}

	public void setLastLogout(Timestamp lastLogout) {
		this.lastLogout = lastLogout;
	}

	public Long getLoginId() {
		return loginId;
	}

	public void setLoginId(Long loginId) {
		this.loginId = loginId;
	}

	public boolean isSave() {
		return save;
	}

	public void setSave(boolean save) {
		this.save = save;
	}
}