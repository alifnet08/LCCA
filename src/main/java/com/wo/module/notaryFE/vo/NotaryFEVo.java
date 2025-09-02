package com.wo.module.notaryFE.vo;

import java.io.Serializable;

public class NotaryFEVo implements Serializable{

	private static final long serialVersionUID = 1059869388424927618L;
	
	private Long notaryId;
	private String notaryCategory;
	private String notaryCategoryCode;
	private String notaryCategoryIn;
	private String notaryCategoryEn;
	private String area;
	private String address;
	private String areaCode;
	private String phoneNo;
	private String faxNo;
	private String email;
	private String mobileNo;
	private String notaryName;
	private String note;
	
	public Long getNotaryId() {
		return notaryId;
	}
	public void setNotaryId(Long notaryId) {
		this.notaryId = notaryId;
	}
	public String getNotaryCategory() {
		return notaryCategory;
	}
	public void setNotaryCategory(String notaryCategory) {
		this.notaryCategory = notaryCategory;
	}
	public String getNotaryCategoryCode() {
		return notaryCategoryCode;
	}
	public void setNotaryCategoryCode(String notaryCategoryCode) {
		this.notaryCategoryCode = notaryCategoryCode;
	}
	public String getNotaryCategoryIn() {
		return notaryCategoryIn;
	}
	public void setNotaryCategoryIn(String notaryCategoryIn) {
		this.notaryCategoryIn = notaryCategoryIn;
	}
	public String getNotaryCategoryEn() {
		return notaryCategoryEn;
	}
	public void setNotaryCategoryEn(String notaryCategoryEn) {
		this.notaryCategoryEn = notaryCategoryEn;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
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
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getNotaryName() {
		return notaryName;
	}
	public void setNotaryName(String notaryName) {
		this.notaryName = notaryName;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	
}
