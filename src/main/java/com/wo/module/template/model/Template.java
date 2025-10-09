package com.wo.module.template.model;

import java.io.Serializable;
import java.util.List;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.parameter.model.ParameterDetail;

public class Template extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 6395152164269025885L;

	private Long templateId;
	private String perihalEn;
	private String perihalIn;
	private ParameterDetail templateCategory;
	private ParameterDetail templateSubCategory;
	private String note;
	private String deskripsi;
	private String saranaPenyampaianLaporan;
	private String sanksi;

	private List<TemplateDocument> templateDocuments;

	public Long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}

	public String getPerihalEn() {
		return perihalEn;
	}

	public void setPerihalEn(String perihalEn) {
		this.perihalEn = perihalEn;
	}

	public String getPerihalIn() {
		return perihalIn;
	}

	public void setPerihalIn(String perihalIn) {
		this.perihalIn = perihalIn;
	}

	public ParameterDetail getTemplateCategory() {
		return templateCategory;
	}

	public void setTemplateCategory(ParameterDetail templateCategory) {
		this.templateCategory = templateCategory;
	}

	public ParameterDetail getTemplateSubCategory() {
		return templateSubCategory;
	}

	public void setTemplateSubCategory(ParameterDetail templateSubCategory) {
		this.templateSubCategory = templateSubCategory;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getdeskripsi() {
		return deskripsi;
	}

	public void setdeskripsi(String deskripsi) {
		this.deskripsi = deskripsi;
	}

	public String getsaranaPenyampaianLaporan() {
		return saranaPenyampaianLaporan;
	}

	public void setsaranaPenyampaianLaporan(String saranaPenyampaianLaporan) {
		this.saranaPenyampaianLaporan = saranaPenyampaianLaporan;
	}

	public String getSanksi() {
		return sanksi;
	}

	public void setSanksi(String sanksi) {
		this.sanksi = sanksi;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<TemplateDocument> getTemplateDocuments() {
		return templateDocuments;
	}

	public void setTemplateDocuments(List<TemplateDocument> templateDocuments) {
		this.templateDocuments = templateDocuments;
	}

}
