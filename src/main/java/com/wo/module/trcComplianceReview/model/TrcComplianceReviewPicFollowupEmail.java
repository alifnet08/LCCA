package com.wo.module.trcComplianceReview.model;

import java.io.Serializable;
import java.util.Date;

import com.wo.module.common.model.BaseEntity;

public class TrcComplianceReviewPicFollowupEmail extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 6140880530217262281L;
	private Long complianceReviewPicFollowupEmailId;
	private TrcComplianceReviewPicFollowup trcComplianceReviewPicFolllowup;
	private Date emailDate;
	private String slaType;
	private Long sla;

	private String emailStatus;

	private String emailSubject;

	private String emailContent;

	private Integer resendCount;

	private Long delId;

	public TrcComplianceReviewPicFollowupEmail() {
		super();
	}

	public Date getEmailDate() {
		return emailDate;
	}

	public void setEmailDate(Date emailDate) {
		this.emailDate = emailDate;
	}

	public Long getComplianceReviewPicFollowupEmailId() {
		return complianceReviewPicFollowupEmailId;
	}

	public void setComplianceReviewPicFollowupEmailId(Long complianceReviewPicFollowupEmailId) {
		this.complianceReviewPicFollowupEmailId = complianceReviewPicFollowupEmailId;
	}

	public TrcComplianceReviewPicFollowup getTrcComplianceReviewPicFolllowup() {
		return trcComplianceReviewPicFolllowup;
	}

	public void setTrcComplianceReviewPicFolllowup(TrcComplianceReviewPicFollowup trcComplianceReviewPicFolllowup) {
		this.trcComplianceReviewPicFolllowup = trcComplianceReviewPicFolllowup;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
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

	public Long getDelId() {
		return delId;
	}

	public void setDelId(Long delId) {
		this.delId = delId;
	}

}
