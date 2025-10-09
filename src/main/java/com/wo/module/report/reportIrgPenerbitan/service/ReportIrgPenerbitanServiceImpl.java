package com.wo.module.report.reportIrgPenerbitan.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportIrgPenerbitan.dao.ReportIrgPenerbitanDao;
import com.wo.module.report.reportIrgPenerbitan.model.ReportIrgPenerbitan;

@Transactional
@Repository("reportIrgPenerbitanService")
public class ReportIrgPenerbitanServiceImpl implements ReportIrgPenerbitanService {

	@Autowired
	@Qualifier("reportIrgPenerbitanDao")
	private ReportIrgPenerbitanDao reportIrgPenerbitanDao;

	@SuppressWarnings("rawtypes")
	@Override
	public List<ReportIrgPenerbitan> getAllIrgDataHeader(List<? extends SearchObject> searchCriteria) {
		return reportIrgPenerbitanDao.getAllIrgDataHeader(searchCriteria);
	}	
	
	
}