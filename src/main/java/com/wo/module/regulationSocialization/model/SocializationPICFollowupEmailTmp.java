package com.wo.module.regulationSocialization.model;

import java.io.Serializable;
import java.util.Date;

import com.wo.module.common.model.BaseEntity;

public class SocializationPICFollowupEmailTmp extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 6395152164269025885L;
	private long socializationPicFollowupEmailId;
	private SocializationPICFollowupTmp socializationPICFollowupTmp;
	
	
	private Date emailDate;
	
	private Long delId;

	public Long getDelId() {
		return delId;
	}

	public void setDelId(Long delId) {
		this.delId = delId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public long getSocializationPicFollowupEmailId() {
		return socializationPicFollowupEmailId;
	}

	public void setSocializationPicFollowupEmailId(long socializationPicFollowupEmailId) {
		this.socializationPicFollowupEmailId = socializationPicFollowupEmailId;
	}

	public SocializationPICFollowupTmp getSocializationPICFollowupTmp() {
		return socializationPICFollowupTmp;
	}

	public void setSocializationPICFollowupTmp(SocializationPICFollowupTmp socializationPICFollowupTmp) {
		this.socializationPICFollowupTmp = socializationPICFollowupTmp;
	}

	public Date getEmailDate() {
		return emailDate;
	}

	public void setEmailDate(Date emailDate) {
		this.emailDate = emailDate;
	}

	
	
	
	

}
