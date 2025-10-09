package com.wo.module.report.reportRegulationMonitoring.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportGen.model.ReportGen;
import com.wo.module.report.reportRegulationMonitoring.vo.ReportRegulationMonitoringDetailVo;
import com.wo.module.report.reportRegulationMonitoring.vo.ReportRegulationMonitoringRekapVo;

public interface ReportRegulationMonitoringDao extends GenericDAO<ReportGen, Long>{

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
