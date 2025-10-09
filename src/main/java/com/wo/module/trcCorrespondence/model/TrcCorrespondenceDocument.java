package com.wo.module.trcCorrespondence.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

import com.wo.module.common.model.BaseEntity;

public class TrcCorrespondenceDocument extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 4484883366657157536L;
	
	private Long correspondenceAttachmentId;
	
	private TrcCorrespondence trcCorrespondence;
	
	private String attachmentFile;
	private String fileId;
	private Long fileSize;
	
	private Long fileSizeKB;

	public TrcCorrespondenceDocument() {
		super();
	}

	public Long getCorrespondenceAttachmentId() {
		return correspondenceAttachmentId;
	}

	public void setCorrespondenceAttachmentId(Long correspondenceAttachmentId) {
		this.correspondenceAttachmentId = correspondenceAttachmentId;
	}

	public String getAttachmentFile() {
		return attachmentFile;
	}

	public void setAttachmentFile(String attachmentFile) {
		this.attachmentFile = attachmentFile;
	}

	public TrcCorrespondence getTrcCorrespondence() {
		return trcCorrespondence;
	}

	public void setTrcCorrespondence(TrcCorrespondence trcCorrespondence) {
		this.trcCorrespondence = trcCorrespondence;
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
