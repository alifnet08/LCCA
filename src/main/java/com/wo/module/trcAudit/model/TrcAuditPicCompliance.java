package com.wo.module.trcAudit.model;

import java.io.Serializable;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.user.model.User;

public class TrcAuditPicCompliance extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -1688724972837542498L;
	private Long auditPicComplianceId;
	private TrcAudit trcAudit;
	private User user;
	
	// Transient
	private Integer sequence;

	public Long getAuditPicComplianceId() {
		return auditPicComplianceId;
	}

	public void setAuditPicComplianceId(Long auditPicComplianceId) {
		this.auditPicComplianceId = auditPicComplianceId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public TrcAudit getTrcAudit() {
		return trcAudit;
	}

	public void setTrcAudit(TrcAudit trcAudit) {
		this.trcAudit = trcAudit;
	}

}
