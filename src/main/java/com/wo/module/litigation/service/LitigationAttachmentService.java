package com.wo.module.litigation.service;

import java.util.List;

import com.wo.module.litigation.model.LitigationAttachment;

public interface LitigationAttachmentService {

	public List<LitigationAttachment> getLitigationAttachmentList(Long litigationId, Long attachId, String attachCode);
}
