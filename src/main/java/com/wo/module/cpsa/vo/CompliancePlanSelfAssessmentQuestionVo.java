package com.wo.module.cpsa.vo;

import java.io.Serializable;
import java.util.Date;

public class CompliancePlanSelfAssessmentQuestionVo implements Serializable {

	private static final long serialVersionUID = 8517845631165211013L;

	private Long cpsaId;
	private Long cpsaQuestionId;
	private Long cpsaParentQuestion;
	private Long divisionId;
	private Long userId1;
	private Long userId2;
	private Long userId3;
	private Long cpsaPicId;
	
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
	private String cpsaAnswer;
	private String cpsaAnswerName;
	private String cpsaNote;
	private String flagHeader;
	
	public CompliancePlanSelfAssessmentQuestionVo() {

	}

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

	public Long getCpsaPicId() {
		return cpsaPicId;
	}

	public void setCpsaPicId(Long cpsaPicId) {
		this.cpsaPicId = cpsaPicId;
	}

	public String getCpsaAnswer() {
		return cpsaAnswer;
	}

	public void setCpsaAnswer(String cpsaAnswer) {
		this.cpsaAnswer = cpsaAnswer;
	}

	public String getCpsaAnswerName() {
		return cpsaAnswerName;
	}

	public void setCpsaAnswerName(String cpsaAnswerName) {
		this.cpsaAnswerName = cpsaAnswerName;
	}

	public String getCpsaNote() {
		return cpsaNote;
	}

	public void setCpsaNote(String cpsaNote) {
		this.cpsaNote = cpsaNote;
	}

	public String getFlagHeader() {
		return flagHeader;
	}

	public void setFlagHeader(String flagHeader) {
		this.flagHeader = flagHeader;
	}

	public Integer getRowIndex() {
		return rowIndex;
	}

	public void setRowIndex(Integer rowIndex) {
		this.rowIndex = rowIndex;
	}
	
}