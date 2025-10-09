/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.regulationMonitoring.dao;


import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.regulationMonitoring.model.RegMonitoringRegulationTmp;

/**
 *
 * @author hendra
 */

@Repository("regMonitoringRegulationTmpDAO")
public class RegMonitoringRegulationTmpDAOImpl extends GenericDAOHibernate<RegMonitoringRegulationTmp, Long> 
    implements RegMonitoringRegulationTmpDAO {
	
    
}
