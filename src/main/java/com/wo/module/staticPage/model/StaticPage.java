package com.wo.module.staticPage.model;

import java.io.Serializable;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.parameter.model.ParameterDetail;

public class StaticPage extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 6395152164269025885L;

	private Long staticPageId;
	private String questionEn;
	private String questionIn;
	private ParameterDetail staticPageCategory;
	private String titleIn;
	private String titleEn;
	private String contentIn;
	private String contentEn;
	
	public Long getStaticPageId() {
		return staticPageId;
	}
	public void setStaticPageId(Long staticPageId) {
		this.staticPageId = staticPageId;
	}
	public String getQuestionEn() {
		return questionEn;
	}
	public void setQuestionEn(String questionEn) {
		this.questionEn = questionEn;
	}
	public String getQuestionIn() {
		return questionIn;
	}
	public void setQuestionIn(String questionIn) {
		this.questionIn = questionIn;
	}
	public ParameterDetail getStaticPageCategory() {
		return staticPageCategory;
	}
	public void setStaticPageCategory(ParameterDetail staticPageCategory) {
		this.staticPageCategory = staticPageCategory;
	}
	public String getTitleIn() {
		return titleIn;
	}
	public void setTitleIn(String titleIn) {
		this.titleIn = titleIn;
	}
	public String getTitleEn() {
		return titleEn;
	}
	public void setTitleEn(String titleEn) {
		this.titleEn = titleEn;
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
	
	
	
	
}
