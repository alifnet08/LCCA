package com.wo.module.tmpFine.model;

import java.io.Serializable;
import java.util.Date;

import com.wo.module.common.model.BaseEntity;

public class TmpFinePicFollowupEmail extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 6140880530217262281L;
	private Long finePicFollowupEmailId;
	private TmpFinePicFollowup tmpFinePicFollowup;
	private Date emailDate;
	private String slaType;
	private Long sla;
	private String emailType;

	public TmpFinePicFollowupEmail() {
		super();
	}

	public Long getFinePicFollowupEmailId() {
		return finePicFollowupEmailId;
	}

	public void setFinePicFollowupEmailId(Long finePicFollowupEmailId) {
		this.finePicFollowupEmailId = finePicFollowupEmailId;
	}

	public Date getEmailDate() {
		return emailDate;
	}

	public void setEmailDate(Date emailDate) {
		this.emailDate = emailDate;
	}

	public TmpFinePicFollowup getTmpFinePicFollowup() {
		return tmpFinePicFollowup;
	}

	public void setTmpFinePicFollowup(TmpFinePicFollowup tmpFinePicFollowup) {
		this.tmpFinePicFollowup = tmpFinePicFollowup;
	}

	public String getSlaType() {
		return slaType;
	}

	public void setSlaType(String slaType) {
		this.slaType = slaType;
	}

	public Long getSla() {
		return sla;
	}

	public void setSla(Long sla) {
		this.sla = sla;
	}

	public String getEmailType() {
		return emailType;
	}

	public void setEmailType(String emailType) {
		this.emailType = emailType;
	}

	

}
