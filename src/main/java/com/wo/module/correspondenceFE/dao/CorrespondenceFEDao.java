package com.wo.module.correspondenceFE.dao;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.correspondenceFE.vo.CorrespondenceFEVO;
import com.wo.module.trcCorrespondence.model.TrcCorrespondence;

public interface CorrespondenceFEDao extends  GenericDAO<TrcCorrespondence, Long>, 
		RetrieverDataPage<CorrespondenceFEVO>{
	
}
