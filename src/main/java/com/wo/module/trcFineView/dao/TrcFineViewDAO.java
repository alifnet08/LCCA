package com.wo.module.trcFineView.dao;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.trcFineApproval.model.TrcFine;
import com.wo.module.trcFineView.vo.TrcFineViewSearchVO;

public interface TrcFineViewDAO extends GenericDAO<TrcFine, Long>, RetrieverDataPage<TrcFineViewSearchVO> {
	
}