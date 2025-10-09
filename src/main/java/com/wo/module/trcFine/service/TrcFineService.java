/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.trcFine.service;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.trcFine.vo.TrcFineSearchVo;
import com.wo.module.trcFineApproval.model.TrcFine;

public interface TrcFineService extends RetrieverDataPage<TrcFineSearchVo> {
	public void save(TrcFine entity); 
	
	public void update(TrcFine entity);
	
	public void delete(TrcFine entity);
  
    public TrcFine findById(Long id) ;
}
