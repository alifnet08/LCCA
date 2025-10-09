package com.wo.module.reportType.model;

import java.io.Serializable;
import java.util.Locale;

import javax.faces.context.FacesContext;

import com.wo.module.common.model.BaseEntity;

public class ReportType extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 6395152164269025885L;
	private Long reportTypeId;
	private String reportTypeIn;
	private String reportTypeEn;
	private String reportTypeDescription;
	private String dueDay;
	private String dueDate;
	private String dueMonth;
	private String dueYear;
	private Long delId;
	private Integer numberOfDueDate;
	
	private boolean dueDayBoolean;
	private boolean dueDateBoolean;
	private boolean dueMonthBoolean;
	private boolean dueYearBoolean;
	
	private String reportType;
	
	private String recurringType;
	
	public Long getReportTypeId() {
		return reportTypeId;
	}
	public void setReportTypeId(Long reportTypeId) {
		this.reportTypeId = reportTypeId;
	}
	
	
	public String getReportTypeIn() {
		return reportTypeIn;
	}
	public void setReportTypeIn(String reportTypeIn) {
		this.reportTypeIn = reportTypeIn;
	}
	public String getReportTypeEn() {
		return reportTypeEn;
	}
	public void setReportTypeEn(String reportTypeEn) {
		this.reportTypeEn = reportTypeEn;
	}
	
	public String getReportTypeDescription() {
		return reportTypeDescription;
	}
	public void setReportTypeDescription(String reportTypeDescription) {
		this.reportTypeDescription = reportTypeDescription;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Long getDelId() {
		return delId;
	}
	public void setDelId(Long delId) {
		this.delId = delId;
	}
	@SuppressWarnings("static-access")
	public String getReportType() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if(locale!=null && locale.equals(locale.ENGLISH)) {
			reportType = reportTypeEn;
		}else {
			reportType = reportTypeIn;
		}
		return reportType;
	}
	
	
	public void setReportType(String reportType) {
		this.reportType = reportType;
	}
	public String getDueDay() {
		return dueDay;
	}
	public void setDueDay(String dueDay) {
		this.dueDay = dueDay;
	}
	public String getDueDate() {
		return dueDate;
	}
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}
	public String getDueMonth() {
		return dueMonth;
	}
	public void setDueMonth(String dueMonth) {
		this.dueMonth = dueMonth;
	}
	public String getDueYear() {
		return dueYear;
	}
	public void setDueYear(String dueYear) {
		this.dueYear = dueYear;
	}
	public boolean isDueDayBoolean() {
		return dueDayBoolean;
	}
	public void setDueDayBoolean(boolean dueDayBoolean) {
		this.dueDayBoolean = dueDayBoolean;
	}
	public boolean isDueDateBoolean() {
		return dueDateBoolean;
	}
	public void setDueDateBoolean(boolean dueDateBoolean) {
		this.dueDateBoolean = dueDateBoolean;
	}
	public boolean isDueMonthBoolean() {
		return dueMonthBoolean;
	}
	public void setDueMonthBoolean(boolean dueMonthBoolean) {
		this.dueMonthBoolean = dueMonthBoolean;
	}
	public boolean isDueYearBoolean() {
		return dueYearBoolean;
	}
	public void setDueYearBoolean(boolean dueYearBoolean) {
		this.dueYearBoolean = dueYearBoolean;
	}
	public Integer getNumberOfDueDate() {
		return numberOfDueDate;
	}
	public void setNumberOfDueDate(Integer numberOfDueDate) {
		this.numberOfDueDate = numberOfDueDate;
	}
	public String getRecurringType() {
		return recurringType;
	}
	public void setRecurringType(String recurringType) {
		this.recurringType = recurringType;
	}
	
	
	

}
