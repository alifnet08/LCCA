package com.wo.module.internalRegulationPenerbitan.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.counterType.model.CounterType;
import com.wo.module.parameter.model.ParameterDetail;

public class InternalRegulationPenerbitan extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1701616836998165285L;

	private Long irgId;
	private Long workUnitTpg;

	private String referenceNo;
	private String irgTitle;
	private String directorateTpg;
	private String regulationNo;
	private String obsoleteInfo;
	private String emailGroupTpk;
	private String emailGroupTpkData;

	private Date regulationInDate;
	private Date finalIrgDate;
	private Date approvalSpvDate;
	private Date approvalPukDate;
	private Date signOffDate;
	private Date effectiveDate;
	private Date emailBlastDate;
	private Date uploadBlastDate;

	private ParameterDetail regulationType;
	private ParameterDetail regulationStatus;
	private ParameterDetail processStatus;
	private ParameterDetail regObsoleteType;

	private CounterType counterType;

	private List<InternalRegulationPenerbitanPicIrg> irgPicIrgs;
	private List<InternalRegulationPenerbitanPicTpg> irgPicTpgs;
	private List<InternalRegulationPenerbitanPicTpk> irgPicTpks;
	private List<InternalRegulationPenerbitanEmailGroup> irgEmailGroups;
	
	private List<InternalRegulationPenerbitanAttachment> irgAttachmentList;
	
	public Long getIrgId() {
		return irgId;
	}

	public void setIrgId(Long irgId) {
		this.irgId = irgId;
	}

	public Long getWorkUnitTpg() {
		return workUnitTpg;
	}

	public void setWorkUnitTpg(Long workUnitTpg) {
		this.workUnitTpg = workUnitTpg;
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

	public ParameterDetail getRegulationType() {
		return regulationType;
	}

	public void setRegulationType(ParameterDetail regulationType) {
		this.regulationType = regulationType;
	}

	public ParameterDetail getRegulationStatus() {
		return regulationStatus;
	}

	public void setRegulationStatus(ParameterDetail regulationStatus) {
		this.regulationStatus = regulationStatus;
	}

	public ParameterDetail getProcessStatus() {
		return processStatus;
	}

	public void setProcessStatus(ParameterDetail processStatus) {
		this.processStatus = processStatus;
	}

	public ParameterDetail getRegObsoleteType() {
		return regObsoleteType;
	}

	public void setRegObsoleteType(ParameterDetail regObsoleteType) {
		this.regObsoleteType = regObsoleteType;
	}

	public CounterType getCounterType() {
		return counterType;
	}

	public void setCounterType(CounterType counterType) {
		this.counterType = counterType;
	}

	public List<InternalRegulationPenerbitanPicTpg> getIrgPicTpgs() {
		return irgPicTpgs;
	}

	public void setIrgPicTpgs(List<InternalRegulationPenerbitanPicTpg> irgPicTpgs) {
		this.irgPicTpgs = irgPicTpgs;
	}

	public List<InternalRegulationPenerbitanPicTpk> getIrgPicTpks() {
		return irgPicTpks;
	}

	public void setIrgPicTpks(List<InternalRegulationPenerbitanPicTpk> irgPicTpks) {
		this.irgPicTpks = irgPicTpks;
	}

	public List<InternalRegulationPenerbitanPicIrg> getIrgPicIrgs() {
		return irgPicIrgs;
	}

	public void setIrgPicIrgs(List<InternalRegulationPenerbitanPicIrg> irgPicIrgs) {
		this.irgPicIrgs = irgPicIrgs;
	}

	public String getEmailGroupTpk() {
		return emailGroupTpk;
	}

	public void setEmailGroupTpk(String emailGroupTpk) {
		this.emailGroupTpk = emailGroupTpk;
	}

	public List<InternalRegulationPenerbitanAttachment> getIrgAttachmentList() {
		return irgAttachmentList;
	}

	public void setIrgAttachmentList(List<InternalRegulationPenerbitanAttachment> irgAttachmentList) {
		this.irgAttachmentList = irgAttachmentList;
	}

	public List<InternalRegulationPenerbitanEmailGroup> getIrgEmailGroups() {
		return irgEmailGroups;
	}

	public void setIrgEmailGroups(List<InternalRegulationPenerbitanEmailGroup> irgEmailGroups) {
		this.irgEmailGroups = irgEmailGroups;
	}

	public String getEmailGroupTpkData() {
		return emailGroupTpkData;
	}

	public void setEmailGroupTpkData(String emailGroupTpkData) {
		this.emailGroupTpkData = emailGroupTpkData;
	}

}