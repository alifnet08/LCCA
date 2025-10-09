package com.wo.module.report.reportIrgPenerbitan.service;

import java.util.List;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportIrgPenerbitan.model.ReportIrgPenerbitan;

public interface ReportIrgPenerbitanService {
	
	@SuppressWarnings("rawtypes")
	public List<ReportIrgPenerbitan> getAllIrgDataHeader(List<? extends SearchObject> searchCriteria);
	
}