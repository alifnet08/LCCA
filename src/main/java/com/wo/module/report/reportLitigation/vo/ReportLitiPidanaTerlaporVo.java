package com.wo.module.report.reportLitigation.vo;

import java.io.Serializable;

public class ReportLitiPidanaTerlaporVo implements Serializable {

	private static final long serialVersionUID = 1665892229148069513L;

	private Long litigationId;
	private String unitKerja;
	private String nomorPerkara;
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
	private String tindakPidana;
	private String mataUangValas;
	private String keteranganPic;
	
	private Long tahunPerkara;
	private Double nilaiPerkaraIDR;
	
	private StringBuilder pelapor;
	private StringBuilder terlapor;
	private StringBuilder perkembanganPerkara;
	
	public Long getLitigationId() {
		return litigationId;
	}
	public void setLitigationId(Long litigationId) {
		this.litigationId = litigationId;
	}
	public String getUnitKerja() {
		return unitKerja;
	}
	public void setUnitKerja(String unitKerja) {
		this.unitKerja = unitKerja;
	}
	public String getNomorPerkara() {
		return nomorPerkara;
	}
	public void setNomorPerkara(String nomorPerkara) {
		this.nomorPerkara = nomorPerkara;
	}
	public String getInstansiPemeriksa() {
		return instansiPemeriksa;
	}
	public void setInstansiPemeriksa(String instansiPemeriksa) {
		this.instansiPemeriksa = instansiPemeriksa;
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
	public StringBuilder getPerkembanganPerkara() {
		return perkembanganPerkara;
	}
	public void setPerkembanganPerkara(StringBuilder perkembanganPerkara) {
		this.perkembanganPerkara = perkembanganPerkara;
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
	public String getTindakPidana() {
		return tindakPidana;
	}
	public void setTindakPidana(String tindakPidana) {
		this.tindakPidana = tindakPidana;
	}
	public String getMataUangValas() {
		return mataUangValas;
	}
	public void setMataUangValas(String mataUangValas) {
		this.mataUangValas = mataUangValas;
	}
	public String getTingkatPemeriksaanJaksa() {
		return tingkatPemeriksaanJaksa;
	}
	public void setTingkatPemeriksaanJaksa(String tingkatPemeriksaanJaksa) {
		this.tingkatPemeriksaanJaksa = tingkatPemeriksaanJaksa;
	}
	public String getKeteranganPic() {
		return keteranganPic;
	}
	public void setKeteranganPic(String keteranganPic) {
		this.keteranganPic = keteranganPic;
	}
	
	
}
