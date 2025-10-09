/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.externalRegulation.service;

import java.util.List;

import com.wo.module.externalRegulation.model.RegulationAttachment;

public interface RegulationAttachmentService  {
    
	public void save(RegulationAttachment regulation); 
	
	public void update(RegulationAttachment regulation);
	
	public void delete(RegulationAttachment regulation);
  
    public RegulationAttachment findById(Long id) ;
    
    public List<RegulationAttachment> getRegulationAttachmentByRegulationId(Long regulationId) throws Exception;
}
