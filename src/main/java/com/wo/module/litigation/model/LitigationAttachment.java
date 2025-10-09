package com.wo.module.litigation.model;

import java.io.Serializable;

import com.wo.module.common.model.BaseEntity;

public class LitigationAttachment extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 6395152164269025885L;

	private Long litigationAttachmentId;
	private Litigation litigation;
	private String attachmentCode;
	private String attachmentFile;
	private String fileId;
	private Long fileSize;
	
	public Long getLitigationAttachmentId() {
		return litigationAttachmentId;
	}
	public void setLitigationAttachmentId(Long litigationAttachmentId) {
		this.litigationAttachmentId = litigationAttachmentId;
	}
	
	public String getAttachmentCode() {
		return attachmentCode;
	}
	public void setAttachmentCode(String attachmentCode) {
		this.attachmentCode = attachmentCode;
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
	public Litigation getLitigation() {
		return litigation;
	}
	public void setLitigation(Litigation litigation) {
		this.litigation = litigation;
	}
	
	
	
	
	
	
}
