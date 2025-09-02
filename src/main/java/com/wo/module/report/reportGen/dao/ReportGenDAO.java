package com.wo.module.report.reportGen.dao;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.report.reportGen.model.ReportGen;

public interface ReportGenDAO  extends GenericDAO<ReportGen, Long>,
		RetrieverDataPage<ReportGen> {
	
}
