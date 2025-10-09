package com.wo.module.regulationSocialization.model;

import java.io.Serializable;
import java.util.List;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.counterType.model.CounterType;
import com.wo.module.parameter.model.ParameterDetail;

public class SocializationTrc extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = -2170966789101349042L;
	
	private Long socializationId;
	private ParameterDetail jenisKetentuan;
	private String followUp;
	private String notes;
	private CounterType counterType;
	
	private String reminderStatus;
	private String status;
	
	private List<SocializationRegulationTrc> socializationRegulationTrcs;
	private List<SocializationPICComplianceTrc> socializationPICComplianceTrcs;
	private List<SocializationPICFollowupTrc> socializationPICFollowupTrcs;
	private List<SocializationDocumentTrc> socializationDocumentTrcs;
	
	
	private Long delId;

	public Long getSocializationId() {
		return socializationId;
	}

	public void setSocializationId(Long socializationId) {
		this.socializationId = socializationId;
	}

	public ParameterDetail getJenisKetentuan() {
		return jenisKetentuan;
	}

	public void setJenisKetentuan(ParameterDetail jenisKetentuan) {
		this.jenisKetentuan = jenisKetentuan;
	}

	public String getFollowUp() {
		return followUp;
	}

	public void setFollowUp(String followUp) {
		this.followUp = followUp;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public CounterType getCounterType() {
		return counterType;
	}

	public void setCounterType(CounterType counterType) {
		this.counterType = counterType;
	}


	public String getReminderStatus() {
		return reminderStatus;
	}

	public void setReminderStatus(String reminderStatus) {
		this.reminderStatus = reminderStatus;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public List<SocializationRegulationTrc> getSocializationRegulationTrcs() {
		return socializationRegulationTrcs;
	}

	public void setSocializationRegulationTrcs(List<SocializationRegulationTrc> socializationRegulationTrcs) {
		this.socializationRegulationTrcs = socializationRegulationTrcs;
	}

	public List<SocializationPICComplianceTrc> getSocializationPICComplianceTrcs() {
		return socializationPICComplianceTrcs;
	}

	public void setSocializationPICComplianceTrcs(List<SocializationPICComplianceTrc> socializationPICComplianceTrcs) {
		this.socializationPICComplianceTrcs = socializationPICComplianceTrcs;
	}

	public List<SocializationPICFollowupTrc> getSocializationPICFollowupTrcs() {
		return socializationPICFollowupTrcs;
	}

	public void setSocializationPICFollowupTrcs(List<SocializationPICFollowupTrc> socializationPICFollowupTrcs) {
		this.socializationPICFollowupTrcs = socializationPICFollowupTrcs;
	}

	public List<SocializationDocumentTrc> getSocializationDocumentTrcs() {
		return socializationDocumentTrcs;
	}

	public void setSocializationDocumentTrcs(List<SocializationDocumentTrc> socializationDocumentTrcs) {
		this.socializationDocumentTrcs = socializationDocumentTrcs;
	}

	
}
