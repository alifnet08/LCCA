package com.wo.module.documentType.model;

import java.io.Serializable;
import java.util.Locale;

import javax.faces.context.FacesContext;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.parameter.model.ParameterDetail;

public class DocumentType extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 6395152164269025885L;
	private Long documentTypeId;
	private ParameterDetail parameterDetail;
	
	private String documentTypeIn;
	private String documentTypeEn;
	
	private String typeDescription;
	private Long delId;
	
	private String parameterDtlName;
	private String parameterDtlNameEn;
	private String parameterDtlNameIn;
	private String documentType;
	
	public Long getDocumentTypeId() {
		return documentTypeId;
	}
	public void setDocumentTypeId(Long documentTypeId) {
		this.documentTypeId = documentTypeId;
	}
	
	public ParameterDetail getParameterDetail() {
		return parameterDetail;
	}
	public void setParameterDetail(ParameterDetail parameterDetail) {
		this.parameterDetail = parameterDetail;
	}
	public String getDocumentTypeIn() {
		return documentTypeIn;
	}
	public void setDocumentTypeIn(String documentTypeIn) {
		this.documentTypeIn = documentTypeIn;
	}
	public String getDocumentTypeEn() {
		return documentTypeEn;
	}
	public void setDocumentTypeEn(String documentTypeEn) {
		this.documentTypeEn = documentTypeEn;
	}
	public String getTypeDescription() {
		return typeDescription;
	}
	public void setTypeDescription(String typeDescription) {
		this.typeDescription = typeDescription;
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
	public String getDocumentType() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if(locale!=null && locale.equals(locale.ENGLISH)) {
			documentType = documentTypeEn;
		}else {
			documentType = documentTypeIn;
		}
		return documentType;
	}
	
	
	public void setDocumentType(String documentType) {
		this.documentType = documentType;
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
	
	
	

}
