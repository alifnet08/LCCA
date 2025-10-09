package com.wo.module.report.reportSuratMasukAmlDetail.service;

import java.util.List;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportSuratMasukAmlDetail.model.ReportSuratMasukAmlDetail;

public interface ReportSuratMasukAmlDetailService {

	@SuppressWarnings("rawtypes")
	List<ReportSuratMasukAmlDetail> getReportSuratMasukAmlDetailByData(List<? extends SearchObject> searchCriteria);
	
	@SuppressWarnings("rawtypes")
	List<Object[]> getReportSuratMasukAmlDetailByObj(List<? extends SearchObject> searchCriteria);
}
