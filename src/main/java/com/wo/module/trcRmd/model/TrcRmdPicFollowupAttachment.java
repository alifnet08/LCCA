package com.wo.module.trcRmd.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

import com.wo.module.common.model.BaseEntity;

public class TrcRmdPicFollowupAttachment extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 3019374791490640764L;

	private Long rmdPicFollowupAttachmentId;
	private TrcRmdPicFollowup trcRmdPicFollowup;

	private String attachmentFile;
	private String fileId;
	private Long fileSize;
	
	private Long fileSizeKB;

	public Long getRmdPicFollowupAttachmentId() {
		return rmdPicFollowupAttachmentId;
	}

	public void setRmdPicFollowupAttachmentId(Long rmdPicFollowupAttachmentId) {
		this.rmdPicFollowupAttachmentId = rmdPicFollowupAttachmentId;
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

	public TrcRmdPicFollowup getTrcRmdPicFollowup() {
		return trcRmdPicFollowup;
	}

	public void setTrcRmdPicFollowup(TrcRmdPicFollowup trcRmdPicFollowup) {
		this.trcRmdPicFollowup = trcRmdPicFollowup;
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
	
}