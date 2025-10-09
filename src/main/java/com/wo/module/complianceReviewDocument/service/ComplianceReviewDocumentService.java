package com.wo.module.complianceReviewDocument.service;

import java.util.List;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.complianceReviewDocument.model.ComplianceReviewDocument;

public interface ComplianceReviewDocumentService extends RetrieverDataPage<ComplianceReviewDocument>{
	
	public void save (ComplianceReviewDocument complianceReviewDocument);
	
	public void update (ComplianceReviewDocument complianceReviewDocument);
	
	public void delete (ComplianceReviewDocument complianceReviewDocument);
	
	public ComplianceReviewDocument findById(Long id);
	
	@SuppressWarnings("rawtypes")
	public List<ComplianceReviewDocument> searchDataXLS(List<? extends SearchObject> searchCriteria);

	public Integer getComplianceDocumentByTypeAndNo(String documentType, String documentNo) throws Exception;
	
	public Integer getComplianceDocumentByIdTypeAndNo(Long id,String documentType, String documentNo) throws Exception;

	public int countRowByCreationYear(int year);
	
}

