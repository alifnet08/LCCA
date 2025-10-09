package com.wo.module.tmpCorrespondence.vo;

import java.io.Serializable;
import java.util.Date;

public class TmpCorrespondenceEmailVo implements Serializable {

	private static final long serialVersionUID = 2118626331928194949L;
	
	private Long crpdcPicConfirmId;
	
	private String emailTo;
	
	private Date targetDate;
	
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

	public Date getTargetDate() {
		return targetDate;
	}

	public void setTargetDate(Date targetDate) {
		this.targetDate = targetDate;
	}
	
	
	
}