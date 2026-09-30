package com.wo.module.notary.model;

import java.io.Serializable;

import com.wo.module.common.model.BaseEntity;

public class NotaryDocument extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long notaryDocumentId;
	private Notary notary;
	private String attachmentType;
	private String attachmentFile;
	private String fileId;
	private Long fileSize;
	private Integer documentNo;

	public Long getNotaryDocumentId() {
		return notaryDocumentId;
	}

	public void setNotaryDocumentId(Long notaryDocumentId) {
		this.notaryDocumentId = notaryDocumentId;
	}

	public Notary getNotary() {
		return notary;
	}

	public void setNotary(Notary notary) {
		this.notary = notary;
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

	public Integer getDocumentNo() {
		return documentNo;
	}

	public void setDocumentNo(Integer documentNo) {
		this.documentNo = documentNo;
	}

}
