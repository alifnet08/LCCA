package com.wo.module.complianceReviewDocumentView.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.complianceReviewDocumentView.dao.ComplianceReviewDocumentAttachmentViewDao;
import com.wo.module.complianceReviewDocumentView.model.ComplianceReviewDocumentAttachmentView;

@Transactional
@Service("complianceReviewDocumentAttachmentViewService")
public class ComplianceReviewDocumentAttachmentViewServiceImpl implements ComplianceReviewDocumentAttachmentViewService{

	@Autowired
    @Qualifier("complianceReviewDocumentAttachmentViewDao")
	private ComplianceReviewDocumentAttachmentViewDao complianceReviewDocumentAttachmentViewDao;
	
	public ComplianceReviewDocumentAttachmentViewDao getComplianceReviewDocumentAttachmentViewDao() {
		return complianceReviewDocumentAttachmentViewDao;
	}

	public void setComplianceReviewDocumentAttachmentViewDao(
			ComplianceReviewDocumentAttachmentViewDao complianceReviewDocumentAttachmentViewDao) {
		this.complianceReviewDocumentAttachmentViewDao = complianceReviewDocumentAttachmentViewDao;
	}

	@Override
	public List<ComplianceReviewDocumentAttachmentView> getComplianceReviewDocumentAttachmentViewByComplianceDocumentId(
			Long complianceReviewDocumentViewId) throws Exception {
		return complianceReviewDocumentAttachmentViewDao.getComplianceReviewDocumentViewByComplianceDocument(complianceReviewDocumentViewId);
	}
	
}
