package com.wo.module.litigation.model;

import java.io.Serializable;
import java.util.Date;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.parameter.model.ParameterDetail;

public class LitigationDetail extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 6395152164269025885L;

	private Long litigationDtlId;
	private Litigation litigation;
	
	private ParameterDetail courtType;
	private ParameterDetail courtName;
	private Date courtDate;
//	private ParameterDetail progress;
	private ParameterDetail sentence;
	private String note;
	private String progress;
	private ParameterDetail legalEffort;
	
	private String sentenceStr;
	private int sequence;
	
	public Long getLitigationDtlId() {
		return litigationDtlId;
	}
	public void setLitigationDtlId(Long litigationDtlId) {
		this.litigationDtlId = litigationDtlId;
	}
	public Litigation getLitigation() {
		return litigation;
	}
	public void setLitigation(Litigation litigation) {
		this.litigation = litigation;
	}
	
	public Date getCourtDate() {
		return courtDate;
	}
	public void setCourtDate(Date courtDate) {
		this.courtDate = courtDate;
	}
	
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public int getSequence() {
		return sequence;
	}
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
	public ParameterDetail getCourtType() {
		return courtType;
	}
	public void setCourtType(ParameterDetail courtType) {
		this.courtType = courtType;
	}
	public ParameterDetail getCourtName() {
		return courtName;
	}
	public void setCourtName(ParameterDetail courtName) {
		this.courtName = courtName;
	}
//	public ParameterDetail getProgress() {
//		return progress;
//	}
//	public void setProgress(ParameterDetail progress) {
//		this.progress = progress;
//	}
	public ParameterDetail getSentence() {
		return sentence;
	}
	public void setSentence(ParameterDetail sentence) {
		this.sentence = sentence;
	}
	public String getSentenceStr() {
		return sentenceStr;
	}
	public void setSentenceStr(String sentenceStr) {
		this.sentenceStr = sentenceStr;
	}
	public String getProgress() {
		return progress;
	}
	public void setProgress(String progress) {
		this.progress = progress;
	}
	public ParameterDetail getLegalEffort() {
		return legalEffort;
	}
	public void setLegalEffort(ParameterDetail legalEffort) {
		this.legalEffort = legalEffort;
	}
}
