package com.wo.module.report.reportAuditDetail.model;

import java.util.Date;

public class ReportAuditDetail {
	
	private String typeAuditIn;
	private String typeAuditEn;
	private String tanggapanBank;
	private String komitmenBank;
	private String unitKerja;
	private String pic1;
	private String pic2;
	private String pic3;
	private Date targetDate;
	private Date rescheduleTarget1;
	private Date rescheduleTarget2;
	private Date rescheduleTarget3;
	private String statusFollowUpIn;
	private String statusFollowUpEn;
	private String statusVerifikasiIn;
	private String statusVerifikasiEn;
	private String statusIn;
	private String statusEn;
	private String attachmentDokumenTindakLanjut;
	private String keterangan;
	private String attachmentSuratOjkBi;
	private String totalDone;
	private String totalNotDone;
	private String sla;
	private Date creationDate;
	private String auditor;
	private Long auditPicFollowupId;
	private int maxAuditFindingsColumn;
	private Date auditDateFrom;
	private Date auditDateTo;
	private String findingNameIn;
	private String findingNameEn;
	private String auditTemplateNameIn;
	private String auditTemplateNameEn;
	private Long mstAuditId;
	
	// helper
	private String targetDateStr;
	private String rescheduleTarget1Str;
	private String rescheduleTarget2Str;
	private String rescheduleTarget3Str;
	private String creationDateStr;
	private String auditDateFromStr;
	private String auditDateToStr;
	
