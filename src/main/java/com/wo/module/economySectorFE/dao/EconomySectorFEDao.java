package com.wo.module.economySectorFE.dao;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.economySector.model.EconomySector;
import com.wo.module.economySectorFE.vo.EconomySectorFEVO;

public interface EconomySectorFEDao extends GenericDAO<EconomySector, Long>, RetrieverDataPage<EconomySectorFEVO>{

}
