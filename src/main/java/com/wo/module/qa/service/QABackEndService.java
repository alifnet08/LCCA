package com.wo.module.qa.service;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.qa.model.QA;

public interface QABackEndService extends RetrieverDataPage<QA>{

	public void update(QA qa);
	
	public QA findById(Long id) ;
	
}
