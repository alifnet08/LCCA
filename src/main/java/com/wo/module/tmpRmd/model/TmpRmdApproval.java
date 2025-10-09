package com.wo.module.tmpRmd.model;

import java.io.Serializable;
import java.util.Date;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.user.model.User;

public class TmpRmdApproval extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 3019374791490640764L;

	private Long rmdApprovalId;
	private TmpRmd tmpRmd;
	private User user;

	//private String approvalStatus;
	private ParameterDetail approvalStatus;
	private Date approvalDate;
	private String approvalNote;
	
	public Long getRmdApprovalId() {
		return rmdApprovalId;
	}

	public void setRmdApprovalId(Long rmdApprovalId) {
		this.rmdApprovalId = rmdApprovalId;
	}

	public TmpRmd getTmpRmd() {
		return tmpRmd;
	}

	public void setTmpRmd(TmpRmd tmpRmd) {
		this.tmpRmd = tmpRmd;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public ParameterDetail getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(ParameterDetail approvalStatus) {
		this.approvalStatus = approvalStatus;
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

}