package com.wo.module.complianceTestingMockup.vo;

import java.io.Serializable;

public class SubjectVO implements Serializable {
	private static final long serialVersionUID = 3019374791490640764L;

	private Boolean checked;
	private String subjectPemeriksaan;
	private String rating;
	private String ratingKeseluruhan;
	private String keterangan;
	private String rekomendasi;
	private String hasilObservasi;
	
	public Boolean getChecked() {
		return checked;
	}
	public void setChecked(Boolean checked) {
		this.checked = checked;
	}
	public String getSubjectPemeriksaan() {
		return subjectPemeriksaan;
	}
	public void setSubjectPemeriksaan(String subjectPemeriksaan) {
		this.subjectPemeriksaan = subjectPemeriksaan;
	}
	public String getRating() {
		return rating;
	}
	public void setRating(String rating) {
		this.rating = rating;
	}
	public String getRatingKeseluruhan() {
		return ratingKeseluruhan;
	}
	public void setRatingKeseluruhan(String ratingKeseluruhan) {
		this.ratingKeseluruhan = ratingKeseluruhan;
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
	public String getRekomendasi() {
		return rekomendasi;
	}
	public void setRekomendasi(String rekomendasi) {
		this.rekomendasi = rekomendasi;
	}
	public String getHasilObservasi() {
		return hasilObservasi;
	}
	public void setHasilObservasi(String hasilObservasi) {
		this.hasilObservasi = hasilObservasi;
	}
	
	
	
	
	
}