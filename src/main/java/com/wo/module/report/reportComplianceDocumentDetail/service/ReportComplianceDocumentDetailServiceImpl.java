package com.wo.module.report.reportComplianceDocumentDetail.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportComplianceDocumentDetail.dao.ReportComplianceDocumentDetailDao;
import com.wo.module.report.reportComplianceDocumentDetail.model.ReportComplianceDocumentDetail;

@Transactional
@Repository("reportComplianceDocumentDetailService")
public class ReportComplianceDocumentDetailServiceImpl implements ReportComplianceDocumentDetailService{

	@Autowired
	@Qualifier("reportComplianceDocumentDetailDao")
	private ReportComplianceDocumentDetailDao reportComplianceDocumentDetailDao;
	
	public ReportComplianceDocumentDetailDao getReportComplianceDocumentDetailDao() {
		return reportComplianceDocumentDetailDao;
	}

	public void setReportComplianceDocumentDetailDao(ReportComplianceDocumentDetailDao reportComplianceDocumentDetailDao) {
		this.reportComplianceDocumentDetailDao = reportComplianceDocumentDetailDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<ReportComplianceDocumentDetail> getReportComplianceDocumentDetailByData(
			List<? extends SearchObject> searchCriteria) {
		return reportComplianceDocumentDetailDao.getReportComplianceDocumentDetailByData(searchCriteria);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Object[]> getReportComplianceDocumentDetailByObj(List<? extends SearchObject> searchCriteria) {
		return reportComplianceDocumentDetailDao.getReportComplianceDocumentDetailByObj(searchCriteria);
	}

}
