package com.wo.module.trcAudit.model;

import java.io.Serializable;
import java.util.Date;

import com.wo.module.common.model.BaseEntity;

public class TrcAuditPicFollowupEmail extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 8917057300708383692L;
	private Long auditPicFollowupEmailId;
	private TrcAuditPicFollowup trcAuditPicFollowup;
	private Date emailDate;
	private String slaType;
	private Long sla;

	private String emailStatus;

	private String emailSubject;

	private String emailContent;

	private Integer resendCount;

	public TrcAuditPicFollowupEmail() {
		super();
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

	public Long getSla() {
		return sla;
	}

	public void setSla(Long sla) {
		this.sla = sla;
	}

	public Long getAuditPicFollowupEmailId() {
		return auditPicFollowupEmailId;
	}

	public void setAuditPicFollowupEmailId(Long auditPicFollowupEmailId) {
		this.auditPicFollowupEmailId = auditPicFollowupEmailId;
	}

	public TrcAuditPicFollowup getTrcAuditPicFollowup() {
		return trcAuditPicFollowup;
	}

	public void setTrcAuditPicFollowup(TrcAuditPicFollowup trcAuditPicFollowup) {
		this.trcAuditPicFollowup = trcAuditPicFollowup;
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

	public String getEmailStatus() {
		return emailStatus;
	}

	public void setEmailStatus(String emailStatus) {
		this.emailStatus = emailStatus;
	}

}
