/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.regulationMonitoring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.regulationMonitoring.dao.RegMonitoringRegulationTmpDAO;

@Transactional
@Service("regMonitoringRegulationTmpService")
public class RegMonitoringRegulationTmpServiceImpl implements RegMonitoringRegulationTmpService {
    @Autowired
    @Qualifier("regMonitoringRegulationTmpDAO")
    private RegMonitoringRegulationTmpDAO regMonitoringRegulationTmpDAO;

	public RegMonitoringRegulationTmpDAO getRegMonitoringRegulationTmpDAO() {
		return regMonitoringRegulationTmpDAO;
	}

	public void setRegMonitoringRegulationTmpDAO(RegMonitoringRegulationTmpDAO regMonitoringRegulationTmpDAO) {
		this.regMonitoringRegulationTmpDAO = regMonitoringRegulationTmpDAO;
	}
}
