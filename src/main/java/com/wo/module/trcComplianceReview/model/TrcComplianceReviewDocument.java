package com.wo.module.trcComplianceReview.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

import com.wo.module.common.model.BaseEntity;

public class TrcComplianceReviewDocument extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 4484883366657157536L;
	private Long complianceReviewAttachmentId;
	private TrcComplianceReview trcComplianceReview;
	private String attachmentFile;
	private String fileId;
	private Long fileSize;

	private Long fileSizeKB;

	public TrcComplianceReviewDocument() {
		super();
	}

	public Long getComplianceReviewAttachmentId() {
		return complianceReviewAttachmentId;
	}

	public void setComplianceReviewAttachmentId(Long complianceReviewAttachmentId) {
		this.complianceReviewAttachmentId = complianceReviewAttachmentId;
	}

	public TrcComplianceReview getTrcComplianceReview() {
		return trcComplianceReview;
	}

	public void setTrcComplianceReview(TrcComplianceReview trcComplianceReview) {
		this.trcComplianceReview = trcComplianceReview;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
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
		if (fileSize != null) {
			BigDecimal var = new BigDecimal(fileSize).divide(new BigDecimal(1024), RoundingMode.UP);
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
