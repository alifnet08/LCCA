package com.wo.module.litigation.model;

import java.io.Serializable;
import java.util.Date;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.rc.model.RC;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LitigationPailitPkpu extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 6395152164269025885L;

	private Long litigationId;
	private LitigationNew litigation;
	
	private int caseYear;
	private String caseNumber;
	private RC countryCourt;
	private ParameterDetail commercialCourt;
	
	private Date hearingDate;
	private ParameterDetail judgementWarning;
	
	private Double invoiceValuePkpu;
	private Double invoiceValuePailit;
	
	private String keterangan;
	private String attorney;
	private ParameterDetail caseHandler;
	private String attorneyOfficeName;

	//helper
	private Long selectCountryCourtId;
	private String selectCommercialCourt;
	private String selectJudgementWarning;
	private String selectCaseHandler;

	public Long getLitigationId() {
		return litigationId;
	}

	public void setLitigationId(Long litigationId) {
		this.litigationId = litigationId;
	}

	public LitigationNew getLitigation() {
		return litigation;
	}

	public void setLitigation(LitigationNew litigation) {
		this.litigation = litigation;
	}

	public int getCaseYear() {
		return caseYear;
	}

	public void setCaseYear(int caseYear) {
		this.caseYear = caseYear;
	}

	public String getCaseNumber() {
		return caseNumber;
	}

	public void setCaseNumber(String caseNumber) {
		this.caseNumber = caseNumber;
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

	public String getKeterangan() {
		return keterangan;
	}

	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}

	public String getAttorney() {
		return attorney;
	}

	public void setAttorney(String attorney) {
		this.attorney = attorney;
	}
	
	public ParameterDetail getCaseHandler() {
		return caseHandler;
	}

	public void setCaseHandler(ParameterDetail caseHandler) {
		this.caseHandler = caseHandler;
	}

	//helper
	public String getSelectJudgementWarning() {
		return selectJudgementWarning;
	}

	public void setSelectJudgementWarning(String selectJudgementWarning) {
		this.selectJudgementWarning = selectJudgementWarning;
	}

	public String getSelectCaseHandler() {
		return selectCaseHandler;
	}

	public void setSelectCaseHandler(String selectCaseHandler) {
		this.selectCaseHandler = selectCaseHandler;
	}
}
