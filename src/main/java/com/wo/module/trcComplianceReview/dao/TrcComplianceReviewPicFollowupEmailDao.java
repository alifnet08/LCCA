package com.wo.module.trcComplianceReview.dao;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.engine.vo.SendEmailVO;
import com.wo.module.trcComplianceReview.model.TrcComplianceReviewPicFollowupEmail;

public interface TrcComplianceReviewPicFollowupEmailDao extends GenericDAO<TrcComplianceReviewPicFollowupEmail, Long> {

	SendEmailVO getEmailPicByFollowupEmailId(Long id) throws Exception;

}
