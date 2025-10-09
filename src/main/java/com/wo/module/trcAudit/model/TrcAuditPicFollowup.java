package com.wo.module.trcAudit.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.common.model.UploadedFileWO;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.user.model.User;

public class TrcAuditPicFollowup extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -1047401298164310412L;
	private Long auditPicFollowupId;
	private TrcAuditPicFollowupBankCommitment trcAuditPicFollowupBankCommitment;
	private TrcAuditPicFollowupRec trcAuditPicFollowupRec;

	private Long divisionId;

	private User user1;
	private User user2;
	private User user3;

	private Date targetDate;
	private Date oldTargetDate;
	private String rescheduleReason;
	private String notes;

	private ParameterDetail followupStatus;
	private User followupBy;
	private Date confirmationDate;
	private Date followupDate;
	private String followupNote;
	private ParameterDetail complianceStatus;
	private String complianceNote;
	private User complianceBy;
	private Date complianceDate;

	private List<TrcAuditPicFollowupEmail> trcAuditPicFollowupEmails;
	private List<TrcAuditPicFollowupAttachment> trcAuditPicFollowupAttachment;
	private List<TrcAuditPicFollowupExt> trcAuditPicFollowupExt;
	private List<TrcAuditPicFollowupSupportingUnit> trcAuditPicFollowupSupportingUnits;  
	private List<TrcAuditPicFollowupRec> trcAuditPicFollowupRecs;

	//transient
	private Boolean isEditableTemp;
	private Integer sequence;
	private List<TrcAuditPicFollowupAttachment> followupAttachDoc;
	private List<TrcAuditPicFollowupAttachment> letterAttachDoc; 
	private ParameterDetail oldComplianceStatus;
	private boolean disableComplianceStatus;
	private List<UploadedFileWO> uploadedFilesAttachmentLetter;
	
	private String isExtension;
	
	private boolean disableEdit;
	//transient
	private boolean checked;

	
	public Long getAuditPicFollowupId() {
		return auditPicFollowupId;
	}

	public void setAuditPicFollowupId(Long auditPicFollowupId) {
		this.auditPicFollowupId = auditPicFollowupId;
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

	public Boolean getIsEditableTemp() {
		return isEditableTemp;
	}

	public void setIsEditableTemp(Boolean isEditableTemp) {
		this.isEditableTemp = isEditableTemp;
	}


	public List<TrcAuditPicFollowupEmail> getTrcAuditPicFollowupEmails() {
		return trcAuditPicFollowupEmails;
	}

	public void setTrcAuditPicFollowupEmails(List<TrcAuditPicFollowupEmail> trcAuditPicFollowupEmails) {
		this.trcAuditPicFollowupEmails = trcAuditPicFollowupEmails;
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

	public List<TrcAuditPicFollowupAttachment> getTrcAuditPicFollowupAttachment() {
		return trcAuditPicFollowupAttachment;
	}

	public void setTrcAuditPicFollowupAttachment(List<TrcAuditPicFollowupAttachment> trcAuditPicFollowupAttachment) {
		this.trcAuditPicFollowupAttachment = trcAuditPicFollowupAttachment;
	}

	public String getFollowupNote() {
		return followupNote;
	}

	public void setFollowupNote(String followupNote) {
		this.followupNote = followupNote;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public String getRescheduleReason() {
		return rescheduleReason;
	}

	public void setRescheduleReason(String rescheduleReason) {
		this.rescheduleReason = rescheduleReason;
	}


	public List<TrcAuditPicFollowupAttachment> getFollowupAttachDoc() {
		if(trcAuditPicFollowupAttachment != null) {
			List<TrcAuditPicFollowupAttachment> temp = trcAuditPicFollowupAttachment.stream().filter( f -> 
				ParameterDetail.PARAM_DET_CODE_FOLLOWUP_ATTACH_DOC.equals(
						f.getAttachmentType() != null ? f.getAttachmentType().getParameterDtlCode() : ""
						)
			).collect(Collectors.toList());
			return temp;
		}
		return followupAttachDoc;
	}

	public void setFollowupAttachDoc(List<TrcAuditPicFollowupAttachment> followupAttachDoc) {
		this.followupAttachDoc = followupAttachDoc;
	}

	public List<TrcAuditPicFollowupAttachment> getLetterAttachDoc() {
		if(trcAuditPicFollowupAttachment != null) {
			List<TrcAuditPicFollowupAttachment> temp = trcAuditPicFollowupAttachment.stream().filter( f -> 
				ParameterDetail.PARAM_DET_CODE_LETTER_ATTACH_DOC.equals(
						f.getAttachmentType() != null ? f.getAttachmentType().getParameterDtlCode() : ""
				)
			).collect(Collectors.toList());
			return temp;
		}
		return letterAttachDoc;
	}

	public void setLetterAttachDoc(List<TrcAuditPicFollowupAttachment> letterAttachDoc) {
		this.letterAttachDoc = letterAttachDoc;
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

	public List<TrcAuditPicFollowupSupportingUnit> getTrcAuditPicFollowupSupportingUnits() {
		return trcAuditPicFollowupSupportingUnits;
	}

	public void setTrcAuditPicFollowupSupportingUnits(List<TrcAuditPicFollowupSupportingUnit> trcAuditPicFollowupSupportingUnits) {
		this.trcAuditPicFollowupSupportingUnits = trcAuditPicFollowupSupportingUnits;
	}

	public List<UploadedFileWO> getUploadedFilesAttachmentLetter() {
		return uploadedFilesAttachmentLetter;
	}

	public void setUploadedFilesAttachmentLetter(List<UploadedFileWO> uploadedFilesAttachmentLetter) {
		this.uploadedFilesAttachmentLetter = uploadedFilesAttachmentLetter;
	}

	public TrcAuditPicFollowupBankCommitment getTrcAuditPicFollowupBankCommitment() {
		return trcAuditPicFollowupBankCommitment;
	}

	public void setTrcAuditPicFollowupBankCommitment(TrcAuditPicFollowupBankCommitment trcAuditPicFollowupBankCommitment) {
		this.trcAuditPicFollowupBankCommitment = trcAuditPicFollowupBankCommitment;
	}

	public String getIsExtension() {
		return isExtension;
	}

	public void setIsExtension(String isExtension) {
		this.isExtension = isExtension;
	}

	public List<TrcAuditPicFollowupExt> getTrcAuditPicFollowupExt() {
		return trcAuditPicFollowupExt;
	}

	public void setTrcAuditPicFollowupExt(List<TrcAuditPicFollowupExt> trcAuditPicFollowupExt) {
		this.trcAuditPicFollowupExt = trcAuditPicFollowupExt;
	}

	public Date getOldTargetDate() {
		return oldTargetDate;
	}

	public void setOldTargetDate(Date oldTargetDate) {
		this.oldTargetDate = oldTargetDate;
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

	public List<TrcAuditPicFollowupRec> getTrcAuditPicFollowupRecs() {
		return trcAuditPicFollowupRecs;
	}

	public void setTrcAuditPicFollowupRecs(List<TrcAuditPicFollowupRec> trcAuditPicFollowupRecs) {
		this.trcAuditPicFollowupRecs = trcAuditPicFollowupRecs;
	}

	public TrcAuditPicFollowupRec getTrcAuditPicFollowupRec() {
		return trcAuditPicFollowupRec;
	}

	public void setTrcAuditPicFollowupRec(TrcAuditPicFollowupRec trcAuditPicFollowupRec) {
		this.trcAuditPicFollowupRec = trcAuditPicFollowupRec;
	}

	

	

	
}
