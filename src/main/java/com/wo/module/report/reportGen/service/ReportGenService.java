package com.wo.module.report.reportGen.service;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.common.util.FileUtil;
import com.wo.module.parameter.service.ParameterDetailService;
import com.wo.module.report.reportGen.model.ReportGen;

public interface ReportGenService extends RetrieverDataPage<ReportGen> {

	public void save(ReportGen reportGen) throws Exception;

	public void update(ReportGen reportGen) throws Exception;

	public void delete(ReportGen reportGen) throws Exception;

	public void bulkDelete(ReportGen[] selectedReportGen, String userId, ParameterDetailService parameterDetailService,
			FileUtil fileUtil) throws Exception;

	public ReportGen findById(Long id);

}
