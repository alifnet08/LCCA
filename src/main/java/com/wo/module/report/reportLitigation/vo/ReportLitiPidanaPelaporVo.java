package com.wo.module.report.reportLitigation.vo;

import java.io.Serializable;

public class ReportLitiPidanaPelaporVo implements Serializable {

	private static final long serialVersionUID = 2307032996139741734L;
	
	private Long litigationId;
	private String cabang;
	private String segmen;
	private String tanggalLaporan;
	private String nomorLaporan;
	private String instansiPemeriksa;
	private String tim1;
	private String tim2;
	private String kasusPosisi;
	private String nilaiPerkaraValas;
	private String tingkatPemeriksaanPol;
	private String tingkatPemeriksaanJaksa;
	private String tingkatPemeriksaanPN;
	private String tingkatPemeriksaanSelesai;
	private String kuasaHukumMaybank;
	private String internal;
	private String lawyer;
	private String namaPic;
	private String mataUangValas;
	private String tindakPidana;
	private String keteranganPic;
	
	private Long tahunPerkara;
	private Double nilaiPerkaraIDR;
	
	private StringBuilder pelapor;
	private StringBuilder terlapor;
	private StringBuilder perkembanganPerkara;
	
	//helper for report print
	
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
	public String getTanggalLaporan() {
		return tanggalLaporan;
	}
	public void setTanggalLaporan(String tanggalLaporan) {
		this.tanggalLaporan = tanggalLaporan;
	}
	public String getNomorLaporan() {
		return nomorLaporan;
	}
	public void setNomorLaporan(String nomorLaporan) {
		this.nomorLaporan = nomorLaporan;
	}
	public String getInstansiPemeriksa() {
		return instansiPemeriksa;
	}
	public void setInstansiPemeriksa(String instansiPemeriksa) {
		this.instansiPemeriksa = instansiPemeriksa;
	}
	public StringBuilder getPelapor() {
		return pelapor;
	}
	public void setPelapor(StringBuilder pelapor) {
		this.pelapor = pelapor;
	}
	public StringBuilder getTerlapor() {
		return terlapor;
	}
	public void setTerlapor(StringBuilder terlapor) {
		this.terlapor = terlapor;
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
	public String getKasusPosisi() {
		return kasusPosisi;
	}
	public void setKasusPosisi(String kasusPosisi) {
		this.kasusPosisi = kasusPosisi;
	}
	public StringBuilder getPerkembanganPerkara() {
		return perkembanganPerkara;
	}
	public void setPerkembanganPerkara(StringBuilder perkembanganPerkara) {
		this.perkembanganPerkara = perkembanganPerkara;
	}
	public String getNilaiPerkaraValas() {
		return nilaiPerkaraValas;
	}
	public void setNilaiPerkaraValas(String nilaiPerkaraValas) {
		this.nilaiPerkaraValas = nilaiPerkaraValas;
	}
	public String getTingkatPemeriksaanPol() {
		return tingkatPemeriksaanPol;
	}
	public void setTingkatPemeriksaanPol(String tingkatPemeriksaanPol) {
		this.tingkatPemeriksaanPol = tingkatPemeriksaanPol;
	}
	public String getTingkatPemeriksaanJaksa() {
		return tingkatPemeriksaanJaksa;
	}
	public void setTingkatPemeriksaanJaksa(String tingkatPemeriksaanJaksa) {
		this.tingkatPemeriksaanJaksa = tingkatPemeriksaanJaksa;
	}
	public String getTingkatPemeriksaanPN() {
		return tingkatPemeriksaanPN;
	}
	public void setTingkatPemeriksaanPN(String tingkatPemeriksaanPN) {
		this.tingkatPemeriksaanPN = tingkatPemeriksaanPN;
	}
	public String getTingkatPemeriksaanSelesai() {
		return tingkatPemeriksaanSelesai;
	}
	public void setTingkatPemeriksaanSelesai(String tingkatPemeriksaanSelesai) {
		this.tingkatPemeriksaanSelesai = tingkatPemeriksaanSelesai;
	}
	public String getKuasaHukumMaybank() {
		return kuasaHukumMaybank;
	}
	public void setKuasaHukumMaybank(String kuasaHukumMaybank) {
		this.kuasaHukumMaybank = kuasaHukumMaybank;
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
	public Long getTahunPerkara() {
		return tahunPerkara;
	}
	public void setTahunPerkara(Long tahunPerkara) {
		this.tahunPerkara = tahunPerkara;
	}
	public Double getNilaiPerkaraIDR() {
		return nilaiPerkaraIDR;
	}
	public void setNilaiPerkaraIDR(Double nilaiPerkaraIDR) {
		this.nilaiPerkaraIDR = nilaiPerkaraIDR;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getNamaPic() {
		return namaPic;
	}
	public void setNamaPic(String namaPic) {
		this.namaPic = namaPic;
	}
	public String getMataUangValas() {
		return mataUangValas;
	}
	public void setMataUangValas(String mataUangValas) {
		this.mataUangValas = mataUangValas;
	}
	public String getTindakPidana() {
		return tindakPidana;
	}
	public void setTindakPidana(String tindakPidana) {
		this.tindakPidana = tindakPidana;
	}
	public String getKeteranganPic() {
		return keteranganPic;
	}
	public void setKeteranganPic(String keteranganPic) {
		this.keteranganPic = keteranganPic;
	}
	
}