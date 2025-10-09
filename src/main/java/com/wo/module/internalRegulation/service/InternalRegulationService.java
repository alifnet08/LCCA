package com.wo.module.internalRegulation.service;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.internalRegulation.model.InternalRegulation;

public interface InternalRegulationService extends RetrieverDataPage<InternalRegulation>  {
    
	public Integer getCheckDataRegulation(Long regulationId);
	
}
