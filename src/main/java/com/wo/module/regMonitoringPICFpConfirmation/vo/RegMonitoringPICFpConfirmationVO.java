package com.wo.module.regMonitoringPICFpConfirmation.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;

import com.wo.module.regulationMonitoring.vo.StatusConfirmationVO;


public class RegMonitoringPICFpConfirmationVO implements Serializable {
	private static final long serialVersionUID = 6395152164269025885L;
	private Long regMonitoringId;
	private String judulPeraturan;
	private String judulPeraturanIn;
	private String judulPeraturanEn;
	//private String pic;
	
	
	private String dueDate;
	private String status;
	private String statusIn;
	private String statusEn;
	
	private Long picFollowupId;
	private String picName1;
	private String picName2;
	private String picName3;
	private String complianceNote;

	private List<StatusConfirmationVO> statusList;
	
	private List<String> picNameList;

	@SuppressWarnings("static-access")
	public String getJudulPeraturan() {
		
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if (locale != null && locale.equals(locale.ENGLISH)) {
			judulPeraturan = judulPeraturanEn;
		} else {
			judulPeraturan = judulPeraturanIn;
		}
		
		return judulPeraturan;
	}

	public void setJudulPeraturan(String judulPeraturan) {
		this.judulPeraturan = judulPeraturan;
	}

	/*public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}*/

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	@SuppressWarnings("static-access")
	public String getStatus() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if (locale != null && locale.equals(locale.ENGLISH)) {
			status = statusEn;
		} else {
			status = statusIn;
		}
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<StatusConfirmationVO> getStatusList() {
		return statusList;
	}

	public void setStatusList(List<StatusConfirmationVO> statusList) {
		this.statusList = statusList;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getJudulPeraturanIn() {
		return judulPeraturanIn;
	}

	public void setJudulPeraturanIn(String judulPeraturanIn) {
		this.judulPeraturanIn = judulPeraturanIn;
	}

	public String getJudulPeraturanEn() {
		return judulPeraturanEn;
	}

	public void setJudulPeraturanEn(String judulPeraturanEn) {
		this.judulPeraturanEn = judulPeraturanEn;
	}

	public String getStatusIn() {
		return statusIn;
	}

	public void setStatusIn(String statusIn) {
		this.statusIn = statusIn;
	}

	public String getStatusEn() {
		return statusEn;
	}

	public void setStatusEn(String statusEn) {
		this.statusEn = statusEn;
	}

	public Long getPicFollowupId() {
		return picFollowupId;
	}

	public void setPicFollowupId(Long picFollowupId) {
		this.picFollowupId = picFollowupId;
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

	public List<String> getPicNameList() {
		return picNameList;
	}

	public void setPicNameList(List<String> picNameList) {
		this.picNameList = picNameList;
	}

	public Long getRegMonitoringId() {
		return regMonitoringId;
	}

	public void setRegMonitoringId(Long regMonitoringId) {
		this.regMonitoringId = regMonitoringId;
	}
}
