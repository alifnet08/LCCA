package com.wo.module.LitigationViewFE.vo;

import java.io.Serializable;
import java.util.Date;

public class LitigationAttachmentViewFEVo implements Serializable{

	private static final long serialVersionUID = 8344173457058660843L;

	private Long litigationAttachmentId;
	private Long litigationId;
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
	public Long getLitigationId() {
		return litigationId;
	}
	public void setLitigationId(Long litigationId) {
		this.litigationId = litigationId;
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
	
}
