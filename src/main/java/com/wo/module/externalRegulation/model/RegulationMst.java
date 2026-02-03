package com.wo.module.externalRegulation.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.counterType.model.CounterType;
import com.wo.module.documentCategory.model.DocumentCategory;
import com.wo.module.documentTopic.model.DocumentTopic;
import com.wo.module.documentType.model.DocumentType;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.user.model.User;

public class RegulationMst extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 6395152164269025885L;
	private Long regulationId;
	private ParameterDetail jenisKetentuan;
	private DocumentType documentType;
	private DocumentCategory documentCategory;
	private DocumentTopic documentTopic;
	private String documentNo;
	private String nameIn;
	private String nameEn;
	private Date publishedDate;
	private Date expiredDate;
	private Date effectiveDate;
//	private String publisherUnit;
	private String status;
	
	private String receiver;
	private String typeReviewDate;
	
	private Long delId;
	
	private String nameTemp;
	
	private List<RegulationTrackRecordMst> regulationTrackRecords;
	private List<RegulationAttachmentMst> regulationAttachments;
	private List<RegulationProposerUnitMst> regulationProposerUnits;
	
	private String publishedDateStr;
	private String expiredDateStr;
	private String effectiveDateStr;
	
	private String descriptionIn;
	private String descriptionEn;
//	private ParameterDetail directorate;
//	private ParameterDetail publisherUnit;
	private String directorate;
	private String publisherUnit;
	private User puk;
	private User pic;
	private CounterType counterType;

	// helper
	private String picNameTemp;
	private String pukNameTemp;
	
	public Long getRegulationId() {
		return regulationId;
	}

	public void setRegulationId(Long regulationId) {
		this.regulationId = regulationId;
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

	public ParameterDetail getJenisKetentuan() {
		return jenisKetentuan;
	}

	public void setJenisKetentuan(ParameterDetail jenisKetentuan) {
		this.jenisKetentuan = jenisKetentuan;
	}

	public String getPublishedDateStr() {
		return publishedDateStr;
	}

	public void setPublishedDateStr(String publishedDateStr) {
		this.publishedDateStr = publishedDateStr;
	}

	public String getExpiredDateStr() {
		return expiredDateStr;
	}

	public void setExpiredDateStr(String expiredDateStr) {
		this.expiredDateStr = expiredDateStr;
	}

	public String getEffectiveDateStr() {
		return effectiveDateStr;
	}

	public void setEffectiveDateStr(String effectiveDateStr) {
		this.effectiveDateStr = effectiveDateStr;
	}

	public List<RegulationTrackRecordMst> getRegulationTrackRecords() {
		return regulationTrackRecords;
	}

	public void setRegulationTrackRecords(List<RegulationTrackRecordMst> regulationTrackRecords) {
		this.regulationTrackRecords = regulationTrackRecords;
	}

	public List<RegulationAttachmentMst> getRegulationAttachments() {
		return regulationAttachments;
	}

	public void setRegulationAttachments(List<RegulationAttachmentMst> regulationAttachments) {
		this.regulationAttachments = regulationAttachments;
	}

	@SuppressWarnings("static-access")
	public String getNameTemp() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if (locale != null && locale.equals(locale.ENGLISH)) {
			nameTemp = nameEn;
		} else {
			nameTemp = nameIn;
		}
		
		return nameTemp;
	}

	public void setNameTemp(String nameTemp) {
		this.nameTemp = nameTemp;
	}

	public String getDescriptionIn() {
		return descriptionIn;
	}

	public void setDescriptionIn(String descriptionIn) {
		this.descriptionIn = descriptionIn;
	}

	public String getDescriptionEn() {
		return descriptionEn;
	}

	public void setDescriptionEn(String descriptionEn) {
		this.descriptionEn = descriptionEn;
	}

	public User getPuk() {
		return puk;
	}

	public void setPuk(User puk) {
		this.puk = puk;
	}

	public User getPic() {
		return pic;
	}

	public void setPic(User pic) {
		this.pic = pic;
	}

	public String getPicNameTemp() {
		return picNameTemp;
	}

	public void setPicNameTemp(String picNameTemp) {
		this.picNameTemp = picNameTemp;
	}

	public String getPukNameTemp() {
		return pukNameTemp;
	}

	public void setPukNameTemp(String pukNameTemp) {
		this.pukNameTemp = pukNameTemp;
	}

//	public ParameterDetail getDirectorate() {
//		return directorate;
//	}
//
//	public void setDirectorate(ParameterDetail directorate) {
//		this.directorate = directorate;
//	}
//
//	public ParameterDetail getPublisherUnit() {
//		return publisherUnit;
//	}
//
//	public void setPublisherUnit(ParameterDetail publisherUnit) {
//		this.publisherUnit = publisherUnit;
//	}

	public CounterType getCounterType() {
		return counterType;
	}

	public void setCounterType(CounterType counterType) {
		this.counterType = counterType;
	}

	public void setDirectorate(String directorate) {
		this.directorate = directorate;
	}

	public void setPublisherUnit(String publisherUnit) {
		this.publisherUnit = publisherUnit;
	}

	public String getDirectorate() {
		return directorate;
	}

	public String getPublisherUnit() {
		return publisherUnit;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getTypeReviewDate() {
		return typeReviewDate;
	}

	public void setTypeReviewDate(String typeReviewDate) {
		this.typeReviewDate = typeReviewDate;
	}

	public List<RegulationProposerUnitMst> getRegulationProposerUnits() {
		return regulationProposerUnits;
	}

	public void setRegulationProposerUnits(List<RegulationProposerUnitMst> regulationProposerUnits) {
		this.regulationProposerUnits = regulationProposerUnits;
	}	
	
}