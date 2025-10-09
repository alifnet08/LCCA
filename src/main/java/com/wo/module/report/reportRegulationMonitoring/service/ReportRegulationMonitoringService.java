package com.wo.module.report.reportRegulationMonitoring.service;

import java.util.List;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportRegulationMonitoring.vo.ReportRegulationMonitoringDetailVo;
import com.wo.module.report.reportRegulationMonitoring.vo.ReportRegulationMonitoringRekapVo;

public interface ReportRegulationMonitoringService {

	@SuppressWarnings("rawtypes")
	public List<ReportRegulationMonitoringDetailVo> getReportRegulationMonitoringDetailAsVo(List<? extends SearchObject> searchCriteria);
	
	@SuppressWarnings("rawtypes")
	public List<Object[]> getReportRegulationMonitoringDetailAsObj(List<? extends SearchObject> searchCriteria);
	
	@SuppressWarnings("rawtypes")
	public List<ReportRegulationMonitoringRekapVo> getReportRegulationMonitoringRekapByCatRegAsVo(List<? extends SearchObject> searchCriteria);
	
	@SuppressWarnings("rawtypes")
	public List<Object[]> getReportRegulationMonitoringRekapByCatRegAsObj(List<? extends SearchObject> searchCriteria);
	
	@SuppressWarnings("rawtypes")
	public List<ReportRegulationMonitoringRekapVo> getReportRegulationMonitoringRekapByTotRegMonAsVo(List<? extends SearchObject> searchCriteria);
	
	@SuppressWarnings("rawtypes")
	public List<Object[]> getReportRegulationMonitoringRekapTotRegMonAsObj(List<? extends SearchObject> searchCriteria);
}
