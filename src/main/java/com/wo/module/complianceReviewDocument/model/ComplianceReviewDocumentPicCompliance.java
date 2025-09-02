package com.wo.module.complianceReviewDocument.model;

import java.io.Serializable;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.user.model.User;

public class ComplianceReviewDocumentPicCompliance extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 2743270353305360911L;
	private Long compliancePicComplianceId;
	private ComplianceReviewDocument complianceReviewDocument;
	private User user;
	
	private Long delId;
	
	private Integer sequence;
	
	//helper
	private String userNIKTemp;
	private String userNameTemp;
	private String userEmailTemp;

	public Long getCompliancePicComplianceId() {
		return compliancePicComplianceId;
	}

	public void setCompliancePicComplianceId(Long compliancePicComplianceId) {
		this.compliancePicComplianceId = compliancePicComplianceId;
	}

	public ComplianceReviewDocument getComplianceReviewDocument() {
		return complianceReviewDocument;
	}

	public void setComplianceReviewDocument(ComplianceReviewDocument complianceReviewDocument) {
		this.complianceReviewDocument = complianceReviewDocument;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Long getDelId() {
		return delId;
	}

	public void setDelId(Long delId) {
		this.delId = delId;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public String getUserNIKTemp() {
		return userNIKTemp;
	}

	public void setUserNIKTemp(String userNIKTemp) {
		this.userNIKTemp = userNIKTemp;
	}

	public String getUserNameTemp() {
		return userNameTemp;
	}

	public void setUserNameTemp(String userNameTemp) {
		this.userNameTemp = userNameTemp;
	}

	public String getUserEmailTemp() {
		return userEmailTemp;
	}

	public void setUserEmailTemp(String userEmailTemp) {
		this.userEmailTemp = userEmailTemp;
	}
}