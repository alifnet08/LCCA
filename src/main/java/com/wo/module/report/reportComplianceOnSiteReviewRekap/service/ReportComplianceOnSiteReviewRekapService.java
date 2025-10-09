package com.wo.module.report.reportComplianceOnSiteReviewRekap.service;

import java.util.List;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportComplianceOnSiteReviewRekap.model.ReportComplianceOnSiteReviewRekap;

public interface ReportComplianceOnSiteReviewRekapService {

	@SuppressWarnings("rawtypes")
	public List<ReportComplianceOnSiteReviewRekap> getReportComplianceOnSiteReviewRekapByData(List<? extends SearchObject> searchCriteria);
	
	@SuppressWarnings("rawtypes")
	public List<Object[]> getReportComplianceOnSiteReviewRekapByObj(List<? extends SearchObject> searchCriteria);
}
