package com.wo.module.report.reportSuratMasukAmlDetail.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Locale;

import javax.faces.context.FacesContext;

import com.wo.module.common.model.BaseEntity;

public class ReportSuratMasukAmlDetail extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 2533282398950873098L;
	
	private String jenisPeraturanIn;
	private String jenisPeraturanEn;
	private String tipeSuratIn;
	private String tipeSuratEn;
	private Date tanggalTerimaSurat;
	private String noSurat;
	private Date tanggalSurat;
	private String perihalIn;
	private String perihalEn;
	private String ringkasanSurat;
	private String tindakLanjut;
	private Date targetDate;
	private String statusIn;
	private String statusEn;
	private String pic1;
	private String pic2;
	private String pic3;
	private String divisi;
	private String picCompliance;
	private String buktiKonfirmasi;
	private Date tanggalKonfirmasi;
	private Date tanggalTindakLanjut;
	private String keterangan;
	private String sla;
	private Date tanggalFilter;
	private String senderCode;
	
	// helper
	private String namaJenisPeraturan;
	private String namaTipeSurat;
	private String namaPerihal;
	private String tanggalSuratStr;
	private String tanggalTerimaSuratStr;
	private String targetDateStr;
	private String tanggalKonfirmasiStr;
	private String tanggalTindakLanjutStr;
	private String tanggalFilterStr;
	
	public String getJenisPeraturanIn() {
		return jenisPeraturanIn;
	}
	public void setJenisPeraturanIn(String jenisPeraturanIn) {
		this.jenisPeraturanIn = jenisPeraturanIn;
	}
	public String getJenisPeraturanEn() {
		return jenisPeraturanEn;
	}
	public void setJenisPeraturanEn(String jenisPeraturanEn) {
		this.jenisPeraturanEn = jenisPeraturanEn;
	}
	public String getTipeSuratIn() {
		return tipeSuratIn;
	}
	public void setTipeSuratIn(String tipeSuratIn) {
		this.tipeSuratIn = tipeSuratIn;
	}
	public String getTipeSuratEn() {
		return tipeSuratEn;
	}
	public void setTipeSuratEn(String tipeSuratEn) {
		this.tipeSuratEn = tipeSuratEn;
	}
	public Date getTanggalTerimaSurat() {
		return tanggalTerimaSurat;
	}
	public void setTanggalTerimaSurat(Date tanggalTerimaSurat) {
		this.tanggalTerimaSurat = tanggalTerimaSurat;
	}
	public String getNoSurat() {
		return noSurat;
	}
	public void setNoSurat(String noSurat) {
		this.noSurat = noSurat;
	}
	public Date getTanggalSurat() {
		return tanggalSurat;
	}
	public void setTanggalSurat(Date tanggalSurat) {
		this.tanggalSurat = tanggalSurat;
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
	public String getRingkasanSurat() {
		return ringkasanSurat;
	}
	public void setRingkasanSurat(String ringkasanSurat) {
		this.ringkasanSurat = ringkasanSurat;
	}
	public String getTindakLanjut() {
		return tindakLanjut;
	}
	public void setTindakLanjut(String tindakLanjut) {
		this.tindakLanjut = tindakLanjut;
	}
	public Date getTargetDate() {
		return targetDate;
	}
	public void setTargetDate(Date targetDate) {
		this.targetDate = targetDate;
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
	public String getDivisi() {
		return divisi;
	}
	public void setDivisi(String divisi) {
		this.divisi = divisi;
	}
	public String getPicCompliance() {
		return picCompliance;
	}
	public void setPicCompliance(String picCompliance) {
		this.picCompliance = picCompliance;
	}
	public String getBuktiKonfirmasi() {
		return buktiKonfirmasi;
	}
	public void setBuktiKonfirmasi(String buktiKonfirmasi) {
		this.buktiKonfirmasi = buktiKonfirmasi;
	}
	public Date getTanggalKonfirmasi() {
		return tanggalKonfirmasi;
	}
	public void setTanggalKonfirmasi(Date tanggalKonfirmasi) {
		this.tanggalKonfirmasi = tanggalKonfirmasi;
	}
	public Date getTanggalTindakLanjut() {
		return tanggalTindakLanjut;
	}
	public void setTanggalTindakLanjut(Date tanggalTindakLanjut) {
		this.tanggalTindakLanjut = tanggalTindakLanjut;
	}
	public String getKeterangan() {
		return keterangan;
	}
	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}
	public String getSla() {
		return sla;
	}
	public void setSla(String sla) {
		this.sla = sla;
	}
	public Date getTanggalFilter() {
		return tanggalFilter;
	}
	public void setTanggalFilter(Date tanggalFilter) {
		this.tanggalFilter = tanggalFilter;
	}
	public String getSenderCode() {
		return senderCode;
	}
	public void setSenderCode(String senderCode) {
		this.senderCode = senderCode;
	}
	@SuppressWarnings("static-access")
	public String getNamaJenisPeraturan() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		
		if(locale != null && locale.equals(locale.ENGLISH)) {
			namaJenisPeraturan = jenisPeraturanEn;
		} else {
			namaJenisPeraturan = jenisPeraturanIn;
		}
		
		return namaJenisPeraturan;
	}
	public void setNamaJenisPeraturan(String namaJenisPeraturan) {
		this.namaJenisPeraturan = namaJenisPeraturan;
	}
	@SuppressWarnings("static-access")
	public String getNamaTipeSurat() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		
		if(locale != null && locale.equals(locale.ENGLISH)) {
			namaTipeSurat = tipeSuratEn;
		} else {
			namaTipeSurat = tipeSuratIn;
		}
		
		return namaTipeSurat;
	}
	public void setNamaTipeSurat(String namaTipeSurat) {
		this.namaTipeSurat = namaTipeSurat;
	}
	@SuppressWarnings("static-access")
	public String getNamaPerihal() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		
		if(locale != null && locale.equals(locale.ENGLISH)) {
			namaPerihal = perihalEn;
		} else {
			namaPerihal = perihalIn;
		}
		return namaPerihal;
	}
	public void setNamaPerihal(String namaPerihal) {
		this.namaPerihal = namaPerihal;
	}
	public String getTanggalSuratStr() {
		return tanggalSuratStr;
	}
	public void setTanggalSuratStr(String tanggalSuratStr) {
		this.tanggalSuratStr = tanggalSuratStr;
	}
	public String getTanggalTerimaSuratStr() {
		return tanggalTerimaSuratStr;
	}
	public void setTanggalTerimaSuratStr(String tanggalTerimaSuratStr) {
		this.tanggalTerimaSuratStr = tanggalTerimaSuratStr;
	}
	public String getTargetDateStr() {
		return targetDateStr;
	}
	public void setTargetDateStr(String targetDateStr) {
		this.targetDateStr = targetDateStr;
	}
	public String getTanggalKonfirmasiStr() {
		return tanggalKonfirmasiStr;
	}
	public void setTanggalKonfirmasiStr(String tanggalKonfirmasiStr) {
		this.tanggalKonfirmasiStr = tanggalKonfirmasiStr;
	}
	public String getTanggalTindakLanjutStr() {
		return tanggalTindakLanjutStr;
	}
	public void setTanggalTindakLanjutStr(String tanggalTindakLanjutStr) {
		this.tanggalTindakLanjutStr = tanggalTindakLanjutStr;
	}
	public String getTanggalFilterStr() {
		return tanggalFilterStr;
	}
	public void setTanggalFilterStr(String tanggalFilterStr) {
		this.tanggalFilterStr = tanggalFilterStr;
	}
}
