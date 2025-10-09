package com.wo.module.trcFine.dao;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;

import com.wo.module.trcFine.vo.TrcFineSearchVo;
import com.wo.module.trcFineApproval.model.TrcFine;

public interface TrcFineDao extends  GenericDAO<TrcFine, Long>, RetrieverDataPage<TrcFineSearchVo> {

}
