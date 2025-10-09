package com.wo.module.tmpFine.dao;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.tmpFine.model.TmpFinePicFollowup;
import com.wo.module.tmpFine.vo.TmpFineSearchVo;

public interface TmpFinePicFollowupDao extends  GenericDAO<TmpFinePicFollowup, Long>, RetrieverDataPage<TmpFineSearchVo> {

	
}
