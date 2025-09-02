package com.wo.module.internalRegulation.dao;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.internalRegulation.model.InternalRegulation;

public interface InternalRegulationDao extends  GenericDAO<InternalRegulation, Long>, RetrieverDataPage<InternalRegulation>{

	public Integer getCheckDataRegulation(Long regulationId);
	
}
