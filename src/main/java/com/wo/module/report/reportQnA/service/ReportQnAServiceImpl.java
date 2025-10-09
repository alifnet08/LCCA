package com.wo.module.report.reportQnA.service;

import java.io.Serializable;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportQnA.dao.ReportQnADao;
import com.wo.module.report.reportQnA.vo.ReportQnADetail;
import com.wo.module.report.reportQnA.vo.ReportQnARekap;

@Transactional
@Service("reportQnAService")
public class ReportQnAServiceImpl implements Serializable, ReportQnAService{

	private static final long serialVersionUID = 410582409757873964L;

	@Autowired
	@Qualifier("reportQnADao")
	private ReportQnADao reportQnADao;
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<ReportQnADetail> getReportQnADetailAsVo(List<? extends SearchObject> searchCriteria) {
		return reportQnADao.getReportQnADetailAsVo(searchCriteria);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<ReportQnARekap> getReportQnARekapAsVo(List<? extends SearchObject> searchCriteria) {
		return reportQnADao.getReportQnARekapAsVo(searchCriteria);
	}

	public ReportQnADao getReportQnADao() {
		return reportQnADao;
	}

	public void setReportQnADao(ReportQnADao reportQnADao) {
		this.reportQnADao = reportQnADao;
	}

}
