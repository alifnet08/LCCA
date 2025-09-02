package com.wo.module.report.reportSuratMasukDetail.service;

import java.util.List;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportSuratMasukDetail.model.ReportSuratMasukDetail;

public interface ReportSuratMasukDetailService {

	@SuppressWarnings("rawtypes")
	public List<ReportSuratMasukDetail> getReportSuratMasukDetailByData(List<? extends SearchObject> searchCriteria);
	
	@SuppressWarnings("rawtypes")
	public List<Object[]> getReportSuratMasukDetailByObj(List<? extends SearchObject> searchCriteria);
	
}
