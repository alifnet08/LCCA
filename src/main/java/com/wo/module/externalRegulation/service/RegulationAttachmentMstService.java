/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.externalRegulation.service;

import java.util.List;

import com.wo.module.externalRegulation.model.RegulationAttachmentMst;

public interface RegulationAttachmentMstService  {
    
	public void save(RegulationAttachmentMst regulation); 
	
	public void update(RegulationAttachmentMst regulation);
	
	public void delete(RegulationAttachmentMst regulation);
  
    public RegulationAttachmentMst findById(Long id);
    
    public List<RegulationAttachmentMst> getRegulationAttachmentByRegulationId(Long regulationId) throws Exception;
    
}
