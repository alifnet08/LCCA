package com.wo.module.trcComplianceReview.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;

import com.wo.module.tmpComplianceReview.vo.StatusConfirmationVO;

public class TrcComplianceReviewVO implements Serializable {
	private static final long serialVersionUID = 3019374791490640764L;

	private Long complianceReviewId;

	private String reviewCategoryCd;
	private String reviewCategory;
	private String reviewCategoryIn;
	private String reviewCategoryEn;
	private String reviewedBranch;
	private String documentNo;
	private String documentDateStr;
	private String perihal;
	private String perihalIn;
	private String perihalEn;
	private String followUpPoints;
	private String followupStatusCd;
	private String reminderStatusCd;
	private String reminderStatus;
	private String statusCd;
	private String status;
	private String statusIn;
	private String statusEn;
	private String statusTindakLanjut;
	private String complianceStatus;
	private String complianceStatusIn;
	private String complianceStatusEn;
	private String targetDate;
	private Long picFollowupId;
	private Date documentDate;
	private String picName1;
	private String picName2;
	private String picName3;
	private String complianceNote;

	private List<StatusConfirmationVO> statusList;

	private List<String> picNameList;

	@SuppressWarnings("static-access")
	public String getPerihal() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if (locale != null && locale.equals(locale.ENGLISH)) {
			perihal = perihalEn;
		} else {
			perihal = perihalIn;
		}
		return perihal;
	}

	public Long getComplianceReviewId() {
		return complianceReviewId;
	}

	public void setComplianceReviewId(Long complianceReviewId) {
		this.complianceReviewId = complianceReviewId;
	}

	public String getReviewCategoryCd() {
		return reviewCategoryCd;
	}

	public void setReviewCategoryCd(String reviewCategoryCd) {
		this.reviewCategoryCd = reviewCategoryCd;
	}

	@SuppressWarnings("static-access")
	public String getReviewCategory() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if (locale != null && locale.equals(locale.ENGLISH)) {
			reviewCategory = reviewCategoryEn;
		} else {
			reviewCategory = reviewCategoryIn;
		}
		return reviewCategory;
	}

	public void setReviewCategory(String reviewCategory) {
		this.reviewCategory = reviewCategory;
	}

	public String getReviewedBranch() {
		return reviewedBranch;
	}

	public void setReviewedBranch(String reviewedBranch) {
		this.reviewedBranch = reviewedBranch;
	}

	public String getDocumentNo() {
		return documentNo;
	}

	public void setDocumentNo(String documentNo) {
		this.documentNo = documentNo;
	}

	public Date getDocumentDate() {
		return documentDate;
	}

	public void setDocumentDate(Date documentDate) {
		this.documentDate = documentDate;
	}

	public String getPerihalIn() {
		return perihalIn;
	}

	public void setPerihalIn(String perihalIn) {
		this.perihalIn = perihalIn;
	}

	public String getPerihalEn() {
		return perihalEn;
	}

	public void setPerihalEn(String perihalEn) {
		this.perihalEn = perihalEn;
	}

	public String getFollowUpPoints() {
		return followUpPoints;
	}

	public void setFollowUpPoints(String followUpPoints) {
		this.followUpPoints = followUpPoints;
	}

	public String getFollowupStatusCd() {
		return followupStatusCd;
	}

	public void setFollowupStatusCd(String followupStatusCd) {
		this.followupStatusCd = followupStatusCd;
	}

	public String getReminderStatusCd() {
		return reminderStatusCd;
	}

	public void setReminderStatusCd(String reminderStatusCd) {
		this.reminderStatusCd = reminderStatusCd;
	}

	public String getReminderStatus() {
		return reminderStatus;
	}

	public void setReminderStatus(String reminderStatus) {
		this.reminderStatus = reminderStatus;
	}

	public String getStatusCd() {
		return statusCd;
	}

	public void setStatusCd(String statusCd) {
		this.statusCd = statusCd;
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

	public void setPerihal(String perihal) {
		this.perihal = perihal;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<StatusConfirmationVO> getStatusList() {
		return statusList;
	}

	public void setStatusList(List<StatusConfirmationVO> statusList) {
		this.statusList = statusList;
	}

	public String getReviewCategoryIn() {
		return reviewCategoryIn;
	}

	public void setReviewCategoryIn(String reviewCategoryIn) {
		this.reviewCategoryIn = reviewCategoryIn;
	}

	public String getReviewCategoryEn() {
		return reviewCategoryEn;
	}

	public void setReviewCategoryEn(String reviewCategoryEn) {
		this.reviewCategoryEn = reviewCategoryEn;
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

	public String getDocumentDateStr() {
		return documentDateStr;
	}

	public void setDocumentDateStr(String documentDateStr) {
		this.documentDateStr = documentDateStr;
	}

	public String getStatusTindakLanjut() {
		return statusTindakLanjut;
	}

	public void setStatusTindakLanjut(String statusTindakLanjut) {
		this.statusTindakLanjut = statusTindakLanjut;
	}

	@SuppressWarnings("static-access")
	public String getComplianceStatus() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if (locale != null && locale.equals(locale.ENGLISH)) {
			complianceStatus = complianceStatusEn;
		} else {
			complianceStatus = complianceStatusIn;
		}
		return complianceStatus;
	}

	public void setComplianceStatus(String complianceStatus) {
		this.complianceStatus = complianceStatus;
	}

	public String getComplianceStatusIn() {
		return complianceStatusIn;
	}

	public void setComplianceStatusIn(String complianceStatusIn) {
		this.complianceStatusIn = complianceStatusIn;
	}

	public String getComplianceStatusEn() {
		return complianceStatusEn;
	}

	public void setComplianceStatusEn(String complianceStatusEn) {
		this.complianceStatusEn = complianceStatusEn;
	}

	public List<String> getPicNameList() {
		return picNameList;
	}

	public void setPicNameList(List<String> picNameList) {
		this.picNameList = picNameList;
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

	public String getComplianceNote() {
		return complianceNote;
	}

	public void setComplianceNote(String complianceNote) {
		this.complianceNote = complianceNote;
	}

	public String getTargetDate() {
		return targetDate;
	}

	public void setTargetDate(String targetDate) {
		this.targetDate = targetDate;
	}

	public Long getPicFollowupId() {
		return picFollowupId;
	}

	public void setPicFollowupId(Long picFollowupId) {
		this.picFollowupId = picFollowupId;
	}

}