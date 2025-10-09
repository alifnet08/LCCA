package com.wo.module.litigation.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.litigation.model.LitigationAttachment;

public interface LitigationAttachmentDao extends GenericDAO<LitigationAttachment, Long>{

	public List<LitigationAttachment> getLitigationAttachmentList(Long litigationId, Long attachId, String attachCode);
}
