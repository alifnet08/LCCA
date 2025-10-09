package com.wo.module.faq.model;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.tmpFaqApproval.model.TmpFaqApproval;

public class TmpFaq extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 138520642151907162L;

	private Long faqId;
	private ParameterDetail institution;
	private String questionEn;
	private String questionIn;
	private ParameterDetail faqCategory;
	private String answerEn;
	private String answerIn;
	private String status;
	private String activeStatus;
	private java.util.Date uploadDate;
	
	private List<TmpFaqKeyword> tmpFaqKeywords;
	private List<TmpFaqDocument> tmpFaqDocuments;
	private List<TmpFaqApproval> tmpFaqApprovals;
	
	private String keyword;
	private String category;
	private String institutionName;
	private Date date;
	
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
	public ParameterDetail getFaqCategory() {
		return faqCategory;
	}
	public void setFaqCategory(ParameterDetail faqCategory) {
		this.faqCategory = faqCategory;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getActiveStatus() {
		return activeStatus;
	}
	public void setActiveStatus(String activeStatus) {
		this.activeStatus = activeStatus;
	}
	public List<TmpFaqKeyword> getTmpFaqKeywords() {
		return tmpFaqKeywords;
	}
	public void setTmpFaqKeywords(List<TmpFaqKeyword> tmpFaqKeywords) {
		this.tmpFaqKeywords = tmpFaqKeywords;
	}
	public List<TmpFaqDocument> getTmpFaqDocuments() {
		return tmpFaqDocuments;
	}
	public void setTmpFaqDocuments(List<TmpFaqDocument> tmpFaqDocuments) {
		this.tmpFaqDocuments = tmpFaqDocuments;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getInstitutionName() {
		return institutionName;
	}
	public void setInstitutionName(String institutionName) {
		this.institutionName = institutionName;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public List<TmpFaqApproval> getTmpFaqApprovals() {
		return tmpFaqApprovals;
	}
	public void setTmpFaqApprovals(List<TmpFaqApproval> tmpFaqApprovals) {
		this.tmpFaqApprovals = tmpFaqApprovals;
	}
	public java.util.Date getUploadDate() {
		return uploadDate;
	}
	public void setUploadDate(java.util.Date uploadDate) {
		this.uploadDate = uploadDate;
	}
	
}
