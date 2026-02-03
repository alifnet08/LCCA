package com.wo.module.internalRegulationPenerbitan.model;

import java.io.Serializable;

import com.wo.module.common.model.BaseEntity;

public class InternalRegulationPenerbitanEmailGroup extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 5094293873448801499L;
	
	private Long irgEmailGroupId;
	
	private InternalRegulationPenerbitan irg;
	
	private String emailGroup;
	private String reviewApprovalFlag;
	
	private Integer sequence;
	
	private Boolean editableTemp;
	private boolean checkFlag;

	public Long getIrgEmailGroupId() {
		return irgEmailGroupId;
	}

	public void setIrgEmailGroupId(Long irgEmailGroupId) {
		this.irgEmailGroupId = irgEmailGroupId;
	}

	public InternalRegulationPenerbitan getIrg() {
		return irg;
	}

	public void setIrg(InternalRegulationPenerbitan irg) {
		this.irg = irg;
	}

	public String getEmailGroup() {
		return emailGroup;
	}

	public void setEmailGroup(String emailGroup) {
		this.emailGroup = emailGroup;
	}

	public String getReviewApprovalFlag() {
		return reviewApprovalFlag;
	}

	public void setReviewApprovalFlag(String reviewApprovalFlag) {
		this.reviewApprovalFlag = reviewApprovalFlag;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public Boolean getEditableTemp() {
		return editableTemp;
	}

	public void setEditableTemp(Boolean editableTemp) {
		this.editableTemp = editableTemp;
	}

	public boolean getCheckFlag() {
		return checkFlag;
	}

	public void setCheckFlag(boolean checkFlag) {
		this.checkFlag = checkFlag;
	}
	
}