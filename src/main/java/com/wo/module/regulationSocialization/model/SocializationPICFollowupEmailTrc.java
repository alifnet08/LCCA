package com.wo.module.regulationSocialization.model;

import java.io.Serializable;
import java.util.Date;

import com.wo.module.common.model.BaseEntity;

public class SocializationPICFollowupEmailTrc extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = 996141908126279968L;
	
	private long socializationPicFollowupEmailId;
	private SocializationPICFollowupTrc socializationPICFollowupTrc;
	
	
	private Date emailDate;
	
	private String slaType;
	
	private String sla;
	
	private String emailStatus;
	
	private String emailSubject;
	
	private String emailContent;
	
	private Integer resendCount;
	
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

	public SocializationPICFollowupTrc getSocializationPICFollowupTrc() {
		return socializationPICFollowupTrc;
	}

	public void setSocializationPICFollowupTrc(SocializationPICFollowupTrc socializationPICFollowupTrc) {
		this.socializationPICFollowupTrc = socializationPICFollowupTrc;
	}

	public Date getEmailDate() {
		return emailDate;
	}

	public void setEmailDate(Date emailDate) {
		this.emailDate = emailDate;
	}

	public String getSlaType() {
		return slaType;
	}

	public void setSlaType(String slaType) {
		this.slaType = slaType;
	}

	public String getSla() {
		return sla;
	}

	public void setSla(String sla) {
		this.sla = sla;
	}

	public String getEmailStatus() {
		return emailStatus;
	}

	public void setEmailStatus(String emailStatus) {
		this.emailStatus = emailStatus;
	}

	public String getEmailSubject() {
		return emailSubject;
	}

	public void setEmailSubject(String emailSubject) {
		this.emailSubject = emailSubject;
	}

	public Integer getResendCount() {
		return resendCount;
	}

	public void setResendCount(Integer resendCount) {
		this.resendCount = resendCount;
	}

	public String getEmailContent() {
		return emailContent;
	}

	public void setEmailContent(String emailContent) {
		this.emailContent = emailContent;
	}

	
	
}
