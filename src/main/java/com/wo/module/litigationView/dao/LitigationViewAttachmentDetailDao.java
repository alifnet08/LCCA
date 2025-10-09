package com.wo.module.litigationView.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.litigationView.vo.LitigationViewAttachmentVo;

public interface LitigationViewAttachmentDetailDao extends GenericDAO<Object, Long>{
	
	public List<LitigationViewAttachmentVo> getLitigationAttachmentList(Long litigationId, Long attachId, String attachCode);

}
