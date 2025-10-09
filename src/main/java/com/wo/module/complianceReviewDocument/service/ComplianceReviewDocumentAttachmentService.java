package com.wo.module.complianceReviewDocument.service;

import java.util.List;

import com.wo.module.complianceReviewDocument.model.ComplianceReviewDocumentAttachment;

public interface ComplianceReviewDocumentAttachmentService {
	
	public void save(ComplianceReviewDocumentAttachment complianceReviewDocumentAttachment);
	
	public void update(ComplianceReviewDocumentAttachment complianceReviewDocumentAttachment);
	
	public void delete(ComplianceReviewDocumentAttachment complianceReviewDocumentAttachment);
	
	public ComplianceReviewDocumentAttachment findById(Long id);
	
	public List<ComplianceReviewDocumentAttachment> getComplianceReviewDocumentAttachmentByComplianceDocumentId(Long complianceReviewDocumentId) throws Exception;
}