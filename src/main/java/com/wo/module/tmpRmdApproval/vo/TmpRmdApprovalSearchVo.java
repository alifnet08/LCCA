package com.wo.module.tmpRmdApproval.vo;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.counterType.model.CounterType;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.reportType.model.ReportType;
import com.wo.module.tmpRmd.model.TmpRmdApproval;
import com.wo.module.tmpRmd.model.TmpRmdDueDate;
import com.wo.module.tmpRmd.model.TmpRmdRegulation;
import com.wo.module.tmpRmd.model.TmpRmdSupportingUnit;
import com.wo.module.user.model.User;

public class TmpRmdApprovalSearchVo extends BaseEntity implements Serializable {
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

	private Timestamp targetDate;
	private String dedicatedTo;
	private String sanctions;
	private String note;

	private ParameterDetail reminderStatus;
	private ParameterDetail status;

	private List<TmpRmdDueDate> dueDateDetails = new ArrayList<TmpRmdDueDate>();
	private List<TmpRmdRegulation> regulationDetails = new ArrayList<TmpRmdRegulation>();
	private List<TmpRmdSupportingUnit> supportingUnitDetails = new ArrayList<TmpRmdSupportingUnit>();
	private List<TmpRmdApproval> approvalDetails = new ArrayList<TmpRmdApproval>();

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
	
	private String reminderStatusName;
	private String reminderStatusNameEn;
	private String reminderStatusNameIn;


	private String picConfirmationName;
	private Date confirmationDate;
	
	private Date followupDate;
	private String followupNote;
	
	private String userIdTemp1;
	private String userIdTemp2;
	private String userIdTemp3;
	private String userNameTemp1;
	private String userNameTemp2;
	private String userNameTemp3;
	
	private String targetDateStr;
	private String emailDateStr;
	
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

	public List<TmpRmdDueDate> getDueDateDetails() {
		return dueDateDetails;
	}

	public void setDueDateDetails(List<TmpRmdDueDate> dueDateDetails) {
		this.dueDateDetails = dueDateDetails;
	}

	public List<TmpRmdRegulation> getRegulationDetails() {
		return regulationDetails;
	}

	public void setRegulationDetails(List<TmpRmdRegulation> regulationDetails) {
		this.regulationDetails = regulationDetails;
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

	public Date getFollowupDate() {
		return followupDate;
	}

	public void setFollowupDate(Date followupDate) {
		this.followupDate = followupDate;
	}

	public Date getConfirmationDate() {
		return confirmationDate;
	}

	public void setConfirmationDate(Date confirmationDate) {
		this.confirmationDate = confirmationDate;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public List<TmpRmdSupportingUnit> getSupportingUnitDetails() {
		return supportingUnitDetails;
	}

	public void setSupportingUnitDetails(List<TmpRmdSupportingUnit> supportingUnitDetails) {
		this.supportingUnitDetails = supportingUnitDetails;
	}

	public String getUserIdTemp1() {
		return userIdTemp1;
	}

	public void setUserIdTemp1(String userIdTemp1) {
		this.userIdTemp1 = userIdTemp1;
	}

	public String getUserIdTemp2() {
		return userIdTemp2;
	}

	public void setUserIdTemp2(String userIdTemp2) {
		this.userIdTemp2 = userIdTemp2;
	}

	public String getUserIdTemp3() {
		return userIdTemp3;
	}

	public void setUserIdTemp3(String userIdTemp3) {
		this.userIdTemp3 = userIdTemp3;
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

	public String getEmailDateStr() {
		return emailDateStr;
	}

	public void setEmailDateStr(String emailDateStr) {
		this.emailDateStr = emailDateStr;
	}

	public List<TmpRmdApproval> getApprovalDetails() {
		return approvalDetails;
	}

	public void setApprovalDetails(List<TmpRmdApproval> approvalDetails) {
		this.approvalDetails = approvalDetails;
	}

	@SuppressWarnings("static-access")
	public String getReminderStatusName() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if (locale != null && locale.equals(locale.ENGLISH)) {
			reminderStatusName = reminderStatusNameEn;
		} else {
			reminderStatusName = reminderStatusNameIn;
		}
		return reminderStatusName;
	}

	public void setReminderStatusName(String reminderStatusName) {
		this.reminderStatusName = reminderStatusName;
	}

	public String getReminderStatusNameEn() {
		return reminderStatusNameEn;
	}

	public void setReminderStatusNameEn(String reminderStatusNameEn) {
		this.reminderStatusNameEn = reminderStatusNameEn;
	}

	public String getReminderStatusNameIn() {
		return reminderStatusNameIn;
	}

	public void setReminderStatusNameIn(String reminderStatusNameIn) {
		this.reminderStatusNameIn = reminderStatusNameIn;
	}

	public String getFollowupNote() {
		return followupNote;
	}

	public void setFollowupNote(String followupNote) {
		this.followupNote = followupNote;
	}
	
	

}