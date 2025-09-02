package com.wo.module.tmpAudit.model;

import java.io.Serializable;

import com.wo.module.common.model.BaseEntity;

public class TmpAuditDocument extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1537367121272308611L;
	private Long auditDocumentId;
//	private String documentType;
	private TmpAudit tmpAudit;
	private String attachmentFile;
	private String fileId;
	private Long fileSize;

	public Long getAuditDocumentId() {
		return auditDocumentId;
	}

	public void setAuditDocumentId(Long auditDocumentId) {
		this.auditDocumentId = auditDocumentId;
	}

//	public String getDocumentType() {
//		return documentType;
//	}
//
//	public void setDocumentType(String documentType) {
//		this.documentType = documentType;
//	}

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

	public TmpAudit getTmpAudit() {
		return tmpAudit;
	}

	public void setTmpAudit(TmpAudit tmpAudit) {
		this.tmpAudit = tmpAudit;
	}

}
