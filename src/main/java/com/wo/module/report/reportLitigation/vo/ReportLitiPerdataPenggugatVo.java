package com.wo.module.report.reportLitigation.vo;

import java.io.Serializable;

public class ReportLitiPerdataPenggugatVo implements Serializable{

	private static final long serialVersionUID = -1872339963230675687L;
	
	private Long litigationId;
	private String unitKerja;
	private String nomorPerkara;
	private String tipePengadilan;
	private String domisiliPengadilan;
	private String kuasaHukumPenggugat;
	private String kuasaHukumTergugat;
	private String kuasaHukumInternal;
	private String kuasaHukumLawyer;
	private String tim1;
	private String tim2;
	private String pic;
	private String tuntutanPenggugat;
	private String materialValas;
	private String putusanPengadilan;
	private String perkaraBerjalanPN;
	private String perkaraBerjalanBani;
	private String perkaraBerjalanPT;
	private String perkaraBerjalanMAKasasi;
	private String perkaraBerjalanMAPK;
	private String perkaraSelesaiBani;
	private String perkaraSelesaiPN;
	private String perkaraSelesaiPT;
	private String perkaraSelesaiMAKasasi;
	private String perkaraSelesaiMAPK;
	private String perkaraSelesaiTanggalSelesai;
	private String mataUangValas;
	private String namaKantorHukum;
	private String keteranganPic;
	
	private Long tahunPerkara;
	private Double materialIDR;
	private Double immaterial;
	private Double totalTuntutan;
	private Long totalPerkaraBerjalan;
	private Long totalPerkaraSelesai;
	
