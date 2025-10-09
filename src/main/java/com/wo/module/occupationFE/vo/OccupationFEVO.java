package com.wo.module.occupationFE.vo;

import java.io.Serializable;

public class OccupationFEVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8052686892078183503L;

	private Long occupationId;
	private String occupationName;
	private String riskRating;
	
	public Long getOccupationId() {
		return occupationId;
	}
	public void setOccupationId(Long occupationId) {
		this.occupationId = occupationId;
	}
	public String getOccupationName() {
		return occupationName;
	}
	public void setOccupationName(String occupationName) {
		this.occupationName = occupationName;
	}
	public String getRiskRating() {
		return riskRating;
	}
	public void setRiskRating(String riskRating) {
		this.riskRating = riskRating;
	}
}
