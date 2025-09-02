/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.trcCorrespondence.service;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.trcCorrespondence.model.TrcCorrespondence;
import com.wo.module.trcCorrespondence.vo.TrcCorrespondenceSearchVo;

public interface TrcCorrespondenceService extends RetrieverDataPage<TrcCorrespondenceSearchVo> {
	public void save(TrcCorrespondence entity); 
	
	public void update(TrcCorrespondence entity);
	
	public void delete(TrcCorrespondence entity);
  
    public TrcCorrespondence findById(Long id) ;
}
