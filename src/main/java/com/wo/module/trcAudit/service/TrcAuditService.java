package com.wo.module.trcAudit.service;

import java.util.List;

import com.wo.module.common.model.UploadedFileWO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.trcAudit.model.TrcAudit;
import com.wo.module.trcAudit.model.TrcAuditPicFollowup;
import com.wo.module.trcAudit.vo.TrcAuditVO;
import com.wo.module.user.model.User;

public interface TrcAuditService extends RetrieverDataPage<TrcAuditVO> {

	public TrcAudit findById(Long id);

	public void processConfirm(TrcAuditPicFollowup picFollowup,
			List<UploadedFileWO> uploadFiles, List<UploadedFileWO> uploadAttachmentLetterFiles, User user) throws Exception;

}
