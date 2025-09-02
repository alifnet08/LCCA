package com.wo.module.regulationMonitoring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.regulationMonitoring.dao.RegMonitoringTrcDAO;
import com.wo.module.regulationMonitoring.model.RegMonitoringTrc;

@Transactional
@Service("regMonitoringTrcService")
public class RegMonitoringTrcServiceImpl implements RegMonitoringTrcService {
  
	@Autowired
    @Qualifier("regMonitoringTrcDAO")
    private RegMonitoringTrcDAO regMonitoringTrcDAO;

	public RegMonitoringTrcDAO getRegMonitoringTrcDAO() {
		return regMonitoringTrcDAO;
	}


	public void setRegMonitoringTrcDAO(RegMonitoringTrcDAO regMonitoringTrcDAO) {
		this.regMonitoringTrcDAO = regMonitoringTrcDAO;
	}


	@Override
	public RegMonitoringTrc findById(Long id) {
		return regMonitoringTrcDAO.findById(id);
	}

}
