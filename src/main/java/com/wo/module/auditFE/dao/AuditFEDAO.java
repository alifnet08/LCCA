package com.wo.module.auditFE.dao;

import com.wo.module.auditFE.vo.AuditFEVO;
import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.trcAudit.model.TrcAudit;

public interface AuditFEDAO extends GenericDAO<TrcAudit, Long>, RetrieverDataPage<AuditFEVO> {

	public AuditFEVO searchForDetail(Long id);
}
