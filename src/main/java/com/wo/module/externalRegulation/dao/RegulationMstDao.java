/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.externalRegulation.dao;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.externalRegulation.model.RegulationMst;
/**
 *
 * @author hendra
 */
public interface RegulationMstDao extends  GenericDAO<RegulationMst, Long>{
	public Integer getCountHitRegulation(Long regulationId, String accessAction) throws Exception; 
}