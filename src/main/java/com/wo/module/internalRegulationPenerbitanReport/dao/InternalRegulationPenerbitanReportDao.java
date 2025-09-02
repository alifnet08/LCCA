package com.wo.module.internalRegulationPenerbitanReport.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.internalRegulationPenerbitan.model.InternalRegulationPenerbitan;
import com.wo.module.internalRegulationPenerbitanReport.model.InternalRegulationPenerbitanReport;
import com.wo.module.internalRegulationPenerbitanReport.vo.InternalRegulationPenerbitanPicIrgReportVo;
import com.wo.module.internalRegulationPenerbitanReport.vo.InternalRegulationPenerbitanReportVo;

public interface InternalRegulationPenerbitanReportDao extends  GenericDAO<InternalRegulationPenerbitan, Long>, RetrieverDataPage<InternalRegulationPenerbitanReportVo>{
	
	public List<InternalRegulationPenerbitanReportVo> getAllIrgDataHeader(InternalRegulationPenerbitanReport irgDataReport);
		
	public List<InternalRegulationPenerbitanPicIrgReportVo> getDataPicIrg();
	
}