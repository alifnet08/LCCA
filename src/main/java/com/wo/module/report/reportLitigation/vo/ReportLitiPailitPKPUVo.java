package com.wo.module.report.reportLitigation.vo;

import java.io.Serializable;

public class ReportLitiPailitPKPUVo implements Serializable {

	private static final long serialVersionUID = 8993745250405987988L;
	
	private Long litigationId;
	private String cabang;
	private String segmen;
	private Long tahunPerkara;
	private String nomorPerkara;
	private String pengadilanNiaga;
	private String pemohon;
	private String termohon;
	private Double nilaiTagihanMaybankPKPU;
	private Double nilaiTagihanMaybankKepailitan;
	private String keterangan;
	private String kuasaHukumMaybank;
	private String tim1;
	private String tim2;
	private String internal;
	private String lawyer;
	private String pic;
	private String keteranganPic;
	
	private StringBuilder paraPihakPenggugat;
	private StringBuilder paraPihakTergugat;
	private StringBuilder amarPutusanPengadilan;
	private StringBuilder tanggalPutusanPengadilan;
	private StringBuilder kurator;
	private StringBuilder perkembanganTerakhir;
	
	public Long getLitigationId() {
		return litigationId;
	}
	public void setLitigationId(Long litigationId) {
		this.litigationId = litigationId;
	}
	public String getCabang() {
		return cabang;
	}
	public void setCabang(String cabang) {
		this.cabang = cabang;
	}
	public String getSegmen() {
		return segmen;
	}
	public void setSegmen(String segmen) {
		this.segmen = segmen;
	}
	public Long getTahunPerkara() {
		return tahunPerkara;
	}
	public void setTahunPerkara(Long tahunPerkara) {
		this.tahunPerkara = tahunPerkara;
	}
	public String getNomorPerkara() {
		return nomorPerkara;
	}
	public void setNomorPerkara(String nomorPerkara) {
		this.nomorPerkara = nomorPerkara;
	}
	public String getPengadilanNiaga() {
		return pengadilanNiaga;
	}
	public void setPengadilanNiaga(String pengadilanNiaga) {
		this.pengadilanNiaga = pengadilanNiaga;
	}
	public String getPemohon() {
		return pemohon;
	}
	public void setPemohon(String pemohon) {
		this.pemohon = pemohon;
	}
	public String getTermohon() {
		return termohon;
	}
	public void setTermohon(String termohon) {
		this.termohon = termohon;
	}
	public Double getNilaiTagihanMaybankPKPU() {
		return nilaiTagihanMaybankPKPU;
	}
	public void setNilaiTagihanMaybankPKPU(Double nilaiTagihanMaybankPKPU) {
		this.nilaiTagihanMaybankPKPU = nilaiTagihanMaybankPKPU;
	}
	public Double getNilaiTagihanMaybankKepailitan() {
		return nilaiTagihanMaybankKepailitan;
	}
	public void setNilaiTagihanMaybankKepailitan(Double nilaiTagihanMaybankKepailitan) {
		this.nilaiTagihanMaybankKepailitan = nilaiTagihanMaybankKepailitan;
	}
	public String getKeterangan() {
		return keterangan;
	}
	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}
	public String getKuasaHukumMaybank() {
		return kuasaHukumMaybank;
	}
	public void setKuasaHukumMaybank(String kuasaHukumMaybank) {
		this.kuasaHukumMaybank = kuasaHukumMaybank;
	}
	public String getTim1() {
		return tim1;
	}
	public void setTim1(String tim1) {
		this.tim1 = tim1;
	}
	public String getTim2() {
		return tim2;
	}
	public void setTim2(String tim2) {
		this.tim2 = tim2;
	}
	public String getInternal() {
		return internal;
	}
	public void setInternal(String internal) {
		this.internal = internal;
	}
	public String getLawyer() {
		return lawyer;
	}
	public void setLawyer(String lawyer) {
		this.lawyer = lawyer;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public StringBuilder getParaPihakPenggugat() {
		return paraPihakPenggugat;
	}
	public void setParaPihakPenggugat(StringBuilder paraPihakPenggugat) {
		this.paraPihakPenggugat = paraPihakPenggugat;
	}
	public StringBuilder getParaPihakTergugat() {
		return paraPihakTergugat;
	}
	public void setParaPihakTergugat(StringBuilder paraPihakTergugat) {
		this.paraPihakTergugat = paraPihakTergugat;
	}
	public StringBuilder getAmarPutusanPengadilan() {
		return amarPutusanPengadilan;
	}
	public void setAmarPutusanPengadilan(StringBuilder amarPutusanPengadilan) {
		this.amarPutusanPengadilan = amarPutusanPengadilan;
	}
	public StringBuilder getTanggalPutusanPengadilan() {
		return tanggalPutusanPengadilan;
	}
	public void setTanggalPutusanPengadilan(StringBuilder tanggalPutusanPengadilan) {
		this.tanggalPutusanPengadilan = tanggalPutusanPengadilan;
	}
	public StringBuilder getKurator() {
		return kurator;
	}
	public void setKurator(StringBuilder kurator) {
		this.kurator = kurator;
	}
	public StringBuilder getPerkembanganTerakhir() {
		return perkembanganTerakhir;
	}
	public void setPerkembanganTerakhir(StringBuilder perkembanganTerakhir) {
		this.perkembanganTerakhir = perkembanganTerakhir;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getKeteranganPic() {
		return keteranganPic;
	}
	public void setKeteranganPic(String keteranganPic) {
		this.keteranganPic = keteranganPic;
	}
	
	
}
