package com.wo.module.report.reportRegulation.service;

import java.util.List;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportRegulation.vo.ReportRegulationDetailVo;
import com.wo.module.report.reportRegulation.vo.ReportRegulationRekapVo;
import com.wo.module.report.reportRegulation.vo.ReportRegulationTrackRecordVo;

public interface ReportRegulationService {

	@SuppressWarnings("rawtypes")
	public List<ReportRegulationDetailVo> getReportRegulationDetailByAllDataAsVo(List<? extends SearchObject> searchCriteria);
	
	@SuppressWarnings("rawtypes")
	public List<ReportRegulationRekapVo> getReportRegulationRekapByProvisionTypeAsVo(List<? extends SearchObject> searchCriteria);
	
	@SuppressWarnings("rawtypes")
	public List<ReportRegulationRekapVo> getReportRegulationRekapByHitsAsVo(List<? extends SearchObject> searchCriteria);
	
}
