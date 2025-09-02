package com.wo.module.regulatoryReportingFE.service;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.regulatoryReportingFE.vo.RegulatoryReportingFEVo;
import com.wo.module.trcRmd.model.TrcRmd;

public interface RegulatoryReportingFEService extends RetrieverDataPage<RegulatoryReportingFEVo>{

	public void save(TrcRmd entity);
	
	public void update(TrcRmd entity);
	
	public void delete(TrcRmd entity);
	
	public TrcRmd findById(Long id);
}
