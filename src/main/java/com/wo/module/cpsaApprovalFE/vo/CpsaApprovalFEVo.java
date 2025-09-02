package com.wo.module.cpsaApprovalFE.vo;

import java.io.Serializable;
import java.util.Date;

public class CpsaApprovalFEVo implements Serializable {

	private static final long serialVersionUID = -3027918868920738406L;
	
	private Integer rowIndex;
	private Integer totalQuestion;
	private Integer totalAnswer;
	
	private Long cpsaId;
	private Long cpsaQuestId;
	private Long divisionId;
	private Long userId1;
	private Long userId2;
	private Long userId3;
	private Long totalDataNotAnswer;
	
	private Date uploadDate;
	private Date letterDate;
	private Date periodStartDate;
	private Date periodEndDate;

	private String cpsaType;
	private String cpsaTypeCode;
	private String cpsaTypeIn;
	private String cpsaTypeEn;
	private String cpsaName;
	private String uploadDateStr;
	private String letterNo;
	private String letterDateStr;
	private String letterAbout;
	private String periodStartDateStr;
	private String periodEndDateStr;
	private String yearStr;
	private String divisionName;
	private String directorate;
	private String cpsaStatus;
	private String cpsaStatusName;
	private String statusPic;
	private String statusPicName;
	private String note;
	private String userNik1;
	private String userName1;
	private String cpsaNote;
	private String branchCode;
	private String branchName;
	private String headerExcelCpsa;
	
	private boolean flagStatus;

	public Long getCpsaId() {
		return cpsaId;
	}

	public void setCpsaId(Long cpsaId) {
		this.cpsaId = cpsaId;
	}

	public Long getDivisionId() {
		return divisionId;
	}

	public void setDivisionId(Long divisionId) {
		this.divisionId = divisionId;
	}

	public Date getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}

	public Date getLetterDate() {
		return letterDate;
	}

	public void setLetterDate(Date letterDate) {
		this.letterDate = letterDate;
	}

	public Date getPeriodStartDate() {
		return periodStartDate;
	}

	public void setPeriodStartDate(Date periodStartDate) {
		this.periodStartDate = periodStartDate;
	}

	public Date getPeriodEndDate() {
		return periodEndDate;
	}

	public void setPeriodEndDate(Date periodEndDate) {
		this.periodEndDate = periodEndDate;
	}

	public String getCpsaType() {
		return cpsaType;
	}

	public void setCpsaType(String cpsaType) {
		this.cpsaType = cpsaType;
	}

	public String getCpsaTypeCode() {
		return cpsaTypeCode;
	}

	public void setCpsaTypeCode(String cpsaTypeCode) {
		this.cpsaTypeCode = cpsaTypeCode;
	}

	public String getCpsaTypeIn() {
		return cpsaTypeIn;
	}

	public void setCpsaTypeIn(String cpsaTypeIn) {
		this.cpsaTypeIn = cpsaTypeIn;
	}

	public String getCpsaTypeEn() {
		return cpsaTypeEn;
	}

	public void setCpsaTypeEn(String cpsaTypeEn) {
		this.cpsaTypeEn = cpsaTypeEn;
	}

	public String getCpsaName() {
		return cpsaName;
	}

	public void setCpsaName(String cpsaName) {
		this.cpsaName = cpsaName;
	}

	public String getUploadDateStr() {
		return uploadDateStr;
	}

	public void setUploadDateStr(String uploadDateStr) {
		this.uploadDateStr = uploadDateStr;
	}

	public String getLetterNo() {
		return letterNo;
	}

	public void setLetterNo(String letterNo) {
		this.letterNo = letterNo;
	}

	public String getLetterDateStr() {
		return letterDateStr;
	}

	public void setLetterDateStr(String letterDateStr) {
		this.letterDateStr = letterDateStr;
	}

	public String getLetterAbout() {
		return letterAbout;
	}

	public void setLetterAbout(String letterAbout) {
		this.letterAbout = letterAbout;
	}

	public String getYearStr() {
		return yearStr;
	}

	public void setYearStr(String yearStr) {
		this.yearStr = yearStr;
	}

	public String getPeriodStartDateStr() {
		return periodStartDateStr;
	}

	public void setPeriodStartDateStr(String periodStartDateStr) {
		this.periodStartDateStr = periodStartDateStr;
	}

	public String getPeriodEndDateStr() {
		return periodEndDateStr;
	}

	public void setPeriodEndDateStr(String periodEndDateStr) {
		this.periodEndDateStr = periodEndDateStr;
	}

	public String getDivisionName() {
		return divisionName;
	}

	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}

	public Integer getRowIndex() {
		return rowIndex;
	}

	public void setRowIndex(Integer rowIndex) {
		this.rowIndex = rowIndex;
	}

	public String getDirectorate() {
		return directorate;
	}

	public void setDirectorate(String directorate) {
		this.directorate = directorate;
	}

	public Long getTotalDataNotAnswer() {
		return totalDataNotAnswer;
	}

	public void setTotalDataNotAnswer(Long totalDataNotAnswer) {
		this.totalDataNotAnswer = totalDataNotAnswer;
	}

	public Long getCpsaQuestId() {
		return cpsaQuestId;
	}

	public void setCpsaQuestId(Long cpsaQuestId) {
		this.cpsaQuestId = cpsaQuestId;
	}

	public Long getUserId1() {
		return userId1;
	}

	public void setUserId1(Long userId1) {
		this.userId1 = userId1;
	}

	public Long getUserId2() {
		return userId2;
	}

	public void setUserId2(Long userId2) {
		this.userId2 = userId2;
	}

	public Long getUserId3() {
		return userId3;
	}

	public void setUserId3(Long userId3) {
		this.userId3 = userId3;
	}

	public String getCpsaStatus() {
		return cpsaStatus;
	}

	public void setCpsaStatus(String cpsaStatus) {
		this.cpsaStatus = cpsaStatus;
	}

	public String getCpsaStatusName() {
		return cpsaStatusName;
	}

	public void setCpsaStatusName(String cpsaStatusName) {
		this.cpsaStatusName = cpsaStatusName;
	}

	public Integer getTotalQuestion() {
		return totalQuestion;
	}

	public void setTotalQuestion(Integer totalQuestion) {
		this.totalQuestion = totalQuestion;
	}

	public Integer getTotalAnswer() {
		return totalAnswer;
	}

	public void setTotalAnswer(Integer totalAnswer) {
		this.totalAnswer = totalAnswer;
	}

	public String getStatusPic() {
		return statusPic;
	}

	public void setStatusPic(String statusPic) {
		this.statusPic = statusPic;
	}

	public String getStatusPicName() {
		return statusPicName;
	}

	public void setStatusPicName(String statusPicName) {
		this.statusPicName = statusPicName;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public boolean isFlagStatus() {
		return flagStatus;
	}

	public void setFlagStatus(boolean flagStatus) {
		this.flagStatus = flagStatus;
	}

	public String getUserNik1() {
		return userNik1;
	}

	public void setUserNik1(String userNik1) {
		this.userNik1 = userNik1;
	}

	public String getUserName1() {
		return userName1;
	}

	public void setUserName1(String userName1) {
		this.userName1 = userName1;
	}

	public String getCpsaNote() {
		return cpsaNote;
	}

	public void setCpsaNote(String cpsaNote) {
		this.cpsaNote = cpsaNote;
	}

	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getHeaderExcelCpsa() {
		return headerExcelCpsa;
	}

	public void setHeaderExcelCpsa(String headerExcelCpsa) {
		this.headerExcelCpsa = headerExcelCpsa;
	}
	
	

}
