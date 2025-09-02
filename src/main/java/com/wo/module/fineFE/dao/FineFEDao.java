package com.wo.module.fineFE.dao;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.fineFE.vo.FineFEVo;
import com.wo.module.trcFineApproval.model.TrcFine;

public interface FineFEDao extends GenericDAO<TrcFine, Long>, RetrieverDataPage<FineFEVo>{

}
