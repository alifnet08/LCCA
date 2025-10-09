package com.wo.module.report.reportComplianceOnSiteReviewDetail.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportComplianceOnSiteReviewDetail.dao.ReportComplianceOnSiteReviewDetailDao;
import com.wo.module.report.reportComplianceOnSiteReviewDetail.model.ReportComplianceOnSiteReviewDetail;

@Transactional
@Repository("reportComplianceOnSiteReviewDetailService")
public class ReportComplianceOnSiteReviewDetailServiceImpl implements ReportComplianceOnSiteReviewDetailService{

	@Autowired
	@Qualifier("reportComplianceOnSiteReviewDetailDao")
	private ReportComplianceOnSiteReviewDetailDao reportComplianceOnSiteReviewDetailDao;
	
	public ReportComplianceOnSiteReviewDetailDao getReportComplianceOnSiteReviewDetailDao() {
		return reportComplianceOnSiteReviewDetailDao;
	}

	public void setReportComplianceOnSiteReviewDetailDao(
			ReportComplianceOnSiteReviewDetailDao reportComplianceOnSiteReviewDetailDao) {
		this.reportComplianceOnSiteReviewDetailDao = reportComplianceOnSiteReviewDetailDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<ReportComplianceOnSiteReviewDetail> getReportComplianceOnSiteReviewDetailByData(
			List<? extends SearchObject> searchCriteria) {
		return reportComplianceOnSiteReviewDetailDao.getReportComplianceOnSiterReviewByData(searchCriteria);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Object[]> getReportComplianceOnSiteReviewDetailByObj(List<? extends SearchObject> searchCriteria) {
		return reportComplianceOnSiteReviewDetailDao.getReportComplianceOnSiterReviewByObj(searchCriteria);
	}

}
