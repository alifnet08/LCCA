package com.wo.module.trcAudit.model;

import java.io.Serializable;

import com.wo.module.common.model.BaseEntity;

public class TrcAuditDocument extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -6239388126726110824L;
	private Long auditDocumentId;
	private TrcAudit trcAudit;
	private String attachmentFile;
	private String fileId;
	private Long fileSize;

	public Long getAuditDocumentId() {
		return auditDocumentId;
	}

	public void setAuditDocumentId(Long auditDocumentId) {
		this.auditDocumentId = auditDocumentId;
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

	public TrcAudit getTrcAudit() {
		return trcAudit;
	}

	public void setTrcAudit(TrcAudit trcAudit) {
		this.trcAudit = trcAudit;
	}

}
