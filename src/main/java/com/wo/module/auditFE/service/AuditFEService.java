package com.wo.module.auditFE.service;

import com.wo.module.auditFE.vo.AuditFEVO;
import com.wo.module.common.paging.RetrieverDataPage;

public interface AuditFEService extends RetrieverDataPage<AuditFEVO> {

	public AuditFEVO searchForDetail(Long id);
}
