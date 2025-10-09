package com.wo.module.faq.model;

import java.io.Serializable;

import com.wo.module.common.model.BaseEntity;

public class TmpFaqDocument extends BaseEntity implements Serializable{

	private static final long serialVersionUID = -7767927485999850448L;

	private Long faqDocumentId;
	private TmpFaq tmpFaq;
	private String attachmentFile;
	private String fileId;
	private Long fileSize;
	
	public Long getFaqDocumentId() {
		return faqDocumentId;
	}
	public void setFaqDocumentId(Long faqDocumentId) {
		this.faqDocumentId = faqDocumentId;
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
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public TmpFaq getTmpFaq() {
		return tmpFaq;
	}
	public void setTmpFaq(TmpFaq tmpFaq) {
		this.tmpFaq = tmpFaq;
	}
	
}