	private StringBuilder paraPihakPenggugat;
	private StringBuilder paraPihakTergugat;
	private StringBuilder paraPihakTurutTergugat;
	private StringBuilder progressTerakhir;
	
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
	public String getTipePengadilan() {
		return tipePengadilan;
	}
	public void setTipePengadilan(String tipePengadilan) {
		this.tipePengadilan = tipePengadilan;
	}
	public String getDomisiliPengadilan() {
		return domisiliPengadilan;
	}
	public void setDomisiliPengadilan(String domisiliPengadilan) {
		this.domisiliPengadilan = domisiliPengadilan;
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
	public String getKuasaHukumInternal() {
		return kuasaHukumInternal;
	}
	public void setKuasaHukumInternal(String kuasaHukumInternal) {
		this.kuasaHukumInternal = kuasaHukumInternal;
	}
	public String getKuasaHukumLawyer() {
		return kuasaHukumLawyer;
	}
	public void setKuasaHukumLawyer(String kuasaHukumLawyer) {
		this.kuasaHukumLawyer = kuasaHukumLawyer;
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
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public String getTuntutanPenggugat() {
		return tuntutanPenggugat;
	}
	public void setTuntutanPenggugat(String tuntutanPenggugat) {
		this.tuntutanPenggugat = tuntutanPenggugat;
	}
	public String getMaterialValas() {
		return materialValas;
	}
	public void setMaterialValas(String materialValas) {
		this.materialValas = materialValas;
	}
	public String getPutusanPengadilan() {
		return putusanPengadilan;
	}
	public void setPutusanPengadilan(String putusanPengadilan) {
		this.putusanPengadilan = putusanPengadilan;
	}
	public StringBuilder getProgressTerakhir() {
		return progressTerakhir;
	}
	public void setProgressTerakhir(StringBuilder progressTerakhir) {
		this.progressTerakhir = progressTerakhir;
	}
	public String getPerkaraBerjalanPN() {
		return perkaraBerjalanPN;
	}
	public void setPerkaraBerjalanPN(String perkaraBerjalanPN) {
		this.perkaraBerjalanPN = perkaraBerjalanPN;
	}
	public String getPerkaraBerjalanBani() {
		return perkaraBerjalanBani;
	}
	public void setPerkaraBerjalanBani(String perkaraBerjalanBani) {
		this.perkaraBerjalanBani = perkaraBerjalanBani;
	}
	public String getPerkaraBerjalanPT() {
		return perkaraBerjalanPT;
	}
	public void setPerkaraBerjalanPT(String perkaraBerjalanPT) {
		this.perkaraBerjalanPT = perkaraBerjalanPT;
	}
	public String getPerkaraBerjalanMAKasasi() {
		return perkaraBerjalanMAKasasi;
	}
	public void setPerkaraBerjalanMAKasasi(String perkaraBerjalanMAKasasi) {
		this.perkaraBerjalanMAKasasi = perkaraBerjalanMAKasasi;
	}
	public String getPerkaraBerjalanMAPK() {
		return perkaraBerjalanMAPK;
	}
	public void setPerkaraBerjalanMAPK(String perkaraBerjalanMAPK) {
		this.perkaraBerjalanMAPK = perkaraBerjalanMAPK;
	}
	public String getPerkaraSelesaiBani() {
		return perkaraSelesaiBani;
	}
	public void setPerkaraSelesaiBani(String perkaraSelesaiBani) {
		this.perkaraSelesaiBani = perkaraSelesaiBani;
	}
	public String getPerkaraSelesaiPN() {
		return perkaraSelesaiPN;
	}
	public void setPerkaraSelesaiPN(String perkaraSelesaiPN) {
		this.perkaraSelesaiPN = perkaraSelesaiPN;
	}
	public String getPerkaraSelesaiPT() {
		return perkaraSelesaiPT;
	}
	public void setPerkaraSelesaiPT(String perkaraSelesaiPT) {
		this.perkaraSelesaiPT = perkaraSelesaiPT;
	}
	public String getPerkaraSelesaiMAKasasi() {
		return perkaraSelesaiMAKasasi;
	}
	public void setPerkaraSelesaiMAKasasi(String perkaraSelesaiMAKasasi) {
		this.perkaraSelesaiMAKasasi = perkaraSelesaiMAKasasi;
	}
	public String getPerkaraSelesaiMAPK() {
		return perkaraSelesaiMAPK;
	}
	public void setPerkaraSelesaiMAPK(String perkaraSelesaiMAPK) {
		this.perkaraSelesaiMAPK = perkaraSelesaiMAPK;
	}
	public String getPerkaraSelesaiTanggalSelesai() {
		return perkaraSelesaiTanggalSelesai;
	}
	public void setPerkaraSelesaiTanggalSelesai(String perkaraSelesaiTanggalSelesai) {
		this.perkaraSelesaiTanggalSelesai = perkaraSelesaiTanggalSelesai;
	}
	public Long getTahunPerkara() {
		return tahunPerkara;
	}
	public void setTahunPerkara(Long tahunPerkara) {
		this.tahunPerkara = tahunPerkara;
	}
	public Double getMaterialIDR() {
		return materialIDR;
	}
	public void setMaterialIDR(Double materialIDR) {
		this.materialIDR = materialIDR;
	}
	public Double getImmaterial() {
		return immaterial;
	}
	public void setImmaterial(Double immaterial) {
		this.immaterial = immaterial;
	}
	public Double getTotalTuntutan() {
		return totalTuntutan;
	}
	public void setTotalTuntutan(Double totalTuntutan) {
		this.totalTuntutan = totalTuntutan;
	}
	public Long getTotalPerkaraBerjalan() {
		return totalPerkaraBerjalan;
	}
	public void setTotalPerkaraBerjalan(Long totalPerkaraBerjalan) {
		this.totalPerkaraBerjalan = totalPerkaraBerjalan;
	}
	public Long getTotalPerkaraSelesai() {
		return totalPerkaraSelesai;
	}
	public void setTotalPerkaraSelesai(Long totalPerkaraSelesai) {
		this.totalPerkaraSelesai = totalPerkaraSelesai;
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
	public StringBuilder getParaPihakTurutTergugat() {
		return paraPihakTurutTergugat;
	}
	public void setParaPihakTurutTergugat(StringBuilder paraPihakTurutTergugat) {
		this.paraPihakTurutTergugat = paraPihakTurutTergugat;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getMataUangValas() {
		return mataUangValas;
	}
	public void setMataUangValas(String mataUangValas) {
		this.mataUangValas = mataUangValas;
	}
	public String getNamaKantorHukum() {
		return namaKantorHukum;
	}
	public void setNamaKantorHukum(String namaKantorHukum) {
		this.namaKantorHukum = namaKantorHukum;
	}
	public String getKeteranganPic() {
		return keteranganPic;
	}
	public void setKeteranganPic(String keteranganPic) {
		this.keteranganPic = keteranganPic;
	}
	

}
