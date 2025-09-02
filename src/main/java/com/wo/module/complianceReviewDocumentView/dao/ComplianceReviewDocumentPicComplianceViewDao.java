package com.wo.module.complianceReviewDocumentView.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.complianceReviewDocumentView.model.ComplianceReviewDocumentPicComplianceView;

public interface ComplianceReviewDocumentPicComplianceViewDao extends GenericDAO<ComplianceReviewDocumentPicComplianceView, Long>{
	
	public List<ComplianceReviewDocumentPicComplianceView> getComplianceReviewDocumentPicComplianceViewByComplianceReviewId(Long complianceReviewViewId);
}
