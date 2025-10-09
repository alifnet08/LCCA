package com.wo.module.report.reportRmdDetail.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportGen.model.ReportGen;
import com.wo.module.report.reportRmdDetail.vo.ReportRmdDetailVo;

public interface ReportRmdDetailDao extends  GenericDAO<ReportGen, Long>{

	@SuppressWarnings("rawtypes")
	public List<ReportRmdDetailVo> getReportRmdDetailAsVo(List<? extends SearchObject> searchCriteria);
	
	@SuppressWarnings("rawtypes")
	public List<Object[]> getReportRmdDetailAsObj(List<? extends SearchObject> searchCriteria);
}
