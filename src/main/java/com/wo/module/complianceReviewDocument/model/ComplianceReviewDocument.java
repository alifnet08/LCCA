package com.wo.module.complianceReviewDocument.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.parameter.model.ParameterDetail;

public class ComplianceReviewDocument extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 4701579265839588096L;
	
	private Long complianceReviewDocumentId;
	private ParameterDetail documentType; 
	private String documentNo;
	private Date receivedDate;
	private Date completeDate;
	private ParameterDetail documentSubmitter;
	private String remarks;
	private Long divisionId;
	private String divisionMultiId;
	
	
	public String getDivisionMultiId() {
		return divisionMultiId;
	}
	public void setDivisionMultiId(String divisionMultiId) {
		this.divisionMultiId = divisionMultiId;
	}
	private String materi;
	
	private List<ComplianceReviewDocumentPicCompliance> complianceReviewDocumentPicCompliance = new ArrayList<ComplianceReviewDocumentPicCompliance>();
	private List<ComplianceReviewDocumentAttachment> complianceReviewDocumentAttachments;
	
	//helper / vo
	private String documentTypeNameIn;
	private String documentTypeNameEn;
	private String documentTypeName;
	
	private String documentSubmitterNameIn;
	private String documentSubmitterNameEn;
	private String documentSubmitterName;
	
	private String receivedDateStr;
	private String completeDateStr;
	
	private String documentTypeCode;
	
	private String divisionName;
	private List<String> divisionNames;
	
	public Long getComplianceReviewDocumentId() {
		return complianceReviewDocumentId;
	}
	public void setComplianceReviewDocumentId(Long complianceReviewDocumentId) {
		this.complianceReviewDocumentId = complianceReviewDocumentId;
	}

	/*
	 * public DocumentType getDocumentType() { return documentType; } public void
	 * setDocumentType(DocumentType documentType) { this.documentType =
	 * documentType; }
	 */
	public String getDocumentNo() {
		return documentNo;
	}
	public void setDocumentNo(String documentNo) {
		this.documentNo = documentNo;
	}
	public Date getReceivedDate() {
		return receivedDate;
	}
	public void setReceivedDate(Date receivedDate) {
		this.receivedDate = receivedDate;
	}
	public Date getCompleteDate() {
		return completeDate;
	}
	public void setCompleteDate(Date completeDate) {
		this.completeDate = completeDate;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public Long getDivisionId() {
		return divisionId;
	}
	public void setDivisionId(Long divisionId) {
		this.divisionId = divisionId;
	}
	public ParameterDetail getDocumentType() {
		return documentType;
	}
	public void setDocumentType(ParameterDetail documentType) {
		this.documentType = documentType;
	}
	public ParameterDetail getDocumentSubmitter() {
		return documentSubmitter;
	}
	public void setDocumentSubmitter(ParameterDetail documentSubmitter) {
		this.documentSubmitter = documentSubmitter;
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
	public String getDocumentTypeName() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if(locale != null && locale.equals(locale.ENGLISH)) {
			documentTypeName = documentTypeNameEn;
		} else {
			documentTypeName = documentTypeNameIn;
		}
		return documentTypeName;
	}
	public void setDocumentTypeName(String documentTypeName) {
		this.documentTypeName = documentTypeName;
	}
	public String getDocumentSubmitterNameIn() {
		return documentSubmitterNameIn;
	}
	public void setDocumentSubmitterNameIn(String documentSubmitterNameIn) {
		this.documentSubmitterNameIn = documentSubmitterNameIn;
	}
	public String getDocumentSubmitterNameEn() {
		return documentSubmitterNameEn;
	}
	public void setDocumentSubmitterNameEn(String documentSubmitterNameEn) {
		this.documentSubmitterNameEn = documentSubmitterNameEn;
	}
	@SuppressWarnings("static-access")
	public String getDocumentSubmitterName() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if(locale != null && locale.equals(locale.ENGLISH)) {
			documentSubmitterName = documentSubmitterNameEn;
		}else {
			documentSubmitterName = documentSubmitterNameIn;
		}
		return documentSubmitterName;
	}
	public void setDocumentSubmitterName(String documentSubmitterName) {
		this.documentSubmitterName = documentSubmitterName;
	}
	public String getReceivedDateStr() {
		return receivedDateStr;
	}
	public void setReceivedDateStr(String receivedDateStr) {
		this.receivedDateStr = receivedDateStr;
	}
	public String getCompleteDateStr() {
		return completeDateStr;
	}
	public void setCompleteDateStr(String completeDateStr) {
		this.completeDateStr = completeDateStr;
	}
	public List<ComplianceReviewDocumentPicCompliance> getComplianceReviewDocumentPicCompliance() {
		return complianceReviewDocumentPicCompliance;
	}
	public void setComplianceReviewDocumentPicCompliance(List<ComplianceReviewDocumentPicCompliance> complianceReviewDocumentPicCompliance) {
		this.complianceReviewDocumentPicCompliance = complianceReviewDocumentPicCompliance;
	}
	public List<ComplianceReviewDocumentAttachment> getComplianceReviewDocumentAttachments() {
		return complianceReviewDocumentAttachments;
	}
	public void setComplianceReviewDocumentAttachments(List<ComplianceReviewDocumentAttachment> complianceReviewDocumentAttachments) {
		this.complianceReviewDocumentAttachments = complianceReviewDocumentAttachments;
	}
	public String getDocumentTypeCode() {
		return documentTypeCode;
	}
	public void setDocumentTypeCode(String documentTypeCode) {
		this.documentTypeCode = documentTypeCode;
	}
	public String getMateri() {
		return materi;
	}
	public void setMateri(String materi) {
		this.materi = materi;
	}
	public String getDivisionName() {
		return divisionName;
	}
	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}
	public List<String> getDivisionNames() {
		return divisionNames;
	}
	public void setDivisionNames(List<String> divisionNames) {
		this.divisionNames = divisionNames;
	}
	
}