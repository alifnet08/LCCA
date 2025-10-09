package com.wo.module.country.vo;

import java.io.Serializable;

import com.wo.module.common.model.BaseEntity;

public class CountryVO extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 6395152164269025885L;

	private Long negaraId;
	private String negara;
	private String riskRating;
	
	public Long getNegaraId() {
		return negaraId;
	}
	public void setNegaraId(Long negaraId) {
		this.negaraId = negaraId;
	}
	public String getNegara() {
		return negara;
	}
	public void setNegara(String negara) {
		this.negara = negara;
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
	
	
}
