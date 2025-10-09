package com.wo.module.trcFineApproval.model;

import java.io.Serializable;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.user.model.User;

public class TrcFinePicCompliance extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -124144149205712705L;
	private Long finePicComplianceId;
	private TrcFine trcFine;
	private User user;

	private String nikTemp;
	private String nameTemp;
	private String emailTemp;

	private int sequence;

	public TrcFinePicCompliance() {
		super();
	}

	public Long getFinePicComplianceId() {
		return finePicComplianceId;
	}

	public void setFinePicComplianceId(Long finePicComplianceId) {
		this.finePicComplianceId = finePicComplianceId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public TrcFine getTrcFine() {
		return trcFine;
	}

	public void setTrcFine(TrcFine trcFine) {
		this.trcFine = trcFine;
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
