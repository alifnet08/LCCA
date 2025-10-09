package com.wo.module.trcRmd.model;

import java.io.Serializable;
import java.util.Date;

import com.wo.module.common.model.BaseEntity;

public class TrcRmdDueDate extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 3019374791490640764L;

	private Long rmdDueDateId;
	private TrcRmd trcRmd;

	private Long dueDay;
	private Long dueDateDateOnly;
	private Date dueDate;
	private Date targetDate;

	private String dueDateStr;

	private int sequence;

	public Long getRmdDueDateId() {
		return rmdDueDateId;
	}

	public void setRmdDueDateId(Long rmdDueDateId) {
		this.rmdDueDateId = rmdDueDateId;
	}

	public TrcRmd getTrcRmd() {
		return trcRmd;
	}

	public void setTrcRmd(TrcRmd trcRmd) {
		this.trcRmd = trcRmd;
	}

	public Long getDueDay() {
		return dueDay;
	}

	public void setDueDay(Long dueDay) {
		this.dueDay = dueDay;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public String getDueDateStr() {
		return dueDateStr;
	}

	public void setDueDateStr(String dueDateStr) {
		this.dueDateStr = dueDateStr;
	}

	public Long getDueDateDateOnly() {
		return dueDateDateOnly;
	}

	public void setDueDateDateOnly(Long dueDateDateOnly) {
		this.dueDateDateOnly = dueDateDateOnly;
	}

	public Date getTargetDate() {
		return targetDate;
	}

	public void setTargetDate(Date targetDate) {
		this.targetDate = targetDate;
	}

}