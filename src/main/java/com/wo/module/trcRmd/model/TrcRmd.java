package com.wo.module.trcRmd.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.counterType.model.CounterType;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.reportType.model.ReportType;
import com.wo.module.user.model.User;

public class TrcRmd extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 3019374791490640764L;

	private Long rmdId;

	private ReportType reportType;
	private String nik;
	private String reportNameIn;
	private String reportNameEn;
	private String description;
	private CounterType counterType;
	private Long divisionId;
	private User user1;
	private User user2;
	private User user3;

	private Date targetDate;
	private String dedicatedTo;
	private String sanctions;
	private String note;

	private ParameterDetail reminderStatus;
	private ParameterDetail status;

	private ParameterDetail followupStatus;
	private User followupBy;
	private Date confirmationDate;
	private Date followupDate;
	private String followupNote;

	private List<TrcRmdDueDate> dueDateDetails = new ArrayList<TrcRmdDueDate>();
	private List<TrcRmdRegulation> regulationDetails = new ArrayList<TrcRmdRegulation>();
	private List<TrcRmdSupportingUnit> supportingUnitDetails = new ArrayList<TrcRmdSupportingUnit>();
	private List<TrcRmdPicFollowup> picFollowupDetails = new ArrayList<TrcRmdPicFollowup>();
	private List<TrcRmdPicFollowupEmail> picFollowupEmailDetails = new ArrayList<TrcRmdPicFollowupEmail>();
	private List<TrcRmdCorrespondence> correspondenceDetails = new ArrayList<TrcRmdCorrespondence>();

	private String reportName;
	private String reportTypeName;
	private String reportTypeNameEn;
	private String reportTypeNameIn;
	private String pic1;
	private String statusCode;
	private String statusName;
	private String statusNameEn;
	private String statusNameIn;

	private String followupStatusName;
	private String followupStatusNameEn;
	private String followupStatusNameIn;

	private String picConfirmationName;

	private String userNameTemp1;
	private String userNameTemp2;
	private String userNameTemp3;

	private String targetDateStr;
	private String followupDateStr;
	
	private String recurringType;
	private Date recurringStartDate;
	private Date recurringEndDate;

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

	public Long getDivisionId() {
		return divisionId;
	}

	public void setDivisionId(Long divisionId) {
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

	public Date getTargetDate() {
		return targetDate;
	}

	public void setTargetDate(Date targetDate) {
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

	public ParameterDetail getReminderStatus() {
		return reminderStatus;
	}

	public void setReminderStatus(ParameterDetail reminderStatus) {
		this.reminderStatus = reminderStatus;
	}

	public ParameterDetail getStatus() {
		return status;
	}

	public void setStatus(ParameterDetail status) {
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

	/*
	 * public List<TmpRmdDueDate> getDueDateDetails() { return dueDateDetails; }
	 * 
	 * public void setDueDateDetails(List<TmpRmdDueDate> dueDateDetails) {
	 * this.dueDateDetails = dueDateDetails; }
	 * 
	 * public List<TmpRmdRegulation> getRegulationDetails() { return
	 * regulationDetails; }
	 * 
	 * public void setRegulationDetails(List<TmpRmdRegulation> regulationDetails) {
	 * this.regulationDetails = regulationDetails; }
	 * 
	 * public List<TmpRmdSupportingUnit> getSupportingUnitDetails() { return
	 * supportingUnitDetails; }
	 * 
	 * public void setSupportingUnitDetails(List<TmpRmdSupportingUnit>
	 * supportingUnitDetails) { this.supportingUnitDetails = supportingUnitDetails;
	 * }
	 */

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

	@SuppressWarnings("static-access")
	public String getFollowupStatusName() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if (locale != null && locale.equals(locale.ENGLISH)) {
			followupStatusName = followupStatusNameEn;
		} else {
			followupStatusName = followupStatusNameIn;
		}
		return followupStatusName;
	}

	public void setFollowupStatusName(String followupStatusName) {
		this.followupStatusName = followupStatusName;
	}

	public String getFollowupStatusNameEn() {
		return followupStatusNameEn;
	}

	public void setFollowupStatusNameEn(String followupStatusNameEn) {
		this.followupStatusNameEn = followupStatusNameEn;
	}

	public String getFollowupStatusNameIn() {
		return followupStatusNameIn;
	}

	public void setFollowupStatusNameIn(String followupStatusNameIn) {
		this.followupStatusNameIn = followupStatusNameIn;
	}

	public String getPicConfirmationName() {
		return picConfirmationName;
	}

	public void setPicConfirmationName(String picConfirmationName) {
		this.picConfirmationName = picConfirmationName;
	}

	public ParameterDetail getFollowupStatus() {
		return followupStatus;
	}

	public void setFollowupStatus(ParameterDetail followupStatus) {
		this.followupStatus = followupStatus;
	}

	public User getFollowupBy() {
		return followupBy;
	}

	public void setFollowupBy(User followupBy) {
		this.followupBy = followupBy;
	}

	public Date getConfirmationDate() {
		return confirmationDate;
	}

	public void setConfirmationDate(Date confirmationDate) {
		this.confirmationDate = confirmationDate;
	}

	public Date getFollowupDate() {
		return followupDate;
	}

	public void setFollowupDate(Date followupDate) {
		this.followupDate = followupDate;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getUserNameTemp1() {
		return userNameTemp1;
	}

	public void setUserNameTemp1(String userNameTemp1) {
		this.userNameTemp1 = userNameTemp1;
	}

	public String getUserNameTemp2() {
		return userNameTemp2;
	}

	public void setUserNameTemp2(String userNameTemp2) {
		this.userNameTemp2 = userNameTemp2;
	}

	public String getUserNameTemp3() {
		return userNameTemp3;
	}

	public void setUserNameTemp3(String userNameTemp3) {
		this.userNameTemp3 = userNameTemp3;
	}

	public String getTargetDateStr() {
		return targetDateStr;
	}

	public void setTargetDateStr(String targetDateStr) {
		this.targetDateStr = targetDateStr;
	}

	public List<TrcRmdDueDate> getDueDateDetails() {
		return dueDateDetails;
	}

	public void setDueDateDetails(List<TrcRmdDueDate> dueDateDetails) {
		this.dueDateDetails = dueDateDetails;
	}

	public List<TrcRmdRegulation> getRegulationDetails() {
		return regulationDetails;
	}

	public void setRegulationDetails(List<TrcRmdRegulation> regulationDetails) {
		this.regulationDetails = regulationDetails;
	}

	public List<TrcRmdSupportingUnit> getSupportingUnitDetails() {
		return supportingUnitDetails;
	}

	public void setSupportingUnitDetails(List<TrcRmdSupportingUnit> supportingUnitDetails) {
		this.supportingUnitDetails = supportingUnitDetails;
	}

	public String getFollowupDateStr() {
		return followupDateStr;
	}

	public void setFollowupDateStr(String followupDateStr) {
		this.followupDateStr = followupDateStr;
	}

	public List<TrcRmdPicFollowupEmail> getPicFollowupEmailDetails() {
		return picFollowupEmailDetails;
	}

	public void setPicFollowupEmailDetails(List<TrcRmdPicFollowupEmail> picFollowupEmailDetails) {
		this.picFollowupEmailDetails = picFollowupEmailDetails;
	}

	public String getFollowupNote() {
		return followupNote;
	}

	public void setFollowupNote(String followupNote) {
		this.followupNote = followupNote;
	}

	public List<TrcRmdPicFollowup> getPicFollowupDetails() {
		return picFollowupDetails;
	}

	public void setPicFollowupDetails(List<TrcRmdPicFollowup> picFollowupDetails) {
		this.picFollowupDetails = picFollowupDetails;
	}

	public List<TrcRmdCorrespondence> getCorrespondenceDetails() {
		return correspondenceDetails;
	}

	public void setCorrespondenceDetails(List<TrcRmdCorrespondence> correspondenceDetails) {
		this.correspondenceDetails = correspondenceDetails;
	}

	public String getRecurringType() {
		return recurringType;
	}

	public void setRecurringType(String recurringType) {
		this.recurringType = recurringType;
	}

	public Date getRecurringStartDate() {
		return recurringStartDate;
	}

	public void setRecurringStartDate(Date recurringStartDate) {
		this.recurringStartDate = recurringStartDate;
	}

	public Date getRecurringEndDate() {
		return recurringEndDate;
	}

	public void setRecurringEndDate(Date recurringEndDate) {
		this.recurringEndDate = recurringEndDate;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
}