package com.wo.module.report.reportRegulationInternalHistory.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportGen.model.ReportGen;
import com.wo.module.report.reportRegulationInternalHistory.vo.ReportRegulationInternalHistoryVo;

public interface ReportRegulationInternalHistoryDao extends GenericDAO<ReportGen, Long>{

	@SuppressWarnings("rawtypes")
	public List<ReportRegulationInternalHistoryVo> getReportRegulationIntHistoryDetailAsVo(List<? extends SearchObject> searchCriteria);
	
}
