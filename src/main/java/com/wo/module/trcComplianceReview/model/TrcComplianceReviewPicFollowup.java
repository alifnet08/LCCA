package com.wo.module.trcComplianceReview.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.user.model.User;

public class TrcComplianceReviewPicFollowup extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 6140880530217262281L;
	private Long complianceReviewPicFollowupId;
	private TrcComplianceReview trcComplianceReview;

	private User user1;
	private User user2;
	private User user3;

	private Date targetDate;
	private ParameterDetail followupStatus;
	private ParameterDetail complianceStatus;
	private String complianceNote;
	private User complianceBy;
	private Date complianceDate;

	private Long delId;

	private Long divisionId;

	private String divisionName;

	private Integer sequence;

	private List<TrcComplianceReviewPicFollowupEmail> trcComplianceReviewPicFollowupEmails;
	private List<TrcComplianceReviewPicFollowupPoints> trcComplianceReviewPicFollowupPoints;
	private List<TrcComplianceReviewPicFollowupFindings> trcComplianceReviewPicFollowupFindings;
	private List<TrcComplianceReviewPicFollowupReview> trcComplianceReviewPicFollowupReviews;
	private List<TrcComplianceReviewPicFollowupRegulation> trcComplianceReviewPicFollowupRegulations;
	private boolean disableComplianceStatus;
	
	// transient
	private ParameterDetail oldComplianceStatus;

	public Long getComplianceReviewPicFollowupId() {
		return complianceReviewPicFollowupId;
	}

	public void setComplianceReviewPicFollowupId(Long complianceReviewPicFollowupId) {
		this.complianceReviewPicFollowupId = complianceReviewPicFollowupId;
	}

	public Long getDivisionId() {
		return divisionId;
	}

	public void setDivisionId(Long divisionId) {
		this.divisionId = divisionId;
	}

	public User getUser1() {
		return user1;
	}

	public void setUser1(User user1) {
		this.user1 = user1;
	}

	public User getUser2() {
		return user2;
	}

	public void setUser2(User user2) {
		this.user2 = user2;
	}

	public User getUser3() {
		return user3;
	}

	public void setUser3(User user3) {
		this.user3 = user3;
	}

	public Date getTargetDate() {
		return targetDate;
	}

	public void setTargetDate(Date targetDate) {
		this.targetDate = targetDate;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public Long getDelId() {
		return delId;
	}

	public void setDelId(Long delId) {
		this.delId = delId;
	}

	public String getDivisionName() {
		return divisionName;
	}

	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public TrcComplianceReview getTrcComplianceReview() {
		return trcComplianceReview;
	}

	public void setTrcComplianceReview(TrcComplianceReview trcComplianceReview) {
		this.trcComplianceReview = trcComplianceReview;
	}

	public ParameterDetail getFollowupStatus() {
		return followupStatus;
	}

	public void setFollowupStatus(ParameterDetail followupStatus) {
		this.followupStatus = followupStatus;
	}

	public ParameterDetail getComplianceStatus() {
		return complianceStatus;
	}

	public void setComplianceStatus(ParameterDetail complianceStatus) {
		this.complianceStatus = complianceStatus;
	}

	public String getComplianceNote() {
		return complianceNote;
	}

	public void setComplianceNote(String complianceNote) {
		this.complianceNote = complianceNote;
	}

	public User getComplianceBy() {
		return complianceBy;
	}

	public void setComplianceBy(User complianceBy) {
		this.complianceBy = complianceBy;
	}

	public Date getComplianceDate() {
		return complianceDate;
	}

	public void setComplianceDate(Date complianceDate) {
		this.complianceDate = complianceDate;
	}

	public List<TrcComplianceReviewPicFollowupEmail> getTrcComplianceReviewPicFollowupEmails() {
		return trcComplianceReviewPicFollowupEmails;
	}

	public void setTrcComplianceReviewPicFollowupEmails(
			List<TrcComplianceReviewPicFollowupEmail> trcComplianceReviewPicFollowupEmails) {
		this.trcComplianceReviewPicFollowupEmails = trcComplianceReviewPicFollowupEmails;
	}

	public List<TrcComplianceReviewPicFollowupPoints> getTrcComplianceReviewPicFollowupPoints() {
		return trcComplianceReviewPicFollowupPoints;
	}

	public void setTrcComplianceReviewPicFollowupPoints(
			List<TrcComplianceReviewPicFollowupPoints> trcComplianceReviewPicFollowupPoints) {
		this.trcComplianceReviewPicFollowupPoints = trcComplianceReviewPicFollowupPoints;
	}

	public List<TrcComplianceReviewPicFollowupFindings> getTrcComplianceReviewPicFollowupFindings() {
		return trcComplianceReviewPicFollowupFindings;
	}

	public void setTrcComplianceReviewPicFollowupFindings(
			List<TrcComplianceReviewPicFollowupFindings> trcComplianceReviewPicFollowupFindings) {
		this.trcComplianceReviewPicFollowupFindings = trcComplianceReviewPicFollowupFindings;
	}

	public List<TrcComplianceReviewPicFollowupReview> getTrcComplianceReviewPicFollowupReviews() {
		return trcComplianceReviewPicFollowupReviews;
	}

	public void setTrcComplianceReviewPicFollowupReviews(
			List<TrcComplianceReviewPicFollowupReview> trcComplianceReviewPicFollowupReviews) {
		this.trcComplianceReviewPicFollowupReviews = trcComplianceReviewPicFollowupReviews;
	}

	public ParameterDetail getOldComplianceStatus() {
		return oldComplianceStatus;
	}

	public void setOldComplianceStatus(ParameterDetail oldComplianceStatus) {
		this.oldComplianceStatus = oldComplianceStatus;
	}

	public boolean isDisableComplianceStatus() {
		return disableComplianceStatus;
	}

	public void setDisableComplianceStatus(boolean disableComplianceStatus) {
		this.disableComplianceStatus = disableComplianceStatus;
	}

	public List<TrcComplianceReviewPicFollowupRegulation> getTrcComplianceReviewPicFollowupRegulations() {
		return trcComplianceReviewPicFollowupRegulations;
	}

	public void setTrcComplianceReviewPicFollowupRegulations(List<TrcComplianceReviewPicFollowupRegulation> trcComplianceReviewPicFollowupRegulations) {
		this.trcComplianceReviewPicFollowupRegulations = trcComplianceReviewPicFollowupRegulations;
	}

}
