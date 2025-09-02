package com.wo.module.report.reportLogUser.service;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportLogUser.dao.ReportLogUserDao;
import com.wo.module.report.reportLogUser.vo.ReportLogUserDiagramVo;
import com.wo.module.report.reportLogUser.vo.ReportLogUserVo;

@Transactional
@Service("reportLogUserService")
public class ReportLogUserServiceImpl implements ReportLogUserService, Serializable{

	private static final long serialVersionUID = 8556717954424337721L;
	private static final Logger logger = Logger.getLogger(ReportLogUserServiceImpl.class);

	@Autowired
	@Qualifier("reportLogUserDao")
	private ReportLogUserDao reportLogUserDao;
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<ReportLogUserVo> getReportLogUserDetailAsVo(List<? extends SearchObject> searchCriteria) {
		return reportLogUserDao.getReportLogUserDetailAsVo(searchCriteria);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<ReportLogUserDiagramVo> getReportLogUserGrafikAsVo(List<? extends SearchObject> searchCriteria) {
		return reportLogUserDao.getReportLogUserGrafikAsVo(searchCriteria);
	}
	
	public ReportLogUserDao getReportLogUserDao() {
		return reportLogUserDao;
	}

	public void setReportLogUserDao(ReportLogUserDao reportLogUserDao) {
		this.reportLogUserDao = reportLogUserDao;
	}

}
