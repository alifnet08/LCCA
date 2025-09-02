package com.wo.module.email.vo;

import java.io.Serializable;

import com.wo.module.common.model.BaseEntity;

public class EmailVO extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -8316261836912273768L;
	private Long emailId;
	private String emailType;
	private String emailDate;
	private String emailStatus;
	private String emailSubject;
	private String emailContent;
	private int resendCount;
	private String lastSentDate;

	public String getEmailType() {
		return emailType;
	}

	public void setEmailType(String emailType) {
		this.emailType = emailType;
	}

	public String getEmailDate() {
		return emailDate;
	}

	public void setEmailDate(String emailDate) {
		this.emailDate = emailDate;
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

	public String getEmailContent() {
		return emailContent;
	}

	public void setEmailContent(String emailContent) {
		this.emailContent = emailContent;
	}

	public int getResendCount() {
		return resendCount;
	}

	public void setResendCount(int resendCount) {
		this.resendCount = resendCount;
	}

	public String getLastSentDate() {
		return lastSentDate;
	}

	public void setLastSentDate(String lastSentDate) {
		this.lastSentDate = lastSentDate;
	}

	public Long getEmailId() {
		return emailId;
	}

	public void setEmailId(Long emailId) {
		this.emailId = emailId;
	}

}
