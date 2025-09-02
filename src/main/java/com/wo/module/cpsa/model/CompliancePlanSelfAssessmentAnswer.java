package com.wo.module.cpsa.model;

import java.io.Serializable;
import java.util.Date;

import com.wo.module.common.model.BaseEntity;

public class CompliancePlanSelfAssessmentAnswer extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -275342237373439497L;

	private Long cpsaAnswerId;
	private Long cpsaParentQuestion;

	private String cpsaAnswer;
	private String cpsaNote;

	private Date cpsaAnswerDate;

	private CompliancePlanSelfAssessmentPic cpsaPic;
	private CompliancePlanSelfAssessmentQuestion cpsaQuestion;

	public Long getCpsaAnswerId() {
		return cpsaAnswerId;
	}

	public void setCpsaAnswerId(Long cpsaAnswerId) {
		this.cpsaAnswerId = cpsaAnswerId;
	}

	public Long getCpsaParentQuestion() {
		return cpsaParentQuestion;
	}

	public void setCpsaParentQuestion(Long cpsaParentQuestion) {
		this.cpsaParentQuestion = cpsaParentQuestion;
	}

	public String getCpsaAnswer() {
		return cpsaAnswer;
	}

	public void setCpsaAnswer(String cpsaAnswer) {
		this.cpsaAnswer = cpsaAnswer;
	}

	public String getCpsaNote() {
		return cpsaNote;
	}

	public void setCpsaNote(String cpsaNote) {
		this.cpsaNote = cpsaNote;
	}

	public Date getCpsaAnswerDate() {
		return cpsaAnswerDate;
	}

	public void setCpsaAnswerDate(Date cpsaAnswerDate) {
		this.cpsaAnswerDate = cpsaAnswerDate;
	}

	public CompliancePlanSelfAssessmentPic getCpsaPic() {
		return cpsaPic;
	}

	public void setCpsaPic(CompliancePlanSelfAssessmentPic cpsaPic) {
		this.cpsaPic = cpsaPic;
	}

	public CompliancePlanSelfAssessmentQuestion getCpsaQuestion() {
		return cpsaQuestion;
	}

	public void setCpsaQuestion(CompliancePlanSelfAssessmentQuestion cpsaQuestion) {
		this.cpsaQuestion = cpsaQuestion;
	}
}