package com.wo.module.litigationView.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.Locale;

import javax.faces.context.FacesContext;

public class LitigationViewDetailVo implements Serializable{
	
	private static final long serialVersionUID = 6222799984612524696L;
	
	private Long litigationDetailId;
	private String courtType;
	private String courtTypeCode;
	private String courtTypeIn;
	private String courtTypeEn;
	private String courtName;
	private String courtNameCode;
	private String courtNameIn;
	private String courtNameEn;
	private String progress;
	private String putusan;
	private String putusanCode;
	private String putusanIn;
	private String putusanEn;
	private String tanggalPutusan;
	private String upayaHukum;
	private String upayaHukumCode;
	private String upayaHukumIn;
	private String upayaHukumEn;
	private Date courtDate;
	private String note;
	
	// helper
	private String courtDateStr;
	
	
	private String keterangan;
	
	@SuppressWarnings("static-access")
	public String getCourtType() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		
		if (locale != null && locale.equals(locale.ENGLISH)) {
			courtType = courtTypeEn;
		} else {
			courtType = courtTypeIn;
		}
		
		return courtType;
	}
	public void setCourtType(String courtType) {
		this.courtType = courtType;
	}
	@SuppressWarnings("static-access")
	public String getCourtName() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		
		if (locale != null && locale.equals(locale.ENGLISH)) {
			courtName = courtNameEn;
		} else {
			courtName = courtNameIn;
		}
		
		return courtName;
	}
	public void setCourtName(String courtName) {
		this.courtName = courtName;
	}
	public String getProgress() {
		return progress;
	}
	public void setProgress(String progress) {
		this.progress = progress;
	}
	@SuppressWarnings("static-access")
	public String getPutusan() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		
		if (locale != null && locale.equals(locale.ENGLISH)) {
			putusan = putusanEn;
		} else {
			putusan = putusanIn;
		}
		
		return putusan;
	}
	public void setPutusan(String putusan) {
		this.putusan = putusan;
	}
	public String getTanggalPutusan() {
		return tanggalPutusan;
	}
	public void setTanggalPutusan(String tanggalPutusan) {
		this.tanggalPutusan = tanggalPutusan;
	}
	@SuppressWarnings("static-access")
	public String getUpayaHukum() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		
		if (locale != null && locale.equals(locale.ENGLISH)) {
			upayaHukum = upayaHukumEn;
		} else {
			upayaHukum = upayaHukumIn;
		}
		
		return upayaHukum;
	}
	public void setUpayaHukum(String upayaHukum) {
		this.upayaHukum = upayaHukum;
	}
	public String getKeterangan() {
		return keterangan;
	}
	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}
	public Long getLitigationDetailId() {
		return litigationDetailId;
	}
	public void setLitigationDetailId(Long litigationDetailId) {
		this.litigationDetailId = litigationDetailId;
	}
	public String getCourtTypeCode() {
		return courtTypeCode;
	}
	public void setCourtTypeCode(String courtTypeCode) {
		this.courtTypeCode = courtTypeCode;
	}
	public String getCourtTypeIn() {
		return courtTypeIn;
	}
	public void setCourtTypeIn(String courtTypeIn) {
		this.courtTypeIn = courtTypeIn;
	}
	
	public String getCourtTypeEn() {
		return courtTypeEn;
	}
	public void setCourtTypeEn(String courtTypeEn) {
		this.courtTypeEn = courtTypeEn;
	}
	public String getCourtNameCode() {
		return courtNameCode;
	}
	public void setCourtNameCode(String courtNameCode) {
		this.courtNameCode = courtNameCode;
	}
	public String getCourtNameIn() {
		return courtNameIn;
	}
	public void setCourtNameIn(String courtNameIn) {
		this.courtNameIn = courtNameIn;
	}
	public String getCourtNameEn() {
		return courtNameEn;
	}
	public void setCourtNameEn(String courtNameEn) {
		this.courtNameEn = courtNameEn;
	}
	public String getUpayaHukumCode() {
		return upayaHukumCode;
	}
	public void setUpayaHukumCode(String upayaHukumCode) {
		this.upayaHukumCode = upayaHukumCode;
	}
	public String getUpayaHukumIn() {
		return upayaHukumIn;
	}
	public void setUpayaHukumIn(String upayaHukumIn) {
		this.upayaHukumIn = upayaHukumIn;
	}
	public String getUpayaHukumEn() {
		return upayaHukumEn;
	}
	public void setUpayaHukumEn(String upayaHukumEn) {
		this.upayaHukumEn = upayaHukumEn;
	}
	public Date getCourtDate() {
		return courtDate;
	}
	public void setCourtDate(Date courtDate) {
		this.courtDate = courtDate;
	}
	public String getCourtDateStr() {
		return courtDateStr;
	}
	public void setCourtDateStr(String courtDateStr) {
		this.courtDateStr = courtDateStr;
	}
	public String getPutusanCode() {
		return putusanCode;
	}
	public void setPutusanCode(String putusanCode) {
		this.putusanCode = putusanCode;
	}
	public String getPutusanIn() {
		return putusanIn;
	}
	public void setPutusanIn(String putusanIn) {
		this.putusanIn = putusanIn;
	}
	public String getPutusanEn() {
		return putusanEn;
	}
	public void setPutusanEn(String putusanEn) {
		this.putusanEn = putusanEn;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	

}
