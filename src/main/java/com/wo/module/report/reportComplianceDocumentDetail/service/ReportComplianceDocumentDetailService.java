package com.wo.module.report.reportComplianceDocumentDetail.service;

import java.util.List;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportComplianceDocumentDetail.model.ReportComplianceDocumentDetail;

public interface ReportComplianceDocumentDetailService {

	@SuppressWarnings("rawtypes")
	public List<ReportComplianceDocumentDetail>	getReportComplianceDocumentDetailByData(List<? extends SearchObject> searchCriteria);
	
	@SuppressWarnings("rawtypes")
	public List<Object[]>	getReportComplianceDocumentDetailByObj(List<? extends SearchObject> searchCriteria);
	
}
