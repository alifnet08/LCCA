package com.wo.module.report.reportSocializationRekap.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportSocializationRekap.dao.ReportSocializationRekapDao;
import com.wo.module.report.reportSocializationRekap.model.ReportSocializationRekap;

@Transactional
@Repository("reportSocializationRekapService")
public class ReportSocializationRekapServiceImpl implements ReportSocializationRekapService{

	@Autowired
	@Qualifier("reportSocializationRekapDao")
	private ReportSocializationRekapDao reportSocializationRekapDao;
	
	public ReportSocializationRekapDao getReportSocializationRekapDao() {
		return reportSocializationRekapDao;
	}

	public void setReportSocializationRekapDao(ReportSocializationRekapDao reportSocializationRekapDao) {
		this.reportSocializationRekapDao = reportSocializationRekapDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Object[]> getReportSocializationRekapByKategoriDokumen(List<? extends SearchObject> searchCriteria) {
		return reportSocializationRekapDao.getReportSocializationRekapDataObjByKategoriDokumen(searchCriteria);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Object[]> getReportSocializationRekapByTotalSosialisasi(List<? extends SearchObject> searchCriteria) {
		return reportSocializationRekapDao.getReportSocializationRekapDataObjByTotalSosialisasi(searchCriteria);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<ReportSocializationRekap> getReportSocializationRekapByKategoriDokumenData(
			List<? extends SearchObject> searchCriteria) {
		return reportSocializationRekapDao.getReportSocializationRekapDataByKategoriDokumen(searchCriteria);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<ReportSocializationRekap> getReportSocializationRekapByTotalSosialisasiData(
			List<? extends SearchObject> searchCriteria) {
		return reportSocializationRekapDao.getReportSocializationRekapDataByTotalSosialisasi(searchCriteria);
	}
}
