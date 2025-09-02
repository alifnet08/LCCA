package com.wo.module.report.reportOutgoingLetterDetail.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportGen.model.ReportGen;
import com.wo.module.report.reportOutgoingLetterDetail.model.ReportOutgoingLetterDetail;

public interface ReportOutgoingLetterDetailDao extends GenericDAO<ReportGen, Long>{
	
	@SuppressWarnings("rawtypes")
	public  List<ReportOutgoingLetterDetail> getReportOutgoingLetterDetailByAllData(List<? extends SearchObject> searchCriteria);
	
	@SuppressWarnings("rawtypes")
	public  List<Object[]> getReportOutgoingLetterDetailByAllObj(List<? extends SearchObject> searchCriteria);
}
