package com.wo.module.report.reportSuratMasukRekap.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportSuratMasukRekap.dao.ReportSuratMasukRekapDao;
import com.wo.module.report.reportSuratMasukRekap.model.ReportSuratMasukRekap;

@Transactional
@Repository("reportSuratMasukRekapService")
public class ReportSuratMasukRekapServiceImpl implements ReportSuratMasukRekapService{

	@Autowired
	@Qualifier("reportSuratMasukRekapDao")
	private ReportSuratMasukRekapDao reportSuratMasukRekapDao;
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<ReportSuratMasukRekap> getReportSuratMasukRekapByData(List<? extends SearchObject> searchCriteria) {
		return reportSuratMasukRekapDao.getReportSuratMasukRekapByData(searchCriteria);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Object[]> getReportSuratMasukRekapByObj(List<? extends SearchObject> searchCriteria) {
		return reportSuratMasukRekapDao.getReportSuratMasukRekapByObj(searchCriteria);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<ReportSuratMasukRekap> getReportSuratMasukRekapByTipeSurat(
			List<? extends SearchObject> searchCriteria) {
		return reportSuratMasukRekapDao.getReportSuratMasukRekapByTipeSurat(searchCriteria);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Object[]> getReportSuratMasukRekapByTipeSuratObj(List<? extends SearchObject> searchCriteria) {
		return reportSuratMasukRekapDao.getReportSuratMasukRekapByTipeSuratObj(searchCriteria);
	}

}
