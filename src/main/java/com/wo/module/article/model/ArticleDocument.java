package com.wo.module.article.model;

import java.io.Serializable;

import com.wo.module.common.model.BaseEntity;

public class ArticleDocument extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 6395152164269025885L;

	private Long articleDocumentId;
	private Article article;
	private String attachmentFile;
	private String fileId;
	private Long fileSize;
	
	private String tag;

	
	public Article getArticle() {
		return article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public Long getArticleDocumentId() {
		return articleDocumentId;
	}

	public void setArticleDocumentId(Long articleDocumentId) {
		this.articleDocumentId = articleDocumentId;
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
