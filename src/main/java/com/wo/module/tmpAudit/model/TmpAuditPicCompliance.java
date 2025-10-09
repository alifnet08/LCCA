package com.wo.module.tmpAudit.model;

import java.io.Serializable;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.user.model.User;

public class TmpAuditPicCompliance extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 3905404978975444632L;
	private Long auditPicComplianceId;
	private TmpAudit tmpAudit;
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

	public TmpAudit getTmpAudit() {
		return tmpAudit;
	}

	public void setTmpAudit(TmpAudit tmpAudit) {
		this.tmpAudit = tmpAudit;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

}
