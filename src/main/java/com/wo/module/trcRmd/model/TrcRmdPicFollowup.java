package com.wo.module.trcRmd.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.user.model.User;

public class TrcRmdPicFollowup extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 4633417012047526315L;

	private Long rmdPicFollowupId;
	private TrcRmd trcRmd;

	private Date targetDate;
	
	private ParameterDetail followupStatus;
	private User followupBy;
	private Date confirmationDate;
	private Date followupDate;
	private String followupNote;

	private List<TrcRmdPicFollowupAttachment> picFollowupAttachmentDetails = new ArrayList<TrcRmdPicFollowupAttachment>();
	
	private String pic1;
	private String pic2;
	private String pic3;
	
	private String followupStatusName;
	private String followupStatusNameEn;
	private String followupStatusNameIn;

	private String targetDateStr;
	private String followupDateStr;
	
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

	public Date getTargetDate() {
		return targetDate;
	}

	public void setTargetDate(Date targetDate) {
		this.targetDate = targetDate;
	}

	public ParameterDetail getFollowupStatus() {
		return followupStatus;
	}

	public void setFollowupStatus(ParameterDetail followupStatus) {
		this.followupStatus = followupStatus;
		
		/*if (followupStatus != null && followupStatus.getParameterDtlId() != null
			&& followupStatus.getName() != null) {
				setFollowupStatusNameEn(followupStatus.getNameEn());
				setFollowupStatusNameIn(followupStatus.getNameIn());
			}*/
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

	public List<TrcRmdPicFollowupAttachment> getPicFollowupAttachmentDetails() {
		return picFollowupAttachmentDetails;
	}

	public void setPicFollowupAttachmentDetails(List<TrcRmdPicFollowupAttachment> picFollowupAttachmentDetails) {
		this.picFollowupAttachmentDetails = picFollowupAttachmentDetails;
	}

	public String getPic1() {
		return pic1;
	}

	public void setPic1(String pic1) {
		this.pic1 = pic1;
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

	public String getFollowupDateStr() {
		return followupDateStr;
	}

	public void setFollowupDateStr(String followupDateStr) {
		this.followupDateStr = followupDateStr;
	}

	public void setFollowupStatusName(String followupStatusName) {
		this.followupStatusName = followupStatusName;
	}

	public String getTargetDateStr() {
		return targetDateStr;
	}

	public void setTargetDateStr(String targetDateStr) {
		this.targetDateStr = targetDateStr;
	}

	public TrcRmd getTrcRmd() {
		return trcRmd;
	}

	public void setTrcRmd(TrcRmd trcRmd) {
		this.trcRmd = trcRmd;
	}

	public Long getRmdPicFollowupId() {
		return rmdPicFollowupId;
	}

	public void setRmdPicFollowupId(Long rmdPicFollowupId) {
		this.rmdPicFollowupId = rmdPicFollowupId;
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
}