package com.wo.module.litigation.model;

import java.io.Serializable;

import com.wo.module.common.model.BaseEntity;

public class LitigationPihakTergugatTermohon extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 6395152164269025885L;

	private Long litiPihakTergugatTermohonId;
	private LitigationNew litigation;
	private String tergugatTermohon;
	
	//helper
	private Integer sequence;
	private Boolean isEditable;
	
	
	public Long getLitiPihakTergugatTermohonId() {
		return litiPihakTergugatTermohonId;
	}
	public void setLitiPihakTergugatTermohonId(Long litiPihakTergugatTermohonId) {
		this.litiPihakTergugatTermohonId = litiPihakTergugatTermohonId;
	}
	public LitigationNew getLitigation() {
		return litigation;
	}
	public void setLitigation(LitigationNew litigation) {
		this.litigation = litigation;
	}
	public String getTergugatTermohon() {
		return tergugatTermohon;
	}
	public void setTergugatTermohon(String tergugatTermohon) {
		this.tergugatTermohon = tergugatTermohon;
	}
	public Integer getSequence() {
		return sequence;
	}
	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}
	public Boolean getIsEditable() {
		return isEditable;
	}
	public void setIsEditable(Boolean isEditable) {
		this.isEditable = isEditable;
	}

	
	
}
