package com.wo.module.qa.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

import org.apache.commons.lang3.StringUtils;

import com.wo.module.common.constant.Constants;
import com.wo.module.common.model.BaseEntity;

public class QAAttachment extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -8680003066498099859L;

	private Long QnaAttachmentId;
	private QA qa;
	private String attachmentFile;
	private String fileId;
	private Long fileSize;
	private String publishAttachment;
	
	// helper
	private Long fileSizeKB;
	private boolean isPublish;
	
	private boolean boolPublishTemp;
	private boolean isPublishTemp;
	
	public Long getQnaAttachmentId() {
		return QnaAttachmentId;
	}
	public void setQnaAttachmentId(Long qnaAttachmentId) {
		QnaAttachmentId = qnaAttachmentId;
	}
	public QA getQa() {
		return qa;
	}
	public void setQa(QA qa) {
		this.qa = qa;
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
	public String getPublishAttachment() {
		return publishAttachment;
	}
	public void setPublishAttachment(String publishAttachment) {
		this.publishAttachment = publishAttachment;
	}
	public Long getFileSizeKB() {
		if(fileSize != null) {
			BigDecimal var = new BigDecimal(fileSize).divide(new BigDecimal(1024) , RoundingMode.UP );
			fileSizeKB = var.longValue();
		} else {
			fileSizeKB = 0l;
		}
		
		return fileSizeKB;
	}
	public void setFileSizeKB(Long fileSizeKB) {
		this.fileSizeKB = fileSizeKB;
	}
	public Boolean getIsPublish() {
		if (StringUtils.isEmpty(publishAttachment)) {
			isPublish = false;
		} else {
			if (publishAttachment.equals(Constants.CONSTANT_NO)) {
				isPublish = false;
			} else {
				isPublish = true;
			}
		}
		
		return isPublish;
	}
	public void setIsPublish(Boolean isPublish) {
		this.isPublish = isPublish;
	}
	
	public boolean isBoolPublishTemp() {
		return boolPublishTemp;
	}
	public void setBoolPublishTemp(boolean boolPublishTemp) {
		this.boolPublishTemp = boolPublishTemp;
	}
	public boolean isPublishTemp() {
		return isPublishTemp;
	}
	public void setPublishTemp(boolean isPublishTemp) {
		this.isPublishTemp = isPublishTemp;
	}
}
