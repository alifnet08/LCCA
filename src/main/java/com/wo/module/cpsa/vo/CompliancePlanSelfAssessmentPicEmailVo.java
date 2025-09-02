package com.wo.module.cpsa.vo;

import java.io.Serializable;
import java.util.Date;

public class CompliancePlanSelfAssessmentPicEmailVo implements Serializable {

	private static final long serialVersionUID = -7837414841614034082L;
	
	private Long cpsaId;
	private Long divisionId;
	private Long userId1;
	private Long userId2;
	private Long userId3;
	private Long cpsaPicId;
	private Long cpsaPicEmailId;
	private Long sla;
	
	private String divisionName;
	private String cpsaStatus;
	private String cpsaStatusName;	
	private String userNik1;
	private String userName1;
	private String directorateName;
	private String letterAbout;
	private String emailPic1;
	private String emailPic2;
	private String emailPic3;
	private String user1Name;
	private String user2Name;
	private String user3Name;
	private String emailTo;
	private String emailCc1;
	private String emailCc2;
	private String slaType;
	private String targetDateStr;
	private String cpsaAdminEmail;
	private String cpsaAdminName;
	
	private Date emailDate;
	private Date targetDate;
	
	public CompliancePlanSelfAssessmentPicEmailVo() {}

	public Long getCpsaId() {
		return cpsaId;
	}

	public void setCpsaId(Long cpsaId) {
		this.cpsaId = cpsaId;
	}

	public Long getDivisionId() {
		return divisionId;
	}

	public void setDivisionId(Long divisionId) {
		this.divisionId = divisionId;
	}

	public Long getUserId1() {
		return userId1;
	}

	public void setUserId1(Long userId1) {
		this.userId1 = userId1;
	}

	public Long getUserId2() {
		return userId2;
	}

	public void setUserId2(Long userId2) {
		this.userId2 = userId2;
	}

	public Long getUserId3() {
		return userId3;
	}

	public void setUserId3(Long userId3) {
		this.userId3 = userId3;
	}

	public Long getCpsaPicId() {
		return cpsaPicId;
	}

	public void setCpsaPicId(Long cpsaPicId) {
		this.cpsaPicId = cpsaPicId;
	}

	public Long getCpsaPicEmailId() {
		return cpsaPicEmailId;
	}

	public void setCpsaPicEmailId(Long cpsaPicEmailId) {
		this.cpsaPicEmailId = cpsaPicEmailId;
	}

	public String getDivisionName() {
		return divisionName;
	}

	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}

	public String getCpsaStatus() {
		return cpsaStatus;
	}

	public void setCpsaStatus(String cpsaStatus) {
		this.cpsaStatus = cpsaStatus;
	}

	public String getCpsaStatusName() {
		return cpsaStatusName;
	}

	public void setCpsaStatusName(String cpsaStatusName) {
		this.cpsaStatusName = cpsaStatusName;
	}

	public String getUserNik1() {
		return userNik1;
	}

	public void setUserNik1(String userNik1) {
		this.userNik1 = userNik1;
	}

	public String getUserName1() {
		return userName1;
	}

	public void setUserName1(String userName1) {
		this.userName1 = userName1;
	}

	public String getDirectorateName() {
		return directorateName;
	}

	public void setDirectorateName(String directorateName) {
		this.directorateName = directorateName;
	}

	public String getLetterAbout() {
		return letterAbout;
	}

	public void setLetterAbout(String letterAbout) {
		this.letterAbout = letterAbout;
	}

	public Date getEmailDate() {
		return emailDate;
	}

	public void setEmailDate(Date emailDate) {
		this.emailDate = emailDate;
	}

	public String getEmailPic1() {
		return emailPic1;
	}

	public void setEmailPic1(String emailPic1) {
		this.emailPic1 = emailPic1;
	}

	public String getEmailPic2() {
		return emailPic2;
	}

	public void setEmailPic2(String emailPic2) {
		this.emailPic2 = emailPic2;
	}

	public String getEmailPic3() {
		return emailPic3;
	}

	public void setEmailPic3(String emailPic3) {
		this.emailPic3 = emailPic3;
	}

	public String getUser1Name() {
		return user1Name;
	}

	public void setUser1Name(String user1Name) {
		this.user1Name = user1Name;
	}

	public String getUser2Name() {
		return user2Name;
	}

	public void setUser2Name(String user2Name) {
		this.user2Name = user2Name;
	}

	public String getUser3Name() {
		return user3Name;
	}

	public void setUser3Name(String user3Name) {
		this.user3Name = user3Name;
	}

	public Long getSla() {
		return sla;
	}

	public void setSla(Long sla) {
		this.sla = sla;
	}

	public String getEmailTo() {
		return emailTo;
	}

	public void setEmailTo(String emailTo) {
		this.emailTo = emailTo;
	}

	public String getEmailCc1() {
		return emailCc1;
	}

	public void setEmailCc1(String emailCc1) {
		this.emailCc1 = emailCc1;
	}

	public String getEmailCc2() {
		return emailCc2;
	}

	public void setEmailCc2(String emailCc2) {
		this.emailCc2 = emailCc2;
	}

	public String getSlaType() {
		return slaType;
	}

	public void setSlaType(String slaType) {
		this.slaType = slaType;
	}

	public String getTargetDateStr() {
		return targetDateStr;
	}

	public void setTargetDateStr(String targetDateStr) {
		this.targetDateStr = targetDateStr;
	}

	public Date getTargetDate() {
		return targetDate;
	}

	public void setTargetDate(Date targetDate) {
		this.targetDate = targetDate;
	}

	public String getCpsaAdminEmail() {
		return cpsaAdminEmail;
	}

	public void setCpsaAdminEmail(String cpsaAdminEmail) {
		this.cpsaAdminEmail = cpsaAdminEmail;
	}

	public String getCpsaAdminName() {
		return cpsaAdminName;
	}

	public void setCpsaAdminName(String cpsaAdminName) {
		this.cpsaAdminName = cpsaAdminName;
	}
	
	

	
}