package com.wo.module.outgoingLetterView.model;

import java.io.Serializable;

import com.wo.module.common.model.BaseEntity;

public class OutgoingLetterAttachmentView extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 5919878892884685625L;

	private Long outgoingLetterAttachmentId;
	private OutgoingLetterView outgoingLetterView;
	private String attachmentFile;
	private String attachmentType;
	private String fileId;
	private Long fileSize;
	
	private Long delId;

	public Long getOutgoingLetterAttachmentId() {
		return outgoingLetterAttachmentId;
	}

	public void setOutgoingLetterAttachmentId(Long outgoingLetterAttachmentId) {
		this.outgoingLetterAttachmentId = outgoingLetterAttachmentId;
	}

	public OutgoingLetterView getOutgoingLetterView() {
		return outgoingLetterView;
	}

	public void setOutgoingLetterView(OutgoingLetterView outgoingLetterView) {
		this.outgoingLetterView = outgoingLetterView;
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

	public String getAttachmentType() {
		return attachmentType;
	}

	public void setAttachmentType(String attachmentType) {
		this.attachmentType = attachmentType;
	}
	
}
