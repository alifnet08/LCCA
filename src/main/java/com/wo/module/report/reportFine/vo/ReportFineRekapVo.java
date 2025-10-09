package com.wo.module.report.reportFine.vo;

import java.io.Serializable;

public class ReportFineRekapVo implements Serializable{

	private static final long serialVersionUID = -1104947250297201833L;

	private String pengirimSuratIn;
	private String pengirimSuratEn;
	private Integer tipeUndangan;
	private Integer totalDenda;
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
	public Integer getTotalDenda() {
		return totalDenda;
	}
	public void setTotalDenda(Integer totalDenda) {
		this.totalDenda = totalDenda;
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
	public Integer getTipeUndangan() {
		return tipeUndangan;
	}
	public void setTipeUndangan(Integer tipeUndangan) {
		this.tipeUndangan = tipeUndangan;
	}
	
}
