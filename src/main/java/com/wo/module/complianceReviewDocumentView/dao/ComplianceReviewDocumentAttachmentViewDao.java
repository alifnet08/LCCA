package com.wo.module.complianceReviewDocumentView.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.complianceReviewDocumentView.model.ComplianceReviewDocumentAttachmentView;

public interface ComplianceReviewDocumentAttachmentViewDao extends GenericDAO<ComplianceReviewDocumentAttachmentView, Long>{

	public List<ComplianceReviewDocumentAttachmentView> getComplianceReviewDocumentViewByComplianceDocument(Long complianceReviewViewId) throws Exception;
}
