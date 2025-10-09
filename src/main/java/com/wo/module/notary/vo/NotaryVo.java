package com.wo.module.notary.vo;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import com.wo.module.notary.model.Notary;

public class NotaryVo {

	private Long notaryId;
	private String area;
	private String notaryName;
	private String address;
	private String notaryCategory;
	private String notaryCategoryName;
	private String areaCode;
	private String phoneNo;
	private String faxNo;
	private String email;
	private String mobileNo;
	private String workArea;
	private String note;
	private String notaryNo;
	
	private List<Notary> notaryList = new ArrayList<Notary>();
	
	private List<SelectItem> errorList = new ArrayList<SelectItem>();
	
	public NotaryVo() {}

	public Long getNotaryId() {
		return notaryId;
	}

	public void setNotaryId(Long notaryId) {
		this.notaryId = notaryId;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getNotaryName() {
		return notaryName;
	}

	public void setNotaryName(String notaryName) {
		this.notaryName = notaryName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getNotaryCategory() {
		return notaryCategory;
	}

	public void setNotaryCategory(String notaryCategory) {
		this.notaryCategory = notaryCategory;
	}

	public String getNotaryCategoryName() {
		return notaryCategoryName;
	}

	public void setNotaryCategoryName(String notaryCategoryName) {
		this.notaryCategoryName = notaryCategoryName;
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

	public String getWorkArea() {
		return workArea;
	}

	public void setWorkArea(String workArea) {
		this.workArea = workArea;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getNotaryNo() {
		return notaryNo;
	}

	public void setNotaryNo(String notaryNo) {
		this.notaryNo = notaryNo;
	}

	public List<Notary> getNotaryList() {
		return notaryList;
	}

	public void setNotaryList(List<Notary> notaryList) {
		this.notaryList = notaryList;
	}

	public List<SelectItem> getErrorList() {
		return errorList;
	}

	public void setErrorList(List<SelectItem> errorList) {
		this.errorList = errorList;
	}
	
}