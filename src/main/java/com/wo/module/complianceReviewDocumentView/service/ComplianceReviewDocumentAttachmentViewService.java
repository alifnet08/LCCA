package com.wo.module.complianceReviewDocumentView.service;

import java.util.List;

import com.wo.module.complianceReviewDocumentView.model.ComplianceReviewDocumentAttachmentView;

public interface ComplianceReviewDocumentAttachmentViewService {

	public List<ComplianceReviewDocumentAttachmentView> getComplianceReviewDocumentAttachmentViewByComplianceDocumentId(Long complianceReviewDocumentViewId) throws Exception;
}
