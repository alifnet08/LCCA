package com.wo.module.report.reportAuditRekap.service;

import java.util.List;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportAuditRekap.model.ReportAuditRekap;

public interface ReportAuditRekapService {
	
	@SuppressWarnings("rawtypes")
	public List<ReportAuditRekap> getReportAuditRekapByData(List<? extends SearchObject> searchCriteria, String findingNameEn, String findingNameIn);
	
	@SuppressWarnings("rawtypes")
	public List<Object[]> getReportAuditRekapByObj(List<? extends SearchObject> searchCriteria);
	
	@SuppressWarnings("rawtypes")
	public List<ReportAuditRekap> getReportAuditRekapByTemplateName(List<? extends SearchObject> searchCriteria, String findingNameEn, String findingNameIn);
}
