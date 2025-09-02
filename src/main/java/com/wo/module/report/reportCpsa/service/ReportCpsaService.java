package com.wo.module.report.reportCpsa.service;

import java.util.List;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportCpsa.model.ReportCpsa;

public interface ReportCpsaService {

	@SuppressWarnings("rawtypes")
	public List<ReportCpsa> getReportCpsaByData(List<? extends SearchObject> searchCriteria);
}
