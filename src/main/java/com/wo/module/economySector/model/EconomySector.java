package com.wo.module.economySector.model;

import java.io.Serializable;
import java.sql.Timestamp;

import com.wo.module.common.model.BaseEntity;

public class EconomySector extends BaseEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6600060712854372640L;
	
	private Long economySectorId;
	private String economySectorCode;
	private String economySectorName;
	private String riskRating;
	
	private String fileName;
	private Timestamp uploadDate;
	
	public Long getEconomySectorId() {
		return economySectorId;
	}
	public void setEconomySectorId(Long economySectorId) {
		this.economySectorId = economySectorId;
	}
	public String getEconomySectorCode() {
		return economySectorCode;
	}
	public void setEconomySectorCode(String economySectorCode) {
		this.economySectorCode = economySectorCode;
	}
	public String getEconomySectorName() {
		return economySectorName;
	}
	public void setEconomySectorName(String economySectorName) {
		this.economySectorName = economySectorName;
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
