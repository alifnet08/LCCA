package com.wo.module.report.reportLogAccess.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportGen.model.ReportGen;
import com.wo.module.report.reportLogAccess.vo.ReportLogAccessVo;

public interface ReportLogAccessDao extends GenericDAO<ReportGen, Long> {

	@SuppressWarnings("rawtypes")
	public List<ReportLogAccessVo> getDataReport(List<? extends SearchObject> searchCriteria);
	
}
