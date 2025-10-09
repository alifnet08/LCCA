package com.wo.module.report.reportFine.service;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportFine.dao.ReportFineDao;
import com.wo.module.report.reportFine.vo.ReportFineDetailVo;
import com.wo.module.report.reportFine.vo.ReportFineRekapVo;

@Transactional
@Service("reportFineService")
public class ReportFineServiceImpl implements ReportFineService, Serializable{

	private static final long serialVersionUID = 2033558320010873876L;

	@Autowired
	@Qualifier("reportFineDao")
	private ReportFineDao reportFineDao;
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<ReportFineDetailVo> getReportFinaDetailAsVo(List<? extends SearchObject> searchCriteria) {
		return reportFineDao.getReportFinaDetailAsVo(searchCriteria);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<ReportFineRekapVo> getReportFinaRekapAsVo(List<? extends SearchObject> searchCriteria) {
		return reportFineDao.getReportFinaRekapAsVo(searchCriteria);
	}

	public ReportFineDao getReportFineTaskDao() {
		return reportFineDao;
	}

	public void setReportFineTaskDao(ReportFineDao reportFineDao) {
		this.reportFineDao = reportFineDao;
	}

}
