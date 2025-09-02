package com.wo.module.report.reportRegulation.service;

import java.io.Serializable;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportRegulation.dao.ReportRegulationDao;
import com.wo.module.report.reportRegulation.vo.ReportRegulationDetailVo;
import com.wo.module.report.reportRegulation.vo.ReportRegulationRekapVo;
import com.wo.module.report.reportRegulation.vo.ReportRegulationTrackRecordVo;

@Transactional
@Service("reportRegulationService")
public class ReportRegulationServiceImpl implements ReportRegulationService, Serializable{

	private static final long serialVersionUID = -2899698684601877753L;

	@Autowired
	@Qualifier("reportRegulationDao")
	private ReportRegulationDao reportRegulationDao;

	@SuppressWarnings("rawtypes")
	@Override
	public List<ReportRegulationDetailVo> getReportRegulationDetailByAllDataAsVo(
			List<? extends SearchObject> searchCriteria) {
		return reportRegulationDao.getReportRegulationDetailByAllDataAsVo(searchCriteria);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<ReportRegulationRekapVo> getReportRegulationRekapByProvisionTypeAsVo(
			List<? extends SearchObject> searchCriteria) {
		return reportRegulationDao.getReportRegulationRekapByProvisionTypeAsVo(searchCriteria);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<ReportRegulationRekapVo> getReportRegulationRekapByHitsAsVo(
			List<? extends SearchObject> searchCriteria) {
		return reportRegulationDao.getReportRegulationRekapByHitsAsVo(searchCriteria);
	}
	
	public ReportRegulationDao getReportRegulationDao() {
		return reportRegulationDao;
	}

	public void setReportRegulationDao(ReportRegulationDao reportRegulationDao) {
		this.reportRegulationDao = reportRegulationDao;
	}

}
