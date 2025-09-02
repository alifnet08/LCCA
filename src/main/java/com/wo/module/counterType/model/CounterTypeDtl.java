
package com.wo.module.counterType.model;

import java.io.Serializable;

import com.wo.module.common.model.BaseEntity;

public class CounterTypeDtl extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 6904164992369379399L;

	private Long counterTypeDtlId;
	private CounterType counterType;

	private String slaType;
	private Long sla;

	private String emailTo;
	private String emailCc2;
	private String emailCc1;

	private int sequence;

	private Boolean tempCheck;

	public Long getCounterTypeDtlId() {
		return counterTypeDtlId;
	}

	public void setCounterTypeDtlId(Long counterTypeDtlId) {
		this.counterTypeDtlId = counterTypeDtlId;
	}

	public CounterType getCounterType() {
		return counterType;
	}

	public void setCounterType(CounterType counterType) {
		this.counterType = counterType;
	}

	public String getSlaType() {
		return slaType;
	}

	public void setSlaType(String slaType) {
		this.slaType = slaType;
	}

	public String getEmailTo() {
		return emailTo;
	}

	public void setEmailTo(String emailTo) {
		this.emailTo = emailTo;
	}

	public String getEmailCc2() {
		return emailCc2;
	}

	public void setEmailCc2(String emailCc2) {
		this.emailCc2 = emailCc2;
	}

	public String getEmailCc1() {
		return emailCc1;
	}

	public void setEmailCc1(String emailCc1) {
		this.emailCc1 = emailCc1;
	}

	public Long getSla() {
		return sla;
	}

	public void setSla(Long sla) {
		this.sla = sla;
	}

	public Boolean getTempCheck() {
		return tempCheck;
	}

	public void setTempCheck(Boolean tempCheck) {
		this.tempCheck = tempCheck;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

}
