package com.wo.module.report.reportSocializationDetail.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportSocializationDetail.dao.ReportSocializationDetailDao;
import com.wo.module.report.reportSocializationDetail.vo.ReportSocializationDetailVo;

@Transactional
@Service("reportSocializationDetailService")
public class ReportSocializationDetailServiceImpl implements ReportSocializationDetailService {
	@Autowired
	@Qualifier("reportSocializationDetailDao")
	private ReportSocializationDetailDao reportSocializationDetailDao;

	public ReportSocializationDetailDao getReportSocializationDetailDao() {
		return reportSocializationDetailDao;
	}

	public void setReportSocializationDetailDao(ReportSocializationDetailDao reportSocializationDetailDao) {
		this.reportSocializationDetailDao = reportSocializationDetailDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Object[]> getReportSocializationDetailAsObj(List<? extends SearchObject> searchCriteria) {
		return reportSocializationDetailDao.getReportSocializationDetailAsObj(searchCriteria);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<ReportSocializationDetailVo> getReportSocializationDetailAsVo(List<? extends SearchObject> searchCriteria) {
		return reportSocializationDetailDao.getReportSocializationDetailAsVo(searchCriteria);
	}

}
