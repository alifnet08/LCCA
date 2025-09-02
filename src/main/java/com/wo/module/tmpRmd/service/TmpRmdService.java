/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.tmpRmd.service;

import java.util.List;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.tmpRmd.model.TmpRmd;
import com.wo.module.trcRmd.model.TrcRmdPicFollowup;

public interface TmpRmdService extends RetrieverDataPage<TmpRmd> {
	public void save(TmpRmd entity); 
	
	public void update(TmpRmd entity);
	
	public void delete(TmpRmd entity);
  
    public TmpRmd findById(Long id) ;
    
    public Boolean isDataDuplicate(TmpRmd entity);
    
    @SuppressWarnings("rawtypes")
	public List<TmpRmd> searchDataXls(List<? extends SearchObject> searchCriteria) throws Exception;
    
    public Integer getTmpRmdByNameIn(String reportNameIn, Long reportTypeId) throws Exception;
	
	public Integer getTmpRmdByIdAndNameIn(Long id, String reportNameIn, Long reportTypeId) throws Exception;
	
	public List<TrcRmdPicFollowup> getRmdPicFollowupByRmdId(Long rmdId);
}