package com.wo.module.report.reportOutgoingLetterRekap.model;

import java.util.Date;

public class ReportOutgoingLetterRekap {

	private String tujuanSuratIn;
	private String tujuanSuratEn;
	private Date tanggalSurat;
	private Integer total;
	
	// helper
	private String tanggalSuratStr;
	
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
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
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
}
