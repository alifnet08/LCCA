package com.wo.module.tmpCorrespondence.vo;

import java.io.Serializable;

public class TmpCorrespondenceEmailVo implements Serializable {

	private static final long serialVersionUID = 2118626331928194949L;
	
	private Long crpdcPicConfirmId;
	
	private String emailTo;
	
	public TmpCorrespondenceEmailVo() {}

	public Long getCrpdcPicConfirmId() {
		return crpdcPicConfirmId;
	}

	public void setCrpdcPicConfirmId(Long crpdcPicConfirmId) {
		this.crpdcPicConfirmId = crpdcPicConfirmId;
	}

	public String getEmailTo() {
		return emailTo;
	}

	public void setEmailTo(String emailTo) {
		this.emailTo = emailTo;
	}
	
	
	
}