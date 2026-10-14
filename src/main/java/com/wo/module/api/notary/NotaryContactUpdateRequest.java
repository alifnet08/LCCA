package com.wo.module.api.notary;

public class NotaryContactUpdateRequest {

	private Long notaryId;
	private String phoneNo;
	private String email;

	public Long getNotaryId() {
		return notaryId;
	}

	public void setNotaryId(Long notaryId) {
		this.notaryId = notaryId;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
