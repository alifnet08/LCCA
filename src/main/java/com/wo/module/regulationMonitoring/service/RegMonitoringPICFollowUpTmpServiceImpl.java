/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.regulationMonitoring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.regulationMonitoring.dao.RegMonitoringPICFollowUpTmpDAO;

@Transactional
@Service("regMonitoringPICFollowUpTmpService")
public class RegMonitoringPICFollowUpTmpServiceImpl implements RegMonitoringPICFollowUpTmpService {
    @Autowired
    @Qualifier("regMonitoringPICFollowUpTmpDAO")
    private RegMonitoringPICFollowUpTmpDAO regMonitoringPICFollowUpTmpDAO;

	public RegMonitoringPICFollowUpTmpDAO getRegMonitoringPICFollowUpTmpDAO() {
		return regMonitoringPICFollowUpTmpDAO;
	}

	public void setRegMonitoringPICFollowUpTmpDAO(RegMonitoringPICFollowUpTmpDAO regMonitoringPICFollowUpTmpDAO) {
		this.regMonitoringPICFollowUpTmpDAO = regMonitoringPICFollowUpTmpDAO;
	}    
}
