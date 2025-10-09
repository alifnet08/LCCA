package com.wo.module.faq.model;

import java.io.Serializable;

import com.wo.module.common.model.BaseEntity;

public class FaqDocument extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 6395152164269025885L;

	private Long faqDocumentId;
	private Faq faq;
	private String attachmentFile;
	private String fileId;
	private Long fileSize;
	
	public Long getFaqDocumentId() {
		return faqDocumentId;
	}
	public void setFaqDocumentId(Long faqDocumentId) {
		this.faqDocumentId = faqDocumentId;
	}
	public Faq getFaq() {
		return faq;
	}
	public void setFaq(Faq faq) {
		this.faq = faq;
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
	
	
	
	
	
	
}
