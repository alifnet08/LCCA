package com.wo.module.regulationSocialization.model;

import java.io.Serializable;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.user.model.User;

public class SocializationPICComplianceTrc extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = -8169474750341075355L;
	
	private Long socializationPicComplianceId;
	private SocializationTrc socializationTrc;
	
	private User user;
	
	private Long delId;
	
	private Integer sequence;
	

	public SocializationTrc getSocializationTrc() {
		return socializationTrc;
	}

	public void setSocializationTrc(SocializationTrc socializationTrc) {
		this.socializationTrc = socializationTrc;
	}

	public Long getSocializationPicComplianceId() {
		return socializationPicComplianceId;
	}

	public void setSocializationPicComplianceId(Long socializationPicComplianceId) {
		this.socializationPicComplianceId = socializationPicComplianceId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Long getDelId() {
		return delId;
	}

	public void setDelId(Long delId) {
		this.delId = delId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

}
