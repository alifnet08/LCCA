package com.wo.module.report.reportAuditRekap.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportAuditRekap.dao.ReportAuditRekapDao;
import com.wo.module.report.reportAuditRekap.model.ReportAuditRekap;

@Transactional
@Repository("reportAuditRekapService")
public class ReportAuditRekapServiceImpl implements ReportAuditRekapService{

	@Autowired
	@Qualifier("reportAuditRekapDao")
	private ReportAuditRekapDao reportAuditRekapDao;
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<ReportAuditRekap> getReportAuditRekapByData(List<? extends SearchObject> searchCriteria, String findingNameEn, String findingNameIn) {
		return reportAuditRekapDao.getReportAuditRekapByData(searchCriteria, findingNameEn, findingNameIn);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Object[]> getReportAuditRekapByObj(List<? extends SearchObject> searchCriteria) {
		return reportAuditRekapDao.getReportAuditRekapByObj(searchCriteria);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<ReportAuditRekap> getReportAuditRekapByTemplateName(List<? extends SearchObject> searchCriteria,
			String findingNameEn, String findingNameIn) {
		return reportAuditRekapDao.getReportAuditRekapByTemplateName(searchCriteria, findingNameEn, findingNameIn);
	}

}
