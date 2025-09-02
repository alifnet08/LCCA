package com.wo.module.report.reportRmdRekap.service;

import java.util.List;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportRmdRekap.model.ReportRmdRekap;

public interface ReportRmdRekapService {
	
	@SuppressWarnings("rawtypes")
	public List<ReportRmdRekap> getReportRmdRekapByData(List<? extends SearchObject> searchCriteria);
	
	@SuppressWarnings("rawtypes")
	public List<Object[]> getReportRmdRekapByObj(List<? extends SearchObject> searchCriteria);
	
	@SuppressWarnings("rawtypes")
	public List<ReportRmdRekap> getReportRmdRekapByJenisOrPeraturanSame(List<? extends SearchObject> searchCriteria, String docNameEn, String docNameIn, String senderNameEn, String senderNameIn);
	
	@SuppressWarnings("rawtypes")
	public List<Object[]> getReportRmdRekapByJenisOrPeraturanObj(List<? extends SearchObject> searchCriteria, String docNameEn, String docNameIn, String senderNameEn, String senderNameIn);
}