package com.wo.module.trcFineApproval.model;

import java.io.Serializable;
import java.util.Date;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.rc.model.RC;
import com.wo.module.user.model.User;

public class TrcFinePicFollowupHistory extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -7707945018477636612L;
	
	private Long finePicFollowupHistoryId;
	
	private TrcFinePicFollowup trcFinePicFollowup;
	
	private RC rc;
	private Date fineDebitted;
	private Long fineAmount;
	private String breaches;
	private String rootCause;
	private String category;
	private Long divisionId;
	private Date targetDate;
	private Date targetResponseDate;
	private String rescheduleReason;
	private String notes;
	private ParameterDetail followupStatus;

	private User followupBy;
	private Date confirmationDate;
	private Date followupDate;
	private String followupNote;
	private ParameterDetail complianceStatus;
	private ParameterDetail targetDateStatus;
	private String complianceNote;
	private User complianceBy;
	private Date complianceDate;
	
	
	public TrcFinePicFollowupHistory() {
	}
	public Long getFinePicFollowupHistoryId() {
		return finePicFollowupHistoryId;
	}
	public void setFinePicFollowupHistoryId(Long finePicFollowupHistoryId) {
		this.finePicFollowupHistoryId = finePicFollowupHistoryId;
	}
	public TrcFinePicFollowup getTrcFinePicFollowup() {
		return trcFinePicFollowup;
	}
	public void setTrcFinePicFollowup(TrcFinePicFollowup trcFinePicFollowup) {
		this.trcFinePicFollowup = trcFinePicFollowup;
	}
	public RC getRc() {
		return rc;
	}
	public void setRc(RC rc) {
		this.rc = rc;
	}
	public Date getFineDebitted() {
		return fineDebitted;
	}
	public void setFineDebitted(Date fineDebitted) {
		this.fineDebitted = fineDebitted;
	}
	public Long getFineAmount() {
		return fineAmount;
	}
	public void setFineAmount(Long fineAmount) {
		this.fineAmount = fineAmount;
	}
	public String getBreaches() {
		return breaches;
	}
	public void setBreaches(String breaches) {
		this.breaches = breaches;
	}
	public String getRootCause() {
		return rootCause;
	}
	public void setRootCause(String rootCause) {
		this.rootCause = rootCause;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public Long getDivisionId() {
		return divisionId;
	}
	public void setDivisionId(Long divisionId) {
		this.divisionId = divisionId;
	}
	public Date getTargetDate() {
		return targetDate;
	}
	public void setTargetDate(Date targetDate) {
		this.targetDate = targetDate;
	}
	public Date getTargetResponseDate() {
		return targetResponseDate;
	}
	public void setTargetResponseDate(Date targetResponseDate) {
		this.targetResponseDate = targetResponseDate;
	}
	public String getRescheduleReason() {
		return rescheduleReason;
	}
	public void setRescheduleReason(String rescheduleReason) {
		this.rescheduleReason = rescheduleReason;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public ParameterDetail getFollowupStatus() {
		return followupStatus;
	}
	public void setFollowupStatus(ParameterDetail followupStatus) {
		this.followupStatus = followupStatus;
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
	public ParameterDetail getComplianceStatus() {
		return complianceStatus;
	}
	public void setComplianceStatus(ParameterDetail complianceStatus) {
		this.complianceStatus = complianceStatus;
	}
	public ParameterDetail getTargetDateStatus() {
		return targetDateStatus;
	}
	public void setTargetDateStatus(ParameterDetail targetDateStatus) {
		this.targetDateStatus = targetDateStatus;
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
	
	
	
}
