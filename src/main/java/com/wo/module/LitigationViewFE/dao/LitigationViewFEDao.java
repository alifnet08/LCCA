package com.wo.module.LitigationViewFE.dao;

import java.util.List;

import com.wo.module.LitigationViewFE.vo.LitigationAttachmentViewFEVo;
import com.wo.module.LitigationViewFE.vo.LitigationDetailViewFEVo;
import com.wo.module.LitigationViewFE.vo.LitigationViewFEVo;
import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.litigation.model.Litigation;

public interface LitigationViewFEDao extends GenericDAO<Litigation, Long>, RetrieverDataPage<LitigationViewFEVo>{

	public List<LitigationDetailViewFEVo> getLitigationDtlListsByLitigationId(Long litigationId);
	
	public List<LitigationAttachmentViewFEVo> getLitigationAttachListsByLitigationIdAndCourtType(Long litigationId, String courtType);
	
}
