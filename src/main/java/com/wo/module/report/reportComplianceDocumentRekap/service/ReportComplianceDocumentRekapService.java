package com.wo.module.report.reportComplianceDocumentRekap.service;

import java.util.List;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportComplianceDocumentRekap.model.ReportComplianceDocumentRekap;

public interface ReportComplianceDocumentRekapService {

	@SuppressWarnings("rawtypes")
	public List<ReportComplianceDocumentRekap> getReportComplianceDocumentRekapByTipeDocumentData(List<? extends SearchObject> searchCriteria);
	
	@SuppressWarnings("rawtypes")
	public List<Object[]> getReportComplianceDocumentRekapByTipeDocumentObj(List<? extends SearchObject> searchCriteria);
	
	@SuppressWarnings("rawtypes")
	public List<ReportComplianceDocumentRekap> getReportComplianceDocumentRekapByKelompokHukData(List<? extends SearchObject> searchCriteria);
	
	@SuppressWarnings("rawtypes")
	public List<Object[]> getReportComplianceDocumentRekapByKelompokHukObj(List<? extends SearchObject> searchCriteria);
	
}
