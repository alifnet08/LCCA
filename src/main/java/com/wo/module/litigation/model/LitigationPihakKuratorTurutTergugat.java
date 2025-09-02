package com.wo.module.litigation.model;

import java.io.Serializable;

import com.wo.module.common.model.BaseEntity;

public class LitigationPihakKuratorTurutTergugat extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 6395152164269025885L;

	private Long litiPihakKuratorTurutTergugatId;
	private LitigationNew litigation;
	private String kuratorTurutTergugat;
	
	//helper
	private Integer sequence;
	private Boolean isEditable;
	
	
	public Long getLitiPihakKuratorTurutTergugatId() {
		return litiPihakKuratorTurutTergugatId;
	}
	public void setLitiPihakKuratorTurutTergugatId(Long litiPihakKuratorTurutTergugatId) {
		this.litiPihakKuratorTurutTergugatId = litiPihakKuratorTurutTergugatId;
	}
	public LitigationNew getLitigation() {
		return litigation;
	}
	public void setLitigation(LitigationNew litigation) {
		this.litigation = litigation;
	}
	public String getKuratorTurutTergugat() {
		return kuratorTurutTergugat;
	}
	public void setKuratorTurutTergugat(String kuratorTurutTergugat) {
		this.kuratorTurutTergugat = kuratorTurutTergugat;
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
