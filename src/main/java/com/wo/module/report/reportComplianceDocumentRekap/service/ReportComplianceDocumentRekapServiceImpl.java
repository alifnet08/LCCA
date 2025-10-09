package com.wo.module.report.reportComplianceDocumentRekap.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportComplianceDocumentRekap.dao.ReportComplianceDocumentRekapDao;
import com.wo.module.report.reportComplianceDocumentRekap.model.ReportComplianceDocumentRekap;

@Transactional
@Repository("reportComplianceDocumentRekapService")
public class ReportComplianceDocumentRekapServiceImpl implements ReportComplianceDocumentRekapService{

	@Autowired
	@Qualifier("reportComplianceDocumentRekapDao")
	private ReportComplianceDocumentRekapDao reportComplianceDocumentRekapDao;
	
	public ReportComplianceDocumentRekapDao getReportComplianceDocumentRekapDao() {
		return reportComplianceDocumentRekapDao;
	}

	public void setReportComplianceDocumentRekapDao(ReportComplianceDocumentRekapDao reportComplianceDocumentRekapDao) {
		this.reportComplianceDocumentRekapDao = reportComplianceDocumentRekapDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<ReportComplianceDocumentRekap> getReportComplianceDocumentRekapByTipeDocumentData(
			List<? extends SearchObject> searchCriteria) {
		return reportComplianceDocumentRekapDao.getReportComplianceDocumentRekapByTipeDocumentData(searchCriteria);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Object[]> getReportComplianceDocumentRekapByTipeDocumentObj(
			List<? extends SearchObject> searchCriteria) {
		return reportComplianceDocumentRekapDao.getReportComplianceDocumentRekapByTipeDocumentObj(searchCriteria);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<ReportComplianceDocumentRekap> getReportComplianceDocumentRekapByKelompokHukData(
			List<? extends SearchObject> searchCriteria) {
		return reportComplianceDocumentRekapDao.getReportComplianceDocumentRekapByKelompokHukData(searchCriteria);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Object[]> getReportComplianceDocumentRekapByKelompokHukObj(
			List<? extends SearchObject> searchCriteria) {
		return reportComplianceDocumentRekapDao.getReportComplianceDocumentRekapByKelompokHukObj(searchCriteria);
	}

}
