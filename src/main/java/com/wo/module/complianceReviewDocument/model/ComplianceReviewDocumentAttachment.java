package com.wo.module.complianceReviewDocument.model;

import java.io.Serializable;

import com.wo.module.common.model.BaseEntity;

public class ComplianceReviewDocumentAttachment extends BaseEntity implements Serializable{

	private static final long serialVersionUID = -4317923639563761435L;
	
	private Long complianceReviewDocumentAttechmentId;
	private ComplianceReviewDocument complianceReviewDocument;
	private String attachmentFile;
	private String fileId;
	private Long fileSize;
	
	private Long delId;

	public Long getComplianceReviewDocumentAttechmentId() {
		return complianceReviewDocumentAttechmentId;
	}

	public void setComplianceReviewDocumentAttechmentId(Long complianceReviewDocumentAttechmentId) {
		this.complianceReviewDocumentAttechmentId = complianceReviewDocumentAttechmentId;
	}

	public ComplianceReviewDocument getComplianceReviewDocument() {
		return complianceReviewDocument;
	}

	public void setComplianceReviewDocument(ComplianceReviewDocument complianceReviewDocument) {
		this.complianceReviewDocument = complianceReviewDocument;
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

	public Long getDelId() {
		return delId;
	}

	public void setDelId(Long delId) {
		this.delId = delId;
	}
}