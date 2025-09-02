package com.wo.module.report.reportLitigationActivityHistory.service;

import java.io.Serializable;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportLitigationActivityHistory.dao.ReportLitigationActivityHistoryDao;
import com.wo.module.report.reportLitigationActivityHistory.vo.ReportLitigationActivityHistoryVo;

@Transactional
@Service("reportLitigationActivityHistoryService")
public class ReportLitigationActivityHistoryServiceImpl implements ReportLitigationActivityHistoryService, Serializable {

	private static final long serialVersionUID = 2814174638711826923L;

	@Autowired
	@Qualifier("reportLitigationActivityHistoryDao")
	private ReportLitigationActivityHistoryDao reportLitigationActivityHistoryDao;
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<ReportLitigationActivityHistoryVo> getDataReport(List<? extends SearchObject> searchCriteria) {
		return reportLitigationActivityHistoryDao.getDataReport(searchCriteria);
	}

}
