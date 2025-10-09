/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.externalRegulation.service;

import com.wo.module.externalRegulation.model.RegulationMst;

public interface RegulationMstService  {
    
	public void save(RegulationMst regulation); 
	
	public void update(RegulationMst regulation);
	
	public void delete(RegulationMst regulation);
  
    public RegulationMst findById(Long id) ;
    
    public Integer getCountHitRegulation(Long regulationId, String accessAction) throws Exception;
    
}