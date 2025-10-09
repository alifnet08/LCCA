package com.wo.module.report.reportRegulationMonitoring.vo;

import java.io.Serializable;

public class ReportRegulationMonitoringRekapVo implements Serializable{

	private static final long serialVersionUID = 3624525812882737567L;
	
	private String jenisPeraturan;
	private String jenisPeraturanIn;
	private String jenisPeraturanEn;
	private String kategoriDokumen;
	private String kategoriDokumenIn;
	private String kategoriDokumenEn;
	private Integer totalRegulationMonitoring;
	private Integer tindakLanjutYes;
	private Integer tindakLanjutNo;
	private Integer statusTindakLanjutInProgress;
	private Integer statusTindakLanjutClosed;
	private Integer meetSla;
	private Integer beforeSla;
	private Integer overSla;
	
	public String getJenisPeraturan() {
		return jenisPeraturan;
	}
	public void setJenisPeraturan(String jenisPeraturan) {
		this.jenisPeraturan = jenisPeraturan;
	}
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
	public String getKategoriDokumen() {
		return kategoriDokumen;
	}
	public void setKategoriDokumen(String kategoriDokumen) {
		this.kategoriDokumen = kategoriDokumen;
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
	public Integer getTotalRegulationMonitoring() {
		return totalRegulationMonitoring;
	}
	public void setTotalRegulationMonitoring(Integer totalRegulationMonitoring) {
		this.totalRegulationMonitoring = totalRegulationMonitoring;
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
	

}
