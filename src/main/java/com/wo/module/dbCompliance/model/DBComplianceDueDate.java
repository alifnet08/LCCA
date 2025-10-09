package com.wo.module.dbCompliance.model;

import java.io.Serializable;
import java.util.Date;

import com.wo.module.common.model.BaseEntity;

public class DBComplianceDueDate extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 6395152164269025885L;

	private Long dbComplianceDueDateId;
	private DBCompliance dbCompliance;
	private Long dueDay;
	private Long dueDateOnly;
	private Date dueDate;
	private Date targetDate;
	private Integer sequence;
	
	public Long getDbComplianceDueDateId() {
		return dbComplianceDueDateId;
	}
	public void setDbComplianceDueDateId(Long dbComplianceDueDateId) {
		this.dbComplianceDueDateId = dbComplianceDueDateId;
	}
	public DBCompliance getDbCompliance() {
		return dbCompliance;
	}
	public void setDbCompliance(DBCompliance dbCompliance) {
		this.dbCompliance = dbCompliance;
	}
	public Long getDueDay() {
		return dueDay;
	}
	public void setDueDay(Long dueDay) {
		this.dueDay = dueDay;
	}
	public Long getDueDateOnly() {
		return dueDateOnly;
	}
	public void setDueDateOnly(Long dueDateOnly) {
		this.dueDateOnly = dueDateOnly;
	}
	public Date getDueDate() {
		return dueDate;
	}
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	public Date getTargetDate() {
		return targetDate;
	}
	public void setTargetDate(Date targetDate) {
		this.targetDate = targetDate;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Integer getSequence() {
		return sequence;
	}
	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}
	
	
	
	
}
