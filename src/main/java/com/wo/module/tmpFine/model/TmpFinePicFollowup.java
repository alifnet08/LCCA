package com.wo.module.tmpFine.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.rc.model.RC;
import com.wo.module.user.model.Division;
import com.wo.module.user.model.User;

public class TmpFinePicFollowup extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 6140880530217262281L;
	private Long finePicFollowupId;
	private TmpFine tmpFine;
	private RC rc;
	private Date fineDebitted;
	private Long fineAmount;
	private String breaches;
	private String rootCause;
	private String category;
	private Long divisionId;
	private User userId1;
	private User userId2;
	private User userId3;
	private Date targetDate;
	private Date targetResponseDate;

	private String rescheduleReason;
	private ParameterDetail followupStatus;
	
	private List<TmpFinePicFollowupEmail> tmpFinePicFollowupEmails;
	private List<TmpFinePicFollowupReschedule> TmpFinePicFollowupReschedules;
	
	private int sequence;

	public TmpFinePicFollowup() {
		super();
	}

	public Long getFinePicFollowupId() {
		return finePicFollowupId;
	}

	public void setFinePicFollowupId(Long finePicFollowupId) {
		this.finePicFollowupId = finePicFollowupId;
	}

	public TmpFine getTmpFine() {
		return tmpFine;
	}

	public void setTmpFine(TmpFine tmpFine) {
		this.tmpFine = tmpFine;
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

	
	public Long getDivisionId() {
		return divisionId;
	}

	public void setDivisionId(Long divisionId) {
		this.divisionId = divisionId;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Date getTargetDate() {
		return targetDate;
	}

	public void setTargetDate(Date targetDate) {
		this.targetDate = targetDate;
	}

	public String getRescheduleReason() {
		return rescheduleReason;
	}

	public void setRescheduleReason(String rescheduleReason) {
		this.rescheduleReason = rescheduleReason;
	}

	public List<TmpFinePicFollowupEmail> getTmpFinePicFollowupEmails() {
		return tmpFinePicFollowupEmails;
	}

	public void setTmpFinePicFollowupEmails(List<TmpFinePicFollowupEmail> tmpFinePicFollowupEmails) {
		this.tmpFinePicFollowupEmails = tmpFinePicFollowupEmails;
	}

	public List<TmpFinePicFollowupReschedule> getTmpFinePicFollowupReschedules() {
		return TmpFinePicFollowupReschedules;
	}

	public void setTmpFinePicFollowupReschedules(List<TmpFinePicFollowupReschedule> tmpFinePicFollowupReschedules) {
		TmpFinePicFollowupReschedules = tmpFinePicFollowupReschedules;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public ParameterDetail getFollowupStatus() {
		return followupStatus;
	}

	public void setFollowupStatus(ParameterDetail followupStatus) {
		this.followupStatus = followupStatus;
	}

	public Date getTargetResponseDate() {
		return targetResponseDate;
	}

	public void setTargetResponseDate(Date targetResponseDate) {
		this.targetResponseDate = targetResponseDate;
	}

	
	

}
