package com.wo.module.trcAudit.model;

import java.io.Serializable;
import java.util.Date;

import com.wo.module.common.model.BaseEntity;

public class TrcAuditPicFollowupRecEmail extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 8917057300708383692L;
	private Long auditPicFollowupEmailRecId;
	private TrcAuditPicFollowupRec trcAuditPicFollowupRec;
	private Date emailDate;
	private String slaType;
	private Long sla;

	public TrcAuditPicFollowupRecEmail() {
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


	public Long getAuditPicFollowupEmailRecId() {
		return auditPicFollowupEmailRecId;
	}

	public void setAuditPicFollowupEmailRecId(Long auditPicFollowupEmailRecId) {
		this.auditPicFollowupEmailRecId = auditPicFollowupEmailRecId;
	}

	public TrcAuditPicFollowupRec getTrcAuditPicFollowupRec() {
		return trcAuditPicFollowupRec;
	}

	public void setTrcAuditPicFollowupRec(TrcAuditPicFollowupRec trcAuditPicFollowupRec) {
		this.trcAuditPicFollowupRec = trcAuditPicFollowupRec;
	}

	

	

}
