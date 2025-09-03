package com.wo.module.trcCorrespondence.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;

import org.apache.commons.lang3.StringUtils;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.common.util.EncryptUtils;
import com.wo.module.counterType.model.CounterType;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.rc.model.RC;
import com.wo.module.user.model.User;

public class TrcCorrespondence extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = 2706615543694303593L;
	private Long correspondenceId;
	private ParameterDetail sender;
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
	private ParameterDetail followupStatus;
	private ParameterDetail picFollowupStatus;
	private User followupBy;
	private Date confirmationDate;
	private Date followupDate;
	private String followupNote;
	private ParameterDetail complianceStatus;
	private String complianceNote;
	private User complianceBy;
	private Date complianceDate;
	
	private List<TrcCorrespondenceDocument> trcCorrespondenceDocuments;
	private List<TrcCorrespondencePicCompliance> trcCorrespondencePicCompliances;
	private List<TrcCorrespondencePicFollowupAttachment> trcCorrespondencePicFollowupAttachments;
	private List<TrcCorrespondencePicFollowupEmail> trcCorrespondencePicFollowupEmails;
	private List<TrcCorrespondenceSupportingUnit> trcCorrespondenceSupportingUnits;
	private List<TrcCrpdcPicConfirm> trcCrpdcPicConfirms;
	private List<TrcCrpdcReffLetter> trcCrpdcReffLetters;
	
	private List<TrcCorrespondencePicFollowupAttendance> trcCorrespondencePicFollowupAttendance;
	
	private String senderName;
	private String senderNameIn;
	private String senderNameEn;
	private String userNameTemp1;
	private String userNameTemp2;
	private String userNameTemp3;
	private String statusCode;
	private String statusName;
	private String statusNameIn;
	private String statusNameEn;
	private String followupStatusCode;
	private String followupStatusName;
	private String followupStatusNameEn;
	private String followupStatusNameIn;
	
	private String picConfirmationName;
	private Date fulfillmentFollowupDate;
	private String complianceCheckerStatus;
	private String complianceCheckerStatusIn;
	private String complianceCheckerStatusEn;
	
	private String picName1;
	private String picName2;
	private String picName3;
	
	private String perihal;
	private String userMaker;
	private String userSpv;
	private String noErr;
	
	private ParameterDetail attendance;
	private ParameterDetail correspondenceCode;
	
	private RC rc;
	
	private Date fineDebitted;
	
	private Long fineAmount;
	
	private String breaches;
	
	private String rootCause;
	
	private String category;
	
	public TrcCorrespondence() {
		super();
	}

	public Long getCorrespondenceId() {
		return correspondenceId;
	}

	public void setCorrespondenceId(Long correspondenceId) {
		this.correspondenceId = correspondenceId;
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

	public String getFollowupNote() {
		return followupNote;
	}

	public void setFollowupNote(String followupNote) {
		this.followupNote = followupNote;
	}

	public ParameterDetail getComplianceStatus() {
		return complianceStatus;
	}

	public void setComplianceStatus(ParameterDetail complianceStatus) {
		this.complianceStatus = complianceStatus;
	}

	public String getComplianceNote() {
		return complianceNote;
	}

	public void setComplianceNote(String complianceNote) {
		this.complianceNote = complianceNote;
	}

	public User getComplianceBy() {
		return complianceBy;
	}

	public void setComplianceBy(User complianceBy) {
		this.complianceBy = complianceBy;
	}

	public Date getComplianceDate() {
		return complianceDate;
	}

	public void setComplianceDate(Date complianceDate) {
		this.complianceDate = complianceDate;
	}

	public List<TrcCorrespondenceDocument> getTrcCorrespondenceDocuments() {
		return trcCorrespondenceDocuments;
	}

	public void setTrcCorrespondenceDocuments(List<TrcCorrespondenceDocument> trcCorrespondenceDocuments) {
		this.trcCorrespondenceDocuments = trcCorrespondenceDocuments;
	}

	public List<TrcCorrespondencePicCompliance> getTrcCorrespondencePicCompliances() {
		return trcCorrespondencePicCompliances;
	}

	public void setTrcCorrespondencePicCompliances(List<TrcCorrespondencePicCompliance> trcCorrespondencePicCompliances) {
		this.trcCorrespondencePicCompliances = trcCorrespondencePicCompliances;
	}

	public List<TrcCorrespondencePicFollowupEmail> getTrcCorrespondencePicFollowupEmails() {
		return trcCorrespondencePicFollowupEmails;
	}

	public void setTrcCorrespondencePicFollowupEmails(
			List<TrcCorrespondencePicFollowupEmail> trcCorrespondencePicFollowupEmails) {
		this.trcCorrespondencePicFollowupEmails = trcCorrespondencePicFollowupEmails;
	}

	public List<TrcCorrespondenceSupportingUnit> getTrcCorrespondenceSupportingUnits() {
		return trcCorrespondenceSupportingUnits;
	}

	public void setTrcCorrespondenceSupportingUnits(
			List<TrcCorrespondenceSupportingUnit> trcCorrespondenceSupportingUnits) {
		this.trcCorrespondenceSupportingUnits = trcCorrespondenceSupportingUnits;
	}

	public List<TrcCorrespondencePicFollowupAttachment> getTrcCorrespondencePicFollowupAttachments() {
		return trcCorrespondencePicFollowupAttachments;
	}

	public void setTrcCorrespondencePicFollowupAttachments(List<TrcCorrespondencePicFollowupAttachment> trcCorrespondencePicFollowupAttachments) {
		this.trcCorrespondencePicFollowupAttachments = trcCorrespondencePicFollowupAttachments;
	}

	public ParameterDetail getSender() {
		return sender;
	}

	public void setSender(ParameterDetail sender) {
		this.sender = sender;
	}

	public ParameterDetail getReminderStatus() {
		return reminderStatus;
	}

	public void setReminderStatus(ParameterDetail reminderStatus) {
		this.reminderStatus = reminderStatus;
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

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
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

	@SuppressWarnings("static-access")
	public String getPerihal() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if (locale != null && locale.equals(locale.ENGLISH)) {
			perihal = perihalEn;
		} else {
			perihal = perihalIn;
		}
		return perihal;
	}

	public void setPerihal(String perihal) {
		this.perihal = perihal;
	}

//	public String getCorrespondenceType() {
//		return correspondenceType;
//	}
//
//	public void setCorrespondenceType(String correspondenceType) {
//		this.correspondenceType = correspondenceType;
//	}

	public ParameterDetail getAttendance() {
		return attendance;
	}

	public void setAttendance(ParameterDetail attendance) {
		this.attendance = attendance;
	}

	public List<TrcCorrespondencePicFollowupAttendance> getTrcCorrespondencePicFollowupAttendance() {
		return trcCorrespondencePicFollowupAttendance;
	}

	public void setTrcCorrespondencePicFollowupAttendance(List<TrcCorrespondencePicFollowupAttendance> trcCorrespondencePicFollowupAttendance) {
		this.trcCorrespondencePicFollowupAttendance = trcCorrespondencePicFollowupAttendance;
	}

	public ParameterDetail getCorrespondenceCode() {
		return correspondenceCode;
	}

	public void setCorrespondenceCode(ParameterDetail correspondenceCode) {
		this.correspondenceCode = correspondenceCode;
	}
	
	public String getNotesDecrypted() {
		if (StringUtils.isNotBlank(notes)) {
			return EncryptUtils.base64decode(notes);
		}
		return notes;
	}

	public RC getRc() {
		return rc;
	}

	public void setRc(RC rc) {
		this.rc = rc;
	}

	public Date getFineDebitted() {
		return fineDebitted;
	}

	public void setFineDebitted(Date fineDebitted) {
		this.fineDebitted = fineDebitted;
	}

	public Long getFineAmount() {
		return fineAmount;
	}

	public void setFineAmount(Long fineAmount) {
		this.fineAmount = fineAmount;
	}

	public String getBreaches() {
		return breaches;
	}

	public void setBreaches(String breaches) {
		this.breaches = breaches;
	}

	public String getRootCause() {
		return rootCause;
	}

	public void setRootCause(String rootCause) {
		this.rootCause = rootCause;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getUserMaker() {
		return userMaker;
	}

	public void setUserMaker(String userMaker) {
		this.userMaker = userMaker;
	}

	public String getUserSpv() {
		return userSpv;
	}

	public void setUserSpv(String userSpv) {
		this.userSpv = userSpv;
	}

	public String getNoErr() {
		return noErr;
	}

	public void setNoErr(String noErr) {
		this.noErr = noErr;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public ParameterDetail getPicFollowupStatus() {
		return picFollowupStatus;
	}

	public void setPicFollowupStatus(ParameterDetail picFollowupStatus) {
		this.picFollowupStatus = picFollowupStatus;
	}

	public List<TrcCrpdcPicConfirm> getTrcCrpdcPicConfirms() {
		return trcCrpdcPicConfirms;
	}

	public void setTrcCrpdcPicConfirms(List<TrcCrpdcPicConfirm> trcCrpdcPicConfirms) {
		this.trcCrpdcPicConfirms = trcCrpdcPicConfirms;
	}

	public List<TrcCrpdcReffLetter> getTrcCrpdcReffLetters() {
		return trcCrpdcReffLetters;
	}

	public void setTrcCrpdcReffLetters(List<TrcCrpdcReffLetter> trcCrpdcReffLetters) {
		this.trcCrpdcReffLetters = trcCrpdcReffLetters;
	}
	
	
	
}
