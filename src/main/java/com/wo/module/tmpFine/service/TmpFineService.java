/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.tmpFine.service;

import java.util.List;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.tmpFine.model.TmpFine;
import com.wo.module.tmpFine.model.TmpFineApproval;
import com.wo.module.tmpFine.model.TmpFinePicFollowup;
import com.wo.module.tmpFine.vo.TmpFineSearchVo;

public interface TmpFineService extends RetrieverDataPage<TmpFineSearchVo> {
	public void save(TmpFine entity); 
	
	public void update(TmpFine entity);
	
	public void delete(TmpFine entity);

	public TmpFine findById(Long id) ;
    
    public Boolean hasReachedMaximumReschedule(Long fineId);
    
    @SuppressWarnings("rawtypes")
	public List<TmpFineSearchVo> searchDataXLS(List<? extends SearchObject> searchCriteria);
    
    public List<TmpFineApproval> getDataApprovalByFineId(Long fineId);
    
    public void saveFolup(TmpFinePicFollowup entity);
    
    public void updateFolup(TmpFinePicFollowup entity);
    
    public TmpFinePicFollowup findFolupById(Long id) ;
}
