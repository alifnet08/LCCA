package com.wo.module.trcAudit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.complianceTestingMockup.model.ComplianceTestingPICFollowup;
import com.wo.module.trcAudit.dao.TrcAuditPICFollowupDao;
import com.wo.module.trcAudit.model.TrcAuditPicFollowup;

@Transactional
@Service("trcAuditPICFollowupService")
public class TrcAuditPICFollowupServiceImpl implements TrcAuditPICFollowupService {
    @Autowired
    @Qualifier("trcAuditPICFollowupDao")
    private TrcAuditPICFollowupDao trcAuditPICFollowupDao;

	public TrcAuditPICFollowupDao getTrcAuditPICFollowupDao() {
		return trcAuditPICFollowupDao;
	}

	public void setTrcAuditPICFollowupDao(TrcAuditPICFollowupDao trcAuditPICFollowupDao) {
		this.trcAuditPICFollowupDao = trcAuditPICFollowupDao;
	}

	@Override
	public TrcAuditPicFollowup findById(Long id) {
		return trcAuditPICFollowupDao.findById(id);
	}

	public void save(TrcAuditPicFollowup entity) {
		trcAuditPICFollowupDao.save(entity);
	}
	
	public void update(TrcAuditPicFollowup entity) {
		trcAuditPICFollowupDao.update(entity);
	}
	    
}
