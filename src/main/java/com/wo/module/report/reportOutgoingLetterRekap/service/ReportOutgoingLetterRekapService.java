package com.wo.module.report.reportOutgoingLetterRekap.service;

import java.util.List;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportOutgoingLetterRekap.model.ReportOutgoingLetterRekap;

public interface ReportOutgoingLetterRekapService {

	@SuppressWarnings("rawtypes")
	public List<ReportOutgoingLetterRekap> getReportOutgoingLetterRekapByTujuanSuratData(List<? extends SearchObject> searchCriteria);
	
	@SuppressWarnings("rawtypes")
	public List<Object[]> getReportOutgoingLetterRekapByTujuanSuratObj(List<? extends SearchObject> searchCriteria);
	
	@SuppressWarnings("rawtypes")
	public List<ReportOutgoingLetterRekap> getReportOutgoingLetterRekapByTanggalSuratData(List<? extends SearchObject> searchCriteria);
	
	@SuppressWarnings("rawtypes")
	public List<Object[]> getReportOutgoingLetterRekapByTanggalSuratObj(List<? extends SearchObject> searchCriteria);
	
}
