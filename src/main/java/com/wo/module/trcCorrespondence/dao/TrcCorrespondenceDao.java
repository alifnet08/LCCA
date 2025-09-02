package com.wo.module.trcCorrespondence.dao;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.trcCorrespondence.model.TrcCorrespondence;
import com.wo.module.trcCorrespondence.vo.TrcCorrespondenceSearchVo;

public interface TrcCorrespondenceDao extends  GenericDAO<TrcCorrespondence, Long>, RetrieverDataPage<TrcCorrespondenceSearchVo> {

}
