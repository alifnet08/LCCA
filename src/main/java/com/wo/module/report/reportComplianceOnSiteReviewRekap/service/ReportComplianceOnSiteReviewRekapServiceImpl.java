package com.wo.module.report.reportComplianceOnSiteReviewRekap.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportComplianceOnSiteReviewRekap.dao.ReportComplianceOnSiteReviewRekapDao;
import com.wo.module.report.reportComplianceOnSiteReviewRekap.model.ReportComplianceOnSiteReviewRekap;

@Transactional
@Repository("reportComplianceOnSiteReviewRekapService")
public class ReportComplianceOnSiteReviewRekapServiceImpl implements ReportComplianceOnSiteReviewRekapService{

	@Autowired
	@Qualifier("reportComplianceOnSiteReviewRekapDao")
	private ReportComplianceOnSiteReviewRekapDao reportComplianceOnSiteReviewRekapDao;
	
	public ReportComplianceOnSiteReviewRekapDao getReportComplianceOnSiteReviewRekapDao() {
		return reportComplianceOnSiteReviewRekapDao;
	}

	public void setReportComplianceOnSiteReviewRekapDao(
			ReportComplianceOnSiteReviewRekapDao reportComplianceOnSiteReviewRekapDao) {
		this.reportComplianceOnSiteReviewRekapDao = reportComplianceOnSiteReviewRekapDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<ReportComplianceOnSiteReviewRekap> getReportComplianceOnSiteReviewRekapByData(
			List<? extends SearchObject> searchCriteria) {
		return reportComplianceOnSiteReviewRekapDao.getReportComplianceOnSiteReviewRekapByData(searchCriteria);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Object[]> getReportComplianceOnSiteReviewRekapByObj(List<? extends SearchObject> searchCriteria) {
		return reportComplianceOnSiteReviewRekapDao.getReportComplianceOnSiteReviewRekapByObj(searchCriteria);
	}

}
