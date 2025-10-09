package com.wo.module.article.model;

import java.io.Serializable;

import com.wo.module.common.model.BaseEntity;

public class ArticleTag extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 6395152164269025885L;

	private Long articleTagId;
	private Article article;
	
	private String tag;

	public Long getArticleTagId() {
		return articleTagId;
	}

	public void setArticleTagId(Long articleTagId) {
		this.articleTagId = articleTagId;
	}

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
	
	
	
	
	
}
