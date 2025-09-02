package com.wo.module.internalRegulationPenerbitan.model;

import java.io.Serializable;

import com.wo.module.common.model.BaseEntity;

public class InternalRegulationPenerbitanAttachment extends BaseEntity implements Serializable{

	private static final long serialVersionUID = -4700108887157386651L;
	
	private Long irgAttachmentId;
	private InternalRegulationPenerbitan internalRegulationPenerbitan;
	private String attachmentFile;
	private String attachmentType;
	private String fileId;
	private Long fileSize;
	
	private Long delId;

	public InternalRegulationPenerbitan getInternalRegulationPenerbitan() {
		return internalRegulationPenerbitan;
	}

	public void setInternalRegulationPenerbitan(InternalRegulationPenerbitan internalRegulationPenerbitan) {
		this.internalRegulationPenerbitan = internalRegulationPenerbitan;
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

	public Long getDelId() {
		return delId;
	}

	public void setDelId(Long delId) {
		this.delId = delId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Long getIrgAttachmentId() {
		return irgAttachmentId;
	}

	public void setIrgAttachmentId(Long irgAttachmentId) {
		this.irgAttachmentId = irgAttachmentId;
	}
}