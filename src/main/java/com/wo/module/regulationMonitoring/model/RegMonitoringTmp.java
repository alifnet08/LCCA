package com.wo.module.regulationMonitoring.model;

import java.io.Serializable;
import java.util.List;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.counterType.model.CounterType;
import com.wo.module.parameter.model.ParameterDetail;

public class RegMonitoringTmp extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long regMonitoringTmpId;
	private ParameterDetail jenisKetentuan;
	private String followUp;
	private String notes;
	private CounterType counterType;
	
	private String reminderStatus;
	private String status;
	
	private Long delId;
	
	private List<RegMonitoringRegulationTmp> regMonitoringRegulationTmps;
	private List<RegMonitoringPICComplianceTmp> regMonitoringPICComplianceTmps;
	private List<RegMonitoringPICFollowUpTmp> regMonitoringPICFollowUpTmps;
	private List<RegMonitoringApprovalTmp> regMonitoringApprovalTmps;

	public Long getRegMonitoringTmpId() {
		return regMonitoringTmpId;
	}

	public void setRegMonitoringTmpId(Long regMonitoringTmpId) {
		this.regMonitoringTmpId = regMonitoringTmpId;
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

	public List<RegMonitoringRegulationTmp> getRegMonitoringRegulationTmps() {
		return regMonitoringRegulationTmps;
	}

	public void setRegMonitoringRegulationTmps(List<RegMonitoringRegulationTmp> regMonitoringRegulationTmps) {
		this.regMonitoringRegulationTmps = regMonitoringRegulationTmps;
	}

	public List<RegMonitoringPICComplianceTmp> getRegMonitoringPICComplianceTmps() {
		return regMonitoringPICComplianceTmps;
	}

	public void setRegMonitoringPICComplianceTmps(List<RegMonitoringPICComplianceTmp> regMonitoringPICComplianceTmps) {
		this.regMonitoringPICComplianceTmps = regMonitoringPICComplianceTmps;
	}

	public List<RegMonitoringPICFollowUpTmp> getRegMonitoringPICFollowUpTmps() {
		return regMonitoringPICFollowUpTmps;
	}

	public void setRegMonitoringPICFollowUpTmps(List<RegMonitoringPICFollowUpTmp> regMonitoringPICFollowUpTmps) {
		this.regMonitoringPICFollowUpTmps = regMonitoringPICFollowUpTmps;
	}

	public List<RegMonitoringApprovalTmp> getRegMonitoringApprovalTmps() {
		return regMonitoringApprovalTmps;
	}

	public void setRegMonitoringApprovalTmps(List<RegMonitoringApprovalTmp> regMonitoringApprovalTmps) {
		this.regMonitoringApprovalTmps = regMonitoringApprovalTmps;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}