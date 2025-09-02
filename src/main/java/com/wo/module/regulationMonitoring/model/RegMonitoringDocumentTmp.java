package com.wo.module.regulationMonitoring.model;

import java.io.Serializable;

import com.wo.module.common.model.BaseEntity;

public class RegMonitoringDocumentTmp extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -7435915050529598562L;

	private Long regMonitoringDocumentTmpId;
	
	private RegMonitoringTmp regMonitoringTmp;
	
	private String attachmentFile;
	private String fileId;
	private Long fileSize;
	
	private Long delId;

	public Long getRegMonitoringDocumentTmpId() {
		return regMonitoringDocumentTmpId;
	}

	public void setRegMonitoringDocumentTmpId(Long regMonitoringDocumentTmpId) {
		this.regMonitoringDocumentTmpId = regMonitoringDocumentTmpId;
	}

	public RegMonitoringTmp getRegMonitoringTmp() {
		return regMonitoringTmp;
	}

	public void setRegMonitoringTmp(RegMonitoringTmp regMonitoringTmp) {
		this.regMonitoringTmp = regMonitoringTmp;
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
}