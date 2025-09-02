package com.wo.module.complianceReviewDocumentView.service;

import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.complianceReviewDocumentView.dao.ComplianceReviewDocumentViewDao;
import com.wo.module.complianceReviewDocumentView.model.ComplianceReviewDocumentView;

@Transactional
@Repository("complianceReviewDocumentViewService")
public class ComplianceReviewDocumentViewServiceImpl implements ComplianceReviewDocumentViewService{

	@Autowired
	@Qualifier("complianceReviewDocumentViewDao")
	private ComplianceReviewDocumentViewDao complianceReviewDocumentViewDao;
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<ComplianceReviewDocumentView> searchData(List<? extends SearchObject> searchCriteria, int first,
			int pageSize, String sortField, SortOrder sortOrder) throws Exception {
		return complianceReviewDocumentViewDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		return complianceReviewDocumentViewDao.searchCountData(searchCriteria);
	}

	public ComplianceReviewDocumentViewDao getComplianceReviewDocumentViewDao() {
		return complianceReviewDocumentViewDao;
	}

	public void setComplianceReviewDocumentViewDao(ComplianceReviewDocumentViewDao complianceReviewDocumentViewDao) {
		this.complianceReviewDocumentViewDao = complianceReviewDocumentViewDao;
	}

	@Override
	public ComplianceReviewDocumentView findById(Long idLong) {
		return complianceReviewDocumentViewDao.findById(idLong);
	}

}
