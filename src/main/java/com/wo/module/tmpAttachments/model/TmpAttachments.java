package com.wo.module.tmpAttachments.model;

import java.io.Serializable;

import com.wo.module.common.model.BaseEntity;

public class TmpAttachments extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -7790938997804623733L;
	private Long attachmentId;
	private String attachmentType;
	private String attachmentFile;
	private String fileId;
	private Long fileSize;

	public TmpAttachments(Long attachmentId, String attachmentType, String attachmentFile, String fileId, Long fileSize) {
		this.attachmentId = attachmentId;
		this.attachmentType = attachmentType;
		this.attachmentFile = attachmentFile;
		this.fileId = fileId;
		this.fileSize = fileSize;
	}

	public TmpAttachments(String attachmentType, String attachmentFile, String fileId, Long fileSize) {
		this.attachmentType = attachmentType;
		this.attachmentFile = attachmentFile;
		this.fileId = fileId;
		this.fileSize = fileSize;
	}
	
	public TmpAttachments() {
		super();
	}

	public Long getAttachmentId() {
		return attachmentId;
	}

	public void setAttachmentId(Long attachmentId) {
		this.attachmentId = attachmentId;
	}

	public String getAttachmentType() {
		return attachmentType;
	}

	public void setAttachmentType(String attachmentType) {
		this.attachmentType = attachmentType;
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

}
