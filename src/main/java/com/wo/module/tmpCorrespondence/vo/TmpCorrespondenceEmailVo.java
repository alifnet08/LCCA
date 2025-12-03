package com.wo.module.tmpCorrespondence.vo;

import java.io.Serializable;
import java.util.Date;

public class TmpCorrespondenceEmailVo implements Serializable {

	private static final long serialVersionUID = 2118626331928194949L;
	
	private Long crpdcPicConfirmId;
	
	private String emailTo;
	
	private String emailCC1;
	
	private String emailCC2;
	
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
	
	public String getEmailCC1() {
		return emailCC1;
	}

	public void setEmailCC1(String emailCC1) {
		this.emailCC1 = emailCC1;
	}
	
	public String getEmailCC2() {
		return emailCC2;
	}

	public void setEmailCC2(String emailCC2) {
		this.emailCC2 = emailCC2;
	}


	public Date getTargetDate() {
		return targetDate;
	}

	public void setTargetDate(Date targetDate) {
		this.targetDate = targetDate;
	}
	
	
	
}