package com.wo.module.regulatoryReportingFE.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.Locale;

import javax.faces.context.FacesContext;

public class RegulatoryReportingFEVo implements Serializable{

	private static final long serialVersionUID = -3190608751396576271L;
	
	private Long regulatoryReportingId;
	private String reportName;
	private String reportNameIn;
	private String reportNameEn;
	private Date targetDate;
	private String targetDateStr;
	private String status;
	private String statusCode;
	private String statusIn;
	private String statusEn;
	private String followupStatus;
	private String followupStatusCode;
	private String followupStatusIn;
	private String followupStatusEn;
	private Date followupDate;
	private String followupDateStr;
	private Long regulatoryReportingFollowupId;
	
	public Long getRegulatoryReportingId() {
		return regulatoryReportingId;
	}
	public void setRegulatoryReportingId(Long regulatoryReportingId) {
		this.regulatoryReportingId = regulatoryReportingId;
	}
	@SuppressWarnings("static-access")
	public String getReportName() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		
		if (locale != null && locale.equals(locale.ENGLISH)) {
			reportName = reportNameEn;
		} else {
			reportName = reportNameIn;
		}
		
		return reportName;
	}
	public void setReportName(String reportName) {
		this.reportName = reportName;
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
	public Date getTargetDate() {
		return targetDate;
	}
	public void setTargetDate(Date targetDate) {
		this.targetDate = targetDate;
	}
	public String getTargetDateStr() {
		return targetDateStr;
	}
	public void setTargetDateStr(String targetDateStr) {
		this.targetDateStr = targetDateStr;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
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
	@SuppressWarnings("static-access")
	public String getFollowupStatus() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		
		if (locale != null && locale.equals(locale.ENGLISH)) {
			followupStatus = followupStatusEn;
		} else {
			followupStatus = followupStatusIn;
		}
		return followupStatus;
	}
	public void setFollowupStatus(String followupStatus) {
		this.followupStatus = followupStatus;
	}
	public String getFollowupStatusCode() {
		return followupStatusCode;
	}
	public void setFollowupStatusCode(String followupStatusCode) {
		this.followupStatusCode = followupStatusCode;
	}
	public String getFollowupStatusIn() {
		return followupStatusIn;
	}
	public void setFollowupStatusIn(String followupStatusIn) {
		this.followupStatusIn = followupStatusIn;
	}
	public String getFollowupStatusEn() {
		return followupStatusEn;
	}
	public void setFollowupStatusEn(String followupStatusEn) {
		this.followupStatusEn = followupStatusEn;
	}
	public Date getFollowupDate() {
		return followupDate;
	}
	public void setFollowupDate(Date followupDate) {
		this.followupDate = followupDate;
	}
	public String getFollowupDateStr() {
		return followupDateStr;
	}
	public void setFollowupDateStr(String followupDateStr) {
		this.followupDateStr = followupDateStr;
	}
	public Long getRegulatoryReportingFollowupId() {
		return regulatoryReportingFollowupId;
	}
	public void setRegulatoryReportingFollowupId(Long regulatoryReportingFollowupId) {
		this.regulatoryReportingFollowupId = regulatoryReportingFollowupId;
	}
	
}
