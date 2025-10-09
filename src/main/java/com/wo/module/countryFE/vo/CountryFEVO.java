package com.wo.module.countryFE.vo;

import java.io.Serializable;

public class CountryFEVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8052686892078183503L;

	private Long countryId;
	private String countryName;
	private String riskRating;
	
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
}
