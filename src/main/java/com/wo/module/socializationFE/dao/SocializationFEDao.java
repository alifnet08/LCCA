package com.wo.module.socializationFE.dao;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.regulationSocialization.model.SocializationTrc;
import com.wo.module.socializationFE.vo.SocializationFEVo;

public interface SocializationFEDao extends GenericDAO<SocializationTrc, Long>, RetrieverDataPage<SocializationFEVo>{

}
