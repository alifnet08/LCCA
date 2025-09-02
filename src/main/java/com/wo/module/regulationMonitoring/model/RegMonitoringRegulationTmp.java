package com.wo.module.regulationMonitoring.model;

import java.io.Serializable;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.externalRegulation.model.Regulation;

public class RegMonitoringRegulationTmp extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 6175683384870425900L;
	
	private Long regMonitoringRegulationTmpId;
	private RegMonitoringTmp regMonitoringTmp;
	private Regulation regulation;
	
	private String primaryFlag;
	private Long delId;

	private Integer sequence;
	private Long regulationLinkId;
	private String regulationLinkName;
	
	public Long getRegMonitoringRegulationTmpId() {
		return regMonitoringRegulationTmpId;
	}
	public void setRegMonitoringRegulationTmpId(Long regMonitoringRegulationTmpId) {
		this.regMonitoringRegulationTmpId = regMonitoringRegulationTmpId;
	}
	public RegMonitoringTmp getRegMonitoringTmp() {
		return regMonitoringTmp;
	}
	public void setRegMonitoringTmp(RegMonitoringTmp regMonitoringTmp) {
		this.regMonitoringTmp = regMonitoringTmp;
	}
	public Regulation getRegulation() {
		return regulation;
	}
	public void setRegulation(Regulation regulation) {
		this.regulation = regulation;
	}
	public String getPrimaryFlag() {
		return primaryFlag;
	}
	public void setPrimaryFlag(String primaryFlag) {
		this.primaryFlag = primaryFlag;
	}
	public Long getDelId() {
		return delId;
	}
	public void setDelId(Long delId) {
		this.delId = delId;
	}
	public Integer getSequence() {
		return sequence;
	}
	public void setSequence(Integer sequence) {
		this.sequence = sequence;
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
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}