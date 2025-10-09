package com.wo.module.trcAuditView.dao;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.trcAudit.model.TrcAudit;
import com.wo.module.trcAuditView.vo.TrcAuditViewVO;

public interface TrcAuditViewDao extends GenericDAO<TrcAudit, Long>, RetrieverDataPage<TrcAuditViewVO> {

}
