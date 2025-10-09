package com.wo.module.internalRegulationPenerbitanReport.service;

import java.util.List;

import org.primefaces.model.StreamedContent;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.internalRegulationPenerbitanReport.model.InternalRegulationPenerbitanReport;
import com.wo.module.internalRegulationPenerbitanReport.vo.InternalRegulationPenerbitanPicIrgReportVo;
import com.wo.module.internalRegulationPenerbitanReport.vo.InternalRegulationPenerbitanReportVo;

public interface InternalRegulationPenerbitanReportService
		extends RetrieverDataPage<InternalRegulationPenerbitanReportVo> {
	
	public StreamedContent generateDataExcel(InternalRegulationPenerbitanReport irgDataReport) throws Exception;

	public List<InternalRegulationPenerbitanPicIrgReportVo> getDataPicIrg();
	
}
