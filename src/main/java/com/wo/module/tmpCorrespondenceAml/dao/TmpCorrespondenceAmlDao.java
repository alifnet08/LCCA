package com.wo.module.tmpCorrespondenceAml.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.tmpCorrespondence.model.TmpCorrespondence;
import com.wo.module.tmpCorrespondence.vo.TmpCorrespondenceSearchVo;

public interface TmpCorrespondenceAmlDao extends  GenericDAO<TmpCorrespondence, Long>, RetrieverDataPage<TmpCorrespondenceSearchVo> {

	public Boolean hasReachedMaximumReschedule(Long correspondenceId);
	
	@SuppressWarnings("rawtypes")
	public List<TmpCorrespondenceSearchVo> searchDataXLS(List<? extends SearchObject> searchCriteria);
}
