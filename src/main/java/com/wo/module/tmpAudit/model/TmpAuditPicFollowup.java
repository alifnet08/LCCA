package com.wo.module.tmpAudit.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.user.model.User;

public class TmpAuditPicFollowup extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -1047401298164310412L;
	private Long auditPicFollowupId;
	private TmpAuditPicFollowupBankCommitment tmpAuditPicFollowupBankCommitment;

	private Long divisionId;
	
	private User user1;
	private User user2;
	private User user3;

	private Date targetDate;
	private Date oldTargetDate;
	private String rescheduleReason;
	private String notes;

	private Integer sequence;

	private List<TmpAuditPicFollowupEmail> tmpAuditPicFollowupEmails;
	private List<TmpAuditPicFollowupReschedule> tmpAuditPicFollowupReschedules;
	//private List<TmpAuditPicFollowupAuditFindings> tmpAuditPicFollowupAuditFindings;
	//private List<TmpAuditPicFollowupBankCommitment> tmpAuditPicFollowupBankCommitments;
	//private List<TmpAuditPicFollowupBankResponse> tmpAuditPicFollowupBankResponses;
	private List<TmpAuditPicFollowupSupportingUnit> tmpAuditPicFollowupSupportingUnits; 
	private List<TmpAuditPicFollowupRec> tmpAuditPicFollowupRecs;
	
	private Boolean isEditableTemp;
	private boolean disableEdit;
	//transient
	private boolean checked;
	
	private TmpAuditPicFollowupSupportingUnit[] selectedSupportingUnit;
	
	public Long getAuditPicFollowupId() {
		return auditPicFollowupId;
	}

	public void setAuditPicFollowupId(Long auditPicFollowupId) {
		this.auditPicFollowupId = auditPicFollowupId;
	}

	

	public TmpAuditPicFollowupBankCommitment getTmpAuditPicFollowupBankCommitment() {
		return tmpAuditPicFollowupBankCommitment;
	}

	public void setTmpAuditPicFollowupBankCommitment(TmpAuditPicFollowupBankCommitment tmpAuditPicFollowupBankCommitment) {
		this.tmpAuditPicFollowupBankCommitment = tmpAuditPicFollowupBankCommitment;
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

	public List<TmpAuditPicFollowupEmail> getTmpAuditPicFollowupEmails() {
		return tmpAuditPicFollowupEmails;
	}

	public void setTmpAuditPicFollowupEmails(List<TmpAuditPicFollowupEmail> tmpAuditPicFollowupEmails) {
		this.tmpAuditPicFollowupEmails = tmpAuditPicFollowupEmails;
	}

	public List<TmpAuditPicFollowupReschedule> getTmpAuditPicFollowupReschedules() {
		return tmpAuditPicFollowupReschedules;
	}

	public void setTmpAuditPicFollowupReschedules(List<TmpAuditPicFollowupReschedule> tmpAuditPicFollowupReschedules) {
		this.tmpAuditPicFollowupReschedules = tmpAuditPicFollowupReschedules;
	}

	public Date getOldTargetDate() {
		return oldTargetDate;
	}

	public void setOldTargetDate(Date oldTargetDate) {
		this.oldTargetDate = oldTargetDate;
	}

	public Boolean getIsEditableTemp() {
		return isEditableTemp;
	}

	public void setIsEditableTemp(Boolean isEditableTemp) {
		this.isEditableTemp = isEditableTemp;
	}

	public String getRescheduleReason() {
		return rescheduleReason;
	}

	public void setRescheduleReason(String rescheduleReason) {
		this.rescheduleReason = rescheduleReason;
	}


	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public boolean getDisableEdit() {
		return disableEdit;
	}

	public void setDisableEdit(boolean disableEdit) {
		this.disableEdit = disableEdit;
	}

	public List<TmpAuditPicFollowupSupportingUnit> getTmpAuditPicFollowupSupportingUnits() {
		return tmpAuditPicFollowupSupportingUnits;
	}

	public void setTmpAuditPicFollowupSupportingUnits(List<TmpAuditPicFollowupSupportingUnit> tmpAuditPicFollowupSupportingUnits) {
		this.tmpAuditPicFollowupSupportingUnits = tmpAuditPicFollowupSupportingUnits;
	}

	public TmpAuditPicFollowupSupportingUnit[] getSelectedSupportingUnit() {
		return selectedSupportingUnit;
	}

	public void setSelectedSupportingUnit(TmpAuditPicFollowupSupportingUnit[] selectedSupportingUnit) {
		this.selectedSupportingUnit = selectedSupportingUnit;
	}

	public List<TmpAuditPicFollowupRec> getTmpAuditPicFollowupRecs() {
		return tmpAuditPicFollowupRecs;
	}

	public void setTmpAuditPicFollowupRecs(List<TmpAuditPicFollowupRec> tmpAuditPicFollowupRecs) {
		this.tmpAuditPicFollowupRecs = tmpAuditPicFollowupRecs;
	}

	
}
