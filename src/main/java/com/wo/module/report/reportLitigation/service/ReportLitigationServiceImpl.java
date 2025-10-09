package com.wo.module.report.reportLitigation.service;

import com.wo.module.report.reportLitigation.vo.ReportLitiPerdataTergugatVo;
import com.wo.module.report.reportLitigation.vo.ReportLitiPerdataPenggugatVo;
import com.wo.module.report.reportLitigation.vo.ReportLitiPailitPKPUVo;
import com.wo.module.report.reportLitigation.vo.ReportLitiPidanaPelaporVo;
import com.wo.module.report.reportLitigation.vo.ReportLitiPidanaTerlaporVo;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportLitigation.dao.ReportLitigationDao;

@Transactional
@Service("reportLitigationService")
public class ReportLitigationServiceImpl implements ReportLitigationService, Serializable{
	
	private static final long serialVersionUID = -7779554030688610180L;
	
	@Autowired
	@Qualifier("reportLitigationDao")
	private ReportLitigationDao reportLitigationDao;

	@SuppressWarnings("rawtypes")
	@Override
	public List<ReportLitiPerdataTergugatVo> getReportLitigationPerDataTergugatAsVo(List<? extends SearchObject> searchCriteria) {
		return reportLitigationDao.getReportLitigationPerDataTergugatAsVo(searchCriteria);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<ReportLitiPerdataPenggugatVo> getReportLitigationPerDataPenggugatAsVo(List<? extends SearchObject> searchCriteria) {
		return reportLitigationDao.getReportLitigationPerDataPenggugatAsVo(searchCriteria);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<ReportLitiPailitPKPUVo> getReportLitigationKepailitanDanPKPUAsVo(List<? extends SearchObject> searchCriteria) {
		return reportLitigationDao.getReportLitigationKepailitanDanPKPUAsVo(searchCriteria);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<ReportLitiPidanaPelaporVo> getReportLitigationPidanaPelaporAsVo(List<? extends SearchObject> searchCriteria) {
		return reportLitigationDao.getReportLitigationPidanaPelaporAsVo(searchCriteria);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<ReportLitiPidanaTerlaporVo> getReportLitigationPidanaTerlaporAsVo(List<? extends SearchObject> searchCriteria) {
		return reportLitigationDao.getReportLitigationPidanaTerlaporAsVo(searchCriteria);
	}
	
	
}