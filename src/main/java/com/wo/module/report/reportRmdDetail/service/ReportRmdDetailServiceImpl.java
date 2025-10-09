package com.wo.module.report.reportRmdDetail.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportRmdDetail.dao.ReportRmdDetailDao;
import com.wo.module.report.reportRmdDetail.vo.ReportRmdDetailVo;

@Transactional
@Service("reportRmdDetailService")
public class ReportRmdDetailServiceImpl implements ReportRmdDetailService {
	@Autowired
	@Qualifier("reportRmdDetailDao")
	private ReportRmdDetailDao reportRmdDetailDao;

	@SuppressWarnings("rawtypes")
	@Override
	public List<Object[]> getReportRmdDetailAsObj(List<? extends SearchObject> searchCriteria) {
		return reportRmdDetailDao.getReportRmdDetailAsObj(searchCriteria);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<ReportRmdDetailVo> getReportRmdDetailAsVo(List<? extends SearchObject> searchCriteria) {
		return reportRmdDetailDao.getReportRmdDetailAsVo(searchCriteria);
	}

	public ReportRmdDetailDao getReportRmdDetailDao() {
		return reportRmdDetailDao;
	}

	public void setReportRmdDetailDao(ReportRmdDetailDao reportRmdDetailDao) {
		this.reportRmdDetailDao = reportRmdDetailDao;
	}

}
