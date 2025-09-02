package com.wo.module.regulationMonitoring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.regulationMonitoring.dao.RegMonitoringPICFollowUpEmailTrcDAO;

@Transactional
@Service("regMonitoringPICFollowUpEmailTrcService")
public class RegMonitoringPICFollowUpEmailTrcServiceImpl implements RegMonitoringPICFollowUpEmailTrcService {
  
	@Autowired
    @Qualifier("regMonitoringPICFollowUpEmailTrcDAO")
    private RegMonitoringPICFollowUpEmailTrcDAO regMonitoringPICFollowUpEmailTrcDAO;

	public RegMonitoringPICFollowUpEmailTrcDAO getRegMonitoringPICFollowUpEmailTrcDAO() {
		return regMonitoringPICFollowUpEmailTrcDAO;
	}

	public void setRegMonitoringPICFollowUpEmailTrcDAO(
			RegMonitoringPICFollowUpEmailTrcDAO regMonitoringPICFollowUpEmailTrcDAO) {
		this.regMonitoringPICFollowUpEmailTrcDAO = regMonitoringPICFollowUpEmailTrcDAO;
	}
}
