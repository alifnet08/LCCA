package com.wo.module.report.reportRegulationInternalHistory.service;

import java.util.List;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportRegulationInternalHistory.vo.ReportRegulationInternalHistoryVo;

public interface ReportRegulationInternalHistoryService {

	@SuppressWarnings("rawtypes")
	public List<ReportRegulationInternalHistoryVo> getReportRegulationIntHistoryDetailAsVo(List<? extends SearchObject> searchCriteria);
		
}
