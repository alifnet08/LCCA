/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.regulationMonitoring.service;

import com.wo.module.regulationMonitoring.model.RegMonitoringTmp;

public interface RegMonitoringTmpService   {
    
	public void save(RegMonitoringTmp regulation); 
	
	public void update(RegMonitoringTmp regulation);
	
	public void delete(RegMonitoringTmp regulation);
  
    public RegMonitoringTmp findById(Long id) ;
    
    public void updateDataAlreadyExist(RegMonitoringTmp regMonitoringNew, RegMonitoringTmp regMonitoringDb, String userLogin) throws Exception;
}
