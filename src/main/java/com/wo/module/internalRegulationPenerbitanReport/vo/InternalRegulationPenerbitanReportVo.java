package com.wo.module.internalRegulationPenerbitanReport.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InternalRegulationPenerbitanReportVo implements Serializable {

	private static final long serialVersionUID = -1195587907650571969L;

	private Integer picTpgTotal;
	private Integer picTpkTotal;
	private Integer totalData;
	
	private Long irgId;
	private Long regulationTypeId;
	private Long workUnitTpgId;
	private Long regulationStatusId;
	private Long processStatusId;
	private Long regObsoleteTypeId;

	private Date regulationInDate;
	private Date finalIrgDate;
	private Date approvalSpvDate;
	private Date approvalPukDate;
	private Date signOffDate;
	private Date effectiveDate;
	private Date emailBlastDate;
	private Date uploadBlastDate;

	private String referenceNo;
	private String irgTitle;
	private String regulationNo;
	private String directorateTpg;
	private String regulationTypeName;
	private String picTpg;
	private String workUnitTpgName;
	private String regulationStatusName;
	private String picIrg;
	private String regulationInDateStr;
	private String finalIrgDateStr;
	private String approvalSpvDateStr;
	private String approvalPukDateStr;
	private String signOffDateStr;	
	private String processStatusName;
	private String effectiveDateStr;
	private String emailBlastDateStr;
	private String uploadBlastDateStr;
	private String obsoleteInfo;
	private String picIrgNik1;
	private String picIrgName1;
	private String picIrgNik2;
	private String picIrgName2;
	private String regObsoleteTypeName;
	private String emailGroupTpk;
	
	private List<InternalRegulationPenerbitanPicTpgReportVo> picTpgReportList = new ArrayList<InternalRegulationPenerbitanPicTpgReportVo>();
	private List<InternalRegulationPenerbitanPicTpkReportVo> picTpkReportList = new ArrayList<InternalRegulationPenerbitanPicTpkReportVo>();

	
	
	public Integer getPicTpgTotal() {
		return picTpgTotal;
	}

	public void setPicTpgTotal(Integer picTpgTotal) {
		this.picTpgTotal = picTpgTotal;
	}

	public Integer getPicTpkTotal() {
		return picTpkTotal;
	}

	public void setPicTpkTotal(Integer picTpkTotal) {
		this.picTpkTotal = picTpkTotal;
	}

	public Long getIrgId() {
		return irgId;
	}

	public void setIrgId(Long irgId) {
		this.irgId = irgId;
	}

	public Long getRegulationTypeId() {
		return regulationTypeId;
	}

	public void setRegulationTypeId(Long regulationTypeId) {
		this.regulationTypeId = regulationTypeId;
	}

	public Long getWorkUnitTpgId() {
		return workUnitTpgId;
	}

	public void setWorkUnitTpgId(Long workUnitTpgId) {
		this.workUnitTpgId = workUnitTpgId;
	}

	public Long getRegulationStatusId() {
		return regulationStatusId;
	}

	public void setRegulationStatusId(Long regulationStatusId) {
		this.regulationStatusId = regulationStatusId;
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

	public String getRegulationNo() {
		return regulationNo;
	}

	public void setRegulationNo(String regulationNo) {
		this.regulationNo = regulationNo;
	}

	public String getDirectorateTpg() {
		return directorateTpg;
	}

	public void setDirectorateTpg(String directorateTpg) {
		this.directorateTpg = directorateTpg;
	}

	public String getRegulationTypeName() {
		return regulationTypeName;
	}

	public void setRegulationTypeName(String regulationTypeName) {
		this.regulationTypeName = regulationTypeName;
	}

	public String getPicTpg() {
		return picTpg;
	}

	public void setPicTpg(String picTpg) {
		this.picTpg = picTpg;
	}

	public String getWorkUnitTpgName() {
		return workUnitTpgName;
	}

	public void setWorkUnitTpgName(String workUnitTpgName) {
		this.workUnitTpgName = workUnitTpgName;
	}

	public String getRegulationStatusName() {
		return regulationStatusName;
	}

	public void setRegulationStatusName(String regulationStatusName) {
		this.regulationStatusName = regulationStatusName;
	}

	public String getPicIrg() {
		return picIrg;
	}

	public void setPicIrg(String picIrg) {
		this.picIrg = picIrg;
	}

	public String getRegulationInDateStr() {
		return regulationInDateStr;
	}

	public void setRegulationInDateStr(String regulationInDateStr) {
		this.regulationInDateStr = regulationInDateStr;
	}

	public String getFinalIrgDateStr() {
		return finalIrgDateStr;
	}

	public void setFinalIrgDateStr(String finalIrgDateStr) {
		this.finalIrgDateStr = finalIrgDateStr;
	}

	public String getApprovalSpvDateStr() {
		return approvalSpvDateStr;
	}

	public void setApprovalSpvDateStr(String approvalSpvDateStr) {
		this.approvalSpvDateStr = approvalSpvDateStr;
	}

	public String getApprovalPukDateStr() {
		return approvalPukDateStr;
	}

	public void setApprovalPukDateStr(String approvalPukDateStr) {
		this.approvalPukDateStr = approvalPukDateStr;
	}

	public String getSignOffDateStr() {
		return signOffDateStr;
	}

	public void setSignOffDateStr(String signOffDateStr) {
		this.signOffDateStr = signOffDateStr;
	}

	public Long getProcessStatusId() {
		return processStatusId;
	}

	public void setProcessStatusId(Long processStatusId) {
		this.processStatusId = processStatusId;
	}

	public String getProcessStatusName() {
		return processStatusName;
	}

	public void setProcessStatusName(String processStatusName) {
		this.processStatusName = processStatusName;
	}

	public String getEffectiveDateStr() {
		return effectiveDateStr;
	}

	public void setEffectiveDateStr(String effectiveDateStr) {
		this.effectiveDateStr = effectiveDateStr;
	}

	public String getEmailBlastDateStr() {
		return emailBlastDateStr;
	}

	public void setEmailBlastDateStr(String emailBlastDateStr) {
		this.emailBlastDateStr = emailBlastDateStr;
	}

	public String getUploadBlastDateStr() {
		return uploadBlastDateStr;
	}

	public void setUploadBlastDateStr(String uploadBlastDateStr) {
		this.uploadBlastDateStr = uploadBlastDateStr;
	}

	public String getObsoleteInfo() {
		return obsoleteInfo;
	}

	public void setObsoleteInfo(String obsoleteInfo) {
		this.obsoleteInfo = obsoleteInfo;
	}

	public String getPicIrgNik1() {
		return picIrgNik1;
	}

	public void setPicIrgNik1(String picIrgNik1) {
		this.picIrgNik1 = picIrgNik1;
	}

	public String getPicIrgName1() {
		return picIrgName1;
	}

	public void setPicIrgName1(String picIrgName1) {
		this.picIrgName1 = picIrgName1;
	}

	public String getPicIrgNik2() {
		return picIrgNik2;
	}

	public void setPicIrgNik2(String picIrgNik2) {
		this.picIrgNik2 = picIrgNik2;
	}

	public String getPicIrgName2() {
		return picIrgName2;
	}

	public void setPicIrgName2(String picIrgName2) {
		this.picIrgName2 = picIrgName2;
	}

	public Long getRegObsoleteTypeId() {
		return regObsoleteTypeId;
	}

	public void setRegObsoleteTypeId(Long regObsoleteTypeId) {
		this.regObsoleteTypeId = regObsoleteTypeId;
	}

	public String getRegObsoleteTypeName() {
		return regObsoleteTypeName;
	}

	public void setRegObsoleteTypeName(String regObsoleteTypeName) {
		this.regObsoleteTypeName = regObsoleteTypeName;
	}

	public List<InternalRegulationPenerbitanPicTpgReportVo> getPicTpgReportList() {
		return picTpgReportList;
	}

	public void setPicTpgReportList(List<InternalRegulationPenerbitanPicTpgReportVo> picTpgReportList) {
		this.picTpgReportList = picTpgReportList;
	}

	public List<InternalRegulationPenerbitanPicTpkReportVo> getPicTpkReportList() {
		return picTpkReportList;
	}

	public void setPicTpkReportList(List<InternalRegulationPenerbitanPicTpkReportVo> picTpkReportList) {
		this.picTpkReportList = picTpkReportList;
	}

	public Integer getTotalData() {
		return totalData;
	}

	public void setTotalData(Integer totalData) {
		this.totalData = totalData;
	}

	public String getEmailGroupTpk() {
		return emailGroupTpk;
	}

	public void setEmailGroupTpk(String emailGroupTpk) {
		this.emailGroupTpk = emailGroupTpk;
	}
	 
	
}