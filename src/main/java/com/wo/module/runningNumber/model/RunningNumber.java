package com.wo.module.runningNumber.model;

import java.io.Serializable;

import com.wo.module.common.model.BaseEntity;

public class RunningNumber extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -220073881024260943L;

	private Long runningNumberId;

	private Integer runningNumberSeq;

	private String runningNumberNo;
	private String runningNumberReset;
	private String runningNumberType;

	public Long getRunningNumberId() {
		return runningNumberId;
	}

	public void setRunningNumberId(Long runningNumberId) {
		this.runningNumberId = runningNumberId;
	}

	public Integer getRunningNumberSeq() {
		return runningNumberSeq;
	}

	public void setRunningNumberSeq(Integer runningNumberSeq) {
		this.runningNumberSeq = runningNumberSeq;
	}

	public String getRunningNumberNo() {
		return runningNumberNo;
	}

	public void setRunningNumberNo(String runningNumberNo) {
		this.runningNumberNo = runningNumberNo;
	}

	public String getRunningNumberReset() {
		return runningNumberReset;
	}

	public void setRunningNumberReset(String runningNumberReset) {
		this.runningNumberReset = runningNumberReset;
	}

	public String getRunningNumberType() {
		return runningNumberType;
	}

	public void setRunningNumberType(String runningNumberType) {
		this.runningNumberType = runningNumberType;
	}

}
