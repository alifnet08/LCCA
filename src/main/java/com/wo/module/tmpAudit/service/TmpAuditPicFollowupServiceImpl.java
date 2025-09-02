package com.wo.module.tmpAudit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.tmpAudit.dao.TmpAuditPicFollowupDao;
import com.wo.module.tmpAudit.model.TmpAuditPicFollowup;

@Transactional
@Service("tmpAuditPicFollowupService")
public class TmpAuditPicFollowupServiceImpl implements TmpAuditPicFollowupService {

	@Autowired
	@Qualifier("tmpAuditPicFollowupDao")
	private TmpAuditPicFollowupDao tmpAuditPicFollowupDao;

	@Override
	public void save(TmpAuditPicFollowup entity) {
		tmpAuditPicFollowupDao.save(entity);
	}

	@Override
	public void update(TmpAuditPicFollowup entity) {
		tmpAuditPicFollowupDao.update(entity);
	}
	
	

	@Override
	public void delete(TmpAuditPicFollowup entity) {
		tmpAuditPicFollowupDao.delete(entity);
	}

	@Override
	public TmpAuditPicFollowup findById(Long id) {
		return tmpAuditPicFollowupDao.getById(id);
	}

	
}
