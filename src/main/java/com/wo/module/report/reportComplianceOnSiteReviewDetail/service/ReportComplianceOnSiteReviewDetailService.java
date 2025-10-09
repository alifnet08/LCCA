package com.wo.module.report.reportComplianceOnSiteReviewDetail.service;

import java.util.List;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportComplianceOnSiteReviewDetail.model.ReportComplianceOnSiteReviewDetail;

public interface ReportComplianceOnSiteReviewDetailService {

	@SuppressWarnings("rawtypes")
	public List<ReportComplianceOnSiteReviewDetail> getReportComplianceOnSiteReviewDetailByData(List<? extends SearchObject> searchCriteria);
	
	@SuppressWarnings("rawtypes")
	public List<Object[]> getReportComplianceOnSiteReviewDetailByObj(List<? extends SearchObject> searchCriteria);
	
}
