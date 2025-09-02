package com.wo.module.templateViewFE.vo;

import java.io.Serializable;

public class TemplateDocumentViewFEVo implements Serializable{

	private static final long serialVersionUID = 7284430602001212217L;

	private Long templateDocumentId;
	private Long templateId;
	private String documentType;
	private String attachmentFile;
	private String fileId;
	private Long fileSize;
	
	public Long getTemplateDocumentId() {
		return templateDocumentId;
	}
	public void setTemplateDocumentId(Long templateDocumentId) {
		this.templateDocumentId = templateDocumentId;
	}
	public Long getTemplateId() {
		return templateId;
	}
	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}
	public String getDocumentType() {
		return documentType;
	}
	public void setDocumentType(String documentType) {
		this.documentType = documentType;
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
