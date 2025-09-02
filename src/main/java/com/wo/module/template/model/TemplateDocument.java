package com.wo.module.template.model;

import java.io.Serializable;

import com.wo.module.common.model.BaseEntity;

public class TemplateDocument extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 6395152164269025885L;

	private Long templateDocumentId;
	private Template template;
	private String attachmentFile;
	private String documentType;
	private String fileId;
	private Long fileSize;
	
	public Long getTemplateDocumentId() {
		return templateDocumentId;
	}
	public void setTemplateDocumentId(Long templateDocumentId) {
		this.templateDocumentId = templateDocumentId;
	}
	public Template getTemplate() {
		return template;
	}
	public void setTemplate(Template template) {
		this.template = template;
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
	public String getDocumentType() {
		return documentType;
	}
	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}
	
	
	
	
	
	
	
}
