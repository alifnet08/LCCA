package com.wo.module.tmpComplianceReview.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;

import com.wo.module.tmpComplianceReview.model.TmpComplianceReviewPicFollowupFindings;
import com.wo.module.tmpComplianceReview.model.TmpComplianceReviewPicFollowupPoints;
import com.wo.module.tmpComplianceReview.model.TmpComplianceReviewPicFollowupReview;
import com.wo.module.trcComplianceReview.model.TrcComplianceReviewPicFollowupFindings;
import com.wo.module.trcComplianceReview.model.TrcComplianceReviewPicFollowupPoints;
import com.wo.module.trcComplianceReview.model.TrcComplianceReviewPicFollowupPointsAttachment;
import com.wo.module.trcComplianceReview.model.TrcComplianceReviewPicFollowupReview;

public class ComplianceStatusConfirmationVO implements Serializable {
	private static final long serialVersionUID = 6395152164269025885L;

	private String pic1;
	private String pic2;
	private String pic3;

	private String followupStatus;
	private String followupStatusIn;
	private String followupStatusEn;
	private String picConfirmation;
	private String confirmationDate;
	private String followupDate;
	private String followupNote;
	private String complianceDate;
	private String complianceStatus;
	private String complianceStatusIn;
	private String complianceStatusEn;
	private String complianceNote;
	private String followupBy;

	private String namePic;
	private Long picFollowupId;
	private String targetDate;
	private Long picFollowupPointsId;

	private List<TrcComplianceReviewPicFollowupPointsAttachment> TrcComplianceReviewPicFollowupAttachments;
	
	private Long complianceReviewPicFollowupId;
	private List<TrcComplianceReviewPicFollowupPoints> pointList;
	private List<TrcComplianceReviewPicFollowupFindings> findingsList;
	private List<TrcComplianceReviewPicFollowupReview> areaReviewList;
	
	// tmp
	private Long complianceReviewPicFollowupIdTmp;
	private List<TmpComplianceReviewPicFollowupPoints> pointListTmp;
	private List<TmpComplianceReviewPicFollowupFindings> findingsListTmp;
	private List<TmpComplianceReviewPicFollowupReview> areaReviewListTmp;
	
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@SuppressWarnings("static-access")
	public String getFollowupStatus() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if (locale != null && locale.equals(locale.ENGLISH)) {
			followupStatus = followupStatusEn;
		} else {
			followupStatus = followupStatusIn;
		}
		
		return followupStatus;
	}

	public void setFollowupStatus(String followupStatus) {
		this.followupStatus = followupStatus;
	}

	public String getFollowupStatusIn() {
		return followupStatusIn;
	}

	public void setFollowupStatusIn(String followupStatusIn) {
		this.followupStatusIn = followupStatusIn;
	}

	public String getFollowupStatusEn() {
		return followupStatusEn;
	}

	public void setFollowupStatusEn(String followupStatusEn) {
		this.followupStatusEn = followupStatusEn;
	}

	public String getPicConfirmation() {
		return picConfirmation;
	}

	public void setPicConfirmation(String picConfirmation) {
		this.picConfirmation = picConfirmation;
	}

	public String getConfirmationDate() {
		return confirmationDate;
	}

	public void setConfirmationDate(String confirmationDate) {
		this.confirmationDate = confirmationDate;
	}

	public String getFollowupDate() {
		return followupDate;
	}

	public void setFollowupDate(String followupDate) {
		this.followupDate = followupDate;
	}

	public String getFollowupNote() {
		return followupNote;
	}

	public void setFollowupNote(String followupNote) {
		this.followupNote = followupNote;
	}

	public String getComplianceDate() {
		return complianceDate;
	}

	public void setComplianceDate(String complianceDate) {
		this.complianceDate = complianceDate;
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

	public String getComplianceNote() {
		return complianceNote;
	}

	public void setComplianceNote(String complianceNote) {
		this.complianceNote = complianceNote;
	}

	public String getFollowupBy() {
		return followupBy;
	}

	public void setFollowupBy(String followupBy) {
		this.followupBy = followupBy;
	}

	public String getNamePic() {
		return namePic;
	}

	public void setNamePic(String namePic) {
		this.namePic = namePic;
	}

	public Long getPicFollowupId() {
		return picFollowupId;
	}

	public void setPicFollowupId(Long picFollowupId) {
		this.picFollowupId = picFollowupId;
	}

	public String getTargetDate() {
		return targetDate;
	}

	public void setTargetDate(String targetDate) {
		this.targetDate = targetDate;
	}

	public List<TrcComplianceReviewPicFollowupPointsAttachment> getTrcComplianceReviewPicFollowupAttachments() {
		return TrcComplianceReviewPicFollowupAttachments;
	}

	public void setTrcComplianceReviewPicFollowupAttachments(
			List<TrcComplianceReviewPicFollowupPointsAttachment> trcComplianceReviewPicFollowupAttachments) {
		TrcComplianceReviewPicFollowupAttachments = trcComplianceReviewPicFollowupAttachments;
	}

	public Long getComplianceReviewPicFollowupId() {
		return complianceReviewPicFollowupId;
	}

	public void setComplianceReviewPicFollowupId(Long complianceReviewPicFollowupId) {
		this.complianceReviewPicFollowupId = complianceReviewPicFollowupId;
	}

	public List<TrcComplianceReviewPicFollowupFindings> getFindingsList() {
		return findingsList;
	}

	public void setFindingsList(List<TrcComplianceReviewPicFollowupFindings> findingsList) {
		this.findingsList = findingsList;
	}

	public List<TrcComplianceReviewPicFollowupReview> getAreaReviewList() {
		return areaReviewList;
	}

	public void setAreaReviewList(List<TrcComplianceReviewPicFollowupReview> areaReviewList) {
		this.areaReviewList = areaReviewList;
	}

	public List<TrcComplianceReviewPicFollowupPoints> getPointList() {
		return pointList;
	}

	public void setPointList(List<TrcComplianceReviewPicFollowupPoints> pointList) {
		this.pointList = pointList;
	}

	public List<TmpComplianceReviewPicFollowupPoints> getPointListTmp() {
		return pointListTmp;
	}

	public void setPointListTmp(List<TmpComplianceReviewPicFollowupPoints> pointListTmp) {
		this.pointListTmp = pointListTmp;
	}

	public List<TmpComplianceReviewPicFollowupFindings> getFindingsListTmp() {
		return findingsListTmp;
	}

	public void setFindingsListTmp(List<TmpComplianceReviewPicFollowupFindings> findingsListTmp) {
		this.findingsListTmp = findingsListTmp;
	}

	public List<TmpComplianceReviewPicFollowupReview> getAreaReviewListTmp() {
		return areaReviewListTmp;
	}

	public void setAreaReviewListTmp(List<TmpComplianceReviewPicFollowupReview> areaReviewListTmp) {
		this.areaReviewListTmp = areaReviewListTmp;
	}

	public Long getComplianceReviewPicFollowupIdTmp() {
		return complianceReviewPicFollowupIdTmp;
	}

	public void setComplianceReviewPicFollowupIdTmp(Long complianceReviewPicFollowupIdTmp) {
		this.complianceReviewPicFollowupIdTmp = complianceReviewPicFollowupIdTmp;
	}

	public Long getPicFollowupPointsId() {
		return picFollowupPointsId;
	}

	public void setPicFollowupPointsId(Long picFollowupPointsId) {
		this.picFollowupPointsId = picFollowupPointsId;
	}

}