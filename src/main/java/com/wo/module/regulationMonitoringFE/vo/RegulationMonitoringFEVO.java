package com.wo.module.regulationMonitoringFE.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.wo.module.regulationMonitoring.vo.StatusConfirmationVO;

public class RegulationMonitoringFEVO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3430056410028381172L;
	
	private Long regMonitoringId;
	private Long regMonitoringPicFpId;
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
	private String targetDateStr;
	
	private List<StatusConfirmationVO> statusList;
	
	private String picName1;
	private String picName2;
	private String picName3;
	
	private String regMonitoringNote;
	private String complianceNote;
	private String complianceStatus;
	private String followUpStatus;

	public Long getRegMonitoringId() {
		return regMonitoringId;
	}

	public void setRegMonitoringId(Long regMonitoringId) {
		this.regMonitoringId = regMonitoringId;
	}

	public String getJenisKetentuan() {
		return jenisKetentuan;
	}

	public void setJenisKetentuan(String jenisKetentuan) {
		this.jenisKetentuan = jenisKetentuan;
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

	public String getTipeDokumen() {
		return tipeDokumen;
	}

	public void setTipeDokumen(String tipeDokumen) {
		this.tipeDokumen = tipeDokumen;
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

	public String getKategori() {
		return kategori;
	}

	public void setKategori(String kategori) {
		this.kategori = kategori;
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

	public String getTopik() {
		return topik;
	}

	public void setTopik(String topik) {
		this.topik = topik;
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

	public String getNoDokumen() {
		return noDokumen;
	}

	public void setNoDokumen(String noDokumen) {
		this.noDokumen = noDokumen;
	}

	public String getJdlPeraturan() {
		return jdlPeraturan;
	}

	public void setJdlPeraturan(String jdlPeraturan) {
		this.jdlPeraturan = jdlPeraturan;
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

	public String getNamaPIC() {
		return namaPIC;
	}

	public void setNamaPIC(String namaPIC) {
		this.namaPIC = namaPIC;
	}

	public String getStatusCd() {
		return statusCd;
	}

	public void setStatusCd(String statusCd) {
		this.statusCd = statusCd;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getFollowupStatusCd() {
		return followupStatusCd;
	}

	public void setFollowupStatusCd(String followupStatusCd) {
		this.followupStatusCd = followupStatusCd;
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

	public Date getEffDateFrom() {
		return effDateFrom;
	}

	public void setEffDateFrom(Date effDateFrom) {
		this.effDateFrom = effDateFrom;
	}

	public Date getEffDateEnd() {
		return effDateEnd;
	}

	public void setEffDateEnd(Date effDateEnd) {
		this.effDateEnd = effDateEnd;
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

	public List<StatusConfirmationVO> getStatusList() {
		return statusList;
	}

	public void setStatusList(List<StatusConfirmationVO> statusList) {
		this.statusList = statusList;
	}

	public String getTargetDateStr() {
		return targetDateStr;
	}

	public void setTargetDateStr(String targetDateStr) {
		this.targetDateStr = targetDateStr;
	}

	public String getPicName1() {
		return picName1;
	}

	public void setPicName1(String picName1) {
		this.picName1 = picName1;
	}

	public String getPicName2() {
		return picName2;
	}

	public void setPicName2(String picName2) {
		this.picName2 = picName2;
	}

	public String getPicName3() {
		return picName3;
	}

	public void setPicName3(String picName3) {
		this.picName3 = picName3;
	}

	public String getRegMonitoringNote() {
		return regMonitoringNote;
	}

	public void setRegMonitoringNote(String regMonitoringNote) {
		this.regMonitoringNote = regMonitoringNote;
	}

	public String getComplianceNote() {
		return complianceNote;
	}

	public void setComplianceNote(String complianceNote) {
		this.complianceNote = complianceNote;
	}

	public String getFollowUpStatus() {
		return followUpStatus;
	}

	public void setFollowUpStatus(String followUpStatus) {
		this.followUpStatus = followUpStatus;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getComplianceStatus() {
		return complianceStatus;
	}

	public void setComplianceStatus(String complianceStatus) {
		this.complianceStatus = complianceStatus;
	}

	public Long getRegMonitoringPicFpId() {
		return regMonitoringPicFpId;
	}

	public void setRegMonitoringPicFpId(Long regMonitoringPicFpId) {
		this.regMonitoringPicFpId = regMonitoringPicFpId;
	}
	
	
}