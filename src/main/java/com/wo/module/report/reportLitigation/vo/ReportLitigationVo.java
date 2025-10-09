package com.wo.module.report.reportLitigation.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.parameter.model.ParameterDetail;

public class ReportLitigationVo extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 6395152164269025885L;

	private Long litigationId;
	private ParameterDetail caseType;
	private String caseTypeDtl;
	private String debitur;
	private String divisionOrBranchOffice;
	private String segment;
	private String litigationNo;
	private String lawSuit;
	
	private ParameterDetail litigationPlace;
	private String charges;
	private String materialCharges;
	private String immaterialCharges;
	
	private String status;
	private String statusCheck;
	
	private List<ReportLitiPerdataTergugatVo> reportLitiPerdataTergugatVo;
	private List<ReportLitiPerdataPenggugatVo> reportLitiPerdataPenggugatVo;
	private List<ReportLitiPailitPKPUVo> reportLitiPailitPKPUVo;
	private List<ReportLitiPidanaPelaporVo> reportLitiPidanaPelaporVo;
	private List<ReportLitiPidanaTerlaporVo> reportLitiPidanaTerlaporVo;
	
	// helper
	private String jenisPerkara;
	private String jenisPerkaraIn;
	private String jenisPerkaraEn;
	private String kantorCabang;
	private String kantorCabangIn;
	private String kantorCabangEn;
	private String noPerkara;
	private String jenisGugatan;
	private String daerahPerkara;
	private String daerahPerkaraIn;
	private String daerahPerkaraEn;
	private Long material;
	private Long immaterial;
	private String pelapor;
	private String terlapor;
	private String supervisoryJudge;
	private String team;
	
	//no longer used
	private ParameterDetail branchOffice;
	//no longer used - END

	public Long getLitigationId() {
		return litigationId;
	}

	public void setLitigationId(Long litigationId) {
		this.litigationId = litigationId;
	}

	public ParameterDetail getCaseType() {
		return caseType;
	}

	public void setCaseType(ParameterDetail caseType) {
		this.caseType = caseType;
	}


	public ParameterDetail getBranchOffice() {
		return branchOffice;
	}

	public void setBranchOffice(ParameterDetail branchOffice) {
		this.branchOffice = branchOffice;
	}

	public String getLitigationNo() {
		return litigationNo;
	}

	public void setLitigationNo(String litigationNo) {
		this.litigationNo = litigationNo;
	}

	public String getLawSuit() {
		return lawSuit;
	}

	public void setLawSuit(String lawSuit) {
		this.lawSuit = lawSuit;
	}

	

	public ParameterDetail getLitigationPlace() {
		return litigationPlace;
	}

	public void setLitigationPlace(ParameterDetail litigationPlace) {
		this.litigationPlace = litigationPlace;
	}

	public String getCharges() {
		return charges;
	}

	public void setCharges(String charges) {
		this.charges = charges;
	}

	public String getMaterialCharges() {
		return materialCharges;
	}

	public void setMaterialCharges(String materialCharges) {
		this.materialCharges = materialCharges;
	}

	public String getImmaterialCharges() {
		return immaterialCharges;
	}

	public void setImmaterialCharges(String immaterialCharges) {
		this.immaterialCharges = immaterialCharges;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	

	public List<ReportLitiPerdataTergugatVo> getReportLitiPerdataTergugatVo() {
		return reportLitiPerdataTergugatVo;
	}

	public void setReportLitiPerdataTergugatVo(List<ReportLitiPerdataTergugatVo> reportLitiPerdataTergugatVo) {
		this.reportLitiPerdataTergugatVo = reportLitiPerdataTergugatVo;
	}

	public List<ReportLitiPerdataPenggugatVo> getReportLitiPerdataPenggugatVo() {
		return reportLitiPerdataPenggugatVo;
	}

	public void setReportLitiPerdataPenggugatVo(List<ReportLitiPerdataPenggugatVo> reportLitiPerdataPenggugatVo) {
		this.reportLitiPerdataPenggugatVo = reportLitiPerdataPenggugatVo;
	}

	public List<ReportLitiPailitPKPUVo> getReportLitiPailitPKPUVo() {
		return reportLitiPailitPKPUVo;
	}

	public void setReportLitiPailitPKPUVo(List<ReportLitiPailitPKPUVo> reportLitiPailitPKPUVo) {
		this.reportLitiPailitPKPUVo = reportLitiPailitPKPUVo;
	}

	public List<ReportLitiPidanaPelaporVo> getReportLitiPidanaPelaporVo() {
		return reportLitiPidanaPelaporVo;
	}

	public void setReportLitiPidanaPelaporVo(List<ReportLitiPidanaPelaporVo> reportLitiPidanaPelaporVo) {
		this.reportLitiPidanaPelaporVo = reportLitiPidanaPelaporVo;
	}

	public List<ReportLitiPidanaTerlaporVo> getReportLitiPidanaTerlaporVo() {
		return reportLitiPidanaTerlaporVo;
	}

	public void setReportLitiPidanaTerlaporVo(List<ReportLitiPidanaTerlaporVo> reportLitiPidanaTerlaporVo) {
		this.reportLitiPidanaTerlaporVo = reportLitiPidanaTerlaporVo;
	}

	public String getDebitur() {
		return debitur;
	}

	public void setDebitur(String debitur) {
		this.debitur = debitur;
	}

	@SuppressWarnings("static-access")
	public String getJenisPerkara() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		
		if (locale != null && locale.equals(locale.ENGLISH)) {
			jenisPerkara = jenisPerkaraEn;
		} else {
			jenisPerkara = jenisPerkaraIn;
		}
		return jenisPerkara;
	}

	public void setJenisPerkara(String jenisPerkara) {
		this.jenisPerkara = jenisPerkara;
	}

	public String getJenisPerkaraIn() {
		return jenisPerkaraIn;
	}

	public void setJenisPerkaraIn(String jenisPerkaraIn) {
		this.jenisPerkaraIn = jenisPerkaraIn;
	}

	public String getJenisPerkaraEn() {
		return jenisPerkaraEn;
	}

	public void setJenisPerkaraEn(String jenisPerkaraEn) {
		this.jenisPerkaraEn = jenisPerkaraEn;
	}

	@SuppressWarnings("static-access")
	public String getKantorCabang() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		
		if (locale != null && locale.equals(locale.ENGLISH)) {
			kantorCabang = kantorCabangEn;
		} else {
			kantorCabang = kantorCabangIn;
		}
		
		return kantorCabang;
	}

	public void setKantorCabang(String kantorCabang) {
		this.kantorCabang = kantorCabang;
	}

	public String getKantorCabangIn() {
		return kantorCabangIn;
	}

	public void setKantorCabangIn(String kantorCabangIn) {
		this.kantorCabangIn = kantorCabangIn;
	}

	public String getKantorCabangEn() {
		return kantorCabangEn;
	}

	public void setKantorCabangEn(String kantorCabangEn) {
		this.kantorCabangEn = kantorCabangEn;
	}

	public String getNoPerkara() {
		return noPerkara;
	}

	public void setNoPerkara(String noPerkara) {
		this.noPerkara = noPerkara;
	}

	public String getJenisGugatan() {
		return jenisGugatan;
	}

	public void setJenisGugatan(String jenisGugatan) {
		this.jenisGugatan = jenisGugatan;
	}

	@SuppressWarnings("static-access")
	public String getDaerahPerkara() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		
		if (locale != null && locale.equals(locale.ENGLISH)) {
			daerahPerkara = daerahPerkaraEn;
		} else {
			daerahPerkara = daerahPerkaraIn;
		}
		
		return daerahPerkara;
	}

	public void setDaerahPerkara(String daerahPerkara) {
		this.daerahPerkara = daerahPerkara;
	}

	public String getDaerahPerkaraIn() {
		return daerahPerkaraIn;
	}

	public void setDaerahPerkaraIn(String daerahPerkaraIn) {
		this.daerahPerkaraIn = daerahPerkaraIn;
	}

	public String getDaerahPerkaraEn() {
		return daerahPerkaraEn;
	}

	public void setDaerahPerkaraEn(String daerahPerkaraEn) {
		this.daerahPerkaraEn = daerahPerkaraEn;
	}

	public Long getMaterial() {
		return material;
	}

	public void setMaterial(Long material) {
		this.material = material;
	}

	public Long getImmaterial() {
		return immaterial;
	}

	public void setImmaterial(Long immaterial) {
		this.immaterial = immaterial;
	}

	public String getPelapor() {
		return pelapor;
	}

	public void setPelapor(String pelapor) {
		this.pelapor = pelapor;
	}

	public String getTerlapor() {
		return terlapor;
	}

	public void setTerlapor(String terlapor) {
		this.terlapor = terlapor;
	}

	public String getSupervisoryJudge() {
		return supervisoryJudge;
	}

	public void setSupervisoryJudge(String supervisoryJudge) {
		this.supervisoryJudge = supervisoryJudge;
	}

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
	}

	public String getStatusCheck() {
		return statusCheck;
	}

	public void setStatusCheck(String statusCheck) {
		this.statusCheck = statusCheck;
	}

	public String getCaseTypeDtl() {
		return caseTypeDtl;
	}

	public void setCaseTypeDtl(String caseTypeDtl) {
		this.caseTypeDtl = caseTypeDtl;
	}

	public String getDivisionOrBranchOffice() {
		return divisionOrBranchOffice;
	}

	public void setDivisionOrBranchOffice(String divisionOrBranchOffice) {
		this.divisionOrBranchOffice = divisionOrBranchOffice;
	}

	public String getSegment() {
		return segment;
	}

	public void setSegment(String segment) {
		this.segment = segment;
	}
	
	
}
