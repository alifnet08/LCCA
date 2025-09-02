package com.wo.module.report.reportRmdRekap.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportRmdRekap.dao.ReportRmdRekapDao;
import com.wo.module.report.reportRmdRekap.model.ReportRmdRekap;

@Transactional
@Repository("reportRmdRekapService")
public class ReportRmdRekapServiceImpl implements ReportRmdRekapService{

	@Autowired
	@Qualifier("reportRmdRekapDao")
	private ReportRmdRekapDao reportRmdRekapDao;
	
	public ReportRmdRekapDao getReportRmdRekapDao() {
		return reportRmdRekapDao;
	}

	public void setReportRmdRekapDao(ReportRmdRekapDao reportRmdRekapDao) {
		this.reportRmdRekapDao = reportRmdRekapDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<ReportRmdRekap> getReportRmdRekapByData(List<? extends SearchObject> searchCriteria) {
		return reportRmdRekapDao.getReportRmdRekapAsData(searchCriteria);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Object[]> getReportRmdRekapByObj(List<? extends SearchObject> searchCriteria) {
		return reportRmdRekapDao.getReportRmdRekapAsObj(searchCriteria);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<ReportRmdRekap> getReportRmdRekapByJenisOrPeraturanSame(List<? extends SearchObject> searchCriteria, String docNameEn, String docNameIn, String senderNameEn, String senderNameIn) {
		return reportRmdRekapDao.getReportRmdRekapAsPeraturanOrJenisSameData(searchCriteria, docNameEn,docNameIn,senderNameEn,senderNameIn);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Object[]> getReportRmdRekapByJenisOrPeraturanObj(List<? extends SearchObject> searchCriteria, String docNameEn, String docNameIn, String senderNameEn, String senderNameIn) {
		return reportRmdRekapDao.getReportRmdRekapAsPeraturanOrJenisSameObj(searchCriteria, docNameEn,docNameIn,senderNameEn,senderNameIn);
	}

}
