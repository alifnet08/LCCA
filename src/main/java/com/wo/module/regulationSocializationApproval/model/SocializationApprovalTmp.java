package com.wo.module.regulationSocializationApproval.model;

import java.io.Serializable;
import java.util.Date;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.regulationSocialization.model.SocializationTmp;
import com.wo.module.user.model.User;

public class SocializationApprovalTmp extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 6395152164269025885L;
	private Long socializationApprovalId;
	private SocializationTmp socializationTmp;
	private User user;
	private String approvalStatus;
	private Date approvalDate;
	private String approvalNote;
	
	
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
	
	public Long getSocializationApprovalId() {
		return socializationApprovalId;
	}
	public void setSocializationApprovalId(Long socializationApprovalId) {
		this.socializationApprovalId = socializationApprovalId;
	}
	public String getApprovalStatus() {
		return approvalStatus;
	}
	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}
	public SocializationTmp getSocializationTmp() {
		return socializationTmp;
	}
	public void setSocializationTmp(SocializationTmp socializationTmp) {
		this.socializationTmp = socializationTmp;
	}
	
	
	
	
	
	
	
	
	
	
	

}
