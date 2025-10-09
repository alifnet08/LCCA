package com.wo.module.report.reportFine.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportFine.vo.ReportFineDetailVo;
import com.wo.module.report.reportFine.vo.ReportFineRekapVo;
import com.wo.module.report.reportGen.model.ReportGen;

public interface ReportFineDao extends GenericDAO<ReportGen, Long>{

	@SuppressWarnings("rawtypes")
	public List<ReportFineDetailVo> getReportFinaDetailAsVo(List<? extends SearchObject> searchCriteria);
	
	@SuppressWarnings("rawtypes")
	public List<ReportFineRekapVo> getReportFinaRekapAsVo(List<? extends SearchObject> searchCriteria);
	
}
