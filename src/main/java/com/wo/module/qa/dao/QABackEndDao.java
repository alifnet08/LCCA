package com.wo.module.qa.dao;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.qa.model.QA;

public interface QABackEndDao extends GenericDAO<QA, Long>, RetrieverDataPage<QA>{
	
}
