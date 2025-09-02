package com.wo.module.regulationMonitoring.dao;

import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.regulationMonitoring.model.RegMonitoringPICComplianceTrc;

@Repository("regMonitoringPICComplianceTrcDAO")
public class RegMonitoringPICComplianceTrcDAOImpl extends GenericDAOHibernate<RegMonitoringPICComplianceTrc, Long>
		implements RegMonitoringPICComplianceTrcDAO {

}
