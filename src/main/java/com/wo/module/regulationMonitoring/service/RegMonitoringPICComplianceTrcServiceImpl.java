package com.wo.module.regulationMonitoring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.regulationMonitoring.dao.RegMonitoringPICComplianceTrcDAO;

@Transactional
@Service("regMonitoringPICComplianceTrcService")
public class RegMonitoringPICComplianceTrcServiceImpl implements RegMonitoringPICComplianceTrcService {
   
	@Autowired
    @Qualifier("regMonitoringPICComplianceTrcDAO")
    private RegMonitoringPICComplianceTrcDAO regMonitoringPICComplianceTrcDAO;

	public RegMonitoringPICComplianceTrcDAO getRegMonitoringPICComplianceTrcDAO() {
		return regMonitoringPICComplianceTrcDAO;
	}

	public void setRegMonitoringPICComplianceTrcDAO(RegMonitoringPICComplianceTrcDAO regMonitoringPICComplianceTrcDAO) {
		this.regMonitoringPICComplianceTrcDAO = regMonitoringPICComplianceTrcDAO;
	}
}
