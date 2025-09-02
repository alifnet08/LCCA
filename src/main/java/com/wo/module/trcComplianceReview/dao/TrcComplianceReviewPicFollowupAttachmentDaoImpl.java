package com.wo.module.trcComplianceReview.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.trcComplianceReview.model.TrcComplianceReviewPicFollowupPointsAttachment;

@Repository("trcComplianceReviewPicFollowupAttachmentDao")
public class TrcComplianceReviewPicFollowupAttachmentDaoImpl extends GenericDAOHibernate<TrcComplianceReviewPicFollowupPointsAttachment, Long>
		implements TrcComplianceReviewPicFollowupAttachmentDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<TrcComplianceReviewPicFollowupPointsAttachment> getPICFollowupAttachmentTrcByPicFollowupId(Long complianceReviewPicFollowupId) throws Exception{
		 String hql = "FROM TrcComplianceReviewPicFollowupPointsAttachment where trcComplianceReviewPicFollowupPoints.complianceReviewPicFollowupPointsId = :complianceReviewPicFollowupId";
		 Query result = getSession().createQuery(hql);
		 result.setParameter("complianceReviewPicFollowupId", complianceReviewPicFollowupId);
		 return result.getResultList();
	 }
}
