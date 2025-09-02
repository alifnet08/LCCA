package com.wo.module.tmpFine.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.tmpFine.model.TmpFine;
import com.wo.module.tmpFine.model.TmpFineApproval;
import com.wo.module.tmpFine.vo.TmpFineSearchVo;

public interface TmpFineDao extends  GenericDAO<TmpFine, Long>, RetrieverDataPage<TmpFineSearchVo> {

	public Boolean hasReachedMaximumReschedule(Long fineId);
	
	@SuppressWarnings("rawtypes")
	public List<TmpFineSearchVo> searchDataXLS(List<? extends SearchObject> searchCriteria);
	
	public List<TmpFineApproval> getDataApprovalByFineId(Long fineId);
}
