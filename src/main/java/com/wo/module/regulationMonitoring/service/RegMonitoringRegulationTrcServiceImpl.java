package com.wo.module.regulationMonitoring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.regulationMonitoring.dao.RegMonitoringRegulationTrcDAO;
import com.wo.module.regulationMonitoring.model.RegMonitoringRegulationTrc;

@Transactional
@Service("regMonitoringRegulationTrcService")
public class RegMonitoringRegulationTrcServiceImpl implements RegMonitoringRegulationTrcService {
    @Autowired
    @Qualifier("regMonitoringRegulationTrcDAO")
    private RegMonitoringRegulationTrcDAO regMonitoringRegulationTrcDAO;

	public RegMonitoringRegulationTrcDAO getRegMonitoringRegulationTrcDAO() {
		return regMonitoringRegulationTrcDAO;
	}

	public void setRegMonitoringRegulationTrcDAO(RegMonitoringRegulationTrcDAO regMonitoringRegulationTrcDAO) {
		this.regMonitoringRegulationTrcDAO = regMonitoringRegulationTrcDAO;
	}

	@Override
	public RegMonitoringRegulationTrc findById(Long id) {
		return regMonitoringRegulationTrcDAO.findById(id);
	}

}
