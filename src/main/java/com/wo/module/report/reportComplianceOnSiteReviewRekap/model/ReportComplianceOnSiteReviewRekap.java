package com.wo.module.report.reportComplianceOnSiteReviewRekap.model;

import java.util.Locale;

import javax.faces.context.FacesContext;

public class ReportComplianceOnSiteReviewRekap {
	
	private String kategoriNameIn;
	private String kategoriNameEn;
	private Integer totalReview;
	private Integer tindakLanjutYes;
	private Integer tindakLanjutNo;
	private Integer inProgress;
	private Integer closed;
	private Integer meetSla;
	private Integer beforeSla;
	private Integer overSla;
	
	// helper
	private String kategoriName;

	public String getKategoriNameIn() {
		return kategoriNameIn;
	}

	public void setKategoriNameIn(String kategoriNameIn) {
		this.kategoriNameIn = kategoriNameIn;
	}

	public String getKategoriNameEn() {
		return kategoriNameEn;
	}

	public void setKategoriNameEn(String kategoriNameEn) {
		this.kategoriNameEn = kategoriNameEn;
	}

	public Integer getTotalReview() {
		return totalReview;
	}

	public void setTotalReview(Integer totalReview) {
		this.totalReview = totalReview;
	}

	public Integer getTindakLanjutYes() {
		return tindakLanjutYes;
	}

	public void setTindakLanjutYes(Integer tindakLanjutYes) {
		this.tindakLanjutYes = tindakLanjutYes;
	}

	public Integer getTindakLanjutNo() {
		return tindakLanjutNo;
	}

	public void setTindakLanjutNo(Integer tindakLanjutNo) {
		this.tindakLanjutNo = tindakLanjutNo;
	}

	public Integer getInProgress() {
		return inProgress;
	}

	public void setInProgress(Integer inProgress) {
		this.inProgress = inProgress;
	}

	public Integer getClosed() {
		return closed;
	}

	public void setClosed(Integer closed) {
		this.closed = closed;
	}

	public Integer getMeetSla() {
		return meetSla;
	}

	public void setMeetSla(Integer meetSla) {
		this.meetSla = meetSla;
	}

	public Integer getBeforeSla() {
		return beforeSla;
	}

	public void setBeforeSla(Integer beforeSla) {
		this.beforeSla = beforeSla;
	}

	public Integer getOverSla() {
		return overSla;
	}

	public void setOverSla(Integer overSla) {
		this.overSla = overSla;
	}

	@SuppressWarnings("static-access")
	public String getKategoriName() {
		
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		
		if (locale != null && locale.equals(locale.ENGLISH)) {
			kategoriName = kategoriNameEn;
		} else {
			kategoriName = kategoriNameIn;
		}
		
		return kategoriName;
	}

	public void setKategoriName(String kategoriName) {
		this.kategoriName = kategoriName;
	}
}
