/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.trcRmd.service;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.trcRmd.model.TrcRmd;
import com.wo.module.trcRmd.model.TrcRmdPicFollowup;
import com.wo.module.trcRmd.vo.TrcRmdSearchVo;

public interface TrcRmdService extends RetrieverDataPage<TrcRmdSearchVo> {
	public void save(TrcRmd entity); 
	
	public void update(TrcRmd entity);
	
	public void delete(TrcRmd entity);
  
    public TrcRmd findById(Long id) ;
    
    public TrcRmdPicFollowup getFirstPicFollowupId(Long rmdId) throws Exception;
}
