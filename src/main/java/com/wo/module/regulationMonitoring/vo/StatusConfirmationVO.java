package com.wo.module.regulationMonitoring.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;

import com.wo.module.regulationMonitoring.model.RegMonitoringPICFollowUpAttachmentTrc;

public class StatusConfirmationVO  implements Serializable {
	private static final long serialVersionUID = 6395152164269025885L;
	
	private String pic1;
	private String pic2;
	private String pic3;
	private String evidence;
	private String followupStatus;
	private String followupStatusIn;
	private String followupStatusEn;
	private String picConfirmation;
	private String confirmationDate;
	private String followupDate;
	private String followupNote;
	private String complianceDate;
	private String complianceStatus;
	private String complianceStatusIn;
	private String complianceStatusEn;
	private String complianceNote;
	private String followupBy;
	private String makerFollowupNote;
	
	private String namePic;
	private Long picFollowupId;
	private String targetDate;
	private String followupStatusCd;
	private List<RegMonitoringPICFollowUpAttachmentTrc> regMonitoringPICFollowUpAttachmentTrcs;
	
	private String picNameHadir;
	
	public String getEvidence() {
		return evidence;
	}
	public void setEvidence(String evidence) {
		this.evidence = evidence;
	}
	@SuppressWarnings("static-access")
	public String getFollowupStatus() {
		
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if (locale != null && locale.equals(locale.ENGLISH)) {
			followupStatus = followupStatusEn;
		} else {
			followupStatus = followupStatusIn;
		}
		
		return followupStatus;
	}
	public void setFollowupStatus(String followupStatus) {
		this.followupStatus = followupStatus;
	}
	public String getPicConfirmation() {
		return picConfirmation;
	}
	public void setPicConfirmation(String picConfirmation) {
		this.picConfirmation = picConfirmation;
	}
	public String getConfirmationDate() {
		return confirmationDate;
	}
	public void setConfirmationDate(String confirmationDate) {
		this.confirmationDate = confirmationDate;
	}
	public String getFollowupDate() {
		return followupDate;
	}
	public void setFollowupDate(String followupDate) {
		this.followupDate = followupDate;
	}
	public String getFollowupNote() {
		return followupNote;
	}
	public void setFollowupNote(String followupNote) {
		this.followupNote = followupNote;
	}
	public String getComplianceDate() {
		return complianceDate;
	}
	public void setComplianceDate(String complianceDate) {
		this.complianceDate = complianceDate;
	}
	@SuppressWarnings("static-access")
	public String getComplianceStatus() {
		
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if (locale != null && locale.equals(locale.ENGLISH)) {
			complianceStatus = complianceStatusEn;
		} else {
			complianceStatus = complianceStatusIn;
		}
		
		return complianceStatus;
	}
	public void setComplianceStatus(String complianceStatus) {
		this.complianceStatus = complianceStatus;
	}
	public String getComplianceNote() {
		return complianceNote;
	}
	public void setComplianceNote(String complianceNote) {
		this.complianceNote = complianceNote;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public String getFollowupBy() {
		return followupBy;
	}
	public void setFollowupBy(String followupBy) {
		this.followupBy = followupBy;
	}
	public String getPic1() {
		return pic1;
	}
	public void setPic1(String pic1) {
		this.pic1 = pic1;
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
	public String getFollowupStatusIn() {
		return followupStatusIn;
	}
	public void setFollowupStatusIn(String followupStatusIn) {
		this.followupStatusIn = followupStatusIn;
	}
	public String getFollowupStatusEn() {
		return followupStatusEn;
	}
	public void setFollowupStatusEn(String followupStatusEn) {
		this.followupStatusEn = followupStatusEn;
	}
	public String getComplianceStatusIn() {
		return complianceStatusIn;
	}
	public void setComplianceStatusIn(String complianceStatusIn) {
		this.complianceStatusIn = complianceStatusIn;
	}
	public String getComplianceStatusEn() {
		return complianceStatusEn;
	}
	public void setComplianceStatusEn(String complianceStatusEn) {
		this.complianceStatusEn = complianceStatusEn;
	}
	public String getNamePic() {
		return namePic;
	}
	public void setNamePic(String namePic) {
		this.namePic = namePic;
	}
	public Long getPicFollowupId() {
		return picFollowupId;
	}
	public void setPicFollowupId(Long picFollowupId) {
		this.picFollowupId = picFollowupId;
	}
	public String getTargetDate() {
		return targetDate;
	}
	public void setTargetDate(String targetDate) {
		this.targetDate = targetDate;
	}
	public String getFollowupStatusCd() {
		return followupStatusCd;
	}
	public void setFollowupStatusCd(String followupStatusCd) {
		this.followupStatusCd = followupStatusCd;
	}
	public String getPicNameHadir() {
		return picNameHadir;
	}
	public void setPicNameHadir(String picNameHadir) {
		this.picNameHadir = picNameHadir;
	}
	public String getMakerFollowupNote() {
		return makerFollowupNote;
	}
	public void setMakerFollowupNote(String makerFollowupNote) {
		this.makerFollowupNote = makerFollowupNote;
	}
	public List<RegMonitoringPICFollowUpAttachmentTrc> getRegMonitoringPICFollowUpAttachmentTrcs() {
		return regMonitoringPICFollowUpAttachmentTrcs;
	}
	public void setRegMonitoringPICFollowUpAttachmentTrcs(List<RegMonitoringPICFollowUpAttachmentTrc> regMonitoringPICFollowUpAttachmentTrcs) {
		this.regMonitoringPICFollowUpAttachmentTrcs = regMonitoringPICFollowUpAttachmentTrcs;
	}
}