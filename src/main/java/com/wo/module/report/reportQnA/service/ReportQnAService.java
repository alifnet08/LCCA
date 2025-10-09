package com.wo.module.report.reportQnA.service;

import java.util.List;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportQnA.vo.ReportQnADetail;
import com.wo.module.report.reportQnA.vo.ReportQnARekap;

public interface ReportQnAService {
	
	@SuppressWarnings("rawtypes")
	public List<ReportQnADetail> getReportQnADetailAsVo(List<? extends SearchObject> searchCriteria);
	
	@SuppressWarnings("rawtypes")
	public List<ReportQnARekap> getReportQnARekapAsVo(List<? extends SearchObject> searchCriteria);

}
