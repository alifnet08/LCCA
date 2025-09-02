/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.regulationMonitoring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.regulationMonitoring.dao.RegMonitoringPICComplianceTmpDAO;

@Transactional
@Service("regMonitoringPICComplianceTmpService")
public class RegMonitoringPICComplianceTmpServiceImpl implements RegMonitoringPICComplianceTmpService {
    @Autowired
    @Qualifier("regMonitoringPICComplianceTmpDAO")
    private RegMonitoringPICComplianceTmpDAO regMonitoringPICComplianceTmpDAO;

	public RegMonitoringPICComplianceTmpDAO getRegMonitoringPICComplianceTmpDAO() {
		return regMonitoringPICComplianceTmpDAO;
	}

	public void setRegMonitoringPICComplianceTmpDAO(RegMonitoringPICComplianceTmpDAO regMonitoringPICComplianceTmpDAO) {
		this.regMonitoringPICComplianceTmpDAO = regMonitoringPICComplianceTmpDAO;
	}    
}
