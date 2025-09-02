package com.wo.module.internalRegulation.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.documentCategory.model.DocumentCategory;
import com.wo.module.documentTopic.model.DocumentTopic;
import com.wo.module.documentType.model.DocumentType;
import com.wo.module.externalRegulation.model.RegulationAttachment;
import com.wo.module.externalRegulation.model.RegulationTrackRecord;
import com.wo.module.parameter.model.ParameterDetail;

public class InternalRegulation extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = 4923524013545521376L;
	
	private Long regulationId;
	private String jenisKetentuan;
	private DocumentType documentType;
	private DocumentCategory documentCategory;
	private DocumentTopic documentTopic;
	private String documentNo;
	private String nameIn;
	private String nameEn;
	private Date publishedDate;
	private Date expiredDate;
	private Date effectiveDate;
	private String publisherUnit;
	private String status;
	private ParameterDetail typeReviewDate;
	
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
	private String directorate;
	
	private Long delId;
	
	private List<RegulationTrackRecord> regulationTrackRecords;
	private List<RegulationAttachment> regulationAttachments;

	
	//helper
	private String statusNameIn;
	private String statusNameEn;
	private String statusName;
	private String rekamJejak;
	private String rekamJejakCode;
	private String prevDocNo;
	private String prevRegTitle;
	
	public Long getRegulationId() {
		return regulationId;
	}

	public void setRegulationId(Long regulationId) {
		this.regulationId = regulationId;
	}

	public String getJenisKetentuan() {
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

	public Date getPublishedDate() {
		return publishedDate;
	}

	public void setPublishedDate(Date publishedDate) {
		this.publishedDate = publishedDate;
	}

	public Date getExpiredDate() {
		return expiredDate;
	}

	public void setExpiredDate(Date expiredDate) {
		this.expiredDate = expiredDate;
	}

	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public String getPublisherUnit() {
		return publisherUnit;
	}

	public void setPublisherUnit(String publisherUnit) {
		this.publisherUnit = publisherUnit;
	}

	public String getStatus() {
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

	public List<RegulationTrackRecord> getRegulationTrackRecords() {
		return regulationTrackRecords;
	}

	public void setRegulationTrackRecords(List<RegulationTrackRecord> regulationTrackRecords) {
		this.regulationTrackRecords = regulationTrackRecords;
	}

	public List<RegulationAttachment> getRegulationAttachments() {
		return regulationAttachments;
	}

	public void setRegulationAttachments(List<RegulationAttachment> regulationAttachments) {
		this.regulationAttachments = regulationAttachments;
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

	@SuppressWarnings("static-access")
	public String getStatusName() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if(locale != null && locale.equals(locale.ENGLISH)) {
			statusName = statusNameEn;
		}else {
			statusName = statusNameIn;
		}
		
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getDirectorate() {
		return directorate;
	}

	public void setDirectorate(String directorate) {
		this.directorate = directorate;
	}

	public ParameterDetail getTypeReviewDate() {
		return typeReviewDate;
	}

	public void setTypeReviewDate(ParameterDetail typeReviewDate) {
		this.typeReviewDate = typeReviewDate;
	}

	public String getRekamJejak() {
		return rekamJejak;
	}

	public void setRekamJejak(String rekamJejak) {
		this.rekamJejak = rekamJejak;
	}

	public String getPrevDocNo() {
		return prevDocNo;
	}

	public void setPrevDocNo(String prevDocNo) {
		this.prevDocNo = prevDocNo;
	}

	public String getPrevRegTitle() {
		return prevRegTitle;
	}

	public void setPrevRegTitle(String prevRegTitle) {
		this.prevRegTitle = prevRegTitle;
	}

	public String getRekamJejakCode() {
		return rekamJejakCode;
	}

	public void setRekamJejakCode(String rekamJejakCode) {
		this.rekamJejakCode = rekamJejakCode;
	}
	
	

}
