package com.wo.module.faqFE.vo;

import java.io.Serializable;
import java.util.List;

import com.wo.module.common.model.BaseEntity;

public class InstitutionVO extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 6395152164269025885L;

	private String institution;
	
	private List<CategoryTypeVO> categoryFaq;

	public String getInstitution() {
		return institution;
	}

	public void setInstitution(String institution) {
		this.institution = institution;
	}

	public List<CategoryTypeVO> getCategoryFaq() {
		return categoryFaq;
	}

	public void setCategoryFaq(List<CategoryTypeVO> categoryFaq) {
		this.categoryFaq = categoryFaq;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
	
	
	
}
