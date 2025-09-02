package com.wo.module.trcComplianceReview.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.trcComplianceReview.model.TrcComplianceReviewPicFollowupPointsAttachment;

public interface TrcComplianceReviewPicFollowupAttachmentDao extends  GenericDAO<TrcComplianceReviewPicFollowupPointsAttachment, Long>{

	public List<TrcComplianceReviewPicFollowupPointsAttachment> getPICFollowupAttachmentTrcByPicFollowupId(Long complianceReviewPicFollowupId) throws Exception;
	
}
