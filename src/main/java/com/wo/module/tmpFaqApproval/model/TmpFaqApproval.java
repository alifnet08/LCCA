package com.wo.module.tmpFaqApproval.model;

import java.io.Serializable;
import java.util.Date;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.faq.model.TmpFaq;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.user.model.User;

public class TmpFaqApproval extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 3821448122642909968L;
	
	private Long faqApprovalId;
	private TmpFaq tmpFaq;
	private User user;
	private ParameterDetail approvalStatus;
	private Date approvalDate;
	private String approvalNote;
	
	public Long getFaqApprovalId() {
		return faqApprovalId;
	}
	public void setFaqApprovalId(Long faqApprovalId) {
		this.faqApprovalId = faqApprovalId;
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
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public TmpFaq getTmpFaq() {
		return tmpFaq;
	}
	public void setTmpFaq(TmpFaq tmpFaq) {
		this.tmpFaq = tmpFaq;
	}

}
