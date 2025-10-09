package com.wo.module.LitigationViewFE.service;

import java.util.List;

import com.wo.module.LitigationViewFE.vo.LitigationAttachmentViewFEVo;
import com.wo.module.LitigationViewFE.vo.LitigationDetailViewFEVo;
import com.wo.module.LitigationViewFE.vo.LitigationViewFEVo;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.litigation.model.Litigation;

public interface LitigationViewFEService extends RetrieverDataPage<LitigationViewFEVo>{

	public Litigation findById(Long id);
	
	public List<LitigationDetailViewFEVo> getLitigationDtlListsByLitigationId(Long litigationId);
	
	public List<LitigationAttachmentViewFEVo> getLitigationAttachListsByLitigationIdAndCourtType(Long litigationId, String courtType);
}
