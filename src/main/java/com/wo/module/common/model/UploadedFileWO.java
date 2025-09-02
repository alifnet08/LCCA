package com.wo.module.common.model;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class UploadedFileWO implements Serializable {

	private static final long serialVersionUID = -7791000961765953025L;

	public UploadedFileWO(String fileId, String fileName, Long fileSize) {
		this.fileId = fileId;
		this.fileName = fileName;
		this.fileSize = fileSize;
	}
	
	public UploadedFileWO(String fileId, String fileName, String contentType, Long fileSize) {
		this.fileId = fileId;
		this.fileName = fileName;
		this.contentType = contentType;
		this.fileSize = fileSize;
	}
	
	public UploadedFileWO(String fileId, String fileName, String contentType, Long fileSize, Boolean isNew) {
		this.fileId = fileId;
		this.fileName = fileName;
		this.contentType = contentType;
		this.fileSize = fileSize;
		this.isNew = isNew;
	}

	public UploadedFileWO() {

	}

	private String fileId;

	private String fileName;

	private String contentType;

	private Long fileSize;
	
	private Long fileSizeKB;

	private Boolean isNew;
	
	private String encodedBase64;
	
	private File file;

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public Boolean getIsNew() {
		return isNew;
	}

	public void setIsNew(Boolean isNew) {
		this.isNew = isNew;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public Long getFileSizeKB() {
		if(fileSize!= null) {
			BigDecimal var = new BigDecimal(fileSize).divide(new BigDecimal(1024) , RoundingMode.UP );
			fileSizeKB = var.longValue();
		} else {
			fileSizeKB = new Long(0);
		}
		
		return fileSizeKB;
	}

	public void setFileSizeKB(Long fileSizeKB) {
		this.fileSizeKB = fileSizeKB;
	}

	public String getEncodedBase64() {
		return encodedBase64;
	}

	public void setEncodedBase64(String encodedBase64) {
		this.encodedBase64 = encodedBase64;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

}
