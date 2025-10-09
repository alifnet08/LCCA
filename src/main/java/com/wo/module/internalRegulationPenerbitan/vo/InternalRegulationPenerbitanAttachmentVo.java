package com.wo.module.internalRegulationPenerbitan.vo;

import java.io.Serializable;

public class InternalRegulationPenerbitanAttachmentVo implements Serializable {

	private static final long serialVersionUID = -7623042331798815524L;
	
	private Long irgAttachmentId;
	private String attachmentFile;
	private String attachmentType;
	private String fileId;
	private Long fileSize;
	
	public Long getIrgAttachmentId() {
		return irgAttachmentId;
	}
	public void setIrgAttachmentId(Long irgAttachmentId) {
		this.irgAttachmentId = irgAttachmentId;
	}
	public String getAttachmentFile() {
		return attachmentFile;
	}
	public void setAttachmentFile(String attachmentFile) {
		this.attachmentFile = attachmentFile;
	}
	public String getAttachmentType() {
		return attachmentType;
	}
	public void setAttachmentType(String attachmentType) {
		this.attachmentType = attachmentType;
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
