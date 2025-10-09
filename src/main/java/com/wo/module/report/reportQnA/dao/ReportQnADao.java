package com.wo.module.report.reportQnA.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportGen.model.ReportGen;
import com.wo.module.report.reportQnA.vo.ReportQnADetail;
import com.wo.module.report.reportQnA.vo.ReportQnARekap;

public interface ReportQnADao extends GenericDAO<ReportGen, Long>{

	@SuppressWarnings("rawtypes")
	public List<ReportQnADetail> getReportQnADetailAsVo(List<? extends SearchObject> searchCriteria);
	
	@SuppressWarnings("rawtypes")
	public List<ReportQnARekap> getReportQnARekapAsVo(List<? extends SearchObject> searchCriteria);
	
}
