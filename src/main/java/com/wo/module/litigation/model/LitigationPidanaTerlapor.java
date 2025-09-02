package com.wo.module.litigation.model;

import java.io.Serializable;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.parameter.model.ParameterDetail;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LitigationPidanaTerlapor extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 6395152164269025885L;

	private Long litigationId;
	private LitigationNew litigation;
	
	private int caseYear;
	private String caseNumber;
	private String tindakPidana;
	private String examiningAgency;
	
	private ParameterDetail caseHandler;
	private String attorneyOfficeName;
	
	private String casePosition;
	private String caseDevelopment;
	private Double caseValueIdr;
	private ParameterDetail foreignCurrencyType;
	private String caseValueValas;
	
	private String inspectionLevelPol;
	private String inspectionLevelPn;
	private String inspectionLevelJaksa;
	private String inspectionLevelFinished;
	private String attorney;
	
	//helper
	private String selectCaseHandler;
	private String selectForeignCurrency;
	
	private Boolean inspectionLevelPolFlag;
	private Boolean inspectionLevelPnFlag;
	private Boolean inspectionLevelJaksaFlag;
	private Boolean inspectionLevelFinishedFlag;
	
	
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
	public String getExaminingAgency() {
		return examiningAgency;
	}
	public void setExaminingAgency(String examiningAgency) {
		this.examiningAgency = examiningAgency;
	}
	public String getCasePosition() {
		return casePosition;
	}
	public void setCasePosition(String casePosition) {
		this.casePosition = casePosition;
	}
	public String getCaseDevelopment() {
		return caseDevelopment;
	}
	public void setCaseDevelopment(String caseDevelopment) {
		this.caseDevelopment = caseDevelopment;
	}
	public String getCaseValueValas() {
		return caseValueValas;
	}
	public void setCaseValueValas(String caseValueValas) {
		this.caseValueValas = caseValueValas;
	}
	public String getInspectionLevelPol() {
		return inspectionLevelPol;
	}
	public void setInspectionLevelPol(String inspectionLevelPol) {
		this.inspectionLevelPol = inspectionLevelPol;
	}
	public String getInspectionLevelPn() {
		return inspectionLevelPn;
	}
	public void setInspectionLevelPn(String inspectionLevelPn) {
		this.inspectionLevelPn = inspectionLevelPn;
	}
	public String getInspectionLevelFinished() {
		return inspectionLevelFinished;
	}
	public void setInspectionLevelFinished(String inspectionLevelFinished) {
		this.inspectionLevelFinished = inspectionLevelFinished;
	}
	public String getAttorney() {
		return attorney;
	}
	public void setAttorney(String attorney) {
		this.attorney = attorney;
	}
	
	
	//helper
	public Boolean getInspectionLevelPolFlag() {
		return inspectionLevelPolFlag;
	}
	public void setInspectionLevelPolFlag(Boolean inspectionLevelPolFlag) {
		this.inspectionLevelPolFlag = inspectionLevelPolFlag;
	}
	public Boolean getInspectionLevelPnFlag() {
		return inspectionLevelPnFlag;
	}
	public void setInspectionLevelPnFlag(Boolean inspectionLevelPnFlag) {
		this.inspectionLevelPnFlag = inspectionLevelPnFlag;
	}
	public Boolean getInspectionLevelFinishedFlag() {
		return inspectionLevelFinishedFlag;
	}
	public void setInspectionLevelFinishedFlag(Boolean inspectionLevelFinishedFlag) {
		this.inspectionLevelFinishedFlag = inspectionLevelFinishedFlag;
	}
	
}
