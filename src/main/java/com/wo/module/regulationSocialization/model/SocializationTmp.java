package com.wo.module.regulationSocialization.model;

import java.io.Serializable;
import java.util.List;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.counterType.model.CounterType;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.regulationSocializationApproval.model.SocializationApprovalTmp;

public class SocializationTmp extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 6395152164269025885L;
	private Long socializationId;
	private ParameterDetail jenisKetentuan;
	private String followUp;
	private String notes;
	private CounterType counterType;
	
	private String reminderStatus;
	private String status;
	
	private List<SocializationRegulationTmp> socializationRegulationTmps;
	private List<SocializationPICComplianceTmp> socializationPICComplianceTmps;
	private List<SocializationPICFollowupTmp> socializationPICFollowupTmps;
	private List<SocializationApprovalTmp> socializationApprovalTmps;
	private List<SocializationDocumentTmp> socializationDocumentTmps;
	
	private Long delId;

	public Long getSocializationId() {
		return socializationId;
	}

	public void setSocializationId(Long socializationId) {
		this.socializationId = socializationId;
	}

	public ParameterDetail getJenisKetentuan() {
		return jenisKetentuan;
	}

	public void setJenisKetentuan(ParameterDetail jenisKetentuan) {
		this.jenisKetentuan = jenisKetentuan;
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

	public CounterType getCounterType() {
		return counterType;
	}

	public void setCounterType(CounterType counterType) {
		this.counterType = counterType;
	}


	public String getReminderStatus() {
		return reminderStatus;
	}

	public void setReminderStatus(String reminderStatus) {
		this.reminderStatus = reminderStatus;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getDelId() {
		return delId;
	}

	public void setDelId(Long delId) {
		this.delId = delId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<SocializationRegulationTmp> getSocializationRegulationTmps() {
		return socializationRegulationTmps;
	}

	public void setSocializationRegulationTmps(List<SocializationRegulationTmp> socializationRegulationTmps) {
		this.socializationRegulationTmps = socializationRegulationTmps;
	}

	public List<SocializationPICComplianceTmp> getSocializationPICComplianceTmps() {
		return socializationPICComplianceTmps;
	}

	public void setSocializationPICComplianceTmps(List<SocializationPICComplianceTmp> socializationPICComplianceTmps) {
		this.socializationPICComplianceTmps = socializationPICComplianceTmps;
	}

	public List<SocializationPICFollowupTmp> getSocializationPICFollowupTmps() {
		return socializationPICFollowupTmps;
	}

	public void setSocializationPICFollowupTmps(List<SocializationPICFollowupTmp> socializationPICFollowupTmps) {
		this.socializationPICFollowupTmps = socializationPICFollowupTmps;
	}

	public List<SocializationApprovalTmp> getSocializationApprovalTmps() {
		return socializationApprovalTmps;
	}

	public void setSocializationApprovalTmps(List<SocializationApprovalTmp> socializationApprovalTmps) {
		this.socializationApprovalTmps = socializationApprovalTmps;
	}

	public List<SocializationDocumentTmp> getSocializationDocumentTmps() {
		return socializationDocumentTmps;
	}

	public void setSocializationDocumentTmps(List<SocializationDocumentTmp> socializationDocumentTmps) {
		this.socializationDocumentTmps = socializationDocumentTmps;
	}
	
	
	
	
	
	

}
