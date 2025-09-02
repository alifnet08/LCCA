package com.wo.module.report.reportSuratMasukDetail.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportGen.model.ReportGen;
import com.wo.module.report.reportSuratMasukDetail.model.ReportSuratMasukDetail;

public interface ReportSuratMasukDetailDao extends GenericDAO<ReportGen, Long>{

	@SuppressWarnings("rawtypes")
	List<ReportSuratMasukDetail> getReportSuratMasukDetailByData(List<? extends SearchObject> searchCriteria);
	
	@SuppressWarnings("rawtypes")
	List<Object[]> getReportSuratMasukDetailByObj(List<? extends SearchObject> searchCriteria);
}
