package com.wo.module.trcAudit.dao;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.engine.vo.SendEmailVO;
import com.wo.module.trcAudit.model.TrcAuditPicFollowupEmail;

public interface TrcAuditPICFollowupEmailDao extends GenericDAO<TrcAuditPicFollowupEmail, Long> {
	SendEmailVO getEmailPicByFollowupEmailId(Long id) throws Exception;
}
