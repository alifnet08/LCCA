package com.wo.module.report.reportCpsa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportCpsa.dao.ReportCpsaDao;
import com.wo.module.report.reportCpsa.model.ReportCpsa;

@Transactional
@Repository("reportCpsaService")
public class ReportCpsaServiceImpl implements ReportCpsaService{

	@Autowired
	@Qualifier("reportCpsaDao")
	private ReportCpsaDao reportCpsaDao;	

	public ReportCpsaDao getReportCpsaDao() {
		return reportCpsaDao;
	}

	public void setReportCpsaDao(ReportCpsaDao reportCpsaDao) {
		this.reportCpsaDao = reportCpsaDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<ReportCpsa> getReportCpsaByData(
			List<? extends SearchObject> searchCriteria) {
		return reportCpsaDao.getReportCpsaByData(searchCriteria);
	}	

}
