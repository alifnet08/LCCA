package com.wo.module.documentTopic.model;

import java.io.Serializable;
import java.util.Locale;

import javax.faces.context.FacesContext;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.documentCategory.model.DocumentCategory;
import com.wo.module.parameter.model.ParameterDetail;

public class DocumentTopic extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 6395152164269025885L;
	private Long documentTopicId;
	private ParameterDetail parameterDetail;
	private DocumentCategory documentCategory;
	
	private String parameterDtlNameEn;
	private String parameterDtlNameIn;
	private String documentTopicIn;
	private String documentTopicEn;
	
	private Long delId;
	
	private String parameterDtlName;
	private String documentTopic;
	private String documentCategoryStr;
	private String documentCategoryStrIn;
	private String documentCategoryStrEn;
	
	public Long getDocumentTopicId() {
		return documentTopicId;
	}
	public void setDocumentTopicId(Long documentTopicId) {
		this.documentTopicId = documentTopicId;
	}
	
	public String getDocumentTopicIn() {
		return documentTopicIn;
	}
	public void setDocumentTopicIn(String documentTopicIn) {
		this.documentTopicIn = documentTopicIn;
	}
	public String getDocumentTopicEn() {
		return documentTopicEn;
	}
	public void setDocumentTopicEn(String documentTopicEn) {
		this.documentTopicEn = documentTopicEn;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Long getDelId() {
		return delId;
	}
	public void setDelId(Long delId) {
		this.delId = delId;
	}
	@SuppressWarnings("static-access")
	public String getDocumentTopic() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if(locale!=null && locale.equals(locale.ENGLISH)) {
			documentTopic = documentTopicEn;
		}else {
			documentTopic = documentTopicIn;
		}
		return documentTopic;
	}
	
	
	public void setDocumentTopic(String documentTopic) {
		this.documentTopic = documentTopic;
	}
	@SuppressWarnings("static-access")
	public String getParameterDtlName() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if(locale!=null && locale.equals(locale.ENGLISH)) {
			parameterDtlName = parameterDtlNameEn;
		}else {
			parameterDtlName = parameterDtlNameIn;
		}
		return parameterDtlName;
	}
	public void setParameterDtlName(String parameterDtlName) {
		this.parameterDtlName = parameterDtlName;
	}
	public String getParameterDtlNameEn() {
		return parameterDtlNameEn;
	}
	public void setParameterDtlNameEn(String parameterDtlNameEn) {
		this.parameterDtlNameEn = parameterDtlNameEn;
	}
	public String getParameterDtlNameIn() {
		return parameterDtlNameIn;
	}
	public void setParameterDtlNameIn(String parameterDtlNameIn) {
		this.parameterDtlNameIn = parameterDtlNameIn;
	}
	
	public DocumentCategory getDocumentCategory() {
		return documentCategory;
	}
	public void setDocumentCategory(DocumentCategory documentCategory) {
		this.documentCategory = documentCategory;
	}
	@SuppressWarnings("static-access")
	public String getDocumentCategoryStr() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if(locale!=null && locale.equals(locale.ENGLISH)) {
			documentCategoryStr = documentCategoryStrEn;
		}else {
			documentCategoryStr = documentCategoryStrIn;
		}
		return documentCategoryStr;
	}
	public void setDocumentCategoryStr(String documentCategoryStr) {
		this.documentCategoryStr = documentCategoryStr;
	}
	public ParameterDetail getParameterDetail() {
		return parameterDetail;
	}
	public void setParameterDetail(ParameterDetail parameterDetail) {
		this.parameterDetail = parameterDetail;
	}
	public String getDocumentCategoryStrIn() {
		return documentCategoryStrIn;
	}
	public void setDocumentCategoryStrIn(String documentCategoryStrIn) {
		this.documentCategoryStrIn = documentCategoryStrIn;
	}
	public String getDocumentCategoryStrEn() {
		return documentCategoryStrEn;
	}
	public void setDocumentCategoryStrEn(String documentCategoryStrEn) {
		this.documentCategoryStrEn = documentCategoryStrEn;
	}
	
	
	
	

}
