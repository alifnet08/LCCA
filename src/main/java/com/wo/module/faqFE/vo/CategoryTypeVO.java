package com.wo.module.faqFE.vo;

import java.io.Serializable;
import java.util.List;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.faq.model.Faq;

public class CategoryTypeVO extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 6395152164269025885L;

	private String faqCategory;
	
	private List<Faq> listFaq;

	public String getFaqCategory() {
		return faqCategory;
	}

	public void setFaqCategory(String faqCategory) {
		this.faqCategory = faqCategory;
	}

	public List<Faq> getListFaq() {
		return listFaq;
	}

	public void setListFaq(List<Faq> listFaq) {
		this.listFaq = listFaq;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
}
