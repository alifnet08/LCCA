package com.wo.module.cpsa.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.Locale;

import javax.faces.context.FacesContext;

public class CompliancePlanSelfAssessmentVo implements Serializable {

	private static final long serialVersionUID = 8743015470418989486L;

	private Long compliancePlanSelfAssessmentId;
	private String cpsaType;
	private String cpsaTypeIn;
	private String cpsaTypeEn;
	private String cpsaTypeCode;
	private String cpsaName;
	private Date uploadDate;
	private String uploadDateStr;
	private String periode;
	private Date periodeFrom;
	private String periodeFromStr;
	private Date periodeTo;
	private String periodeToStr;
	private String uploadFile;
	private String fileId;
	private Long fileSize;
	private String note;
	private String letterNo;
	private Date letterDate;
	private String letterDateStr;
	private String letterAbout;
	private String cpsaStatus;
	private String cpsaStatusName;
	private String statusDownload;
	private String statusDownloadName;
	private String filePathDownload;
	private String cpsaStatusCode;
	private String cpsaStatusNameTemp;
	private String cpsaStatusNameEn;
	private String cpsaStatusNameIn;
	private String cpsaBranchSubBranch;

	private Long fileSizeKB;

	public Long getCompliancePlanSelfAssessmentId() {
		return compliancePlanSelfAssessmentId;
	}

	public void setCompliancePlanSelfAssessmentId(Long compliancePlanSelfAssessmentId) {
		this.compliancePlanSelfAssessmentId = compliancePlanSelfAssessmentId;
	}

	@SuppressWarnings("static-access")
	public String getCpsaType() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();

		if (locale != null && locale.equals(locale.ENGLISH)) {
			cpsaType = cpsaTypeEn;
		} else {
			cpsaType = cpsaTypeIn;
		}

