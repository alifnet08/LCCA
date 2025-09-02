package com.wo.module.litigation.model;

import java.io.Serializable;
import java.util.Date;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.parameter.model.ParameterDetail;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LitigationPerdataPenggugat extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 6395152164269025885L;

	private Long litigationId;
	private LitigationNew litigation;
	private int caseYear;
	private String caseNumber;
	private ParameterDetail courtType;
	private String courtDomicile;
	
	private String kuasaHukumPenggugat;
	private ParameterDetail caseHandler;
	private String attorneyOfficeName;
	
	private ParameterDetail caseMainTopic;
	private ParameterDetail caseSubTopic;
	private String plaintiffDemand;
	
	private Double claimsValueMaterialIdr;
	private Double claimsValueMaterialValas;
	private String claimsValueMaterialValasStr;
	private ParameterDetail foreignCurrencyType;
	private Double claimsValueImmaterialIdr;
	private Double totalClaimsValueIdr;
	
	private String courtDecision;
	
	private String caseOngoingPn;
	private String caseOngoingBani;
	private String caseOngoingPt;
	private String caseOngoingMaKasasi;
	private String caseOngoingMaPk;
	
	private String caseFinishedPn;
	private String caseFinishedBani;
	private String caseFinishedPt;
	private String caseFinishedMaKasasi;
	private String caseFinishedMaPk;
	
	private int totalOngoingCase;
	private int totalFinishedCase;
	
	private Date finishDate;
	
	//helpers
	private String selectCaseHandler;
	private String selectCourtType;
	private String selectForeignCurrency;
	private String selectCaseMainTopic;
	private String selectCaseSubTopic;
	
	private Boolean caseOngoingPnFlag;
	private Boolean caseOngoingBaniFlag;
	private Boolean caseOngoingPtFlag;
	private Boolean caseOngoingMaKasasiFlag;
	private Boolean caseOngoingMaPkFlag;
	
	private Boolean caseFinishedPnFlag;
	private Boolean caseFinishedBaniFlag;
	private Boolean caseFinishedPtFlag;
	private Boolean caseFinishedMaKasasiFlag;
	private Boolean caseFinishedMaPkFlag;

	public Long getLitigationId() {
		return litigationId;
	}

	public void setLitigationId(Long litigationId) {
		this.litigationId = litigationId;
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

	public String getPlaintiffDemand() {
		return plaintiffDemand;
	}

	public void setPlaintiffDemand(String plaintiffDemand) {
		this.plaintiffDemand = plaintiffDemand;
	}

	public String getCourtDecision() {
		return courtDecision;
	}

	public void setCourtDecision(String courtDecision) {
		this.courtDecision = courtDecision;
	}

	public Date getFinishDate() {
		return finishDate;
	}

	public void setFinishDate(Date finishDate) {
		this.finishDate = finishDate;
	}

	public LitigationNew getLitigation() {
		return litigation;
	}

	public void setLitigation(LitigationNew litigation) {
		this.litigation = litigation;
	}
	
	public String getCaseOngoingPn() {
		return caseOngoingPn;
	}

	public void setCaseOngoingPn(String caseOngoingPn) {
		this.caseOngoingPn = caseOngoingPn;
	}

	public String getCaseOngoingBani() {
		return caseOngoingBani;
	}

	public void setCaseOngoingBani(String caseOngoingBani) {
		this.caseOngoingBani = caseOngoingBani;
	}

	public String getCaseOngoingPt() {
		return caseOngoingPt;
	}

	public void setCaseOngoingPt(String caseOngoingPt) {
		this.caseOngoingPt = caseOngoingPt;
	}

	public String getCaseOngoingMaKasasi() {
		return caseOngoingMaKasasi;
	}

	public void setCaseOngoingMaKasasi(String caseOngoingMaKasasi) {
		this.caseOngoingMaKasasi = caseOngoingMaKasasi;
	}

	public String getCaseOngoingMaPk() {
		return caseOngoingMaPk;
	}

	public void setCaseOngoingMaPk(String caseOngoingMaPk) {
		this.caseOngoingMaPk = caseOngoingMaPk;
	}

	public String getCaseFinishedPn() {
		return caseFinishedPn;
	}

	public void setCaseFinishedPn(String caseFinishedPn) {
		this.caseFinishedPn = caseFinishedPn;
	}

	public String getCaseFinishedBani() {
		return caseFinishedBani;
	}

	public void setCaseFinishedBani(String caseFinishedBani) {
		this.caseFinishedBani = caseFinishedBani;
	}

	public String getCaseFinishedPt() {
		return caseFinishedPt;
	}

	public void setCaseFinishedPt(String caseFinishedPt) {
		this.caseFinishedPt = caseFinishedPt;
	}

	public String getCaseFinishedMaKasasi() {
		return caseFinishedMaKasasi;
	}

	public void setCaseFinishedMaKasasi(String caseFinishedMaKasasi) {
		this.caseFinishedMaKasasi = caseFinishedMaKasasi;
	}

	public String getCaseFinishedMaPk() {
		return caseFinishedMaPk;
	}

	public void setCaseFinishedMaPk(String caseFinishedMaPk) {
		this.caseFinishedMaPk = caseFinishedMaPk;
	}

	public int getTotalOngoingCase() {
		return totalOngoingCase;
	}

	public void setTotalOngoingCase(int totalOngoingCase) {
		this.totalOngoingCase = totalOngoingCase;
	}

	public int getTotalFinishedCase() {
		return totalFinishedCase;
	}

	public void setTotalFinishedCase(int totalFinishedCase) {
		this.totalFinishedCase = totalFinishedCase;
	}

	//helpers getter setter
	public String getSelectCaseHandler() {
		return selectCaseHandler;
	}

	public void setSelectCaseHandler(String selectCaseHandler) {
		this.selectCaseHandler = selectCaseHandler;
	}

	public Boolean getCaseOngoingPnFlag() {
		return caseOngoingPnFlag;
	}

	public void setCaseOngoingPnFlag(Boolean caseOngoingPnFlag) {
		this.caseOngoingPnFlag = caseOngoingPnFlag;
	}

	public Boolean getCaseOngoingBaniFlag() {
		return caseOngoingBaniFlag;
	}

	public void setCaseOngoingBaniFlag(Boolean caseOngoingBaniFlag) {
		this.caseOngoingBaniFlag = caseOngoingBaniFlag;
	}

	public Boolean getCaseOngoingPtFlag() {
		return caseOngoingPtFlag;
	}

	public void setCaseOngoingPtFlag(Boolean caseOngoingPtFlag) {
		this.caseOngoingPtFlag = caseOngoingPtFlag;
	}

	public Boolean getCaseOngoingMaKasasiFlag() {
		return caseOngoingMaKasasiFlag;
	}

	public void setCaseOngoingMaKasasiFlag(Boolean caseOngoingMaKasasiFlag) {
		this.caseOngoingMaKasasiFlag = caseOngoingMaKasasiFlag;
	}

	public Boolean getCaseOngoingMaPkFlag() {
		return caseOngoingMaPkFlag;
	}

	public void setCaseOngoingMaPkFlag(Boolean caseOngoingMaPkFlag) {
		this.caseOngoingMaPkFlag = caseOngoingMaPkFlag;
	}

	public Boolean getCaseFinishedPnFlag() {
		return caseFinishedPnFlag;
	}

	public void setCaseFinishedPnFlag(Boolean caseFinishedPnFlag) {
		this.caseFinishedPnFlag = caseFinishedPnFlag;
	}

	public Boolean getCaseFinishedBaniFlag() {
		return caseFinishedBaniFlag;
	}

	public void setCaseFinishedBaniFlag(Boolean caseFinishedBaniFlag) {
		this.caseFinishedBaniFlag = caseFinishedBaniFlag;
	}

	public Boolean getCaseFinishedPtFlag() {
		return caseFinishedPtFlag;
	}

	public void setCaseFinishedPtFlag(Boolean caseFinishedPtFlag) {
		this.caseFinishedPtFlag = caseFinishedPtFlag;
	}

	public Boolean getCaseFinishedMaKasasiFlag() {
		return caseFinishedMaKasasiFlag;
	}

	public void setCaseFinishedMaKasasiFlag(Boolean caseFinishedMaKasasiFlag) {
		this.caseFinishedMaKasasiFlag = caseFinishedMaKasasiFlag;
	}

	public Boolean getCaseFinishedMaPkFlag() {
		return caseFinishedMaPkFlag;
	}

	public void setCaseFinishedMaPkFlag(Boolean caseFinishedMaPkFlag) {
		this.caseFinishedMaPkFlag = caseFinishedMaPkFlag;
	}

	public String getSelectCourtType() {
		return selectCourtType;
	}

	public void setSelectCourtType(String selectCourtType) {
		this.selectCourtType = selectCourtType;
	}
	
	
	
	
}
