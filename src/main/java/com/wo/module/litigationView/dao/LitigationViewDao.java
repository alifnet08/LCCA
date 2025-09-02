package com.wo.module.litigationView.dao;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.litigationView.vo.LitigationViewVo;

public interface LitigationViewDao extends GenericDAO<Object, Long>, RetrieverDataPage<LitigationViewVo>{

	public LitigationViewVo getData(Long litigationId);
}
