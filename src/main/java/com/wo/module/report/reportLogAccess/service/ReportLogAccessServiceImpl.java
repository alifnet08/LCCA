package com.wo.module.report.reportLogAccess.service;

import java.io.Serializable;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportLogAccess.dao.ReportLogAccessDao;
import com.wo.module.report.reportLogAccess.vo.ReportLogAccessVo;

@Transactional
@Service("reportLogAccessService")
public class ReportLogAccessServiceImpl implements ReportLogAccessService, Serializable {

	private static final long serialVersionUID = -7570280136117774803L;
	
	@Autowired
	@Qualifier("reportLogAccessDao")
	private ReportLogAccessDao reportLogAccessDao;
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<ReportLogAccessVo> getDataReport(List<? extends SearchObject> searchCriteria) {
		return reportLogAccessDao.getDataReport(searchCriteria);
	}

	
	

}