package com.wo.module.trcAuditVerification.dao;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.trcAudit.model.TrcAudit;
import com.wo.module.trcAuditVerification.vo.TrcAuditVerificationSearchVO;

public interface TrcAuditVerificationDao
		extends GenericDAO<TrcAudit, Long>, RetrieverDataPage<TrcAuditVerificationSearchVO> {

}
