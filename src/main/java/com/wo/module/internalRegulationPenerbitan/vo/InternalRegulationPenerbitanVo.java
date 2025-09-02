package com.wo.module.internalRegulationPenerbitan.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;

public class InternalRegulationPenerbitanVo implements Serializable {

	private static final long serialVersionUID = 316323515783355317L;

	private Long irgId;
	private Long workUnitTpgId;
	private Long counterTypeId;
	private Long regulationTypeId;
	private Long regulationStatusId;
	private Long userId1;

	private String referenceNo;
	private String irgTitle;
	private String directorateTpg;
	private String followUp;
	private String notes;
	private String regulationNo;
	private String obsoleteInfo;
	private String regulationTypeName;
	private String regulationStatusName;
	private String processStatus;
	private String processStatusName;
	private String processStatusNameTemp;
	private String processStatusNameIn;
	private String processStatusNameEn;
	private String regObsoleteType;
	private String regObsoleteTypeName;
	private String regulationInDateStr;
	private String workUnitTpgName;
	private String userNik1;
	private String userName1;

	private Date regulationInDate;
	private Date finalIrgDate;
	private Date approvalSpvDate;
	private Date approvalPukDate;
	private Date signOffDate;
	private Date effectiveDate;
	private Date emailBlastDate;
	private Date uploadBlastDate;

	private List<InternalRegulationPenerbitanPicTpgVo> irgPicTpgs;
	private List<InternalRegulationPenerbitanPicTpkVo> irgPicTpks;
	private List<InternalRegulationPenerbitanPicIrgVo> irgPicIrgs;
	
	//helpers
	private List<String> tanggalReminderStrList;

	public Long getIrgId() {
		return irgId;
	}

	public void setIrgId(Long irgId) {
		this.irgId = irgId;
	}

	public Long getWorkUnitTpgId() {
		return workUnitTpgId;
	}

	public void setWorkUnitTpgId(Long workUnitTpgId) {
		this.workUnitTpgId = workUnitTpgId;
	}

	public Long getCounterTypeId() {
		return counterTypeId;
	}

	public void setCounterTypeId(Long counterTypeId) {
		this.counterTypeId = counterTypeId;
	}

	public Long getRegulationTypeId() {
		return regulationTypeId;
	}

	public void setRegulationTypeId(Long regulationTypeId) {
		this.regulationTypeId = regulationTypeId;
	}

	public Long getRegulationStatusId() {
		return regulationStatusId;
	}

	public void setRegulationStatusId(Long regulationStatusId) {
		this.regulationStatusId = regulationStatusId;
	}

