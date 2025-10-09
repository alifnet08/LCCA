package com.wo.module.country.model;

import java.io.Serializable;
import java.sql.Timestamp;

import com.wo.module.common.model.BaseEntity;

public class Country extends BaseEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6600060712854372640L;
	
	private Long countryId;
	private String countryName;
	private String riskRating;
	private String fileName;
	private Timestamp uploadDate;
	
	public Long getCountryId() {
		return countryId;
	}
	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public String getRiskRating() {
		return riskRating;
	}
	public void setRiskRating(String riskRating) {
		this.riskRating = riskRating;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public Timestamp getUploadDate() {
		return uploadDate;
	}
	public void setUploadDate(Timestamp uploadDate) {
		this.uploadDate = uploadDate;
	}
}
