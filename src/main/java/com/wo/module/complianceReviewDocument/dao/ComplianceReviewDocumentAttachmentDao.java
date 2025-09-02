package com.wo.module.complianceReviewDocument.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.complianceReviewDocument.model.ComplianceReviewDocumentAttachment;

public interface ComplianceReviewDocumentAttachmentDao extends GenericDAO<ComplianceReviewDocumentAttachment, Long>{
	
	public List<ComplianceReviewDocumentAttachment> getComplianceReviewDocumentByComplianceDocument(Long complianceReviewId) throws Exception;
}