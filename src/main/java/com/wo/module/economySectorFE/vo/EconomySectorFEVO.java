package com.wo.module.economySectorFE.vo;

import java.io.Serializable;

public class EconomySectorFEVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8052686892078183503L;

	private Long economySectorId;
	private String economySectorCode;
	private String economySectorName;
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
}
