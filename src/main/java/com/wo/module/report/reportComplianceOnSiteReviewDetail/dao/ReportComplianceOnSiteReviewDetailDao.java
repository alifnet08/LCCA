package com.wo.module.report.reportComplianceOnSiteReviewDetail.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportComplianceOnSiteReviewDetail.model.ReportComplianceOnSiteReviewDetail;
import com.wo.module.report.reportGen.model.ReportGen;

public interface ReportComplianceOnSiteReviewDetailDao extends GenericDAO<ReportGen, Long>{

	@SuppressWarnings("rawtypes")
	public List<ReportComplianceOnSiteReviewDetail> getReportComplianceOnSiterReviewByData(List<? extends SearchObject> searchCriteria);
	
	@SuppressWarnings("rawtypes")
	public List<Object[]> getReportComplianceOnSiterReviewByObj(List<? extends SearchObject> searchCriteria);
	
}
