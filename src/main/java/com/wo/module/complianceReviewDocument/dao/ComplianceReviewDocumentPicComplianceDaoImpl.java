package com.wo.module.complianceReviewDocument.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.complianceReviewDocument.model.ComplianceReviewDocumentPicCompliance;

@Repository("complianceReviewDocumentPicComplianceDao")
public class ComplianceReviewDocumentPicComplianceDaoImpl extends GenericDAOHibernate<ComplianceReviewDocumentPicCompliance, Long> 
	implements ComplianceReviewDocumentPicComplianceDao{

	@SuppressWarnings("unchecked")
	@Override
	public List<ComplianceReviewDocumentPicCompliance> getComplianceReviewDocumentPicComplianceByComplianceReviewId(Long complianceReviewId) {
		
		String hql = "	FROM ComplianceReviewDocumentPicCompliance WHERE complianceReviewDocument.complianceReviewDocumentId = :complianceReviewDocumentId and enabledFlag = 'Y' ";
		Query result = getSession().createQuery(hql);
		result.setParameter("complianceReviewDocumentId", complianceReviewId);
		
		return (List<ComplianceReviewDocumentPicCompliance>) result.getResultList();
	}
	
}