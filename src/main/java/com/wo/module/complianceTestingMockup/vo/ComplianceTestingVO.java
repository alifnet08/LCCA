package com.wo.module.complianceTestingMockup.vo;

import java.io.Serializable;

public class ComplianceTestingVO implements Serializable {
	private static final long serialVersionUID = 3019374791490640764L;

	private Long id;
	private String judulPemeriksaan;
	private String noPemeriksaan;
	private String namaPic;
	private String targetTgl;
	private String statusTindakLanjut;
	private String tglPemenuhanTindakLanjut;
	private String hasilObservasi;
	private String rekomendasi;
	private String keterangan;
	private String statusCompliance;
	private Integer closeFlag;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getJudulPemeriksaan() {
		return judulPemeriksaan;
	}
	public void setJudulPemeriksaan(String judulPemeriksaan) {
		this.judulPemeriksaan = judulPemeriksaan;
	}
	public String getNoPemeriksaan() {
		return noPemeriksaan;
	}
	public void setNoPemeriksaan(String noPemeriksaan) {
		this.noPemeriksaan = noPemeriksaan;
	}
	public String getNamaPic() {
		return namaPic;
	}
	public void setNamaPic(String namaPic) {
		this.namaPic = namaPic;
	}
	public String getTargetTgl() {
		return targetTgl;
	}
	public void setTargetTgl(String targetTgl) {
		this.targetTgl = targetTgl;
	}
	public String getStatusTindakLanjut() {
		return statusTindakLanjut;
	}
	public void setStatusTindakLanjut(String statusTindakLanjut) {
		this.statusTindakLanjut = statusTindakLanjut;
	}
	public String getTglPemenuhanTindakLanjut() {
		return tglPemenuhanTindakLanjut;
	}
	public void setTglPemenuhanTindakLanjut(String tglPemenuhanTindakLanjut) {
		this.tglPemenuhanTindakLanjut = tglPemenuhanTindakLanjut;
	}
	public String getHasilObservasi() {
		return hasilObservasi;
	}
	public void setHasilObservasi(String hasilObservasi) {
		this.hasilObservasi = hasilObservasi;
	}
	public String getRekomendasi() {
		return rekomendasi;
	}
	public void setRekomendasi(String rekomendasi) {
		this.rekomendasi = rekomendasi;
	}
	public String getKeterangan() {
		return keterangan;
	}
	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getStatusCompliance() {
		return statusCompliance;
	}
	public void setStatusCompliance(String statusCompliance) {
		this.statusCompliance = statusCompliance;
	}
	public Integer getCloseFlag() {
		return closeFlag;
	}
	public void setCloseFlag(Integer closeFlag) {
		this.closeFlag = closeFlag;
	}
	
	
}