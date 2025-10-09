package com.wo.module.report.reportRegulationInternalHistory.service;

import java.io.Serializable;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportRegulationInternalHistory.dao.ReportRegulationInternalHistoryDao;
import com.wo.module.report.reportRegulationInternalHistory.vo.ReportRegulationInternalHistoryVo;


@Transactional
@Service("reportRegulationInternalHistoryService")
public class ReportRegulationInternalHistoryServiceImpl implements ReportRegulationInternalHistoryService, Serializable{

	private static final long serialVersionUID = -2899698684601877753L;

	@Autowired
	@Qualifier("reportRegulationInternalHistoryDao")
	private ReportRegulationInternalHistoryDao reportRegulationInternalHistoryDao;

	@SuppressWarnings("rawtypes")
	@Override
	public List<ReportRegulationInternalHistoryVo> getReportRegulationIntHistoryDetailAsVo(
			List<? extends SearchObject> searchCriteria) {
		return reportRegulationInternalHistoryDao.getReportRegulationIntHistoryDetailAsVo(searchCriteria);
	}
	

	public ReportRegulationInternalHistoryDao getReportRegulationInternalHistoryDao() {
		return reportRegulationInternalHistoryDao;
	}

	public void setReportRegulationInternalHistoryDao(ReportRegulationInternalHistoryDao reportRegulationInternalHistoryDao) {
		this.reportRegulationInternalHistoryDao = reportRegulationInternalHistoryDao;
	}
	

	
}
