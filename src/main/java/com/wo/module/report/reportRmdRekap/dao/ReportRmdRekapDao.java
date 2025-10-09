package com.wo.module.report.reportRmdRekap.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportGen.model.ReportGen;
import com.wo.module.report.reportRmdRekap.model.ReportRmdRekap;

public interface ReportRmdRekapDao extends GenericDAO<ReportGen, Long>{

	@SuppressWarnings("rawtypes")
	public List<ReportRmdRekap> getReportRmdRekapAsData(List<? extends SearchObject> searchCriteria);
	
	@SuppressWarnings("rawtypes")
	public List<Object[]> getReportRmdRekapAsObj(List<? extends SearchObject> searchCriteria);
	
	@SuppressWarnings("rawtypes")
	public List<ReportRmdRekap> getReportRmdRekapAsPeraturanOrJenisSameData(List<? extends SearchObject> searchCriteria, String docNameEn, String docNameIn, String senderNameEn, String senderNameIn);
	
	@SuppressWarnings("rawtypes")
	public List<Object[]> getReportRmdRekapAsPeraturanOrJenisSameObj(List<? extends SearchObject> searchCriteria, String docNameEn, String docNameIn, String senderNameEn, String senderNameIn);
}
