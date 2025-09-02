package com.wo.module.articleApproval.model;

import java.io.Serializable;
import java.util.Date;

import com.wo.module.article.model.TmpArticle;
import com.wo.module.common.model.BaseEntity;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.user.model.User;

public class TmpArticleApproval extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 9115864400506864699L;
	
	private Long artcileApprovalId;
	private TmpArticle tmpArticle;
	private User user;
	private ParameterDetail approvalStatus;
	private Date approvalDate;
	private String approvalNote;
	
	public Long getArtcileApprovalId() {
		return artcileApprovalId;
	}
	public void setArtcileApprovalId(Long artcileApprovalId) {
		this.artcileApprovalId = artcileApprovalId;
	}
	public TmpArticle getTmpArticle() {
		return tmpArticle;
	}
	public void setTmpArticle(TmpArticle tmpArticle) {
		this.tmpArticle = tmpArticle;
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
