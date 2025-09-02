package com.wo.module.tmpFine.model;

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

public class TmpFine extends BaseEntity implements Serializable {
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
	
	private CounterType counterType;
	private CounterType counterTypeResponse;
	private ParameterDetail reminderStatus;
	private ParameterDetail status;
	
	private ParameterDetail reportName;
	private Date targetSettlement;

	private List<TmpFineApproval> tmpFineApprovals;
	private List<TmpFineDocument> tmpFineDocuments;
	private List<TmpFinePicCompliance> tmpFinePicCompliances;
	private List<TmpFinePicFollowup> tmpFinePicFollowups;
	
	private String userNameTemp1;
	private String userNameTemp2;
	private String userNameTemp3;
	private String perihal;

	private Date oldTargetDate;

	private ParameterDetail fineCode;
	
	private RC rc;
	
	private Date fineDebitted;
	
	private Long fineAmount;
	
	private String breaches;
	
	private String rootCause;
	
	private String category;
	
	private String otherReportName;

	public TmpFine() {
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

	public List<TmpFineApproval> getTmpFineApprovals() {
		return tmpFineApprovals;
	}

	public void setTmpFineApprovals(List<TmpFineApproval> tmpFineApprovals) {
		this.tmpFineApprovals = tmpFineApprovals;
	}

	public List<TmpFineDocument> getTmpFineDocuments() {
		return tmpFineDocuments;
	}

	public void setTmpFineDocuments(List<TmpFineDocument> tmpFineDocuments) {
		this.tmpFineDocuments = tmpFineDocuments;
	}

	public List<TmpFinePicCompliance> getTmpFinePicCompliances() {
		return tmpFinePicCompliances;
	}

	public void setTmpFinePicCompliances(
			List<TmpFinePicCompliance> tmpFinePicCompliances) {
		this.tmpFinePicCompliances = tmpFinePicCompliances;
	}


	public List<TmpFinePicFollowup> getTmpFinePicFollowups() {
		return tmpFinePicFollowups;
	}

	public void setTmpFinePicFollowups(List<TmpFinePicFollowup> tmpFinePicFollowups) {
		this.tmpFinePicFollowups = tmpFinePicFollowups;
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

	public ParameterDetail getFineCode() {
		return fineCode;
	}

	public void setFineCode(ParameterDetail fineCode) {
		this.fineCode = fineCode;
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

	public ParameterDetail getReportName() {
		return reportName;
	}

	public void setReportName(ParameterDetail reportName) {
		this.reportName = reportName;
	}

	public Date getTargetSettlement() {
		return targetSettlement;
	}

	public void setTargetSettlement(Date targetSettlement) {
		this.targetSettlement = targetSettlement;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getOtherReportName() {
		return otherReportName;
	}

	public void setOtherReportName(String otherReportName) {
		this.otherReportName = otherReportName;
	}

	public CounterType getCounterTypeResponse() {
		return counterTypeResponse;
	}

	public void setCounterTypeResponse(CounterType counterTypeResponse) {
		this.counterTypeResponse = counterTypeResponse;
	}

	
	
}