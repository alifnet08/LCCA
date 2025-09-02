package com.wo.module.regulationMonitoring.dao;

import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.regulationMonitoring.model.RegMonitoringRegulationTrc;

@Repository("regMonitoringRegulationTrcDAO")
public class RegMonitoringRegulationTrcDAOImpl extends GenericDAOHibernate<RegMonitoringRegulationTrc, Long>
		implements RegMonitoringRegulationTrcDAO {

}
