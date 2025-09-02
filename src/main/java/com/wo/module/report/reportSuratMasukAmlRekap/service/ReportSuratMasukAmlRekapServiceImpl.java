package com.wo.module.report.reportSuratMasukAmlRekap.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportSuratMasukAmlRekap.dao.ReportSuratMasukAmlRekapDao;
import com.wo.module.report.reportSuratMasukAmlRekap.model.ReportSuratMasukAmlRekap;

@Transactional
@Repository("reportSuratMasukAmlRekapService")
public class ReportSuratMasukAmlRekapServiceImpl implements ReportSuratMasukAmlRekapService{

	@Autowired
	@Qualifier("reportSuratMasukAmlRekapDao")
	private ReportSuratMasukAmlRekapDao reportSuratMasukAmlRekapDao;
	
	public ReportSuratMasukAmlRekapDao getReportSuratMasukAmlRekapDao() {
		return reportSuratMasukAmlRekapDao;
	}

	public void setReportSuratMasukAmlRekapDao(ReportSuratMasukAmlRekapDao reportSuratMasukAmlRekapDao) {
		this.reportSuratMasukAmlRekapDao = reportSuratMasukAmlRekapDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<ReportSuratMasukAmlRekap> getReportSuratMasukAmlRekapByData(
			List<? extends SearchObject> searchCriteria) {
		return reportSuratMasukAmlRekapDao.getReportSuratMasukAmlRekapByData(searchCriteria);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Object[]> getReportSuratMasukAmlRekapByObj(List<? extends SearchObject> searchCriteria) {
		return reportSuratMasukAmlRekapDao.getReportSuratMasukAmlRekapByObj(searchCriteria);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<ReportSuratMasukAmlRekap> getReportSuratMasukAmlRekapByTipeSurat(
			List<? extends SearchObject> searchCriteria) {
		return reportSuratMasukAmlRekapDao.getReportSuratMasukAmlRekapByTipeSurat(searchCriteria);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Object[]> getReportSuratMasukAmlRekapByTipeSuratObj(List<? extends SearchObject> searchCriteria) {
		return reportSuratMasukAmlRekapDao.getReportSuratMasukAmlRekapByTipeSuratObj(searchCriteria);
	}

}
