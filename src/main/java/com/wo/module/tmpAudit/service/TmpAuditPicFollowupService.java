package com.wo.module.tmpAudit.service;

import com.wo.module.tmpAudit.model.TmpAuditPicFollowup;

public interface TmpAuditPicFollowupService  {

	public void save(TmpAuditPicFollowup entity);

	public void update(TmpAuditPicFollowup entity);

	public void delete(TmpAuditPicFollowup entity);

	public TmpAuditPicFollowup findById(Long id);

}
