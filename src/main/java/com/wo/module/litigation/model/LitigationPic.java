package com.wo.module.litigation.model;

import java.io.Serializable;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.user.model.User;

public class LitigationPic extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 6395152164269025885L;

	private Long litigationPicId;
	private LitigationNew litigation;
	private User user;
	
	//helper
	private Integer sequence;
	private Boolean isEditable;
	
	public Long getLitigationPicId() {
		return litigationPicId;
	}
	public void setLitigationPicId(Long litigationPicId) {
		this.litigationPicId = litigationPicId;
	}
	public LitigationNew getLitigation() {
		return litigation;
	}
	public void setLitigation(LitigationNew litigation) {
		this.litigation = litigation;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
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
