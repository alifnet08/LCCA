package com.wo.module.tmpCorrespondence.model;

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

public class TmpCorrespondence extends BaseEntity implements Serializable {
	private static final long serialVersionUID = -4056586582044466709L;
	private Long correspondenceId;
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
	private Long userDivisionId;

	private List<TmpCorrespondenceApproval> tmpCorrespondenceApprovals;
	private List<TmpCorrespondenceDocument> tmpCorrespondenceDocuments;
	private List<TmpCorrespondencePicCompliance> tmpCorrespondencePicCompliances;
	private List<TmpCorrespondencePicFollowupEmail> tmpCorrespondencePicFollowupEmails;
	private List<TmpCorrespondencePicFollowupReschedule> tmpCorrespondencePicFollowupReschedules;
	private List<TmpCorrespondenceSupportingUnit> tmpCorrespondenceSupportingUnits;

	private String userNameTemp1;
	private String userNameTemp2;
	private String userNameTemp3;
	private String perihal;
	private String userNameInputer;
	private String userNameAtasanInputer;

	private Date oldTargetDate;

	private ParameterDetail correspondenceCode;
	
	private RC rc;
	
	private Date fineDebitted;
	
	private Long fineAmount;
	
	private String breaches;
	
	private String rootCause;
	
	private String category;
	
	

	public TmpCorrespondence() {
		super();
	}

	public Long getCorrespondenceId() {
		return correspondenceId;
	}

	public void setCorrespondenceId(Long correspondenceId) {
		this.correspondenceId = correspondenceId;
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

	public List<TmpCorrespondenceApproval> getTmpCorrespondenceApprovals() {
		return tmpCorrespondenceApprovals;
	}

	public void setTmpCorrespondenceApprovals(List<TmpCorrespondenceApproval> tmpCorrespondenceApprovals) {
		this.tmpCorrespondenceApprovals = tmpCorrespondenceApprovals;
	}

	public List<TmpCorrespondenceDocument> getTmpCorrespondenceDocuments() {
		return tmpCorrespondenceDocuments;
	}

	public void setTmpCorrespondenceDocuments(List<TmpCorrespondenceDocument> tmpCorrespondenceDocuments) {
		this.tmpCorrespondenceDocuments = tmpCorrespondenceDocuments;
	}

	public List<TmpCorrespondencePicCompliance> getTmpCorrespondencePicCompliances() {
		return tmpCorrespondencePicCompliances;
	}

	public void setTmpCorrespondencePicCompliances(
			List<TmpCorrespondencePicCompliance> tmpCorrespondencePicCompliances) {
		this.tmpCorrespondencePicCompliances = tmpCorrespondencePicCompliances;
	}

	public List<TmpCorrespondencePicFollowupEmail> getTmpCorrespondencePicFollowupEmails() {
		return tmpCorrespondencePicFollowupEmails;
	}

	public void setTmpCorrespondencePicFollowupEmails(
			List<TmpCorrespondencePicFollowupEmail> tmpCorrespondencePicFollowupEmails) {
		this.tmpCorrespondencePicFollowupEmails = tmpCorrespondencePicFollowupEmails;
	}

	public List<TmpCorrespondencePicFollowupReschedule> getTmpCorrespondencePicFollowupReschedules() {
		return tmpCorrespondencePicFollowupReschedules;
	}

	public void setTmpCorrespondencePicFollowupReschedules(
			List<TmpCorrespondencePicFollowupReschedule> tmpCorrespondencePicFollowupReschedules) {
		this.tmpCorrespondencePicFollowupReschedules = tmpCorrespondencePicFollowupReschedules;
	}

	public List<TmpCorrespondenceSupportingUnit> getTmpCorrespondenceSupportingUnits() {
		return tmpCorrespondenceSupportingUnits;
	}

	public void setTmpCorrespondenceSupportingUnits(
			List<TmpCorrespondenceSupportingUnit> tmpCorrespondenceSupportingUnits) {
		this.tmpCorrespondenceSupportingUnits = tmpCorrespondenceSupportingUnits;
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

	public Date getOldTargetDate() {
		return oldTargetDate;
	}

	public void setOldTargetDate(Date oldTargetDate) {
		this.oldTargetDate = oldTargetDate;
	}

	public ParameterDetail getCorrespondenceCode() {
		return correspondenceCode;
	}

	public void setCorrespondenceCode(ParameterDetail correspondenceCode) {
		this.correspondenceCode = correspondenceCode;
	}

	public String getNotesEncrypted() {
		if (StringUtils.isNotBlank(notes)) {
			return EncryptUtils.base64encode(notes);
		}
		return notes;
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

	public Long getUserDivisionId() {
		return userDivisionId;
	}

	public void setUserDivisionId(Long userDivisionId) {
		this.userDivisionId = userDivisionId;
	}
	
	
}