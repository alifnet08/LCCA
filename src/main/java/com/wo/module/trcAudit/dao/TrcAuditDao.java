package com.wo.module.trcAudit.dao;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.trcAudit.model.TrcAudit;
import com.wo.module.trcAudit.vo.TrcAuditVO;

public interface TrcAuditDao extends GenericDAO<TrcAudit, Long>, RetrieverDataPage<TrcAuditVO> {

}
