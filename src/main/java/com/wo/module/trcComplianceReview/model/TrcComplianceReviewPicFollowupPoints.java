package com.wo.module.trcComplianceReview.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.common.model.UploadedFileWO;
import com.wo.module.user.model.User;

public class TrcComplianceReviewPicFollowupPoints extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -2113211494356274810L;
	private Long complianceReviewPicFollowupPointsId;
	private TrcComplianceReviewPicFollowup trcComplianceReviewPicFollowup;
	private String followupPoints;
	private User followupBy; 
	private String strFollowupBy; 
	private Date confirmationDate;
	private String strConfirmationDate;
	private Date followupDate; 
	private String strFollowupDate; 
	private String followupNote; 
	
	private List<TrcComplianceReviewPicFollowupPointsAttachment> trcComplianceReviewPicFollowupPointsAttachments;
	
	// transient
	private int seq;
	private List<UploadedFileWO> uploadedFilesEvidence = new ArrayList<UploadedFileWO>();
	
	public Long getComplianceReviewPicFollowupPointsId() {
		return complianceReviewPicFollowupPointsId;
	}

	public void setComplianceReviewPicFollowupPointsId(Long complianceReviewPicFollowupPointsId) {
		this.complianceReviewPicFollowupPointsId = complianceReviewPicFollowupPointsId;
	}

	public String getFollowupPoints() {
		return followupPoints;
	}

	public void setFollowupPoints(String followupPoints) {
		this.followupPoints = followupPoints;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public TrcComplianceReviewPicFollowup getTrcComplianceReviewPicFollowup() {
		return trcComplianceReviewPicFollowup;
	}

	public void setTrcComplianceReviewPicFollowup(TrcComplianceReviewPicFollowup trcComplianceReviewPicFollowup) {
		this.trcComplianceReviewPicFollowup = trcComplianceReviewPicFollowup;
	}

	public User getFollowupBy() {
		return followupBy;
	}

	public void setFollowupBy(User followupBy) {
		this.followupBy = followupBy;
	}

	public Date getConfirmationDate() {
		return confirmationDate;
	}

	public void setConfirmationDate(Date confirmationDate) {
		this.confirmationDate = confirmationDate;
	}

	public Date getFollowupDate() {
		return followupDate;
	}

	public void setFollowupDate(Date followupDate) {
		this.followupDate = followupDate;
	}

	public String getFollowupNote() {
		return followupNote;
	}

	public void setFollowupNote(String followupNote) {
		this.followupNote = followupNote;
	}

	public List<TrcComplianceReviewPicFollowupPointsAttachment> getTrcComplianceReviewPicFollowupPointsAttachments() {
		return trcComplianceReviewPicFollowupPointsAttachments;
	}

	public void setTrcComplianceReviewPicFollowupPointsAttachments(
			List<TrcComplianceReviewPicFollowupPointsAttachment> trcComplianceReviewPicFollowupPointsAttachments) {
		this.trcComplianceReviewPicFollowupPointsAttachments = trcComplianceReviewPicFollowupPointsAttachments;
	}

	public List<UploadedFileWO> getUploadedFilesEvidence() {
		return uploadedFilesEvidence;
	}

	public void setUploadedFilesEvidence(List<UploadedFileWO> uploadedFilesEvidence) {
		this.uploadedFilesEvidence = uploadedFilesEvidence;
	}

	public String getStrFollowupBy() {
		return strFollowupBy;
	}

	public void setStrFollowupBy(String strFollowupBy) {
		this.strFollowupBy = strFollowupBy;
	}

	public String getStrConfirmationDate() {
		return strConfirmationDate;
	}

	public void setStrConfirmationDate(String strConfirmationDate) {
		this.strConfirmationDate = strConfirmationDate;
	}

	public String getStrFollowupDate() {
		return strFollowupDate;
	}

	public void setStrFollowupDate(String strFollowupDate) {
		this.strFollowupDate = strFollowupDate;
	}

}
