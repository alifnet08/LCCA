package com.wo.module.report.reportSocializationDetail.vo;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.Locale;

import javax.faces.context.FacesContext;

public class ReportSocializationDetailVo implements Serializable {

	private static final long serialVersionUID = -2061844600745241506L;
	private int no; 
	private String peraturan;
	private String peraturanIn;
	private String peraturanEn;
	private String jenisPeraturan;
	private String jenisPeraturanIn;
	private String jenisPeraturanEn;
	private String noPeraturan;
	private String judulPeraturan;
	private String judulPeraturanIn;
	private String judulPeraturanEn;
	private String kategoriDokumen;
	private String kategoriDokumenIn;
	private String kategoriDokumenEn;
	private String tindakLanjut;
	private String tindakLanjutNote;
	private Date targetDate;
	private Date rescheduleTargetDate1;
	private Date rescheduleTargetDate2;
	private Date rescheduleTargetDate3;
	private String status;
	private String statusIn;
	private String statusEn;
	private String pic1;
	private String pic2;
	private String pic3;
	private String divisi;
	private String picCompliance;
	private String buktiKonfirmasi;
	private Date tanggalKonfirmasi;
	private Date tanggalTindakLanjut;
	private String keterangan;
	private String sla;
	private BigInteger cnt;
	private Date tanggalFilter;
	private String jenisKetentuan;
	private Long documentTypeId;

	public String getPeraturanIn() {
		return peraturanIn;
	}

	public void setPeraturanIn(String peraturanIn) {
		this.peraturanIn = peraturanIn;
	}

	public String getPeraturanEn() {
		return peraturanEn;
	}

	public void setPeraturanEn(String peraturanEn) {
		this.peraturanEn = peraturanEn;
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

	public String getNoPeraturan() {
		return noPeraturan;
	}

	public void setNoPeraturan(String noPeraturan) {
		this.noPeraturan = noPeraturan;
	}

	public String getJudulPeraturanIn() {
		return judulPeraturanIn;
	}

	public void setJudulPeraturanIn(String judulPeraturanIn) {
		this.judulPeraturanIn = judulPeraturanIn;
	}

	public String getJudulPeraturanEn() {
		return judulPeraturanEn;
	}

	public void setJudulPeraturanEn(String judulPeraturanEn) {
		this.judulPeraturanEn = judulPeraturanEn;
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

	public String getTindakLanjut() {
		return tindakLanjut;
	}

	public void setTindakLanjut(String tindakLanjut) {
		this.tindakLanjut = tindakLanjut;
	}

	public String getTindakLanjutNote() {
		return tindakLanjutNote;
	}

	public void setTindakLanjutNote(String tindakLanjutNote) {
		this.tindakLanjutNote = tindakLanjutNote;
	}

	public Date getTargetDate() {
		return targetDate;
	}

	public void setTargetDate(Date targetDate) {
		this.targetDate = targetDate;
	}

	public Date getRescheduleTargetDate1() {
		return rescheduleTargetDate1;
	}

	public void setRescheduleTargetDate1(Date rescheduleTargetDate1) {
		this.rescheduleTargetDate1 = rescheduleTargetDate1;
	}

	public Date getRescheduleTargetDate2() {
		return rescheduleTargetDate2;
	}

	public void setRescheduleTargetDate2(Date rescheduleTargetDate2) {
		this.rescheduleTargetDate2 = rescheduleTargetDate2;
	}

	public Date getRescheduleTargetDate3() {
		return rescheduleTargetDate3;
	}

	public void setRescheduleTargetDate3(Date rescheduleTargetDate3) {
		this.rescheduleTargetDate3 = rescheduleTargetDate3;
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

	public String getPic1() {
		return pic1;
	}

	public void setPic1(String pic1) {
		this.pic1 = pic1;
	}

	public String getPic2() {
		return pic2;
	}

	public void setPic2(String pic2) {
		this.pic2 = pic2;
	}

	public String getPic3() {
		return pic3;
	}

	public void setPic3(String pic3) {
		this.pic3 = pic3;
	}

	public String getDivisi() {
		return divisi;
	}

	public void setDivisi(String divisi) {
		this.divisi = divisi;
	}

	public String getPicCompliance() {
		return picCompliance;
	}

	public void setPicCompliance(String picCompliance) {
		this.picCompliance = picCompliance;
	}

	public String getBuktiKonfirmasi() {
		return buktiKonfirmasi;
	}

	public void setBuktiKonfirmasi(String buktiKonfirmasi) {
		this.buktiKonfirmasi = buktiKonfirmasi;
	}

	public Date getTanggalKonfirmasi() {
		return tanggalKonfirmasi;
	}

	public void setTanggalKonfirmasi(Date tanggalKonfirmasi) {
		this.tanggalKonfirmasi = tanggalKonfirmasi;
	}

	public Date getTanggalTindakLanjut() {
		return tanggalTindakLanjut;
	}

	public void setTanggalTindakLanjut(Date tanggalTindakLanjut) {
		this.tanggalTindakLanjut = tanggalTindakLanjut;
	}

	public String getKeterangan() {
		return keterangan;
	}

	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}

	public String getSla() {
		return sla;
	}

	public void setSla(String sla) {
		this.sla = sla;
	}

	public BigInteger getCnt() {
		return cnt;
	}

	public void setCnt(BigInteger cnt) {
		this.cnt = cnt;
	}

	public Date getTanggalFilter() {
		return tanggalFilter;
	}

	public void setTanggalFilter(Date tanggalFilter) {
		this.tanggalFilter = tanggalFilter;
	}

	public String getJenisKetentuan() {
		return jenisKetentuan;
	}

	public void setJenisKetentuan(String jenisKetentuan) {
		this.jenisKetentuan = jenisKetentuan;
	}

	public Long getDocumentTypeId() {
		return documentTypeId;
	}

	public void setDocumentTypeId(Long documentTypeId) {
		this.documentTypeId = documentTypeId;
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	@SuppressWarnings("static-access")
	public String getPeraturan() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if (locale != null && locale.equals(locale.ENGLISH)) {
			peraturan = peraturanEn;
		} else {
			peraturan = peraturanIn;
		}
		return peraturan;
	}

	public void setPeraturan(String peraturan) {
		this.peraturan = peraturan;
	}

	@SuppressWarnings("static-access")
	public String getJenisPeraturan() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if (locale != null && locale.equals(locale.ENGLISH)) {
			jenisPeraturan = jenisPeraturanEn;
		} else {
			jenisPeraturan = jenisPeraturanIn;
		}
		return jenisPeraturan;
	}

	public void setJenisPeraturan(String jenisPeraturan) {
		this.jenisPeraturan = jenisPeraturan;
	}

	@SuppressWarnings("static-access")
	public String getJudulPeraturan() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if (locale != null && locale.equals(locale.ENGLISH)) {
			judulPeraturan = judulPeraturanEn;
		} else {
			judulPeraturan = judulPeraturanIn;
		}
		return judulPeraturan;
	}

	public void setJudulPeraturan(String judulPeraturan) {
		this.judulPeraturan = judulPeraturan;
	}

	@SuppressWarnings("static-access")
	public String getKategoriDokumen() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if (locale != null && locale.equals(locale.ENGLISH)) {
			kategoriDokumen = kategoriDokumenEn;
		} else {
			kategoriDokumen = kategoriDokumenIn;
		}
		return kategoriDokumen;
	}

	public void setKategoriDokumen(String kategoriDokumen) {
		this.kategoriDokumen = kategoriDokumen;
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
}
