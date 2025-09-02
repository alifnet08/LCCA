package com.wo.module.searchAllFE.dao;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.searchAllFE.vo.SearchAllFEVO;
import com.wo.module.trcCorrespondence.model.TrcCorrespondence;

public interface SearchAllFEDao extends  GenericDAO<TrcCorrespondence, Long>, 
		RetrieverDataPage<SearchAllFEVO>{
	
}
