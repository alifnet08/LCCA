package com.wo.module.complianceReviewDocumentView.model;

import java.io.Serializable;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.complianceReviewDocumentView.model.ComplianceReviewDocumentView;

public class ComplianceReviewDocumentAttachmentView extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 4515938122889578448L;
	
	private Long complianceReviewDocumentAttechmentId;
	private ComplianceReviewDocumentView complianceReviewDocumentView;
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

	public ComplianceReviewDocumentView getComplianceReviewDocumentView() {
		return complianceReviewDocumentView;
	}

	public void setComplianceReviewDocumentView(ComplianceReviewDocumentView complianceReviewDocumentView) {
		this.complianceReviewDocumentView = complianceReviewDocumentView;
	}
}
