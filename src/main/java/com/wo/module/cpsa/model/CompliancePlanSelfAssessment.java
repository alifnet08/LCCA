package com.wo.module.cpsa.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.counterType.model.CounterType;
import com.wo.module.parameter.model.ParameterDetail;

public class CompliancePlanSelfAssessment extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 9109140040146899800L;

	private Long cpsaId;
	private Long fileSize;
	private Long fileQuestSize;
	
	private Date uploadDate;
	private Date periodFrom;
	private Date periodTo;
	private Date targetDate;	
	private Date letterDate;

	private String attachmentFile;
	private String fileId;
	private String cpsaName;
	private String cpsaBranchSubBranch;
	private String note;
	private String letterNo;
	private String letterAbout;
	private String attachmentQuestFile;
	private String fileQuestId;
	private String filePathDownload;
	private String statusDownload;
	private String cpsaStatus;
	
	private ParameterDetail cpsaType;
	private CounterType counterType;
	private List<CompliancePlanSelfAssessmentPic> cpsaPics;
	
	private String lockFlag;
	private String userNikLock;

	public Long getCpsaId() {
		return cpsaId;
	}

	public void setCpsaId(Long cpsaId) {
		this.cpsaId = cpsaId;
	}

	public ParameterDetail getCpsaType() {
		return cpsaType;
	}

	public void setCpsaType(ParameterDetail cpsaType) {
		this.cpsaType = cpsaType;
	}

	public String getCpsaName() {
		return cpsaName;
	}

	public void setCpsaName(String cpsaName) {
		this.cpsaName = cpsaName;
	}

	public Date getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}

	public Date getPeriodFrom() {
		return periodFrom;
	}

	public void setPeriodFrom(Date periodFrom) {
		this.periodFrom = periodFrom;
	}

	public Date getPeriodTo() {
		return periodTo;
	}

	public void setPeriodTo(Date periodTo) {
		this.periodTo = periodTo;
	}

	public String getAttachmentFile() {
		return attachmentFile;
	}

	public void setAttachmentFile(String attachmentFile) {
		this.attachmentFile = attachmentFile;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public List<CompliancePlanSelfAssessmentPic> getCpsaPics() {
		return cpsaPics;
	}

	public void setCpsaPics(List<CompliancePlanSelfAssessmentPic> cpsaPics) {
		this.cpsaPics = cpsaPics;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	

	public Date getTargetDate() {
		return targetDate;
	}

	public void setTargetDate(Date targetDate) {
		this.targetDate = targetDate;
	}

	public Date getLetterDate() {
		return letterDate;
	}

	public void setLetterDate(Date letterDate) {
		this.letterDate = letterDate;
	}

	public Long getFileQuestSize() {
		return fileQuestSize;
	}

	public void setFileQuestSize(Long fileQuestSize) {
		this.fileQuestSize = fileQuestSize;
	}

	public String getLetterNo() {
		return letterNo;
	}

	public void setLetterNo(String letterNo) {
		this.letterNo = letterNo;
	}

	public String getLetterAbout() {
		return letterAbout;
	}

	public void setLetterAbout(String letterAbout) {
		this.letterAbout = letterAbout;
	}

	public String getAttachmentQuestFile() {
		return attachmentQuestFile;
	}

	public void setAttachmentQuestFile(String attachmentQuestFile) {
		this.attachmentQuestFile = attachmentQuestFile;
	}

	public String getFileQuestId() {
		return fileQuestId;
	}

	public void setFileQuestId(String fileQuestId) {
		this.fileQuestId = fileQuestId;
	}

	public CounterType getCounterType() {
		return counterType;
	}

	public void setCounterType(CounterType counterType) {
		this.counterType = counterType;
	}

	public String getFilePathDownload() {
		return filePathDownload;
	}

	public void setFilePathDownload(String filePathDownload) {
		this.filePathDownload = filePathDownload;
	}

	public String getStatusDownload() {
		return statusDownload;
	}

	public void setStatusDownload(String statusDownload) {
		this.statusDownload = statusDownload;
	}

	public String getCpsaStatus() {
		return cpsaStatus;
	}

	public void setCpsaStatus(String cpsaStatus) {
		this.cpsaStatus = cpsaStatus;
	}

	public String getCpsaBranchSubBranch() {
		return cpsaBranchSubBranch;
	}

	public void setCpsaBranchSubBranch(String cpsaBranchSubBranch) {
		this.cpsaBranchSubBranch = cpsaBranchSubBranch;
	}

	public String getLockFlag() {
		return lockFlag;
	}

	public void setLockFlag(String lockFlag) {
		this.lockFlag = lockFlag;
	}

	public String getUserNikLock() {
		return userNikLock;
	}

	public void setUserNikLock(String userNikLock) {
		this.userNikLock = userNikLock;
	}

	
}
