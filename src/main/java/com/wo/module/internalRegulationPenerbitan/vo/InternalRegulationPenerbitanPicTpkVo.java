package com.wo.module.internalRegulationPenerbitan.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class InternalRegulationPenerbitanPicTpkVo implements Serializable {

	private static final long serialVersionUID = 1449441075959499475L;
	
	private Long irgId;
	private Long irgPicId;
	private Long divisionId;
	private Long fileSize;
	private Long user1Id;
	private Long user2Id;
	private Long user3Id;
	private Long delId;
	private Long irgPicEmailId;
	private Long sla;
	
	private Date targetDate;
	private Date oldTargetDate;
	private Date emailDate;

	private String user1Name;
	private String user2Name;
	private String user3Name;
	private String reviewApprovalFlag;
	private String attachmentFile;
	private String fileId;
	private String note;
	private String divisionName;
	private String emailPic1;
	private String emailPic2;
	private String emailPic3;
	private String emailTo;
	private String emailCc1;
	private String emailCc2;
	private String slaType;
	private String irgTitle;
	private String regulationTypeName;
	private String targetDateStr;
	private String emailIrgPic1;
	private String emailIrgPic2;
	private String emailIrgPic3;
	private String irgPicName1;
	private String irgPicName2;
	private String irgPicName3;
	private String emailGroupTpk;

	private Integer sequence;

	private Boolean isEditableTemp;
	
	private List<InternalRegulationPenerbitanPicTpgVo> irgPicTpgVoList;
	private List<InternalRegulationPenerbitanAttachmentVo> irgAttachmentVoList;

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

	public Long getDivisionId() {
		return divisionId;
	}

	public void setDivisionId(Long divisionId) {
		this.divisionId = divisionId;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public Long getUser1Id() {
		return user1Id;
	}

	public void setUser1Id(Long user1Id) {
		this.user1Id = user1Id;
	}

	public Long getUser2Id() {
		return user2Id;
	}

	public void setUser2Id(Long user2Id) {
		this.user2Id = user2Id;
	}

	public Long getUser3Id() {
		return user3Id;
	}

	public void setUser3Id(Long user3Id) {
		this.user3Id = user3Id;
	}

	public Long getDelId() {
		return delId;
	}

	public void setDelId(Long delId) {
		this.delId = delId;
	}

	public Date getTargetDate() {
		return targetDate;
	}

	public void setTargetDate(Date targetDate) {
		this.targetDate = targetDate;
	}

	public Date getOldTargetDate() {
		return oldTargetDate;
	}

	public void setOldTargetDate(Date oldTargetDate) {
		this.oldTargetDate = oldTargetDate;
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

	public String getReviewApprovalFlag() {
		return reviewApprovalFlag;
	}

	public void setReviewApprovalFlag(String reviewApprovalFlag) {
		this.reviewApprovalFlag = reviewApprovalFlag;
	}

	public String getAttachmentFile() {
		return attachmentFile;
	}

	public void setAttachmentFile(String attachmentFile) {
		this.attachmentFile = attachmentFile;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getDivisionName() {
		return divisionName;
	}

	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public Boolean getIsEditableTemp() {
		return isEditableTemp;
	}

	public void setIsEditableTemp(Boolean isEditableTemp) {
		this.isEditableTemp = isEditableTemp;
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

	public Date getEmailDate() {
		return emailDate;
	}

	public void setEmailDate(Date emailDate) {
		this.emailDate = emailDate;
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

	public Long getIrgPicEmailId() {
		return irgPicEmailId;
	}

	public void setIrgPicEmailId(Long irgPicEmailId) {
		this.irgPicEmailId = irgPicEmailId;
	}

	public Long getSla() {
		return sla;
	}

	public void setSla(Long sla) {
		this.sla = sla;
	}

	public String getSlaType() {
		return slaType;
	}

	public void setSlaType(String slaType) {
		this.slaType = slaType;
	}

	public String getIrgTitle() {
		return irgTitle;
	}

	public void setIrgTitle(String irgTitle) {
		this.irgTitle = irgTitle;
	}

	public String getRegulationTypeName() {
		return regulationTypeName;
	}

	public void setRegulationTypeName(String regulationTypeName) {
		this.regulationTypeName = regulationTypeName;
	}

	public String getTargetDateStr() {
		return targetDateStr;
	}

	public void setTargetDateStr(String targetDateStr) {
		this.targetDateStr = targetDateStr;
	}

	public String getEmailIrgPic1() {
		return emailIrgPic1;
	}

	public void setEmailIrgPic1(String emailIrgPic1) {
		this.emailIrgPic1 = emailIrgPic1;
	}

	public String getEmailIrgPic2() {
		return emailIrgPic2;
	}

	public void setEmailIrgPic2(String emailIrgPic2) {
		this.emailIrgPic2 = emailIrgPic2;
	}

	public String getEmailIrgPic3() {
		return emailIrgPic3;
	}

	public void setEmailIrgPic3(String emailIrgPic3) {
		this.emailIrgPic3 = emailIrgPic3;
	}

	public String getIrgPicName1() {
		return irgPicName1;
	}

	public void setIrgPicName1(String irgPicName1) {
		this.irgPicName1 = irgPicName1;
	}

	public String getIrgPicName2() {
		return irgPicName2;
	}

	public void setIrgPicName2(String irgPicName2) {
		this.irgPicName2 = irgPicName2;
	}

	public String getIrgPicName3() {
		return irgPicName3;
	}

	public void setIrgPicName3(String irgPicName3) {
		this.irgPicName3 = irgPicName3;
	}

	public List<InternalRegulationPenerbitanPicTpgVo> getIrgPicTpgVoList() {
		return irgPicTpgVoList;
	}

	public void setIrgPicTpgVoList(List<InternalRegulationPenerbitanPicTpgVo> irgPicTpgVoList) {
		this.irgPicTpgVoList = irgPicTpgVoList;
	}

	public List<InternalRegulationPenerbitanAttachmentVo> getIrgAttachmentVoList() {
		return irgAttachmentVoList;
	}

	public void setIrgAttachmentVoList(List<InternalRegulationPenerbitanAttachmentVo> irgAttachmentVoList) {
		this.irgAttachmentVoList = irgAttachmentVoList;
	}

	public String getEmailGroupTpk() {
		return emailGroupTpk;
	}

	public void setEmailGroupTpk(String emailGroupTpk) {
		this.emailGroupTpk = emailGroupTpk;
	}
	
}