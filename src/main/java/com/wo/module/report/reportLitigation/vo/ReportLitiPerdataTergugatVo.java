package com.wo.module.report.reportLitigation.vo;

import java.io.Serializable;

public class ReportLitiPerdataTergugatVo implements Serializable {

	private static final long serialVersionUID = -740903955124309043L;

	private Long litigationId;
	private String unitKerja;
	private String Segmen;
	private String debitur;
	private String nomorPerkara;
	private String tipePengadilan;
	private String domisiliPengadilan;
	private String penggugatKuasaHukum;
	private String tergugatKuasaHukum;
	private String tim1;
	private String tim2;
	private String pic;
	private String internal;
	private String lawyer;
	private String tanggalPanggilanSidang;
	private String pokokPerkara;
	private String subPokokPerkara;
	private String kasusPosisiGugatan;
	private String kedudukanHukumMaybank;
	private String tuntutanPenggugat;
	private String materialValasTipe;
	private String materialValasValue;
	private String putusanPengadilan;
	private String statusPutusanMenang;
	private String statusPutusanKalah;
	private String statusPutusanSidang;
	private String perkaraBerjalanBani;
	private String perkaraBerjalanPN;
	private String perkaraBerjalanPT;
	private String perkaraBerjalanPTUN;
	private String perkaraBerjalanPA;
	private String perkaraBerjalanPTAgama;
	private String perkaraBerjalanMAKasasi;
	private String perkaraBerjalanMAPK;
	private String perkaraSelesaiBani;
	private String perkaraSelesaiPA;
	private String perkaraSelesaiPN;
	private String perkaraSelesaiPTUN;
	private String perkaraSelesaiPT;
	private String perkaraSelesaiMAKasasi;
	private String perkaraSelesaiMAPK;
	private String perkaraSelesaiTanggalSelesai;
	private String namaKantorHukum;
	private String keteranganPic;
	
	private Long tahunPerkara;
	private Double materialIDR;
	private Double immaterial;
	private Double totalTuntutan;
	private Double totalNilaiHakTanggungan;
	private Double potensiKerugianLain;
	private Long totalPerkaraBerjalan;
	private Long totalPerkaraSelesai;
	
	private StringBuilder paraPihakPenggugat;
	private StringBuilder paraPihakTergugat;
	private StringBuilder paraPihakTurutTergugat;
	private StringBuilder progressTerakhir;
	
