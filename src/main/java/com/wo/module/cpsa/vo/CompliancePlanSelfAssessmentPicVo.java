package com.wo.module.cpsa.vo;

import java.io.Serializable;
import java.util.Date;

public class CompliancePlanSelfAssessmentPicVo implements Serializable {

	private static final long serialVersionUID = 8734141169534362778L;
	
	private Long cpsaId;
	private Long cpsaQuestionId;
	private Long cpsaParentQuestion;
	private Long divisionId;
	private Long userId1;
	private Long userId2;
	private Long userId3;
	private Long cpsaPicId;
	private Long totalNotAnswer;
	private Long cpsaPicEmailId;
	private String branchCode;
	private String branchName;
	private String headerExcelCpsa;
	
	private Integer rowIndex;
	
	private Date periodStartDate;
	private Date periodEndDate;

	private String divisionName;
	private String cpsaQuestionNo;
	private String cpsaQuestion;
	private String periodStartDateStr;
	private String periodEndDateStr;
	private String yearStr;		
	private String cpsaStatus;
	private String cpsaStatusName;	
	private String userNik1;
	private String userName1;
	private String directorateName;
	private String letterAbout;
	
	public CompliancePlanSelfAssessmentPicVo() {}

	public Long getCpsaId() {
		return cpsaId;
	}

	public void setCpsaId(Long cpsaId) {
		this.cpsaId = cpsaId;
	}

	public Long getCpsaQuestionId() {
		return cpsaQuestionId;
	}

	public void setCpsaQuestionId(Long cpsaQuestionId) {
		this.cpsaQuestionId = cpsaQuestionId;
	}

	public Long getCpsaParentQuestion() {
		return cpsaParentQuestion;
	}

	public void setCpsaParentQuestion(Long cpsaParentQuestion) {
		this.cpsaParentQuestion = cpsaParentQuestion;
	}

	public Long getDivisionId() {
		return divisionId;
	}

	public void setDivisionId(Long divisionId) {
		this.divisionId = divisionId;
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

	public Long getCpsaPicId() {
		return cpsaPicId;
	}

	public void setCpsaPicId(Long cpsaPicId) {
		this.cpsaPicId = cpsaPicId;
	}

	public Integer getRowIndex() {
		return rowIndex;
	}

	public void setRowIndex(Integer rowIndex) {
		this.rowIndex = rowIndex;
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

	public String getDivisionName() {
		return divisionName;
	}

	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}

	public String getCpsaQuestionNo() {
		return cpsaQuestionNo;
	}

	public void setCpsaQuestionNo(String cpsaQuestionNo) {
		this.cpsaQuestionNo = cpsaQuestionNo;
	}

	public String getCpsaQuestion() {
		return cpsaQuestion;
	}

	public void setCpsaQuestion(String cpsaQuestion) {
		this.cpsaQuestion = cpsaQuestion;
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

	public String getYearStr() {
		return yearStr;
	}

	public void setYearStr(String yearStr) {
		this.yearStr = yearStr;
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

	public Long getTotalNotAnswer() {
		return totalNotAnswer;
	}

	public void setTotalNotAnswer(Long totalNotAnswer) {
		this.totalNotAnswer = totalNotAnswer;
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

	public String getDirectorateName() {
		return directorateName;
	}

	public void setDirectorateName(String directorateName) {
		this.directorateName = directorateName;
	}

	public Long getCpsaPicEmailId() {
		return cpsaPicEmailId;
	}

	public void setCpsaPicEmailId(Long cpsaPicEmailId) {
		this.cpsaPicEmailId = cpsaPicEmailId;
	}

	public String getLetterAbout() {
		return letterAbout;
	}

	public void setLetterAbout(String letterAbout) {
		this.letterAbout = letterAbout;
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