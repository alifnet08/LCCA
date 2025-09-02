package com.wo.module.regulationMonitoringView.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;

import com.wo.module.regulationMonitoring.vo.StatusConfirmationVO;


public class RegMonitoringViewVO implements Serializable {

	private static final long serialVersionUID = 6171577852485712555L;

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
	private String status;
	private String statusTindakLanjut;
	private String picKonfirmasi;
	private String tglKonfirmasi;
	private String tglTindakLanjut;
	private String statusNameIn;
	private String statusNameEn;
	private String statusCode;
	@SuppressWarnings("unused")
	private String statusName;
	
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

	public String getStatus() {
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

	public String getStatusNameIn() {
		return statusNameIn;
	}

	public void setStatusNameIn(String statusNameIn) {
		this.statusNameIn = statusNameIn;
	}

	public String getStatusNameEn() {
		return statusNameEn;
	}

	public void setStatusNameEn(String statusNameEn) {
		this.statusNameEn = statusNameEn;
	}

	public List<StatusConfirmationVO> getStatusList() {
		return statusList;
	}

	public void setStatusList(List<StatusConfirmationVO> statusList) {
		this.statusList = statusList;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	@SuppressWarnings("static-access")
	public String getStatusName() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if (locale != null && locale.equals(locale.ENGLISH)) {
			status = statusNameEn;
		} else {
			status = statusNameIn;
		}
		return status;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
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

}
