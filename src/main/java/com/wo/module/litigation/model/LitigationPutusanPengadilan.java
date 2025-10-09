package com.wo.module.litigation.model;

import java.io.Serializable;
import java.util.Date;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.parameter.model.ParameterDetail;

public class LitigationPutusanPengadilan extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 6395152164269025885L;

	private Long litiPutusanPengadilanId;
	private LitigationNew litigation;
	private Date hearingDate;
	private ParameterDetail judgementWarning;
	
	//helper
	private String selectJudgementWarning;
	
	private Integer sequence;
	private Boolean isEditable;
	
	
	public Long getLitiPutusanPengadilanId() {
		return litiPutusanPengadilanId;
	}
	public void setLitiPutusanPengadilanId(Long litiPutusanPengadilanId) {
		this.litiPutusanPengadilanId = litiPutusanPengadilanId;
	}
	public LitigationNew getLitigation() {
		return litigation;
	}
	public void setLitigation(LitigationNew litigation) {
		this.litigation = litigation;
	}
	public Date getHearingDate() {
		return hearingDate;
	}
	public void setHearingDate(Date hearingDate) {
		this.hearingDate = hearingDate;
	}
	public ParameterDetail getJudgementWarning() {
		return judgementWarning;
	}
	public void setJudgementWarning(ParameterDetail judgementWarning) {
		this.judgementWarning = judgementWarning;
	}
	public String getSelectJudgementWarning() {
		return selectJudgementWarning;
	}
	public void setSelectJudgementWarning(String selectJudgementWarning) {
		this.selectJudgementWarning = selectJudgementWarning;
	}
	public Integer getSequence() {
		return sequence;
	}
	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}
	public Boolean getIsEditable() {
		return isEditable;
	}
	public void setIsEditable(Boolean isEditable) {
		this.isEditable = isEditable;
	}

	
	
}
