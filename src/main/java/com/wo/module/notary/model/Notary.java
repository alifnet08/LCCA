package com.wo.module.notary.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.parameter.model.ParameterDetail;

public class Notary extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 6395152164269025885L;

	private Long notaryId;
	private String area;
	private String notaryName;
	private String address;
	private ParameterDetail notaryCategory;
	private String areaCode;
	private String phoneNo;
	private String faxNo;
	private String email;
	private String mobileNo;
	private String workArea;
	private String note;
	private String notaryNo;
	private String status;
	private Date tanggalPensiun;
	private Date tanggalBerakhirPks;
	private List<NotaryDocument> notaryDocuments = new ArrayList<NotaryDocument>();
	
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

	public ParameterDetail getNotaryCategory() {
		return notaryCategory;
	}

	public void setNotaryCategory(ParameterDetail notaryCategory) {
		this.notaryCategory = notaryCategory;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getNotaryNo() {
		return notaryNo;
	}

	public void setNotaryNo(String notaryNo) {
		this.notaryNo = notaryNo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getTanggalPensiun() {
		return tanggalPensiun;
	}

	public void setTanggalPensiun(Date tanggalPensiun) {
		this.tanggalPensiun = tanggalPensiun;
	}

	public Date getTanggalBerakhirPks() {
		return tanggalBerakhirPks;
	}

	public void setTanggalBerakhirPks(Date tanggalBerakhirPks) {
		this.tanggalBerakhirPks = tanggalBerakhirPks;
	}

	public List<NotaryDocument> getNotaryDocuments() {
		return notaryDocuments;
	}

	public void setNotaryDocuments(List<NotaryDocument> notaryDocuments) {
		this.notaryDocuments = notaryDocuments;
	}

}
