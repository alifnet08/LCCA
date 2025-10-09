package com.wo.module.report.reportSuratMasukAmlRekap.service;

import java.util.List;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportSuratMasukAmlRekap.model.ReportSuratMasukAmlRekap;

public interface ReportSuratMasukAmlRekapService {

	@SuppressWarnings("rawtypes")
	public List<ReportSuratMasukAmlRekap> getReportSuratMasukAmlRekapByData(List<? extends SearchObject> searchCriteria);
	
	@SuppressWarnings("rawtypes")
	public List<Object[]> getReportSuratMasukAmlRekapByObj(List<? extends SearchObject> searchCriteria);
	
	@SuppressWarnings("rawtypes")
	public List<ReportSuratMasukAmlRekap> getReportSuratMasukAmlRekapByTipeSurat(List<? extends SearchObject> searchCriteria);
	
	@SuppressWarnings("rawtypes")
	public List<Object[]> getReportSuratMasukAmlRekapByTipeSuratObj(List<? extends SearchObject> searchCriteria);
	
}
