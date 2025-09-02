package com.wo.module.report.reportIrgPenerbitan.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportGen.model.ReportGen;
import com.wo.module.report.reportIrgPenerbitan.model.ReportIrgPenerbitan;

public interface ReportIrgPenerbitanDao extends GenericDAO<ReportGen, Long>{

	@SuppressWarnings("rawtypes")
	public List<ReportIrgPenerbitan> getAllIrgDataHeader(List<? extends SearchObject> searchCriteria);
	
}
