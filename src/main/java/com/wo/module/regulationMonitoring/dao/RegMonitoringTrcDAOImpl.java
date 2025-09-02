package com.wo.module.regulationMonitoring.dao;


import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.regulationMonitoring.model.RegMonitoringTrc;


@Repository("regMonitoringTrcDAO")
public class RegMonitoringTrcDAOImpl extends GenericDAOHibernate<RegMonitoringTrc, Long> 
    implements RegMonitoringTrcDAO {
	   
    
}
