package com.wo.module.article.model;

import java.io.Serializable;

import com.wo.module.common.model.BaseEntity;

public class TmpArticleDocument extends BaseEntity implements Serializable{

	private static final long serialVersionUID = -4695065077381588574L;
	
	private Long articleDocumentId;
	private TmpArticle tmpArticle;
	private String attachmentFile;
	private String fileId;
	private Long fileSize;
	
	public Long getArticleDocumentId() {
		return articleDocumentId;
	}
	public void setArticleDocumentId(Long articleDocumentId) {
		this.articleDocumentId = articleDocumentId;
	}
	public TmpArticle getTmpArticle() {
		return tmpArticle;
	}
	public void setTmpArticle(TmpArticle tmpArticle) {
		this.tmpArticle = tmpArticle;
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
}
