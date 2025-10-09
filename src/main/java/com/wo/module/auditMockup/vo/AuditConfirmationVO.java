package com.wo.module.auditMockup.vo;

import java.io.Serializable;
import java.util.List;

import com.wo.module.regulationSocialization.vo.StatusConfirmationVO;
import com.wo.module.trcAudit.model.TrcAuditPicFollowupAttachment;

public class AuditConfirmationVO extends StatusConfirmationVO implements Serializable {
	private static final long serialVersionUID = -8517871983615611517L;

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

}