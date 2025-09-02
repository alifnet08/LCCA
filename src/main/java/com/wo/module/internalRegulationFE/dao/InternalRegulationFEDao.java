package com.wo.module.internalRegulationFE.dao;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.internalRegulation.model.InternalRegulation;
import com.wo.module.internalRegulationFE.vo.InternalRegulationFEVO;

public interface InternalRegulationFEDao extends  GenericDAO<InternalRegulation, Long>, 
		RetrieverDataPage<InternalRegulationFEVO>{
	
}
