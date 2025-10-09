package com.wo.module.trcComplianceReview.model;

import java.io.Serializable;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.user.model.User;

public class TrcComplianceReviewPicCompliance extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -1707141996366990113L;
	private Long complianceReviewPicComplianceId;
	private TrcComplianceReview trcComplianceReview;
	private User user;
	
	private String nikTemp;
	private String nameTemp;
	private String emailTemp;
	
	private int sequence;

	public TrcComplianceReviewPicCompliance() {
		super();
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public String getNikTemp() {
		return nikTemp;
	}

	public void setNikTemp(String nikTemp) {
		this.nikTemp = nikTemp;
	}

	public String getEmailTemp() {
		return emailTemp;
	}

	public void setEmailTemp(String emailTemp) {
		this.emailTemp = emailTemp;
	}

	public String getNameTemp() {
		return nameTemp;
	}

	public void setNameTemp(String nameTemp) {
		this.nameTemp = nameTemp;
	}

	public Long getComplianceReviewPicComplianceId() {
		return complianceReviewPicComplianceId;
	}

	public void setComplianceReviewPicComplianceId(Long complianceReviewPicComplianceId) {
		this.complianceReviewPicComplianceId = complianceReviewPicComplianceId;
	}

	public TrcComplianceReview getTrcComplianceReview() {
		return trcComplianceReview;
	}

	public void setTrcComplianceReview(TrcComplianceReview trcComplianceReview) {
		this.trcComplianceReview = trcComplianceReview;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	
}
