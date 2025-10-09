/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.regulationMonitoring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.regulationMonitoring.dao.RegMonitoringPICFollowUpEmailTmpDAO;

@Transactional
@Service("regMonitoringPICFollowUpEmailTmpService")
public class RegMonitoringPICFollowUpEmailTmpServiceImpl implements RegMonitoringPICFollowUpEmailTmpService {
    @Autowired
    @Qualifier("regMonitoringPICFollowUpEmailTmpDAO")
    private RegMonitoringPICFollowUpEmailTmpDAO regMonitoringPICFollowUpEmailTmpDAO;

	public RegMonitoringPICFollowUpEmailTmpDAO getRegMonitoringPICFollowUpEmailTmpDAO() {
		return regMonitoringPICFollowUpEmailTmpDAO;
	}

	public void setRegMonitoringPICFollowUpEmailTmpDAO(
			RegMonitoringPICFollowUpEmailTmpDAO regMonitoringPICFollowUpEmailTmpDAO) {
		this.regMonitoringPICFollowUpEmailTmpDAO = regMonitoringPICFollowUpEmailTmpDAO;
	}
}
