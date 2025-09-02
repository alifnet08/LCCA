package com.wo.module.report.reportRmdDetail.service;

import java.util.List;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportRmdDetail.vo.ReportRmdDetailVo;

public interface ReportRmdDetailService {

	@SuppressWarnings("rawtypes")
	List<Object[]> getReportRmdDetailAsObj(List<? extends SearchObject> searchCriteria);

	@SuppressWarnings("rawtypes")
	List<ReportRmdDetailVo> getReportRmdDetailAsVo(List<? extends SearchObject> searchCriteria);
}
