package com.wo.module.regulationMonitoring.model;

import java.io.Serializable;
import java.util.List;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.counterType.model.CounterType;
import com.wo.module.parameter.model.ParameterDetail;

public class RegMonitoringTrc extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long regMonitoringTrcId;
	private ParameterDetail jenisKetentuan;
	private String followUp;
	private String notes;
	private CounterType counterType;
	
	private String reminderStatus;
	private String status;
	
	private Long delId;

	private List<RegMonitoringRegulationTrc> regMonitoringRegulationTrcs;
	private List<RegMonitoringPICComplianceTrc> regMonitoringPICComplianceTrcs;
	private List<RegMonitoringPICFollowUpTrc> regMonitoringPICFollowUpTrcs;
	
	public Long getRegMonitoringTrcId() {
		return regMonitoringTrcId;
	}

	public void setRegMonitoringTrcId(Long regMonitoringTrcId) {
		this.regMonitoringTrcId = regMonitoringTrcId;
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

	public List<RegMonitoringRegulationTrc> getRegMonitoringRegulationTrcs() {
		return regMonitoringRegulationTrcs;
	}

	public void setRegMonitoringRegulationTrcs(List<RegMonitoringRegulationTrc> regMonitoringRegulationTrcs) {
		this.regMonitoringRegulationTrcs = regMonitoringRegulationTrcs;
	}

	public List<RegMonitoringPICComplianceTrc> getRegMonitoringPICComplianceTrcs() {
		return regMonitoringPICComplianceTrcs;
	}

	public void setRegMonitoringPICComplianceTrcs(List<RegMonitoringPICComplianceTrc> regMonitoringPICComplianceTrcs) {
		this.regMonitoringPICComplianceTrcs = regMonitoringPICComplianceTrcs;
	}

	public List<RegMonitoringPICFollowUpTrc> getRegMonitoringPICFollowUpTrcs() {
		return regMonitoringPICFollowUpTrcs;
	}

	public void setRegMonitoringPICFollowUpTrcs(List<RegMonitoringPICFollowUpTrc> regMonitoringPICFollowUpTrcs) {
		this.regMonitoringPICFollowUpTrcs = regMonitoringPICFollowUpTrcs;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}