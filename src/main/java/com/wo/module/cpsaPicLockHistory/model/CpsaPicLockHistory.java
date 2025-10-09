package com.wo.module.cpsaPicLockHistory.model;

import java.io.Serializable;

import com.wo.module.common.model.BaseEntity;

public class CpsaPicLockHistory extends BaseEntity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7372037255144952699L;
	
	private Long cpsaPicLockHistoryId;
	private Long cpsaPicId;
	private Long cpsaId;
	private String lockStatus; 
	
	public Long getCpsaPicLockHistoryId() {
		return cpsaPicLockHistoryId;
	}
	public void setCpsaPicLockHistoryId(Long cpsaPicLockHistoryId) {
		this.cpsaPicLockHistoryId = cpsaPicLockHistoryId;
	}
	public Long getCpsaPicId() {
		return cpsaPicId;
	}
	public void setCpsaPicId(Long cpsaPicId) {
		this.cpsaPicId = cpsaPicId;
	}
	public Long getCpsaId() {
		return cpsaId;
	}
	public void setCpsaId(Long cpsaId) {
		this.cpsaId = cpsaId;
	}
	public String getLockStatus() {
		return lockStatus;
	}
	public void setLockStatus(String lockStatus) {
		this.lockStatus = lockStatus;
	}
	
	
}
