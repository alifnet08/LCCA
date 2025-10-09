package com.wo.module.socializationFE.vo;

import java.io.Serializable;
import java.util.Date;

public class SocializationFEVo implements Serializable{

	private static final long serialVersionUID = -5496439334482087382L;

	private Long trcSocializationId;
	private Long trcSocializationFollowupId;
	private String regulationName;
	private String regulationNameIn;
	private String regulationNameEn;
	private String status;
	private String statusCode;
	private String statusIn;
	private String statusEn;
	private Date targetDate;
	private String targetDateStr;
	private String followupStatus;
	private String followupStatusCode;
	private String followupStatusIn;
	private String followupStatusEn;
	private String pic1;
	private String pic2;
	private String pic3;
	private String complianceNote;
	private String notes;
	
	public Long getTrcSocializationId() {
		return trcSocializationId;
	}
	public void setTrcSocializationId(Long trcSocializationId) {
		this.trcSocializationId = trcSocializationId;
	}
	public String getRegulationName() {
		return regulationName;
	}
	public void setRegulationName(String regulationName) {
		this.regulationName = regulationName;
	}
	public String getRegulationNameIn() {
		return regulationNameIn;
	}
	public void setRegulationNameIn(String regulationNameIn) {
		this.regulationNameIn = regulationNameIn;
	}
	public String getRegulationNameEn() {
		return regulationNameEn;
	}
	public void setRegulationNameEn(String regulationNameEn) {
		this.regulationNameEn = regulationNameEn;
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
	public String getFollowupStatus() {
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
	public String getComplianceNote() {
		return complianceNote;
	}
	public void setComplianceNote(String complianceNote) {
		this.complianceNote = complianceNote;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Long getTrcSocializationFollowupId() {
		return trcSocializationFollowupId;
	}
	public void setTrcSocializationFollowupId(Long trcSocializationFollowupId) {
		this.trcSocializationFollowupId = trcSocializationFollowupId;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	
}
