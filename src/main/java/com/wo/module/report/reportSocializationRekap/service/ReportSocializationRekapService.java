package com.wo.module.report.reportSocializationRekap.service;

import java.util.List;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportSocializationRekap.model.ReportSocializationRekap;

public interface ReportSocializationRekapService {
	
	@SuppressWarnings("rawtypes")
	public List<Object[]> getReportSocializationRekapByKategoriDokumen(List<? extends SearchObject> searchCriteria);
	
	@SuppressWarnings("rawtypes")
	public List<Object[]> getReportSocializationRekapByTotalSosialisasi(List<? extends SearchObject> searchCriteria);
	
	@SuppressWarnings("rawtypes")
	public List<ReportSocializationRekap> getReportSocializationRekapByKategoriDokumenData(List<? extends SearchObject> searchCriteria);
	
	@SuppressWarnings("rawtypes")
	public List<ReportSocializationRekap> getReportSocializationRekapByTotalSosialisasiData(List<? extends SearchObject> searchCriteria);
}