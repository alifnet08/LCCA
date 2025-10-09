package com.wo.module.report.reportAuditDetail.service;

import java.util.List;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportAuditDetail.model.ReportAuditDetail;

public interface ReportAuditDetailService {

	@SuppressWarnings("rawtypes")
	public List<ReportAuditDetail> getReportAuditDetailByData(List<? extends SearchObject> searchCriteria, String findingNameEn, String findingNameIn);
	
	@SuppressWarnings("rawtypes")
	public List<Object[]> getReportAuditDetailByObj(List<? extends SearchObject> searchCriteria);
	
}
