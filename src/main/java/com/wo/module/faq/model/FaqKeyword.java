package com.wo.module.faq.model;

import java.io.Serializable;

import com.wo.module.common.model.BaseEntity;

public class FaqKeyword extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 6395152164269025885L;

	private Long faqKeywordId;
	private Faq faq;
	
	private String keyword;

	public Long getFaqKeywordId() {
		return faqKeywordId;
	}

	public void setFaqKeywordId(Long faqKeywordId) {
		this.faqKeywordId = faqKeywordId;
	}

	public Faq getFaq() {
		return faq;
	}

	public void setFaq(Faq faq) {
		this.faq = faq;
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

	
	
	
	
	
}
