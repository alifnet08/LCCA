package com.wo.module.regulationSocialization.vo;

import java.io.Serializable;
import java.util.Locale;

import javax.faces.context.FacesContext;

public class SocializationApprovalVO  implements Serializable {
	private static final long serialVersionUID = 6395152164269025885L;
	
	private String approvalBy;
	private String approvalStatus;
	private String approvalStatusEn;
	private String approvalStatusIn;
	private String approvalDate;
	private String approvalNote;
	
	public String getApprovalBy() {
		return approvalBy;
	}
	public void setApprovalBy(String approvalBy) {
		this.approvalBy = approvalBy;
	}
	@SuppressWarnings("static-access")
	public String getApprovalStatus() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if (locale != null && locale.equals(locale.ENGLISH)) {
			approvalStatus = approvalStatusEn;
		} else {
			approvalStatus = approvalStatusIn;
		}

		return approvalStatus;
	}
	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}
	public String getApprovalDate() {
		return approvalDate;
	}
	public void setApprovalDate(String approvalDate) {
		this.approvalDate = approvalDate;
	}
	public String getApprovalNote() {
		return approvalNote;
	}
	public void setApprovalNote(String approvalNote) {
		this.approvalNote = approvalNote;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getApprovalStatusEn() {
		return approvalStatusEn;
	}
	public void setApprovalStatusEn(String approvalStatusEn) {
		this.approvalStatusEn = approvalStatusEn;
	}
	public String getApprovalStatusIn() {
		return approvalStatusIn;
	}
	public void setApprovalStatusIn(String approvalStatusIn) {
		this.approvalStatusIn = approvalStatusIn;
	}
	
	
	

}
