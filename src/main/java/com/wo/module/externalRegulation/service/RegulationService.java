/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.externalRegulation.service;

import com.wo.module.externalRegulation.model.Regulation;

public interface RegulationService  {
    
	public void save(Regulation regulation); 
	
	public void update(Regulation regulation);
	
	public void delete(Regulation regulation);
  
    public Regulation findById(Long id) ;
    
    public Integer getRegulationByDocNoAndDocName(String docNo,String docNameIn,String docNameEn,Long regulationId) throws Exception ;

    public Integer getCheckDataRegulationSocialization(Long regulationId) throws Exception;
    
    public Regulation getCheckDataRegulation(Long regulationId, String jenisRegulation, String docNo, String nameIn) throws Exception;
    
    public void updateDataAlreadyExist(Regulation regulationNew, Regulation regulationDb, String userLogin) throws Exception;
    
    public Integer getCountHitRegulation(Long regulationId, String accessAction) throws Exception;
    
}
