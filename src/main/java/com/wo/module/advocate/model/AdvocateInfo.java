package com.wo.module.advocate.model;

import java.io.Serializable;

import com.wo.module.common.model.BaseEntity;

public class AdvocateInfo extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 6395152164269025885L;

	private Long advocateInfoId;
	private Advocate advocate;
	
	private String officeAddress;
	private String phoneNo;
	private String faxNo;
	private String email;
	
	private int sequence;
	
	
	public Long getAdvocateInfoId() {
		return advocateInfoId;
	}
	public void setAdvocateInfoId(Long advocateInfoId) {
		this.advocateInfoId = advocateInfoId;
	}
	public String getOfficeAddress() {
		return officeAddress;
	}
	public void setOfficeAddress(String officeAddress) {
		this.officeAddress = officeAddress;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public String getFaxNo() {
		return faxNo;
	}
	public void setFaxNo(String faxNo) {
		this.faxNo = faxNo;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public int getSequence() {
		return sequence;
	}
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
	public Advocate getAdvocate() {
		return advocate;
	}
	public void setAdvocate(Advocate advocate) {
		this.advocate = advocate;
	}

	
	
	
	
	
	
}
