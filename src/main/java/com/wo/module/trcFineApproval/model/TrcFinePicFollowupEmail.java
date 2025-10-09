package com.wo.module.trcFineApproval.model;

import java.io.Serializable;
import java.util.Date;

import com.wo.module.common.model.BaseEntity;

public class TrcFinePicFollowupEmail extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 5549822696402850117L;
	private Long finePicFollowupEmailId;
	private TrcFinePicFollowup trcFinePicFollowup;
	private Date emailDate;
	private String emailStatus;
	private String emailSubject;
	private String emailContent;
	private Integer resendCount;
	private String emailType;
	
	private String slaType;
	private Long sla;

	public TrcFinePicFollowupEmail() {
		super();
	}

	public Long getFinePicFollowupEmailId() {
		return finePicFollowupEmailId;
	}

	public void setFinePicFollowupEmailId(Long finePicFollowupEmailId) {
		this.finePicFollowupEmailId = finePicFollowupEmailId;
	}

	public Date getEmailDate() {
		return emailDate;
	}

	public void setEmailDate(Date emailDate) {
		this.emailDate = emailDate;
	}

	

	public TrcFinePicFollowup getTrcFinePicFollowup() {
		return trcFinePicFollowup;
	}

	public void setTrcFinePicFollowup(TrcFinePicFollowup trcFinePicFollowup) {
		this.trcFinePicFollowup = trcFinePicFollowup;
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

	public Integer getResendCount() {
		return resendCount;
	}

	public void setResendCount(Integer resendCount) {
		this.resendCount = resendCount;
	}

	public String getSlaType() {
		return slaType;
	}

	public void setSlaType(String slaType) {
		this.slaType = slaType;
	}

	public Long getSla() {
		return sla;
	}

	public void setSla(Long sla) {
		this.sla = sla;
	}

	public String getEmailType() {
		return emailType;
	}

	public void setEmailType(String emailType) {
		this.emailType = emailType;
	}


	
}
