/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.externalRegulation.dao;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.externalRegulation.model.Regulation;
/**
 *
 * @author hendra
 */
public interface RegulationDao extends  GenericDAO<Regulation, Long>{

	public Integer getRegulationByDocNoAndDocName(String docNo,String docNameIn,String docNameEn,Long regulationId) throws Exception ;

	public String procedureUpdateTmpTrackRecord(Long internalId) throws Exception;
	
	public Integer getCheckDataRegulationSocialization(Long regulationId) throws Exception;
	
	public Regulation getCheckDataRegulation(Long regulationId, String jenisRegulation, String docNo, String nameIn) throws Exception;
	
	public Integer getCountHitRegulation(Long regulationId, String accessAction) throws Exception; 

}
