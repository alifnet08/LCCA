package com.wo.module.tmpFaqApproval.service;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.faq.model.TmpFaq;

public interface TmpFaqApprovalService extends RetrieverDataPage<TmpFaq>{

	public void update(TmpFaq tmpFaq);
	
	public TmpFaq findById(Long id);
	
}
