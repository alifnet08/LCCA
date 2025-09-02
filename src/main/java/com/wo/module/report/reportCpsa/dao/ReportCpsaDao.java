package com.wo.module.report.reportCpsa.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportCpsa.model.ReportCpsa;
import com.wo.module.report.reportGen.model.ReportGen;

public interface ReportCpsaDao extends GenericDAO<ReportGen, Long>{

	@SuppressWarnings("rawtypes")
	public List<ReportCpsa> getReportCpsaByData(List<? extends SearchObject> searchCriteria);
	
}
