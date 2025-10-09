package com.wo.module.report.reportSuratMasukRekap.model;

import com.wo.module.common.model.BaseEntity;

public class ReportSuratMasukRekap extends BaseEntity{

	private String pengirimSuratIn;
	private String pengirimSuratEn;
	private Integer undangan;
	private Integer nonUndangan;
	private Integer totalSuratMasuk;
	private Integer tindakLanjutYes;
	private Integer tindakLanjutNo;
	private Integer inProgress;
	private Integer closed;
	private Integer meetSla;
	private Integer beforeSla;
	private Integer overSla;
	
	
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
	public Integer getTotalSuratMasuk() {
		return totalSuratMasuk;
	}
	public void setTotalSuratMasuk(Integer totalSuratMasuk) {
		this.totalSuratMasuk = totalSuratMasuk;
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
	public Integer getUndangan() {
		return undangan;
	}
	public void setUndangan(Integer undangan) {
		this.undangan = undangan;
	}
	public Integer getNonUndangan() {
		return nonUndangan;
	}
	public void setNonUndangan(Integer nonUndangan) {
		this.nonUndangan = nonUndangan;
	}
}
