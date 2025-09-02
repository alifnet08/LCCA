package com.wo.module.complianceTestingMockup.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class StatusKonfirmasiVO implements Serializable {
	private static final long serialVersionUID = 3019374791490640764L;

	private List<AttachmentVO> attachments;
	private List<AttachmentVO> attachmentsPerpanjangan;
	private String followupStatus;
	private String followupBy;
	private String confirmationDate;
	private String followupDate;
	private String followupNote;
	private String newTargetDate;
	private String perpanjanganNote;
	private String complianceDate;
	private String complianceStatus;
	private String complianceNote;
	
	
	private int sequence;


	public List<AttachmentVO> getAttachments() {
		return attachments;
	}


	public void setAttachments(List<AttachmentVO> attachments) {
		this.attachments = attachments;
	}


	public String getFollowupStatus() {
		return followupStatus;
	}


	public void setFollowupStatus(String followupStatus) {
		this.followupStatus = followupStatus;
	}


	public String getFollowupBy() {
		return followupBy;
	}


	public void setFollowupBy(String followupBy) {
		this.followupBy = followupBy;
	}


	public String getConfirmationDate() {
		return confirmationDate;
	}


	public void setConfirmationDate(String confirmationDate) {
		this.confirmationDate = confirmationDate;
	}


	public String getFollowupDate() {
		return followupDate;
	}


	public void setFollowupDate(String followupDate) {
		this.followupDate = followupDate;
	}


	public String getFollowupNote() {
		return followupNote;
	}


	public void setFollowupNote(String followupNote) {
		this.followupNote = followupNote;
	}


	public String getNewTargetDate() {
		return newTargetDate;
	}


	public void setNewTargetDate(String newTargetDate) {
		this.newTargetDate = newTargetDate;
	}


	public String getPerpanjanganNote() {
		return perpanjanganNote;
	}


	public void setPerpanjanganNote(String perpanjanganNote) {
		this.perpanjanganNote = perpanjanganNote;
	}


	public String getComplianceDate() {
		return complianceDate;
	}


	public void setComplianceDate(String complianceDate) {
		this.complianceDate = complianceDate;
	}


	public String getComplianceStatus() {
		return complianceStatus;
	}


	public void setComplianceStatus(String complianceStatus) {
		this.complianceStatus = complianceStatus;
	}


	public String getComplianceNote() {
		return complianceNote;
	}


	public void setComplianceNote(String complianceNote) {
		this.complianceNote = complianceNote;
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


	public List<AttachmentVO> getAttachmentsPerpanjangan() {
		return attachmentsPerpanjangan;
	}


	public void setAttachmentsPerpanjangan(List<AttachmentVO> attachmentsPerpanjangan) {
		this.attachmentsPerpanjangan = attachmentsPerpanjangan;
	}
	


	

	
	
}