package com.wo.module.regulationSocialization.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

import com.wo.module.common.model.BaseEntity;

public class SocializationPICFollowupAttachmentTrc extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = 996141908126279968L;
	
	private long socializationPicFollowupAttachmentId;
	private SocializationPICFollowupTrc socializationPICFollowupTrc;
	
	
	private String attachmentFile;
	private String fileId;
	private Long fileSize;
	
	private Long fileSizeKB;
	
	private Long delId;

	public Long getDelId() {
		return delId;
	}

	public void setDelId(Long delId) {
		this.delId = delId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	

	public long getSocializationPicFollowupAttachmentId() {
		return socializationPicFollowupAttachmentId;
	}

	public void setSocializationPicFollowupAttachmentId(long socializationPicFollowupAttachmentId) {
		this.socializationPicFollowupAttachmentId = socializationPicFollowupAttachmentId;
	}

	public SocializationPICFollowupTrc getSocializationPICFollowupTrc() {
		return socializationPICFollowupTrc;
	}

	public void setSocializationPICFollowupTrc(SocializationPICFollowupTrc socializationPICFollowupTrc) {
		this.socializationPICFollowupTrc = socializationPICFollowupTrc;
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


}
