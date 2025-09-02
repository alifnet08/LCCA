package com.wo.module.tmpFine.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

import com.wo.module.common.model.BaseEntity;

public class TmpFineDocument extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 4484883366657157536L;
	private Long fineAttachmentId;
	private TmpFine tmpFine;
	private String attachmentFile;
	private String fileId;
	private Long fileSize;
	
	private Long fileSizeKB;
	
	public TmpFineDocument() {
		super();
	}

	public Long getFineAttachmentId() {
		return fineAttachmentId;
	}

	public void setFineAttachmentId(Long fineAttachmentId) {
		this.fineAttachmentId = fineAttachmentId;
	}

	public String getAttachmentFile() {
		return attachmentFile;
	}

	public void setAttachmentFile(String attachmentFile) {
		this.attachmentFile = attachmentFile;
	}

	public TmpFine getTmpFine() {
		return tmpFine;
	}

	public void setTmpFine(TmpFine tmpFine) {
		this.tmpFine = tmpFine;
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
