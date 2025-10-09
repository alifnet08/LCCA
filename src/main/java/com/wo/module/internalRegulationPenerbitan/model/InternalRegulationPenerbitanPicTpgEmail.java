package com.wo.module.internalRegulationPenerbitan.model;

import java.io.Serializable;
import java.sql.Timestamp;

import com.wo.module.common.model.BaseEntity;

public class InternalRegulationPenerbitanPicTpgEmail extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -8026415835724610794L;
	
	private Long irgPicTpgEmailId;
	private InternalRegulationPenerbitanPicTpg internalRegulationPenerbitanPicTpg;
	private Timestamp emailDate;
	private String slaType;
	private Integer sla;
	private String emailStatus;
	private String emailSubject;
	private String emailContent;
	private Integer resendCount;
	
	public Long getIrgPicTpgEmailId() {
		return irgPicTpgEmailId;
	}
	public void setIrgPicTpgEmailId(Long irgPicTpgEmailId) {
		this.irgPicTpgEmailId = irgPicTpgEmailId;
	}
	public InternalRegulationPenerbitanPicTpg getInternalRegulationPenerbitanPicTpg() {
		return internalRegulationPenerbitanPicTpg;
	}
	public void setInternalRegulationPenerbitanPicTpg(
			InternalRegulationPenerbitanPicTpg internalRegulationPenerbitanPicTpg) {
		this.internalRegulationPenerbitanPicTpg = internalRegulationPenerbitanPicTpg;
	}
	public Timestamp getEmailDate() {
		return emailDate;
	}
	public void setEmailDate(Timestamp emailDate) {
		this.emailDate = emailDate;
	}
	public String getSlaType() {
		return slaType;
	}
	public void setSlaType(String slaType) {
		this.slaType = slaType;
	}
	public Integer getSla() {
		return sla;
	}
	public void setSla(Integer sla) {
		this.sla = sla;
	}
	public String getEmailStatus() {
		return emailStatus;
	}
	public void setEmailStatus(String emailStatus) {
		this.emailStatus = emailStatus;
	}
	public String getEmailSubject() {
		return emailSubject;
	}
	public void setEmailSubject(String emailSubject) {
		this.emailSubject = emailSubject;
	}
	public String getEmailContent() {
		return emailContent;
	}
	public void setEmailContent(String emailContent) {
		this.emailContent = emailContent;
	}
	public Integer getResendCount() {
		return resendCount;
	}
	public void setResendCount(Integer resendCount) {
		this.resendCount = resendCount;
	}

}
