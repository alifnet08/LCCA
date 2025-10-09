package com.wo.module.trcAuditVerification.service;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.trcAudit.model.TrcAudit;
import com.wo.module.trcAudit.model.TrcAuditPicFollowup;
import com.wo.module.trcAuditVerification.vo.TrcAuditVerificationSearchVO;
import com.wo.module.user.model.User;

public interface TrcAuditVerificationService extends RetrieverDataPage<TrcAuditVerificationSearchVO> {

	public void processConfirm(TrcAuditPicFollowup trcAuditPicFollowup, User user) throws Exception;

	void processConfirm(TrcAudit trcAudit, User user) throws Exception;
}
