package com.wo.module.trcCorrespondence.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.user.model.User;

public class TrcCrpdcPicConfirm extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -6047699672874785689L;
	
	private Long crpdcPicConfirmId;
	private Long divisionId;

	private Date targetDate;
	
	private Timestamp picConfirmationDate;
	private Timestamp picFollowupDate;
	private Timestamp complianceDate;
	
	private String picFollowupNote;	
	private String complianceNote;
		
	private User user1;
	private User user2;
	private User user3;
	private User complianceBy;
	private TrcCorrespondence trcCorrespondence;
	private ParameterDetail statusPic;
	private ParameterDetail complianceStatus;

	//helper
	private Integer sequence;
	
	private String divisionName;
	private String userName1;
	private String userName2;
	private String userName3;
	private String complianceStatusCode;
	
	private boolean isCanEdit;
	
	private List<TrcCorrespondencePicFollowupAttachment> trcCorrespondencePicFollowupAttachments;
	private List<TrcCorrespondencePicFollowupAttendance> trcCorrespondencePicFollowupAttendance;
	
	
	public Long getCrpdcPicConfirmId() {
		return crpdcPicConfirmId;
	}

	public void setCrpdcPicConfirmId(Long crpdcPicConfirmId) {
		this.crpdcPicConfirmId = crpdcPicConfirmId;
	}

	public Long getDivisionId() {
		return divisionId;
	}

	public void setDivisionId(Long divisionId) {
		this.divisionId = divisionId;
	}

	public String getDivisionName() {
		return divisionName;
	}

	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}

	public User getUser1() {
		return user1;
	}

	public void setUser1(User user1) {
		this.user1 = user1;
	}

	public User getUser2() {
		return user2;
	}

	public void setUser2(User user2) {
		this.user2 = user2;
	}

	public User getUser3() {
		return user3;
	}

	public void setUser3(User user3) {
		this.user3 = user3;
	}

	public TrcCorrespondence getTrcCorrespondence() {
		return trcCorrespondence;
	}

	public void setTrcCorrespondence(TrcCorrespondence trcCorrespondence) {
		this.trcCorrespondence = trcCorrespondence;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public String getUserName1() {
		return userName1;
	}

	public void setUserName1(String userName1) {
		this.userName1 = userName1;
	}

	public String getUserName2() {
		return userName2;
	}

	public void setUserName2(String userName2) {
		this.userName2 = userName2;
	}

	public String getUserName3() {
		return userName3;
	}

	public void setUserName3(String userName3) {
		this.userName3 = userName3;
	}

	public ParameterDetail getStatusPic() {
		return statusPic;
	}

	public void setStatusPic(ParameterDetail statusPic) {
		this.statusPic = statusPic;
	}

	public ParameterDetail getComplianceStatus() {
		return complianceStatus;
	}

	public void setComplianceStatus(ParameterDetail complianceStatus) {
		this.complianceStatus = complianceStatus;
	}

	public Date getTargetDate() {
		return targetDate;
	}

	public void setTargetDate(Date targetDate) {
		this.targetDate = targetDate;
	}

	public Timestamp getPicConfirmationDate() {
		return picConfirmationDate;
	}

	public void setPicConfirmationDate(Timestamp picConfirmationDate) {
		this.picConfirmationDate = picConfirmationDate;
	}

	public Timestamp getPicFollowupDate() {
		return picFollowupDate;
	}

	public void setPicFollowupDate(Timestamp picFollowupDate) {
		this.picFollowupDate = picFollowupDate;
	}

	public Timestamp getComplianceDate() {
		return complianceDate;
	}

	public void setComplianceDate(Timestamp complianceDate) {
		this.complianceDate = complianceDate;
	}

	public String getPicFollowupNote() {
		return picFollowupNote;
	}

	public void setPicFollowupNote(String picFollowupNote) {
		this.picFollowupNote = picFollowupNote;
	}

	public String getComplianceNote() {
		return complianceNote;
	}

	public void setComplianceNote(String complianceNote) {
		this.complianceNote = complianceNote;
	}

	public User getComplianceBy() {
		return complianceBy;
	}

	public void setComplianceBy(User complianceBy) {
		this.complianceBy = complianceBy;
	}

	public boolean isCanEdit() {
		boolean flag = false;
		
		if (complianceStatus != null) {
			if (complianceStatus.getParameterDtlCode().equals("COMPLIANCE_CLOSE")) {
				flag = true;
			}
		}
		return flag;
	}

	public void setCanEdit(boolean isCanEdit) {
		this.isCanEdit = isCanEdit;
	}

	public String getComplianceStatusCode() {
		return complianceStatusCode;
	}

	public void setComplianceStatusCode(String complianceStatusCode) {
		this.complianceStatusCode = complianceStatusCode;
	}

	public List<TrcCorrespondencePicFollowupAttachment> getTrcCorrespondencePicFollowupAttachments() {
		return trcCorrespondencePicFollowupAttachments;
	}

	public void setTrcCorrespondencePicFollowupAttachments(
			List<TrcCorrespondencePicFollowupAttachment> trcCorrespondencePicFollowupAttachments) {
		this.trcCorrespondencePicFollowupAttachments = trcCorrespondencePicFollowupAttachments;
	}

	public List<TrcCorrespondencePicFollowupAttendance> getTrcCorrespondencePicFollowupAttendance() {
		return trcCorrespondencePicFollowupAttendance;
	}

	public void setTrcCorrespondencePicFollowupAttendance(
			List<TrcCorrespondencePicFollowupAttendance> trcCorrespondencePicFollowupAttendance) {
		this.trcCorrespondencePicFollowupAttendance = trcCorrespondencePicFollowupAttendance;
	}
	
}