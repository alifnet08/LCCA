package com.wo.module.trcCorrespondence.model;

import java.io.Serializable;
import java.util.Date;

import com.wo.module.common.model.BaseEntity;

public class TrcCorrespondencePicFollowupEmail extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 5549822696402850117L;
	private Long correspondencePicFollowupEmailId;
	private TrcCorrespondence trcCorrespondence;
	private Date emailDate;
	private String emailStatus;
	private String emailSubject;
	private String emailContent;
	private Integer resendCount;
	
	private String slaType;
	private String sla;

	public TrcCorrespondencePicFollowupEmail() {
		super();
	}

	public Long getCorrespondencePicFollowupEmailId() {
		return correspondencePicFollowupEmailId;
	}

	public void setCorrespondencePicFollowupEmailId(Long correspondencePicFollowupEmailId) {
		this.correspondencePicFollowupEmailId = correspondencePicFollowupEmailId;
	}

	public Date getEmailDate() {
		return emailDate;
	}

	public void setEmailDate(Date emailDate) {
		this.emailDate = emailDate;
	}

	public TrcCorrespondence getTrcCorrespondence() {
		return trcCorrespondence;
	}

	public void setTrcCorrespondence(TrcCorrespondence trcCorrespondence) {
		this.trcCorrespondence = trcCorrespondence;
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

	public String getSla() {
		return sla;
	}

	public void setSla(String sla) {
		this.sla = sla;
	}

	
}
