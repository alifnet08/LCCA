package com.wo.module.externalRegulationFE.dao;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.externalRegulation.model.ExternalRegulation;
import com.wo.module.externalRegulationFE.vo.ExternalRegulationFEVO;

public interface ExternalRegulationFEDAO extends  GenericDAO<ExternalRegulation, Long>, 
		RetrieverDataPage<ExternalRegulationFEVO>{
	
}
