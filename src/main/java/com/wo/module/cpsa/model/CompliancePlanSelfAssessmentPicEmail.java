package com.wo.module.cpsa.model;

import java.io.Serializable;
import java.sql.Timestamp;

import com.wo.module.common.model.BaseEntity;

public class CompliancePlanSelfAssessmentPicEmail extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 196099225654842515L;

	private Long cpsaPicEmailId;
	private CompliancePlanSelfAssessmentPic cpsaPic;
	private Timestamp emailDate;
	private String slaType;
	private Integer sla;
	private String emailStatus;
	private String emailSubject;
	private String emailContent;
	private Integer resendCount;
	
	public Long getCpsaPicEmailId() {
		return cpsaPicEmailId;
	}
	public void setCpsaPicEmailId(Long cpsaPicEmailId) {
		this.cpsaPicEmailId = cpsaPicEmailId;
	}
	public CompliancePlanSelfAssessmentPic getCpsaPic() {
		return cpsaPic;
	}
	public void setCpsaPic(CompliancePlanSelfAssessmentPic cpsaPic) {
		this.cpsaPic = cpsaPic;
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