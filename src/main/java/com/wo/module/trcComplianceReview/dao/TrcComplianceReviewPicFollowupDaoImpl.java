package com.wo.module.trcComplianceReview.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.trcComplianceReview.model.TrcComplianceReviewPicFollowup;

@Repository("trcComplianceReviewPicFollowupDao")
public class TrcComplianceReviewPicFollowupDaoImpl extends GenericDAOHibernate<TrcComplianceReviewPicFollowup, Long>
		implements TrcComplianceReviewPicFollowupDao {
	
	@SuppressWarnings("unchecked")
	@Override
	public List<TrcComplianceReviewPicFollowup> getPICFollowupTrcByComplianceReviewId(Long complianceReviewId) throws Exception{
		 String hql = "FROM TrcComplianceReviewPicFollowup where trcComplianceReviewPicFollowup.trcComplianceReview.complianceReviewId = :complianceReviewId";
		 Query result = getSession().createQuery(hql);
		 result.setParameter("complianceReviewId", complianceReviewId);
		 return result.getResultList();
	 }

}
