package com.wo.module.advocate.model;

import java.io.Serializable;

import com.wo.module.common.model.BaseEntity;

public class AdvocatePartners extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 6395152164269025885L;

	private Long advocatePartnersId;
	private Advocate advocate;
	
	private String partners;
	private String phoneNo;
	
	private int sequence;
	
	public Long getAdvocatePartnersId() {
		return advocatePartnersId;
	}
	public void setAdvocatePartnersId(Long advocatePartnersId) {
		this.advocatePartnersId = advocatePartnersId;
	}
	public Advocate getAdvocate() {
		return advocate;
	}
	public void setAdvocate(Advocate advocate) {
		this.advocate = advocate;
	}
	public String getPartners() {
		return partners;
	}
	public void setPartners(String partners) {
		this.partners = partners;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public int getSequence() {
		return sequence;
	}
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
	
	
	
	
	
	
	
	
}
