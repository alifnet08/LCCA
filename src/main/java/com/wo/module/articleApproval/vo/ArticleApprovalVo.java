package com.wo.module.articleApproval.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.Locale;

import javax.faces.context.FacesContext;

public class ArticleApprovalVo implements Serializable{

	private static final long serialVersionUID = -5445603990098891542L;
	
	private Long articleId;
	private String articleType;
	private String articleTypeCode;
	private String articleTypeIn;
	private String articleTypeEn;
	private String articleTitle;
	private String articleIn;
	private String articleEn;
	private Date publishDate;
	private String publishDateStr;
	
	public Long getArticleId() {
		return articleId;
	}
	public void setArticleId(Long articleId) {
		this.articleId = articleId;
	}
	@SuppressWarnings("static-access")
	public String getArticleType() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		
		if (locale != null && locale.equals(locale.ENGLISH)) {
			articleType = articleTypeEn;
		} else {
			articleType = articleTypeIn;
		}
		
		return articleType;
	}
	public void setArticleType(String articleType) {
		this.articleType = articleType;
	}
	public String getArticleTypeCode() {
		return articleTypeCode;
	}
	public void setArticleTypeCode(String articleTypeCode) {
		this.articleTypeCode = articleTypeCode;
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
	@SuppressWarnings("static-access")
	public String getArticleTitle() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		
		if (locale != null && locale.equals(locale.ENGLISH)) {
			articleTitle = articleEn;
		} else {
			articleTitle = articleIn;
		}
		
		return articleTitle;
	}
	public void setArticleTitle(String articleTitle) {
		this.articleTitle = articleTitle;
	}
	public String getArticleIn() {
		return articleIn;
	}
	public void setArticleIn(String articleIn) {
		this.articleIn = articleIn;
	}
	public String getArticleEn() {
		return articleEn;
	}
	public void setArticleEn(String articleEn) {
		this.articleEn = articleEn;
	}
	public Date getPublishDate() {
		return publishDate;
	}
	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}
	public String getPublishDateStr() {
		return publishDateStr;
	}
	public void setPublishDateStr(String publishDateStr) {
		this.publishDateStr = publishDateStr;
	}
	

}
