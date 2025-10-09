package com.wo.module.report.reportRegulation.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReportRegulationDetailVo implements Serializable{

	private static final long serialVersionUID = -7047034826642723536L;
	
	private Long regulationId;
	private String peraturanIn;
	private String peraturanEn;
	private String directorate;
	private String judul;
	private Date publishDate;
	private String url;
	private String documentTypeIn;
	private String documentTypeEn;
	private Long hits;
	private Date expiredDate;
	private Long expiredWarning;
	private String statusIn;
	private String statusEn;
	private String status;
	private String jenisKetentuan;
	private String typeReviewDateCode;
	
	private String typeReviewDateName;
	private String publishDateStr;
	private String expiredDateStr;
	
	private List<RegulationTrackRecordVo> trackRecordVos = new ArrayList<>();
	private Integer countTrackRecord;
	
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
	public String getDirectorate() {
		return directorate;
	}
	public void setDirectorate(String directorate) {
		this.directorate = directorate;
	}
	public String getJudul() {
		return judul;
	}
	public void setJudul(String judul) {
		this.judul = judul;
	}
	public Date getPublishDate() {
		return publishDate;
	}
	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getDocumentTypeIn() {
		return documentTypeIn;
	}
	public void setDocumentTypeIn(String documentTypeIn) {
		this.documentTypeIn = documentTypeIn;
	}
	public String getDocumentTypeEn() {
		return documentTypeEn;
	}
	public void setDocumentTypeEn(String documentTypeEn) {
		this.documentTypeEn = documentTypeEn;
	}
	public Long getHits() {
		return hits;
	}
	public void setHits(Long hits) {
		this.hits = hits;
	}
	public Date getExpiredDate() {
		return expiredDate;
	}
	public void setExpiredDate(Date expiredDate) {
		this.expiredDate = expiredDate;
	}
	public Long getExpiredWarning() {
		return expiredWarning;
	}
	public void setExpiredWarning(Long expiredWarning) {
		this.expiredWarning = expiredWarning;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getJenisKetentuan() {
		return jenisKetentuan;
	}
	public void setJenisKetentuan(String jenisKetentuan) {
		this.jenisKetentuan = jenisKetentuan;
	}
	public String getPublishDateStr() {
		return publishDateStr;
	}
	public void setPublishDateStr(String publishDateStr) {
		this.publishDateStr = publishDateStr;
	}
	public String getExpiredDateStr() {
		return expiredDateStr;
	}
	public void setExpiredDateStr(String expiredDateStr) {
		this.expiredDateStr = expiredDateStr;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Long getRegulationId() {
		return regulationId;
	}
	public void setRegulationId(Long regulationId) {
		this.regulationId = regulationId;
	}
	public List<RegulationTrackRecordVo> getTrackRecordVos() {
		return trackRecordVos;
	}
	public void setTrackRecordVos(List<RegulationTrackRecordVo> trackRecordVos) {
		this.trackRecordVos = trackRecordVos;
	}
	public Integer getCountTrackRecord() {
		return countTrackRecord;
	}
	public void setCountTrackRecord(Integer countTrackRecord) {
		this.countTrackRecord = countTrackRecord;
	}
	public String getTypeReviewDateCode() {
		return typeReviewDateCode;
	}
	public void setTypeReviewDateCode(String typeReviewDateCode) {
		this.typeReviewDateCode = typeReviewDateCode;
	}
	public String getTypeReviewDateName() {
		return typeReviewDateName;
	}
	public void setTypeReviewDateName(String typeReviewDateName) {
		this.typeReviewDateName = typeReviewDateName;
	}

}
