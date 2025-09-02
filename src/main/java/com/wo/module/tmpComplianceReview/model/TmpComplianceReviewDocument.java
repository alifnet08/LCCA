package com.wo.module.tmpComplianceReview.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

import com.wo.module.common.model.BaseEntity;

public class TmpComplianceReviewDocument extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 4484883366657157536L;
	private Long complianceReviewAttachmentId;
	private TmpComplianceReview tmpComplianceReview;
	private String attachmentFile;
	private String fileId;
	private Long fileSize;

	private Long fileSizeKB;

	public TmpComplianceReviewDocument() {
		super();
	}

	public Long getComplianceReviewAttachmentId() {
		return complianceReviewAttachmentId;
	}

	public void setComplianceReviewAttachmentId(Long complianceReviewAttachmentId) {
		this.complianceReviewAttachmentId = complianceReviewAttachmentId;
	}

	public TmpComplianceReview getTmpComplianceReview() {
		return tmpComplianceReview;
	}

	public void setTmpComplianceReview(TmpComplianceReview tmpComplianceReview) {
		this.tmpComplianceReview = tmpComplianceReview;
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
