package com.wo.module.tmpAudit.dao;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.tmpAudit.model.TmpAuditCheckPoint;

public interface TmpAuditCheckPointDao extends GenericDAO<TmpAuditCheckPoint, Long> {

	public Boolean hasDuplicateBankCommitment(Long auditId,String auditFinding,String bankResponse,String bankCommitment);
	
}
