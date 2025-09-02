package com.wo.module.internalRegulationObsolete.model;

import java.io.Serializable;
import java.util.Date;

import com.wo.module.common.model.BaseEntity;

public class InternalRegulationObsoleteEmailTmp extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 4524599126340739057L;

	private Long internalRegulationObsoleteEmailId;
	private InternalRegulationObsoletePic irgObsoletePic;
	
	private Date emailDate;
	private String emailStatus;
	private String emailSubject;
	private String emailContent;
	
	private String slaType;
	private Long sla;
	
	private Integer resendCount;
	
	//GETTER SETTER SECTION
	public Long getInternalRegulationObsoleteEmailId() {
		return internalRegulationObsoleteEmailId;
	}

	public void setInternalRegulationObsoleteEmailId(Long internalRegulationObsoleteEmailId) {
		this.internalRegulationObsoleteEmailId = internalRegulationObsoleteEmailId;
	}
	
	public InternalRegulationObsoletePic getIrgObsoletePic() {
		return irgObsoletePic;
	}

	public void setIrgObsoletePic(InternalRegulationObsoletePic irgObsoletePic) {
		this.irgObsoletePic = irgObsoletePic;
	}

	public Date getEmailDate() {
		return emailDate;
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

	public void setEmailDate(Date emailDate) {
		this.emailDate = emailDate;
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

	public Integer getResendCount() {
		return resendCount;
	}

	public void setResendCount(Integer resendCount) {
		this.resendCount = resendCount;
	}
}