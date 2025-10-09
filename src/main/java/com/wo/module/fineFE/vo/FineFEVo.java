package com.wo.module.fineFE.vo;

import java.io.Serializable;
import java.util.Date;

public class FineFEVo implements Serializable{

	private static final long serialVersionUID = 6575743490748472309L;
	
	private Long fineId;
	private Long finePicFollowupId;
	private String letterNo;
	private String followupStatus;
	private String followupStatusCode;
	private String followupStatusIn;
	private String followupStatusEn;
	private Date targetDate;
	private String targetDateStr;
	private Date targetResponseDate;
	private String targetResponseDateStr;
	private String status;
	private String statusCode;
	private String statusIn;
	private String statusEn;
	
	public Long getFineId() {
		return fineId;
	}
	public void setFineId(Long fineId) {
		this.fineId = fineId;
	}
	public Long getFinePicFollowupId() {
		return finePicFollowupId;
	}
	public void setFinePicFollowupId(Long finePicFollowupId) {
		this.finePicFollowupId = finePicFollowupId;
	}
	public String getLetterNo() {
		return letterNo;
	}
	public void setLetterNo(String letterNo) {
		this.letterNo = letterNo;
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
	public static long getSerialversionuid() {
		return serialVersionUID;
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
	public Date getTargetResponseDate() {
		return targetResponseDate;
	}
	public void setTargetResponseDate(Date targetResponseDate) {
		this.targetResponseDate = targetResponseDate;
	}
	public String getTargetResponseDateStr() {
		return targetResponseDateStr;
	}
	public void setTargetResponseDateStr(String targetResponseDateStr) {
		this.targetResponseDateStr = targetResponseDateStr;
	}
	
}
