package com.wo.module.tmpCorrespondence.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

import com.wo.module.common.model.BaseEntity;

public class TmpCorrespondenceDocument extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 4484883366657157536L;
	private Long correspondenceAttachmentId;
	private TmpCorrespondence tmpCorrespondence;
	private String attachmentFile;
	private String fileId;
	private Long fileSize;
	
	private Long fileSizeKB;
	
	public TmpCorrespondenceDocument() {
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

	public TmpCorrespondence getTmpCorrespondence() {
		return tmpCorrespondence;
	}

	public void setTmpCorrespondence(TmpCorrespondence tmpCorrespondence) {
		this.tmpCorrespondence = tmpCorrespondence;
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
