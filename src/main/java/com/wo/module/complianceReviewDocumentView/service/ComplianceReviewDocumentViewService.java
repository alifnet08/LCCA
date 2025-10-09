package com.wo.module.complianceReviewDocumentView.service;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.complianceReviewDocumentView.model.ComplianceReviewDocumentView;

public interface ComplianceReviewDocumentViewService extends RetrieverDataPage<ComplianceReviewDocumentView>{

	public ComplianceReviewDocumentView findById(Long idLong);
}
