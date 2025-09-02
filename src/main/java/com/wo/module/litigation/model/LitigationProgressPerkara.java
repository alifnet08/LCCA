package com.wo.module.litigation.model;

import java.io.Serializable;
import java.util.Date;

import com.wo.module.common.model.BaseEntity;

public class LitigationProgressPerkara extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 6395152164269025885L;

	private Long litiProgressPerkaraId;
	private LitigationNew litigation;
	private Date progressDate;
	private String progressDesc;
	
	//helper
	private Integer sequence;
	private Boolean isEditable;
	
	public Long getLitiProgressPerkaraId() {
		return litiProgressPerkaraId;
	}
	public void setLitiProgressPerkaraId(Long litiProgressPerkaraId) {
		this.litiProgressPerkaraId = litiProgressPerkaraId;
	}
	public LitigationNew getLitigation() {
		return litigation;
	}
	public void setLitigation(LitigationNew litigation) {
		this.litigation = litigation;
	}
	public Date getProgressDate() {
		return progressDate;
	}
	public void setProgressDate(Date progressDate) {
		this.progressDate = progressDate;
	}
	public String getProgressDesc() {
		return progressDesc;
	}
	public void setProgressDesc(String progressDesc) {
		this.progressDesc = progressDesc;
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
