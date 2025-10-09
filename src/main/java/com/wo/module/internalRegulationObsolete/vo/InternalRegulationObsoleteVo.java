package com.wo.module.internalRegulationObsolete.vo;

import java.io.Serializable;

/**
 * @author WhiteOpen Teknologi
 *
 */
public class InternalRegulationObsoleteVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7649410630168454842L;

	private Long internalRegulasiObsoleteId;
	private String judulRegulasi;
	private String tipeRegulasiObsolete;
	private String nomorObsolete;
	private String infoObsolete;
	private String publishDivision;
	private String publishDirectorateName;
	private String tanggalObsoleteStr;
	private String statusOpenCloseObsolete;
	private String picIrgName;
	
	//Extension variable for obsolete report
	private String picIrgName2;
	private String unitKerjaPicStr;
	private String picName1;
	private String picName2;
	private String picName3;
	private String tanggalKonversiStr;
	private String notes;
	
	public Long getInternalRegulasiObsoleteId() {
		return internalRegulasiObsoleteId;
	}
	public void setInternalRegulasiObsoleteId(Long internalRegulasiObsoleteId) {
		this.internalRegulasiObsoleteId = internalRegulasiObsoleteId;
	}
	public String getJudulRegulasi() {
		return judulRegulasi;
	}
	public void setJudulRegulasi(String judulRegulasi) {
		this.judulRegulasi = judulRegulasi;
	}
	public String getTipeRegulasiObsolete() {
		return tipeRegulasiObsolete;
	}
	public void setTipeRegulasiObsolete(String tipeRegulasiObsolete) {
		this.tipeRegulasiObsolete = tipeRegulasiObsolete;
	}
	public String getNomorObsolete() {
		return nomorObsolete;
	}
	public void setNomorObsolete(String nomorObsolete) {
		this.nomorObsolete = nomorObsolete;
	}
	public String getInfoObsolete() {
		return infoObsolete;
	}
	public void setInfoObsolete(String infoObsolete) {
		this.infoObsolete = infoObsolete;
	}
	public String getPublishDivision() {
		return publishDivision;
	}
	public void setPublishDivision(String publishDivision) {
		this.publishDivision = publishDivision;
	}
	public String getPublishDirectorateName() {
		return publishDirectorateName;
	}
	public void setPublishDirectorateName(String publishDirectorateName) {
		this.publishDirectorateName = publishDirectorateName;
	}
	public String getTanggalObsoleteStr() {
		return tanggalObsoleteStr;
	}
	public void setTanggalObsoleteStr(String tanggalObsoleteStr) {
		this.tanggalObsoleteStr = tanggalObsoleteStr;
	}
	public String getStatusOpenCloseObsolete() {
		return statusOpenCloseObsolete;
	}
	public void setStatusOpenCloseObsolete(String statusOpenCloseObsolete) {
		this.statusOpenCloseObsolete = statusOpenCloseObsolete;
	}
	public String getPicIrgName() {
		return picIrgName;
	}
	public void setPicIrgName(String picIrgName) {
		this.picIrgName = picIrgName;
	}
	
	//split for extension
	
	public String getPicIrgName2() {
		return picIrgName2;
	}
	public void setPicIrgName2(String picIrgName2) {
		this.picIrgName2 = picIrgName2;
	}
	public String getUnitKerjaPicStr() {
		return unitKerjaPicStr;
	}
	public void setUnitKerjaPicStr(String unitKerjaPicStr) {
		this.unitKerjaPicStr = unitKerjaPicStr;
	}
	public String getPicName1() {
		return picName1;
	}
	public void setPicName1(String picName1) {
		this.picName1 = picName1;
	}
	public String getPicName2() {
		return picName2;
	}
	public void setPicName2(String picName2) {
		this.picName2 = picName2;
	}
	public String getPicName3() {
		return picName3;
	}
	public void setPicName3(String picName3) {
		this.picName3 = picName3;
	}
	public String getTanggalKonversiStr() {
		return tanggalKonversiStr;
	}
	public void setTanggalKonversiStr(String tanggalKonversiStr) {
		this.tanggalKonversiStr = tanggalKonversiStr;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}	
}
