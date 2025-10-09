package com.wo.module.report.reportSocializationRekap.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportSocializationRekap.model.ReportSocializationRekap;
import com.wo.module.report.reportGen.model.ReportGen;

public interface ReportSocializationRekapDao extends GenericDAO<ReportGen, Long>{

	@SuppressWarnings("rawtypes")
	public List<ReportSocializationRekap> getReportSocializationRekapDataByKategoriDokumen(List<? extends SearchObject> searchCriteria);
	
	@SuppressWarnings("rawtypes")
	public List<Object[]> getReportSocializationRekapDataObjByKategoriDokumen(List<? extends SearchObject> searchCriteria);
	
	@SuppressWarnings("rawtypes")
	public List<ReportSocializationRekap> getReportSocializationRekapDataByTotalSosialisasi(List<? extends SearchObject> searchCriteria);
	
	@SuppressWarnings("rawtypes")
	public List<Object[]> getReportSocializationRekapDataObjByTotalSosialisasi(List<? extends SearchObject> searchCriteria);
	
}
