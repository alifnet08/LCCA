package com.wo.module.internalRegulationApproval.dao;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.internalRegulationApproval.model.InternalRegulationApproval;

public interface InternalRegulationApprovalDao extends  GenericDAO<InternalRegulationApproval, Long>, 
		RetrieverDataPage<InternalRegulationApproval>{
	
}
