package com.wo.module.report.reportAuditDetail.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportAuditDetail.model.ReportAuditDetail;
import com.wo.module.report.reportGen.model.ReportGen;

public interface ReportAuditDetailDao extends GenericDAO<ReportGen, Long>{

	@SuppressWarnings("rawtypes")
	public List<ReportAuditDetail> getReportAuditDetailByData(List<? extends SearchObject> searchCriteria, String findingNameEn, String findingNameIn);
	
	@SuppressWarnings("rawtypes")
	public List<Object[]> getReportAuditDetailByObj(List<? extends SearchObject> searchCriteria);
	
}
