package com.wo.module.litigation.model;

import java.io.Serializable;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.parameter.model.ParameterDetail;

public class LitigationPerdataInspectLevel extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 6395152164269025885L;

	private Long litigationPerdataTergugatId;
	private LitigationPerdataTergugat litigationPerdata;
	
	private String caseOngoingPn;
	private String caseOngoingBani;
	private String caseOngoingPt;
	private String caseOngoingPtun;
	private String caseOngoingPa;
	private String caseOngoingPtAgama;
	private String caseOngoingMaKasasi;
	private String caseOngoingMaPk;
	
	private String caseFinishedPn;
	private String caseFinishedBani;
	private String caseFinishedPt;
	private String caseFinishedPa;
	private String caseFinishedPtAgama;
	private String caseFinishedPtun;
	private String caseFinishedMaKasasi;
	private String caseFinishedMaPk;
	
	private int totalOngoingCase;
	private int totalFinishedCase;
	
	//helper
	private Boolean caseOngoingPnFlag;
	private Boolean caseOngoingBaniFlag;
	private Boolean caseOngoingPtFlag;
	private Boolean caseOngoingPtunFlag;
	private Boolean caseOngoingPaFlag;
	private Boolean caseOngoingPtAgamaFlag;
	private Boolean caseOngoingMaKasasiFlag;
	private Boolean caseOngoingMaPkFlag;
	
	private Boolean caseFinishedPnFlag;
	private Boolean caseFinishedBaniFlag;
	private Boolean caseFinishedPtFlag;
	private Boolean caseFinishedPaFlag;
	private Boolean caseFinishedPtAgamaFlag;
	private Boolean caseFinishedPtunFlag;
	private Boolean caseFinishedMaKasasiFlag;
	private Boolean caseFinishedMaPkFlag;
	
	public Long getLitigationPerdataTergugatId() {
		return litigationPerdataTergugatId;
	}
	public void setLitigationPerdataTergugatId(Long litigationPerdataTergugatId) {
		this.litigationPerdataTergugatId = litigationPerdataTergugatId;
	}
	public LitigationPerdataTergugat getLitigationPerdata() {
		return litigationPerdata;
	}
	public void setLitigationPerdata(LitigationPerdataTergugat litigationPerdata) {
		this.litigationPerdata = litigationPerdata;
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
	public String getCaseOngoingPtun() {
		return caseOngoingPtun;
	}
	public void setCaseOngoingPtun(String caseOngoingPtun) {
		this.caseOngoingPtun = caseOngoingPtun;
	}
	public String getCaseOngoingPa() {
		return caseOngoingPa;
	}
	public void setCaseOngoingPa(String caseOngoingPa) {
		this.caseOngoingPa = caseOngoingPa;
	}
	public String getCaseOngoingPtAgama() {
		return caseOngoingPtAgama;
	}
	public void setCaseOngoingPtAgama(String caseOngoingPtAgama) {
		this.caseOngoingPtAgama = caseOngoingPtAgama;
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
	public String getCaseFinishedPa() {
		return caseFinishedPa;
	}
	public void setCaseFinishedPa(String caseFinishedPa) {
		this.caseFinishedPa = caseFinishedPa;
	}
	public String getCaseFinishedPtAgama() {
		return caseFinishedPtAgama;
	}
	public void setCaseFinishedPtAgama(String caseFinishedPtAgama) {
		this.caseFinishedPtAgama = caseFinishedPtAgama;
	}
	public String getCaseFinishedPtun() {
		return caseFinishedPtun;
	}
	public void setCaseFinishedPtun(String caseFinishedPtun) {
		this.caseFinishedPtun = caseFinishedPtun;
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
	public Boolean getCaseOngoingPtunFlag() {
		return caseOngoingPtunFlag;
	}
	public void setCaseOngoingPtunFlag(Boolean caseOngoingPtunFlag) {
		this.caseOngoingPtunFlag = caseOngoingPtunFlag;
	}
	public Boolean getCaseOngoingPaFlag() {
		return caseOngoingPaFlag;
	}
	public void setCaseOngoingPaFlag(Boolean caseOngoingPaFlag) {
		this.caseOngoingPaFlag = caseOngoingPaFlag;
	}
	public Boolean getCaseOngoingPtAgamaFlag() {
		return caseOngoingPtAgamaFlag;
	}
	public void setCaseOngoingPtAgamaFlag(Boolean caseOngoingPtAgamaFlag) {
		this.caseOngoingPtAgamaFlag = caseOngoingPtAgamaFlag;
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
	public Boolean getCaseFinishedPaFlag() {
		return caseFinishedPaFlag;
	}
	public void setCaseFinishedPaFlag(Boolean caseFinishedPaFlag) {
		this.caseFinishedPaFlag = caseFinishedPaFlag;
	}
	public Boolean getCaseFinishedPtAgamaFlag() {
		return caseFinishedPtAgamaFlag;
	}
	public void setCaseFinishedPtAgamaFlag(Boolean caseFinishedPtAgamaFlag) {
		this.caseFinishedPtAgamaFlag = caseFinishedPtAgamaFlag;
	}
	public Boolean getCaseFinishedPtunFlag() {
		return caseFinishedPtunFlag;
	}
	public void setCaseFinishedPtunFlag(Boolean caseFinishedPtunFlag) {
		this.caseFinishedPtunFlag = caseFinishedPtunFlag;
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
	
	
}
