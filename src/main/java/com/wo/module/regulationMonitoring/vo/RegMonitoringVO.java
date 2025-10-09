package com.wo.module.regulationMonitoring.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;

public class RegMonitoringVO  implements Serializable {
	private static final long serialVersionUID = 6395152164269025885L;
	
	private Long regMonitoringId;
	private String jenisKetentuan;
	private String jenisKetentuanIn;
	private String jenisKetentuanEn;
	private String tipeDokumen;
	private String tipeDokumenIn;
	private String tipeDokumenEn;
	private String kategori;
	private String kategoriIn;
	private String kategoriEn;
	private String topik;
	private String topikIn;
	private String topikEn;
	private String noDokumen;
	private String jdlPeraturan;
	private String jdlPeraturanIn;
	private String jdlPeraturanEn;
	private String namaPIC;
	private String statusCd;
	private String status;
	private String statusIn;
	private String statusEn;
	private String statusTindakLanjut;
	private String picKonfirmasi;
	private String tglKonfirmasi;
	private String tglTindakLanjut;
	private String followupStatusCd;
	
	private Date pubDateFrom;
	private Date pubDateEnd;
	private Date effDateFrom;
	private Date effDateEnd;
	private Date targetDateFrom;
	private Date targetDateEnd;
	
	private String publishedDateStr;
	private String effectiveDateStr;
	
	private List<StatusConfirmationVO> statusList;
	
