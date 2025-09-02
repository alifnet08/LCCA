package com.wo.module.complianceReviewDocumentView.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.complianceReviewDocumentView.model.ComplianceReviewDocumentAttachmentView;
import com.wo.module.complianceReviewDocumentView.model.ComplianceReviewDocumentPicComplianceView;
import com.wo.module.parameter.model.ParameterDetail;

public class ComplianceReviewDocumentView extends BaseEntity implements Serializable{

	private static final long serialVersionUID = -3756610714653700159L;
	
	private Long complianceReviewDocumentId;
	private ParameterDetail documentType; 
	private String documentNo;
	private Date receivedDate;
	private Date completeDate;
	private ParameterDetail documentSubmitter;
	private String remarks;
	private Long divisionId;
	private String materi;
	
	private List<ComplianceReviewDocumentPicComplianceView> complianceReviewDocumentPicComplianceViews;
	private List<ComplianceReviewDocumentAttachmentView> complianceReviewDocumentAttachmentViews;
	
	//helper
	private String documentTypeNameIn;
	private String documentTypeNameEn;
	private String documentTypeName;
	
	private String documentSubmitterNameIn;
	private String documentSubmitterNameEn;
	private String documentSubmitterName;
	
	private String receivedDateStr;
	private String completeDateStr;
	
	private String divisionName;
	
	public Long getComplianceReviewDocumentId() {
		return complianceReviewDocumentId;
	}
	public void setComplianceReviewDocumentId(Long complianceReviewDocumentId) {
		this.complianceReviewDocumentId = complianceReviewDocumentId;
	}
	public ParameterDetail getDocumentType() {
		return documentType;
	}
	public void setDocumentType(ParameterDetail documentType) {
		this.documentType = documentType;
	}
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
	public ParameterDetail getDocumentSubmitter() {
		return documentSubmitter;
	}
	public void setDocumentSubmitter(ParameterDetail documentSubmitter) {
		this.documentSubmitter = documentSubmitter;
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
	public List<ComplianceReviewDocumentPicComplianceView> getComplianceReviewDocumentPicComplianceViews() {
		return complianceReviewDocumentPicComplianceViews;
	}
	public void setComplianceReviewDocumentPicComplianceViews(
			List<ComplianceReviewDocumentPicComplianceView> complianceReviewDocumentPicComplianceViews) {
		this.complianceReviewDocumentPicComplianceViews = complianceReviewDocumentPicComplianceViews;
	}
	public List<ComplianceReviewDocumentAttachmentView> getComplianceReviewDocumentAttachmentViews() {
		return complianceReviewDocumentAttachmentViews;
	}
	public void setComplianceReviewDocumentAttachmentViews(
			List<ComplianceReviewDocumentAttachmentView> complianceReviewDocumentAttachmentViews) {
		this.complianceReviewDocumentAttachmentViews = complianceReviewDocumentAttachmentViews;
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
}
