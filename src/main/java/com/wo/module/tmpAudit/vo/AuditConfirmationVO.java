package com.wo.module.tmpAudit.vo;

import java.io.Serializable;
import java.util.List;

import com.wo.module.complianceTestingMockup.vo.AttachmentVO;
import com.wo.module.regulationSocialization.vo.StatusConfirmationVO;
import com.wo.module.trcAudit.model.TrcAuditPicFollowupAttachment;

public class AuditConfirmationVO extends StatusConfirmationVO implements Serializable {
	private static final long serialVersionUID = -8517871983615611517L;

	private String newTargetDate;
	private String perpanjanganNote;
	private String complianceStatusNew;
	
	private List<AttachmentVO> attachmentsPerpanjangan;
	private List<AttachmentVO> attachments;
	
	private List<TrcAuditPicFollowupAttachment> trcAuditPicFollowupAttachments;
	private List<TrcAuditPicFollowupAttachment> trcAuditPicFollowupLetterAttachments;

	public List<TrcAuditPicFollowupAttachment> getTrcAuditPicFollowupAttachments() {
		return trcAuditPicFollowupAttachments;
	}

	public void setTrcAuditPicFollowupAttachments(List<TrcAuditPicFollowupAttachment> trcAuditPicFollowupAttachments) {
		this.trcAuditPicFollowupAttachments = trcAuditPicFollowupAttachments;
	}

	public List<TrcAuditPicFollowupAttachment> getTrcAuditPicFollowupLetterAttachments() {
		return trcAuditPicFollowupLetterAttachments;
	}

	public void setTrcAuditPicFollowupLetterAttachments(List<TrcAuditPicFollowupAttachment> trcAuditPicFollowupLetterAttachments) {
		this.trcAuditPicFollowupLetterAttachments = trcAuditPicFollowupLetterAttachments;
	}

	public String getNewTargetDate() {
		return newTargetDate;
	}

	public void setNewTargetDate(String newTargetDate) {
		this.newTargetDate = newTargetDate;
	}

	public String getPerpanjanganNote() {
		return perpanjanganNote;
	}

	public void setPerpanjanganNote(String perpanjanganNote) {
		this.perpanjanganNote = perpanjanganNote;
	}

	public List<AttachmentVO> getAttachmentsPerpanjangan() {
		return attachmentsPerpanjangan;
	}

	public void setAttachmentsPerpanjangan(List<AttachmentVO> attachmentsPerpanjangan) {
		this.attachmentsPerpanjangan = attachmentsPerpanjangan;
	}

	public List<AttachmentVO> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<AttachmentVO> attachments) {
		this.attachments = attachments;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getComplianceStatusNew() {
		return complianceStatusNew;
	}

	public void setComplianceStatusNew(String complianceStatusNew) {
		this.complianceStatusNew = complianceStatusNew;
	}

	
}