		return cpsaType;
	}

	public void setCpsaType(String cpsaType) {
		this.cpsaType = cpsaType;
	}

	public String getCpsaTypeIn() {
		return cpsaTypeIn;
	}

	public void setCpsaTypeIn(String cpsaTypeIn) {
		this.cpsaTypeIn = cpsaTypeIn;
	}

	public String getCpsaTypeEn() {
		return cpsaTypeEn;
	}

	public void setCpsaTypeEn(String cpsaTypeEn) {
		this.cpsaTypeEn = cpsaTypeEn;
	}

	public String getCpsaTypeCode() {
		return cpsaTypeCode;
	}

	public void setCpsaTypeCode(String cpsaTypeCode) {
		this.cpsaTypeCode = cpsaTypeCode;
	}

	public String getCpsaName() {
		return cpsaName;
	}

	public void setCpsaName(String cpsaName) {
		this.cpsaName = cpsaName;
	}

	public Date getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}

	public String getUploadDateStr() {
		return uploadDateStr;
	}

	public void setUploadDateStr(String uploadDateStr) {
		this.uploadDateStr = uploadDateStr;
	}

	public Date getPeriodeFrom() {
		return periodeFrom;
	}

	public void setPeriodeFrom(Date periodeFrom) {
		this.periodeFrom = periodeFrom;
	}

	public String getPeriodeFromStr() {
		return periodeFromStr;
	}

	public void setPeriodeFromStr(String periodeFromStr) {
		this.periodeFromStr = periodeFromStr;
	}

	public Date getPeriodeTo() {
		return periodeTo;
	}

	public void setPeriodeTo(Date periodeTo) {
		this.periodeTo = periodeTo;
	}

	public String getPeriodeToStr() {
		return periodeToStr;
	}

	public void setPeriodeToStr(String periodeToStr) {
		this.periodeToStr = periodeToStr;
	}

	public String getPeriode() {

		if (periodeFromStr != null && !periodeFromStr.equals("")) {
			periode = periodeFromStr;
			if (periodeToStr != null && !periodeToStr.equals("")) {
				periode = periodeFromStr.concat(" - ").concat(periodeToStr);
			}
		} else if (periodeToStr != null && !periodeToStr.equals("")) {
			periode = periodeToStr;
		}

		return periode;
	}

	public void setPeriode(String periode) {
		this.periode = periode;
	}

	public String getUploadFile() {
		return uploadFile;
	}

	public void setUploadFile(String uploadFile) {
		this.uploadFile = uploadFile;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public Long getFileSizeKB() {
		if (fileSize != null) {
			BigDecimal var = new BigDecimal(fileSize).divide(new BigDecimal(1024), RoundingMode.UP);
			fileSizeKB = var.longValue();
		} else {
			fileSizeKB = new Long(0);
		}
		return fileSizeKB;
	}

	public void setFileSizeKB(Long fileSizeKB) {
		this.fileSizeKB = fileSizeKB;
	}

	public String getLetterNo() {
		return letterNo;
	}

	public void setLetterNo(String letterNo) {
		this.letterNo = letterNo;
	}

	public Date getLetterDate() {
		return letterDate;
	}

	public void setLetterDate(Date letterDate) {
		this.letterDate = letterDate;
	}

	public String getLetterDateStr() {
		return letterDateStr;
	}

	public void setLetterDateStr(String letterDateStr) {
		this.letterDateStr = letterDateStr;
	}

	public String getLetterAbout() {
		return letterAbout;
	}

	public void setLetterAbout(String letterAbout) {
		this.letterAbout = letterAbout;
	}

	public String getCpsaStatus() {
		return cpsaStatus;
	}

	public void setCpsaStatus(String cpsaStatus) {
		this.cpsaStatus = cpsaStatus;
	}

	public String getCpsaStatusName() {
		return cpsaStatusName;
	}

	public void setCpsaStatusName(String cpsaStatusName) {
		this.cpsaStatusName = cpsaStatusName;
	}

	public String getStatusDownload() {
		return statusDownload;
	}

	public void setStatusDownload(String statusDownload) {
		this.statusDownload = statusDownload;
	}

	public String getStatusDownloadName() {
		return statusDownloadName;
	}

	public void setStatusDownloadName(String statusDownloadName) {
		this.statusDownloadName = statusDownloadName;
	}

	public String getFilePathDownload() {
		return filePathDownload;
	}

	public void setFilePathDownload(String filePathDownload) {
		this.filePathDownload = filePathDownload;
	}

	public String getCpsaStatusCode() {
		return cpsaStatusCode;
	}

	public void setCpsaStatusCode(String cpsaStatusCode) {
		this.cpsaStatusCode = cpsaStatusCode;
	}

	@SuppressWarnings("static-access")
	public String getCpsaStatusNameTemp() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();

		if (locale != null && locale.equals(locale.ENGLISH)) {
			cpsaStatusNameTemp = cpsaStatusNameEn;
		} else {
			cpsaStatusNameTemp = cpsaStatusNameIn;
		}
		
		return cpsaStatusNameTemp;
	}

	public void setCpsaStatusNameTemp(String cpsaStatusNameTemp) {
		this.cpsaStatusNameTemp = cpsaStatusNameTemp;
	}

	public String getCpsaStatusNameEn() {
		return cpsaStatusNameEn;
	}

	public void setCpsaStatusNameEn(String cpsaStatusNameEn) {
		this.cpsaStatusNameEn = cpsaStatusNameEn;
	}

	public String getCpsaStatusNameIn() {
		return cpsaStatusNameIn;
	}

	public void setCpsaStatusNameIn(String cpsaStatusNameIn) {
		this.cpsaStatusNameIn = cpsaStatusNameIn;
	}

	public String getCpsaBranchSubBranch() {
		return cpsaBranchSubBranch;
	}

	public void setCpsaBranchSubBranch(String cpsaBranchSubBranch) {
		this.cpsaBranchSubBranch = cpsaBranchSubBranch;
	}

}
