package com.wo.module.complianceReviewDocumentView.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.complianceReviewDocumentView.model.ComplianceReviewDocumentAttachmentView;

@Repository("complianceReviewDocumentAttachmentViewDao")
public class ComplianceReviewDocumentAttachmentViewDaoImpl extends GenericDAOHibernate<ComplianceReviewDocumentAttachmentView, Long>
	implements ComplianceReviewDocumentAttachmentViewDao{

	@SuppressWarnings("unchecked")
	@Override
	public List<ComplianceReviewDocumentAttachmentView> getComplianceReviewDocumentViewByComplianceDocument(
			Long complianceReviewViewId) throws Exception {
		String hql = "	FROM ComplianceReviewDocumentAttachmentView WHERE complianceReviewDocumentView.complianceReviewDocumentId =:complianceReviewDocumentId and enabledFlag = 'Y' ";
		Query result = getSession().createQuery(hql);
		result.setParameter("complianceReviewDocumentId", complianceReviewViewId);
		
		return (List<ComplianceReviewDocumentAttachmentView>) result.getResultList();
	}

}
