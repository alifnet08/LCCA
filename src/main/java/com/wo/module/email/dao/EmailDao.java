package com.wo.module.email.dao;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.email.vo.EmailVO;
import com.wo.module.trcAudit.model.TrcAudit;

public interface EmailDao extends GenericDAO<TrcAudit, Long>, RetrieverDataPage<EmailVO> {
}
