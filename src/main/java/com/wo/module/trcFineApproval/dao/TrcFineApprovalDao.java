package com.wo.module.trcFineApproval.dao;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.trcFineApproval.model.TrcFine;
import com.wo.module.trcFineApproval.vo.TrcFineSearchVo;


public interface TrcFineApprovalDao extends  GenericDAO<TrcFine, Long>, RetrieverDataPage<TrcFineSearchVo> {

}
