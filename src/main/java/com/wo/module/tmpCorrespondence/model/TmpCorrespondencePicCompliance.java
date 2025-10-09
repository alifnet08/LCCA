package com.wo.module.tmpCorrespondence.model;

import java.io.Serializable;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.user.model.User;

public class TmpCorrespondencePicCompliance extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -1707141996366990113L;
	private Long correspondencePicComplianceId;
	private TmpCorrespondence tmpCorrespondence;
	private User user;
	private User user2;
	private User user3;
	
	private String nikTemp;
	private String nameTemp;
	private String emailTemp;
	
	private String nameTemp2;
	private String nameTemp3;
	
	private int sequence;

	public TmpCorrespondencePicCompliance() {
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
	
	public User getUser2() {
		return user2;
	}

	public void setUser2(User user2) {
		this.user2 = user2;
	}
	
	public User getUser3() {
		return user3;
	}

	public void setUser3(User user3) {
		this.user3 = user3;
	}
	
	public TmpCorrespondence getTmpCorrespondence() {
		return tmpCorrespondence;
	}

	public void setTmpCorrespondence(TmpCorrespondence tmpCorrespondence) {
		this.tmpCorrespondence = tmpCorrespondence;
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
	
	public String getNameTemp2() {
		return nameTemp2;
	}

	public void setNameTemp2(String nameTemp2) {
		this.nameTemp2 = nameTemp2;
	}
	public String getNameTemp3() {
		return nameTemp3;
	}
	public void setNameTemp3(String nameTemp3) {
		this.nameTemp3 = nameTemp3;
	}
}
