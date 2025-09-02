package com.wo.module.trcFineApproval.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.rc.model.RC;
import com.wo.module.user.model.User;

public class TrcFinePicFollowup extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 6140880530217262281L;
	private Long finePicFollowupId;
	private TrcFine trcFine;
	private RC rc;
	private Date fineDebitted;
	private Long fineAmount;
	private String breaches;
	private String rootCause;
	private String category;
	private Long divisionId;
	private User userId1;
	private User userId2;
	private User userId3;
	private Date targetDate;
	private Date targetResponseDate;
	private String rescheduleReason;
	private String userMaker;
	private String userSpv;
	private String noErr;
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
	
	private List<TrcFinePicFollowupEmail> trcFinePicFollowupEmails;
	private List<TrcFinePicFollowupAttachment> trcFinePicFollowupAttachments;
	
	
	private List<TrcFinePicFollowupAttachment> trcFinePicExtendeds;
	private List<TrcFinePicFollowupAttachment> trcFinePicFollowups;
	private List<TrcFinePicFollowupHistory> trcFinePicFollowupHis;
	
	private int sequence;

	public TrcFinePicFollowup() {
		super();
	}

	public Long getFinePicFollowupId() {
		return finePicFollowupId;
	}

	public void setFinePicFollowupId(Long finePicFollowupId) {
		this.finePicFollowupId = finePicFollowupId;
	}

	public TrcFine getTrcFine() {
		return trcFine;
	}

	public void setTrcFine(TrcFine trcFine) {
		this.trcFine = trcFine;
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

	public User getUserId1() {
		return userId1;
	}

	public void setUserId1(User userId1) {
		this.userId1 = userId1;
	}

	public User getUserId2() {
		return userId2;
	}

	public void setUserId2(User userId2) {
		this.userId2 = userId2;
	}

	public User getUserId3() {
		return userId3;
	}

	public void setUserId3(User userId3) {
		this.userId3 = userId3;
	}

	public Date getTargetDate() {
		return targetDate;
	}

	public void setTargetDate(Date targetDate) {
		this.targetDate = targetDate;
	}

	public String getRescheduleReason() {
		return rescheduleReason;
	}

	public void setRescheduleReason(String rescheduleReason) {
		this.rescheduleReason = rescheduleReason;
	}

	public List<TrcFinePicFollowupEmail> getTrcFinePicFollowupEmails() {
		return trcFinePicFollowupEmails;
	}

	public void setTrcFinePicFollowupEmails(List<TrcFinePicFollowupEmail> trcFinePicFollowupEmails) {
		this.trcFinePicFollowupEmails = trcFinePicFollowupEmails;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<TrcFinePicFollowupAttachment> getTrcFinePicFollowupAttachments() {
		return trcFinePicFollowupAttachments;
	}

	public void setTrcFinePicFollowupAttachments(List<TrcFinePicFollowupAttachment> trcFinePicFollowupAttachments) {
		this.trcFinePicFollowupAttachments = trcFinePicFollowupAttachments;
	}

	public String getUserMaker() {
		return userMaker;
	}

	public void setUserMaker(String userMaker) {
		this.userMaker = userMaker;
	}

	public String getUserSpv() {
		return userSpv;
	}

	public void setUserSpv(String userSpv) {
		this.userSpv = userSpv;
	}

	public String getNoErr() {
		return noErr;
	}

	public void setNoErr(String noErr) {
		this.noErr = noErr;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
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

	public Date getTargetResponseDate() {
		return targetResponseDate;
	}

	public void setTargetResponseDate(Date targetResponseDate) {
		this.targetResponseDate = targetResponseDate;
	}

	public ParameterDetail getTargetDateStatus() {
		return targetDateStatus;
	}

	public void setTargetDateStatus(ParameterDetail targetDateStatus) {
		this.targetDateStatus = targetDateStatus;
	}

	public List<TrcFinePicFollowupAttachment> getTrcFinePicExtendeds() {
		return trcFinePicExtendeds;
	}

	public void setTrcFinePicExtendeds(List<TrcFinePicFollowupAttachment> trcFinePicExtendeds) {
		this.trcFinePicExtendeds = trcFinePicExtendeds;
	}

	public List<TrcFinePicFollowupAttachment> getTrcFinePicFollowups() {
		return trcFinePicFollowups;
	}

	public void setTrcFinePicFollowups(List<TrcFinePicFollowupAttachment> trcFinePicFollowups) {
		this.trcFinePicFollowups = trcFinePicFollowups;
	}

	public List<TrcFinePicFollowupHistory> getTrcFinePicFollowupHis() {
		return trcFinePicFollowupHis;
	}

	public void setTrcFinePicFollowupHis(List<TrcFinePicFollowupHistory> trcFinePicFollowupHis) {
		this.trcFinePicFollowupHis = trcFinePicFollowupHis;
	}
	

}
