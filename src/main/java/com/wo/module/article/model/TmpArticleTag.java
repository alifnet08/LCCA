package com.wo.module.article.model;

import java.io.Serializable;

import com.wo.module.common.model.BaseEntity;

public class TmpArticleTag extends BaseEntity implements Serializable{
	
	private static final long serialVersionUID = -8167330800684825705L;

	private Long articleTagId;
	private TmpArticle tmpArticle;
	private String tag;
	
	public Long getArticleTagId() {
		return articleTagId;
	}
	public void setArticleTagId(Long articleTagId) {
		this.articleTagId = articleTagId;
	}
	public TmpArticle getTmpArticle() {
		return tmpArticle;
	}
	public void setTmpArticle(TmpArticle tmpArticle) {
		this.tmpArticle = tmpArticle;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
}
