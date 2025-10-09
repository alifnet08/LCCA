package com.wo.module.qaFE.vo;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

public class QAFEAnswerVo implements Serializable{

	private static final long serialVersionUID = -8165661608498830524L;
	
	private String answer;
	private Timestamp answerDate;
	private String answerDateStr;
	private Long answerId;
	private String answerBy;
	private String answerDivisionName;
	
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public Date getAnswerDate() {
		return answerDate;
	}
	public void setAnswerDate(Timestamp answerDate) {
		this.answerDate = answerDate;
	}
	public String getAnswerDateStr() {
		return answerDateStr;
	}
	public void setAnswerDateStr(String answerDateStr) {
		this.answerDateStr = answerDateStr;
	}
	public Long getAnswerId() {
		return answerId;
	}
	public void setAnswerId(Long answerId) {
		this.answerId = answerId;
	}
	public String getAnswerBy() {
		return answerBy;
	}
	public void setAnswerBy(String answerBy) {
		this.answerBy = answerBy;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getAnswerDivisionName() {
		return answerDivisionName;
	}
	public void setAnswerDivisionName(String answerDivisionName) {
		this.answerDivisionName = answerDivisionName;
	}
}
