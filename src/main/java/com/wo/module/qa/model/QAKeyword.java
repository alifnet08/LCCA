package com.wo.module.qa.model;

import java.io.Serializable;

import com.wo.module.common.model.BaseEntity;

public class QAKeyword extends BaseEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6395152164269025885L;
	private Long qnaKeywordId;
	private QA qa;
	private String keyword;
	private Long qnaId;
	
	public Long getQnaKeywordId() {
		return qnaKeywordId;
	}
	public void setQnaKeywordId(Long qnaKeywordId) {
		this.qnaKeywordId = qnaKeywordId;
	}
	public QA getQa() {
		return qa;
	}
	public void setQa(QA qa) {
		this.qa = qa;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Long getQnaId() {
		return qnaId;
	}
	public void setQnaId(Long qnaId) {
		this.qnaId = qnaId;
	}
	
	

	
	

}
