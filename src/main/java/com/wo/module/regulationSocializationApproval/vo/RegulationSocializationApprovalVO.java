package com.wo.module.regulationSocializationApproval.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.documentCategory.model.DocumentCategory;
import com.wo.module.documentTopic.model.DocumentTopic;
import com.wo.module.documentType.model.DocumentType;
import com.wo.module.regulationSocialization.vo.StatusConfirmationVO;


public class RegulationSocializationApprovalVO extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 6395152164269025885L;
	private Long socializationId;
	private String jenisKetentuan;
	private DocumentType documentType;
	private DocumentCategory documentCategory;
	private DocumentTopic documentTopic;
	private String documentNo;
	private String nameIn;
	private String nameEn;
	private String picName;
	private String status;
	private String statusIn;
	private String statusEn;
	
	private String name;
	
	private String documentTypeName;
	private String documentTypeNameIn;
	private String documentTypeNameEn;
	
	private String documentCategoryName;
	private String documentCategoryNameIn;
	private String documentCategoryNameEn;
	
	private String documentTopicName;
	private String documentTopicNameIn;
	private String documentTopicNameEn;
	
	private String publishedDateStr;
	private String effectiveDateStr;
	private String expiredDateStr;
	
	private Long delId;
	
	//helper
	private String statusNameIn;
	private String statusNameEn;
	private String jenisKetentuanIn;
	private String jenisKetentuanEn;

	private List<StatusConfirmationVO> statusList;

	@SuppressWarnings("static-access")
	public String getJenisKetentuan() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if (locale != null && locale.equals(locale.ENGLISH)) {
			jenisKetentuan = jenisKetentuanEn;
		} else {
			jenisKetentuan = jenisKetentuanIn;
		}
		return jenisKetentuan;
	}

	public void setJenisKetentuan(String jenisKetentuan) {
		this.jenisKetentuan = jenisKetentuan;
	}

	public DocumentType getDocumentType() {
		return documentType;
	}

	public void setDocumentType(DocumentType documentType) {
		this.documentType = documentType;
	}

	public DocumentCategory getDocumentCategory() {
		return documentCategory;
	}

	public void setDocumentCategory(DocumentCategory documentCategory) {
		this.documentCategory = documentCategory;
	}

	public DocumentTopic getDocumentTopic() {
		return documentTopic;
	}

	public void setDocumentTopic(DocumentTopic documentTopic) {
		this.documentTopic = documentTopic;
	}

	public String getDocumentNo() {
		return documentNo;
	}

	public void setDocumentNo(String documentNo) {
		this.documentNo = documentNo;
	}

	public String getNameIn() {
		return nameIn;
	}

	public void setNameIn(String nameIn) {
		this.nameIn = nameIn;
	}

	public String getNameEn() {
		return nameEn;
	}

	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

	

	public String getPicName() {
		return picName;
	}

	public void setPicName(String picName) {
		this.picName = picName;
	}

	@SuppressWarnings("static-access")
	public String getStatus() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if (locale != null && locale.equals(locale.ENGLISH)) {
			status = statusEn;
		} else {
			status = statusIn;
		}
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getDelId() {
		return delId;
	}

	public void setDelId(Long delId) {
		this.delId = delId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@SuppressWarnings("static-access")
	public String getDocumentTypeName() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if (locale != null && locale.equals(locale.ENGLISH)) {
			documentTypeName = documentTypeNameEn;
		} else {
			documentTypeName = documentTypeNameIn;
		}
		return documentTypeName;
	}

	public void setDocumentTypeName(String documentTypeName) {
		this.documentTypeName = documentTypeName;
	}

	public String getDocumentTypeNameIn() {
		return documentTypeNameIn;
	}

	public void setDocumentTypeNameIn(String documentTypeNameIn) {
		this.documentTypeNameIn = documentTypeNameIn;
	}

	public String getDocumentTypeNameEn() {
		return documentTypeNameEn;
	}

	public void setDocumentTypeNameEn(String documentTypeNameEn) {
		this.documentTypeNameEn = documentTypeNameEn;
	}

	@SuppressWarnings("static-access")
	public String getDocumentCategoryName() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if (locale != null && locale.equals(locale.ENGLISH)) {
			documentCategoryName = documentCategoryNameEn;
		} else {
			documentCategoryName = documentCategoryNameIn;
		}
		return documentCategoryName;
	}

	public void setDocumentCategoryName(String documentCategoryName) {
		this.documentCategoryName = documentCategoryName;
	}

	public String getDocumentCategoryNameIn() {
		return documentCategoryNameIn;
	}

	public void setDocumentCategoryNameIn(String documentCategoryNameIn) {
		this.documentCategoryNameIn = documentCategoryNameIn;
	}

	public String getDocumentCategoryNameEn() {
		return documentCategoryNameEn;
	}

	public void setDocumentCategoryNameEn(String documentCategoryNameEn) {
		this.documentCategoryNameEn = documentCategoryNameEn;
	}

	@SuppressWarnings("static-access")
	public String getDocumentTopicName() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if (locale != null && locale.equals(locale.ENGLISH)) {
			documentTopicName = documentTopicNameEn;
		} else {
			documentTopicName = documentTopicNameIn;
		}
		return documentTopicName;
	}

	public void setDocumentTopicName(String documentTopicName) {
		this.documentTopicName = documentTopicName;
	}

	public String getDocumentTopicNameIn() {
		return documentTopicNameIn;
	}

	public void setDocumentTopicNameIn(String documentTopicNameIn) {
		this.documentTopicNameIn = documentTopicNameIn;
	}

	public String getDocumentTopicNameEn() {
		return documentTopicNameEn;
	}

	public void setDocumentTopicNameEn(String documentTopicNameEn) {
		this.documentTopicNameEn = documentTopicNameEn;
	}

	@SuppressWarnings("static-access")
	public String getName() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if (locale != null && locale.equals(locale.ENGLISH)) {
			name = nameEn;
		} else {
			name = nameIn;
		}
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPublishedDateStr() {
		return publishedDateStr;
	}

	public void setPublishedDateStr(String publishedDateStr) {
		this.publishedDateStr = publishedDateStr;
	}

	public String getEffectiveDateStr() {
		return effectiveDateStr;
	}

	public void setEffectiveDateStr(String effectiveDateStr) {
		this.effectiveDateStr = effectiveDateStr;
	}

	public String getExpiredDateStr() {
		return expiredDateStr;
	}

	public void setExpiredDateStr(String expiredDateStr) {
		this.expiredDateStr = expiredDateStr;
	}

	public String getStatusNameIn() {
		return statusNameIn;
	}

	public void setStatusNameIn(String statusNameIn) {
		this.statusNameIn = statusNameIn;
	}

	public String getStatusNameEn() {
		return statusNameEn;
	}

	public void setStatusNameEn(String statusNameEn) {
		this.statusNameEn = statusNameEn;
	}

	public Long getSocializationId() {
		return socializationId;
	}

	public void setSocializationId(Long socializationId) {
		this.socializationId = socializationId;
	}

	public List<StatusConfirmationVO> getStatusList() {
		return statusList;
	}

	public void setStatusList(List<StatusConfirmationVO> statusList) {
		this.statusList = statusList;
	}

	public String getJenisKetentuanIn() {
		return jenisKetentuanIn;
	}

	public void setJenisKetentuanIn(String jenisKetentuanIn) {
		this.jenisKetentuanIn = jenisKetentuanIn;
	}

	public String getJenisKetentuanEn() {
		return jenisKetentuanEn;
	}

	public void setJenisKetentuanEn(String jenisKetentuanEn) {
		this.jenisKetentuanEn = jenisKetentuanEn;
	}

	public String getStatusIn() {
		return statusIn;
	}

	public void setStatusIn(String statusIn) {
		this.statusIn = statusIn;
	}

	public String getStatusEn() {
		return statusEn;
	}

	public void setStatusEn(String statusEn) {
		this.statusEn = statusEn;
	}	
	
	
	
}
