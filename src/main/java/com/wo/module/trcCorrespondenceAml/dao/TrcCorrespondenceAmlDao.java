package com.wo.module.trcCorrespondenceAml.dao;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.trcCorrespondence.model.TrcCorrespondence;
import com.wo.module.trcCorrespondence.vo.TrcCorrespondenceSearchVo;

public interface TrcCorrespondenceAmlDao extends GenericDAO<TrcCorrespondence, Long>, RetrieverDataPage<TrcCorrespondenceSearchVo>{

}
