package com.wo.module.report.reportRmdRekap.model;

import java.io.Serializable;
import java.util.Locale;

import javax.faces.context.FacesContext;

import com.wo.module.common.model.BaseEntity;

public class ReportRmdRekap extends BaseEntity implements Serializable{

	private static final long serialVersionUID = -8551867117934077720L;

	private String jenisPeraturanIn;
	private String jenisPeraturanEn;
	private String pengirimSuratIn;
	private String pengirimSuratEn;
	private Integer bulanan;
	private Integer tahunan;
	private Integer triwulanan;
	private Integer semester;
	private Integer insidentil;
	private Integer mingguan;
	private Integer harian;
	private Integer jumlahLaporan;
	private Integer inProgress;
	private Integer closed;
	private Integer meetSla;
	private Integer beforeSla;
	private Integer overSla;
	
	// helper
	private String namaJenisPeraturan;
	private String namaPengirimSurat;
	
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
	public String getPengirimSuratIn() {
		return pengirimSuratIn;
	}
	public void setPengirimSuratIn(String pengirimSuratIn) {
		this.pengirimSuratIn = pengirimSuratIn;
	}
	public String getPengirimSuratEn() {
		return pengirimSuratEn;
	}
	public void setPengirimSuratEn(String pengirimSuratEn) {
		this.pengirimSuratEn = pengirimSuratEn;
	}
	public Integer getBulanan() {
		return bulanan;
	}
	public void setBulanan(Integer bulanan) {
		this.bulanan = bulanan;
	}
	public Integer getTahunan() {
		return tahunan;
	}
	public void setTahunan(Integer tahunan) {
		this.tahunan = tahunan;
	}
	public Integer getTriwulanan() {
		return triwulanan;
	}
	public void setTriwulanan(Integer triwulanan) {
		this.triwulanan = triwulanan;
	}
	public Integer getSemester() {
		return semester;
	}
	public void setSemester(Integer semester) {
		this.semester = semester;
	}
	public Integer getInsidentil() {
		return insidentil;
	}
	public void setInsidentil(Integer insidentil) {
		this.insidentil = insidentil;
	}
	public Integer getMingguan() {
		return mingguan;
	}
	public void setMingguan(Integer mingguan) {
		this.mingguan = mingguan;
	}
	public Integer getHarian() {
		return harian;
	}
	public void setHarian(Integer harian) {
		this.harian = harian;
	}
	public Integer getJumlahLaporan() {
		return jumlahLaporan;
	}
	public void setJumlahLaporan(Integer jumlahLaporan) {
		this.jumlahLaporan = jumlahLaporan;
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
	public String getNamaPengirimSurat() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		
		if (locale != null && locale.equals(locale.ENGLISH)) {
			namaPengirimSurat = pengirimSuratEn;
		} else {
			namaPengirimSurat = pengirimSuratIn;
		}
		
		return namaPengirimSurat;
	}
	public void setNamaPengirimSurat(String namaPengirimSurat) {
		this.namaPengirimSurat = namaPengirimSurat;
	}
	
}
