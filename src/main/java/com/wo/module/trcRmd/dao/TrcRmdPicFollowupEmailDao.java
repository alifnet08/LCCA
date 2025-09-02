package com.wo.module.trcRmd.dao;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.engine.vo.SendEmailVO;
import com.wo.module.trcRmd.model.TrcRmdPicFollowupEmail;

public interface TrcRmdPicFollowupEmailDao extends  GenericDAO<TrcRmdPicFollowupEmail, Long> {

	SendEmailVO getEmailPicByFollowupEmailId(Long id) throws Exception;
	
}