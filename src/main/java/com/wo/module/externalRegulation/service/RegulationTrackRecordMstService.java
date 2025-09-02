/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.externalRegulation.service;

import java.util.List;

import com.wo.module.externalRegulation.model.RegulationTrackRecordMst;

public interface RegulationTrackRecordMstService  {
    
	public void save(RegulationTrackRecordMst regulation); 
	
	public void update(RegulationTrackRecordMst regulation);
	
	public void delete(RegulationTrackRecordMst regulation);
  
    public RegulationTrackRecordMst findById(Long id) ;
    
    public List<RegulationTrackRecordMst> getRegulationTrackRecordByRegulationId(Long regulationId) throws Exception;
	
	public List<RegulationTrackRecordMst> getRegulationTrackRecordByRegulationLinkId(Long regulationId) throws Exception;
    
}
