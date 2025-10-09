package com.wo.module.report.reportSocializationDetail.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportGen.model.ReportGen;
import com.wo.module.report.reportSocializationDetail.vo.ReportSocializationDetailVo;

public interface ReportSocializationDetailDao extends  GenericDAO<ReportGen, Long>{

	@SuppressWarnings("rawtypes")
	public List<ReportSocializationDetailVo> getReportSocializationDetailAsVo(List<? extends SearchObject> searchCriteria);
	
	@SuppressWarnings("rawtypes")
	public List<Object[]> getReportSocializationDetailAsObj(List<? extends SearchObject> searchCriteria);
}
