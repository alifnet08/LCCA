package com.wo.module.report.reportOutgoingLetterRekap.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportOutgoingLetterRekap.dao.ReportOutgoingLetterRekapDao;
import com.wo.module.report.reportOutgoingLetterRekap.model.ReportOutgoingLetterRekap;

@Transactional
@Repository("reportOutgoingLetterRekapService")
public class ReportOutgoingLetterRekapServiceImpl implements ReportOutgoingLetterRekapService{
	
	@Autowired
	@Qualifier("reportOutgoingLetterRekapDao")
	private ReportOutgoingLetterRekapDao reportOutgoingLetterRekapDao;

	public ReportOutgoingLetterRekapDao getReportOutgoingLetterRekapDao() {
		return reportOutgoingLetterRekapDao;
	}

	public void setReportOutgoingLetterRekapDao(ReportOutgoingLetterRekapDao reportOutgoingLetterRekapDao) {
		this.reportOutgoingLetterRekapDao = reportOutgoingLetterRekapDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<ReportOutgoingLetterRekap> getReportOutgoingLetterRekapByTujuanSuratData(
			List<? extends SearchObject> searchCriteria) {
		return reportOutgoingLetterRekapDao.getReportOutgoingLetterRekapByTujuanSuratData(searchCriteria);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Object[]> getReportOutgoingLetterRekapByTujuanSuratObj(List<? extends SearchObject> searchCriteria) {
		return reportOutgoingLetterRekapDao.getReportOutgoingLetterRekapByTujuanSuratObj(searchCriteria);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<ReportOutgoingLetterRekap> getReportOutgoingLetterRekapByTanggalSuratData(
			List<? extends SearchObject> searchCriteria) {
		return reportOutgoingLetterRekapDao.getReportOutgoingLetterRekapByTanggalSuratData(searchCriteria);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Object[]> getReportOutgoingLetterRekapByTanggalSuratObj(List<? extends SearchObject> searchCriteria) {
		return reportOutgoingLetterRekapDao.getReportOutgoingLetterRekapByTanggalSuratObj(searchCriteria);
	}
	
	
}
