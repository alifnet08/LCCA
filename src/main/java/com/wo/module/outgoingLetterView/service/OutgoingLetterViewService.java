package com.wo.module.outgoingLetterView.service;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.outgoingLetterView.model.OutgoingLetterView;

public interface OutgoingLetterViewService extends RetrieverDataPage<OutgoingLetterView>{

	public OutgoingLetterView findById(Long idLong);
}
