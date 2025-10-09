package com.wo.module.tmpComplianceReview.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.counterType.model.CounterType;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.tmpComplianceReviewApproval.model.TmpComplianceReviewApproval;

public class TmpComplianceReview extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 3019374791490640764L;

	private Long complianceReviewId;

	private ParameterDetail reviewCategory;

	private String reviewedBranch;
	private String documentNo;
	private Date documentDate;
	private String perihalIn;
	private String perihalEn;
	private String notes;
	private String followUp;
	private String followUpPoints;
	private CounterType counterType;
	private ParameterDetail reminderStatus;
	private ParameterDetail status;

	private List<TmpComplianceReviewApproval> tmpComplianceReviewApprovals = new ArrayList<TmpComplianceReviewApproval>();
	private List<TmpComplianceReviewDocument> tmpComplianceReviewDocuments = new ArrayList<TmpComplianceReviewDocument>();
	private List<TmpComplianceReviewPicCompliance> tmpComplianceReviewPicCompliances = new ArrayList<TmpComplianceReviewPicCompliance>();
	
	private List<TmpComplianceReviewPicFollowup> tmpComplianceReviewPicFollowupPoints = new ArrayList<TmpComplianceReviewPicFollowup>();

	private String perihal;

	public Long getComplianceReviewId() {
		return complianceReviewId;
	}

	public void setComplianceReviewId(Long complianceReviewId) {
		this.complianceReviewId = complianceReviewId;
	}

	public ParameterDetail getReviewCategory() {
		return reviewCategory;
	}

	public void setReviewCategory(ParameterDetail reviewCategory) {
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public CounterType getCounterType() {
		return counterType;
	}

	public void setCounterType(CounterType counterType) {
		this.counterType = counterType;
	}

	public ParameterDetail getReminderStatus() {
		return reminderStatus;
	}

	public void setReminderStatus(ParameterDetail reminderStatus) {
		this.reminderStatus = reminderStatus;
	}

	public ParameterDetail getStatus() {
		return status;
	}

	public void setStatus(ParameterDetail status) {
		this.status = status;
	}

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

	public void setPerihal(String perihal) {
		this.perihal = perihal;
	}

	public List<TmpComplianceReviewApproval> getTmpComplianceReviewApprovals() {
		return tmpComplianceReviewApprovals;
	}

	public void setTmpComplianceReviewApprovals(List<TmpComplianceReviewApproval> tmpComplianceReviewApprovals) {
		this.tmpComplianceReviewApprovals = tmpComplianceReviewApprovals;
	}

	public List<TmpComplianceReviewDocument> getTmpComplianceReviewDocuments() {
		return tmpComplianceReviewDocuments;
	}

	public void setTmpComplianceReviewDocuments(List<TmpComplianceReviewDocument> tmpComplianceReviewDocuments) {
		this.tmpComplianceReviewDocuments = tmpComplianceReviewDocuments;
	}

	public List<TmpComplianceReviewPicCompliance> getTmpComplianceReviewPicCompliances() {
		return tmpComplianceReviewPicCompliances;
	}

	public void setTmpComplianceReviewPicCompliances(
			List<TmpComplianceReviewPicCompliance> tmpComplianceReviewPicCompliances) {
		this.tmpComplianceReviewPicCompliances = tmpComplianceReviewPicCompliances;
	}

	public String getFollowUp() {
		return followUp;
	}

	public void setFollowUp(String followUp) {
		this.followUp = followUp;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public List<TmpComplianceReviewPicFollowup> getTmpComplianceReviewPicFollowupPoints() {
		return tmpComplianceReviewPicFollowupPoints;
	}

	public void setTmpComplianceReviewPicFollowupPoints(List<TmpComplianceReviewPicFollowup> tmpComplianceReviewPicFollowupPoints) {
		this.tmpComplianceReviewPicFollowupPoints = tmpComplianceReviewPicFollowupPoints;
	}

}