package com.wo.module.report.reportAuditDetail.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportAuditDetail.dao.ReportAuditDetailDao;
import com.wo.module.report.reportAuditDetail.model.ReportAuditDetail;

@Transactional
@Repository("reportAuditDetailService")
public class ReportAuditDetailServiceImpl implements ReportAuditDetailService{

	@Autowired
	@Qualifier("reportAuditDetailDao")
	private ReportAuditDetailDao reportAuditDetailDao;
	
	public ReportAuditDetailDao getReportAuditDetailDao() {
		return reportAuditDetailDao;
	}

	public void setReportAuditDetailDao(ReportAuditDetailDao reportAuditDetailDao) {
		this.reportAuditDetailDao = reportAuditDetailDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<ReportAuditDetail> getReportAuditDetailByData(List<? extends SearchObject> searchCriteria,String findingNameEn, String findingNameIn) {
		return reportAuditDetailDao.getReportAuditDetailByData(searchCriteria,findingNameEn, findingNameIn);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Object[]> getReportAuditDetailByObj(List<? extends SearchObject> searchCriteria) {
		return reportAuditDetailDao.getReportAuditDetailByObj(searchCriteria);
	}

	
	
}
