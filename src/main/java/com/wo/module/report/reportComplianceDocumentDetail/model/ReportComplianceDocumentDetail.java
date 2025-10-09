package com.wo.module.report.reportComplianceDocumentDetail.model;

import java.util.Date;
import java.util.Locale;

import javax.faces.context.FacesContext;

public class ReportComplianceDocumentDetail {

	private String tipeDokumenIn;
	private String tipeDokumenEn;
	private String noDokumen;
	private Date tanggalDokumen;
	private Date tanggalDiterima;
	private String materi;
	private String kelompokHukIn;
	private String kelompokHukEn;
	private String unitKerjaPengusul;
	private String picCompliance;
	private String keterangan;
	private String attachmentDokumen;
	private Date creationDate;
	private String documentType;
	
	// helper
	private String namaTipeDokumen;
	private String tanggalDokumenStr;
	private String tanggalDiterimaStr;
	private String creationDateStr;
	
	
	public String getTipeDokumenIn() {
		return tipeDokumenIn;
	}
	public void setTipeDokumenIn(String tipeDokumenIn) {
		this.tipeDokumenIn = tipeDokumenIn;
	}
	public String getTipeDokumenEn() {
		return tipeDokumenEn;
	}
	public void setTipeDokumenEn(String tipeDokumenEn) {
		this.tipeDokumenEn = tipeDokumenEn;
	}
	public String getNoDokumen() {
		return noDokumen;
	}
	public void setNoDokumen(String noDokumen) {
		this.noDokumen = noDokumen;
	}
	public Date getTanggalDokumen() {
		return tanggalDokumen;
	}
	public void setTanggalDokumen(Date tanggalDokumen) {
		this.tanggalDokumen = tanggalDokumen;
	}
	public Date getTanggalDiterima() {
		return tanggalDiterima;
	}
	public void setTanggalDiterima(Date tanggalDiterima) {
		this.tanggalDiterima = tanggalDiterima;
	}
	public String getMateri() {
		return materi;
	}
	public void setMateri(String materi) {
		this.materi = materi;
	}
	public String getKelompokHukIn() {
		return kelompokHukIn;
	}
	public void setKelompokHukIn(String kelompokHukIn) {
		this.kelompokHukIn = kelompokHukIn;
	}
	public String getKelompokHukEn() {
		return kelompokHukEn;
	}
	public void setKelompokHukEn(String kelompokHukEn) {
		this.kelompokHukEn = kelompokHukEn;
	}
	public String getUnitKerjaPengusul() {
		return unitKerjaPengusul;
	}
	public void setUnitKerjaPengusul(String unitKerjaPengusul) {
		this.unitKerjaPengusul = unitKerjaPengusul;
	}
	public String getPicCompliance() {
		return picCompliance;
	}
	public void setPicCompliance(String picCompliance) {
		this.picCompliance = picCompliance;
	}
	public String getKeterangan() {
		return keterangan;
	}
	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
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
	public String getDocumentType() {
		return documentType;
	}
	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}
	@SuppressWarnings("static-access")
	public String getNamaTipeDokumen() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		
		if (locale != null && locale.equals(locale.ENGLISH)) {
			namaTipeDokumen = tipeDokumenEn;
		} else {
			namaTipeDokumen = tipeDokumenIn;
		}
		
		return namaTipeDokumen;
	}
	public void setNamaTipeDokumen(String namaTipeDokumen) {
		this.namaTipeDokumen = namaTipeDokumen;
	}
	public String getTanggalDokumenStr() {
		return tanggalDokumenStr;
	}
	public void setTanggalDokumenStr(String tanggalDokumenStr) {
		this.tanggalDokumenStr = tanggalDokumenStr;
	}
	public String getTanggalDiterimaStr() {
		return tanggalDiterimaStr;
	}
	public void setTanggalDiterimaStr(String tanggalDiterimaStr) {
		this.tanggalDiterimaStr = tanggalDiterimaStr;
	}
	public String getCreationDateStr() {
		return creationDateStr;
	}
	public void setCreationDateStr(String creationDateStr) {
		this.creationDateStr = creationDateStr;
	}
}
