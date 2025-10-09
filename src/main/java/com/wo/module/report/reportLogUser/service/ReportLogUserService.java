package com.wo.module.report.reportLogUser.service;

import java.util.List;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportLogUser.vo.ReportLogUserDiagramVo;
import com.wo.module.report.reportLogUser.vo.ReportLogUserVo;

public interface ReportLogUserService {

	@SuppressWarnings("rawtypes")
	public List<ReportLogUserVo> getReportLogUserDetailAsVo(List<? extends SearchObject> searchCriteria);
	
	@SuppressWarnings("rawtypes")
	public List<ReportLogUserDiagramVo> getReportLogUserGrafikAsVo(List<? extends SearchObject> searchCriteria);
}
