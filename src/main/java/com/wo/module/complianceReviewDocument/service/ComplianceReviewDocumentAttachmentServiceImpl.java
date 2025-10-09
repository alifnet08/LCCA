package com.wo.module.complianceReviewDocument.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.complianceReviewDocument.dao.ComplianceReviewDocumentAttachmentDao;
import com.wo.module.complianceReviewDocument.model.ComplianceReviewDocumentAttachment;

@Transactional
@Service("complianceReviewDocumentAttachmentService")
public class ComplianceReviewDocumentAttachmentServiceImpl implements ComplianceReviewDocumentAttachmentService{

	@Autowired
    @Qualifier("complianceReviewDocumentAttachmentDao")
	private ComplianceReviewDocumentAttachmentDao complianceReviewDocumentAttachmentDao;
	
	public void save(ComplianceReviewDocumentAttachment complianceReviewDocumentAttachment) {
		complianceReviewDocumentAttachmentDao.save(complianceReviewDocumentAttachment);
	}

	@Override
	public void update(ComplianceReviewDocumentAttachment complianceReviewDocumentAttachment) {
		complianceReviewDocumentAttachmentDao.update(complianceReviewDocumentAttachment);
	}

	@Override
	public void delete(ComplianceReviewDocumentAttachment complianceReviewDocumentAttachment) {
		complianceReviewDocumentAttachmentDao.delete(complianceReviewDocumentAttachment);
	}

	@Override
	public ComplianceReviewDocumentAttachment findById(Long id) {
		return complianceReviewDocumentAttachmentDao.getById(id);
	}

	@Override
	public List<ComplianceReviewDocumentAttachment> getComplianceReviewDocumentAttachmentByComplianceDocumentId(
			Long complianceReviewDocumentId) throws Exception {
		return complianceReviewDocumentAttachmentDao.getComplianceReviewDocumentByComplianceDocument(complianceReviewDocumentId);
	}

	public ComplianceReviewDocumentAttachmentDao getComplianceReviewDocumentAttachmentDao() {
		return complianceReviewDocumentAttachmentDao;
	}

	public void setComplianceReviewDocumentAttachmentDao(
			ComplianceReviewDocumentAttachmentDao complianceReviewDocumentAttachmentDao) {
		this.complianceReviewDocumentAttachmentDao = complianceReviewDocumentAttachmentDao;
	}
}