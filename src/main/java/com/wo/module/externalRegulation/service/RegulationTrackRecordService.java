/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.externalRegulation.service;

import java.util.List;

import com.wo.module.externalRegulation.model.RegulationTrackRecord;

public interface RegulationTrackRecordService  {
    
	public void save(RegulationTrackRecord regulation); 
	
	public void update(RegulationTrackRecord regulation);
	
	public void delete(RegulationTrackRecord regulation);
  
    public RegulationTrackRecord findById(Long id) ;
    
    public List<RegulationTrackRecord> getRegulationTrackRecordByRegulationId(Long regulationId) throws Exception;
}
