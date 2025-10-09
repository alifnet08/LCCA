package com.wo.module.report.reportSuratMasukRekap.service;

import java.util.List;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportSuratMasukRekap.model.ReportSuratMasukRekap;

public interface ReportSuratMasukRekapService {
	
	@SuppressWarnings("rawtypes")
	public List<ReportSuratMasukRekap> getReportSuratMasukRekapByData(List<? extends SearchObject> searchCriteria);
	
	@SuppressWarnings("rawtypes")
	public List<Object[]> getReportSuratMasukRekapByObj(List<? extends SearchObject> searchCriteria);
	
	@SuppressWarnings("rawtypes")
	public List<ReportSuratMasukRekap> getReportSuratMasukRekapByTipeSurat(List<? extends SearchObject> searchCriteria);
	
	@SuppressWarnings("rawtypes")
	public List<Object[]> getReportSuratMasukRekapByTipeSuratObj(List<? extends SearchObject> searchCriteria);
}
