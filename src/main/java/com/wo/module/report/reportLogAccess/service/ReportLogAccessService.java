package com.wo.module.report.reportLogAccess.service;

import java.util.List;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportLogAccess.vo.ReportLogAccessVo;

public interface ReportLogAccessService {

	@SuppressWarnings("rawtypes")
	public List<ReportLogAccessVo> getDataReport(List<? extends SearchObject> searchCriteria);
	
}
