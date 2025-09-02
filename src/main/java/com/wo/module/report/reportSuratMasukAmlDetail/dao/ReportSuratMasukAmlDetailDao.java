package com.wo.module.report.reportSuratMasukAmlDetail.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportGen.model.ReportGen;
import com.wo.module.report.reportSuratMasukAmlDetail.model.ReportSuratMasukAmlDetail;

public interface ReportSuratMasukAmlDetailDao extends GenericDAO<ReportGen, Long>{

	@SuppressWarnings("rawtypes")
	List<ReportSuratMasukAmlDetail> getReportSuratMasukAmlDetailByData(List<? extends SearchObject> searchCriteria);
	
	@SuppressWarnings("rawtypes")
	List<Object[]> getReportSuratMasukAmlDetailByObj(List<? extends SearchObject> searchCriteria);
	
}
