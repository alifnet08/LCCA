package com.wo.module.report.reportFine.service;

import java.util.List;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportFine.vo.ReportFineDetailVo;
import com.wo.module.report.reportFine.vo.ReportFineRekapVo;

public interface ReportFineService {

	@SuppressWarnings("rawtypes")
	public List<ReportFineDetailVo> getReportFinaDetailAsVo(List<? extends SearchObject> searchCriteria);
	
	@SuppressWarnings("rawtypes")
	public List<ReportFineRekapVo> getReportFinaRekapAsVo(List<? extends SearchObject> searchCriteria);
}
