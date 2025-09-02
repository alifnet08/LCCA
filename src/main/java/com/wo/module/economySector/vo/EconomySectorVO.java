package com.wo.module.economySector.vo;

import java.io.Serializable;

import com.wo.module.common.model.BaseEntity;

public class EconomySectorVO extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 6395152164269025885L;

	private Long economySectorId;
	private String economySectorCode;
	private String economySector;
	private String riskRating;
	
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
	public String getRiskRating() {
		return riskRating;
	}
	public void setRiskRating(String riskRating) {
		this.riskRating = riskRating;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getEconomySector() {
		return economySector;
	}
	public void setEconomySector(String economySector) {
		this.economySector = economySector;
	}
	
	
}