	public String getTypeAuditIn() {
		return typeAuditIn;
	}
	public void setTypeAuditIn(String typeAuditIn) {
		this.typeAuditIn = typeAuditIn;
	}
	public String getTypeAuditEn() {
		return typeAuditEn;
	}
	public void setTypeAuditEn(String typeAuditEn) {
		this.typeAuditEn = typeAuditEn;
	}
	public String getTanggapanBank() {
		return tanggapanBank;
	}
	public void setTanggapanBank(String tanggapanBank) {
		this.tanggapanBank = tanggapanBank;
	}
	public String getKomitmenBank() {
		return komitmenBank;
	}
	public void setKomitmenBank(String komitmenBank) {
		this.komitmenBank = komitmenBank;
	}
	public String getUnitKerja() {
		return unitKerja;
	}
	public void setUnitKerja(String unitKerja) {
		this.unitKerja = unitKerja;
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
	public Date getTargetDate() {
		return targetDate;
	}
	public void setTargetDate(Date targetDate) {
		this.targetDate = targetDate;
	}
	public Date getRescheduleTarget1() {
		return rescheduleTarget1;
	}
	public void setRescheduleTarget1(Date rescheduleTarget1) {
		this.rescheduleTarget1 = rescheduleTarget1;
	}
	public Date getRescheduleTarget2() {
		return rescheduleTarget2;
	}
	public void setRescheduleTarget2(Date rescheduleTarget2) {
		this.rescheduleTarget2 = rescheduleTarget2;
	}
	public Date getRescheduleTarget3() {
		return rescheduleTarget3;
	}
	public void setRescheduleTarget3(Date rescheduleTarget3) {
		this.rescheduleTarget3 = rescheduleTarget3;
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
	public String getAttachmentDokumenTindakLanjut() {
		return attachmentDokumenTindakLanjut;
	}
	public void setAttachmentDokumenTindakLanjut(String attachmentDokumenTindakLanjut) {
		this.attachmentDokumenTindakLanjut = attachmentDokumenTindakLanjut;
	}
	public String getKeterangan() {
		return keterangan;
	}
	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}
	public String getAttachmentSuratOjkBi() {
		return attachmentSuratOjkBi;
	}
	public void setAttachmentSuratOjkBi(String attachmentSuratOjkBi) {
		this.attachmentSuratOjkBi = attachmentSuratOjkBi;
	}
	public String getSla() {
		return sla;
	}
	public void setSla(String sla) {
		this.sla = sla;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public String getAuditor() {
		return auditor;
	}
	public void setAuditor(String auditor) {
		this.auditor = auditor;
	}
	public Long getAuditPicFollowupId() {
		return auditPicFollowupId;
	}
	public void setAuditPicFollowupId(Long auditPicFollowupId) {
		this.auditPicFollowupId = auditPicFollowupId;
	}
	public String getTargetDateStr() {
		return targetDateStr;
	}
	public void setTargetDateStr(String targetDateStr) {
		this.targetDateStr = targetDateStr;
	}
	public String getRescheduleTarget1Str() {
		return rescheduleTarget1Str;
	}
	public void setRescheduleTarget1Str(String rescheduleTarget1Str) {
		this.rescheduleTarget1Str = rescheduleTarget1Str;
	}
	public String getRescheduleTarget2Str() {
		return rescheduleTarget2Str;
	}
	public void setRescheduleTarget2Str(String rescheduleTarget2Str) {
		this.rescheduleTarget2Str = rescheduleTarget2Str;
	}
	public String getRescheduleTarget3Str() {
		return rescheduleTarget3Str;
	}
	public void setRescheduleTarget3Str(String rescheduleTarget3Str) {
		this.rescheduleTarget3Str = rescheduleTarget3Str;
	}
	public String getCreationDateStr() {
		return creationDateStr;
	}
	public void setCreationDateStr(String creationDateStr) {
		this.creationDateStr = creationDateStr;
	}
	public String getTotalDone() {
		return totalDone;
	}
	public void setTotalDone(String totalDone) {
		this.totalDone = totalDone;
	}
	public String getTotalNotDone() {
		return totalNotDone;
	}
	public void setTotalNotDone(String totalNotDone) {
		this.totalNotDone = totalNotDone;
	}
	public int getMaxAuditFindingsColumn() {
		return maxAuditFindingsColumn;
	}
	public void setMaxAuditFindingsColumn(int maxAuditFindingsColumn) {
		this.maxAuditFindingsColumn = maxAuditFindingsColumn;
	}
	public Date getAuditDateFrom() {
		return auditDateFrom;
	}
	public void setAuditDateFrom(Date auditDateFrom) {
		this.auditDateFrom = auditDateFrom;
	}
	public Date getAuditDateTo() {
		return auditDateTo;
	}
	public void setAuditDateTo(Date auditDateTo) {
		this.auditDateTo = auditDateTo;
	}
	public String getAuditDateFromStr() {
		return auditDateFromStr;
	}
	public void setAuditDateFromStr(String auditDateFromStr) {
		this.auditDateFromStr = auditDateFromStr;
	}
	public String getAuditDateToStr() {
		return auditDateToStr;
	}
	public void setAuditDateToStr(String auditDateToStr) {
		this.auditDateToStr = auditDateToStr;
	}
	public String getStatusFollowUpIn() {
		return statusFollowUpIn;
	}
	public void setStatusFollowUpIn(String statusFollowUpIn) {
		this.statusFollowUpIn = statusFollowUpIn;
	}
	public String getStatusFollowUpEn() {
		return statusFollowUpEn;
	}
	public void setStatusFollowUpEn(String statusFollowUpEn) {
		this.statusFollowUpEn = statusFollowUpEn;
	}
	public String getStatusVerifikasiIn() {
		return statusVerifikasiIn;
	}
	public void setStatusVerifikasiIn(String statusVerifikasiIn) {
		this.statusVerifikasiIn = statusVerifikasiIn;
	}
	public String getStatusVerifikasiEn() {
		return statusVerifikasiEn;
	}
	public void setStatusVerifikasiEn(String statusVerifikasiEn) {
		this.statusVerifikasiEn = statusVerifikasiEn;
	}
	public String getFindingNameIn() {
		return findingNameIn;
	}
	public void setFindingNameIn(String findingNameIn) {
		this.findingNameIn = findingNameIn;
	}
	public String getFindingNameEn() {
		return findingNameEn;
	}
	public void setFindingNameEn(String findingNameEn) {
		this.findingNameEn = findingNameEn;
	}
	public String getAuditTemplateNameIn() {
		return auditTemplateNameIn;
	}
	public void setAuditTemplateNameIn(String auditTemplateNameIn) {
		this.auditTemplateNameIn = auditTemplateNameIn;
	}
	public String getAuditTemplateNameEn() {
		return auditTemplateNameEn;
	}
	public void setAuditTemplateNameEn(String auditTemplateNameEn) {
		this.auditTemplateNameEn = auditTemplateNameEn;
	}
	public Long getMstAuditId() {
		return mstAuditId;
	}
	public void setMstAuditId(Long mstAuditId) {
		this.mstAuditId = mstAuditId;
	}
	
}
