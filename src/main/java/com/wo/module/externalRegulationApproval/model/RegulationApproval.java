package com.wo.module.externalRegulationApproval.model;

import java.io.Serializable;
import java.util.Date;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.externalRegulation.model.Regulation;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.user.model.User;

public class RegulationApproval extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 6395152164269025885L;
	private Long regulationApprovalId;
	private Regulation regulation;
	private User user;
	private ParameterDetail approvalStatus;
	private Date approvalDate;
	private String approvalNote;

	public Regulation getRegulation() {
		return regulation;
	}

	public void setRegulation(Regulation regulation) {
		this.regulation = regulation;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getApprovalDate() {
		return approvalDate;
	}

	public void setApprovalDate(Date approvalDate) {
		this.approvalDate = approvalDate;
	}

	public String getApprovalNote() {
		return approvalNote;
	}

	public void setApprovalNote(String approvalNote) {
		this.approvalNote = approvalNote;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Long getRegulationApprovalId() {
		return regulationApprovalId;
	}

	public void setRegulationApprovalId(Long regulationApprovalId) {
		this.regulationApprovalId = regulationApprovalId;
	}

	public ParameterDetail getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(ParameterDetail approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

}
