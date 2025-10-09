package com.wo.module.report.reportOutgoingLetterDetail.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportOutgoingLetterDetail.dao.ReportOutgoingLetterDetailDao;
import com.wo.module.report.reportOutgoingLetterDetail.model.ReportOutgoingLetterDetail;

@Transactional
@Repository("reportOutgoingLetterDetailService")
public class ReportOutgoingLetterDetailServiceImpl implements ReportOutgoingLetterDetailService{

	@Autowired
	@Qualifier("reportOutgoingLetterDetailDao")
	private ReportOutgoingLetterDetailDao reportOutgoingLetterDetailDao;
	
	public ReportOutgoingLetterDetailDao getReportOutgoingLetterDetailDao() {
		return reportOutgoingLetterDetailDao;
	}

	public void setReportOutgoingLetterDetailDao(ReportOutgoingLetterDetailDao reportOutgoingLetterDetailDao) {
		this.reportOutgoingLetterDetailDao = reportOutgoingLetterDetailDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<ReportOutgoingLetterDetail> getReportOutgoingLetterDetailByAllData(
			List<? extends SearchObject> searchCriteria) {
		return reportOutgoingLetterDetailDao.getReportOutgoingLetterDetailByAllData(searchCriteria);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Object[]> getReportOutgoingLetterDetailByAllObj(List<? extends SearchObject> searchCriteria) {
		return reportOutgoingLetterDetailDao.getReportOutgoingLetterDetailByAllObj(searchCriteria);
	}

}
