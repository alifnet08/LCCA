package com.wo.module.regulationMonitoring.model;

import java.io.Serializable;

import com.wo.module.common.model.BaseEntity;

public class RegMonitoringPICFollowUpAttachmentTrc extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -8882737168209382219L;
	
	private Long regMonitoringPicFollowUpAttchTrcId;
	private RegMonitoringPICFollowUpTrc regMonitoringPicFollowUpTrc;
	
	private String attachmentFile;
	private String fileId;
	private Long fileSize;
	
	private Long delId;

	public Long getRegMonitoringPicFollowUpAttchTrcId() {
		return regMonitoringPicFollowUpAttchTrcId;
	}

	public void setRegMonitoringPicFollowUpAttchTrcId(Long regMonitoringPicFollowUpAttchTrcId) {
		this.regMonitoringPicFollowUpAttchTrcId = regMonitoringPicFollowUpAttchTrcId;
	}

	public RegMonitoringPICFollowUpTrc getRegMonitoringPicFollowUpTrc() {
		return regMonitoringPicFollowUpTrc;
	}

	public void setRegMonitoringPicFollowUpTrc(RegMonitoringPICFollowUpTrc regMonitoringPicFollowUpTrc) {
		this.regMonitoringPicFollowUpTrc = regMonitoringPicFollowUpTrc;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}