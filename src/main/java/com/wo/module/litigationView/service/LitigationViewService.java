package com.wo.module.litigationView.service;

import java.util.List;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.litigationView.vo.LitigationViewAttachmentVo;
import com.wo.module.litigationView.vo.LitigationViewVo;

public interface LitigationViewService extends RetrieverDataPage<LitigationViewVo>{

	public LitigationViewVo getData(Long litigationId);
	
	public List<LitigationViewAttachmentVo> getLitigationAttachmentList(Long litigationId, Long attachId, String attachCode);
}
