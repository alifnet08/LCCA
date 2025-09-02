package com.wo.module.cpsaFE.vo;

import java.io.Serializable;

public class CompliancePlanSelfAssessmentQuestionFEVo implements Serializable {

	private static final long serialVersionUID = -5753378304715625060L;
	
	private Integer rowIndex;
	
	private Long cpsaId;
	private Long cpsaPicId;
	private Long cpsaQuestId;
	private Long cpsaParentQuestId;
	
	private String cpsaQuestNo;
	private String cpsaQuestion;
	private String cpsaAnswer;
	private String cpsaAnswerName;
	private String cpsaNote;
	private String flagHeader;
	private Boolean isCanEdit;

	public CompliancePlanSelfAssessmentQuestionFEVo() {}

	public Long getCpsaId() {
		return cpsaId;
	}

	public void setCpsaId(Long cpsaId) {
		this.cpsaId = cpsaId;
	}

	public Long getCpsaQuestId() {
		return cpsaQuestId;
	}

	public void setCpsaQuestId(Long cpsaQuestId) {
		this.cpsaQuestId = cpsaQuestId;
	}

	public String getCpsaQuestNo() {
		return cpsaQuestNo;
	}

	public void setCpsaQuestNo(String cpsaQuestNo) {
		this.cpsaQuestNo = cpsaQuestNo;
	}

	public String getCpsaQuestion() {
		return cpsaQuestion;
	}

	public void setCpsaQuestion(String cpsaQuestion) {
		this.cpsaQuestion = cpsaQuestion;
	}

	public String getFlagHeader() {
		return flagHeader;
	}

	public void setFlagHeader(String flagHeader) {
		this.flagHeader = flagHeader;
	}

	public Long getCpsaParentQuestId() {
		return cpsaParentQuestId;
	}

	public void setCpsaParentQuestId(Long cpsaParentQuestId) {
		this.cpsaParentQuestId = cpsaParentQuestId;
	}

	public Integer getRowIndex() {
		return rowIndex;
	}

	public void setRowIndex(Integer rowIndex) {
		this.rowIndex = rowIndex;
	}

	public Long getCpsaPicId() {
		return cpsaPicId;
	}

	public void setCpsaPicId(Long cpsaPicId) {
		this.cpsaPicId = cpsaPicId;
	}

	public String getCpsaNote() {
		return cpsaNote;
	}

	public void setCpsaNote(String cpsaNote) {
		this.cpsaNote = cpsaNote;
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

	public Boolean getIsCanEdit() {
		return isCanEdit;
	}

	public void setIsCanEdit(Boolean isCanEdit) {
		this.isCanEdit = isCanEdit;
	}

}
