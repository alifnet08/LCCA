package com.wo.module.trcAudit.service;

import com.wo.module.complianceTestingMockup.model.ComplianceTestingPICFollowup;
import com.wo.module.trcAudit.model.TrcAuditPicFollowup;

public interface TrcAuditPICFollowupService   {
    
	public TrcAuditPicFollowup findById(Long id) ;
	
	void save(TrcAuditPicFollowup entity);
	
	void update(TrcAuditPicFollowup entity);
}
