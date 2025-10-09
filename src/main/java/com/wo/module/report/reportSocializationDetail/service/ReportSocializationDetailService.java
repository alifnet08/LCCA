package com.wo.module.report.reportSocializationDetail.service;

import java.util.List;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportSocializationDetail.vo.ReportSocializationDetailVo;

public interface ReportSocializationDetailService {

	@SuppressWarnings("rawtypes")
	List<Object[]> getReportSocializationDetailAsObj(List<? extends SearchObject> searchCriteria);

	@SuppressWarnings("rawtypes")
	List<ReportSocializationDetailVo> getReportSocializationDetailAsVo(List<? extends SearchObject> searchCriteria);
}
