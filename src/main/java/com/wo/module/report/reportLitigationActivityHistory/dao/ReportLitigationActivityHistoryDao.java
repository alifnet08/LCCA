package com.wo.module.report.reportLitigationActivityHistory.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportGen.model.ReportGen;
import com.wo.module.report.reportLitigationActivityHistory.vo.ReportLitigationActivityHistoryVo;

public interface ReportLitigationActivityHistoryDao extends GenericDAO<ReportGen, Long> {

	@SuppressWarnings("rawtypes")
	public List<ReportLitigationActivityHistoryVo> getDataReport(List<? extends SearchObject> searchCriteria);
	
}
