package com.wo.module.trcAudit.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.trcAudit.model.TrcAuditPicFollowup;
import com.wo.module.trcAudit.model.TrcAuditPicFollowupAttachment;

@Repository("trcAuditPICFollowupDao")
public class TrcAuditPICFollowupDaoImpl extends GenericDAOHibernate<TrcAuditPicFollowup, Long>
		implements TrcAuditPICFollowupDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<TrcAuditPicFollowupAttachment> getTrcAuditPICFollowupAttachmentsByAuditPicFollowupId(Long auditPicFollowupId) throws Exception {
		String hql = "FROM TrcAuditPicFollowupAttachment where trcAuditPicFollowup.auditPicFollowupId= :auditPicFollowupId and attachmentType.parameterDtlCode = '"+ParameterDetail.PARAM_DET_CODE_FOLLOWUP_ATTACH_DOC+"' ";
		Query result = getSession().createQuery(hql);
		result.setParameter("auditPicFollowupId", auditPicFollowupId);
		return result.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TrcAuditPicFollowupAttachment> getTrcAuditPICFollowupLetterAttachmentsByAuditPicFollowupId(Long auditPicFollowupId) throws Exception {
		String hql = "FROM TrcAuditPicFollowupAttachment where trcAuditPicFollowup.auditPicFollowupId= :auditPicFollowupId and attachmentType.parameterDtlCode = '"+ParameterDetail.PARAM_DET_CODE_LETTER_ATTACH_DOC+"'";
		Query result = getSession().createQuery(hql);
		result.setParameter("auditPicFollowupId", auditPicFollowupId);
		return result.getResultList();
	}
}
