package com.wo.module.qaAdmin.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.Locale;

import javax.faces.context.FacesContext;

public class QAAdminVo implements Serializable{

	private static final long serialVersionUID = -3045245741083745899L;

	private Long qnaId;
	private String status;
	private String statusCode;
	private String statusIn;
	private String statusEn;
	private String ticketNo;
	private String qName;
	private String kategori;
	private String kategoriCode;
	private String kategoriIn;
	private String kategoriEn;
	private String qTitle;
	private Date tanggalPembuatan;
	private String tanggalPembuatanStr;
	private String kataKunci;
	private String assignedTo;
	private String elapsedTime;
	private String terakhirDiUbah;
	private Date tanggalTerkahirdiUbah;
	private String tanggalTerkahirdiUbahStr;
	private Date qDate;
	private String qDateStr;
	private String slaType;
	private String userKategori;
	private Date creationDate;
	private String creationDateStr;
	private String answerBy;
	
	public Long getQnaId() {
		return qnaId;
	}
	public void setQnaId(Long qnaId) {
		this.qnaId = qnaId;
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
	public String getTicketNo() {
		return ticketNo;
	}
	public void setTicketNo(String ticketNo) {
		this.ticketNo = ticketNo;
	}
	public String getqName() {
		return qName;
	}
	public void setqName(String qName) {
		this.qName = qName;
	}
	@SuppressWarnings("static-access")
	public String getKategori() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		
		if (locale != null && locale.equals(locale.ENGLISH)) {
			kategori = kategoriEn;
		} else {
			kategori = kategoriIn;
		}
		return kategori;
	}
	public void setKategori(String kategori) {
		this.kategori = kategori;
	}
	public String getqTitle() {
		return qTitle;
	}
	public void setqTitle(String qTitle) {
		this.qTitle = qTitle;
	}
	public Date getTanggalPembuatan() {
		return tanggalPembuatan;
	}
	public void setTanggalPembuatan(Date tanggalPembuatan) {
		this.tanggalPembuatan = tanggalPembuatan;
	}
	public String getTanggalPembuatanStr() {
		return tanggalPembuatanStr;
	}
	public void setTanggalPembuatanStr(String tanggalPembuatanStr) {
		this.tanggalPembuatanStr = tanggalPembuatanStr;
	}
	public String getKataKunci() {
		return kataKunci;
	}
	public void setKataKunci(String kataKunci) {
		this.kataKunci = kataKunci;
	}
	public String getAssignedTo() {
		return assignedTo;
	}
	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}
	public String getElapsedTime() {
		return elapsedTime;
	}
	public void setElapsedTime(String elapsedTime) {
		this.elapsedTime = elapsedTime;
	}
	public String getTerakhirDiUbah() {
		return terakhirDiUbah;
	}
	public void setTerakhirDiUbah(String terakhirDiUbah) {
		this.terakhirDiUbah = terakhirDiUbah;
	}
	public Date getTanggalTerkahirdiUbah() {
		return tanggalTerkahirdiUbah;
	}
	public void setTanggalTerkahirdiUbah(Date tanggalTerkahirdiUbah) {
		this.tanggalTerkahirdiUbah = tanggalTerkahirdiUbah;
	}
	public String getTanggalTerkahirdiUbahStr() {
		return tanggalTerkahirdiUbahStr;
	}
	public void setTanggalTerkahirdiUbahStr(String tanggalTerkahirdiUbahStr) {
		this.tanggalTerkahirdiUbahStr = tanggalTerkahirdiUbahStr;
	}
	public Date getqDate() {
		return qDate;
	}
	public void setqDate(Date qDate) {
		this.qDate = qDate;
	}
	public String getqDateStr() {
		return qDateStr;
	}
	public void setqDateStr(String qDateStr) {
		this.qDateStr = qDateStr;
	}
	public String getStatusEn() {
		return statusEn;
	}
	public void setStatusEn(String statusEn) {
		this.statusEn = statusEn;
	}
	public String getSlaType() {
		return slaType;
	}
	public void setSlaType(String slaType) {
		this.slaType = slaType;
	}
	public String getKategoriCode() {
		return kategoriCode;
	}
	public void setKategoriCode(String kategoriCode) {
		this.kategoriCode = kategoriCode;
	}
	public String getKategoriIn() {
		return kategoriIn;
	}
	public void setKategoriIn(String kategoriIn) {
		this.kategoriIn = kategoriIn;
	}
	public String getKategoriEn() {
		return kategoriEn;
	}
	public void setKategoriEn(String kategoriEn) {
		this.kategoriEn = kategoriEn;
	}
	public String getUserKategori() {
		return userKategori;
	}
	public void setUserKategori(String userKategori) {
		this.userKategori = userKategori;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public String getCreationDateStr() {
		return creationDateStr;
	}
	public void setCreationDateStr(String creationDateStr) {
		this.creationDateStr = creationDateStr;
	}
	public String getAnswerBy() {
		return answerBy;
	}
	public void setAnswerBy(String answerBy) {
		this.answerBy = answerBy;
	}
}
