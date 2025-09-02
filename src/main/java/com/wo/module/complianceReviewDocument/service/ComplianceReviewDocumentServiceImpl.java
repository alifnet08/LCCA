package com.wo.module.complianceReviewDocument.service;

import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.complianceReviewDocument.dao.ComplianceReviewDocumentDao;
import com.wo.module.complianceReviewDocument.model.ComplianceReviewDocument;

@Transactional
@Service("complianceReviewDocumentService")
public class ComplianceReviewDocumentServiceImpl implements ComplianceReviewDocumentService {

	@Autowired
	@Qualifier("complianceReviewDocumentDao")
	private ComplianceReviewDocumentDao complianceReviewDocumentDao;

	@SuppressWarnings("rawtypes")
	@Override
	@Transactional(readOnly = true)
	public List<ComplianceReviewDocument> searchData(List<? extends SearchObject> searchCriteria, int first,
			int pageSize, String sortField, SortOrder sortOrder) throws Exception {
		return complianceReviewDocumentDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings("rawtypes")
	@Override
	@Transactional(readOnly=true)
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		return complianceReviewDocumentDao.searchCountData(searchCriteria);
	}

	public void save(ComplianceReviewDocument complianceReviewDocument) {
		complianceReviewDocumentDao.save(complianceReviewDocument);
	}

	public void update(ComplianceReviewDocument complianceReviewDocument) {
		complianceReviewDocumentDao.update(complianceReviewDocument);
	}

	public void delete(ComplianceReviewDocument complianceReviewDocument) {
		complianceReviewDocumentDao.delete(complianceReviewDocument);
	}

	public ComplianceReviewDocument findById(Long id) {
		return complianceReviewDocumentDao.findById(id);
	}

	public ComplianceReviewDocumentDao getComplianceReviewDocumentDao() {
		return complianceReviewDocumentDao;
	}

	public void setComplianceReviewDocumentDao(ComplianceReviewDocumentDao complianceReviewDocumentDao) {
		this.complianceReviewDocumentDao = complianceReviewDocumentDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<ComplianceReviewDocument> searchDataXLS(List<? extends SearchObject> searchCriteria) {
		return complianceReviewDocumentDao.searchDataXLS(searchCriteria);
	}

	@Override
	public Integer getComplianceDocumentByTypeAndNo(String documentType, String documentNo) throws Exception {
		return complianceReviewDocumentDao.getComplianceDocumentByTypeAndNo(documentType, documentNo);
	}

	@Override
	public Integer getComplianceDocumentByIdTypeAndNo(Long id, String documentType, String documentNo)
			throws Exception {
		return complianceReviewDocumentDao.getComplianceDocumentByIdTypeAndNo(id, documentType, documentNo);
	}

	@Override
	public int countRowByCreationYear(int year) {
		return complianceReviewDocumentDao.countRowByCreationYear(year);
	}

}