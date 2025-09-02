package com.wo.module.trcRmd.model;

import java.io.Serializable;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.user.model.User;

public class TrcRmdSupportingUnit extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 3019374791490640764L;

	private Long rmdSupportingUnitId;
	private TrcRmd trcRmd;

	private Long divisionId;
	private User emailCc1;
	private User emailCc2;
	private User emailCc3;
	
	private String emailCcTemp1;
	private String emailCcTemp2;
	private String emailCcTemp3;

	private int sequence;

	public Long getRmdSupportingUnitId() {
		return rmdSupportingUnitId;
	}

	public void setRmdSupportingUnitId(Long rmdSupportingUnitId) {
		this.rmdSupportingUnitId = rmdSupportingUnitId;
	}

	public TrcRmd getTrcRmd() {
		return trcRmd;
	}

	public void setTrcRmd(TrcRmd trcRmd) {
		this.trcRmd = trcRmd;
	}

	public Long getDivisionId() {
		return divisionId;
	}

	public void setDivisionId(Long divisionId) {
		this.divisionId = divisionId;
	}

	public User getEmailCc1() {
		return emailCc1;
	}

	public void setEmailCc1(User emailCc1) {
		this.emailCc1 = emailCc1;
	}

	public User getEmailCc2() {
		return emailCc2;
	}

	public void setEmailCc2(User emailCc2) {
		this.emailCc2 = emailCc2;
	}

	public User getEmailCc3() {
		return emailCc3;
	}

	public void setEmailCc3(User emailCc3) {
		this.emailCc3 = emailCc3;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public String getEmailCcTemp1() {
		return emailCcTemp1;
	}

	public void setEmailCcTemp1(String emailCcTemp1) {
		this.emailCcTemp1 = emailCcTemp1;
	}

	public String getEmailCcTemp2() {
		return emailCcTemp2;
	}

	public void setEmailCcTemp2(String emailCcTemp2) {
		this.emailCcTemp2 = emailCcTemp2;
	}

	public String getEmailCcTemp3() {
		return emailCcTemp3;
	}

	public void setEmailCcTemp3(String emailCcTemp3) {
		this.emailCcTemp3 = emailCcTemp3;
	}
	
	

}