	@SuppressWarnings("static-access")
	public String getJenisKetentuan() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if (locale != null && locale.equals(locale.ENGLISH)) {
			jenisKetentuan = jenisKetentuanEn;
		} else {
			jenisKetentuan = jenisKetentuanIn;
		}
		return jenisKetentuan;
	}
	public void setJenisKetentuan(String jenisKetentuan) {
		this.jenisKetentuan = jenisKetentuan;
	}
	@SuppressWarnings("static-access")
	public String getTipeDokumen() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if (locale != null && locale.equals(locale.ENGLISH)) {
			tipeDokumen = tipeDokumenEn;
		} else {
			tipeDokumen = tipeDokumenIn;
		}
		return tipeDokumen;
	}
	public void setTipeDokumen(String tipeDokumen) {
		this.tipeDokumen = tipeDokumen;
	}
	@SuppressWarnings("static-access")
	public String getKategori() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if (locale != null && locale.equals(locale.ENGLISH)) {
			kategori = kategoriEn;
		} else {
			kategori = kategoriIn;
		}
		return kategori;
	}
	public void setKategori(String kategori) {
		this.kategori = kategori;
	}
	@SuppressWarnings("static-access")
	public String getTopik() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if (locale != null && locale.equals(locale.ENGLISH)) {
			topik = topikEn;
		} else {
			topik = topikIn;
		}
		return topik;
	}
	public void setTopik(String topik) {
		this.topik = topik;
	}
	public String getNoDokumen() {
		return noDokumen;
	}
	public void setNoDokumen(String noDokumen) {
		this.noDokumen = noDokumen;
	}
	@SuppressWarnings("static-access")
	public String getJdlPeraturan() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if (locale != null && locale.equals(locale.ENGLISH)) {
			jdlPeraturan = jdlPeraturanEn;
		} else {
			jdlPeraturan = jdlPeraturanIn;
		}
		return jdlPeraturan;
	}
	public void setJdlPeraturan(String jdlPeraturan) {
		this.jdlPeraturan = jdlPeraturan;
	}
	
	public String getNamaPIC() {
		return namaPIC;
	}
	public void setNamaPIC(String namaPIC) {
		this.namaPIC = namaPIC;
	}
	@SuppressWarnings("static-access")
	public String getStatus() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if (locale != null && locale.equals(locale.ENGLISH)) {
			status = statusEn;
		} else {
			status = statusIn;
		}
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatusTindakLanjut() {
		return statusTindakLanjut;
	}
	public void setStatusTindakLanjut(String statusTindakLanjut) {
		this.statusTindakLanjut = statusTindakLanjut;
	}
	public String getPicKonfirmasi() {
		return picKonfirmasi;
	}
	public void setPicKonfirmasi(String picKonfirmasi) {
		this.picKonfirmasi = picKonfirmasi;
	}
	public String getTglKonfirmasi() {
		return tglKonfirmasi;
	}
	public void setTglKonfirmasi(String tglKonfirmasi) {
		this.tglKonfirmasi = tglKonfirmasi;
	}
	public String getTglTindakLanjut() {
		return tglTindakLanjut;
	}
	public void setTglTindakLanjut(String tglTindakLanjut) {
		this.tglTindakLanjut = tglTindakLanjut;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getJenisKetentuanIn() {
		return jenisKetentuanIn;
	}
	public void setJenisKetentuanIn(String jenisKetentuanIn) {
		this.jenisKetentuanIn = jenisKetentuanIn;
	}
	public String getJenisKetentuanEn() {
		return jenisKetentuanEn;
	}
	public void setJenisKetentuanEn(String jenisKetentuanEn) {
		this.jenisKetentuanEn = jenisKetentuanEn;
	}
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
	public String getTopikIn() {
		return topikIn;
	}
	public void setTopikIn(String topikIn) {
		this.topikIn = topikIn;
	}
	public String getTopikEn() {
		return topikEn;
	}
	public void setTopikEn(String topikEn) {
		this.topikEn = topikEn;
	}
	public String getJdlPeraturanIn() {
		return jdlPeraturanIn;
	}
	public void setJdlPeraturanIn(String jdlPeraturanIn) {
		this.jdlPeraturanIn = jdlPeraturanIn;
	}
	public String getJdlPeraturanEn() {
		return jdlPeraturanEn;
	}
	public void setJdlPeraturanEn(String jdlPeraturanEn) {
		this.jdlPeraturanEn = jdlPeraturanEn;
	}
	public Long getRegMonitoringId() {
		return regMonitoringId;
	}
	public void setRegMonitoringId(Long regMonitoringId) {
		this.regMonitoringId = regMonitoringId;
	}
	public List<StatusConfirmationVO> getStatusList() {
		return statusList;
	}
	public void setStatusList(List<StatusConfirmationVO> statusList) {
		this.statusList = statusList;
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
	public String getStatusCd() {
		return statusCd;
	}
	public void setStatusCd(String statusCd) {
		this.statusCd = statusCd;
	}
	public String getFollowupStatusCd() {
		return followupStatusCd;
	}
	public void setFollowupStatusCd(String followupStatusCd) {
		this.followupStatusCd = followupStatusCd;
	}
	public String getPublishedDateStr() {
		return publishedDateStr;
	}
	public void setPublishedDateStr(String publishedDateStr) {
		this.publishedDateStr = publishedDateStr;
	}
	public String getEffectiveDateStr() {
		return effectiveDateStr;
	}
	public void setEffectiveDateStr(String effectiveDateStr) {
		this.effectiveDateStr = effectiveDateStr;
	}
	public Date getPubDateFrom() {
		return pubDateFrom;
	}
	public void setPubDateFrom(Date pubDateFrom) {
		this.pubDateFrom = pubDateFrom;
	}
	public Date getPubDateEnd() {
		return pubDateEnd;
	}
	public void setPubDateEnd(Date pubDateEnd) {
		this.pubDateEnd = pubDateEnd;
	}
	public Date getEffDateEnd() {
		return effDateEnd;
	}
	public void setEffDateEnd(Date effDateEnd) {
		this.effDateEnd = effDateEnd;
	}
	public Date getEffDateFrom() {
		return effDateFrom;
	}
	public void setEffDateFrom(Date effDateFrom) {
		this.effDateFrom = effDateFrom;
	}
	public Date getTargetDateFrom() {
		return targetDateFrom;
	}
	public void setTargetDateFrom(Date targetDateFrom) {
		this.targetDateFrom = targetDateFrom;
	}
	public Date getTargetDateEnd() {
		return targetDateEnd;
	}
	public void setTargetDateEnd(Date targetDateEnd) {
		this.targetDateEnd = targetDateEnd;
	}
}
