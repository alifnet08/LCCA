package com.wo.module.dbCompliance.model;

import java.io.Serializable;

import com.wo.module.common.model.BaseEntity;

public class DBComplianceDoc extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 6395152164269025885L;

	private Long dbComplianceDocumentId;
	private DBCompliance dbCompliance;
	private String attachmentFile;
	private String fileId;
	private Long fileSize;
	
	
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
	public Long getDbComplianceDocumentId() {
		return dbComplianceDocumentId;
	}
	public void setDbComplianceDocumentId(Long dbComplianceDocumentId) {
		this.dbComplianceDocumentId = dbComplianceDocumentId;
	}
	public DBCompliance getDbCompliance() {
		return dbCompliance;
	}
	public void setDbCompliance(DBCompliance dbCompliance) {
		this.dbCompliance = dbCompliance;
	}
	
	
	
	
	
	
}
