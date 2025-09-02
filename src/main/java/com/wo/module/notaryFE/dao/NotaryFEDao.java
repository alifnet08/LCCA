package com.wo.module.notaryFE.dao;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.notary.model.Notary;
import com.wo.module.notaryFE.vo.NotaryFEVo;

public interface NotaryFEDao extends GenericDAO<Notary, Long>, RetrieverDataPage<NotaryFEVo>{

}
