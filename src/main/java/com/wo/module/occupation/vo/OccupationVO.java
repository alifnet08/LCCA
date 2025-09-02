package com.wo.module.occupation.vo;

import java.io.Serializable;

import com.wo.module.common.model.BaseEntity;

public class OccupationVO extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 6395152164269025885L;

	private Long occupationId;
	private String occupation;
	private String riskRating;
	
	public Long getOccupationId() {
		return occupationId;
	}
	public void setOccupationId(Long occupationId) {
		this.occupationId = occupationId;
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
	public String getOccupation() {
		return occupation;
	}
	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}
	
	
	
}
