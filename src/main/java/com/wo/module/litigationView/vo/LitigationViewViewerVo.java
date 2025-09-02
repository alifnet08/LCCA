package com.wo.module.litigationView.vo;

import java.util.Locale;

import javax.faces.context.FacesContext;

public class LitigationViewViewerVo {

	private Long litigationViewId;
	private String nik;
	private String nama;
	private String status;
	private String statusCode;
	private String statusIn;
	private String statusEn;
	private String modifedBy;
	
	private int sequence;
	
	public Long getLitigationViewId() {
		return litigationViewId;
	}
	public void setLitigationViewId(Long litigationViewId) {
		this.litigationViewId = litigationViewId;
	}
	public String getNik() {
		return nik;
	}
	public void setNik(String nik) {
		this.nik = nik;
	}
	public String getNama() {
		return nama;
	}
	public void setNama(String nama) {
		this.nama = nama;
	}
	@SuppressWarnings("static-access")
	public String getStatus() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		
		if (locale != null && locale.equals(locale.ENGLISH)) {
			status = statusEn;
		} else {
			status = statusIn;
		}
		
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getModifedBy() {
		return modifedBy;
	}
	public void setModifedBy(String modifedBy) {
		this.modifedBy = modifedBy;
	}
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	public String getStatusIn() {
		return statusIn;
	}
	public void setStatusIn(String statusIn) {
		this.statusIn = statusIn;
	}
	public String getStatusEn() {
		return statusEn;
	}
	public void setStatusEn(String statusEn) {
		this.statusEn = statusEn;
	}
	public int getSequence() {
		return sequence;
	}
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
	
}
