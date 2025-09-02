package com.wo.module.tmpAudit.model;

import java.io.Serializable;
import java.util.Date;

import com.wo.module.common.model.BaseEntity;

public class TmpAuditPicFollowupEmail extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 8917057300708383692L;
	private Long auditPicFollowupEmailId;
	private TmpAuditPicFollowup tmpAuditPicFollowup;
	private Date emailDate;
	private String slaType;
	private Long sla;

	public TmpAuditPicFollowupEmail() {
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

	public TmpAuditPicFollowup getTmpAuditPicFollowup() {
		return tmpAuditPicFollowup;
	}

	public void setTmpAuditPicFollowup(TmpAuditPicFollowup tmpAuditPicFollowup) {
		this.tmpAuditPicFollowup = tmpAuditPicFollowup;
	}

}
