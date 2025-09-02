package com.wo.module.complianceTestingMockup.model;

import java.io.Serializable;

import com.wo.module.common.model.BaseEntity;

public class ComplianceTestingDoc extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 4701579265839588096L;
	
	private Long complianceTestingAttachId;
	private ComplianceTesting complianceTesting;
	private String attachmentFile;
	private String fileId;
	private Long fileSize;
	
	
	public Long getComplianceTestingAttachId() {
		return complianceTestingAttachId;
	}
	public void setComplianceTestingAttachId(Long complianceTestingAttachId) {
		this.complianceTestingAttachId = complianceTestingAttachId;
	}
	public ComplianceTesting getComplianceTesting() {
		return complianceTesting;
	}
	public void setComplianceTesting(ComplianceTesting complianceTesting) {
		this.complianceTesting = complianceTesting;
	}
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
	
	
	
	
	
	
}