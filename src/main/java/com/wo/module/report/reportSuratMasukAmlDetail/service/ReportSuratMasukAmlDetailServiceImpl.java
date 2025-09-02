package com.wo.module.report.reportSuratMasukAmlDetail.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportSuratMasukAmlDetail.dao.ReportSuratMasukAmlDetailDao;
import com.wo.module.report.reportSuratMasukAmlDetail.model.ReportSuratMasukAmlDetail;

@Transactional
@Repository("reportSuratMasukAmlDetailService")
public class ReportSuratMasukAmlDetailServiceImpl implements ReportSuratMasukAmlDetailService{

	@Autowired
	@Qualifier("reportSuratMasukAmlDetailDao")
	private ReportSuratMasukAmlDetailDao reportSuratMasukAmlDetailDao;
	
	public ReportSuratMasukAmlDetailDao getReportSuratMasukAmlDetailDao() {
		return reportSuratMasukAmlDetailDao;
	}

	public void setReportSuratMasukAmlDetailDao(ReportSuratMasukAmlDetailDao reportSuratMasukAmlDetailDao) {
		this.reportSuratMasukAmlDetailDao = reportSuratMasukAmlDetailDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<ReportSuratMasukAmlDetail> getReportSuratMasukAmlDetailByData(
			List<? extends SearchObject> searchCriteria) {
		return reportSuratMasukAmlDetailDao.getReportSuratMasukAmlDetailByData(searchCriteria);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Object[]> getReportSuratMasukAmlDetailByObj(List<? extends SearchObject> searchCriteria) {
		return reportSuratMasukAmlDetailDao.getReportSuratMasukAmlDetailByObj(searchCriteria);
	}

	
	
}
