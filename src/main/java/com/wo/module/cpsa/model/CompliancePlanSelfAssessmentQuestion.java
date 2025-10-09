package com.wo.module.cpsa.model;

import java.io.Serializable;

import com.wo.module.common.model.BaseEntity;

public class CompliancePlanSelfAssessmentQuestion extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 7022999818104519095L;

	private Long cpsaQuestionId;
	private Long cpsaParentQuestion;

	private String cpsaQuestionNo;
	private String cpsaQuestion;

	private CompliancePlanSelfAssessment cpsa;

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

	public CompliancePlanSelfAssessment getCpsa() {
		return cpsa;
	}

	public void setCpsa(CompliancePlanSelfAssessment cpsa) {
		this.cpsa = cpsa;
	}

}
