package com.wo.module.complianceReviewDocument.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.complianceReviewDocument.model.ComplianceReviewDocumentAttachment;

@Repository("complianceReviewDocumentAttachmentDao")
public class ComplianceReviewDocumentAttachmentDaoImpl extends GenericDAOHibernate<ComplianceReviewDocumentAttachment, Long>
	implements ComplianceReviewDocumentAttachmentDao{

	@SuppressWarnings("unchecked")
	@Override
	public List<ComplianceReviewDocumentAttachment> getComplianceReviewDocumentByComplianceDocument(
			Long complianceReviewId) throws Exception {
		String hql = "	FROM ComplianceReviewDocumentAttachment WHERE complianceReviewDocument.complianceReviewDocumentId =:complianceReviewDocumentId and enabledFlag = 'Y' ";
		Query result = getSession().createQuery(hql);
		result.setParameter("complianceReviewDocumentId", complianceReviewId);
		
		return (List<ComplianceReviewDocumentAttachment>) result.getResultList();
	}
	
}