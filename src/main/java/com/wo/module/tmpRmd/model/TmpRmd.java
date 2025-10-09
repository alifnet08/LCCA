package com.wo.module.tmpRmd.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.counterType.model.CounterType;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.regulationSocialization.vo.StatusConfirmationVO;
import com.wo.module.reportType.model.ReportType;
import com.wo.module.trcRmd.model.TrcRmdPicFollowup;
import com.wo.module.user.model.User;

public class TmpRmd extends BaseEntity implements Serializable {
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
	private User picCompliance;
	private User picComplianceSuperior;

	private Date targetDate;
	private String dedicatedTo;
	private String sanctions;
	private String note;

	private ParameterDetail reminderStatus;
	private ParameterDetail status;

	private List<TmpRmdDueDate> dueDateDetails = new ArrayList<TmpRmdDueDate>();
	private List<TmpRmdRegulation> regulationDetails = new ArrayList<TmpRmdRegulation>();
	private List<TmpRmdCorrespondence> correspondenceDetails = new ArrayList<TmpRmdCorrespondence>();
	private List<TmpRmdSupportingUnit> supportingUnitDetails = new ArrayList<TmpRmdSupportingUnit>();
	private List<TmpRmdPicFollowupEmail> picFollowupEmailDetails = new ArrayList<TmpRmdPicFollowupEmail>();
	private List<TmpRmdPicFollowupReschedule> picFollowupRescheduleDetails = new ArrayList<TmpRmdPicFollowupReschedule>();
	private List<TmpRmdApproval> approvalDetails = new ArrayList<TmpRmdApproval>();
	private List<TrcRmdPicFollowup> picFollowupDetails = new ArrayList<TrcRmdPicFollowup>();
	
	private String reportName;
	private String reportTypeName;
	private String reportTypeNameEn;
	private String reportTypeNameIn;
	private String pic1;
	private String pic2;
	private String pic3;
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
	
	private String userNameTemp1;
	private String userNameTemp2;
	private String userNameTemp3;
	
	private String picComplianceTemp;
	private String picComplianceSuperiorTemp;
	
	private String targetDateStr;
	private String emailDateStr;
	
	private String followupStatusCode;
	private String namePIC;
	private String supportingUnitName;
	private String supportingUnit1;
	private String supportingUnit2;
	private String supportingUnit3;
	private String reportDescription;
	
	private String userNameInputer;
	private String userNameAtasanInputer;

	
	private String recurringType;
	private Date recurringStartDate;
	private Date recurringEndDate;
	
	private List<StatusConfirmationVO> statusList;
	
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
	
	
	public User getPicCompliance() {
		return picCompliance;
	}

	public void setPicCompliance(User picCompliance) {
		this.picCompliance = picCompliance;
	}
	
	public User getPicComplianceSuperior() {
		return picComplianceSuperior;
	}

	public void setPicComplianceSuperior(User picComplianceSuperior) {
		this.picComplianceSuperior = picComplianceSuperior;
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
	

	public String getPicComplianceTemp() {
		return picComplianceTemp;
	}

	public void setPicComplianceTemp(String picComplianceTemp) {
		this.picComplianceTemp = picComplianceTemp;
	}
	
	public String getPicComplianceSuperiorTemp() {
		return picComplianceSuperiorTemp;
	}

	public void setPicComplianceSuperiorTemp(String picComplianceSuperiorTemp) {
		this.picComplianceSuperiorTemp = picComplianceSuperiorTemp;
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

	public List<TmpRmdPicFollowupEmail> getPicFollowupEmailDetails() {
		return picFollowupEmailDetails;
	}

	public void setPicFollowupEmailDetails(List<TmpRmdPicFollowupEmail> picFollowupEmailDetails) {
		this.picFollowupEmailDetails = picFollowupEmailDetails;
	}

	public List<TmpRmdPicFollowupReschedule> getPicFollowupRescheduleDetails() {
		return picFollowupRescheduleDetails;
	}

	public void setPicFollowupRescheduleDetails(List<TmpRmdPicFollowupReschedule> picFollowupRescheduleDetails) {
		this.picFollowupRescheduleDetails = picFollowupRescheduleDetails;
	}

	public String getFollowupNote() {
		return followupNote;
	}

	public void setFollowupNote(String followupNote) {
		this.followupNote = followupNote;
	}

	public List<TmpRmdCorrespondence> getCorrespondenceDetails() {
		return correspondenceDetails;
	}

	public void setCorrespondenceDetails(List<TmpRmdCorrespondence> correspondenceDetails) {
		this.correspondenceDetails = correspondenceDetails;
	}

	public String getFollowupStatusCode() {
		return followupStatusCode;
	}

	public void setFollowupStatusCode(String followupStatusCode) {
		this.followupStatusCode = followupStatusCode;
	}

	public List<TrcRmdPicFollowup> getPicFollowupDetails() {
		return picFollowupDetails;
	}

	public void setPicFollowupDetails(List<TrcRmdPicFollowup> picFollowupDetails) {
		this.picFollowupDetails = picFollowupDetails;
	}

	public List<StatusConfirmationVO> getStatusList() {
		return statusList;
	}

	public void setStatusList(List<StatusConfirmationVO> statusList) {
		this.statusList = statusList;
	}

	public String getPic2() {
		return pic2;
	}

	public void setPic2(String pic2) {
		this.pic2 = pic2;
	}

	public String getPic3() {
		return pic3;
	}

	public void setPic3(String pic3) {
		this.pic3 = pic3;
	}

	public String getNamePIC() {
		return namePIC;
	}

	public void setNamePIC(String namePIC) {
		this.namePIC = namePIC;
	}

	public String getSupportingUnitName() {
		return supportingUnitName;
	}

	public void setSupportingUnitName(String supportingUnitName) {
		this.supportingUnitName = supportingUnitName;
	}

	public String getSupportingUnit1() {
		return supportingUnit1;
	}

	public void setSupportingUnit1(String supportingUnit1) {
		this.supportingUnit1 = supportingUnit1;
	}

	public String getSupportingUnit2() {
		return supportingUnit2;
	}

	public void setSupportingUnit2(String supportingUnit2) {
		this.supportingUnit2 = supportingUnit2;
	}

	public String getSupportingUnit3() {
		return supportingUnit3;
	}

	public void setSupportingUnit3(String supportingUnit3) {
		this.supportingUnit3 = supportingUnit3;
	}

	public String getReportDescription() {
		return reportDescription;
	}

	public void setReportDescription(String reportDescription) {
		this.reportDescription = reportDescription;
	}

	public String getRecurringType() {
		return recurringType;
	}

	public void setRecurringType(String recurringType) {
		this.recurringType = recurringType;
	}

	public Date getRecurringEndDate() {
		return recurringEndDate;
	}

	public void setRecurringEndDate(Date recurringEndDate) {
		this.recurringEndDate = recurringEndDate;
	}

	public Date getRecurringStartDate() {
		return recurringStartDate;
	}

	public void setRecurringStartDate(Date recurringStartDate) {
		this.recurringStartDate = recurringStartDate;
	}
	
	public String getUserNameInputer() {
		return userNameInputer;
	}
	
	public void setUserNameInputer(String userNameInputer) {
		this.userNameInputer = userNameInputer;
	}
	
	public String getUserNameAtasanInputer() {
		return userNameAtasanInputer;
	}
	
	public void setUserNameAtasanInputer(String userNameAtasanInputer) {
		this.userNameAtasanInputer = userNameAtasanInputer;
	}
	
	
	
}