package com.wo.module.tmpFine.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.counterType.model.CounterType;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.regulationSocialization.vo.StatusConfirmationVO;
import com.wo.module.trcFineApproval.model.TrcFine;
import com.wo.module.user.model.User;

public class TmpFineSearchVo extends BaseEntity implements Serializable {
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
	
	private String statusCode;
	private String statusName;
	private String statusNameIn;
	private String statusNameEn;
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

	private String letterReceivedDateStr;
	private String letterDateStr;
	private String confirmationDateStr;
	private String followDateStr;
	private String fullfillmentDateStr;
	private String targetDateStr;
	
	private String perihalName;
	private String supportingUnitName;
	private String supportingUnit1;
	private String supportingUnit2;
	private String supportingUnit3;
	
	private String picAttendeeName;
	
	private List<StatusConfirmationVO> statusList;
	
	private TrcFine trcFine;


	public TmpFineSearchVo() {
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

	public String getLetterReceivedDateStr() {
		return letterReceivedDateStr;
	}

	public void setLetterReceivedDateStr(String letterReceivedDateStr) {
		this.letterReceivedDateStr = letterReceivedDateStr;
	}

	public String getLetterDateStr() {
		return letterDateStr;
	}

	public void setLetterDateStr(String letterDateStr) {
		this.letterDateStr = letterDateStr;
	}

	public String getConfirmationDateStr() {
		return confirmationDateStr;
	}

	public void setConfirmationDateStr(String confirmationDateStr) {
		this.confirmationDateStr = confirmationDateStr;
	}

	public String getFollowDateStr() {
		return followDateStr;
	}

	public void setFollowDateStr(String followDateStr) {
		this.followDateStr = followDateStr;
	}

	public String getFullfillmentDateStr() {
		return fullfillmentDateStr;
	}

	public void setFullfillmentDateStr(String fullfillmentDateStr) {
		this.fullfillmentDateStr = fullfillmentDateStr;
	}

	public List<StatusConfirmationVO> getStatusList() {
		return statusList;
	}

	public void setStatusList(List<StatusConfirmationVO> statusList) {
		this.statusList = statusList;
	}

	public String getTargetDateStr() {
		return targetDateStr;
	}

	public void setTargetDateStr(String targetDateStr) {
		this.targetDateStr = targetDateStr;
	}

	@SuppressWarnings("static-access")
	public String getPerihalName() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if (locale != null && locale.equals(locale.ENGLISH)) {
			perihalName = perihalEn;
		} else {
			perihalName = perihalIn;
		}
		return perihalName;
	}

	public void setPerihalName(String perihalName) {
		this.perihalName = perihalName;
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

	public String getSupportingUnitName() {
		return supportingUnitName;
	}

	public void setSupportingUnitName(String supportingUnitName) {
		this.supportingUnitName = supportingUnitName;
	}

	public String getPicAttendeeName() {
		return picAttendeeName;
	}

	public void setPicAttendeeName(String picAttendeeName) {
		this.picAttendeeName = picAttendeeName;
	}

	public TrcFine getTrcFine() {
		return trcFine;
	}

	public void setTrcFine(TrcFine trcFine) {
		this.trcFine = trcFine;
	}
	
	
	
	
}
