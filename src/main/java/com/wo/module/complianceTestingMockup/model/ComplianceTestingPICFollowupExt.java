package com.wo.module.complianceTestingMockup.model;

import java.io.Serializable;
import java.util.Date;

import com.wo.module.common.model.BaseEntity;

public class ComplianceTestingPICFollowupExt extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 4701579265839588096L;
	
	private Long complianceTestingPICFollowExtId;
	private ComplianceTestingPICFollowup complianceTestingPICFollowup;
	private Date newTargetDate;
	private Date oldTargetDate;
	private String attachmentFile;
	private String fileId;
	private Long fileSize;
	
	public String getAttachmentFile() {
		return attachmentFile;
	}
	public void setAttachmentFile(String attachmentFile) {
		this.attachmentFile = attachmentFile;
	}
	public String getFileId() {
		return fileId;
	}
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	
	public Long getFileSize() {
		return fileSize;
	}
	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public Long getComplianceTestingPICFollowExtId() {
		return complianceTestingPICFollowExtId;
	}
	public void setComplianceTestingPICFollowExtId(Long complianceTestingPICFollowExtId) {
		this.complianceTestingPICFollowExtId = complianceTestingPICFollowExtId;
	}
	public ComplianceTestingPICFollowup getComplianceTestingPICFollowup() {
		return complianceTestingPICFollowup;
	}
	public void setComplianceTestingPICFollowup(ComplianceTestingPICFollowup complianceTestingPICFollowup) {
		this.complianceTestingPICFollowup = complianceTestingPICFollowup;
	}
	public Date getNewTargetDate() {
		return newTargetDate;
	}
	public void setNewTargetDate(Date newTargetDate) {
		this.newTargetDate = newTargetDate;
	}
	public Date getOldTargetDate() {
		return oldTargetDate;
	}
	public void setOldTargetDate(Date oldTargetDate) {
		this.oldTargetDate = oldTargetDate;
	}
	
	
	
	
	
	
}