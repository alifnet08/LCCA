package com.wo.module.documentCategory.model;

import java.io.Serializable;
import java.util.Locale;

import javax.faces.context.FacesContext;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.parameter.model.ParameterDetail;

public class DocumentCategory extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 6395152164269025885L;
	private Long documentCategoryId;
	private ParameterDetail parameterDetail;
	private String parameterDtlName;
	private String parameterDtlNameEn;
	private String parameterDtlNameIn;
	private String documentCategoryIn;
	private String documentCategoryEn;
	private String documentCategory;
	private Long delId;
	
	//tambahan
	private String jenisKetentuanCode;
	
	public String getDocumentCategoryIn() {
		return documentCategoryIn;
	}
	public void setDocumentCategoryIn(String documentCategoryIn) {
		this.documentCategoryIn = documentCategoryIn;
	}
	public String getDocumentCategoryEn() {
		return documentCategoryEn;
	}
	public void setDocumentCategoryEn(String documentCategoryEn) {
		this.documentCategoryEn = documentCategoryEn;
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
	public String getDocumentCategory() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if(locale!=null && locale.equals(locale.ENGLISH)) {
			documentCategory = documentCategoryEn;
		}else {
			documentCategory = documentCategoryIn;
		}
		return documentCategory;
	}
	
	
	public void setDocumentCategory(String documentCategory) {
		this.documentCategory = documentCategory;
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
	public Long getDocumentCategoryId() {
		return documentCategoryId;
	}
	public void setDocumentCategoryId(Long documentCategoryId) {
		this.documentCategoryId = documentCategoryId;
	}
	public ParameterDetail getParameterDetail() {
		return parameterDetail;
	}
	public void setParameterDetail(ParameterDetail parameterDetail) {
		this.parameterDetail = parameterDetail;
	}
	public String getJenisKetentuanCode() {
		return jenisKetentuanCode;
	}
	public void setJenisKetentuanCode(String jenisKetentuanCode) {
		this.jenisKetentuanCode = jenisKetentuanCode;
	}
	
	
	
	

}
