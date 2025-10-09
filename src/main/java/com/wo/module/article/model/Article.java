package com.wo.module.article.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.parameter.model.ParameterDetail;

public class Article extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 6395152164269025885L;

	private Long articleId;
	private ParameterDetail articleType;
	private String author;
	private Date publishDate;
	private String tagName;
	private String articleTitleEn;
	private String articleTitleIn;
	private String descriptionIn;
	private String descriptionEn;
	private String contentIn;
	private String contentEn;
	private ParameterDetail status;
	private ParameterDetail activeStatus;
	
	private String articleTitle;
	private List<ArticleTag> articleTags;
	private List<ArticleDocument> articleDocuments;
	
	private String description;
	private String content;
	
	// helper
	private String articleTypeIn;
	private String articleTypeEn;
	private String articleTypeName;
	private String statusCode;
	private String statusName;
	private String statusIn;
	private String statusEn;
	
	private String activeStatusCd;
	private String activeStatusIn;
	
	@SuppressWarnings("static-access")
	public String getDescription() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if (locale != null && locale.equals(locale.ENGLISH)) {
			description = descriptionEn;
		} else {
			description = descriptionIn;
		}
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@SuppressWarnings("static-access")
	public String getContent() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if (locale != null && locale.equals(locale.ENGLISH)) {
			content = contentEn;
		} else {
			content = contentIn;
		}
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Long getArticleId() {
		return articleId;
	}
	public void setArticleId(Long articleId) {
		this.articleId = articleId;
	}
	
	public ParameterDetail getArticleType() {
		return articleType;
	}
	public void setArticleType(ParameterDetail articleType) {
		this.articleType = articleType;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	
	public Date getPublishDate() {
		return publishDate;
	}
	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}
	public String getArticleTitleEn() {
		return articleTitleEn;
	}
	public void setArticleTitleEn(String articleTitleEn) {
		this.articleTitleEn = articleTitleEn;
	}
	public String getArticleTitleIn() {
		return articleTitleIn;
	}
	public void setArticleTitleIn(String articleTitleIn) {
		this.articleTitleIn = articleTitleIn;
	}
	public String getDescriptionIn() {
		return descriptionIn;
	}
	public void setDescriptionIn(String descriptionIn) {
		this.descriptionIn = descriptionIn;
	}
	public String getDescriptionEn() {
		return descriptionEn;
	}
	public void setDescriptionEn(String descriptionEn) {
		this.descriptionEn = descriptionEn;
	}
	public String getContentIn() {
		return contentIn;
	}
	public void setContentIn(String contentIn) {
		this.contentIn = contentIn;
	}
	public String getContentEn() {
		return contentEn;
	}
	public void setContentEn(String contentEn) {
		this.contentEn = contentEn;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@SuppressWarnings("static-access")
	public String getArticleTitle() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if (locale != null && locale.equals(locale.ENGLISH)) {
			articleTitle = articleTitleEn;
		} else {
			articleTitle = articleTitleIn;
		}
		return articleTitle;
	}
	public void setArticleTitle(String articleTitle) {
		this.articleTitle = articleTitle;
	}
	public String getTagName() {
		return tagName;
	}
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	public List<ArticleTag> getArticleTags() {
		return articleTags;
	}
	public void setArticleTags(List<ArticleTag> articleTags) {
		this.articleTags = articleTags;
	}
	public List<ArticleDocument> getArticleDocuments() {
		return articleDocuments;
	}
	public void setArticleDocuments(List<ArticleDocument> articleDocuments) {
		this.articleDocuments = articleDocuments;
	}
	public ParameterDetail getStatus() {
		return status;
	}
	public void setStatus(ParameterDetail status) {
		this.status = status;
	}
	public ParameterDetail getActiveStatus() {
		return activeStatus;
	}
	public void setActiveStatus(ParameterDetail activeStatus) {
		this.activeStatus = activeStatus;
	}
	public String getArticleTypeIn() {
		return articleTypeIn;
	}
	public void setArticleTypeIn(String articleTypeIn) {
		this.articleTypeIn = articleTypeIn;
	}
	public String getArticleTypeEn() {
		return articleTypeEn;
	}
	public void setArticleTypeEn(String articleTypeEn) {
		this.articleTypeEn = articleTypeEn;
	}
	public String getArticleTypeName() {
		return articleTypeName;
	}
	public void setArticleTypeName(String articleTypeName) {
		this.articleTypeName = articleTypeName;
	}
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	public String getStatusIn() {
		return statusIn;
	}
	public void setStatusIn(String statusIn) {
		this.statusIn = statusIn;
	}
	public String getStatusEn() {
		return statusEn;
	}
	public void setStatusEn(String statusEn) {
		this.statusEn = statusEn;
	}
	public String getActiveStatusCd() {
		return activeStatusCd;
	}
	public void setActiveStatusCd(String activeStatusCd) {
		this.activeStatusCd = activeStatusCd;
	}
	public String getActiveStatusIn() {
		return activeStatusIn;
	}
	public void setActiveStatusIn(String activeStatusIn) {
		this.activeStatusIn = activeStatusIn;
	}
	
	
	
	
}
