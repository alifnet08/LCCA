package com.wo.module.division.model;

import java.io.Serializable;
import java.sql.Timestamp;

import com.wo.module.common.model.BaseEntity;

public class Division extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 6395152164269025885L;

	private Long divisionId;
	
	private String divisionName;
	
	protected Timestamp creationDate;
	
	protected String createdBy;
	
	public Division() {
		super();
	}
	
	public Division(Long divisionId, String divisionName, Timestamp creationDate, String createdBy) {
		this.divisionId = divisionId;
		this.divisionName = divisionName;
		this.creationDate = creationDate;
		this.createdBy = createdBy;
	}
	
	public Long getDivisionId() {
		return divisionId;
	}
	public void setDivisionId(Long divisionId) {
		this.divisionId = divisionId;
	}
	public String getDivisionName() {
		return divisionName;
	}
	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}
	public Timestamp getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
