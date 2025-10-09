/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.externalRegulation.dao;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.externalRegulation.model.ExternalRegulation;
/**
 *
 * @author hendra
 */
public interface ExternalRegulationDao extends  GenericDAO<ExternalRegulation, Long>, RetrieverDataPage<ExternalRegulation>{

	public String getQueryTrackRecord(Long regulationId,String language);
	
}
