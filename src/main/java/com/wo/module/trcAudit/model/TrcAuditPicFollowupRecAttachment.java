package com.wo.module.trcAudit.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.parameter.model.ParameterDetail;

public class TrcAuditPicFollowupRecAttachment extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 7415371777586018187L;

	private Long auditPicFollowupRecAttachmentId;
	
	private TrcAuditPicFollowupRec trcAuditPicFollowupRec;
	private ParameterDetail attachmentType;
	private String attachmentFile;
	private String fileId;
	private Long fileSize;
	
	private Long fileSizeKB;
	
	public TrcAuditPicFollowupRecAttachment() {
		super();
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

	public ParameterDetail getAttachmentType() {
		return attachmentType;
	}

	public void setAttachmentType(ParameterDetail attachmentType) {
		this.attachmentType = attachmentType;
	}

	public Long getAuditPicFollowupRecAttachmentId() {
		return auditPicFollowupRecAttachmentId;
	}

	public void setAuditPicFollowupRecAttachmentId(Long auditPicFollowupRecAttachmentId) {
		this.auditPicFollowupRecAttachmentId = auditPicFollowupRecAttachmentId;
	}

	public TrcAuditPicFollowupRec getTrcAuditPicFollowupRec() {
		return trcAuditPicFollowupRec;
	}

	public void setTrcAuditPicFollowupRec(TrcAuditPicFollowupRec trcAuditPicFollowupRec) {
		this.trcAuditPicFollowupRec = trcAuditPicFollowupRec;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
