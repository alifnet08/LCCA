package com.wo.module.report.reportLogUser.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportGen.model.ReportGen;
import com.wo.module.report.reportLogUser.vo.ReportLogUserDiagramVo;
import com.wo.module.report.reportLogUser.vo.ReportLogUserVo;

public interface ReportLogUserDao extends GenericDAO<ReportGen, Long>{

	@SuppressWarnings("rawtypes")
	public List<ReportLogUserVo> getReportLogUserDetailAsVo(List<? extends SearchObject> searchCriteria);
	
	@SuppressWarnings("rawtypes")
	public List<ReportLogUserDiagramVo> getReportLogUserGrafikAsVo(List<? extends SearchObject> searchCriteria);
	
}
