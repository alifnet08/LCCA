package com.wo.module.internalRegulationPenerbitanReport.vo;

import java.io.Serializable;
import java.util.Date;

public class InternalRegulationPenerbitanPicTpkReportVo implements Serializable {

	private static final long serialVersionUID = -4612011836093757456L;

	private Long irgId;
	private Long irgPicId;
	private Long divisionId;

	private Date targetDate;

	private String reviewApproval;
	private String divisionName;
	private String picNik1;
	private String picName1;
	private String picNik2;
	private String picName2;
	private String picNik3;
	private String picName3;
	private String note;
	private String targetDateStr;
	private String reminderDateH10;

	public Long getIrgId() {
		return irgId;
	}

	public void setIrgId(Long irgId) {
		this.irgId = irgId;
	}

	public Long getIrgPicId() {
		return irgPicId;
	}

	public void setIrgPicId(Long irgPicId) {
		this.irgPicId = irgPicId;
	}

	public Date getTargetDate() {
		return targetDate;
	}

	public void setTargetDate(Date targetDate) {
		this.targetDate = targetDate;
	}

	public String getPicNik1() {
		return picNik1;
	}

	public void setPicNik1(String picNik1) {
		this.picNik1 = picNik1;
	}

	public String getPicName1() {
		return picName1;
	}

	public void setPicName1(String picName1) {
		this.picName1 = picName1;
	}

	public String getPicNik2() {
		return picNik2;
	}

	public void setPicNik2(String picNik2) {
		this.picNik2 = picNik2;
	}

	public String getPicName2() {
		return picName2;
	}

	public void setPicName2(String picName2) {
		this.picName2 = picName2;
	}

	public String getPicNik3() {
		return picNik3;
	}

	public void setPicNik3(String picNik3) {
		this.picNik3 = picNik3;
	}

	public String getPicName3() {
		return picName3;
	}

	public void setPicName3(String picName3) {
		this.picName3 = picName3;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Long getDivisionId() {
		return divisionId;
	}

	public void setDivisionId(Long divisionId) {
		this.divisionId = divisionId;
	}

	public String getReviewApproval() {
		return reviewApproval;
	}

	public void setReviewApproval(String reviewApproval) {
		this.reviewApproval = reviewApproval;
	}

	public String getDivisionName() {
		return divisionName;
	}

	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}

	public String getTargetDateStr() {
		return targetDateStr;
	}

	public void setTargetDateStr(String targetDateStr) {
		this.targetDateStr = targetDateStr;
	}

	public String getReminderDateH10() {
		return reminderDateH10;
	}

	public void setReminderDateH10(String reminderDateH10) {
		this.reminderDateH10 = reminderDateH10;
	}

}