package com.wo.module.trcCorrespondence.model;

import java.io.Serializable;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.user.model.User;

public class TrcCorrespondencePicCompliance extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -124144149205712705L;
	private Long correspondencePicComplianceId;
	private TrcCorrespondence trcCorrespondence;
	private User user;

	private String nikTemp;
	private String nameTemp;
	private String emailTemp;

	private int sequence;

	public TrcCorrespondencePicCompliance() {
		super();
	}

	public Long getCorrespondencePicComplianceId() {
		return correspondencePicComplianceId;
	}

	public void setCorrespondencePicComplianceId(Long correspondencePicComplianceId) {
		this.correspondencePicComplianceId = correspondencePicComplianceId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public TrcCorrespondence getTrcCorrespondence() {
		return trcCorrespondence;
	}

	public void setTrcCorrespondence(TrcCorrespondence trcCorrespondence) {
		this.trcCorrespondence = trcCorrespondence;
	}

	public String getNikTemp() {
		return nikTemp;
	}

	public void setNikTemp(String nikTemp) {
		this.nikTemp = nikTemp;
	}

	public String getNameTemp() {
		return nameTemp;
	}

	public void setNameTemp(String nameTemp) {
		this.nameTemp = nameTemp;
	}

	public String getEmailTemp() {
		return emailTemp;
	}

	public void setEmailTemp(String emailTemp) {
		this.emailTemp = emailTemp;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

}
