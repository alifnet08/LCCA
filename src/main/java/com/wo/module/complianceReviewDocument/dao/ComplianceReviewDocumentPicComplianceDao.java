package com.wo.module.complianceReviewDocument.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.complianceReviewDocument.model.ComplianceReviewDocumentPicCompliance;

public interface ComplianceReviewDocumentPicComplianceDao extends GenericDAO<ComplianceReviewDocumentPicCompliance, Long> {
	
	public List<ComplianceReviewDocumentPicCompliance> getComplianceReviewDocumentPicComplianceByComplianceReviewId(Long complianceReviewId);
	
}