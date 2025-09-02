package com.wo.module.report.reportComplianceDocumentDetail.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportComplianceDocumentDetail.model.ReportComplianceDocumentDetail;
import com.wo.module.report.reportGen.model.ReportGen;

public interface ReportComplianceDocumentDetailDao extends GenericDAO<ReportGen, Long>{
	
	@SuppressWarnings("rawtypes")
	public List<ReportComplianceDocumentDetail>	getReportComplianceDocumentDetailByData(List<? extends SearchObject> searchCriteria);
	
	@SuppressWarnings("rawtypes")
	public List<Object[]>	getReportComplianceDocumentDetailByObj(List<? extends SearchObject> searchCriteria);
	
}
