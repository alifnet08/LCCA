package com.wo.module.complianceTestingMockup.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.user.model.User;

public class ComplianceTestingPICFollowup extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 4701579265839588096L;
	
	private Long complianceTestingPICFollowupId;
	private ComplianceTestingDtl complianceTestingDtl;
	private Long divisionId;
	private String branchCode;
	private User user1;
	private User user2;
	private User user3;
	private Date targetDate;
	
	private String followup;
	//private String followupStatus;
	private ParameterDetail followupStatus;
	private Long followupById;
	private Date confirmationDate;
	private Date followupDate;
	private String followupNote;
	//private String complianceStatus;
	private ParameterDetail complianceStatus;
	private String complianceNote;
	private Long complianceById;
	private Date complianceDate;
	private String rescheduleReason;
	private String isExtension;
	
	private int sequence;
	private String isFollowup;
	
	private String branchName;
	private String subBranchName;
	
	private boolean checked;
	
	private List<ComplianceTestingPICFollowupAttachment> complianceTestingPicFollowupAttachs;
	private List<ComplianceTestingPICFollowupExt> complianceTestingPicFollowupExts;
	private List<ComplianceTestingPICFollowupEmail> complianceTestingPicFollowupEmails;
	
	
	public Long getComplianceTestingPICFollowupId() {
		return complianceTestingPICFollowupId;
	}
	public void setComplianceTestingPICFollowupId(Long complianceTestingPICFollowupId) {
		this.complianceTestingPICFollowupId = complianceTestingPICFollowupId;
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
	
	public Long getFollowupById() {
		return followupById;
	}
	public void setFollowupById(Long followupById) {
		this.followupById = followupById;
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
	public Long getComplianceById() {
		return complianceById;
	}
	public void setComplianceById(Long complianceById) {
		this.complianceById = complianceById;
	}
	public Date getComplianceDate() {
		return complianceDate;
	}
	public void setComplianceDate(Date complianceDate) {
		this.complianceDate = complianceDate;
	}
	public String getRescheduleReason() {
		return rescheduleReason;
	}
	public void setRescheduleReason(String rescheduleReason) {
		this.rescheduleReason = rescheduleReason;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public ComplianceTestingDtl getComplianceTestingDtl() {
		return complianceTestingDtl;
	}
	public void setComplianceTestingDtl(ComplianceTestingDtl complianceTestingDtl) {
		this.complianceTestingDtl = complianceTestingDtl;
	}
	public String getBranchCode() {
		return branchCode;
	}
	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}
	public String getFollowup() {
		return followup;
	}
	public void setFollowup(String followup) {
		this.followup = followup;
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
	public int getSequence() {
		return sequence;
	}
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
	public String getIsFollowup() {
		return isFollowup;
	}
	public void setIsFollowup(String isFollowup) {
		this.isFollowup = isFollowup;
	}
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	public String getSubBranchName() {
		return subBranchName;
	}
	public void setSubBranchName(String subBranchName) {
		this.subBranchName = subBranchName;
	}
	public List<ComplianceTestingPICFollowupAttachment> getComplianceTestingPicFollowupAttachs() {
		return complianceTestingPicFollowupAttachs;
	}
	public void setComplianceTestingPicFollowupAttachs(
			List<ComplianceTestingPICFollowupAttachment> complianceTestingPicFollowupAttachs) {
		this.complianceTestingPicFollowupAttachs = complianceTestingPicFollowupAttachs;
	}
	public String getIsExtension() {
		return isExtension;
	}
	public void setIsExtension(String isExtension) {
		this.isExtension = isExtension;
	}
	public List<ComplianceTestingPICFollowupExt> getComplianceTestingPicFollowupExts() {
		return complianceTestingPicFollowupExts;
	}
	public void setComplianceTestingPicFollowupExts(
			List<ComplianceTestingPICFollowupExt> complianceTestingPicFollowupExts) {
		this.complianceTestingPicFollowupExts = complianceTestingPicFollowupExts;
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
	public List<ComplianceTestingPICFollowupEmail> getComplianceTestingPicFollowupEmails() {
		return complianceTestingPicFollowupEmails;
	}
	public void setComplianceTestingPicFollowupEmails(
			List<ComplianceTestingPICFollowupEmail> complianceTestingPicFollowupEmails) {
		this.complianceTestingPicFollowupEmails = complianceTestingPicFollowupEmails;
	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	
	
	
	
	
	
	
	
}