	public String getReferenceNo() {
		return referenceNo;
	}

	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}

	public String getIrgTitle() {
		return irgTitle;
	}

	public void setIrgTitle(String irgTitle) {
		this.irgTitle = irgTitle;
	}

	public String getDirectorateTpg() {
		return directorateTpg;
	}

	public void setDirectorateTpg(String directorateTpg) {
		this.directorateTpg = directorateTpg;
	}

	public String getFollowUp() {
		return followUp;
	}

	public void setFollowUp(String followUp) {
		this.followUp = followUp;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getRegulationNo() {
		return regulationNo;
	}

	public void setRegulationNo(String regulationNo) {
		this.regulationNo = regulationNo;
	}

	public String getObsoleteInfo() {
		return obsoleteInfo;
	}

	public void setObsoleteInfo(String obsoleteInfo) {
		this.obsoleteInfo = obsoleteInfo;
	}

	public String getRegulationTypeName() {
		return regulationTypeName;
	}

	public void setRegulationTypeName(String regulationTypeName) {
		this.regulationTypeName = regulationTypeName;
	}

	public String getRegulationStatusName() {
		return regulationStatusName;
	}

	public void setRegulationStatusName(String regulationStatusName) {
		this.regulationStatusName = regulationStatusName;
	}

	public String getProcessStatus() {
		return processStatus;
	}

	public void setProcessStatus(String processStatus) {
		this.processStatus = processStatus;
	}

	public String getProcessStatusName() {
		return processStatusName;
	}

	public void setProcessStatusName(String processStatusName) {
		this.processStatusName = processStatusName;
	}

	public String getRegObsoleteType() {
		return regObsoleteType;
	}

	public void setRegObsoleteType(String regObsoleteType) {
		this.regObsoleteType = regObsoleteType;
	}

	public String getRegObsoleteTypeName() {
		return regObsoleteTypeName;
	}

	public void setRegObsoleteTypeName(String regObsoleteTypeName) {
		this.regObsoleteTypeName = regObsoleteTypeName;
	}

	public String getRegulationInDateStr() {
		return regulationInDateStr;
	}

	public void setRegulationInDateStr(String regulationInDateStr) {
		this.regulationInDateStr = regulationInDateStr;
	}

	public String getWorkUnitTpgName() {
		return workUnitTpgName;
	}

	public void setWorkUnitTpgName(String workUnitTpgName) {
		this.workUnitTpgName = workUnitTpgName;
	}

	public Date getRegulationInDate() {
		return regulationInDate;
	}

	public void setRegulationInDate(Date regulationInDate) {
		this.regulationInDate = regulationInDate;
	}

	public Date getFinalIrgDate() {
		return finalIrgDate;
	}

	public void setFinalIrgDate(Date finalIrgDate) {
		this.finalIrgDate = finalIrgDate;
	}

	public Date getApprovalSpvDate() {
		return approvalSpvDate;
	}

	public void setApprovalSpvDate(Date approvalSpvDate) {
		this.approvalSpvDate = approvalSpvDate;
	}

	public Date getApprovalPukDate() {
		return approvalPukDate;
	}

	public void setApprovalPukDate(Date approvalPukDate) {
		this.approvalPukDate = approvalPukDate;
	}

	public Date getSignOffDate() {
		return signOffDate;
	}

	public void setSignOffDate(Date signOffDate) {
		this.signOffDate = signOffDate;
	}

	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public Date getEmailBlastDate() {
		return emailBlastDate;
	}

	public void setEmailBlastDate(Date emailBlastDate) {
		this.emailBlastDate = emailBlastDate;
	}

	public Date getUploadBlastDate() {
		return uploadBlastDate;
	}

	public void setUploadBlastDate(Date uploadBlastDate) {
		this.uploadBlastDate = uploadBlastDate;
	}

	public List<InternalRegulationPenerbitanPicTpgVo> getIrgPicTpgs() {
		return irgPicTpgs;
	}

	public void setIrgPicTpgs(List<InternalRegulationPenerbitanPicTpgVo> irgPicTpgs) {
		this.irgPicTpgs = irgPicTpgs;
	}

	public List<InternalRegulationPenerbitanPicTpkVo> getIrgPicTpks() {
		return irgPicTpks;
	}

	public void setIrgPicTpks(List<InternalRegulationPenerbitanPicTpkVo> irgPicTpks) {
		this.irgPicTpks = irgPicTpks;
	}

	public List<InternalRegulationPenerbitanPicIrgVo> getIrgPicIrgs() {
		return irgPicIrgs;
	}

	public void setIrgPicIrgs(List<InternalRegulationPenerbitanPicIrgVo> irgPicIrgs) {
		this.irgPicIrgs = irgPicIrgs;
	}

	public Long getUserId1() {
		return userId1;
	}

	public void setUserId1(Long userId1) {
		this.userId1 = userId1;
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

	public String getProcessStatusNameIn() {
		return processStatusNameIn;
	}

	public void setProcessStatusNameIn(String processStatusNameIn) {
		this.processStatusNameIn = processStatusNameIn;
	}

	public String getProcessStatusNameEn() {
		return processStatusNameEn;
	}

	public void setProcessStatusNameEn(String processStatusNameEn) {
		this.processStatusNameEn = processStatusNameEn;
	}

	public String getProcessStatusNameTemp() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if (locale != null && locale.equals(locale.ENGLISH)) {
			processStatusNameTemp = processStatusNameEn;
		} else {
			processStatusNameTemp = processStatusNameIn;
		}
		
		return processStatusNameTemp;
	}

	public void setProcessStatusNameTemp(String processStatusNameTemp) {
		this.processStatusNameTemp = processStatusNameTemp;
	}

	public List<String> getTanggalReminderStrList() {
		return tanggalReminderStrList;
	}

	public void setTanggalReminderStrList(List<String> tanggalReminderStrList) {
		this.tanggalReminderStrList = tanggalReminderStrList;
	}
}