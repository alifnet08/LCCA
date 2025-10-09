package com.wo.module.trcRmd.model;

import java.io.Serializable;
import java.util.Date;

import com.wo.module.common.model.BaseEntity;

public class TrcRmdPicFollowupEmail extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 3019374791490640764L;

	private Long rmdPicFollowupEmailId;
	private TrcRmd trcRmd;

	private Date emailDate;
	private String emailStatus;
	private String emailSubject;
	private String emailContent;
	private Long resendCount;
	
	private String slaType;
	private String sla;
	private Date targetDate;

	public Long getRmdPicFollowupEmailId() {
		return rmdPicFollowupEmailId;
	}

	public void setRmdPicFollowupEmailId(Long rmdPicFollowupEmailId) {
		this.rmdPicFollowupEmailId = rmdPicFollowupEmailId;
	}

	public TrcRmd getTrcRmd() {
		return trcRmd;
	}

	public void setTrcRmd(TrcRmd trcRmd) {
		this.trcRmd = trcRmd;
	}

	public Date getEmailDate() {
		return emailDate;
	}

	public void setEmailDate(Date emailDate) {
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

	public Long getResendCount() {
		return resendCount;
	}

	public void setResendCount(Long resendCount) {
		this.resendCount = resendCount;
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

	public Date getTargetDate() {
		return targetDate;
	}

	public void setTargetDate(Date targetDate) {
		this.targetDate = targetDate;
	}

	
}