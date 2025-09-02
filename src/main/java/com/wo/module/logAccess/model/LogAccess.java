package com.wo.module.logAccess.model;

import java.io.Serializable;
import java.sql.Timestamp;

import com.wo.module.user.model.User;

public class LogAccess  implements Serializable {

	private static final long serialVersionUID = 6395152164269025885L;

	private Long logAccessId;
	private User user;
	private String sourceIp;
	private Timestamp accessTime;
	private String accessAction;
	private Long accessId;
	private String divisionName;
	
	private String userName;
	
	public Long getLogAccessId() {
		return logAccessId;
	}
	public void setLogAccessId(Long logAccessId) {
		this.logAccessId = logAccessId;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
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
	public String getAccessAction() {
		return accessAction;
	}
	public void setAccessAction(String accessAction) {
		this.accessAction = accessAction;
	}
	public Long getAccessId() {
		return accessId;
	}
	public void setAccessId(Long accessId) {
		this.accessId = accessId;
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
	public String getDivisionName() {
		return divisionName;
	}
	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}
	
	
	
	
	
	
	
	
}
