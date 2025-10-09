package com.wo.module.regulationMonitoring.model;

import java.io.Serializable;

import com.wo.module.common.model.BaseEntity;

public class RegMonitoringDocumentTrc extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -7435915050529598562L;

	private Long regMonitoringDocumentTrcId;
	
	private RegMonitoringTrc regMonitoringTrc;
	
	private String attachmentFile;
	private String fileId;
	private Long fileSize;
	
	private Long delId;
	
	private Integer sequence;

	public Long getRegMonitoringDocumentTrcId() {
		return regMonitoringDocumentTrcId;
	}

	public void setRegMonitoringDocumentTrcId(Long regMonitoringDocumentTrcId) {
		this.regMonitoringDocumentTrcId = regMonitoringDocumentTrcId;
	}

	public RegMonitoringTrc getRegMonitoringTrc() {
		return regMonitoringTrc;
	}

	public void setRegMonitoringTrc(RegMonitoringTrc regMonitoringTrc) {
		this.regMonitoringTrc = regMonitoringTrc;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
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

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}
}