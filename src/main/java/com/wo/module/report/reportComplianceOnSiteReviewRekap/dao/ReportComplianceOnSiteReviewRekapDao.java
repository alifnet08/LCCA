package com.wo.module.report.reportComplianceOnSiteReviewRekap.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportComplianceOnSiteReviewRekap.model.ReportComplianceOnSiteReviewRekap;
import com.wo.module.report.reportGen.model.ReportGen;

public interface ReportComplianceOnSiteReviewRekapDao extends GenericDAO<ReportGen, Long>{

	@SuppressWarnings("rawtypes")
	public List<ReportComplianceOnSiteReviewRekap> getReportComplianceOnSiteReviewRekapByData(List<? extends SearchObject> searchCriteria);
	
	@SuppressWarnings("rawtypes")
	public List<Object[]> getReportComplianceOnSiteReviewRekapByObj(List<? extends SearchObject> searchCriteria);
	
}
