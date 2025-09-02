package com.wo.module.tmpRmd.vo;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Locale;

import javax.faces.context.FacesContext;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.counterType.model.CounterType;
import com.wo.module.reportType.model.ReportType;
import com.wo.module.user.model.User;

public class TmpRmdSearchVo extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 3019374791490640764L;

	private Long rmdId;

	private ReportType reportType;
	private String nik;
	private String reportNameIn;
	private String reportNameEn;
	private String description;
	private CounterType counterType;
	private String divisionId;
	private User user1;
	private User user2;
	private User user3;

	private Timestamp targetDate;
	private String dedicatedTo;
	private String sanctions;
	private String note;

	private String reminderStatus;
	private String status;

	private String reportName;
	private String reportTypeName;
	private String reportTypeNameEn;
	private String reportTypeNameIn;
	private String pic1;
	private String statusName;
	private String statusNameEn;
	private String statusNameIn;

	private String followUpPicStatus;

	public Long getRmdId() {
		return rmdId;
	}

	public void setRmdId(Long rmdId) {
		this.rmdId = rmdId;
	}

	public ReportType getReportType() {
		return reportType;
	}

	public void setReportType(ReportType reportType) {
		this.reportType = reportType;
	}

	public String getNik() {
		return nik;
	}

	public void setNik(String nik) {
		this.nik = nik;
	}

	public String getReportNameIn() {
		return reportNameIn;
	}

	public void setReportNameIn(String reportNameIn) {
		this.reportNameIn = reportNameIn;
	}

	public String getReportNameEn() {
		return reportNameEn;
	}

	public void setReportNameEn(String reportNameEn) {
		this.reportNameEn = reportNameEn;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public CounterType getCounterType() {
		return counterType;
	}

	public void setCounterType(CounterType counterType) {
		this.counterType = counterType;
	}

	public String getDivisionId() {
		return divisionId;
	}

	public void setDivisionId(String divisionId) {
		this.divisionId = divisionId;
	}

	public User getUser1() {
		return user1;
	}

	public void setUser1(User user1) {
		this.user1 = user1;
	}

	public User getUser2() {
		return user2;
	}

	public void setUser2(User user2) {
		this.user2 = user2;
	}

	public User getUser3() {
		return user3;
	}

	public void setUser3(User user3) {
		this.user3 = user3;
	}

	public Timestamp getTargetDate() {
		return targetDate;
	}

	public void setTargetDate(Timestamp targetDate) {
		this.targetDate = targetDate;
	}

	public String getDedicatedTo() {
		return dedicatedTo;
	}

	public void setDedicatedTo(String dedicatedTo) {
		this.dedicatedTo = dedicatedTo;
	}

	public String getSanctions() {
		return sanctions;
	}

	public void setSanctions(String sanctions) {
		this.sanctions = sanctions;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
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

	@SuppressWarnings("static-access")
	public String getReportName() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if (locale != null && locale.equals(locale.ENGLISH)) {
			reportName = reportNameEn;
		} else {
			reportName = reportNameIn;
		}

		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	@SuppressWarnings("static-access")
	public String getReportTypeName() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if (locale != null && locale.equals(locale.ENGLISH)) {
			reportTypeName = reportTypeNameEn;
		} else {
			reportTypeName = reportTypeNameIn;
		}
		return reportTypeName;
	}

	public void setReportTypeName(String reportTypeName) {
		this.reportTypeName = reportTypeName;
	}

	public String getPic1() {
		return pic1;
	}

	public void setPic1(String pic1) {
		this.pic1 = pic1;
	}

	public String getFollowUpPicStatus() {
		return followUpPicStatus;
	}

	public void setFollowUpPicStatus(String followUpPicStatus) {
		this.followUpPicStatus = followUpPicStatus;
	}

	public String getReportTypeNameEn() {
		return reportTypeNameEn;
	}

	public void setReportTypeNameEn(String reportTypeNameEn) {
		this.reportTypeNameEn = reportTypeNameEn;
	}

	public String getReportTypeNameIn() {
		return reportTypeNameIn;
	}

	public void setReportTypeNameIn(String reportTypeNameIn) {
		this.reportTypeNameIn = reportTypeNameIn;
	}

	@SuppressWarnings("static-access")
	public String getStatusName() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if (locale != null && locale.equals(locale.ENGLISH)) {
			statusName = statusNameEn;
		} else {
			statusName = statusNameIn;
		}
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getStatusNameEn() {
		return statusNameEn;
	}

	public void setStatusNameEn(String statusNameEn) {
		this.statusNameEn = statusNameEn;
	}

	public String getStatusNameIn() {
		return statusNameIn;
	}

	public void setStatusNameIn(String statusNameIn) {
		this.statusNameIn = statusNameIn;
	}

}