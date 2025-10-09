package com.wo.module.faq.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.parameter.model.ParameterDetail;

public class Faq extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 6395152164269025885L;

	private Long faqId;
	private ParameterDetail institution;
	private String questionEn;
	private String questionIn;
	private ParameterDetail faqCategory;
	private String answerEn;
	private String answerIn;
	private Date uploadDate;
	
	private List<FaqKeyword> faqKeywords;
	private List<FaqDocument> faqDocuments;
	
	private String keyword;
	private String category;
	
	public Long getFaqId() {
		return faqId;
	}
	public void setFaqId(Long faqId) {
		this.faqId = faqId;
	}
	
	public ParameterDetail getInstitution() {
		return institution;
	}
	public void setInstitution(ParameterDetail institution) {
		this.institution = institution;
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
	
	public String getAnswerEn() {
		return answerEn;
	}
	public void setAnswerEn(String answerEn) {
		this.answerEn = answerEn;
	}
	public String getAnswerIn() {
		return answerIn;
	}
	public void setAnswerIn(String answerIn) {
		this.answerIn = answerIn;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public List<FaqKeyword> getFaqKeywords() {
		return faqKeywords;
	}
	public void setFaqKeywords(List<FaqKeyword> faqKeywords) {
		this.faqKeywords = faqKeywords;
	}
	public List<FaqDocument> getFaqDocuments() {
		return faqDocuments;
	}
	public void setFaqDocuments(List<FaqDocument> faqDocuments) {
		this.faqDocuments = faqDocuments;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public ParameterDetail getFaqCategory() {
		return faqCategory;
	}
	public void setFaqCategory(ParameterDetail faqCategory) {
		this.faqCategory = faqCategory;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public Date getUploadDate() {
		return uploadDate;
	}
	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}
	
	
	
	
	
}
