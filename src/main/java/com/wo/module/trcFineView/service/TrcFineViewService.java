package com.wo.module.trcFineView.service;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.trcFineApproval.model.TrcFine;
import com.wo.module.trcFineView.vo.TrcFineViewSearchVO;

public interface TrcFineViewService extends RetrieverDataPage<TrcFineViewSearchVO> {
	
	public TrcFine findById(Long id);
}