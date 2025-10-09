package com.wo.module.trcFineApproval.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

import com.wo.module.common.model.BaseEntity;

public class TrcFinePicFollowupAttachment extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -7707945018477636612L;
	
	private Long finePicFollowupAttachmentId;
	
	private TrcFinePicFollowup trcFinePicFollowup;
	
	private String attachmentFile;
	private String attachmentFrom;
	private String fileId;
	private Long fileSize;
	
	private Long fileSizeKB;
	
	public TrcFinePicFollowupAttachment() {
		super();
	}

	public Long getFinePicFollowupAttachmentId() {
		return finePicFollowupAttachmentId;
	}

	public void setFinePicFollowupAttachmentId(Long finePicFollowupAttachmentId) {
		this.finePicFollowupAttachmentId = finePicFollowupAttachmentId;
	}

	

	public TrcFinePicFollowup getTrcFinePicFollowup() {
		return trcFinePicFollowup;
	}

	public void setTrcFinePicFollowup(TrcFinePicFollowup trcFinePicFollowup) {
		this.trcFinePicFollowup = trcFinePicFollowup;
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

	public String getAttachmentFrom() {
		return attachmentFrom;
	}

	public void setAttachmentFrom(String attachmentFrom) {
		this.attachmentFrom = attachmentFrom;
	}
}
