package com.wo.module.report.reportOutgoingLetterDetail.model;

import java.util.Date;
import java.util.Locale;

import javax.faces.context.FacesContext;

public class ReportOutgoingLetterDetail {

	private String tujuanSuratIn;
	private String tujuanSuratEn;
	private String noSurat;
	private Date tanggalSurat;
	private String perihalIn;
	private String perihalEn;
	private String disampaikanKepada;
	private String tembusanSurat;
	private String attachmentDokumen;
	private Date creationDate;
	
	// helper
	private String namaTujuanSurat;
	private String namaPerihal;
	private String creationDateStr;
	private String tanggalSuratStr;
	private String namaPembuatSurat;
	
	public String getTujuanSuratIn() {
		return tujuanSuratIn;
	}
	public void setTujuanSuratIn(String tujuanSuratIn) {
		this.tujuanSuratIn = tujuanSuratIn;
	}
	public String getTujuanSuratEn() {
		return tujuanSuratEn;
	}
	public void setTujuanSuratEn(String tujuanSuratEn) {
		this.tujuanSuratEn = tujuanSuratEn;
	}
	public String getNoSurat() {
		return noSurat;
	}
	public void setNoSurat(String noSurat) {
		this.noSurat = noSurat;
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
	public String getDisampaikanKepada() {
		return disampaikanKepada;
	}
	public void setDisampaikanKepada(String disampaikanKepada) {
		this.disampaikanKepada = disampaikanKepada;
	}
	public String getTembusanSurat() {
		return tembusanSurat;
	}
	public void setTembusanSurat(String tembusanSurat) {
		this.tembusanSurat = tembusanSurat;
	}
	public String getAttachmentDokumen() {
		return attachmentDokumen;
	}
	public void setAttachmentDokumen(String attachmentDokumen) {
		this.attachmentDokumen = attachmentDokumen;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	@SuppressWarnings("static-access")
	public String getNamaTujuanSurat() {
		
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		
		if (locale != null && locale.equals(locale.ENGLISH)) {
			namaTujuanSurat = tujuanSuratEn;
		} else {
			namaTujuanSurat = tujuanSuratIn;
		}
		
		return namaTujuanSurat;
	}
	public void setNamaTujuanSurat(String namaTujuanSurat) {
		this.namaTujuanSurat = namaTujuanSurat;
	}
	@SuppressWarnings("static-access")
	public String getNamaPerihal() {
		
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		
		if (locale != null && locale.equals(locale.ENGLISH)) {
			namaPerihal = perihalEn;
		} else {
			namaPerihal = perihalIn;
		}
		
		return namaPerihal;
	}
	public void setNamaPerihal(String namaPerihal) {
		this.namaPerihal = namaPerihal;
	}
	public String getCreationDateStr() {
		return creationDateStr;
	}
	public void setCreationDateStr(String creationDateStr) {
		this.creationDateStr = creationDateStr;
	}
	public Date getTanggalSurat() {
		return tanggalSurat;
	}
	public void setTanggalSurat(Date tanggalSurat) {
		this.tanggalSurat = tanggalSurat;
	}
	public String getTanggalSuratStr() {
		return tanggalSuratStr;
	}
	public void setTanggalSuratStr(String tanggalSuratStr) {
		this.tanggalSuratStr = tanggalSuratStr;
	}
	public String getNamaPembuatSurat() {
		return namaPembuatSurat;
	}
	public void setNamaPembuatSurat(String namaPembuatSurat) {
		this.namaPembuatSurat = namaPembuatSurat;
	}
	
}
