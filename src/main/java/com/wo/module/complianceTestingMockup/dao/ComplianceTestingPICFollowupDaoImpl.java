/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.complianceTestingMockup.dao;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.complianceTestingMockup.model.ComplianceTestingPICFollowup;

/**
 *
 * @author hendra
 */
@Repository("complianceTestingPICFollowupDao")
public class ComplianceTestingPICFollowupDaoImpl extends GenericDAOHibernate<ComplianceTestingPICFollowup, Long> implements ComplianceTestingPICFollowupDao {
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(ComplianceTestingPICFollowupDaoImpl.class);

	
	

}
