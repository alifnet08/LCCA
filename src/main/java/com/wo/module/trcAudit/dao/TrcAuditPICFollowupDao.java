package com.wo.module.trcAudit.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.trcAudit.model.TrcAuditPicFollowup;
import com.wo.module.trcAudit.model.TrcAuditPicFollowupAttachment;

public interface TrcAuditPICFollowupDao extends GenericDAO<TrcAuditPicFollowup, Long> {

	public List<TrcAuditPicFollowupAttachment> getTrcAuditPICFollowupAttachmentsByAuditPicFollowupId(Long auditFollowupId) throws Exception;

	List<TrcAuditPicFollowupAttachment> getTrcAuditPICFollowupLetterAttachmentsByAuditPicFollowupId(
			Long auditPicFollowupId) throws Exception;
}
