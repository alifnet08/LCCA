package com.wo.module.complianceReviewDocument.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.complianceReviewDocument.model.ComplianceReviewDocument;

public interface ComplianceReviewDocumentDao extends GenericDAO<ComplianceReviewDocument, Long>, RetrieverDataPage<ComplianceReviewDocument>{
	
	@SuppressWarnings("rawtypes")
	public List<ComplianceReviewDocument> searchDataXLS(List<? extends SearchObject> searchCriteria );
	
	public Integer getComplianceDocumentByTypeAndNo(String documentType, String documentNo) throws Exception;
	
	public Integer getComplianceDocumentByIdTypeAndNo(Long id,String documentType, String documentNo) throws Exception;

	public int countRowByCreationYear(int year);
	
}