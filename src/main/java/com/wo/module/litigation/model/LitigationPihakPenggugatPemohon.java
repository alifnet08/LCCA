package com.wo.module.litigation.model;

import java.io.Serializable;

import com.wo.module.common.model.BaseEntity;

public class LitigationPihakPenggugatPemohon extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 6395152164269025885L;

	private Long litiPihakPenggugatPemohonId;
	private LitigationNew litigation;
	private String penggugatPemohon;
	
	//helper
	private Integer sequence;
	private Boolean isEditable;
	
	
	
	public Long getLitiPihakPenggugatPemohonId() {
		return litiPihakPenggugatPemohonId;
	}
	public void setLitiPihakPenggugatPemohonId(Long litiPihakPenggugatPemohonId) {
		this.litiPihakPenggugatPemohonId = litiPihakPenggugatPemohonId;
	}
	public LitigationNew getLitigation() {
		return litigation;
	}
	public void setLitigation(LitigationNew litigation) {
		this.litigation = litigation;
	}
	public String getPenggugatPemohon() {
		return penggugatPemohon;
	}
	public void setPenggugatPemohon(String penggugatPemohon) {
		this.penggugatPemohon = penggugatPemohon;
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
