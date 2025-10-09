package com.wo.module.litigation.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.user.model.User;

public class LitigationPerdataTergugat extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 6395152164269025885L;

	private Long litigationId;
	private LitigationNew litigation;
	private String debitur;
	private int caseYear;
	private String caseNumber;
	private ParameterDetail courtType;
	private String courtDomicile;
	private String kuasaHukumPenggugat;
	private String kuasaHukumTergugat;
	private ParameterDetail caseHandler;
	private String attorneyOfficeName;
	private Date hearingDate;
	private ParameterDetail caseMainTopic;
	private ParameterDetail caseSubTopic;
	private String caseClaimsPosition;
	private String maybankLegalPosition;
	private String plaintiffDemand;
	private Double claimsValueMaterialIdr;
	private Double claimsValueImmaterialIdr;
	private Double totalClaimsValueIdr;
	private ParameterDetail foreignCurrencyType;
	private Double claimsValueMaterialValas;
	private String claimsValueMaterialValasStr;
	private Double potentialLossCessieValue;
	private Double potentialLossOthers;
	private String courtDecision;
	private String lastProgress;
	private ParameterDetail decisionStatus;
	private Date finishDate;
	
	private LitigationPerdataInspectLevel litigationPerdataInspectLevel;
	
	//helpers
	private String selectDecisionStatus;
	private String selectCaseHandler;
	private String selectCourtType;
	private String selectCaseMainTopic;
	private String selectCaseSubTopic;
	private String selectForeignCurrency;

	public Long getLitigationId() {
		return litigationId;
	}

	public void setLitigationId(Long litigationId) {
		this.litigationId = litigationId;
	}

	public String getDebitur() {
		return debitur;
	}

	public void setDebitur(String debitur) {
		this.debitur = debitur;
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

	public ParameterDetail getCourtType() {
		return courtType;
	}

	public void setCourtType(ParameterDetail courtType) {
		this.courtType = courtType;
	}

	public String getCourtDomicile() {
		return courtDomicile;
	}

	public void setCourtDomicile(String courtDomicile) {
		this.courtDomicile = courtDomicile;
	}

	public String getKuasaHukumPenggugat() {
		return kuasaHukumPenggugat;
	}

	public void setKuasaHukumPenggugat(String kuasaHukumPenggugat) {
		this.kuasaHukumPenggugat = kuasaHukumPenggugat;
	}

	public String getKuasaHukumTergugat() {
		return kuasaHukumTergugat;
	}

	public void setKuasaHukumTergugat(String kuasaHukumTergugat) {
		this.kuasaHukumTergugat = kuasaHukumTergugat;
	}

	public ParameterDetail getCaseHandler() {
		return caseHandler;
	}

	public void setCaseHandler(ParameterDetail caseHandler) {
		this.caseHandler = caseHandler;
	}

	public String getAttorneyOfficeName() {
		return attorneyOfficeName;
	}

	public void setAttorneyOfficeName(String attorneyOfficeName) {
		this.attorneyOfficeName = attorneyOfficeName;
	}

	public Date getHearingDate() {
		return hearingDate;
	}

	public void setHearingDate(Date hearingDate) {
		this.hearingDate = hearingDate;
	}

	public ParameterDetail getCaseMainTopic() {
		return caseMainTopic;
	}

	public void setCaseMainTopic(ParameterDetail caseMainTopic) {
		this.caseMainTopic = caseMainTopic;
	}

	public ParameterDetail getCaseSubTopic() {
		return caseSubTopic;
	}

	public void setCaseSubTopic(ParameterDetail caseSubTopic) {
		this.caseSubTopic = caseSubTopic;
	}

	public String getCaseClaimsPosition() {
		return caseClaimsPosition;
	}

	public void setCaseClaimsPosition(String caseClaimsPosition) {
		this.caseClaimsPosition = caseClaimsPosition;
	}

	public String getMaybankLegalPosition() {
		return maybankLegalPosition;
	}

	public void setMaybankLegalPosition(String maybankLegalPosition) {
		this.maybankLegalPosition = maybankLegalPosition;
	}

	public String getPlaintiffDemand() {
		return plaintiffDemand;
	}

	public void setPlaintiffDemand(String plaintiffDemand) {
		this.plaintiffDemand = plaintiffDemand;
	}

	public Double getClaimsValueMaterialIdr() {
		return claimsValueMaterialIdr;
	}

	public void setClaimsValueMaterialIdr(Double claimsValueMaterialIdr) {
		this.claimsValueMaterialIdr = claimsValueMaterialIdr;
	}

	public Double getClaimsValueImmaterialIdr() {
		return claimsValueImmaterialIdr;
	}

	public void setClaimsValueImmaterialIdr(Double claimsValueImmaterialIdr) {
		this.claimsValueImmaterialIdr = claimsValueImmaterialIdr;
	}

	public Double getClaimsValueMaterialValas() {
		return claimsValueMaterialValas;
	}

	public void setClaimsValueMaterialValas(Double claimsValueMaterialValas) {
		this.claimsValueMaterialValas = claimsValueMaterialValas;
	}

	public void setPotentialLossCessieValue(Double potentialLossCessieValue) {
		this.potentialLossCessieValue = potentialLossCessieValue;
	}

	public void setPotentialLossOthers(Double potentialLossOthers) {
		this.potentialLossOthers = potentialLossOthers;
	}

	public Double getTotalClaimsValueIdr() {
		return totalClaimsValueIdr;
	}

	public void setTotalClaimsValueIdr(Double totalClaimsValueIdr) {
		this.totalClaimsValueIdr = totalClaimsValueIdr;
	}

	public String getClaimsValueMaterialValasStr() {
		return claimsValueMaterialValasStr;
	}

	public void setClaimsValueMaterialValasStr(String claimsValueMaterialValasStr) {
		this.claimsValueMaterialValasStr = claimsValueMaterialValasStr;
	}

	public ParameterDetail getForeignCurrencyType() {
		return foreignCurrencyType;
	}

	public void setForeignCurrencyType(ParameterDetail foreignCurrencyType) {
		this.foreignCurrencyType = foreignCurrencyType;
	}

	public String getCourtDecision() {
		return courtDecision;
	}

	public void setCourtDecision(String courtDecision) {
		this.courtDecision = courtDecision;
	}

	public String getLastProgress() {
		return lastProgress;
	}

	public void setLastProgress(String lastProgress) {
		this.lastProgress = lastProgress;
	}

	public ParameterDetail getDecisionStatus() {
		return decisionStatus;
	}

	public void setDecisionStatus(ParameterDetail decisionStatus) {
		this.decisionStatus = decisionStatus;
	}

	public Date getFinishDate() {
		return finishDate;
	}

	public void setFinishDate(Date finishDate) {
		this.finishDate = finishDate;
	}

	public LitigationPerdataInspectLevel getLitigationPerdataInspectLevel() {
		return litigationPerdataInspectLevel;
	}

	public void setLitigationPerdataInspectLevel(LitigationPerdataInspectLevel litigationPerdataInspectLevel) {
		this.litigationPerdataInspectLevel = litigationPerdataInspectLevel;
	}

	public LitigationNew getLitigation() {
		return litigation;
	}

	public void setLitigation(LitigationNew litigation) {
		this.litigation = litigation;
	}
	
	//helpers getter setter
	public String getSelectCaseHandler() {
		return selectCaseHandler;
	}

	public String getSelectDecisionStatus() {
		return selectDecisionStatus;
	}

	public void setSelectDecisionStatus(String selectDecisionStatus) {
		this.selectDecisionStatus = selectDecisionStatus;
	}

	public void setSelectCaseHandler(String selectCaseHandler) {
		this.selectCaseHandler = selectCaseHandler;
	}

	public String getSelectCourtType() {
		return selectCourtType;
	}

	public void setSelectCourtType(String selectCourtType) {
		this.selectCourtType = selectCourtType;
	}

	public String getSelectCaseMainTopic() {
		return selectCaseMainTopic;
	}

	public void setSelectCaseMainTopic(String selectCaseMainTopic) {
		this.selectCaseMainTopic = selectCaseMainTopic;
	}

	public String getSelectCaseSubTopic() {
		return selectCaseSubTopic;
	}

	public void setSelectCaseSubTopic(String selectCaseSubTopic) {
		this.selectCaseSubTopic = selectCaseSubTopic;
	}

	public String getSelectForeignCurrency() {
		return selectForeignCurrency;
	}

	public void setSelectForeignCurrency(String selectForeignCurrency) {
		this.selectForeignCurrency = selectForeignCurrency;
	}

	public Double getPotentialLossCessieValue() {
		return potentialLossCessieValue;
	}

	public Double getPotentialLossOthers() {
		return potentialLossOthers;
	}
	
	
}
