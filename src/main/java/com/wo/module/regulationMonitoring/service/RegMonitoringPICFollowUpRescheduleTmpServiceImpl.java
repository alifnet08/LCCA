/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.regulationMonitoring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.regulationMonitoring.dao.RegMonitoringPICFollowUpRescheduleTmpDAO;

@Transactional
@Service("regMonitoringPICFollowUpRescheduleTmpService")
public class RegMonitoringPICFollowUpRescheduleTmpServiceImpl implements RegMonitoringPICFollowUpRescheduleTmpService {
    @Autowired
    @Qualifier("regMonitoringPICFollowUpRescheduleTmpDAO")
    private RegMonitoringPICFollowUpRescheduleTmpDAO regMonitoringPICFollowUpRescheduleTmpDAO;

	public RegMonitoringPICFollowUpRescheduleTmpDAO getRegMonitoringPICFollowUpRescheduleTmpDAO() {
		return regMonitoringPICFollowUpRescheduleTmpDAO;
	}

	public void setRegMonitoringPICFollowUpRescheduleTmpDAO(
			RegMonitoringPICFollowUpRescheduleTmpDAO regMonitoringPICFollowUpRescheduleTmpDAO) {
		this.regMonitoringPICFollowUpRescheduleTmpDAO = regMonitoringPICFollowUpRescheduleTmpDAO;
	}
}
