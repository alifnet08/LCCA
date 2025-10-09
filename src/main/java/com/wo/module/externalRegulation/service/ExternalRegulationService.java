/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.externalRegulation.service;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.externalRegulation.model.ExternalRegulation;

public interface ExternalRegulationService extends RetrieverDataPage<ExternalRegulation>  {
    
	public String getQueryTrackRecord(Long regulationId,String language);
	
}
