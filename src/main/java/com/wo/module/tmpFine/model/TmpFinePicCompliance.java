package com.wo.module.tmpFine.model;

import java.io.Serializable;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.user.model.User;

public class TmpFinePicCompliance extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -1707141996366990113L;
	private Long finePicComplianceId;
	private TmpFine tmpFine;
	private User user;
	
	private String nikTemp;
	private String nameTemp;
	private String emailTemp;
	
	private int sequence;

	public TmpFinePicCompliance() {
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

	public TmpFine getTmpFine() {
		return tmpFine;
	}

	public void setTmpFine(TmpFine tmpFine) {
		this.tmpFine = tmpFine;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public String getNikTemp() {
		return nikTemp;
	}

	public void setNikTemp(String nikTemp) {
		this.nikTemp = nikTemp;
	}

	public String getEmailTemp() {
		return emailTemp;
	}

	public void setEmailTemp(String emailTemp) {
		this.emailTemp = emailTemp;
	}

	public String getNameTemp() {
		return nameTemp;
	}

	public void setNameTemp(String nameTemp) {
		this.nameTemp = nameTemp;
	}

	
}
