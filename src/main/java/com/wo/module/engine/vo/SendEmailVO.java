package com.wo.module.engine.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class SendEmailVO implements Serializable {

	private static final long serialVersionUID = 6395152164269025885L;

	private String pic1;

	private String pic2;

	private String pic3;

	private Date emailDate;

	private String counterType;

	private String emailTo;

	private String emailCc1;

	private String emailCc2;

	private String regulationTitleIn;

	private String regulationTitleEn;

	private Date targetDate;

	private String documentNo;

	private Date publishedDate;

	private Date effctiveDate;

	private Date letterReceiveDate;

	private Date letterDate;

	private String letterNo;

	private String reportNameIn;

	private String reportNameEn;

	private String reportType;

	private Date dueDate;

	private String supportingUnit;

	private String dedicatedTo;

	private String sanction;

	private Long id;

	private Long emailId;
	
	private Long parentId;

	private String slaType;

	private String sla;

	private String publisherUnit;

	private String divisionName;

	private String pic1Name;

	private String pic2Name;

	private String pic3Name;

	private String counterTypeIn;

	private String counterTypeEn;

	private String senderIn;

	private String senderEn;

	private String reportTypeIn;

	private String reportTypeEn;

	private String documentCategoryIn;

	private String documentCategoryEn;

	private String letterSummary;

	private List<SendEmailDetailVO> listDetail;

	private String reviewCategoryIn;

	private String reviewCategoryEn;

	private String reviewedBranch;

	private String documentDateStr;

	private String perihalIn;

	private String perihalEn;
	
	private String auditObject;
	private String auditObjectIn;
	private String auditObjectEn;
	private String auditTopicIn;
	private String auditTopicEn;
	private String auditCategory;
	private String auditCategoryIn;
	private String auditCategoryEn;
	private String auditDateFrom;
	private String auditDateTo;
	private String auditTypeIn;
	private String auditTypeEn;
	private String scope;
	private List<String> detail1;
	private List<String> detail2;
	private List<String> detail3;
	
	private String emailCcCompliance;
	
	private String senderCode;
	
	private String regionCode;
	
	private Date debitted;
	
	private Long amount;
	
	private String breaches;
	
	private String rootCause;
	
	private String category;
	
	private String inspectionTitle;
	private String inspectionNo;
	private String subject;
	private String observationResult;
	private String recommendation;
	private Date startDate;
	private Date endDate;
	

	public String getPic1() {
		return pic1;
	}

	public void setPic1(String pic1) {
		this.pic1 = pic1;
	}

	public String getPic2() {
		return pic2;
	}

	public void setPic2(String pic2) {
		this.pic2 = pic2;
	}

	public String getPic3() {
		return pic3;
	}

	public void setPic3(String pic3) {
		this.pic3 = pic3;
	}

	public Date getEmailDate() {
		return emailDate;
	}

	public void setEmailDate(Date emailDate) {
		this.emailDate = emailDate;
	}

	public String getCounterType() {
		return counterType;
	}

	public void setCounterType(String counterType) {
		this.counterType = counterType;
	}

	public String getEmailTo() {
		return emailTo;
	}

	public void setEmailTo(String emailTo) {
		this.emailTo = emailTo;
	}

	public String getEmailCc1() {
		return emailCc1;
	}

	public void setEmailCc1(String emailCc1) {
		this.emailCc1 = emailCc1;
	}

	public String getEmailCc2() {
		return emailCc2;
	}

	public void setEmailCc2(String emailCc2) {
		this.emailCc2 = emailCc2;
	}

	public Date getTargetDate() {
		return targetDate;
	}

	public void setTargetDate(Date targetDate) {
		this.targetDate = targetDate;
	}

	public String getDocumentNo() {
		return documentNo;
	}

	public void setDocumentNo(String documentNo) {
		this.documentNo = documentNo;
	}

	public Date getPublishedDate() {
		return publishedDate;
	}

	public void setPublishedDate(Date publishedDate) {
		this.publishedDate = publishedDate;
	}

	public Date getEffctiveDate() {
		return effctiveDate;
	}

	public void setEffctiveDate(Date effctiveDate) {
		this.effctiveDate = effctiveDate;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Date getLetterReceiveDate() {
		return letterReceiveDate;
	}

	public void setLetterReceiveDate(Date letterReceiveDate) {
		this.letterReceiveDate = letterReceiveDate;
	}

	public Date getLetterDate() {
		return letterDate;
	}

	public void setLetterDate(Date letterDate) {
		this.letterDate = letterDate;
	}

	public String getLetterNo() {
		return letterNo;
	}

	public void setLetterNo(String letterNo) {
		this.letterNo = letterNo;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public String getSupportingUnit() {
		return supportingUnit;
	}

	public void setSupportingUnit(String supportingUnit) {
		this.supportingUnit = supportingUnit;
	}

	public String getDedicatedTo() {
		return dedicatedTo;
	}

	public void setDedicatedTo(String dedicatedTo) {
		this.dedicatedTo = dedicatedTo;
	}

	public String getSanction() {
		return sanction;
	}

	public void setSanction(String sanction) {
		this.sanction = sanction;
	}

	public Long getEmailId() {
		return emailId;
	}

	public void setEmailId(Long emailId) {
		this.emailId = emailId;
	}

	public String getSlaType() {
		return slaType;
	}

	public void setSlaType(String slaType) {
		this.slaType = slaType;
	}

	public String getSla() {
		return sla;
	}

	public void setSla(String sla) {
		this.sla = sla;
	}

	public String getReportNameIn() {
		return reportNameIn;
	}

	public void setReportNameIn(String reportNameIn) {
		this.reportNameIn = reportNameIn;
	}

	public String getReportNameEn() {
		return reportNameEn;
	}

	public void setReportNameEn(String reportNameEn) {
		this.reportNameEn = reportNameEn;
	}

	public String getRegulationTitleIn() {
		return regulationTitleIn;
	}

	public void setRegulationTitleIn(String regulationTitleIn) {
		this.regulationTitleIn = regulationTitleIn;
	}

	public String getRegulationTitleEn() {
		return regulationTitleEn;
	}

	public void setRegulationTitleEn(String regulationTitleEn) {
		this.regulationTitleEn = regulationTitleEn;
	}

	public String getPublisherUnit() {
		return publisherUnit;
	}

	public void setPublisherUnit(String publisherUnit) {
		this.publisherUnit = publisherUnit;
	}

	public String getDivisionName() {
		return divisionName;
	}

	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}

	public String getPic1Name() {
		return pic1Name;
	}

	public void setPic1Name(String pic1Name) {
		this.pic1Name = pic1Name;
	}

	public String getPic2Name() {
		return pic2Name;
	}

	public void setPic2Name(String pic2Name) {
		this.pic2Name = pic2Name;
	}

	public String getPic3Name() {
		return pic3Name;
	}

	public void setPic3Name(String pic3Name) {
		this.pic3Name = pic3Name;
	}

	public List<SendEmailDetailVO> getListDetail() {
		return listDetail;
	}

	public void setListDetail(List<SendEmailDetailVO> listDetail) {
		this.listDetail = listDetail;
	}

	public String getCounterTypeIn() {
		return counterTypeIn;
	}

	public void setCounterTypeIn(String counterTypeIn) {
		this.counterTypeIn = counterTypeIn;
	}

	public String getCounterTypeEn() {
		return counterTypeEn;
	}

	public void setCounterTypeEn(String counterTypeEn) {
		this.counterTypeEn = counterTypeEn;
	}

	public String getSenderIn() {
		return senderIn;
	}

	public void setSenderIn(String senderIn) {
		this.senderIn = senderIn;
	}

	public String getSenderEn() {
		return senderEn;
	}

	public void setSenderEn(String senderEn) {
		this.senderEn = senderEn;
	}

	public String getReportTypeIn() {
		return reportTypeIn;
	}

	public void setReportTypeIn(String reportTypeIn) {
		this.reportTypeIn = reportTypeIn;
	}

	public String getReportTypeEn() {
		return reportTypeEn;
	}

	public void setReportTypeEn(String reportTypeEn) {
		this.reportTypeEn = reportTypeEn;
	}

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

	public String getLetterSummary() {
		return letterSummary;
	}

	public void setLetterSummary(String letterSummary) {
		this.letterSummary = letterSummary;
	}

	public String getReviewCategoryIn() {
		return reviewCategoryIn;
	}

	public void setReviewCategoryIn(String reviewCategoryIn) {
		this.reviewCategoryIn = reviewCategoryIn;
	}

	public String getReviewCategoryEn() {
		return reviewCategoryEn;
	}

	public void setReviewCategoryEn(String reviewCategoryEn) {
		this.reviewCategoryEn = reviewCategoryEn;
	}

	public String getReviewedBranch() {
		return reviewedBranch;
	}

	public void setReviewedBranch(String reviewedBranch) {
		this.reviewedBranch = reviewedBranch;
	}

	public String getDocumentDateStr() {
		return documentDateStr;
	}

	public void setDocumentDateStr(String documentDateStr) {
		this.documentDateStr = documentDateStr;
	}

	public String getPerihalIn() {
		return perihalIn;
	}

	public void setPerihalIn(String perihalIn) {
		this.perihalIn = perihalIn;
	}

	public String getPerihalEn() {
		return perihalEn;
	}

	public void setPerihalEn(String perihalEn) {
		this.perihalEn = perihalEn;
	}

	public String getAuditObject() {
		return auditObject;
	}

	public void setAuditObject(String auditObject) {
		this.auditObject = auditObject;
	}

	public String getAuditTopicIn() {
		return auditTopicIn;
	}

	public void setAuditTopicIn(String auditTopicIn) {
		this.auditTopicIn = auditTopicIn;
	}

	public String getAuditTopicEn() {
		return auditTopicEn;
	}

	public void setAuditTopicEn(String auditTopicEn) {
		this.auditTopicEn = auditTopicEn;
	}

	public String getAuditCategory() {
		return auditCategory;
	}

	public void setAuditCategory(String auditCategory) {
		this.auditCategory = auditCategory;
	}

	public String getAuditDateFrom() {
		return auditDateFrom;
	}

	public void setAuditDateFrom(String auditDateFrom) {
		this.auditDateFrom = auditDateFrom;
	}

	public String getAuditDateTo() {
		return auditDateTo;
	}

	public void setAuditDateTo(String auditDateTo) {
		this.auditDateTo = auditDateTo;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getAuditObjectIn() {
		return auditObjectIn;
	}

	public void setAuditObjectIn(String auditObjectIn) {
		this.auditObjectIn = auditObjectIn;
	}

	public String getAuditObjectEn() {
		return auditObjectEn;
	}

	public void setAuditObjectEn(String auditObjectEn) {
		this.auditObjectEn = auditObjectEn;
	}

	public String getAuditCategoryIn() {
		return auditCategoryIn;
	}

	public void setAuditCategoryIn(String auditCategoryIn) {
		this.auditCategoryIn = auditCategoryIn;
	}

	public String getAuditCategoryEn() {
		return auditCategoryEn;
	}

	public void setAuditCategoryEn(String auditCategoryEn) {
		this.auditCategoryEn = auditCategoryEn;
	}

	public List<String> getDetail1() {
		return detail1;
	}

	public void setDetail1(List<String> detail1) {
		this.detail1 = detail1;
	}

	public List<String> getDetail2() {
		return detail2;
	}

	public void setDetail2(List<String> detail2) {
		this.detail2 = detail2;
	}

	public List<String> getDetail3() {
		return detail3;
	}

	public void setDetail3(List<String> detail3) {
		this.detail3 = detail3;
	}

	public String getSenderCode() {
		return senderCode;
	}

	public void setSenderCode(String senderCode) {
		this.senderCode = senderCode;
	}

	public String getAuditTypeIn() {
		return auditTypeIn;
	}

	public void setAuditTypeIn(String auditTypeIn) {
		this.auditTypeIn = auditTypeIn;
	}

	public String getAuditTypeEn() {
		return auditTypeEn;
	}

	public void setAuditTypeEn(String auditTypeEn) {
		this.auditTypeEn = auditTypeEn;
	}

	public String getEmailCcCompliance() {
		return emailCcCompliance;
	}

	public void setEmailCcCompliance(String emailCcCompliance) {
		this.emailCcCompliance = emailCcCompliance;
	}

	public String getRegionCode() {
		return regionCode;
	}

	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}

	public Date getDebitted() {
		return debitted;
	}

	public void setDebitted(Date debitted) {
		this.debitted = debitted;
	}

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public String getBreaches() {
		return breaches;
	}

	public void setBreaches(String breaches) {
		this.breaches = breaches;
	}

	public String getRootCause() {
		return rootCause;
	}

	public void setRootCause(String rootCause) {
		this.rootCause = rootCause;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getInspectionTitle() {
		return inspectionTitle;
	}

	public void setInspectionTitle(String inspectionTitle) {
		this.inspectionTitle = inspectionTitle;
	}

	public String getInspectionNo() {
		return inspectionNo;
	}

	public void setInspectionNo(String inspectionNo) {
		this.inspectionNo = inspectionNo;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getObservationResult() {
		return observationResult;
	}

	public void setObservationResult(String observationResult) {
		this.observationResult = observationResult;
	}

	public String getRecommendation() {
		return recommendation;
	}

	public void setRecommendation(String recommendation) {
		this.recommendation = recommendation;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	
}