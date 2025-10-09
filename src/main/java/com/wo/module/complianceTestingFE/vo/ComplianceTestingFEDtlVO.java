package com.wo.module.complianceTestingFE.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;

import com.wo.module.tmpComplianceReview.vo.StatusConfirmationVO;

public class ComplianceTestingFEDtlVO  implements Serializable {
		
	private static final long serialVersionUID = 1031961290076567131L;

	private Long complianceTestingId;
	private String inspectionTitle;
	private String inspectionNo;
	private String startDate;
	private String endDate;
	private String notes;
	
	private String followupStatusCd;
	private String targetDate;
	private Long complianceTestingPICFollowupId;
	private String picName1;
	private String picName2;
	private String picName3;
	private String statusIn;
	
	public Long getComplianceTestingId() {
		return complianceTestingId;
	}
	public void setComplianceTestingId(Long complianceTestingId) {
		this.complianceTestingId = complianceTestingId;
	}
	public String getInspectionTitle() {
		return inspectionTitle;
	}
	public void setInspectionTitle(String inspectionTitle) {
		this.inspectionTitle = inspectionTitle;
	}
	public String getInspectionNo() {
		return inspectionNo;
	}
	public void setInspectionNo(String inspectionNo) {
		this.inspectionNo = inspectionNo;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public String getFollowupStatusCd() {
		return followupStatusCd;
	}
	public void setFollowupStatusCd(String followupStatusCd) {
		this.followupStatusCd = followupStatusCd;
	}
	public String getTargetDate() {
		return targetDate;
	}
	public void setTargetDate(String targetDate) {
		this.targetDate = targetDate;
	}
	
	public Long getComplianceTestingPICFollowupId() {
		return complianceTestingPICFollowupId;
	}
	public void setComplianceTestingPICFollowupId(Long complianceTestingPICFollowupId) {
		this.complianceTestingPICFollowupId = complianceTestingPICFollowupId;
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
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getStatusIn() {
		return statusIn;
	}
	public void setStatusIn(String statusIn) {
		this.statusIn = statusIn;
	}
	
	

	
	
}
