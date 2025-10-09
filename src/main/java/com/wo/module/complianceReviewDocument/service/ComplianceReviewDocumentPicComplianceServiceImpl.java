package com.wo.module.complianceReviewDocument.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.complianceReviewDocument.dao.ComplianceReviewDocumentPicComplianceDao;

@Transactional
@Service("complianceReviewDocumentPicComplianceService")
public class ComplianceReviewDocumentPicComplianceServiceImpl implements ComplianceReviewDocumentPicComplianceService {
	
	@Autowired
    @Qualifier("complianceReviewDocumentPicComplianceDao")
	private ComplianceReviewDocumentPicComplianceDao complianceReviewDocumentPicComplianceDao;

	public ComplianceReviewDocumentPicComplianceDao getComplianceReviewDocumentPicComplianceDao() {
		return complianceReviewDocumentPicComplianceDao;
	}

	public void setComplianceReviewDocumentPicComplianceDao(ComplianceReviewDocumentPicComplianceDao complianceReviewDocumentPicComplianceDao) {
		this.complianceReviewDocumentPicComplianceDao = complianceReviewDocumentPicComplianceDao;
	}
}