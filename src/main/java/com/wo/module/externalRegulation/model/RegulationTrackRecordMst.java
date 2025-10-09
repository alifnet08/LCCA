package com.wo.module.externalRegulation.model;

import java.io.Serializable;

import com.wo.module.common.model.BaseEntity;

public class RegulationTrackRecordMst extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 6395152164269025885L;
	private Long regulationTrackRecordId;
	private RegulationMst regulationMst;
	private String trackCode;
	private Long regulationLinkId;
	private String trackNote;
	
	private Long regulationId;
	private String regulationLinkName;
	private String inActiveFlag;
	private Boolean inActive;
	private String regulationLinkIdEnc;
	
	private Long delId;
	
	private int sequence;
	
	private String trackName;
	
	public RegulationMst getRegulationMst() {
		return regulationMst;
	}

	public void setRegulationMst(RegulationMst regulationMst) {
		this.regulationMst = regulationMst;
	}

	public Long getRegulationTrackRecordId() {
		return regulationTrackRecordId;
	}

	public void setRegulationTrackRecordId(Long regulationTrackRecordId) {
		this.regulationTrackRecordId = regulationTrackRecordId;
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

	public String getTrackNote() {
		return trackNote;
	}

	public void setTrackNote(String trackNote) {
		this.trackNote = trackNote;
	}

	public Long getDelId() {
		return delId;
	}

	public void setDelId(Long delId) {
		this.delId = delId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getRegulationLinkName() {
		return regulationLinkName;
	}

	public void setRegulationLinkName(String regulationLinkName) {
		this.regulationLinkName = regulationLinkName;
	}

	public Long getRegulationId() {
		return regulationId;
	}

	public void setRegulationId(Long regulationId) {
		this.regulationId = regulationId;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public String getInActiveFlag() {
		return inActiveFlag;
	}

	public void setInActiveFlag(String inActiveFlag) {
		this.inActiveFlag = inActiveFlag;
		if (inActiveFlag != null && inActiveFlag.equals("Y"))
			setInActive(new Boolean(true));
		else
			setInActive(new Boolean(false));
	}

	public Boolean getInActive() {
		return inActive;
	}

	public void setInActive(Boolean inActive) {
		this.inActive = inActive;
	}

	public String getRegulationLinkIdEnc() {
		return regulationLinkIdEnc;
	}

	public void setRegulationLinkIdEnc(String regulationLinkIdEnc) {
		this.regulationLinkIdEnc = regulationLinkIdEnc;
	}

	public String getTrackName() {
		return trackName;
	}

	public void setTrackName(String trackName) {
		this.trackName = trackName;
	}
	
	
	
	
	
	
	
	
	
	

}
