package com.wo.module.complianceTestingMockup.model;

import java.io.Serializable;

import com.wo.module.common.model.BaseEntity;

public class ComplianceTestingPICFollowupAttachment extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 4701579265839588096L;
	
	private Long complianceTestingPICFollowAttachId;
	private ComplianceTestingPICFollowup complianceTestingPICFollowup;
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
	public Long getComplianceTestingPICFollowAttachId() {
		return complianceTestingPICFollowAttachId;
	}
	public void setComplianceTestingPICFollowAttachId(Long complianceTestingPICFollowAttachId) {
		this.complianceTestingPICFollowAttachId = complianceTestingPICFollowAttachId;
	}
	public ComplianceTestingPICFollowup getComplianceTestingPICFollowup() {
		return complianceTestingPICFollowup;
	}
	public void setComplianceTestingPICFollowup(ComplianceTestingPICFollowup complianceTestingPICFollowup) {
		this.complianceTestingPICFollowup = complianceTestingPICFollowup;
	}
	
	
	
	
	
	
}