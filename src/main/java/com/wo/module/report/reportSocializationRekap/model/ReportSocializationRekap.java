package com.wo.module.report.reportSocializationRekap.model;

import java.io.Serializable;
import java.util.Locale;

import javax.faces.context.FacesContext;
	
import com.wo.module.common.model.BaseEntity;

public class ReportSocializationRekap extends BaseEntity implements Serializable{
	
	private static final long serialVersionUID = -8930004446952473596L;
	
	private String jenisPeraturanIn;
	private String jenisPeraturanEn;
	private String kategoriDokumenIn;
	private String kategoriDokumenEn;
	private Integer totalSosialisasi;
	private	Integer tindakLanjutYes;
	private Integer tindakLanjutNo;
	private Integer statusTindakLanjutInProgress;
	private Integer statusTindakLanjutClosed;
	private Integer meetSla;
	private Integer beforeSla;
	private Integer overSla;
	
	// helper
	private String namaJenisPeraturan;
	private String namaKategoriDokumen;
	
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
	public String getKategoriDokumenIn() {
		return kategoriDokumenIn;
	}
	public void setKategoriDokumenIn(String kategoriDokumenIn) {
		this.kategoriDokumenIn = kategoriDokumenIn;
	}
	public String getKategoriDokumenEn() {
		return kategoriDokumenEn;
	}
	public void setKategoriDokumenEn(String kategoriDokumenEn) {
		this.kategoriDokumenEn = kategoriDokumenEn;
	}
	public Integer getTotalSosialisasi() {
		return totalSosialisasi;
	}
	public void setTotalSosialisasi(Integer totalSosialisasi) {
		this.totalSosialisasi = totalSosialisasi;
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
	public Integer getStatusTindakLanjutInProgress() {
		return statusTindakLanjutInProgress;
	}
	public void setStatusTindakLanjutInProgress(Integer statusTindakLanjutInProgress) {
		this.statusTindakLanjutInProgress = statusTindakLanjutInProgress;
	}
	public Integer getStatusTindakLanjutClosed() {
		return statusTindakLanjutClosed;
	}
	public void setStatusTindakLanjutClosed(Integer statusTindakLanjutClosed) {
		this.statusTindakLanjutClosed = statusTindakLanjutClosed;
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
	public String getNamaJenisPeraturan() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		
		if (locale != null && locale.equals(locale.ENGLISH)) {
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
	public String getNamaKategoriDokumen() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		
		if (locale != null && locale.equals(locale.ENGLISH)) {
			namaKategoriDokumen = kategoriDokumenEn;
		} else {
			namaKategoriDokumen = kategoriDokumenIn;
		}
		
		return namaKategoriDokumen;
	}
	public void setNamaKategoriDokumen(String namaKategoriDokumen) {
		this.namaKategoriDokumen = namaKategoriDokumen;
	}
	
	
}