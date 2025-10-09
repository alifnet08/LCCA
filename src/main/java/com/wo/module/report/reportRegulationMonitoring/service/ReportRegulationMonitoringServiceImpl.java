package com.wo.module.report.reportRegulationMonitoring.service;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportRegulationMonitoring.dao.ReportRegulationMonitoringDao;
import com.wo.module.report.reportRegulationMonitoring.vo.ReportRegulationMonitoringDetailVo;
import com.wo.module.report.reportRegulationMonitoring.vo.ReportRegulationMonitoringRekapVo;

@Transactional
@Service("reportRegulationMonitoringService")
public class ReportRegulationMonitoringServiceImpl implements ReportRegulationMonitoringService, Serializable{

	private static final long serialVersionUID = -7779554030688610180L;

	@Autowired
	@Qualifier("reportRegulationMonitoringDao")
	private ReportRegulationMonitoringDao reportRegulationMonitoringDao;
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<ReportRegulationMonitoringDetailVo> getReportRegulationMonitoringDetailAsVo(
			List<? extends SearchObject> searchCriteria) {
		return reportRegulationMonitoringDao.getReportRegulationMonitoringDetailAsVo(searchCriteria);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Object[]> getReportRegulationMonitoringDetailAsObj(List<? extends SearchObject> searchCriteria) {
		return reportRegulationMonitoringDao.getReportRegulationMonitoringDetailAsObj(searchCriteria);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<ReportRegulationMonitoringRekapVo> getReportRegulationMonitoringRekapByCatRegAsVo(
			List<? extends SearchObject> searchCriteria) {
		return reportRegulationMonitoringDao.getReportRegulationMonitoringRekapByCatRegAsVo(searchCriteria);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Object[]> getReportRegulationMonitoringRekapByCatRegAsObj(List<? extends SearchObject> searchCriteria) {
		return reportRegulationMonitoringDao.getReportRegulationMonitoringRekapByCatRegAsObj(searchCriteria);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<ReportRegulationMonitoringRekapVo> getReportRegulationMonitoringRekapByTotRegMonAsVo(
			List<? extends SearchObject> searchCriteria) {
		return reportRegulationMonitoringDao.getReportRegulationMonitoringRekapByTotRegMonAsVo(searchCriteria);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Object[]> getReportRegulationMonitoringRekapTotRegMonAsObj(
			List<? extends SearchObject> searchCriteria) {
		return reportRegulationMonitoringDao.getReportRegulationMonitoringRekapTotRegMonAsObj(searchCriteria);
	}
	
	public ReportRegulationMonitoringDao getReportRegulationMonitoringDao() {
		return reportRegulationMonitoringDao;
	}

	public void setReportRegulationMonitoringDao(ReportRegulationMonitoringDao reportRegulationMonitoringDao) {
		this.reportRegulationMonitoringDao = reportRegulationMonitoringDao;
	}

}
