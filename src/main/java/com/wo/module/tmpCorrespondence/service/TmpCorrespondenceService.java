/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.tmpCorrespondence.service;

import java.util.List;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.tmpCorrespondence.model.TmpCorrespondence;
import com.wo.module.tmpCorrespondence.vo.TmpCorrespondenceSearchVo;

public interface TmpCorrespondenceService extends RetrieverDataPage<TmpCorrespondenceSearchVo> {
	public void save(TmpCorrespondence entity); 
	
	public void update(TmpCorrespondence entity);
	
	public void delete(TmpCorrespondence entity);

	public TmpCorrespondence findById(Long id) ;
    
    public Boolean hasReachedMaximumReschedule(Long correspondenceId);
    
    @SuppressWarnings("rawtypes")
	public List<TmpCorrespondenceSearchVo> searchDataXLS(List<? extends SearchObject> searchCriteria);
}
