package com.wo.module.complianceTestingMockup.vo;

import java.io.Serializable;
import java.util.Date;

public class PICTindakLanjutVO implements Serializable {
	private static final long serialVersionUID = 3019374791490640764L;

	private String nik;
	private String name;
	private String tindakLanjut;
	private Date targetDate;
	private int sequence;
	private Long divisionId;
	
	public String getNik() {
		return nik;
	}
	public void setNik(String nik) {
		this.nik = nik;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getTargetDate() {
		return targetDate;
	}
	public void setTargetDate(Date targetDate) {
		this.targetDate = targetDate;
	}
	public int getSequence() {
		return sequence;
	}
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getTindakLanjut() {
		return tindakLanjut;
	}
	public void setTindakLanjut(String tindakLanjut) {
		this.tindakLanjut = tindakLanjut;
	}
	public Long getDivisionId() {
		return divisionId;
	}
	public void setDivisionId(Long divisionId) {
		this.divisionId = divisionId;
	}
	
	
	
	
}