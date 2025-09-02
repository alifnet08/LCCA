package com.wo.module.report.reportComplianceDocumentRekap.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportComplianceDocumentRekap.model.ReportComplianceDocumentRekap;
import com.wo.module.report.reportGen.model.ReportGen;

public interface ReportComplianceDocumentRekapDao extends GenericDAO<ReportGen, Long>{

	@SuppressWarnings("rawtypes")
	public List<ReportComplianceDocumentRekap> getReportComplianceDocumentRekapByTipeDocumentData(List<? extends SearchObject> searchCriteria);
	
	@SuppressWarnings("rawtypes")
	public List<Object[]> getReportComplianceDocumentRekapByTipeDocumentObj(List<? extends SearchObject> searchCriteria);
	
	@SuppressWarnings("rawtypes")
	public List<ReportComplianceDocumentRekap> getReportComplianceDocumentRekapByKelompokHukData(List<? extends SearchObject> searchCriteria);
	
	@SuppressWarnings("rawtypes")
	public List<Object[]> getReportComplianceDocumentRekapByKelompokHukObj(List<? extends SearchObject> searchCriteria);
}
