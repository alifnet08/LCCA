package com.wo.module.report.reportRegulation.vo;

import java.io.Serializable;

public class RegulationTrackRecordVo implements Serializable{

	private static final long serialVersionUID = -1421020710105627586L;
	
	private Long regulationTrackRecordId;
	private Long regulationId;
	private String trackCode;
	private Long regulationLinkId;
	private String regulationLinkName;
	private String trackNote;
	
	private String trackName;
	private String regulationNoPrev;
	private String regulationNamePrev;
	
	public Long getRegulationTrackRecordId() {
		return regulationTrackRecordId;
	}
	public void setRegulationTrackRecordId(Long regulationTrackRecordId) {
		this.regulationTrackRecordId = regulationTrackRecordId;
	}
	public Long getRegulationId() {
		return regulationId;
	}
	public void setRegulationId(Long regulationId) {
		this.regulationId = regulationId;
	}
	public String getTrackCode() {
		return trackCode;
	}
	public void setTrackCode(String trackCode) {
		this.trackCode = trackCode;
	}
	public Long getRegulationLinkId() {
		return regulationLinkId;
	}
	public void setRegulationLinkId(Long regulationLinkId) {
		this.regulationLinkId = regulationLinkId;
	}
	public String getRegulationLinkName() {
		return regulationLinkName;
	}
	public void setRegulationLinkName(String regulationLinkName) {
		this.regulationLinkName = regulationLinkName;
	}
	public String getTrackNote() {
		return trackNote;
	}
	public void setTrackNote(String trackNote) {
		this.trackNote = trackNote;
	}
	public String getTrackName() {
		return trackName;
	}
	public void setTrackName(String trackName) {
		this.trackName = trackName;
	}
	public String getRegulationNoPrev() {
		return regulationNoPrev;
	}
	public void setRegulationNoPrev(String regulationNoPrev) {
		this.regulationNoPrev = regulationNoPrev;
	}
	public String getRegulationNamePrev() {
		return regulationNamePrev;
	}
	public void setRegulationNamePrev(String regulationNamePrev) {
		this.regulationNamePrev = regulationNamePrev;
	}
	

	
}
