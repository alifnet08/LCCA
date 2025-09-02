package com.wo.module.trcCorrespondence.dao;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.engine.vo.SendEmailVO;
import com.wo.module.trcCorrespondence.model.TrcCorrespondencePicFollowupEmail;

public interface TrcCorrespondencePicFollowupEmailDao extends  GenericDAO<TrcCorrespondencePicFollowupEmail, Long> {

	SendEmailVO getEmailPicByFollowupEmailId(Long emailFollowId);

}
