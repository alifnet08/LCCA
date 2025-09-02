package com.wo.module.report.reportSuratMasukDetail.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportSuratMasukDetail.dao.ReportSuratMasukDetailDao;
import com.wo.module.report.reportSuratMasukDetail.model.ReportSuratMasukDetail;

@Transactional
@Repository("reportSuratMasukDetailService")
public class ReportSuratMasukDetailServiceImpl implements ReportSuratMasukDetailService{

	@Autowired
	@Qualifier("reportSuratMasukDetailDao")
	private ReportSuratMasukDetailDao reportSuratMasukDetailDao;
	
	public ReportSuratMasukDetailDao getReportSuratMasukDetailDao() {
		return reportSuratMasukDetailDao;
	}

	public void setReportSuratMasukDetailDao(ReportSuratMasukDetailDao reportSuratMasukDetailDao) {
		this.reportSuratMasukDetailDao = reportSuratMasukDetailDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<ReportSuratMasukDetail> getReportSuratMasukDetailByData(List<? extends SearchObject> searchCriteria) {
		return reportSuratMasukDetailDao.getReportSuratMasukDetailByData(searchCriteria);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Object[]> getReportSuratMasukDetailByObj(List<? extends SearchObject> searchCriteria) {
		return reportSuratMasukDetailDao.getReportSuratMasukDetailByObj(searchCriteria);
	}

}
