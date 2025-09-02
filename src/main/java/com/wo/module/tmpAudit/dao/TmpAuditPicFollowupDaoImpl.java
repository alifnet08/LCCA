package com.wo.module.tmpAudit.dao;

import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.tmpAudit.model.TmpAuditPicFollowup;

@Repository("tmpAuditPicFollowupDao")
public class TmpAuditPicFollowupDaoImpl extends GenericDAOHibernate<TmpAuditPicFollowup, Long> implements TmpAuditPicFollowupDao{

	
}
