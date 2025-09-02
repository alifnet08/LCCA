/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.complianceTestingMockup.service;

import com.wo.module.complianceTestingMockup.model.ComplianceTestingPICFollowup;

public interface ComplianceTestingPICFollowupService  {

	public void save(ComplianceTestingPICFollowup entity);

	public void update(ComplianceTestingPICFollowup entity);

	public void delete(ComplianceTestingPICFollowup entity);

	public ComplianceTestingPICFollowup findById(Long id);

	
	
}
