/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.regulationSocialization.service;

import com.wo.module.regulationSocialization.model.SocializationTmp;

public interface SocializationTmpService   {
    
	public void save(SocializationTmp regulation); 
	
	public void update(SocializationTmp regulation);
	
	public void delete(SocializationTmp regulation);
  
    public SocializationTmp findById(Long id) ;
    
    public void updateDataAlreadyExist(SocializationTmp socializationNew, SocializationTmp socializationDb, String userLogin) throws Exception;
}
