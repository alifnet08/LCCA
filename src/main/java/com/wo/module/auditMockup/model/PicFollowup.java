package com.wo.module.auditMockup.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.tmpAudit.model.TmpAuditPicFollowupEmail;
import com.wo.module.tmpAudit.model.TmpAuditPicFollowupReschedule;
import com.wo.module.tmpAudit.model.TmpAuditPicFollowupSupportingUnit;
import com.wo.module.user.model.User;

public class PicFollowup extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -1047401298164310412L;
	private Long auditPicFollowupId;
	private BankCommitment bankCommitment;

	private Long divisionId;
	
	private User user1;
	private User user2;
	private User user3;

	private Date targetDate;
	private Date oldTargetDate;
	private String rescheduleReason;
	private String notes;

	private Integer sequence;
 
	private List<TmpAuditPicFollowupSupportingUnit> tmpAuditPicFollowupSupportingUnits; 
	private List<TmpAuditPicFollowupEmail> tmpAuditPicFollowupEmails;
	private List<TmpAuditPicFollowupReschedule> tmpAuditPicFollowupReschedules;
	
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

	public BankCommitment getBankCommitment() {
		return bankCommitment;
	}

	public void setBankCommitment(BankCommitment bankCommitment) {
		this.bankCommitment = bankCommitment;
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

	public Date getOldTargetDate() {
		return oldTargetDate;
	}

	public void setOldTargetDate(Date oldTargetDate) {
		this.oldTargetDate = oldTargetDate;
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

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public Boolean getIsEditableTemp() {
		return isEditableTemp;
	}

	public void setIsEditableTemp(Boolean isEditableTemp) {
		this.isEditableTemp = isEditableTemp;
	}

	public boolean isDisableEdit() {
		return disableEdit;
	}

	public void setDisableEdit(boolean disableEdit) {
		this.disableEdit = disableEdit;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public TmpAuditPicFollowupSupportingUnit[] getSelectedSupportingUnit() {
		return selectedSupportingUnit;
	}

	public void setSelectedSupportingUnit(TmpAuditPicFollowupSupportingUnit[] selectedSupportingUnit) {
		this.selectedSupportingUnit = selectedSupportingUnit;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<TmpAuditPicFollowupSupportingUnit> getTmpAuditPicFollowupSupportingUnits() {
		return tmpAuditPicFollowupSupportingUnits;
	}

	public void setTmpAuditPicFollowupSupportingUnits(
			List<TmpAuditPicFollowupSupportingUnit> tmpAuditPicFollowupSupportingUnits) {
		this.tmpAuditPicFollowupSupportingUnits = tmpAuditPicFollowupSupportingUnits;
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
	
	

}
