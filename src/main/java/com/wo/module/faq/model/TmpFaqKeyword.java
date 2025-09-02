package com.wo.module.faq.model;

import java.io.Serializable;

import com.wo.module.common.model.BaseEntity;

public class TmpFaqKeyword extends BaseEntity implements Serializable{

	private static final long serialVersionUID = -6711179115345206768L;

	private Long faqKeywordId;
	private TmpFaq tmpFaq;
	private String keyword;
	
	public Long getFaqKeywordId() {
		return faqKeywordId;
	}
	public void setFaqKeywordId(Long faqKeywordId) {
		this.faqKeywordId = faqKeywordId;
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
	public TmpFaq getTmpFaq() {
		return tmpFaq;
	}
	public void setTmpFaq(TmpFaq tmpFaq) {
		this.tmpFaq = tmpFaq;
	}
	
}
