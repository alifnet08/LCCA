package com.wo.module.report.reportComplianceOnSiteReviewDetail.model;

import java.util.Date;
import java.util.Locale;

import javax.faces.context.FacesContext;

public class ReportComplianceOnSiteReviewDetail{

	private String kategoriReviewIn;
	private String kategoriReviewEn;
	private String documentNo;
	private String perihalIn;
	private String perihalEn;
	private String areaReview;
	private String temuan;
	private String poinTindakLanjut;
	private Date targetDate;
	private String picCompliance;
	private String pic1;
	private String pic2;
	private String pic3;
	private String statusIn;
	private String statusEn;
	private String buktiKonfirmasi;
	private Date tanggalPemenuhan;
	private Date tanggalKonfirmasi;
	private Date creationDate;
	private String reviewCategory;
	private String Sla;
	
	// helper
	private String kategoriReviewName;
	private String perihalName;
	private String targetDateStr;
	private String tanggalPemenuhanStr;
	private String tanggalKonfirmasiStr;
	private String creationDateStr;
	
	
	public String getKategoriReviewIn() {
		return kategoriReviewIn;
	}
	public void setKategoriReviewIn(String kategoriReviewIn) {
		this.kategoriReviewIn = kategoriReviewIn;
	}
	public String getKategoriReviewEn() {
		return kategoriReviewEn;
	}
	public void setKategoriReviewEn(String kategoriReviewEn) {
		this.kategoriReviewEn = kategoriReviewEn;
	}
	public String getDocumentNo() {
		return documentNo;
	}
	public void setDocumentNo(String documentNo) {
		this.documentNo = documentNo;
	}
	public String getPerihalIn() {
		return perihalIn;
	}
	public void setPerihalIn(String perihalIn) {
		this.perihalIn = perihalIn;
	}
	public String getPerihalEn() {
		return perihalEn;
	}
	public void setPerihalEn(String perihalEn) {
		this.perihalEn = perihalEn;
	}
	public String getAreaReview() {
		return areaReview;
	}
	public void setAreaReview(String areaReview) {
		this.areaReview = areaReview;
	}
	public String getTemuan() {
		return temuan;
	}
	public void setTemuan(String temuan) {
		this.temuan = temuan;
	}
	public String getPoinTindakLanjut() {
		return poinTindakLanjut;
	}
	public void setPoinTindakLanjut(String poinTindakLanjut) {
		this.poinTindakLanjut = poinTindakLanjut;
	}
	public Date getTargetDate() {
		return targetDate;
	}
	public void setTargetDate(Date targetDate) {
		this.targetDate = targetDate;
	}
	public String getPicCompliance() {
		return picCompliance;
	}
	public void setPicCompliance(String picCompliance) {
		this.picCompliance = picCompliance;
	}
	public String getPic1() {
		return pic1;
	}
	public void setPic1(String pic1) {
		this.pic1 = pic1;
	}
	public String getPic2() {
		return pic2;
	}
	public void setPic2(String pic2) {
		this.pic2 = pic2;
	}
	public String getPic3() {
		return pic3;
	}
	public void setPic3(String pic3) {
		this.pic3 = pic3;
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
	public String getBuktiKonfirmasi() {
		return buktiKonfirmasi;
	}
	public void setBuktiKonfirmasi(String buktiKonfirmasi) {
		this.buktiKonfirmasi = buktiKonfirmasi;
	}
	public Date getTanggalPemenuhan() {
		return tanggalPemenuhan;
	}
	public void setTanggalPemenuhan(Date tanggalPemenuhan) {
		this.tanggalPemenuhan = tanggalPemenuhan;
	}
	public Date getTanggalKonfirmasi() {
		return tanggalKonfirmasi;
	}
	public void setTanggalKonfirmasi(Date tanggalKonfirmasi) {
		this.tanggalKonfirmasi = tanggalKonfirmasi;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public String getReviewCategory() {
		return reviewCategory;
	}
	public void setReviewCategory(String reviewCategory) {
		this.reviewCategory = reviewCategory;
	}
	@SuppressWarnings("static-access")
	public String getKategoriReviewName() {
		
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		
		if (locale != null && locale.equals(locale.ENGLISH)) {
			kategoriReviewName = kategoriReviewEn;
		} else {
			kategoriReviewName = kategoriReviewIn;
		}
		
		return kategoriReviewName;
	}
	public void setKategoriReviewName(String kategoriReviewName) {
		this.kategoriReviewName = kategoriReviewName;
	}
	@SuppressWarnings("static-access")
	public String getPerihalName() {
		
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		
		if (locale != null && locale.equals(locale.ENGLISH)) {
			perihalName = perihalEn;
		} else {
			perihalName = perihalIn;
		}
		
		return perihalName;
	}
	public void setPerihalName(String perihalName) {
		this.perihalName = perihalName;
	}
	public String getTargetDateStr() {
		return targetDateStr;
	}
	public void setTargetDateStr(String targetDateStr) {
		this.targetDateStr = targetDateStr;
	}
	public String getTanggalPemenuhanStr() {
		return tanggalPemenuhanStr;
	}
	public void setTanggalPemenuhanStr(String tanggalPemenuhanStr) {
		this.tanggalPemenuhanStr = tanggalPemenuhanStr;
	}
	public String getTanggalKonfirmasiStr() {
		return tanggalKonfirmasiStr;
	}
	public void setTanggalKonfirmasiStr(String tanggalKonfirmasiStr) {
		this.tanggalKonfirmasiStr = tanggalKonfirmasiStr;
	}
	public String getCreationDateStr() {
		return creationDateStr;
	}
	public void setCreationDateStr(String creationDateStr) {
		this.creationDateStr = creationDateStr;
	}
	public String getSla() {
		return Sla;
	}
	public void setSla(String sla) {
		Sla = sla;
	}
	
}
