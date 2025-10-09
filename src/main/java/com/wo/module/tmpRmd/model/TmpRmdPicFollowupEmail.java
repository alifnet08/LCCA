package com.wo.module.tmpRmd.model;

import java.io.Serializable;
import java.util.Date;

import com.wo.module.common.model.BaseEntity;

public class TmpRmdPicFollowupEmail extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 3019374791490640764L;
	
	private Long rmdPicFollowupEmailId;
	private TmpRmd tmpRmd;

	private Date emailDate;
	private Date targetDate;
	
	private Long sla;
	private String slaType;

	public Long getRmdPicFollowupEmailId() {
		return rmdPicFollowupEmailId;
	}

	public void setRmdPicFollowupEmailId(Long rmdPicFollowupEmailId) {
		this.rmdPicFollowupEmailId = rmdPicFollowupEmailId;
	}

	public TmpRmd getTmpRmd() {
		return tmpRmd;
	}

	public void setTmpRmd(TmpRmd tmpRmd) {
		this.tmpRmd = tmpRmd;
	}

	public Date getEmailDate() {
		return emailDate;
	}

	public void setEmailDate(Date emailDate) {
		this.emailDate = emailDate;
	}

	public Date getTargetDate() {
		return targetDate;
	}

	public void setTargetDate(Date targetDate) {
		this.targetDate = targetDate;
	}

	public Long getSla() {
		return sla;
	}

	public void setSla(Long sla) {
		this.sla = sla;
	}

	public String getSlaType() {
		return slaType;
	}

	public void setSlaType(String slaType) {
		this.slaType = slaType;
	}
	
	
	
}