package com.wo.module.complianceReviewDocumentView.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.complianceReviewDocumentView.model.ComplianceReviewDocumentPicComplianceView;

@Repository("complianceReviewDocumentPicComplianceViewDao")
public class ComplianceReviewDocumentPicComplianceViewDaoImpl extends GenericDAOHibernate<ComplianceReviewDocumentPicComplianceView, Long>
	implements ComplianceReviewDocumentPicComplianceViewDao{

	@SuppressWarnings("unchecked")
	@Override
	public List<ComplianceReviewDocumentPicComplianceView> getComplianceReviewDocumentPicComplianceViewByComplianceReviewId(
			Long complianceReviewViewId) {
		String hql = "	FROM ComplianceReviewDocumentPicComplianceView WHERE complianceReviewDocumentView.complianceReviewDocumentId = :complianceReviewDocumentId and enabledFlag = 'Y' ";
		Query result = getSession().createQuery(hql);
		result.setParameter("complianceReviewDocumentId", complianceReviewViewId);
		
		return (List<ComplianceReviewDocumentPicComplianceView>) result.getResultList();
	}

}