	public String getUnitKerja() {
		return unitKerja;
	}
	public void setUnitKerja(String unitKerja) {
		this.unitKerja = unitKerja;
	}
	public String getSegmen() {
		return Segmen;
	}
	public void setSegmen(String segmen) {
		Segmen = segmen;
	}
	public String getDebitur() {
		return debitur;
	}
	public void setDebitur(String debitur) {
		this.debitur = debitur;
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
	public String getPenggugatKuasaHukum() {
		return penggugatKuasaHukum;
	}
	public void setPenggugatKuasaHukum(String penggugatKuasaHukum) {
		this.penggugatKuasaHukum = penggugatKuasaHukum;
	}
	public String getTergugatKuasaHukum() {
		return tergugatKuasaHukum;
	}
	public void setTergugatKuasaHukum(String tergugatKuasaHukum) {
		this.tergugatKuasaHukum = tergugatKuasaHukum;
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
	public String getTanggalPanggilanSidang() {
		return tanggalPanggilanSidang;
	}
	public void setTanggalPanggilanSidang(String tanggalPanggilanSidang) {
		this.tanggalPanggilanSidang = tanggalPanggilanSidang;
	}
	public String getPokokPerkara() {
		return pokokPerkara;
	}
	public void setPokokPerkara(String pokokPerkara) {
		this.pokokPerkara = pokokPerkara;
	}
	public String getSubPokokPerkara() {
		return subPokokPerkara;
	}
	public void setSubPokokPerkara(String subPokokPerkara) {
		this.subPokokPerkara = subPokokPerkara;
	}
	public String getKasusPosisiGugatan() {
		return kasusPosisiGugatan;
	}
	public void setKasusPosisiGugatan(String kasusPosisiGugatan) {
		this.kasusPosisiGugatan = kasusPosisiGugatan;
	}
	public String getKedudukanHukumMaybank() {
		return kedudukanHukumMaybank;
	}
	public void setKedudukanHukumMaybank(String kedudukanHukumMaybank) {
		this.kedudukanHukumMaybank = kedudukanHukumMaybank;
	}
	public String getTuntutanPenggugat() {
		return tuntutanPenggugat;
	}
	public void setTuntutanPenggugat(String tuntutanPenggugat) {
		this.tuntutanPenggugat = tuntutanPenggugat;
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
	public Double getTotalNilaiHakTanggungan() {
		return totalNilaiHakTanggungan;
	}
	public void setTotalNilaiHakTanggungan(Double totalNilaiHakTanggungan) {
		this.totalNilaiHakTanggungan = totalNilaiHakTanggungan;
	}
	public Double getPotensiKerugianLain() {
		return potensiKerugianLain;
	}
	public void setPotensiKerugianLain(Double potensiKerugianLain) {
		this.potensiKerugianLain = potensiKerugianLain;
	}
	public String getMaterialValasTipe() {
		return materialValasTipe;
	}
	public void setMaterialValasTipe(String materialValasTipe) {
		this.materialValasTipe = materialValasTipe;
	}
	public String getMaterialValasValue() {
		return materialValasValue;
	}
	public void setMaterialValasValue(String materialValasValue) {
		this.materialValasValue = materialValasValue;
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
	public String getStatusPutusanMenang() {
		return statusPutusanMenang;
	}
	public void setStatusPutusanMenang(String statusPutusanMenang) {
		this.statusPutusanMenang = statusPutusanMenang;
	}
	public String getStatusPutusanKalah() {
		return statusPutusanKalah;
	}
	public void setStatusPutusanKalah(String statusPutusanKalah) {
		this.statusPutusanKalah = statusPutusanKalah;
	}
	public String getStatusPutusanSidang() {
		return statusPutusanSidang;
	}
	public void setStatusPutusanSidang(String statusPutusanSidang) {
		this.statusPutusanSidang = statusPutusanSidang;
	}
	public String getPerkaraBerjalanBani() {
		return perkaraBerjalanBani;
	}
	public void setPerkaraBerjalanBani(String perkaraBerjalanBani) {
		this.perkaraBerjalanBani = perkaraBerjalanBani;
	}
	public String getPerkaraBerjalanPN() {
		return perkaraBerjalanPN;
	}
	public void setPerkaraBerjalanPN(String perkaraBerjalanPN) {
		this.perkaraBerjalanPN = perkaraBerjalanPN;
	}
	public String getPerkaraBerjalanPT() {
		return perkaraBerjalanPT;
	}
	public void setPerkaraBerjalanPT(String perkaraBerjalanPT) {
		this.perkaraBerjalanPT = perkaraBerjalanPT;
	}
	public String getPerkaraBerjalanPTUN() {
		return perkaraBerjalanPTUN;
	}
	public void setPerkaraBerjalanPTUN(String perkaraBerjalanPTUN) {
		this.perkaraBerjalanPTUN = perkaraBerjalanPTUN;
	}
	public String getPerkaraBerjalanPA() {
		return perkaraBerjalanPA;
	}
	public void setPerkaraBerjalanPA(String perkaraBerjalanPA) {
		this.perkaraBerjalanPA = perkaraBerjalanPA;
	}
	public String getPerkaraBerjalanPTAgama() {
		return perkaraBerjalanPTAgama;
	}
	public void setPerkaraBerjalanPTAgama(String perkaraBerjalanPTAgama) {
		this.perkaraBerjalanPTAgama = perkaraBerjalanPTAgama;
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
	public String getPerkaraSelesaiBani() {
		return perkaraSelesaiBani;
	}
	public void setPerkaraSelesaiBani(String perkaraSelesaiBani) {
		this.perkaraSelesaiBani = perkaraSelesaiBani;
	}
	public String getPerkaraSelesaiPA() {
		return perkaraSelesaiPA;
	}
	public void setPerkaraSelesaiPA(String perkaraSelesaiPA) {
		this.perkaraSelesaiPA = perkaraSelesaiPA;
	}
	public String getPerkaraSelesaiPN() {
		return perkaraSelesaiPN;
	}
	public void setPerkaraSelesaiPN(String perkaraSelesaiPN) {
		this.perkaraSelesaiPN = perkaraSelesaiPN;
	}
	public String getPerkaraSelesaiPTUN() {
		return perkaraSelesaiPTUN;
	}
	public void setPerkaraSelesaiPTUN(String perkaraSelesaiPTUN) {
		this.perkaraSelesaiPTUN = perkaraSelesaiPTUN;
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
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Long getLitigationId() {
		return litigationId;
	}
	public void setLitigationId(Long litigationId) {
		this.litigationId = litigationId;
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
	public String getNamaKantorHukum() {
		return namaKantorHukum;
	}
	public void setNamaKantorHukum(String namaKantorHukum) {
		this.namaKantorHukum = namaKantorHukum;
	}
	public StringBuilder getParaPihakTurutTergugat() {
		return paraPihakTurutTergugat;
	}
	public void setParaPihakTurutTergugat(StringBuilder paraPihakTurutTergugat) {
		this.paraPihakTurutTergugat = paraPihakTurutTergugat;
	}
	public String getKeteranganPic() {
		return keteranganPic;
	}
	public void setKeteranganPic(String keteranganPic) {
		this.keteranganPic = keteranganPic;
	}
	
}