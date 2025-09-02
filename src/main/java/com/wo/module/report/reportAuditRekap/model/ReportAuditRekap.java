package com.wo.module.report.reportAuditRekap.model;

import java.util.Locale;

import javax.faces.context.FacesContext;

public class ReportAuditRekap {

	private String typeAuditIn;
	private String typeAuditEn;
	private Integer totalAudit;
	private Integer tindakLanjutYes;
	private Integer tindakLanjutNo;
	private Integer inProgress;
	private Integer closed;
	private Integer meetSla;
	private Integer beforeSla;
	private Integer overSla;
	
	private String auditTemplateNameIn;
	private String auditTemplateNameEn;
	
	// helper
	private String typeAuditName;

	public String getTypeAuditIn() {
		return typeAuditIn;
	}

	public void setTypeAuditIn(String typeAuditIn) {
		this.typeAuditIn = typeAuditIn;
	}

	public String getTypeAuditEn() {
		return typeAuditEn;
	}

	public void setTypeAuditEn(String typeAuditEn) {
		this.typeAuditEn = typeAuditEn;
	}

	public Integer getTotalAudit() {
		return totalAudit;
	}

	public void setTotalAudit(Integer totalAudit) {
		this.totalAudit = totalAudit;
	}

	public Integer getTindakLanjutYes() {
		return tindakLanjutYes;
	}

	public void setTindakLanjutYes(Integer tindakLanjutYes) {
		this.tindakLanjutYes = tindakLanjutYes;
	}

	public Integer getTindakLanjutNo() {
		return tindakLanjutNo;
	}

	public void setTindakLanjutNo(Integer tindakLanjutNo) {
		this.tindakLanjutNo = tindakLanjutNo;
	}

	public Integer getInProgress() {
		return inProgress;
	}

	public void setInProgress(Integer inProgress) {
		this.inProgress = inProgress;
	}

	public Integer getClosed() {
		return closed;
	}

	public void setClosed(Integer closed) {
		this.closed = closed;
	}

	public Integer getMeetSla() {
		return meetSla;
	}

	public void setMeetSla(Integer meetSla) {
		this.meetSla = meetSla;
	}

	public Integer getBeforeSla() {
		return beforeSla;
	}

	public void setBeforeSla(Integer beforeSla) {
		this.beforeSla = beforeSla;
	}

	public Integer getOverSla() {
		return overSla;
	}

	public void setOverSla(Integer overSla) {
		this.overSla = overSla;
	}

	@SuppressWarnings("static-access")
	public String getTypeAuditName() {
		
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		
		if (locale != null && locale.equals(locale.ENGLISH)) {
			typeAuditName = typeAuditEn;
		} else {
			typeAuditName = typeAuditIn;
		}
		
		return typeAuditName;
	}

	public void setTypeAuditName(String typeAuditName) {
		this.typeAuditName = typeAuditName;
	}

	public String getAuditTemplateNameIn() {
		return auditTemplateNameIn;
	}

	public void setAuditTemplateNameIn(String auditTemplateNameIn) {
		this.auditTemplateNameIn = auditTemplateNameIn;
	}

	public String getAuditTemplateNameEn() {
		return auditTemplateNameEn;
	}

	public void setAuditTemplateNameEn(String auditTemplateNameEn) {
		this.auditTemplateNameEn = auditTemplateNameEn;
	}
}
