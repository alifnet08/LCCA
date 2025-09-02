package com.wo.module.report.reportOutgoingLetterDetail.service;

import java.util.List;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportOutgoingLetterDetail.model.ReportOutgoingLetterDetail;

public interface ReportOutgoingLetterDetailService {

	@SuppressWarnings("rawtypes")
	public  List<ReportOutgoingLetterDetail> getReportOutgoingLetterDetailByAllData(List<? extends SearchObject> searchCriteria);
	
	@SuppressWarnings("rawtypes")
	public  List<Object[]> getReportOutgoingLetterDetailByAllObj(List<? extends SearchObject> searchCriteria);
}
