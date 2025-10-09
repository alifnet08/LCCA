package com.wo.module.regulationSocialization.model;

import java.io.Serializable;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.externalRegulation.model.Regulation;

public class SocializationRegulationTmp extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 6395152164269025885L;
	private Long socializationRegulationId;
	private SocializationTmp socialization;
	private Regulation regulation;
	private String primaryFlag;
	
	private Long delId;
	private Integer sequence;
	private Long regulationLinkId;
	private String regulationLinkName;

	public SocializationTmp getSocialization() {
		return socialization;
	}

	public void setSocialization(SocializationTmp socialization) {
		this.socialization = socialization;
	}

	public Regulation getRegulation() {
		return regulation;
	}

	public void setRegulation(Regulation regulation) {
		this.regulation = regulation;
	}

	public String getPrimaryFlag() {
		return primaryFlag;
	}

	public void setPrimaryFlag(String primaryFlag) {
		this.primaryFlag = primaryFlag;
	}

	public Long getDelId() {
		return delId;
	}

	public void setDelId(Long delId) {
		this.delId = delId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Long getSocializationRegulationId() {
		return socializationRegulationId;
	}

	public void setSocializationRegulationId(Long socializationRegulationId) {
		this.socializationRegulationId = socializationRegulationId;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public Long getRegulationLinkId() {
		return regulationLinkId;
	}

	public void setRegulationLinkId(Long regulationLinkId) {
		this.regulationLinkId = regulationLinkId;
	}

	public String getRegulationLinkName() {
		return regulationLinkName;
	}

	public void setRegulationLinkName(String regulationLinkName) {
		this.regulationLinkName = regulationLinkName;
	}

	
	
	
	

}
