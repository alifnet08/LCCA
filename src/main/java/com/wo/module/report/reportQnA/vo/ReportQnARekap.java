package com.wo.module.report.reportQnA.vo;

import java.io.Serializable;

public class ReportQnARekap implements Serializable{

	private static final long serialVersionUID = -7090063256867700471L;

	private String kategoriIn;
	private String kategoriEn;
	private Long meetSla;
	private Long overSla;
	private Long overDue;
	private Long beforeSla;
	
	public String getKategoriIn() {
		return kategoriIn;
	}
	public void setKategoriIn(String kategoriIn) {
		this.kategoriIn = kategoriIn;
	}
	public String getKategoriEn() {
		return kategoriEn;
	}
	public void setKategoriEn(String kategoriEn) {
		this.kategoriEn = kategoriEn;
	}
	public Long getMeetSla() {
		return meetSla;
	}
	public void setMeetSla(Long meetSla) {
		this.meetSla = meetSla;
	}
	public Long getOverSla() {
		return overSla;
	}
	public void setOverSla(Long overSla) {
		this.overSla = overSla;
	}
	public Long getOverDue() {
		return overDue;
	}
	public void setOverDue(Long overDue) {
		this.overDue = overDue;
	}
	public Long getBeforeSla() {
		return beforeSla;
	}
	public void setBeforeSla(Long beforeSla) {
		this.beforeSla = beforeSla;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
