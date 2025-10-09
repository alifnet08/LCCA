/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.complianceTestingMockup.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.complianceTestingMockup.dao.ComplianceTestingPICFollowupDao;
import com.wo.module.complianceTestingMockup.model.ComplianceTestingPICFollowup;


@Transactional
@Service("complianceTestingPICFollowupService")
public class ComplianceTestingPICFollowupServiceImpl implements ComplianceTestingPICFollowupService {
    @Autowired
    @Qualifier("complianceTestingPICFollowupDao")
    private ComplianceTestingPICFollowupDao complianceTestingPICFollowupDao;
    
	public ComplianceTestingPICFollowupDao getComplianceTestingPICFollowupDao() {
		return complianceTestingPICFollowupDao;
	}

	public void setComplianceTestingPICFollowupDao(ComplianceTestingPICFollowupDao complianceTestingPICFollowupDao) {
		this.complianceTestingPICFollowupDao = complianceTestingPICFollowupDao;
	}

	public void save(ComplianceTestingPICFollowup entity) {
		complianceTestingPICFollowupDao.save(entity);
	}
	
	public void update(ComplianceTestingPICFollowup entity) {
		complianceTestingPICFollowupDao.update(entity);
	}
	
	public void delete(ComplianceTestingPICFollowup entity) {
		complianceTestingPICFollowupDao.delete(entity);
	}
  
    public ComplianceTestingPICFollowup findById(Long id) {
    	return complianceTestingPICFollowupDao.getById(id);
    }

	
    
   
        
}
