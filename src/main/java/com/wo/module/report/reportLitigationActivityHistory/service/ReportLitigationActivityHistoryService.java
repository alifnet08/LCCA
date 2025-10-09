package com.wo.module.report.reportLitigationActivityHistory.service;

import java.util.List;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportLitigationActivityHistory.vo.ReportLitigationActivityHistoryVo;

public interface ReportLitigationActivityHistoryService {

	@SuppressWarnings("rawtypes")
	public List<ReportLitigationActivityHistoryVo> getDataReport(List<? extends SearchObject> searchCriteria);
	
}
