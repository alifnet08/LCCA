package com.wo.module.api.notary;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import com.wo.module.notary.model.Notary;

public class NotaryContactResponse {

	private static final DateTimeFormatter UPDATED_AT = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

	private Long notaryId;
	private String notaryName;
	private String phoneNo;
	private String email;
	private String lastUpdateBy;
	private String lastUpdateDate;

	public static NotaryContactResponse from(Notary notary) {
		NotaryContactResponse response = new NotaryContactResponse();
		response.setNotaryId(notary.getNotaryId());
		response.setNotaryName(notary.getNotaryName());
		response.setPhoneNo(notary.getPhoneNo());
		response.setEmail(notary.getEmail());
		response.setLastUpdateBy(notary.getLastUpdateBy());
		if (notary.getLastUpdateDate() != null) {
			response.setLastUpdateDate(UPDATED_AT.format(
					notary.getLastUpdateDate().toInstant().atZone(ZoneId.systemDefault())));
		}
		return response;
	}

	public Long getNotaryId() {
		return notaryId;
	}

	public void setNotaryId(Long notaryId) {
		this.notaryId = notaryId;
	}

	public String getNotaryName() {
		return notaryName;
	}

	public void setNotaryName(String notaryName) {
		this.notaryName = notaryName;
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

	public String getLastUpdateBy() {
		return lastUpdateBy;
	}

	public void setLastUpdateBy(String lastUpdateBy) {
		this.lastUpdateBy = lastUpdateBy;
	}

	public String getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(String lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
}
