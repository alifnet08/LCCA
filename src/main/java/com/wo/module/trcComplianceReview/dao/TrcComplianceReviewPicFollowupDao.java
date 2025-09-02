package com.wo.module.trcComplianceReview.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.trcComplianceReview.model.TrcComplianceReviewPicFollowup;

public interface TrcComplianceReviewPicFollowupDao extends GenericDAO<TrcComplianceReviewPicFollowup, Long> {

	public List<TrcComplianceReviewPicFollowup> getPICFollowupTrcByComplianceReviewId(Long complianceReviewId) throws Exception;
}
