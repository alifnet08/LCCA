package com.wo.module.regulationMonitoring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.regulationMonitoring.dao.RegMonitoringPICFollowUpTrcDAO;
import com.wo.module.regulationMonitoring.model.RegMonitoringPICFollowUpTrc;

@Transactional
@Service("regMonitoringPICFollowUpTrcService")
public class RegMonitoringPICFollowUpTrcServiceImpl implements RegMonitoringPICFollowUpTrcService {
    @Autowired
    @Qualifier("regMonitoringPICFollowUpTrcDAO")
    private RegMonitoringPICFollowUpTrcDAO regMonitoringPICFollowUpTrcDAO;

	public RegMonitoringPICFollowUpTrcDAO getRegMonitoringPICFollowUpTrcDAO() {
		return regMonitoringPICFollowUpTrcDAO;
	}

	public void setRegMonitoringPICFollowUpTrcDAO(RegMonitoringPICFollowUpTrcDAO regMonitoringPICFollowUpTrcDAO) {
		this.regMonitoringPICFollowUpTrcDAO = regMonitoringPICFollowUpTrcDAO;
	}

	@Override
	public RegMonitoringPICFollowUpTrc findById(Long id) {
		return regMonitoringPICFollowUpTrcDAO.findById(id);
	}
}
