package com.wo.module.tmpComplianceReview.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.user.model.User;

public class TmpComplianceReviewPicFollowup extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 6140880530217262281L;
	private Long complianceReviewPicFollowupId;
	private TmpComplianceReview tmpComplianceReview;

	private Long divisionId;

	private User user1;
	private User user2;
	private User user3;

	private Date targetDate;
	private String notes;

	private Date oldTargetDate;

	private Long delId;

	private String divisionName;

	private Integer sequence;

	private Boolean isEditableTemp;
	
	private boolean disableEdit;

	private List<TmpComplianceReviewPicFollowupEmail> tmpComplianceReviewPicFollowupEmails;

	private List<TmpComplianceReviewPicFollowupReschedule> tmpComplianceReviewPicFollowupReschedules;

	private List<TmpComplianceReviewPicFollowupFindings> tmpComplianceReviewPicFollowupFindings;

	private List<TmpComplianceReviewPicFollowupPoints> tmpComplianceReviewPicFollowupPoints;

	private List<TmpComplianceReviewPicFollowupReview> tmpComplianceReviewPicFollowupReviews;
	
	private List<TmpComplianceReviewPicFollowupRegulation> tmpComplianceReviewPicFollowupRegulations;

	// transient
	private TmpComplianceReviewPicFollowupReviewTableModel<TmpComplianceReviewPicFollowupReview> tmpComplianceReviewPicFollowupReviewsList;
	private TmpComplianceReviewPicFollowupFindingsTableModel<TmpComplianceReviewPicFollowupFindings> tmpComplianceReviewPicFollowupFindingList;
	private TmpComplianceReviewPicFollowupPointsTableModel<TmpComplianceReviewPicFollowupPoints> tmpComplianceReviewPicFollowupPointsList;
	private TmpComplianceReviewPicFollowupRegulationsTableModel<TmpComplianceReviewPicFollowupRegulation> tmpComplianceReviewPicFollowupRegulationsList;
	private int lastSequenceOfFollowupAreaReviewPoints;
	
	public Long getComplianceReviewPicFollowupId() {
		return complianceReviewPicFollowupId;
	}

	public void setComplianceReviewPicFollowupId(Long complianceReviewPicFollowupId) {
		this.complianceReviewPicFollowupId = complianceReviewPicFollowupId;
	}

	public TmpComplianceReview getTmpComplianceReview() {
		return tmpComplianceReview;
	}

	public void setTmpComplianceReview(TmpComplianceReview tmpComplianceReview) {
		this.tmpComplianceReview = tmpComplianceReview;
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

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public List<TmpComplianceReviewPicFollowupEmail> getTmpComplianceReviewPicFollowupEmails() {
		return tmpComplianceReviewPicFollowupEmails;
	}

	public void setTmpComplianceReviewPicFollowupEmails(
			List<TmpComplianceReviewPicFollowupEmail> tmpComplianceReviewPicFollowupEmails) {
		this.tmpComplianceReviewPicFollowupEmails = tmpComplianceReviewPicFollowupEmails;
	}

	public List<TmpComplianceReviewPicFollowupReschedule> getTmpComplianceReviewPicFollowupReschedules() {
		return tmpComplianceReviewPicFollowupReschedules;
	}

	public void setTmpComplianceReviewPicFollowupReschedules(
			List<TmpComplianceReviewPicFollowupReschedule> tmpComplianceReviewPicFollowupReschedules) {
		this.tmpComplianceReviewPicFollowupReschedules = tmpComplianceReviewPicFollowupReschedules;
	}

	public Date getOldTargetDate() {
		return oldTargetDate;
	}

	public void setOldTargetDate(Date oldTargetDate) {
		this.oldTargetDate = oldTargetDate;
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

	public Boolean getIsEditableTemp() {
		return isEditableTemp;
	}

	public void setIsEditableTemp(Boolean isEditableTemp) {
		this.isEditableTemp = isEditableTemp;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@SuppressWarnings("unchecked")
	public List<TmpComplianceReviewPicFollowupFindings> getTmpComplianceReviewPicFollowupFindings() {
		return ((List<TmpComplianceReviewPicFollowupFindings>) tmpComplianceReviewPicFollowupFindingList
				.getWrappedData());
	}

	public void setTmpComplianceReviewPicFollowupFindings(
			List<TmpComplianceReviewPicFollowupFindings> tmpComplianceReviewPicFollowupFindings) {
		this.tmpComplianceReviewPicFollowupFindings = tmpComplianceReviewPicFollowupFindings;
	}

	@SuppressWarnings("unchecked")
	public List<TmpComplianceReviewPicFollowupPoints> getTmpComplianceReviewPicFollowupPoints() {
		return ((List<TmpComplianceReviewPicFollowupPoints>) tmpComplianceReviewPicFollowupPointsList.getWrappedData());
	}

	public void setTmpComplianceReviewPicFollowupPoints(
			List<TmpComplianceReviewPicFollowupPoints> tmpComplianceReviewPicFollowupPoints) {
		this.tmpComplianceReviewPicFollowupPoints = tmpComplianceReviewPicFollowupPoints;
	}

	@SuppressWarnings("unchecked")
	public List<TmpComplianceReviewPicFollowupReview> getTmpComplianceReviewPicFollowupReviews() {
		return ((List<TmpComplianceReviewPicFollowupReview>) tmpComplianceReviewPicFollowupReviewsList
				.getWrappedData());
	}

	public void setTmpComplianceReviewPicFollowupReviews(
			List<TmpComplianceReviewPicFollowupReview> tmpComplianceReviewPicFollowupReviews) {
		this.tmpComplianceReviewPicFollowupReviews = tmpComplianceReviewPicFollowupReviews;
	}

	public TmpComplianceReviewPicFollowupReviewTableModel<TmpComplianceReviewPicFollowupReview> getTmpComplianceReviewPicFollowupReviewsList() {
		if (tmpComplianceReviewPicFollowupReviews != null) {
			tmpComplianceReviewPicFollowupReviewsList = new TmpComplianceReviewPicFollowupReviewTableModel<TmpComplianceReviewPicFollowupReview>(
					tmpComplianceReviewPicFollowupReviews);
		}
		
		return tmpComplianceReviewPicFollowupReviewsList;
	}

	public void setTmpComplianceReviewPicFollowupReviewsList(
			TmpComplianceReviewPicFollowupReviewTableModel<TmpComplianceReviewPicFollowupReview> tmpComplianceReviewPicFollowupReviewsList) {
		this.tmpComplianceReviewPicFollowupReviewsList = tmpComplianceReviewPicFollowupReviewsList;
	}

	public TmpComplianceReviewPicFollowupFindingsTableModel<TmpComplianceReviewPicFollowupFindings> getTmpComplianceReviewPicFollowupFindingList() {
		if (tmpComplianceReviewPicFollowupFindings != null) {
			tmpComplianceReviewPicFollowupFindingList = new TmpComplianceReviewPicFollowupFindingsTableModel<TmpComplianceReviewPicFollowupFindings>(
					tmpComplianceReviewPicFollowupFindings);
		}
		
		return tmpComplianceReviewPicFollowupFindingList;
	}

	public void setTmpComplianceReviewPicFollowupFindingList(
			TmpComplianceReviewPicFollowupFindingsTableModel<TmpComplianceReviewPicFollowupFindings> tmpComplianceReviewPicFollowupFindingList) {
		this.tmpComplianceReviewPicFollowupFindingList = tmpComplianceReviewPicFollowupFindingList;
	}

	public TmpComplianceReviewPicFollowupPointsTableModel<TmpComplianceReviewPicFollowupPoints> getTmpComplianceReviewPicFollowupPointsList() {
		if (tmpComplianceReviewPicFollowupPoints != null) {
			tmpComplianceReviewPicFollowupPointsList = new TmpComplianceReviewPicFollowupPointsTableModel<TmpComplianceReviewPicFollowupPoints>(
					tmpComplianceReviewPicFollowupPoints);
		}
		return tmpComplianceReviewPicFollowupPointsList;
	}

	public void setTmpComplianceReviewPicFollowupPointsList(
			TmpComplianceReviewPicFollowupPointsTableModel<TmpComplianceReviewPicFollowupPoints> tmpComplianceReviewPicFollowupPointsList) {
		this.tmpComplianceReviewPicFollowupPointsList = tmpComplianceReviewPicFollowupPointsList;
	}

	public boolean getDisableEdit() {
		return disableEdit;
	}

	public void setDisableEdit(boolean disableEdit) {
		this.disableEdit = disableEdit;
	}

	public int getLastSequenceOfFollowupAreaReviewPoints() {
		return lastSequenceOfFollowupAreaReviewPoints;
	}

	public void setLastSequenceOfFollowupAreaReviewPoints(int lastSequenceOfFollowupAreaReviewPoints) {
		this.lastSequenceOfFollowupAreaReviewPoints = lastSequenceOfFollowupAreaReviewPoints;
	}

	@SuppressWarnings("unchecked")
	public List<TmpComplianceReviewPicFollowupRegulation> getTmpComplianceReviewPicFollowupRegulations() {
		return ((List<TmpComplianceReviewPicFollowupRegulation>) tmpComplianceReviewPicFollowupRegulationsList
				.getWrappedData());
	}

	public void setTmpComplianceReviewPicFollowupRegulations(List<TmpComplianceReviewPicFollowupRegulation> tmpComplianceReviewPicFollowupRegulations) {
		this.tmpComplianceReviewPicFollowupRegulations = tmpComplianceReviewPicFollowupRegulations;
	}

	public TmpComplianceReviewPicFollowupRegulationsTableModel<TmpComplianceReviewPicFollowupRegulation> getTmpComplianceReviewPicFollowupRegulationsList() {
		if (tmpComplianceReviewPicFollowupRegulations != null) {
			tmpComplianceReviewPicFollowupRegulationsList = new TmpComplianceReviewPicFollowupRegulationsTableModel<TmpComplianceReviewPicFollowupRegulation>(
					tmpComplianceReviewPicFollowupRegulations);
		}
		return tmpComplianceReviewPicFollowupRegulationsList;
	}

	public void setTmpComplianceReviewPicFollowupRegulationsList(
			TmpComplianceReviewPicFollowupRegulationsTableModel<TmpComplianceReviewPicFollowupRegulation> tmpComplianceReviewPicFollowupRegulationsList) {
		this.tmpComplianceReviewPicFollowupRegulationsList = tmpComplianceReviewPicFollowupRegulationsList;
	}

}
