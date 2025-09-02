package com.wo.module.litigation.model;

import java.io.Serializable;
import java.util.List;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.user.model.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LitigationNew extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 6395152164269025885L;

	private Long litigationId;
	private ParameterDetail caseType;
	private ParameterDetail caseTypeDtl;
	private String divisionOrBranchOffice;
	private String segment;
	private String description;
	private ParameterDetail caseTeamHandler;
	private User pic;
	private String picDescription;
	
	private LitigationPerdataTergugat litigationPerdataTergugat;
	private LitigationPerdataPenggugat litigationPerdataPenggugat;
	private LitigationPidanaPelapor litigationPidanaPelapor;
	private LitigationPidanaTerlapor litigationPidanaTerlapor;
	private LitigationPailitPkpu litigationPailitPkpu;
	
	private List<LitigationPic> litigationPics; 
	private List<LitigationProgressPerkara> listProgresPerkara;
	private List<LitigationPihakPenggugatPemohon> listPihakPenggugatPemohon;
	private List<LitigationPihakTergugatTermohon> listPihakTergugatTermohon;
	private List<LitigationPihakKuratorTurutTergugat> listPihakKuratorTurutTergugat;
	private List<LitigationPutusanPengadilan> listPutusanPengadilan;
	
	//helpers
	private String selectCaseType;
	private String selectCaseTypeDtlPerdata;
	private String prevDescription;
	private String selectCaseTypeDtl;
	private String selectedPicName;
	

	private String selectCaseTeamHandlerPerdataTer;
	private String selectCaseTeamHandlerPerdataPeng;
	private String selectCaseTeamHandlerPailitPkpu;
	private String selectCaseTeamHandlerPidanaPel;
	private String selectCaseTeamHandlerPidanaTer;
	
	private String picDescPerdataTer;
	private String picDescPerdataPeng;
	private String picDescPailitPkpu;
	private String picDescPidanaPel;
	private String picDescPidanaTer;
	
	private String noUrutOld;
	private String noPerkaraOld;
	
	//viewer on search table;
	private String caseTypeName;
	private String caseTypeDtlName;
	private String debtorName;
	private String caseNumberName;
	private String reportNumberName;
	private List<String> listNamaPelapor;
	private List<String> listNamaTerlapor;

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

	public ParameterDetail getCaseTypeDtl() {
		return caseTypeDtl;
	}

	public void setCaseTypeDtl(ParameterDetail caseTypeDtl) {
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

	public LitigationPerdataTergugat getLitigationPerdataTergugat() {
		return litigationPerdataTergugat;
	}

	public void setLitigationPerdataTergugat(LitigationPerdataTergugat litigationPerdataTergugat) {
		this.litigationPerdataTergugat = litigationPerdataTergugat;
	}

	public LitigationPidanaPelapor getLitigationPidanaPelapor() {
		return litigationPidanaPelapor;
	}

	public void setLitigationPidanaPelapor(LitigationPidanaPelapor litigationPidanaPelapor) {
		this.litigationPidanaPelapor = litigationPidanaPelapor;
	}

	public LitigationPidanaTerlapor getLitigationPidanaTerlapor() {
		return litigationPidanaTerlapor;
	}

	public void setLitigationPidanaTerlapor(LitigationPidanaTerlapor litigationPidanaTerlapor) {
		this.litigationPidanaTerlapor = litigationPidanaTerlapor;
	}

	public LitigationPailitPkpu getLitigationPailitPkpu() {
		return litigationPailitPkpu;
	}

	public void setLitigationPailitPkpu(LitigationPailitPkpu litigationPailitPkpu) {
		this.litigationPailitPkpu = litigationPailitPkpu;
	}

	public String getSelectCaseType() {
		return selectCaseType;
	}

	public void setSelectCaseType(String selectCaseType) {
		this.selectCaseType = selectCaseType;
	}

	public ParameterDetail getCaseTeamHandler() {
		return caseTeamHandler;
	}

	public void setCaseTeamHandler(ParameterDetail caseTeamHandler) {
		this.caseTeamHandler = caseTeamHandler;
	}

	public User getPic() {
		return pic;
	}

	public void setPic(User pic) {
		this.pic = pic;
	}

	public String getPicDescription() {
		return picDescription;
	}

	public void setPicDescription(String picDescription) {
		this.picDescription = picDescription;
	}

	public String getSelectCaseTypeDtl() {
		return selectCaseTypeDtl;
	}

	public void setSelectCaseTypeDtl(String selectCaseTypeDtl) {
		this.selectCaseTypeDtl = selectCaseTypeDtl;
	}

	public List<LitigationPic> getLitigationPics() {
		return litigationPics;
	}

	public void setLitigationPics(List<LitigationPic> litigationPics) {
		this.litigationPics = litigationPics;
	}

	public LitigationPerdataPenggugat getLitigationPerdataPenggugat() {
		return litigationPerdataPenggugat;
	}

	public void setLitigationPerdataPenggugat(LitigationPerdataPenggugat litigationPerdataPenggugat) {
		this.litigationPerdataPenggugat = litigationPerdataPenggugat;
	}
	
	public List<LitigationPihakPenggugatPemohon> getListPihakPenggugatPemohon() {
		return listPihakPenggugatPemohon;
	}

	public void setListPihakPenggugatPemohon(List<LitigationPihakPenggugatPemohon> listPihakPenggugatPemohon) {
		this.listPihakPenggugatPemohon = listPihakPenggugatPemohon;
	}

	public List<LitigationPihakTergugatTermohon> getListPihakTergugatTermohon() {
		return listPihakTergugatTermohon;
	}

	public void setListPihakTergugatTermohon(List<LitigationPihakTergugatTermohon> listPihakTergugatTermohon) {
		this.listPihakTergugatTermohon = listPihakTergugatTermohon;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<LitigationProgressPerkara> getListProgresPerkara() {
		return listProgresPerkara;
	}

	public void setListProgresPerkara(List<LitigationProgressPerkara> listProgresPerkara) {
		this.listProgresPerkara = listProgresPerkara;
	}
	
	//viewer on search table
	public String getCaseTypeName() {
		return caseTypeName;
	}

	public void setCaseTypeName(String caseTypeName) {
		this.caseTypeName = caseTypeName;
	}

	public String getCaseTypeDtlName() {
		return caseTypeDtlName;
	}

	public void setCaseTypeDtlName(String caseTypeDtlName) {
		this.caseTypeDtlName = caseTypeDtlName;
	}

	public String getNoUrutOld() {
		return noUrutOld;
	}

	public void setNoUrutOld(String noUrutOld) {
		this.noUrutOld = noUrutOld;
	}

	public String getNoPerkaraOld() {
		return noPerkaraOld;
	}

	public void setNoPerkaraOld(String noPerkaraOld) {
		this.noPerkaraOld = noPerkaraOld;
	}

	public String getPrevDescription() {
		return prevDescription;
	}

	public void setPrevDescription(String prevDescription) {
		this.prevDescription = prevDescription;
	}

	public String getSelectCaseTypeDtlPerdata() {
		return selectCaseTypeDtlPerdata;
	}

	public void setSelectCaseTypeDtlPerdata(String selectCaseTypeDtlPerdata) {
		this.selectCaseTypeDtlPerdata = selectCaseTypeDtlPerdata;
	}

	public String getSelectedPicName() {
		return selectedPicName;
	}

	public void setSelectedPicName(String selectedPicName) {
		this.selectedPicName = selectedPicName;
	}
	
}
