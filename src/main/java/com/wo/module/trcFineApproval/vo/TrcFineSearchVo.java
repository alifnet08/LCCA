package com.wo.module.trcFineApproval.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.Locale;

import javax.faces.context.FacesContext;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.counterType.model.CounterType;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.trcFineApproval.model.TrcFine;
import com.wo.module.user.model.User;

public class TrcFineSearchVo extends BaseEntity implements Serializable {
	private static final long serialVersionUID = -4056586582044466709L;
	private Long fineId;
	private ParameterDetail senderCode;
	private Date letterReceivedDate;
	private String letterNo;
	private Date letterDate;
	private String perihalIn;
	private String perihalEn;
	private String letterSummary;
	private String followUp;
	private String notes;
	private Long divisionId;
	private User userId1;
	private User userId2;
	private User userId3;
	private Date targetDate;
	private CounterType counterType;
	private ParameterDetail reminderStatus;
	private ParameterDetail status;

	private String senderName;
	private String senderNameIn;
	private String senderNameEn;
	private String userNameTemp1;
	private String userNameTemp2;
	private String userNameTemp3;
	private String statusNameIn;
	private String statusNameEn;
	private String statusName;
	private String statusCode;
	private String followupStatusCode;
	private String followupStatusName;
	private String followupStatusNameEn;
	private String followupStatusNameIn;
	
	private String picConfirmationName;
	private Date confirmationDate;
	
	private Date followupDate;
	private String followupNote;
	private Date fulfillmentFollowupDate;
	private String complianceCheckerStatus;
	private String complianceCheckerStatusIn;
	private String complianceCheckerStatusEn;
	
	private String picName1;
	private String picName2;
	private String picName3;
	
	private String complianceNote;
	
	private String fineType;
	
	private TrcFine trcFine;

	public TrcFineSearchVo() {
		super();
	}

	public Long getFineId() {
		return fineId;
	}

	public void setFineId(Long fineId) {
		this.fineId = fineId;
	}

	public ParameterDetail getSenderCode() {
		return senderCode;
	}

	public void setSenderCode(ParameterDetail senderCode) {
		this.senderCode = senderCode;
	}

	public Date getLetterReceivedDate() {
		return letterReceivedDate;
	}

	public void setLetterReceivedDate(Date letterReceivedDate) {
		this.letterReceivedDate = letterReceivedDate;
	}

	public String getLetterNo() {
		return letterNo;
	}

	public void setLetterNo(String letterNo) {
		this.letterNo = letterNo;
	}

	public Date getLetterDate() {
		return letterDate;
	}

	public void setLetterDate(Date letterDate) {
		this.letterDate = letterDate;
	}

	public String getPerihalIn() {
		return perihalIn;
	}

	public void setPerihalIn(String perihalIn) {
		this.perihalIn = perihalIn;
	}

	public String getPerihalEn() {
		return perihalEn;
	}

	public void setPerihalEn(String perihalEn) {
		this.perihalEn = perihalEn;
	}

	public String getLetterSummary() {
		return letterSummary;
	}

	public void setLetterSummary(String letterSummary) {
		this.letterSummary = letterSummary;
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

	public Long getDivisionId() {
		return divisionId;
	}

	public void setDivisionId(Long divisionId) {
		this.divisionId = divisionId;
	}

	public Date getTargetDate() {
		return targetDate;
	}

	public void setTargetDate(Date targetDate) {
		this.targetDate = targetDate;
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

	public CounterType getCounterType() {
		return counterType;
	}

	public void setCounterType(CounterType counterType) {
		this.counterType = counterType;
	}
	
	public User getUserId1() {
		return userId1;
	}

	public void setUserId1(User userId1) {
		this.userId1 = userId1;
	}

	public User getUserId2() {
		return userId2;
	}

	public void setUserId2(User userId2) {
		this.userId2 = userId2;
	}

	public User getUserId3() {
		return userId3;
	}

	public void setUserId3(User userId3) {
		this.userId3 = userId3;
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

	@SuppressWarnings("static-access")
	public String getSenderName() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if (locale != null && locale.equals(locale.ENGLISH)) {
			senderName = senderNameEn;
		} else {
			senderName = senderNameIn;
		}
		
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
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

	public String getSenderNameIn() {
		return senderNameIn;
	}

	public void setSenderNameIn(String senderNameIn) {
		this.senderNameIn = senderNameIn;
	}

	public String getSenderNameEn() {
		return senderNameEn;
	}

	public void setSenderNameEn(String senderNameEn) {
		this.senderNameEn = senderNameEn;
	}

	public String getStatusNameIn() {
		return statusNameIn;
	}

	public void setStatusNameIn(String statusNameIn) {
		this.statusNameIn = statusNameIn;
	}

	public String getStatusNameEn() {
		return statusNameEn;
	}

	public void setStatusNameEn(String statusNameEn) {
		this.statusNameEn = statusNameEn;
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

	public String getFollowupNote() {
		return followupNote;
	}

	public void setFollowupNote(String followupNote) {
		this.followupNote = followupNote;
	}

	public Date getFulfillmentFollowupDate() {
		return fulfillmentFollowupDate;
	}

	public void setFulfillmentFollowupDate(Date fulfillmentFollowupDate) {
		this.fulfillmentFollowupDate = fulfillmentFollowupDate;
	}

	@SuppressWarnings("static-access")
	public String getComplianceCheckerStatus() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if (locale != null && locale.equals(locale.ENGLISH)) {
			complianceCheckerStatus = complianceCheckerStatusEn;
		} else {
			complianceCheckerStatus = complianceCheckerStatusIn;
		}
		return complianceCheckerStatus;
	
	}

	public void setComplianceCheckerStatus(String complianceCheckerStatus) {
		this.complianceCheckerStatus = complianceCheckerStatus;
	}

	public String getComplianceCheckerStatusIn() {
		return complianceCheckerStatusIn;
	}

	public void setComplianceCheckerStatusIn(String complianceCheckerStatusIn) {
		this.complianceCheckerStatusIn = complianceCheckerStatusIn;
	}

	public String getComplianceCheckerStatusEn() {
		return complianceCheckerStatusEn;
	}

	public void setComplianceCheckerStatusEn(String complianceCheckerStatusEn) {
		this.complianceCheckerStatusEn = complianceCheckerStatusEn;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getFollowupStatusCode() {
		return followupStatusCode;
	}

	public void setFollowupStatusCode(String followupStatusCode) {
		this.followupStatusCode = followupStatusCode;
	}

	public String getPicName1() {
		return picName1;
	}

	public void setPicName1(String picName1) {
		this.picName1 = picName1;
	}

	public String getPicName2() {
		return picName2;
	}

	public void setPicName2(String picName2) {
		this.picName2 = picName2;
	}

	public String getPicName3() {
		return picName3;
	}

	public void setPicName3(String picName3) {
		this.picName3 = picName3;
	}

	public String getComplianceNote() {
		return complianceNote;
	}

	public void setComplianceNote(String complianceNote) {
		this.complianceNote = complianceNote;
	}

	public String getFineType() {
		return fineType;
	}

	public void setFineType(String fineType) {
		this.fineType = fineType;
	}

	public TrcFine getTrcFine() {
		return trcFine;
	}

	public void setTrcFine(TrcFine trcFine) {
		this.trcFine = trcFine;
	}
	
	
}
