package com.wo.module.regulationSocializationView.dao;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.regulationSocialization.model.SocializationTrc;
import com.wo.module.regulationSocializationView.vo.RegulationSocializationViewVO;

public interface RegulationSocializationViewDao extends  GenericDAO<SocializationTrc, Long>, 
    RetrieverDataPage<RegulationSocializationViewVO>{

	
	
}
